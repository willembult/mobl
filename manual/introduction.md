Introduction
============

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

A Little Mobile History
-----------------------

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

The Mobile Web App Landscape
----------------------------

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
