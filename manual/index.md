---
layout: docs
title: mobl manual
---

Mobl is a language to rapidly develop mobile applications. The mobl
compiler compiles mobl code to a combination of Javascript and HTML.
The generated HTML/Javascript application can be executed by modern
browsers, including
[Webkit](http://en.wikipedia.org/wiki/Webkit)-based browsers (such as
Safari on the desktop, Safari on the iPhone and iPad and the Android
browser) and Firefox (in combination with [Google
Gears](http://gears.google.com)).

While mobl uses web technologies, mobl applications should not be
thought of as mobile web sites. The programming model is very much
like "native" mobile applications like e.g. those developed using
[Xcode](http://developer.apple.com/technologies/tools/xcode.html) and
[Android's Java SDK](http://developer.android.com/sdk/index.html).
Mobl applications, like native applications can:

* look-and-feel like native applications;
* store data locally on the device (in a SQLite database);
* run when no internet connection is available (applications are
cached on the device itself) -- note that at this point this feature
is not fully supported;
* have access to (GPS) location information

The mobl language consists of a number of domain-specific languages:

* a [data modeling language](manual/data.html)
* a [user interface language](manual/ui.html)
* a [scripting language](manual/script.html)
