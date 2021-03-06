![Maven Central](https://img.shields.io/maven-central/v/it.tidalwave.thesefoolishthings/thesefoolishthings.svg)
[![Build Status](https://img.shields.io/jenkins/s/http/services.tidalwave.it/ci/job/TheseFoolishThings_Build_from_Scratch.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Test Status](https://img.shields.io/jenkins/t/http/services.tidalwave.it/ci/job/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Coverage](https://img.shields.io/jenkins/c/http/services.tidalwave.it/ci/job/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)

TheseFoolishThings
================================

This project was born, several years ago, as the classic “kitchen sink” of stuff from other projects that was deemed of being reusable.
Sometimes it grew in a disordered way, other times it was reviewed and cleaned up accurately. It contains different sort of things: from
simple stuff such as a **```TimeProvider```** that can be mocked (for time-based tests), to **type safe ```Map```s** (following
advice of Joshua Bloch); up to some more structured things such an extensible **```Finder```** to manipulate queries from a generic data
source; **a facility for working with the DCI (Data, Context and Interactions) architectural pattern**; a **simple ```MessageBus```** suitable for using
the Publish and Subscribe pattern inside an application.

There is also some really exotic stuff, such as small implementation of **collaborative
Actors** (working, but designed basically for didactic purposes). Actually this whole project is often used in Java classes to show things
that are reasonably contextualized in a real-world scenario (the core parts of this project are used by working pet projects; something is
also part of industrial project). There is good stuff and some strange stuff - in general an attitude of this project is to also try things
in a different way than the standard.

TheseFoolishThings supports JDK 8, but requires JDK 11 or 16 to be compiled.


Bootstrapping
-------------

In order to build the project, run from the command line:

```mvn -DskipTests```

The project can be opened and built by a recent version of the NetBeans, Eclipse or Idea IDEs.


Documentation
-------------

More information can be found on the [homepage](http://tidalwave.it/projects/thesefoolishthings) of the project.


Contributing
------------

We accept pull requests via Bitbucket or GitHub.

There are some guidelines which will make applying pull requests easier for us:

* No tabs! Please use spaces for indentation.
* Respect the code style.
* Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source
  ode should be reformatted create a separate PR for this change.
* Provide TestNG tests for your changes and make sure your changes don't break any existing tests by running
```mvn clean test```.

If you plan to contribute on a regular basis, please consider filing a contributor license agreement. Contact us for
 more information


License
-------

Code is released under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).


Additional Resources
--------------------

* [Tidalwave Homepage](http://tidalwave.it)
