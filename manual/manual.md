Title:      Mobl: An Incomplete Guide
Author:     Zef Hemel
Web:        http://mobl-lang.org

Introduction
------------

When Google launched [Gmail](http://gmail.com) on April 1st, 2004,
people were amazed about the user experience. It was fast-loading and
very responsive -- miles ahead of any other web-based e-mail service
at that time. Gmail was a true competitor to desktop e-mail applications
like Outlook and Thunderbird, with the advantage of _running in the
browser_. These advantages are plentyful:

* No need to install any software other than the browser that users
  already have;
* Log-in from any computer with an internet connection;
* Seamless upgrades. Gmail is updated many times per week, without
  requiring users to explicitly upgrade their software.

Sure, there were disadvantages to web applications as well:

* No access without an internet connection;
* If you are locked out of your account, you can't access your e-mail;
* Poor integration with the operating system, e.g. web applications
  do not have access to the local file system.

However, by now, these restrictions have been partly lifted. In May
2007, Google launched [Google Gears](http://gears.google.com). A
browser plug-in that added a few capabilities to web applications,
including the ability to store data (in a database) locally, in the
user's browser, and the ability to cache the web application in the
browser. This enabled an offline version of Gmail where a bunch of
e-mail is cached locally and can be accessed without an internet
connection.

Many of the ideas from Google Gears have now been integrated in the
upcoming [HTML5](http://dev.w3.org/html5/spec/Overview.html) standard,
which is the successor to HTML 4 and XHTML 2. The focus of HTML5 is
on creating a richer user experience in the browser by introducing not
only new HTML tags, but also a large set of new Javascript APIs and
a bunch of new features in CSS (in the form of
[CSS3](http://www.w3.org/TR/css3-roadmap/)).

The new HTML5 standard enables more and more applications that used to
be desktop applications to move to "the cloud", i.e. to the browser.
There are pretty impressive word processors developed by
[Google](http://docs.google.com) and even
[Microsoft](http://docs.com), spreadsheets, book keeping, project
management tools. The trend appears to be to move to the browser.
Heavy graphical games are an exception, but do not be surprised if
even those start to appear in a few years.

But, we're not here to talk about Gmail, or Google, or web
applications in general -- we're here to talk about mobile
applications. So what does that have to do with any of this?

### A Little Mobile History

Before 2007, or even 2008, applications on mobile phones were not all
that interesting. Most mobile phones couldn't run any "apps" that did
not already pre-installed on them. The smarter phones, dubbed "smart
phones" could, but the market was hugely _fragmented_. Nokia had its
own operating system, as did Samsung, Sony-Ericsson and others. The
goal of Sun's [Java 2 Mobile
Edition](http://java.sun.com/javame/index.jsp) was to let developers
build applications once and run them on all other platforms, but due
to wildly different J2ME implementation, screen sizes etc. J2ME more
or less failed.

Internet on mobile phones was not really there either. Many phones
supported a sub-set of HTML, called
[WAP](http://en.wikipedia.org/wiki/Wireless_Application_Protocol)
(Wireless Application Protocol), but it too was very limited and did
not provide much of an experience. In addition, mobile internet was
extremely slow also contributing to the mobile internet not really
taking off.

That is -- until January 2007 when Steve Jobs took the stage on MacWorld
and introduced the iPhone. The original iPhone was a huge leap forward
in user experience and the mobile internet experience. In fact, Apple
considered the mobile internet experience to be good enough to choose
web applications as _the only_ way to develop applications for the
platform. Sadly, this approach turned out not to be very successful at
the time, for a few reasons:

1. __The state of hardware.__ The original iPhone hardware was still
too slow to provide a sufficiently swift web experience. 
2. __The state of software.__ The race to developing very fast
browsers and Javascript interpreters had not fully started yet, so the
browser software itself was still slow.
2. __The state of web standards.__ HTML5 did not exist yet (a first
draft was published early 2008). Web applications had no offline
capabilities (meaning applications had to be downloaded every time
they were invoked), no local data storage, no access to position
information.
3. __Not close enough to "the metal".__ Developers wanted to build 3D
games, applications that used other phone applications such as the
calendar, SMS and so on. There was little integration with the
operating system beyond the ability to link to a phone number.

So, when the iPhone 3G was introduced, Apple introduced the iPhone SDK
and AppStore, allowing developers to build native iPhone applications
and _sell_ them through iTunes. Needless to say, Apple has been
extremely successful with it.

The iTunes AppStore is not just pure joy, however. Anybody who
is a registered iPhone developer ($99/year) can submit, but ultimately,
Apple decides which applications to approve. If you develop an application
that Apple does not like, they may reject it. [Ask
Google](http://techcrunch.com/2009/07/27/apple-is-growing-rotten-to-the-core-and-its-likely-atts-fault/).
There have been numerous cases where Apple rejected an application for
seemingly random reasons.

Pushing updates to users is another issue. Although the iPhone has a
very nice update feature, updates _also_ have to be approved by Apple.
Consequently, pushing an update to users can take weeks.

Unsurprisingly, other companies couldn't wait to copy Apple's success,
so a number of competitors jumped in. Most notable is Android
developed by Google. Android powered devices (manufactured by numerous
manufacturers) are [outselling the iPhone by now in the
US](http://www.npd.com/press/releases/press_100510.html). Another
competitor is Palm with their WebOS devices, now acquired by
Hewlett-Packard. WebOS, as its name suggests is completely based on
web technologies and allows deep OS integration through
Javascript APIs.

If you are a mobile application developer today, you have to pick your
battles. Are you going to develop for the iPhone using Objective-C,
for Android using Java or using HTML and Javascript for Web OS?
Developing native applications for all of them is very expensive
because Apple in particular uses every measure they have to prevent
you from developing cross-platform native applications.

Still, somehow these problems seem kind of familiar. In the desktop world
we had similar issues:

* Lots of different devices;
* Lots of different operating systems;
* Difficult to deploy software updates

But ehm, hadn't we solved this problem already? Web apps, anybody?
Let's have a look at the state of the web on mobile devices today.
Essentially all "smarter" phones today are based on the open source
[Webkit](http://webkit.org) rendering engine and therefore support
many new HTML5 technologies, including:

* WebDatabases, the ability to create and use local
  [SQLite](http://sqlite.org) databases to locally store structured
  data.
* Location, the ability to find out the GPS coordinates of the phone
* Offline support, cache the application files locally so that it also
  runs when you're on AT&T in San Fransisco (i.e. you do not have an
  internet connection).
* Multi-touch, detect swipes and touches.
* Canvas, programatically draw anything you want on a canvas (for
  simple games, for instance).
* CSS3 transitions, allowing cool fading, turning and twisting effects.

These technologies are supported by

* iPhone phones with iOS version 3 and up
* iPad
* Android phones
* Palm WebOS phones

RIM will also include a proper Webkit-based browser in Blackberry OS
6, which will likely also support these technologies. 

With support from most of the manufacturers of modern mobile phones we
can conclude that developing mobile web applications are an
interesting alternative to developing native applications for each
platforms individually.

### The Mobile Web App Landscape

mobl is not the first toolkit to help you develop mobile web
applications. There are a number of other viable options including:

* [Sencha Touch](http://www.sencha.com), a very slick Javascript
  toolkit based on Ext JS to develop touch-based web applications.
* [jQTouch](http://jqtouch.com), another Javascript toolkit, this one
  based on [jQuery](http://www.jquery.com). Recently acquired by Sencha.
* [MooTouch](http://www.mootouch.net), based on the [MooTools](http://mootools.net) Javascript framework.
* [SproutCore Touch](http://touch.sproutcore.com/hedwig/), a
  Javascript framework based on SproutCore.
* [GWT Mobile Webkit](http://code.google.com/p/gwt-mobile-webkit/), a
  set of libraries for [Google Web
  Toolkit](http://code.google.com/webtoolkit/). The GWT compiler
  compiles Java to Javascript.

So why develop another, why develop mobl?

Although existing frameworks are very impressive, we identified a few problems:

1. They are all (with the exception of GWT Mobile) based on
   Javascript, which is an acceptable language, but far from perfect. It
   lacks a proper module system, for instance. It is a dynamically typed
   language which makes adding good IDE support, such as code completion,
   reference resolving and in-place error reporting very difficult.
2. Ugly user interface definition languages. Some frameworks use a
   HTML-encoding of user interfaces, filled with `div`s with class
   `button` or `datepicker`. HTML has no abstraction mechanism, so
   defining new controls with HTML is very difficult. Other frameworks,
   such as Sencha Touch uses a Javascript object encoding of user
   interfaces which is also sub-optimal.
3. Asynchronous APIs. Because Javascript has always lacked threading
   support, many APIs are implemented as asynchronous APIs. The location
   API is a good example of this. Instead of providing a `getPosition`
   function that _returns_ the location, it has one that takes a callback
   function as an argument that will be invoked once the position is
   known. This results in a very messy programming model.
4. Lack of data binding support. Most frameworks do not support data
   binding where a control is explicitly bound to a e.g. a data object
   property, resulting in changes in the controls to propagate back to
   the data object and vice versa.

In mobl we try to address these issues by taking a different approach.
Rather than building a Javascript framework we build a _domain-specific
language_ for developing mobile web applications. That is, a language
_specifically_ designed to build mobile applications, with special
keywords and language constructs for concepts such as _controls_,
_screens_ and _model entities_.

By adding a simple type system to the language we can provide a rich
IDE to go along with it, featuring in-line error highlighting, code
completion, reference resolving and, in the future, refactoring.
Due to _type inference_ type declarations are often optional,
resulting in code about as concise as dynamically typed languages with
the advantages of a statically typed one.

APIs provided in mobl are all exposed as _synchronous_ APIs, greatly
simplifying the programming model. Under the hood the synchronous APIs
are translated to asynchronous API calls to take advantage of
performance benefits.

To maintain Javascript's rapid code-save-run development
experience, mobl's compiler is invoked automatically on a module when
it is saved. The resulting Javascript can be run immediately by simply
reloading the application in the browser.

The next chapters will give a brief tutorial of mobl explaining how to
install it and build your first mobile application.
Setting up mobl
---------------

Currently mobl is solely distributed as an
[Eclipse](http://www.eclipse.org) plug-in. Eclipse is a very popular
integrated development environment with support for many different
programming languages. It is free and open source and plug-ins exist
for practically every version control system. Mobl requires Eclipse
3.5 "classic" or [3.6
"classic"](http://www.eclipse.org/downloads/packages/eclipse-classic-360/heliosr)
to run.

To make mobl run as smoothly as possible you need to make some adjustments
to the `eclipse.ini` file, which is usually found alongside the
eclipse executable. On Mac OS X there are two `eclipse.ini`
files, the one you have to adjust is stored _inside_ the `Ecipse.app`
directory. On my Mac it's located in
`/Applications/eclipse/Eclipse.app/Contents/MacOS/eclipse.ini`.

In this file, look for the line that says `-vmargs`. Before that line, make
sure it says:

    --launcher.XXMaxPermSize
    256m

and after the `-vmargs` line add the following:

    -ss512m
    -Xms200m
    -Xmx1024m

For me the entire file now looks like this (on Mac OS X Snow Leopard):

    -startup
    ../../../plugins/org.eclipse.equinox.launcher_1.0.201.R35x_v20090715.jar
    --launcher.library
    ../../../plugins/org.eclipse.equinox.launcher.cocoa.macosx_1.0.1.R35x_v20090707
    -showsplash
    org.eclipse.platform
    --launcher.XXMaxPermSize
    256m
    -vmargs
    -ss512m
    -Xms200m
    -Xmx1024m
    -Xdock:icon=../Resources/Eclipse.icns
    -XstartOnFirstThread
    -Dorg.eclipse.swt.internal.carbon.smallFonts
    
### Installing the Plug-in

After adjusting `eclipse.ini`, launch Eclipse and choose from the "Help" menu the
option "Install new software...". 

<img src="img/install1.png" width="250"/>

Then add the following update site:

    http://mobl-lang.org/update

<img src="img/install2.png" width="330"/>

If no packages appear, uncheck "Group items by category", to show
them. Then select to install `mobl editor`, choose next, accept and
finish, let it download and restart.  You can ignore the warning about
unsigned content.

<img src="img/install3.png" width="330"/>

<img src="img/install4.png" width="330"/>

### Creating Your First Project

To test if everything works OK, let's create our first mobl project.
Go to File > New > Project... 

<img src="img/file-newproject.png" width="300"/>

Then select `mobl project` and press _Next_. 

<img src="img/pick-newproject.png" width="350"/>

Fill in a project and application name, name your project and
application "firstapp" and push _Finish_.

<img src="img/newproject-wiz.png" width="350"/>

Modify the generated `firstapp.mobl` file to say the following:

    application firstapp

    import mobl::ui

    screen root() {
      header("Hello world")
    }

Then, save the file. A directory structure is now being
generated in your project's `www/` folder. To test your application
this `www/` directory needs to be accessible through a web server. If
you have a webserver running on your machine you can configure it to
serve files from this `www` directory, if you don't, you can copy the
contents of the `www/` folder to a web server someplace else. Note that
if have a [Dropbox](http://www.dropbox.com) account, you can create
a symlink from your `Public` folder, which is accessible from the
web and load your application from there.

You can test your application either on your phone, a simulator or
simply a Webkit-based browser such as Safari or Chrome, Firefox also
works if Google Gears is installed. In Safari 5 you can switch
your User Agent between "Default", "iPhone" and "iPad" to switch
between the generic and iOS-specific controlsets.

### iPhone: Turning Your Web App Into a "Proper" App

On the iPhone (or iPhone simulator) you set-up your application as a
"proper" application by tapping the "+" icon at the bottom, then
choosing "Add to Home Screen", give it a name and push _Add_. The
application will be added to the home screen and will appear
full-screen when launched, without any browser chrome (like the
address bar or bottom navigation buttons.

<img src="img/make-app-1.png" width="150"/>
<img src="img/make-app-2.png" width="150"/>
<img src="img/make-app-3.png" width="150"/>
<img src="img/make-app-4.png" width="150"/>
<img src="img/ui-hello-2.png" width="150"/>
A Brief Tutorial
----------------

Alright now that we're all set up, let's get moblin'! In this chapter
we are going to build a simple task manager.

### A data model

To start, create a new mobl project called "tasks". In the generated
`tasks.mobl` file, we will start with defining our _data model_. A
data model defines all the _entities_ or persistent object types that
we will need. In our case we will need only one: `Task`. We define it
as follows underneath the `import mobl::ui` line:

    entity Task {
      name    : String
      done    : Bool
      created : DateTime
    }

This 5-line definition tells mobl that we are going to need `Task` objects
to be persisted in the phone's local database. A `Task` object has three
properties: a textual `name` property, a `done` property that
represents a truth (a boolean value) of either `true` or `false` and
an `created` property that contains the date and time the task was created.

### A basic user interface

Next up, we are going to create an initial version of our `root`
screen, the screen that will appear when the application is launched.
Modify the definition of the `root` screen as follows:

    screen root() {
      header("Tasks")
      group {
        list(t in Task.all()) {
          item { label(t.name) }
        }
      }
    }

This definition declares the `root` screen with no arguments. The
screen consists of a number of controls. The first is a `header`
control that appears as bar along the top of the screen. The `group`
control groups together a number of `item`s. Rather than enumerating
a fixed set of group items, we use the `list` construct to loop over a
collection. The `list` construct is similar to a `foreach` loop in
other languages -- it repeats its body (between the curly braces) for
every item in a particular collection -- in this case `Task.all()`.
`Task.all()` is the collection containing all instances of `Task`
stored in the local database. If you save the `tasks.mobl` file, it
will be compiled. If you load it as explained in the previous chapter
you will likely see a screen with only the header and no actual
items. The reason for this is that, initially, the database is going
to be empty -- we need a screen to add tasks.

### An `addTask` screen

The screen to add a new task looks as follows:

    screen addTask() {
      var newTask = Task { done=false, created=now() }
      header("Add") {
        button("Done")
      }
      group {
        item { textField(newTask.name) }
      }
    }
Data
----------

Persistent data in mobl is stored locally at the device, in a
[SQLite](http://sqlite.org) database. Rather than executing SQL
queries, mobl provides its users with a more convenient, declarative
way of defining data model entities and a nice API to query those
entities.

### Defining entities

Using mobl's data modeling language you can declaratively define your
data model using `entity` declarations. 

Let's consider the following example:

    entity Task {
      name  : String
      done  : Bool
      date  : DateTime
      tags  : Collection<Tag> (inverse: tasks)
    }

    entity Tag {
      name  : String
      tasks : Collection<Task> (inverse: tags)
    }

This data model declaration declares two entities, one named `Task`
and another named `Tag`. `Task` has four properties, a `name` property
of type `String`, a `done` property of type `Bool`, a `date` property
of type `DateTime` and a collection of `Tag`s named `tags`.
The entity `Tag` has two properties: a property `name` of type
`String` and a collection of `Task`s named `tasks`. 

The `tags` and `tasks` properties both have an _annotation_ that
declares they are _inverse properties_. Inverse properties are linked
properties where a change to one affects the other. For instance, if a
`Tag` object is added to the `tags` collection of a `Task`, that same
`Task` object is also added to the `tasks` property of the `Tag`
object.

### Manipulating persistent data

In application logic is possible to create and manipulate instances of
entities. The following code creates a new `Task` object and initializes
a few properties:

    function createTask() {
      var newTask = Task{}; // create Task instance
      newTask.done = false; // set properties
      newTask.date = now();
    }

However, at this point the `newTask` object will not yet be persisted
to the database. To persist database, an object has to be _tracked_,
which can be achieved using the `add` function:

    add(newTask);

Once tracked, any changes to the objects are automatically persisted,
i.e. there is no need to `add` the object every time something
changes. Objects retrieved from the database are also automatically
tracked. __Therefore, the use of `add` is only ever needed to mark
_new_ objects to be persisted in the database.__

The previous statements, plus the marking for persistence can be more
concisely written as follows:

    function createTask() {
      add(Task{ done = false, date = now() });
    }

Whenever an entity object is marked for persistence (using an `add`
call), subsequent changes to the object are also persisted. Objects
_retrieved_ from the database are automatically marked for persistence
and changes to them are therefore also automatically stored in the
database.

### Collections of data

Collections of data are handled in mobl using the generic `Collection`
type. A `Collection` represents a (sometimes) virtual collection of entity
instances that can be filtered and sorted. The most-used collections are
the collection that contain all the instances of a particular entity:

    var allTasks = Task.all();

But you can also create collections that, for instance, only contain
all completed tasks:

    var completedTasks = Task.all()
                         .filter("done", "=", true);

Or, all completed tasks ordered by date, in descending order:

    var completedByDate = Task.all()
                          .filter("done", "=", true)
                          .order("date", ascending=false);

As could be seen in the entity definition of `Category`, its `tasks`
property is a `Collection` as well that has the same features:

    var completedInCat = category
                         .tasks
                         .filter("done", "=", true);

It is important to realize that collections are in fact virtual and only
retrieved from the database (using an efficient query) when iterated
over, e.g. using a `for`-loop. For instance in this `screen` definition:

    screen showCompletedTasks() {
      group {
        list(t in Task.all().filter("done", "=", true)) {
          item { label(t.name) }
        }
      }
    }

Although `Collection`s are virtual, they can be manipulated using its
`add` and `remove` methods:

    var newTask = Task{ name="New Task" };
    category.tasks.add(newTask);

This code will create a new `Task` object and add it to the `tasks`
collection of `category`. Note that objects added to persisted
collections are automatically `add`ed, i.e. a separate `add(newTask)`
call is not required. Adding and removing from the `Task.all()`
collection works as well, and has the same behavior as using the
`add(obj)` and `remove(obj)` calls.

    Task.all().add(task);    // same as add(task)
    Task.all().remove(task); // same as remove(task)

### Search

mobl also features simple full-text search. Textual data model
properties can be indexed by using the `(searchable)` annotation:
    
    entity Task {
      name  : String (searchable)
      done  : Bool
      date  : DateTime
      tags  : Collection<Tag> (inverse: tasks)
    }

Indexing happens automatically and transparently. To query the index,
use the `Task.search` method:

    screen searchTasks(query : String) {
      group {
        list(t in Task.search(query)) {
          item { label(t.name) }
        }
      }

    }
User interface definition
-------------------------

User interfaces in mobl are built of three things:

* __screens__, as the name suggests, these are full-screen "windows"
containing controls. Screens can return values and are
invoked from the scripting language. The `root` screen is the screen
that is loaded when the application is launched.
* __controls__, are the components that are composed to build the user
interface. Controls vary from simple labels to full-fledged
interactive maps.
* __control structures and primitives__, mobl contains a couple of
low-level primitives (such as HTML tags and string literals) and
control structures (such as `list` and `cond`) to build user
interfaces.


### Screens

Every application is required to define a `root` screen:

    screen root() {
      // content here
    }

Screens and controls both have the same basic structure, they start
with a `screen` or `control` keyword, followed by a name and signature
of the screen or control. The structure of the control or screen is
subsequently defined between curly braces. Here is the complete code of a simple "Hello
world" application:

    application hello

    import mobl

    screen root() {
      "Hello world!"
    }

When executed in the iPhone simulator, it will render the following screen:

<img src="img/ui-hello.png" width="150"/>

Not very exciting, let's use a control that is part of the imported
`mobl` module. This control is called `header` and renders, well, a
header:

    screen root() {
      header("Hello world")
    }

Now, that looks more like it!

<img src="img/ui-hello-2.png" width="150"/>

Now, let's add a group with three items:

    screen root() {
      header("Hello world")
      group {
        item { "Item 1" }
        item { "Item 2" }
        item { "Item 3" }
      }
    }

Which will look like this:

<img src="img/ui-hello-3.png" width="150"/>

### Controls

Let's build our own very simple control. A control that renders an
item with the the text "Item", followed by the number that is passed
as an argument. We define the control as follows:

    control itemNumber(n : Num) {
      item { "Item " label(n) }
    }

The `label` control (also part of the `mobl` module) simply renders
the text of its argument on the screen. You can use your brand new
control from the `root` screen (or any other screen or control, for
that matter) as follows:

    screen root() {
      header("Hello world")
      group {
        itemNumber(1)
        itemNumber(2)
        itemNumber(3)
      }
    }

When loaded, the application looks exactly the same as before, but is
slightly more concise. As you may have noticed some controls get
arguments between parentheses `(` and `)`, but others also get passed
controls as a body, between curly braces, like `group` for instance.

You can build controls yourself that allow that by using the special
`elements()` control. As an example, let's build a control that combines
a header with a group. It takes the string displayed in the header as an argument, and the items we want to put in the group as body elements:

    control headerGroup(title : String) {
      header(title)
      group {
        elements()
      }
    }

We can now use it as follows:

    screen root() {
      headerGroup("Hello world") {
        itemNumber(1)
        itemNumber(2)
        itemNumber(3)
      }
    }


### Variables

Screen and controls can also define and use local variables. In the
context of user interfaces there are two types of variables:

* Regular variables, can be read from and written to
* Derived variables, that can only be read and are derived from other variables

The following screen defines a local variable `n` that is editable
using the `inputNum` control. Note that we pass a _named arguments_ to
it, a named argument is an optional argument. In this case it will set
the `label` argument of the control, resulting in a label to be
displayed as part of the control:

    screen root() {
      header("Number")
      var n = 0
      group {
        item { inputNum(n, label="N:") }
      }
    }
    
This will look as follows:

<img src="img/ui-input-n.png" width="150"/>

We will now add a _derived_ variable using the `var variable <-
expression` syntax. We define a variable `nSquared` that will always
represent the value of `n * n`. Note that `nSquared` will be
recalculated when the value of `n` changes (e.g. when editing it using
the `inputNum` control):

    screen root() {
      header("Number")
      var n = 0
      var nSquared <- n * n
      group {
        item { inputNum(n, label="N:") }
        item { label(nSquared) }
      }
    }

When we run the application, and _as we edit_ the value of `n` in the
textbox the value of `nSquared`, which appears below it, is updated:
  
<img src="img/ui-derived.png" width="150"/>

### Lists

Sometimes you want to repeat a part of the user interface a couple of
times, for instance for every element in a list. To realize this, mobl
has the `list` construct. The following `root` screen creates a group
with an item for every number from 0 to 10 (0, 1, 2, 4, 5, 6, 7, 8, 9).
It uses the `range` function that returns a list of numbers:

    screen root() {
      header("0..10")
      group {
        list(n in range(0, 10)) {
          item { "Item " label(n) }
        }
      }
    }

Which will look as follows:

<img src="img/ui-list.png" width="150"/>

### Cond

    screen root() {
      header("Check!")
      var b = false
      group {
        item { checkbox(b, label="Check me!") }
      }
      cond(b) {
        "The checkbox is checked!"
      }
    }

Scripting
---------

Yet to write
