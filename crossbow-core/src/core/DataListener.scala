/*
 * Copyright 2010-2011 Vilius Normantas <code@norma.lt>
 *
 * This file is part of Crossbow library.
 *
 * Crossbow is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Crossbow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Crossbow.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package lt.norma.crossbow.core

/** Receives data messages. Instances of `BasicDataListener` cannot receive data messages directly
  * from data providers. Therefore this trait should be implemented only to create data listeners
  * which need some special data handling. For example, `Indicator`s implement `BasicDataListener`
  * because they receive data not directly from the data provider, but from the indicators' list
  * they belong to.
  *
  * Some examples of `receive` method:
  * {{{
  * class MyDataListener extends DataListener {
  *   def dependencies = Set.empty
  *   def receive = {
  *     case t: Trade => println("Trade received: "+t)
  *     case q: Quote => println("Quote received: "+q)
  *     case d: Data => println("Unkown type of data received: "+d)
  *   }
  * }
  * }}}
  * It is safe to ignore unnecessary types of data:
  * {{{
  * class MyTradeListener extends DataListener {
  *   def dependencies = Set.empty
  *   def receive = {
  *     case t: Trade => println("Trade received: "+t)
  *   }
  * }
  * }}}
  * This example captures any data message:
  * {{{
  * class MyDataCounter extends DataListener {
  *   var counter = 0
  *   def dependencies = Set.empty
  *   def receive = {
  *     case _ => counter += 1
  *   }
  * }
  * }}} */
trait BasicDataListener {
  implicit def emptyToReceiver(empty: Empty): PartialFunction[Data, Unit] = { case _ => }

  /** Send data to this listener. */
  final def send(data: Data) { if(receive.isDefinedAt(data)) receive(data) }

  /** Creates a receiver for this listener. Partial function provided as an argument is executed
    * only when data is received via `send` method and only if it is defined for that particular
    * data message. Therefore there is no need to provide the default `case _ =>` option. */
  protected def receive: PartialFunction[Data, Unit]
}

/** Extend this trait to create custom data listeners. */
trait DataListener extends BasicDataListener with Dependant[DataListener]

/** Contains factory methods for simplified creation of data liteners. */
object DataListener {
  /** Enables inline creation of data listeners. Example:
    * {{{
    * val l = DataListener {
    *   case t: Trade => println("Trade received "+t)
    *   case q: Quote => println("Quote received "+q)
    *   case d: Data => println("Unkown type of data received "+d)
    * }
    * }}} */
  def apply(action: PartialFunction[Data, Unit]) = new DataListener {
    def dependencies = Empty
    def receive = action
  }
}
