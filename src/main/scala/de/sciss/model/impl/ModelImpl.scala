/*
 *  ModelImpl.scala
 *  (Model)
 *
 *  Copyright (c) 2013-2017 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.model
package impl

import scala.util.control.NonFatal

/** A straight forward implementation of a model. The trait implements all required methods and
  * provides a `dispatch` method to fire any updates.
  */
trait ModelImpl[U] extends Model[U] {
  private type Listener = Model.Listener[U]
  private[this] val sync = new AnyRef

  @volatile
  private[this] var listeners = Vector.empty[Listener]

  /** Removes all listeners. This is useful when disposing the model, to remove any unnecessary references. */
  protected def releaseListeners(): Unit = sync.synchronized {
    listeners = Vector.empty
  }

  /** Synchronously dispatches an update to all currently registered listeners. Non fatal exceptions are
    * caught on a per-listener basis without stopping the dispatch.
    */
  final protected def dispatch(update: U): Unit = {
    val ls = listeners // sync.synchronized(listeners)
    ls.foreach { pf =>
      try {
        if (pf.isDefinedAt(update)) {
          pf(update)
        }
      } catch {
        case NonFatal(e) => e.printStackTrace()
      }
    }
  }

  /** Subclasses can override this to issue particular actions when the first listener has been registered */
  protected def startListening(): Unit = ()
  /** Subclasses can override this to issue particular actions when the last listener has been unregistered */
  protected def stopListening (): Unit = ()

  def addListener(pf: Model.Listener[U]): pf.type = sync.synchronized {
    val start = listeners.isEmpty
    listeners :+= pf
    if (start) startListening()
    pf
  }

  def removeListener(pf: Model.Listener[U]): Unit = sync.synchronized {
    sync.synchronized {
      val idx = listeners.indexOf(pf)
      val stop = (idx >=0) && {
        listeners = listeners.patch(idx, Nil, 1)
        listeners.isEmpty
      }
      if (stop) stopListening()
    }
  }
}