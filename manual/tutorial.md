The Tip Calculator
==================

While researching other frameworks and languages for mobile web
development, you may have been disappointed with their tutorial
offerings. They build boring applications in them. Stuff you don't
really need. Therefore, in this first mobl tutorial we're going to
build something exciting and useful.

**A tip calculator!**

Yep, you heard that right. You may ask "are you sure I'm ready? I'm
still so young", but I say: yes. Yes, I think you are ready to enrich
the wondrous world of tip calculators. If you weren't aware, a tip
calculator is an application that given the amount you have to pay in,
for instance, a restaurant and a tip percentage, calculates the total
amount due.

I predict you will only need at most 15 lines to do it. Does that
sound exciting or what? This is what the end result will look like:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/tipcalculator-159x300.png" alt="" title="tipcalculator" width="159" height="300" class="alignnone size-medium wp-image-57" />

[And here you can see it in
action](http://hydra.nixos.org/build/850237/download/1/www/) (use any
webkit-based browser).

To speak the memorable words of [Demetri
Martin](http://en.wikipedia.org/wiki/Demetri_Martin): "Crazy awesome!"

I'll assume you will have [installed the mobl
plug-in](http://www.mobl-lang.org/get/), and created your first "Hello
world!" application. If not, please follow the instructions in that
link. I'll wait until you're done.

Done? Ok.

Create a new mobl project
---------------------------------

Give it an imaginative name. [Naming is
everything](http://www.quora.com/When-Choosing-a-Mobile-App-Name-what-are-the-two-most-important-factors).
Suggestions: "tipcalculator". That's what I called mine.

Build the application
-----------------------------

Replace the `root` screen with the following snippet of code:

    screen root() {
      var amount     =  20
      var percentage =  10

      header("Tip calculator")
      group {
        item { numField(amount, label="amount") }
        item { numField(percentage, label="percentage") }
        item { "$" label(amount * (1 + percentage/100)) }
      }
    }

What does it mean?
------------------

Woah! What does it mean? Let's start at the top.

    screen root() { ... }

This defines a screen named `root` with no arguments. A screen is
exactly what you think it is. A screen can have zero or more arguments
(this one has zero), and optionally a return type, which we will
ignore for now. Within the curly braces is the _body_ of the screen. A
screen body defines the structure of the user interface and user
interface state.

    var amount     = 20
    var percentage = 10

This code defines two screen state _variables_. These are variables
that are only accessible from within the screen. While mobl is a
_typed_ language, it is often not required to explicitly define types
of variables, because their types can be _inferred_. In fact, the
above two lines are short hand for:

    var amount     : Num = 20
    var percentage : Num = 10

So, `Num` is the mobl type that allows numeric values (both integer
and floating point, it maps directly to Javascript's numeric type).
Variables, as their names suggest can change (vary -- get it?). There
are two main ways that the value of a variable can change:

1. it is explicitly assigned a new value; or
2. it is _bound_ to a control, which, through interaction with the
user, implicitly changes the value

Don't get what that last one? No problemo, we'll get to that in a sec.

    header("Tip calculator")

This line instantiates the `header` control. One of the controls that
is part of the `mobl::ui::generic` library that we imported at the top
of the application. Controls, like screens, can have arguments. In
fact, they can have two kinds of arguments: regular arguments and body
elements. Regular arguments are passed to the control within the
parentheses `(` and `)`. Some arguments may be optional, and arguments
can be named. The first argument of the `header` control is called
`text`, and therefore, the `header` control may also be called as
follows:

    header(text="Tip calculator")

Which style you choose depends on taste and/or clarity. So, how about
the second type of arguments -- body elements?

    group {
      item { ... }
      item { ... }
      item { ... }
    }

This is another instantiation of a control, in this case of the
`group` control. Unlike the `header` control, it is not passed regular
arguments, in fact, the parentheses were left out entirely (which is
allowed). However, the `group` control does take _body elements_,
which are a set of controls in the _body_ of the control (between the
curly braces). In this case the body elements argument of the `group`
control consists of three `item` controls. The control can render its
body elements if it wishes so, or ignore them altogether.

    item { numField(amount, label="amount") }

Here we use two other controls. An `item` control, with a `numField`
control in its body. `item` controls have to be embedded within a
`group`, otherwise they will not work. The `numField` is an _input_
control. It takes two arguments, one of which is named. The `label`
argument defines a label that will appear to the left of the input
control.

The other argument, `amount` is more interesting. `amount`, as you'll
recall is one of our screen variables. By passing `amount` to the
`numField` control, we __bind its value to the control__. In the case
of an input control, this __data binding__ happens in two directions.
When the user edits the text in the input field (using the phone's
keyboard, for instance), this changed value is also automatically
assigned to the `amount` variable. Vice versa, if the `amount`
variable's value is changed, either by directly assigning to it, or by
binding it to another control, the value that appears in the input
control changes with it.

    item { numField(percentage, label="percentage") }

This line does a similar thing as the previous one, except with a
different label and binding to the `percentage` variable instead of
`amount`.

    item { "$" label(amount * (1 + percentage/100)) }

And then, the final line. The line where the magic happens. Again, the
`item` control is instantiated. It contains two controls. The first is
a literal string "$", which, well renders a dollar sign on the screen.
The second is a `label` control, which is passed an mathematical
expression as argument. A `label` control simply displays its argument
on the screen. However, like the `numField` control, it also binds its
argument to the label control. In this case this is a one-way data
binding. Whenever the value of the argument of `label` changes, the
new label control reflects that change on the screen. When does the
value of `label`'s argument change? When either `amount` or
`percentage` changes. When do those variables change? When their value
is changed through the `numField` controls.

So, what is the result of all of this? The best way to find out is by
trying it. You will see that the value of the `label` control
_recalculates_ whenever either the amount of tip percentage is
modified -- even as the user types. Cool huh?

Configuring your application
----------------------------

You will notice that your application's title (as it appears in the
browser's title bar) is derived from your application's file name. To
change this, we have to create a configuration file for our project.
Right-click on your project, pick "New" and then "File". Name your
file `config.mobl` and copy the following code into it:

    configuration

    title "Tip Calculator"

The `config.mobl` file is used to set various application
configuration options, such as `debug` for debug mode, `database` to
configure the name of the database to use. To see all configuration
settings, use Ctrl+space on an empty line in the configuration file.

Save the configuration file. Go back to your application's mobl file,
make a little change (like, put a space somewhere) and save it to
recompile it with the new configuration settings.

Conclusion
-----------

Let's take a little step back and see what we did here.

We created a new application. Every application in mobl starts with
`application <application-name>`, followed by zero or more `import`s,
which define the libraries that the application will use. An
application needs at least a single screen, named `root` without any
arguments.

For this application we created root screen with two local
_variables_. We bound those variables to two input _controls_. In
addition, we created a label that performs a calculation based on the
values of those variables that automatically recalculates whenever the
input controls change. This may seem strange at first, but hopefully
you'll quickly appreciate its power. It is in fact very similar to the
way spreadsheets like Excel work. Some cells are filled with data,
others contain formulas and show derived values. Whenever one of the
data cells change, all the cells that depend on that value change
automatically. This is called __reactive programming__ and is one of
mobl's core programming models.

Note that a screen definition is not like defining an imperative
function. It does not contain a list of statements that are to be
executed sequentially, but rather _declares_ the structure of the user
interface, that may be updated through state changes at any time.

