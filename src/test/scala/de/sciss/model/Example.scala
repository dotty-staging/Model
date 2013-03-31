package de.sciss.model

import collection.mutable

object Example extends App {
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
}