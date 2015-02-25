# Model

[![Flattr this](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=sciss&url=https%3A%2F%2Fgithub.com%2FSciss%2FModel&title=Model%20Library&language=Scala&tags=github&category=software)
[![Build Status](https://travis-ci.org/Sciss/Model.svg?branch=master)](https://travis-ci.org/Sciss/Model)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/model_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/model_2.11)

## statement

Model is a simple building block for the Scala programming language, providing a typed publisher-observer mechanism. It is (C)opyright 2013&ndash;2014 by Hanns Holger Rutz. All rights reserved. This project is released under the [GNU Lesser General Public License](https://raw.github.com/Sciss/Model/master/LICENSE) and comes with absolutely no warranties. To contact the author, send an email to `contact at sciss.de`

## linking

To link to this library:

    libraryDependencies += "de.sciss" %% "model" % v

The current version `v` is `"0.3.2+"`

## building

This project currently builds against Scala 2.10, using sbt 0.13.

## example

You would declare the mixin of `Model[U]` where `U` is the type of update sent by the model. The actual implementation would then most likely use the implementation `impl.ModelImpl`. Observers register with the model using `addListener` which takes a partial function. In that respect a model is similar to Scala-Swing's `Reactor`, however being specific in the argument type `U`. The `addListener` method returns the partial function for future reference, useful in unregistering the observer via `removeListener` (be careful not to use Scala's method-to-function conversion for listeners, as repeated calls will produce non-identical partial function instances, so `removeListener` would fail).

Typically you declare the update type in the model's companion object as a sealed trait. Here is an example of an observed set (included in the test sources):

```scala

    object SetModel {
      sealed trait Update[A]
      case class Added  [A](elem: A) extends Update[A]
      case class Removed[A](elem: A) extends Update[A]

      def empty[A]: SetModel[A] = new Impl[A]

      private class Impl[A] extends SetModel[A] with impl.ModelImpl[Update[A]] {
        private val peer = mutable.Set.empty[A]

        def add(elem: A): Boolean = {
          peer.synchronized {
            val res = peer.add(elem)
            if (res) dispatch(Added(elem))
            res
          }
        }

        def remove(elem: A): Boolean = {
          peer.synchronized {
            val res = peer.remove(elem)
            if (res) dispatch(Removed(elem))
            res
          }
        }
      }
    }
    trait SetModel[A] extends Model[SetModel.Update[A]] {
      def add   (elem: A): Boolean
      def remove(elem: A): Boolean
    }

    val set = SetModel.empty[Int]
    val obs = set.addListener {
      case SetModel.Added  (elem) => println(s"Observed addition of $elem")
      case SetModel.Removed(elem) => println(s"Observed removal  of $elem")
    }
    set.add(1)            // observed
    set.add(2)            // observed
    set.add(1)            // no-op
    set.remove(1)         // observed
    set.remove(3)         // no-op
    set.removeListener(obs)
    assert(set.remove(2)) // unobserved
```
