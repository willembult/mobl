Services
========

Mobl has a declarative way of defining interfaces to web services.
These web services allow you to perform AJAX calls to your server.
Let's see how this works with a simple example. Let's say you have
a few JSON RESTful services running on your server to pull in 
messages:

* `/messsages.json` to pull in a list of all your messages
* `/message.json?id=...` to pull in a specific message

When you perform a `GET` request on `/messages.json` you get a response, similar to this:

    [{"subject": "Hello there", "author": "Hank", "body": "How are you doing?"},
     {"subject": "Important", "author": "Hank", "body": "This is an important message"}]

Similary, the response from `/message.json?id=1` will be something like:


    {"subject": "Hello there", "author": "Hank", "body": "How are you doing?"}

In mobl you can declare these services as follows:

    type Message {
      subject : String
      author  : String
      body    : String
    }

    service MessageService {
      resource messages() : [Message] {
        uri = "/messages.json"
        method = "GET"
        encoding = "json"
      }

      resource message(id : Num) : Message {
        uri = "/message.json?id=" + id
        method = "GET"
        encoding = "json"
      }
    }

Anywhere in your code you can now make calls to this service:

    var messages : [Message] = MessageService.messages();
    group {
      list(message in messagess) {
        item { label(message.subject) }
      }
    }

Service definitions
-------------------

A service definition has the following syntax:

    service <ServiceName> {
      <service properties>
      <list of resources>
    }

Valid service properties are:

* `root`, to specify the root of the service. For instance `root =
  "http://someotherserver.com/api"`. Note that browsers do not allow
  cross-domain AJAX calls. 

Resources
---------

Every services will have one or more resources. The syntax for a
`resource` declaration is:

    resource <resourceName>(<arguments>) : <ReturnType> {
      <list of properties>
    }

You can use `String`, `Bool` or `Num` typed arguments. The values of
the arguments will be appended to the uri of the service, e.g. a
resource `message` with argument `id : Num` (value `1`) and `uri`
property of `/message.json` will result in a request to
`/message.json?id=1`.

Supported properties:

* `uri`, the URI to call, relative to the `root` property of the
  service, e.g. `root = "/questions"`
* `method`, the HTTP method to use, either `"GET"`, `"POST"` or
  "`PUT`".
* `encoding`, the encoding of the response, currently only `"json"` is
  supported.
* `mapper`, a function that maps the result from the service, to the
  desired `<ReturnType>`

