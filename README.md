![Maven Central](https://img.shields.io/maven-central/v/it.tidalwave.thesefoolishthings/thesefoolishthings.svg)
[![Build Status](https://img.shields.io/jenkins/s/http/services.tidalwave.it/ci/job/TheseFoolishThings_Build_from_Scratch.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Test Status](https://img.shields.io/jenkins/t/http/services.tidalwave.it/ci/job/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)
[![Coverage](https://img.shields.io/jenkins/c/http/services.tidalwave.it/ci/job/TheseFoolishThings.svg)](http://services.tidalwave.it/ci/view/TheseFoolishThings)

TheseFoolishThings
================================

This project is a collection of miscellaneous tools shared by a number of projects of the same author. There are **simple tuples** to use with Java Streams,
**type-safe maps** ([inspired by the heterogeneous map pattern](https://www.informit.com/articles/article.aspx?p=2861454&seqNum=8)) described in Effective Java
by Joshua Bloch, a **finder** that handles in a smart way queries to data sources, a facility to use  the **DCI (Data, Context and Interactions) architectural
pattern**, a simple **message bus** suitable for using the pub-sub pattern inside an application, some **test utilities**, an **experimental actor framework**
and a few other small things.

Yes, the project name is a tribute to the [jazz standard with the same name](https://en.wikipedia.org/wiki/These_Foolish_Things_(Remind_Me_of_You)) by
Maschwitz and Strachey.

TheseFoolishThings requires and is tested with JDKs in this range: [11, 17].
It is released under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).

Please have a look at the [project website](${project.url}/${project.version}) for a quick introduction with samples, tutorials, JavaDocs and build reports.


Bootstrapping
-------------

In order to build the project, run from the command line:

```shell
mkdir thesefoolishthings
cd thesefoolishthings
git clone https://bitbucket.org/tidalwave/thesefoolishthings-src .
mvn -DskipTests
```

The project can be opened with a recent version of the [IntelliJ IDEA](https://www.jetbrains.com/idea/), 
[Apache NetBeans](https://netbeans.apache.org/) or [Eclipse](https://www.eclipse.org/ide/) IDEs.


Contributing
------------

Pull requests are accepted via [Bitbucket](https://bitbucket.org/tidalwave/thesefoolishthings-src) or [GitHub](https://github.com/tidalwave-it/thesefoolishthings-src). There are some guidelines which will make 
applying pull requests easier:

* No tabs: please use spaces for indentation.
* Respect the code style.
* Create minimal diffs — disable 'on save' actions like 'reformat source code' or 'organize imports' (unless you use the IDEA specific configuration for 
  this project).
* Provide [TestNG](https://testng.org/doc/) tests for your changes and make sure your changes don't break any existing tests by running
```mvn clean test```. You can check whether there are currently broken tests at the [Continuous Integration](http://services.tidalwave.it/ci/view/TheseFoolishThings) page.

If you plan to contribute on a regular basis, please consider filing a contributor license agreement. Contact us for
 more information.


Additional Resources
--------------------

* [Issue tracking](http://services.tidalwave.it/jira/browse/TFT)
* [Continuous Integration](http://services.tidalwave.it/ci/view/TheseFoolishThings)
* [Tidalwave Homepage](http://tidalwave.it)
