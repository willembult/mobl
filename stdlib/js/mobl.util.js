var mobl = window.mobl || {};

// var core = window.core || {};

// core.alert = alert;

function ref(e, property) {
    return new mobl.Reference(e, property);
}

function fromScope(that, prop) {
    if(prop) {
        return $(that).scope().get(prop);
    } else {
        return $(that).scope();
    }
}

mobl.stringTomobl__Num = function (s) {
    return parseFloat(s, 10);
};

mobl.stringTomobl__String = function (s) {
    return s;
};

mobl.conditionalDef = function(oldDef, condFn, newDef) {
  return function() {
    if(condFn()) {
      return newDef.apply(null, arguments);
    } else {
      return oldDef.apply(null, arguments);
    }
  };
}

mobl.stringTomobl__DateTime = function(s) {
    return new Date(s);
};

mobl.loadingSpan = function() {
    return $("<span>Loading... <img src=\"mobl/loading.gif\"/></span>");
};

mobl.encodeUrlObj = function(obj) {
  var parts = [];
  for(var k in obj) {
    if(obj.hasOwnProperty(k)) {
      parts.push(encodeURI(k)+"="+encodeURI(obj[k]));
    }
  }
  return "?" + parts.join("&");
};

function op(operator, e1, e2, callback) {
    switch(operator) {
    case '+': callback(e1 + e2); break;
    case '-': callback(e1 - e2); break;
    case '*': callback(e1 * e2); break;
    case '/': callback(e1 / e2); break;
    case '%': callback(e1 % e2); break;
    }
}

mobl.proxyUrl = function(url, user, password) {
    if(user && password) {
        return '/proxy.php?user=' + user + '&pwd=' + password + '&proxy_url=' + encodeURIComponent(url);
    } else {
        return '/proxy.php?proxy_url=' + encodeURIComponent(url);
    }
}

mobl.remoteCollection = function(uri, datatype, processor) {
  return {
    addEventListener: function() {},
    list: function(_, callback) {
      $.ajax({
         url: mobl.proxyUrl(uri),
         datatype: datatype,
         error: function(_, message, error) {
           console.log(message);
           console.log(error);
           callback([]);
         },
         success: function(data) {
            callback(processor(data));
         }
      });
    }
  };
};

mobl.instantiate = function(sup, props) {
  var obj = {};
  for(var p in sup) {
    if(sup.hasOwnProperty(p)) {
      obj[p] = sup[p];
    }
  }
  for(var p in props) {
    if(props.hasOwnProperty(p)) {
      obj[p] = props[p];
    }
  }
  return new mobl.ObservableObject(obj);
};

mobl.ObservableObject = function(props) {
  this._data = props;
  this.subscribers = {};
  var that = this;
  for(var property in props) {
    if(props.hasOwnProperty(property)) {
      (function() {
        var p = property;
        that.__defineGetter__(p, function() {
          return that._data[p];
        });
        that.__defineSetter__(p, function(val) {
          that._data[p] = val;
          that.triggerEvent('change', that, p, val);
        });
      }());
    }
  }
};

mobl.ObservableObject.prototype = new persistence.Observable();

mobl.ObservableObject.prototype.toJSON = function() {
  var obj = {};
  for(var p in this._data) {
    if(this._data.hasOwnProperty(p)) {
      obj[p] = this._data[p];
    }
  }
  return obj;
};

function log(s) {
    console.log(s);
}

mobl.implementInterface = function(sourceModule, targetModule, items) {
  for(var i = 0; i < items.length; i++) {
    targetModule[items[i]] = sourceModule[items[i]];
  }
};


(function () {
    function Tuple() {
        for(var i = 0; i < arguments.length; i++) {
            this['_' + (i+1)] = arguments[i];
        }
        this.subscribers = {}; // Observable
        this.length = arguments.length;
    }

    Tuple.prototype = new persistence.Observable();
    Tuple.prototype.toJSON = function() {
      var obj = {};
      for(var i = 0; i < this.length; i++) {
        obj['_' + (i+1)] = this['_' + (i+1)];
      }
      return obj;
    };

    function Template(renderFn) {
        this.render = renderFn;
        this.calledTemplates = [];
        this.subscriptions = [];
    }

    Template.prototype.addSubscription = function(subId) {
        this.subscriptions.push(subId);
    };

    Template.prototype.addCalledTemplate = function(template) {
        this.calledTemplates.push(template);
    };

    function LinkedMap (parent, values) {
        this.values = values || {};
        this.parent = parent;
    }

    LinkedMap.prototype.get = function (key) {
        if (key in this.values) {
            return this.values[key];
        } else if (this.parent) {
            return this.parent.get(key);
        } else {
            return undefined;
        }
    };

    LinkedMap.prototype.set = function (key, value) {
        var current = this;
        while (!(key in current.values) && current.parent) {
            current = current.parent;
        }
        if (key in current.values) {
            current.values[key] = value;
        } else {
            this.values[key] = value;
        }
    };

    LinkedMap.prototype.setLocal = function (key, value) {
        this.values[key] = value;
    };

    LinkedMap.prototype.getRoot = function () {
        return !this.parent ? this : this.parent.getRoot();
    };

    /**
     * Represents a reference to a property
     *
     * @param ref
     *            parent ref to reference
     * @param prop
     *            property to reference, if null/undefined this reference
     *            represents a reference to a decoupled values
     * @constructor
     */
    function Reference(ref, prop) {
        this.ref = ref;
        this.prop = prop;
        this.childRefs = [];
        if(prop) {
            ref.childRefs.push(this);
        }
        this.subscribers = {}; // Observable
        var that = this;
    }

    Reference.prototype = new persistence.Observable();

    Reference.prototype.oldAddListener = Reference.prototype.addEventListener;

    Reference.prototype.addEventListener = function(eventType, callback) {
        if(eventType === 'change' && this.prop && this.ref.get().addEventListener) {
            var that = this;
            this.ref.get().addEventListener('change', function(_, _, prop, value) {
                if(prop === that.prop) {
                    callback(eventType, that, value);
                }
            });
        }
        this.oldAddListener(eventType, callback);
    };

    Reference.prototype.addSetListener = function(callback) {
        var that = this;
        if(this.ref.addEventListener) {
           this.ref.addEventListener('change', function(_, _, prop, value) {
             if(prop === that.prop) {
               callback(eventType, that, value);
             }
           });
        }
    };

    Reference.prototype.get = function() {
        if(!this.prop) {
            return this.ref;
        }
        if(this.ref.get) {
            return this.ref.get()[this.prop];
        }
    };

    Reference.prototype.set = function(value) {
        // trigger rebinding on all child refs
        if(!this.prop) {
            this.ref = value;
            this.triggerEvent('change', this, value);
        } else  {
            this.ref.get()[this.prop] = value;
        }
        for(var i = 0; i < this.childRefs.length; i++) {
            // this.childRefs[i].ref = this;
            var childRef = this.childRefs[i];
            childRef.rebind();
            childRef.triggerEvent('change', childRef, childRef.get());
        }
    };

    Reference.prototype.rebind = function() {
        var that = this;
        if(this.prop) {
            if(this.ref.get().addEventListener) {
                window.newTask2 = this.ref.get();
                // console.log("Attaching event listener to property: " +
                // this.prop)
                this.ref.get().addEventListener('change', function(_, _, prop, value) {
                    if(prop === that.prop) {
                        that.triggerEvent('change', that, value);
                    }
                });
            } else {
                //console.log("Could not rebind for: " + this.prop);
                //console.log(this.ref.get());
            }
        }
        for(var i = 0; i < this.childRefs.length; i++) {
            this.childRefs[i].rebind(value[this.childRefs[i].prop]);
        }
    };

    mobl.Tuple = Tuple;
    //mobl.List = List;
    mobl.LinkedMap = LinkedMap;
    mobl.Reference = Reference;
}());