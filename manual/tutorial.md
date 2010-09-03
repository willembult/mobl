A Brief Tutorial
================

Alright, now that we're all set up, let's get moblin'! In this chapter
we are going to build a simple task manager.

A Data Model
------------

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

A Basic User Interface
----------------------

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

This definition declares a `root` screen with no arguments. The
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

An `addTask` Screen
-------------------

The screen to add a new task looks as follows:

    screen addTask() {
      var newTask = Task(done=false, created=now())
      header("Add") {
        button("Done")
      }
      group {
        item { textField(newTask.name) }
      }
    }

This `addTask` screen uses a local variable called `newTask`. It
initializes that variable with a fresh `Task` object and initializes
its `done` property to `false` (the task has not yet been completed)
and `created` property to `now()`, which -- surprisingly -- sets this
property to the current time. In this screen, the header is used as a
container, it contains a button that says "Done".  A `textField`
control for the `name` property of `newTask` is wrapped inside a group
and item control.

Before we proceed, let's focus on an important concept to understand:
data binding. The following line defines a `textField` control, and
passes `newTask.name` to it. However, unlike many programming languages you
may know we do not pass the _value_ of `newTask.name` to the control;
instead we pass a _reference_ to it. A reference that enables to control
to make changes to the `name` property of `newTask`:

    textField(newTask.name)

And this is what a `textField` control will do: whenever the text in
the text field changes (upon every key press that updates the value),
the new text value of the text field will be written back to
`newTask.name`. Similarly, whenever the value `newTask.name` is updated
from elsewhere, the `textField` will immediately reflect this update.
This synchronization of data and control is what we call _data
binding_. Data binding does not only happen with form fields, but with
other controls as well. For instance, if we choose to pass a string
_variable_ to the header, rather than a string _literal_, we can change
the header on the fly. The following lines demonstrate this:

    var headerTitle = "My header";
    header(headerTitle)
    group {
      item { textField(headerTitle) }
    }

Whenever the user changes the text in the text field, the header will
immediately reflect this change.

Ok, we now have a screen that instantiates a task and allows to edit
its `name`, however, there is no way to leave from this screen and the
created task is not persisted to the database yet. To do that, we need
to add an `onclick` event to the button, as follows:

    button("Done", onclick={
      // Your code here
    })

If you know other programming languages, such as Javascript, C or
Java, you will know the `return` construct that is used to return a
result from a _function_. Mobl has `return`s for functions as well, but
it has `screen return`s too. Screens in mobl are just like functions,
they take arguments, you can call them and they can return a value. 

From the `root()` screen we will now call the `addTask()` screen, and
when `addTask()` has finished whatever it needs to do, we will `screen
return` back to `root()` (or whever it was called from).

So, in the `root()` screen, adapt the header a bit. We will add an "Add" button:

    header("Tasks") {
      button("Add", onclick={
        addTask();
      })
    }

So, whenever somebody clicks the "Add" button, the `addTask()` screen
will be invoked. However, there currently no way to return from it. We
already added the "Done" button, but it doesn't do anything yet. Let's
change that:

    screen addTask() {
      var newTask = Task(done=false, created=now())
      header("Add") {
        button("Done", onclick={
          add(newTask);
          screen return;
        })
      }
      group {
        item { textField(newTask.name) }
      }
    }

In the `onclick` event script for the "Done" button we do two things:

1. We add `newTask` to the database. This new task will now be persisted.
2. We `screen return` back to wherever we came from (in this case the
   `root` screen), yielding no value.

Ticking Off Tasks
-----------------

So, we have now created a task list where we can add tasks. But that
is not very useful. What we need, is a way to tick items off the list.
We want a little checkbox in front of every item to mark it as done.

That requires only a small change in our `root()` screen.

    list(t in Task.all()) {
      item { 
        checkBox(t.done)
        " "
        label(t.name)
      }
    }

Every item in the list is now prefixed with a `checkBox` control (for
the task's `done` property) and a space. That's it.

It would be nice to also be able to archive those items though,
wouldn't it? Let's add another button to do that:


    header("Tasks") {
      button("Archive", onclick={
        Task.all().filter("done", "=", true).destroyAll();
      })
      button("Add", onclick={
        addTask();
      })
    }

That should do it! When you push the "Archive" button, it will remove
all tasks objects in the database where `done` is set to `true`.
