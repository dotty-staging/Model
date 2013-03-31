/*
 *  ModelImpl.scala
 *  (Model)
 *
 *  Copyright (c) 2013 Hanns Holger Rutz. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.model
package impl

import scala.util.control.NonFatal

trait ModelImpl[U] extends Model[U] {
  private type Listener = Model.Listener[U]
  private val sync      = new AnyRef
  private var listeners = Vector.empty[Listener]

  final protected def dispatch(update: U) {
    sync.synchronized {
      listeners.foreach { pf =>
        if (pf.isDefinedAt(update)) try {
          pf(update)
        } catch {
          case NonFatal(e) => e.printStackTrace()
        }
      }
    }
  }

  final def addListener(pf: Listener) = sync.synchronized {
    listeners :+= pf
    pf
  }

  final def removeListener(pf: Listener) { sync.synchronized {
    val idx = listeners.indexOf(pf)
    if (idx >=0 ) listeners = listeners.patch(idx, Nil, 1)
  }}
}