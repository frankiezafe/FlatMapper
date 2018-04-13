# FlatMapper
Video / generative visual mapper, running in processing.org.

## installation

* download / clone this repository on your drive;
* install [processing](http://processing.org) and required dependencies (see below);
* open the sketch;
* press *run* and that's it!

## structure of the repository

* root: processing sketches (.pde) and java classes (.java);
* **assets** folder: svg file of the logo and [pureadata](http://puredata.info/) patch to demonstrate OSC functionalities;
* **data** folder: images and video loaded by the code.

## requirements

The code runs in [processing](http://processing.org) (developped in version 3.3.6). It relies on several libraries of the framework:

* [oscP5](http://www.sojamo.de/libraries/oscP5/) - enable Open Sound Control functionalities;
* [ControlP5](http://www.sojamo.de/libraries/controlP5/) - management of the user interface;
* [video](https://processing.org/reference/libraries/video/index.html) - video management based on [GStreamer](https://gstreamer.freedesktop.org/), not mandatory, but used in the example;

All these addons are installable via the standard procedure: *Tools* > *Add tool...* in the IDE interface.

To run the puredata patch, you have to install:

* [pureadata](http://puredata.info/);
* [mrpeach](https://github.com/reduzent/pd-mrpeach) - enable Open Sound Control functionalities.

mrpeach is installable via **deken**. Open puredata, go to *Help* > *Find externals*, type *mrpeach* in the search box and install the first result.

## OSC

You can control the properties of *planes* and *lines* via OSC. To do so, set the OSC address field of each element. The default value is **/M** for planes and **/L** for lines.

The application is listening to port **23000** by default. Modify **oscP5_port** parameter in FlatMapper.pde to yur needs.

*warning*: The OSC address displayed in lists is set to uppercase by ControlP5!

### message form

* **address**: must correspond to the osc address parameters of the element, **/M** by default;
* **arguments**
* * **name**: the name of the parameter;
* * **float**: depending on the paramater, a serie of 0 to 3 floats.

For instance, a message to control a plane with osc address set to */myplane* would look like:

* address: **/myplane**;
* arg[0], string: **pos**;
* arg[1], float: a number between 0 and 3, representing the point of the plane;
* arg[2], float: a number representing the **x** position of the point;
* arg[3], float: a number representing the **y** position of the point.

This will change the position of one point.

* address: **/myplane**;
* arg[0], string: **hide**.

This will hide the plane.

I guess you get the idea...

### valid names

See *Mappable.java*, method *parse(OscMessage msg)* for the implementation.

* **opacity** followed by 1 float;
* **red** followed by 1 float;
* **green** followed by 1 float;
* **blue** followed by 1 float;
* **rgb** followed by 3 floats;
* **rgba** followed by 4 floats;
* **pos** followed by 3 floats, first is the point index, [0,3] for a plane, [0,1] for a line, followed by the XY coordinates;
* **div** followed by 1 float, this will affect the number of *subdivisions* of the object's mesh;
* **tex** followed by 1 string, this will affect the *texture* used by the object;
* **osc** followed by 1 string, this will affect the *osc address* of the object;
* **hide**;
* **show**.

If none of these names are found in the message, the method *parse_custom(OscMessage msg)* will be call automatically. The method can be overloaded in *Line.java* and *Plane.java*.

## classes hierarchy

* **FlatMapper.pde**: main object, in charge of the rendering window and instanciating all the other objects.
* **FM_extra.pde**: sub-part of FlatMapper, contains osc, textures management and serialisation methods.
* **ControlFrame.java**: floating window containing the UI.
* **FlatMap.java**: a simple object containing the lists of lines and planes to serialise.
* **Mappable.java**: base class of any object used in the main window, it contains all common parameters and methods.
* **Line.java**: a specialisation of Mappable with 2 points and thickness control.
* **Plane.java**: a specialisation of Mappable with 4 points.
* **ResolutionChooser.java**: a pure java popup, allowing user to select the resolution and position of the main window.
* **DEMO_RT.pde**: processing example *Topics/Geometry/Shapetransform.pde* rendered on  texture.

