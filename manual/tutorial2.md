A Task Manager
==============

The previous tutorial taught the bare basics of creating user
interfaces using mobl. This next tutorial will focus on two additional
aspects of building mobl applications:

* Data modeling (using a database)
* Application logic (scripting)

To keep it simple, we'll be constructing a todo list application.
Here's what the end result will look like:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-9.41.47-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 9.41.47 AM" width="159" height="300" class="alignnone size-medium wp-image-118" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-9.43.48-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 9.43.48 AM" width="159" height="300" class="alignnone size-medium wp-image-122" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-9.44.35-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 9.44.35 AM" width="159" height="300" class="alignnone size-medium wp-image-124" />

[Click here to see it in action](http://hydra.nixos.org/build/851410/download/1/www/) (Webkit-based browser required, e.g. Safari, Chrome, iOS or Android browser).

So, let's started, shall we?

Divide and conquer
------------------

In the previous tutorial we put our entire application in a single file. That clearly won't scale. In a larger project we'd like to divide our applications into multiple _modules_. And luckily, mobl allows you to do so. In mobl there are three kinds of modules:

* __Application modules__, which start with `application <app-name>`. These are required to have a `root` screen, and in addition to being compiled to Javascript, a HTML file is also generated for application modules. One mobl project can have multiple application modules, although typically, there's only one.
* __Configuration module__, named `config.mobl` and starts with `configuration`. This module defines some application configuration, such as the application's title, database name and so on.
* __Regular modules__, which start with `module <module-name>`. These are typically imported from an application module or from other modules. Just like application modules they can define new controls, screens, functions etc.

Data model
----------

Create a new mobl project called "todo". The `todo.mobl` application module is now automatically generated. We could define our data model directly in this file, but instead, we're going to define it a dedicated data model module. Right-click your project, pick "New" and then "File". Name the file `model.mobl`. Copy the following code into it:

    module model

    entity Task {
      name        : String (searchable)
      description : String (searchable)
      done        : Bool
      date        : DateTime
    }

Every module starts with the keyword `module`, followed by the name of the module, which has to match its filename. This module contains a single definition, an `entity` definition. Entities are used to define your application's data model -- the types of objects that your application will deal with that need to be persisted to the mobile device's local database. In mobl there are two kinds of types:

* `entity` types, for which mobl handles persistency fully automatically: it creates tables in the database and it ensure that changes make to these objects are automatically saved.
* regular `type`s, which are volatile in-memory types, whose values are lost when the application is shut down.

The `model` module defines a single entity named `Task`. `Task` has four _properties_: `name`, `description`, `done` and `date`. `name` and `description` are textual properties (of type `String`). `done` keeps track of whether the task has been completed or not -- a boolean (`true` or `false`) value. `date` is used to keep track of when the task was added. Entity properties can have _annotations_, which are enumerated in-between parentheses. The `name` and `description` properties have been annotated with a `searchable` annotation, which makes them full-text searchable. We'll see how to use that ability later in this tutorial.

Save the module and switch back to your main application file (`todo.mobl`). Import the module there:

    import model

Save the application. When running the application for the first in the browser, it will create a local database on the device. It is recommended to use a webkit-based desktop browser during development. For instance, [Safari](http://www.apple.com/safari) or [Chrome](http://www.google.com/chrome). When your application is loaded you can see the database tables that have been created using your browser's developer tools. In Chrome you can access these using the "Developer", "Developer Tools" menu. In Safari using the "Develop" menu, "Show error console". Chrome and Safari's developer tools look similar and consist of a number of tabs:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/developertools1.png" alt="" title="developertools" width="600" height="181" class="alignnone size-full wp-image-132" />

The two most useful ones during mobl development are the "Storage" and "Console" tabs. "Storage" shows you your local databases, its tables and data. In this case you'll see it contains two tables, one for `Task` objects and one table for keeping the full-text search index. The "Console" tab shows you all kinds of error messages and logging information if you enabled the `debug` setting in your `config.mobl` file as follows:

    configuration

    debug
    title "Todo"

Remember to force a recompile of your main application after creating or changing your `config.mobl` file (by inserting a newline or space somewhere and saving the main application file), you'll now see all the SQL statements that mobl executes to create the database schema when your application loads.

An initial root screen
------------------------

Let's get started with an initial `root` screen:

    screen root() {
      header("Tasks")
      group {
        list(t in Task.all() order by date desc) {
          item { label(t.name) }
        }
      }
    }

A lot of this you will have seen before. There's the familiar `header` control, and the familiar `group` and `item` controls. What's new is the `list` construct. What does that do? `list` is one of mobl's control structures for user interfaces. It can be compared to a for-each loop, except that it adapts automatically to changes to the collection it iterates over. What that means will become clear later on. The syntax is:

    list(<item> in <collection-exp>) {
      ...
    }

or, if you want to be more explicit about types:

    list(<item> : <Type> in <collection-exp>) {
      ...
    }

This particular `list` iterates over the `Task.all() order by date desc` collection. So what's that? The `Task.all()` collection is a virtual collection that contains all known instances of the `Task` entity. `Task.all()` is of type `Collection<Task>`. `Collection`s represent (sometimes virtual) collections of objects that can be filtered, sorted and manipulated.

A collection's `order(prop, asc)` method, for instance, sets the sort order for a collection: `Task.all().order("date", false)` is a collection of all instances of `Task` ordered in descending order by date.

A collection's `filter(prop, op, value)` method filters a collection based on a property: `Task.all().filter("done", "=", false)` is a collection of all `Task` instances that have not yet been completed.

These can also be combined, of course: `Task.all().filter("done", "=", false).order("date", false)`. However, this chained method call syntax does not look very pretty. Therefore, mobl has a syntactic abstraction over these methods that looks more SQL-like. The latter expression can therefore be rewritten as `Task.all() where done == false order by date desc`.

When we test our new `root` screen it will be a disappointing sight. Where are all the tasks? The database is still empty, so there's not much to see.

We need a way to add new tasks.

Adding tasks
------------

Add a new screen above or below the `root` screen:

    screen addTask() {
      var newTask = Task()

      header("Add") {
        backButton()
        button("Add", onclick={
          newTask.date = now();
          add(newTask);
          screen return;
        })
      }
      group {
        item { textField(newTask.name, 
                         placeholder="Task name") }
      }
    }

Woah! Some new stuff there. First of all, yes, you can create multiple screens. Here we created a new screen named `addTask`, again without any arguments. The screen has a local variable `newTask` that is initialized with a new `Task` instance. Instantiating a mobl type can be done simply by calling the type name as a function. Optionally, we can initialize properties directly while creating the object:

    var newTask = Task(done=false, date=now())

Let's skip the `header` for now and move on to the `textField` control. This control is bound to the `name` property of the newly created `newTask` object, meaning that whenever the value of the text field is changed by the user, it automatically changes `newTask`'s `name` property as well. The `placeholder` argument sets the place holder text that is displayed within the text field when it has no value. It functions as a hint to the user as to what is supposed to be filled in there.

We now have a new `Task` object whose `name` property is bound to a text field. So when the user fills in the task name, it is assigned to the `newTask.name` property. Great. But what should the user do when he or she's done filling in the task name? That's what the "Done" button is for.

As you can see, a `header` control can have body elements as well. In fact, this one has two: a `backButton` and a `button` element. These controls will appear _inside_ the header. By convention, a `backButton` always appears at the left, and regular `button`s appear along the right of the header.

Let's focus on the "Done" button. A `button` has two important arguments: the button text and the onclick handler. In this case, the button text is "Done". The `onclick` arguments defines what should happen when the user clicks (or "taps") the button. The `onclick` argument is of type `Callback`. A callback function is a snippet of application logic that is to be executed when a certain event occurs. Application logic is encoded using mobl's scripting language.

Scripting
---------

Mobl's scripting language is syntactically similar to Javascript. It has many of Javascript's constructs, such as `var` declarations, `if`, `else`, `while` and `return` statements. However, like the rest of mobl, it is a typed language. Callbacks are typically defined in-line by enclosing scripting code within curly braces. So, let's see what happens when the "Done" button is clicked:

    newTask.date = now();
    add(newTask);
    screen return;

The first line assigns a new value to `newTask`'s `date` property. It calls the `now()` function, which returns a `DateTime` object representing, shockingly, the current time and date. The next line marks the `newTask` object for persistence. Newly instantiated entity objects are not immediately persisted, that would lead to a lot of garbage in the database. They have to be _marked for persistence_ once in their lifetime using the `add(obj)` function. From then on, mobl manages the persistence of the objects. Whenever its properties are changed, it will make sure those changes are persisted to the database. The third line may seem the strangest. You may know about `return` statements, but what is a `screen return`?

Screens in mobl are called like functions. To invoke the `addTask` screen, you can simply call `addTask()` from script. When doing so, the currently visible screen will be hidden and replaced by the called screen. The question is, how does a screen signal it's "done"? How does the user return to the previous screen? That's what `screen return` does. `screen return` says: I'm done, return to whatever screen you were at before. Optionally, a value can be returned, if a return type is defined for a screen.

While we created an `addTask` screen, there's no way to get to it yet. In order to invoke the screen, we will add an "Add" button to the header of our `root` screen. Adapt the `header` call in `root` to the following:

    header("Tasks") {
      button("Add", onclick={ addTask(); })
    }

When the user pushes the button, the `addTask` screen will appear, the user will fill in a task name and push the "Done" button. That will add the new task to the database, set the `date` property and then return the user to the `root` screen (using `screen return`).

Save your application and test it. You can now add new tasks!

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-11.31.02-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 11.31.02 AM" width="159" height="300" class="alignnone size-medium wp-image-144" />

Did you notice that newly added tasks automatically appear in the task list, without you having to do anything? Implicitly, when `add()`ing a new task, you modify the `Task.all()` collection, which triggers a re-render of the `list` in the `root` screen.

Marking tasks as done
---------------------

Although we can now add tasks, and they appear in a list, we cannot yet mark them as done. To enable that, replace the `item` control within the `list` in the `root` screen with the following:

    item { checkBox(t.done, label=t.name) }

This uses the `checkBox` control (which renders, well, a checkbox) and binds its value to `t.done`. Whenever the user taps the checkbox and changes its value, the `t.done` property is automatically changed as well (and the change is propagated to the database as well). The `label` argument adds a label to the checkbox. An alternative solution could have been:

    item {
      checkBox(t.done)
      label(t.name)
    }

The only difference is that in the former solution the label can be clicked to toggle the checkbox, in the latter solution the user has to aim a little bit better and click the checkbox itself.

Save your application and run it:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-11.43.11-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 11.43.11 AM" width="159" height="300" class="alignnone size-medium wp-image-147" />

Editing and deleting tasks
--------------------------

We can now add, and mark tasks as done. Now let's also enable the user to edit and delete tasks.

First define an `editTask` screen:

    screen editTask(t : Task) {
      header("Edit") {
        button("Done", onclick={ screen return; })
      }
      group {
        item { textField(t.name, 
                         placeholder="Task name") }
        item { textField(t.description, 
                         placeholder="Task description") }
      }
    }

Yep. The first screen with an argument -- the task to be edited, to be precise. For the rest, there's not much new. The screen defines two text fields, one for the task name, the other for its description. The "Done" button doesn't do anything other than returning the user to the previous screen. Note an `add(obj)` call is not required, because the task is already in the database. There's also no explicit save call, changes are persisted to the database as the user edits the text fields.

In order to expose the edit and remove features, we will create a context menu for every task in our `root` screen. Again, adapt the `item` control call in your `root` screen:

    item {
      checkBox(t.done, label=t.name)
      contextMenu {
        button("Delete", onclick={
          remove(t);
        })
        button("Edit", onclick={
          editTask(t);
        })
      }
    }

We now added a `contextMenu` control. In its body, we add two buttons that will become visible when we push the context menu's icon. The first `button` is is for deleting tasks. It simply calls `remove(t)` when clicked. `remove(obj)` removes that object from the database, as expected. The "Edit" button invoked the `editTask` screen, as expected.

Save your application and run it, you can now remove and edit tasks!

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-11.53.13-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 11.53.13 AM" width="159" height="300" class="alignnone size-medium wp-image-151" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-11.54.09-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 11.54.09 AM" width="159" height="300" class="alignnone size-medium wp-image-153" />

Googling it up
--------------

If you're a very busy person, the amount of tasks may overwhelm you. Wouldn't it be nice to be able to search tasks as well? That would also make adding those `searchable` annotations in our data model more useful.

Before we start, we'll first do some refactoring of our current code. In our search screen, we'll also want to display task items, with the same checkbox and edit/delete buttons. Can't we reuse that code somehow?

We can do that by creating our own _control_. We'll call this control `taskItem` and we define it as follows:

    control taskItem(t : Task) {
      checkBox(t.done, label=t.name)
      contextMenu {
        button("Delete", onclick={
          remove(t);
        })
        button("Edit", onclick={
          editTask(t);
        })
      }
    }

As you can see, a control is defined very similar to screens. Easy huh? So, let's use it. Let's once again (last time, I promise) change the `item` control in our `root` screen, in fact, let's replace it, turning the `list` construct there into:

    list(t in Task.all() order by date desc) {
      item { taskItem(t) }
    }

We replaced that whole `item` thing with a single `taskItem` control call. Cool huh? Cleaner code with exactly the same behavior.

Time to add search. Define the following new screen:

    screen search() {
      var phrase = ""

      header("Search") { backButton() }
      searchBox(phrase, placeholder="Search term")
      group {
        list(t in Task.searchPrefix(phrase)) {
          item { taskItem(t) }
        }
      }
    }

Once again, we use a local screen variable. It's the variable that will keep the search phrase. We use the `searchBox` control to render a search input box. A `searchBox` is basically the same as a `textField`, except with different styling. It binds the control to the `phrase` variable.

The `list` construct iterates over the `Task.searchPrefix(phrase)` collection. This is a special collection that performs a full-text search on all properties that have been marked `searchable`. Because the collection depends on the value of the `phrase` variable, the list of updates automatically updates as the user types in his or her search query. We reuse our `taskItem` control here to render the task.

Add a button to the search screen at the bottom of the `root` screen:

    button("Search", onclick={ search(); })

Save your application and run it. We're done!

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-9.41.47-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 9.41.47 AM" width="159" height="300" class="alignnone size-medium wp-image-118" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-07-at-9.44.35-AM-159x300.png" alt="" title="Screen shot 2011-01-07 at 9.44.35 AM" width="159" height="300" class="alignnone size-medium wp-image-124" />

In barely 80 lines of code we built a pretty functional todo list applications that supports adding, editing, removing and searching tasks. We defined a simple data model, a custom control and a number of screens. We defined some event callbacks to navigate between screens and set properties.

What about MVC?
-------------------

While mobl supports the [Model-View-Controller pattern](http://en.wikipedia.org/wiki/Model%E2%80%93View%E2%80%93Controller), it does not _enforce_ it. The model (entities) of the application can be defined separately from the rest of the application. However, views (controls and screens) and controllers (script callbacks) are mixed. In the todo application we built, the amount of controller logic is so small that it would a waste to create a whole separate controller to implement it. However, if desired, it is possible to move more of the logic to functions. For instance, the following snippet of code:

    button("Add", onclick={
      newTask.date = now();
      add(newTask);
      screen return;
    })

Could be refactored to:

    button("Add", onclick={
      createTask(newTask);
      screen return;
    })

    function createTask(t : Task) {
      t.date = now();
      add(t);
    }

The `function` could even be moved to a separate module. But you don't have to. Mobl lets you organize the code the way you like. It is good practice to move complex logic into functions in a separate module, but it is typically much more productive to write simple one or two-liners inline in the control or screen itself.

Higher-Order Controls
---------------------

Now, we'll continue improving the application by using a few _higher-level controls_.

__A higher-level control is a control that takes other controls as arguments.__

Say what?

We're going to add new functionality to our task manager, while _reducing_ the amount of lines of code. How does that sound? Here's screenshots of the end result, both on iPhone and iPad:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.45.48-AM-159x300.png" alt="" title="Screen shot 2011-01-13 at 11.45.48 AM" width="159" height="300" class="alignnone size-medium wp-image-249" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-1.37.18-PM-159x300.png" alt="" title="Screen shot 2011-01-13 at 1.37.18 PM" width="159" height="300" class="alignnone size-medium wp-image-259" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.58.12-AM-159x300.png" alt="" title="Screen shot 2011-01-13 at 11.58.12 AM" width="159" height="300" class="alignnone size-medium wp-image-247" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.59.44-AM-300x232.png" alt="" title="Screen shot 2011-01-13 at 11.59.44 AM" width="300" height="232" class="alignnone size-medium wp-image-246" />

The last screenshots shows the same screen as the first one. However, as you can see, it automatically adapts to the fact that the screen is wider. The iPad version shows the list of tasks to the left and details immediately at the right, whereas with the narrow (iPhone) version, details appear on a separate screen when the task is selected.

Tabs
----

The original version of our todo list application had a single screen of tasks, and a button to move to a separate "Search" screen. Kind of silly, isn't it? Wouldn't it be nicer to have a tabbed screen, one showing all tasks, and a second that had the search functionality? (One could argue it would be nice to have everything integrated, but for the purpose of this exercise, let's not.)

In order to do that, we have to make a few changes. First, we're going to rename our `root` screen, and turn it into a control. So, we change our `root` screen:

    screen root() {
      ...
    }

Into:

    control tasks() {
      ...
    }

Now we'll do the same thing to the `search` screen, we'll turn this screen into a control:

    screen search() {
      header("Search") {
        backButton()
      }
      var phrase = ""
      ...
    }

Becomes:

    control search() {
      header("Search")
      var phrase = ""
      ...
    }

We changed `screen` into `control` and removed the `backButton`, because we won't use it anymore. The IDE will now complain about the missing `root` screen, so let's define a new one:

    screen root() {
      tabSet([("Tasks", "", tasks),
              ("Search", "", search)])
    }

So, what does that do? It uses the `tabSet` control to build a tab set. The `tabSet` control takes a single argument: an array of tuples. A tuple could be described as an array with a fixed length. Mobl's syntax to create arrays, as you can tell, is `[item1, item2]` and its syntax to create tuples is `(item1, item2)`. Each tuple in this array represents a single tab:

* The first element of the tuple is the tab's title
* The second an URL to an icon (not used, at the moment)
* The third is the control to use as the body of the tab.

Yes, you can pass a control as an argument to another control. Controls that use this functionality are called _higher-order controls_. So, what happens after making these minor changes? Go look for yourself.

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.45.48-AM-159x300.png" alt="" title="Screen shot 2011-01-13 at 11.45.48 AM" width="159" height="300" class="alignnone size-medium wp-image-249" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-1.37.18-PM-159x300.png" alt="" title="Screen shot 2011-01-13 at 1.37.18 PM" width="159" height="300" class="alignnone size-medium wp-image-259" />


A nicely tabbed interface. That wasn't so hard. Let's see if we can use some more of these higher-order controls.

Task details
------------

Let's define a control that gives some details about a single task:

    control taskDetail(t : Task) {
      group {
        item { label(t.name) }
        when(t.description) {
          item { label(t.description) }
        }
        item { label(t.date.toDateString()) } 
        item {
          label(t.done ? "This task has been performed"
                       : "This task hasn't been performed")
        }
      }
      button("Edit", onclick={ editTask(t); })
    }

This control uses two mobl language features we haven't seen before:

* The `when` construct, which conditionally shows its body elements (somewhat like an if-statement). Yes, it does have an optional `else` clause.
* The [ternary `e1 ? e2 : e3` operator](http://en.wikipedia.org/wiki/%3F:), which is like an if-expression. If `e1` is true, the return `e2`, else return `e3`.

In this particular application, such a detail control is not extremely useful, but typically applications do often have more information to display than fits a list view.

We're going to use this `taskDetail` control in combination with the `masterDetail` control. [Wikipedia defines the master-detail user interface pattern as follows](http://en.wikipedia.org/wiki/Master-detail):

> In computer user interface design, a master-detail page is one where a master area and its related detail area are represented on the same page. The content of the detail area is displayed based on the current record selection in the master area.

This UI pattern works great on larger screens (say, tablets), but not so much on smaller screens (say, phones). Therefore, the `mobl::ui::generic` library contains two implementations of `masterDetail`:

1. One for screen widths <= 500 pixels. This version initially only renders a list view. Then, when the user selects an item from the list, it shows its details view on a separate screen (with a "Back" button to return). 
2. One for screen widths > 500 pixels. This version shows the list of items along the left side of the screen, and the details of the currently selected item directly to the right.

The "right" version of the controls is picked at run-time, automatically. Let's see how we use it. Replace the `tasks` control with the following implementation:

    control tasks() {
      header("Tasks") {
        button("Add", onclick={ addTask(); })
      }
      masterDetail(Task.all() order by date desc,
                   taskItem, taskDetail)
    }

Compared to the previous version, we now removed the `group` control that was there before with the `masterDetail` control. As you can see, `masterDetail` takes three arguments:

1. A collection of items to show the master-detail view for.
2. A control taking a single argument (an item from the collection) to use in the _list view_.
3. A control taking a single argument (again, an item from the collection) to use in the _detail view_.

The result looks as follows on phones:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.45.48-AM-159x300.png" alt="" title="Screen shot 2011-01-13 at 11.45.48 AM" width="159" height="300" class="alignnone size-medium wp-image-249" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.58.12-AM-159x300.png" alt="" title="Screen shot 2011-01-13 at 11.58.12 AM" width="159" height="300" class="alignnone size-medium wp-image-247" />

And as follows on tablets:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-11.59.44-AM-300x232.png" alt="" title="Screen shot 2011-01-13 at 11.59.44 AM" width="300" height="232" class="alignnone size-medium wp-image-246" />

Pretty nice huh, those higher-level controls? One more? Alright, because you insist.

Searching
---------

Replace your current `search` control with the following:

    control search() {
      header("Search")
      searchList(Task, taskItem, taskDetail,
                                 resultLimit=10)
    }

That's right. There's a `searchList` control -- very similar to the `masterDetail` control -- that automatically creates a standard search for you. It takes three required and one optional argument:

1. An entity _type_
2. A control taking a single argument (an item from the collection) to use in the _list view_.
3. A control taking a single argument (again, an item from the collection) to use in the _detail view_.
4. A maximum number of search results to show (defaults to 10)

And voila, the new search screen:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-13-at-1.37.18-PM-159x300.png" alt="" title="Screen shot 2011-01-13 at 1.37.18 PM" width="159" height="300" class="alignnone size-medium wp-image-259" />

Conclusion
----------

Higher-order controls are controls that take other controls as arguments. They enable the implementation of controls such as tab sets, master-detail and search lists (and more to come), greatly reducing the amount of code you need to write to build your applications.

