/*
 *  DummyImpl.scala
 *  (Model)
 *
 *  Copyright (c) 2013-2014 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.model
package impl

import de.sciss.model.Model.Listener

//object DummyImpl {
//  def testVariance: Model[String] = new DummyImpl {}
//}
trait DummyImpl extends Model[Nothing] {
  def removeListener(pf: Listener[Nothing]): Unit = ()
  def addListener   (pf: Listener[Nothing]): pf.type = pf
}
