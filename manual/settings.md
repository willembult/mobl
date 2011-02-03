Application Settings
====================

The projects `config.mobl` file can contain additional settings that
influence the generated code of your application. Every `config.mobl`
should start with the line:

    configuration

followed by zero or more configuration settings. All settings are
optional. The settings are as follows:

    title "My Application"

The `title` setting is used as the title of your page and therefore
also the default name of the application on the home screen or in
bookmarks. Default to the filename (without `.mobl`) of your application.

    database "mydb"

The name of the database used locally to store data. Like `title`,
defaults to the application name.

    offline true

Will generate a [HTML5 cache
manifest](http://www.w3.org/TR/html5/offline.html) for your
application. This enables your web app to be cached on the device
locally so that it can be launched even if no internet connection is
available. We recommend not to enable this setting during development,
because browsers are spoty when it comes to quickly detecting changes
to cached files. _Note:_ This setting does not currently deal with web
services, so if you use anything that makes AJAX requests, don't enable
this setting yet.

    icon path/to/icon.png
    ...

    resource path/to/icon.png

Specifies an icon to use for the application, currently is only used
for the generated Chrome WebApp manifest, but will soon be used for
other icons as well. Image should be 128x128 pixels. Be sure to also
add the icon as a resource.

    version "1.0"

Specifies the version of your application.

    lib /path/to/lib/dir

Adds a directory to the search path for modules.

    stdlib /path/to/lib/dir

Uses a different path for the standard library (by default, the
plug-ins standard library is used).

A sample set of settings:

    configuration

    title "My Application"
    version "0.1"
    icon icon.png
    offline true
