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
