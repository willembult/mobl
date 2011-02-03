Calling Web Services
====================

In previous tutorials we have built applications that don't rely on data sources other than local ones. However, a large class of application needs to retrieve data from external sources such as web services. Therefore, in this tutorial we will build a simple application I will call "Twitter Trends". It shows trending topics on [Twitter](http://twitter.com) and searches for tweets that mention them. It's simple, but it demonstrates mobl's `service` construct.

The first thing to note is that we're building _web applications_ that run inside the browser. Calling web services happens through [AJAX calls](http://en.wikipedia.org/wiki/Ajax_(programming)). Browsers restrict these calls to only the server that the code was loaded from, i.e. if I host my mobl app on `http://www.mobl-lang.org`, I can only make AJAX calls to `http://www.mobl-lang.org`. In this example we are going to call the twitter API, which runs on `http://api.twitter.com` and `http://search.twitter.com` -- not the domain our application will be hosted on.

To work around this, during development, the [fileserver.jar](http://www.mobl-lang.org/fileserver.jar) web server comes with a service proxy. It passes on any request posted to `/_proxy/...` on to external servers. For instance, when your server runs on port `8080`, a call to `http://localhost:8080/_proxy/api.twitter.com/1/trends.json` will be forwarded internally to `http://api.twitter.com/1/trends.json` and its result will be returned to the application. The code we will write during this tutorial, therefore, _will only run when using the `fileserver.jar`, or a server that mimicks this `/_proxy` behavior_.

In "real" applications you'll typically call services that you built yourself and that reside on the same domain as where your application is hosted. If not, you will need to build a similar proxying mechanism yourself.

The Application
---------------

This is what the end result will look like:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-17-at-7.26.35-PM-159x300.png" alt="" title="Screen shot 2011-01-17 at 7.26.35 PM" width="159" height="300" class="alignnone size-medium wp-image-367" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-17-at-9.33.42-PM-159x300.png" alt="" title="Screen shot 2011-01-17 at 9.33.42 PM" width="159" height="300" class="alignnone size-medium wp-image-377" />

It's a basic two-screen application. The first screen shows [Twitter](http://twitter.com) trending topics. When a topic is selected it performs a twitter search for that topic and shows results.

Defining a service interface
----------------------------

Before starting to use a web service using mobl, you first need to define its interface and define how it maps to URLs. A service is defined using the `service` construct. A service has a name, optionally a set of attributes and a number of `resource`s. A `resource` is like a method to be called. It has zero or more arguments and a return type. Unlike methods, however, its body does not contain logic. Instead it contains attributes that define how a call should be mapped to a service call. Here's an initial version of the `service` definition that we will use in this tutorial:

    service Twitter {
      resource trends() : JSON {
        uri = "/_proxy/api.twitter.com/1/trends.json"
      }

      resource search(query : String) : JSON {
        uri = "/_proxy/search.twitter.com/search.json?q="
              + escape(query)
      }
    }

It defines two `resource`s, one is `trends`, a second is `search`. `search` takes an argument, namely the query string to search for, this argument is HTML URL encoded (using `escape(...)`) and used in the `uri` attribute.

A service is called like any other method: `Twitter.trends()`

The `JSON` type is a sub-type of mobl's `Dynamic` type, a special type that enables dynamic typing of parts of your program. On objects of type `Dynamic` any method can be called and any property accessed without checking if those actually exist, very similarly to dynamic languages. This is useful in certain cases, for instance when starting to play with web services.

In your browser, open the following url: [http://api.twitter.com/1/trends.json](http://api.twitter.com/1/trends.json). What you will see is a JSON object that this service call returns. If you're not familiar with JSON, this is probably a good time [to learn more about it](http://json.org/). As you can see, the actual list of trends is an array stored under the key of `trends`.

Alright, let's define a screen that calls the `trends()` service and shows them in a list:

    screen root() {
      header("Twitter trends")
      var trendsResult = Twitter.trends()

      group {
        list(topic in trendsResult.trends) {
          item {
            label(topic.name)
          }
        }
      }
    }

The `trendsResult` (incidentally of type `JSON`) is filled with the result of the `Twitter.trends()` service call. However, as this service does not return an array of topics, but instead an object with a `trends` key whose value contain an array of topics, we have to iterate over `trendsResult.trends`. Note that this is all allowed by the mobl compiler, because `trendResult` is of type `JSON`, a sub-type of `Dynamic`. Then, for every topic we create an item in the group and show the topic name as label on the item.

There's two problems with this code:

1. It does a lot of juggling with variables of type `Dynamic` which is error prone. You don't get code completion for these things, there's no error checking, etc.
2. A call to `trends()` does not really return the data structure we'd like (an array of topics), we have to grab it from a property named `trends`.

There's another problem, but we'll deal with that one later.

To make this code more robust and reusable, let's create some types to go with this service. Note that all of this is optional, but generally, recommended.

Typing and mapping
------------------

The first thing we'll do is have a closer look at the exact structure of a `trends()` call result:

    {"trends":
      [{"url":"http:\/\/search.twitter.com\/search?q=...",
        "name":"#ihaveadream"},
       {"url":"http:\/\/search.twitter.com\/search?q=...",
        "name":"#mlkday"}
       ...
      ]
    }

Essentially it's a `trends` key with an array of objects, each with an `url` and a `name` property. Alright, let's model such a trend object:

    type Trend {
      name : String
      url  : String
    }

We have defined _entities_ before, but this is our first _type_. A _type_ is basically the same thing as an entity with one key difference: _type_ are volatile, they are not persisted to the local database. Ok, so now how do we create such `Trend` objects? Well, we don't have to, we only have to tell mobl that they already exist -- in the JSON result from the service. We define the following function:

    function trendsMapper(json : JSON) : [Trend] {
      return json.trends;
    }

This function takes a JSON object and arguments and returns an array of `Trend` objects. So, what can we pass to it? How about the result of the `Twitter.trends()` call. Sounds good? We can have this done automatically in fact, by using a `resource`'s `mapper` attribute. Redefine the `Twitter` service's `trends` resource as follows:

    resource trends() : [Trend] {
      uri = "/_proxy/api.twitter.com/1/trends.json"
      mapper = trendsMapper
    }

What we changed is the resource's return type. In addition, we defined a mapper function to use. We can now rewrite our `root` screen as follows:

    screen root() {
      header("Twitter trends")
      var trends = Twitter.trends()

      group {
        list(topic in trends) {
          item {
            label(topic.name)
          }
        }
      }
    }

A little bit cleaner, and type checked. We now get code completion and error markers for properties of `topic`.

There's one issue left -- you may have noticed it if your Internet connection isn't that fast -- the screen only appears _after_ the web service has returned its result. The `Twitter.trends()` call basically blocks the rendering of the screen. Not a good user experience. Luckily there's a fairly easy fix. We can use the special `async` construct:

    var trends = async(Twitter.trends())

The `async` construct takes one or two arguments. The first one is the expression to evaluate asynchronously, the second one is the value to temporarily assign to the value for as long as the result of the expression is not yet known. If not provided, this defaults to `null`. However, when we test our application now it will crash. The reason is that, initially, the `trends` variable is initialized to `null`. A value that the `list` has problems iterating over. To fix this, the `mobl::ui::generic` library contains the convenient `whenLoaded` control that waits until the expression passed as an argument is non-`null` and only then renders its body. We use it as follows:

    screen root() {
      header("Twitter trends")
      var trends = async(Twitter.trends())

      whenLoaded(trends) {
        group {
          list(topic in trends) {
            item {
              label(topic.name)
            }
          }
        }
      }
    }

And voila, our interface now appears instantly. And while the web service call is ongoing, an overlay appears that says "Loading..." with a progress indicator.

Searching tweets
----------------

Next we have to perform the same tricks for searching tweets. When calling the Twitter search API, we [get results like this](http://search.twitter.com/search.json?q=mobl):

    {"results":[
      {"from_user_id_str":"24186479",
       "profile_image_url":"http://..."
       "created_at":"Mon, 17 Jan 2011 20:21:56 +0000",
       "from_user":"hannahVwatson",
       "id_str":"27098314218414080",
       "metadata":{"result_type":"recent"},
       "to_user_id":null,
       "text":"RT @starbucks: Yes it is true...",
       "id":27098314218414080,
       "from_user_id":24186479,
       "geo":null,
       "iso_language_code":"en",
       "to_user_id_str":null,
       "source":"..."
      }, ...]
    }

Not all interesting stuff, but let's define a `type` for a subset of this anyway, as well as a mapper function:

    type Tweet {
      profile_image_url : String
      created_at        : String
      from_user         : String
      text              : String
    }

    function tweetsMapper(json : JSON) : [Tweet] {
      return json.results;
    }

Next, we adapt our `resource` definition:

    resource search(query : String) : [Tweet] {
      uri = "/_proxy/search.twitter.com/search.json?q="
            + escape(query)
      mapper = tweetsMapper
    }

And define a screen for it:

    screen search(query : String) {
      header(query) {
        backButton()
      }
      var results = async(Twitter.search(query))
      whenLoaded(results) {
        list(tweet in results) {
          block {
            image(tweet.profile_image_url)
            <b>label(tweet.from_user) ": "</b>
            label(tweet.text)
          }
        }
      }
    }

We call the `search` screen when a trending topic is clicked in our `root` screen:

    item(onclick={ search(topic.name); }) {
      label(topic.name)
    }

Cool. Except the search screen looks ugly. It needs some [styling](http://www.mobl-lang.org/307/styling-uis/):

    import mobl::ui::stylemixin

    style tweetStyle {
      padding: 10px;
      margin: 5px;
      min-height: 50px;
      background-color: white;
      border: 1px solid #444;
      borderRadiusMixin(10px);
    }

    style tweetIconStyle {
      float: left;
      margin: 0 10px 0 0;
    }

Let's use those styles in the `search` screen:

    screen search(query : String) {
      header(query) {
        backButton()
      }
      var results = async(Twitter.search(query))
      whenLoaded(results) {
        list(tweet in results) {
          block(tweetStyle) {
            image(tweet.profile_image_url,
                  style=tweetIconStyle)
            <b>label(tweet.from_user) ": "</b>
            label(tweet.text)
          }
        }
      }
    }

And voila. Our twitter trends application is done.

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-17-at-7.26.35-PM-159x300.png" alt="" title="Screen shot 2011-01-17 at 7.26.35 PM" width="159" height="300" class="alignnone size-medium wp-image-367" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-17-at-9.33.42-PM-159x300.png" alt="" title="Screen shot 2011-01-17 at 9.33.42 PM" width="159" height="300" class="alignnone size-medium wp-image-377" />

Conclusion
----------

`service` definitions in mobl enable concise definition of web service interfaces. Together with a mapper function and some `type` function we can construct robust web service code.
