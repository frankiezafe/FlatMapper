# FlatMapper
Video / generative visual mapper, running in processing.org.

## Structure of the repository

* root folder: processing sketches (.pde) and java classes (.java)
* **assets** folder: processing sketches (.pde) and java classes (.java)

## Requirements

The code must be open in [processing](http://processing.org) (developped in version 3.3.6). It relies on several libraries of the framework:

* [oscP5])(http://www.sojamo.de/libraries/oscP5/) - enable Open Sound Control functionalities;
* [ControlP5])(http://www.sojamo.de/libraries/controlP5/) - management of the user interface;
* [video])(https://processing.org/reference/libraries/video/index.html) - video management based on [GStreamer](https://gstreamer.freedesktop.org/), this library not mandatory, but used in the example;

All these addons are installable via the standard procedure: *Tools* > *Add tool...* in the IDE interface.

To run the 