# Popsicle

Remember the summer days when we were young and life was simple? Then you lay on the lawn and enjoyed an icy cool popsicle.

Gather round, friends, and let's enjoy a popsicle together once more!

## Overview

This is an experimental repository for full-stack web development in Scala. It comprises:
- A [Spray](http://spray.io/) server (running on the JVM) serving a single-page-application (HTML) composed of [React](http://facebook.github.io/react/) UI components built in [Scala.js](http://www.scala-js.org/) with [extended](https://github.com/japgolly/scalajs-react) [scalatags](https://github.com/lihaoyi/scalatags).
- An API mounted at `/api/` for client RPC via http post
- A websocket Spray server (currently running as a separate process on port 8081).
- [Autowire](https://github.com/lihaoyi/autowire) to provide seamless, type-safe RPC from client to server (via ajax) and from server to client (via websocket).
- Single sbt project cross-compiled to JS and JVM via [cross-build](http://www.scala-js.org/doc/sbt/cross-building.html).
- [utest](https://github.com/lihaoyi/utest) framework for client and server tests

## Project Organization

There are three main sub-directories of `app`:
- `js`: client code; React component defintions compiled to Javascript with Google Closure compiler
- `jvm`: server code; serves compiled Javascript in an single-page HTML app, mounts an API for remote procedure call, websocket server
- `shared`: model and RPC interface definitions; shared by client and server code

## Motivation

Scala is a powerful language. With nothing more than its core features this project provides a completely transparent interface for client and server communication all managed within one project. The compiler eliminates an entire class of problems: end-to-end type errors. With strong typing we also get the benefits of powerful code (analysis, jump-to-definition, find-usages) and refactoring tools. It is an interesting study of what full-stack web development could feel like if we could completely avoid Javascript.

Warning: tools and support for development are weak with this project setup. Testing, in particular, can get tricky. JVM tests go pretty much as you expect, but testing your React components in PhantomJS can get tricky without sourcemap support (currently missing feature in Phantom, not ScalaJS).

## Building

Import sbt project into IntelliJ.

In the console:

```
sbt ~re-start
```

And go to `localhost:8080`

## Testing

(To run both scalajs and scalajvm tests...)

In the console:

```
sbt ~test
```
