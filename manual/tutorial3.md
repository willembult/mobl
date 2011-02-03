Styling Your Application
========================

In this tutorial we'll grab the code from the tip calculator and play with mobl styling a little bit. Note that mobl styling is a still somewhat experimental feature and therefore some IDE features, such as code completion may not always be up to par.

The application we will be styling is our tip calculator application. Here's the code that we [ended up with](http://www.mobl-lang.org/56/your-first-application/):

    application tipcalculator

    import mobl::ui::generic

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

As you can tell from the `import` statement, we are using the `mobl::ui::generic` set of controls. This set of controls has the nice feature of having some _style variables_ that we can override. Let's try that now, shall we?

First, we have to define a new module, we will call it `ui.mobl` in our project. In it, we put two lines:

    module ui
    
    mixin mobl::ui::generic

The `mixin` construct effectively takes all definitions from the `mobl::ui::generic` module and copies them in the current module. Conceptually this is basically a copy & paste operation. Save your module. If you get an error message at the first line, close the `ui.mobl` file and open it again in Eclipse (this is a IDE bug), the error should now be gone.

Adapt the `tipcalculator.mobl` file and replace:

    import mobl::ui::generic

with

    import ui

Save your application and run it. Everything should still work, nothing changed.

Now, let's move back to the `ui.mobl` module. We're going to override the `$baseColor` styling variable. Styling variables are all prefixed with a `$`. After the `mixin` line, add the following line:

    style $baseColor = $baseColorGreen

The `mobl::ui::generic` module defines a number of base colors to choose from, these are somewhat reasonable-looking choices. Here are the colors and their definitions:

    style $baseColor = rgb(72, 100, 180)
    style $baseColorGreen = rgb(60, 150, 30)
    style $baseColorRed = rgb(150, 50, 20)
    style $baseColorRedish = rgb(150, 80, 0)

Just for the purpose of demonstration, I added a button at the bottom of the tip calculator, it doesn't do anything. It's just there to show off how the colors work.

Now, by simply redefining the `$baseColor`, we can change the basic color scheme:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-12.58.36-PM-159x300.png" alt="" title="Screen shot 2011-01-14 at 12.58.36 PM" width="159" height="300" class="alignnone size-medium wp-image-308" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-12.58.49-PM-159x300.png" alt="" title="Screen shot 2011-01-14 at 12.58.49 PM" width="159" height="300" class="alignnone size-medium wp-image-309" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-12.59.06-PM-159x300.png" alt="" title="Screen shot 2011-01-14 at 12.59.06 PM" width="159" height="300" class="alignnone size-medium wp-image-310" />
<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-12.59.20-PM-159x300.png" alt="" title="Screen shot 2011-01-14 at 12.59.20 PM" width="159" height="300" class="alignnone size-medium wp-image-311" />

Pretty nifty, right? Of course, we can also choose our own, custom color. Pick any `rgb` values you like, a gray one, for instance:

    style $baseColor = rgb(100, 100, 100)

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-1.03.04-PM-159x300.png" alt="" title="Screen shot 2011-01-14 at 1.03.04 PM" width="159" height="300" class="alignnone size-medium wp-image-314" />

We can get more granular than this, however. For instance, colors for buttons and shadows are derived from the `$baseColor` by default, but can be overridden in a similar fashion. Here are the default values for these variables:

    style $buttonFromColor       = rgb($baseColor.r+170,
                                       $baseColor.g+170,
                                       $baseColor.b+170)
    style $buttonToColor         = rgb($baseColor.r+120,
                                       $baseColor.g+120,
                                       $baseColor.b + 120)
    style $buttonPushedFromColor = rgb($baseColor.r+100,
                                       $baseColor.g+100,
                                       $baseColor.b+100)
    style $buttonPushedToColor   = rgb($baseColor.r+50,
                                       $baseColor.g+50,
                                       $baseColor.b+50)
    style $textButtonColor       = rgb($baseColor.r~50,
                                       $baseColor.g~50,
                                       $baseColor.b~50)
    style $textButtonShadowColor = rgb($baseColor.r+100,
                                       $baseColor.g+100,
                                       $baseColor.b+100)

As you can tell, it is possible to do simple calculations in these values. Operators that are supported are `+`, `*`, `/` and `~` (for subtraction, using `-` would be ambiguous with CSS). Each of these individual style variables can be overridden.

But what if we want something completely different, a different style for one particular button, for instance? Well, that's possible too.

Custom styles
-------------

The `button` control has two optional styling arguments: `style` and `pushedStyle`. The latter is used to render the button when it is, well, pushed. Before we set those, however, we have to define new versions of these styles. Therefore, we add the following new styles to our `ui.mobl`:

    style myButtonStyle {
      border: 1px solid #000;
      padding: 10px;
      background-color: #ccc;
      line-height: 3em;
      margin: 5px;
    }

    style myButtonPushedStyle {
      border: 1px solid #000;
      padding: 10px;
      background-color: #777;
      line-height: 3em;
      margin: 5px;
    }

To those familiar with CSS this will immediately look familiar. The body of the style is pure CSS, with a few additional features that we will get to later. As can be seen, a style has a name and a body. Since all top-level definitions in a module share the same namespace it is good practice to end a style name with `Style`. Now, from our `tipcalculator.mobl` file we can create a button that uses this style:

    button("A button", style=myButtonStyle,
                       pushedStyle=myButtonPushedStyle)

Save the file and run the application to see the resulting, simple plain button:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-1.49.46-PM.png" alt="" title="Screen shot 2011-01-14 at 1.49.46 PM" width="87" height="48" class="alignnone size-full wp-image-340" />

Looks kind of ugly though. It would be much improved if the button had rounded corners. Although the `border-radius` property is part of CSS3, it has not been implemented in many browser with that name. In webkit browser this property is named `-webkit-border-radius` and in Firefox it's `-moz-border-radius`. To do it well, to create borders we basically would have to repeat ourselves a few times:

    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;

Obviously, that is sub-optimal. To prevent repeating yourself, mobl offers _style mixins_. A few useful ones are defined in the `mobl::ui::stylemixin` module. Here's the definition of the `borderRadiusMixin`:

    style mixin borderRadiusMixin($radius) {
      -moz-border-radius: $radius;
      -webkit-border-radius: $radius;
      border-radius: $radius;
    }

It can be used as follows:

    style myButtonStyle {
      ...
      margin: 5px;
      borderRadiusMixin(5px);
    }

Which will be expanded by the compiler to:

    style myButtonStyle {
      ...
      margin: 5px;
      -moz-border-radius: 5px;
      -webkit-border-radius: 5px;
      border-radius: 5px;
    }

The button now has rounded corners!

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-1.33.21-PM1.png" alt="" title="Screen shot 2011-01-14 at 1.33.21 PM" width="90" height="46" class="alignnone size-full wp-image-323" />

Background gradients also look nice on buttons, let's add one of those using the `backgroundGradient` mixin:

    style myButtonStyle {
      border: 1px solid #000;
      padding: 10px;
      line-height: 3em;
      margin: 5px;
      borderRadiusMixin(5px);
      backgroundGradientMixin(#ddd, #bbb);
    }

    style myButtonPushedStyle {
      border: 1px solid #000;
      padding: 10px;
      line-height: 3em;
      margin: 5px;
      borderRadiusMixin(5px);
      backgroundGradientMixin(#aaa, #999);
    }

Now we have buttons with a nice, subtle gradient:

<img src="http://www.mobl-lang.org/wp-content/uploads/2011/01/Screen-shot-2011-01-14-at-1.37.11-PM.png" alt="" title="Screen shot 2011-01-14 at 1.37.11 PM" width="87" height="47" class="alignnone size-full wp-image-325" />

There is still a fair bit of duplication in the pushed and non-pushed version of the style. Let's git rid of that as well, using a mixin:

    style mixin myButtonStyleMixin($fromColor, $toColor) {
      border: 1px solid #000;
      padding: 10px;
      line-height: 3em;
      margin: 5px;
      borderRadiusMixin(5px);
      backgroundGradientMixin($fromColor, $toColor);
    }

    style myButtonStyle {
      myButtonStyleMixin(#ddd, #bbb);
    }

    style myButtonPushedStyle {
      myButtonStyleMixin(#aaa, #999);
    }

Wohoo!

Conclusion
----------

Mobl's styling language is designed to be flexible as well as powerful. CSS is an excellent language to style applications and mobl embraces its features. However, CSS does miss a few useful features, such as style variables and mixins. Custom languages like [Sass](http://sass-lang.com/) add some of these features, but require an additional tool in the tool chain. Mobl borrows some of Sass's features and integrates them into its styling language.

When styling an application a first step may be to adapt the `$baseColor` variable and other styling variables. Then, to style particular controls, control-specific styles can be defined and used.

