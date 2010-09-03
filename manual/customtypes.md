Defining Your Own Types
=======================

You can create your own volatile (i.e. non-persistent) types, using
the `type` construct. At this point types can only have properties, no
methods.

Example:

    type Message {
      subject : String
      author  : String
      body    : String
    }

General syntax:

    type <TypeName> {
      <properties>
    }
