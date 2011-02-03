Persistent Data
===============

There is two kinds of data in mobl: _persistent_ data and _transient_
data. Persistent data is stored in `entities`, data objects that are
_tracked_ by the persistence system -- any change to them is saved
to the database. Transient data is data that is volatile, it disappears
when the application is shut down or when the variable goes out of scope.
This chapter will focus on persistent data, later chapter will talk
more about all the transient data types that mobl has to offer.

Persistent data in mobl is stored locally at the device, in a
[SQLite](http://sqlite.org) database. Rather than executing SQL
queries, mobl provides its users with a more convenient, declarative
way of defining data model entities and a nice API to query those
entities.

Defining entities
-----------------

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

Manipulating persistent data
----------------------------

In application logic is possible to create and manipulate instances of
entities. The following code creates a new `Task` object and initializes
a few properties:

    function createTask() {
      var newTask = Task(); // create Task instance
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
      add(Task(done=false, date=now()));
    }

Whenever an entity object is marked for persistence (using an `add`
call), subsequent changes to the object are also persisted. Objects
_retrieved_ from the database are automatically marked for persistence
and changes to them are therefore also automatically stored in the
database.

Collections of data
-------------------

Collections of data are handled in mobl using the generic `Collection`
type. A `Collection` represents a (sometimes) virtual collection of entity
instances that can be filtered and sorted. The most-used collections are
the collection that contain all the instances of a particular entity:

    var allTasks = Task.all();

But you can also create collections that, for instance, only contain
all completed tasks:

    var completedTasks = Task.all() where done == true;

Or, all completed tasks ordered by date, in descending order:

    var completedByDate = Task.all()
                          where done == true
                          order by date desc;

As could be seen in the entity definition of `Category`, its `tasks`
property is a `Collection` as well that has the same features:

    var completedInCat = category.tasks where done == true

It is important to realize that collections are in fact virtual and only
retrieved from the database (using an efficient query) when iterated
over, e.g. using a `for`-loop. For instance in this `screen` definition:

    screen showCompletedTasks() {
      group {
        list(t in Task.all() where done == true) {
          item { label(t.name) }
        }
      }
    }

Although `Collection`s are virtual, they can be manipulated using its
`add` and `remove` methods:

    var newTask = Task(name="New Task");
    category.tasks.add(newTask);

This code will create a new `Task` object and add it to the `tasks`
collection of `category`. Note that objects added to persisted
collections are automatically `add`ed, i.e. a separate `add(newTask)`
call is not required. Adding and removing from the `Task.all()`
collection works as well, and has the same behavior as using the
`add(obj)` and `remove(obj)` calls.

    Task.all().add(task);    // same as add(task)
    Task.all().remove(task); // same as remove(task)

Search
------

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
