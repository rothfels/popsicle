# Popsicle

Remember the summer days when we were young and life was simple? Then you lay on the lawn and enjoyed yourself an icy cool popsicle.

Gather round, friends, and let's enjoy a popsicle together once more!

## Overview

This is an experimental repository for full-stack web development in Scala. It comprises:
- A [Spray](http://spray.io/) server (run on the JVM) serving a single-page-application (HTML) composed of [React](http://facebook.github.io/react/) UI components, as well as a RPC API mounted at `/api/`.
- A [Scala.js](http://www.scala-js.org/) web client using the [Autowire](https://github.com/lihaoyi/autowire) library to connect React components to the Spray server API via Ajax (wrapped in type-safe, auto-routed RPCs).

The server connects to mongo using the [ReactiveMongo](http://reactivemongo.org/#step-by-step-example) Scala driver, which is fully async / non-blocking. It builds on [Akka](http://akka.io/) and makes a perfect complement to Spray, which builds on Akka too.

The React components are written in Scala with [extended](https://github.com/japgolly/scalajs-react) [scalatags](https://github.com/lihaoyi/scalatags) and compile down to optimized Javascript via [Google Closure Compiler](https://developers.google.com/closure/compiler/).

## Project Organization

There are three main sub-directories of `app`:
- `js`: client code; React component defintions compiled to Javascript
- `jvm`: server code; serves compiled Javascript in an single-page HTML app and mounts an API for remote procedure call
- `shared`: RPC definitions; shared by client and server code

## Motivation

Scala is a powerful language. Together with Autowire (macro to perform type-safe, reflection-free RPC between Scala systems), Scala.js, and scalatags it provides an extremely efficient way to build cross platform, scalable web applications in an integrated environment where the language (Scala) does most of the work. The dependencies for the project are extremely thin.

Under-the-hood, Autowire macros convert method calls into RPCs, together with relevant serialization code, and allow you to make type-safe API calls from Javascript to your JVM. Since the RPCs appear to be method calls, tools like IDEs are able to work with them doing project-wide renames or analysis, jump-to-definition, find-usages, etc. Most importantly, the compiler is able to typecheck these RPCs and ensure that you are always calling them with the correct arguments and handling the return-value correctly in turn. This removes an entire class of errors. Autowire is completely agnostic to both the serialization library and the transport-mechanism.

The ability to write (and test!) React components in the same compiled and type-safe environment you build your APIs and server code in doesn't sound or even feel real. You must see it to believe it.

## Building

Import sbt project into IntelliJ.

In the console:

```
sbt ~re-start
```

And go to `localhost:8080`
