Basic Types
-----

Mobl's standard library comes with a bunch of types built-in. This
chapter will quickly go through the most important ones. All these
built-in types are defined in the `mobl` library (which resides
in `stdlib/mobl.mobl` in the `mobl` repository that is imported
automatically in your applications and modules).

### void

    external type void {}

`void` is a type that represents nothing. It is mostly used as a return
type for functions and screens. If a function only has side-effects
and does not need to return anything, its return value will be `void`.
Typically you can also leave out this return type. That is:

    function doNuthing() : void {
      // Nuthin'
      return;
    }

Is the same as:

    function doNuthing() {
      // Nuthin'
      return;
    }

`void` has no values. Fun trivia fact: `void` is the only mobl type
that does not start with a capital letter. Why? Because, well, that
would just look silly.

### Object

    external type Object {
      sync function toString() : String
    }

`Object` is the root of all types in mobl. Every type is a sub-class
of `Object`. It only has a single method: `toString()` to get the string
representation of anything (not always a very useful one though).

### String

    external type String : Object {
      length : Num
      sync function charAt(index : Num) : String
      sync function charCodeAt(index : Num) : Num
      sync function indexOf(searchstring : String, start : Num = 0) : Num
      sync function lastIndexOf(searchstring : String, start : Num = 0) : Num
      sync function match(regexp : RegExp) : Array<String>
      sync function replace(regexp : RegExp, newstring : String) : String
      sync function replace(substr : String, newstring : String) : String
      sync function search(regexp : RegExp) : Num
      sync function slice(start : Num, end : Num) : String
      sync function split(separator : String, limit : Num) : Array<String>
      sync function substr(start : Num, length : Num) : String
      sync function substring(from : Num, to : Num) : String
      sync function toLowerCase() : String
      sync function toUpperCase() : String
    }

The `String` represents textual values. It can be created using the
`"Hello there"` syntax. Strings are inmutable, that is you cannot
change its value after it was created.

    var s = "Hello";
    alert(s.chatAt(0)); // => H
    alert(s.replace("e", "a")); // => Hallo
    alert(s.toUpperCase()); // => HELLO
    var world = s + " world!";
    alert(world); // => Hello world!

### Num

    external type Num : Object { }

The `Num` type represents numbers, both integer and floating number
types, it automatically switches between them as required. You can
add, multiply, subtract, divide and modulo numbers:

    var n = 10;
    alert(n + 1); // => 11
    alert(n * n); // => 100
    alert(n / 3); // => 3.3333333...
    alert(n % 2); // => 0

For more interesting mathematical manipulations, static methods of the
`Math` type can be used:

    external type Math {
      static sync function round(n : Num) : Num
      static sync function floor(n : Num) : Num
      static sync function ceil(n : Num) : Num
      static sync function abs(n : Num) : Num
      static sync function acos(n : Num) : Num
      static sync function asin(n : Num) : Num
      static sync function atan(n : Num) : Num
      static sync function atan2(n : Num) : Num
      static sync function cos(n : Num) : Num
      static sync function exp(n : Num) : Num
      static sync function log(n : Num) : Num
      static sync function pow(n1 : Num, n2 : Num) : Num
      static sync function random() : Num
      static sync function sin(n : Num) : Num
      static sync function sqrt(n : Num) : Num
      static sync function tan(n : Num) : Num
    }

For instance:

    alert(Math.pow(2, 10)); // => 1024
    alert(Math.floor(2.012222)); // => 2

### Bool

    external type Bool : Object { }

`Bool` represents a boolean value, a truth. It can be either `true` or
`false`. Boolean values can be and'ed and or'ed using `&&` and '||':

    alert(true && false); // => false
    alert(true && true); // => true
    alert(true || false); // => true

### RegExp

    external type RegExp : Object {
      global : Bool
      ignoreCase : Bool
      lastIndex : Num
      multiline : Bool
      source : String
      sync function compile(regexp : RegExp) : void
      sync function compile(regexp : RegExp, modifier : String) : void
      sync function exec(string : String) : String
      sync function test(string : String) : Bool
    }

`RegExp` is a type representing regular expressions. To create a
regular expression you use the special `/a*b/` syntax. I won't go into
the regular expression syntax here, to learn about regular
expressions, go to [wikipedia or
something](http://en.wikipedia.org/wiki/Regular_expression).

To check if a string matches the form or a US zip-code (e.g. 23232)
you could do something like this:

    if(/^[0-9]{5}$/.test(zipcode)) {
      alert("Very good!");
    }

### DateTime

    external type DateTime {
      static sync function parse(s : String) : DateTime
      static sync function fromTimestamp(timestamp : Num) : DateTime

      sync function getFullYear() : Num
      sync function getMonth() : Num

      @doc "Day of the month"
      sync function getDate() : Num

      sync function setFullYear(y : Num) : Num
      sync function setMonth(m : Num) : Num

      @doc "Day of the month"
      sync function setDate(d : Num) : Num

      sync function toString() : String
      sync function toDateString() : String

      sync function getTime() : Num
    }

The `DateTime` type represents, well, dates and times. You can create
them from a string, using `DateTime.parse("some date")`, or using the
built-in `now()` function. The `.getTime()` return the date and time
represented as Unix timestamp in ms since 1/1/1970.

### Collection

    external type Collection<T> {
      function get() : Array<T>
      function one() : T

      sync function prefetch(property : String) : Collection<T>

      sync function filter(property : String, op : String, value : Object) : Collection<T>

      sync function order(property : String, ascending : Bool) : Collection<T>

      function destroyAll() : void

      function count() : Num

      function list() : Array<T>

      sync function limit(n : Num) : Collection<T>
      sync function offset(n : Num) : Collection<T>
      sync function add(item : T) : void
      sync function remove(item : T) : void
      sync function updated() : void
    }

`Collection`s represent collections of objects, typically entities.
Collections do not have a predefined order in which items are kept.
The order in which they come out largely depends on its `order`
filter. Collections are often virtual collections; they do not contain
the items themselves, they merely represent description of the kind of
items that are in it. Only when `get()` is called (or it is iterated
over in a `for` or `list`), its items are calculated and returned
as a fixed array.

    var completed = Task.all().filter("done", "=", true);

### Array

    external type Array<T> {
      length : Num
      sync function get(n : Num) : T
      sync function push(item : T) : void
      sync function join(sep : String) : String
      function one() : T
      sync function map(fn : Function1<T, Dynamic>) : [Dynamic]
      sync function filter(fn : Function1<T, Bool>) : [T]
      sync function contains(el : T) : Bool
      sync function splice(idx : Num, numToDelete : Num) : Array<T>
      sync function insert(idx : Num, item : T) : void
      sync function remove(item : T) : void
    }

`Array`s are more concrete, real lists of objects, not necessarily
entities. They are used to quickly and efficiently manipulate arrays
of data, but are less convenient for filtering and sorting. Array types
can be written as either `Array<T>` or shorter `[T]`:

    var ar : [Num] = [1, 2, 3];
    var ar : Array<Num> = [1, 2, 3];

### Map

    external type Map<K, V> {
      sync function get(k : K) : V
      sync function set(k : K, v : V) : void
      sync function keys() : [K]
    }

`Map`s, or dictionaries, are simple key, value stores. You put some value in with a certain key, and get it out later:

    var ages = Map<String, Num>();
    ages.set("Pete", 27);
    ages.set("Lisa", 22);

    alert(ages.get("Lisa")); // => 22

### Tuple

    external type Tuple1<T1> {
      _1 : T1
    }

    external type Tuple2<T1, T2> {
      _1 : T1
      _2 : T2
    }

    external type Tuple3<T1, T2, T3> {
      _1 : T1
      _2 : T2
      _3 : T3
    }

    external type Tuple4<T1, T2, T3, T4> {
      _1 : T1
      _2 : T2
      _3 : T3
      _4 : T4
    }

`Tuple`s are very convenient when passing around multiple values that
does not really constitute a type by itself. Mobl comes with a number
of tuple types, each with a different number of items. However, typically
you would use the nicer syntax for tuple types `(T1, T2)`:

    var coords : [(Num, Num)] = [(20, 31), (10, 21)];
    for((x, y) in coords) {
      alert("X: " + x + " Y: " + y);
    }

It can also be used to quickly swap values:

    (x, y) = (y, x);

### Control

    external type Control {}
    external type Control1<T1> { }
    external type Control2<T1, T2> { }
    external type Control3<T1, T2, T3> { }
    external type Control4<T1, T2, T3, T4> { }
    external type Control5<T1, T2, T3, T4, T5> { }

### Callback, Function

    external type Callback {}
    external type Function0<RT> { }
    external type Function1<T1, RT> { }
    external type Function2<T1, T2, RT> { }
    external type Function3<T1, T2, T3, RT> { }
    external type Function4<T1, T2, T3, T4, RT> { }
    external type Function5<T1, T2, T3, T4, T5, RT> { }

### Dynamic

    external type Dynamic : Object { }
