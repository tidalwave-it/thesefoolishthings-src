![Maven Central](https://img.shields.io/maven-central/v/it.tidalwave.thesefoolishthings/thesefoolishthings.svg)
[![Build Status](https://img.shields.io/jenkins/s/http/services.tidalwave.it/ci/TheseFoolishThings_Build_from_Scratch.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Test Status](https://img.shields.io/jenkins/t/http/services.tidalwave.it/ci/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Coverage](https://img.shields.io/jenkins/c/http/services.tidalwave.it/ci/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)

TheseFoolishThings
================================

A kitchen sink of small libraries that are used by all projects by Tidalwave. It mostly contains:

* a few generic stuff such as ```Id``` and ```IdFactory```;
* the ```Finder``` API for fluent query builders;
* support for the DCI pattern (Data Context and Interaction), with optional Spring integration;
* a tiny but functional local Message Bus, with optional Spring integration;
* a small, experimental Actor library;
* some test utilities.

TheseFoolishThings supports JDK 7 and has got a small JDK 8 integration module.

Since version 3.0 lots of old stuff have been dropped:

* Support for Swing.
* Support for the NetBeans Platform.
* A very small support library for Vaadin.


Bootstrapping
-------------

In order to build the project, run from the command line:

```mvn -DskipTests```

The project can be opened and built by a recent version of the NetBeans, Eclipse or Idea IDEs.


Documentation
-------------

More information can be found on the [homepage](http://thesefoolishthings.tidalwave.it) of the project.


Where can I get the latest release?
-----------------------------------
You can download source and binaries from the [download page](https://bitbucket.org/tidalwave/thesefoolishthings-src/src).

Alternatively you can pull it from the central Maven repositories:

```xml
<dependency>
    <groupId>it.tidalwave.thesefoolishthings<groupId>
    <artifactId>thesefoolishthings</artifactId>
    <version>-- version --</version>
</dependency>
```


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
* [Project Issue Tracker (Jira)](http://services.tidalwave.it/jira/browse/TFT)
* [Project Continuous Integration (Jenkins)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
