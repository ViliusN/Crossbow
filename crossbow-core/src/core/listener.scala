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

/** Receives messages. Instances of `BasicListener` cannot receive data messages directly from data
  * providers. Therefore this trait should be implemented only to create data listeners which need
  * some special data handling. For example, `Indicator`s implement `BasicListener` because they
  * receive data messages not directly from the data provider, but from the indicators' list they
  * belong to. */
trait BasicListener {
  /** Implicitly converts object `Empty` to an empty receiver. */
  implicit def emptyToReceiver(empty: Empty) = new PartialFunction[Message, Unit] {
    def isDefinedAt(x: Message) = false
    def apply(v1: Message) { }
  }
  /** Send data to this listener. */
  final def send(message: Message) { if(this supports message) receive(message) }
  // TODO private def send(message: Message) {
  //  if(this supports message) {
  //    val result = receive(message) }
  //    check type of result, it must be Unit. In perfect case do this on compile time
  //  }
  /** Checks if the specified message is supported by this listener without actually sending the
    * message. */
  final def supports(message: Message): Boolean = receive.isDefinedAt(message)
  /** Creates a receiver for this listener. Partial function returned by this method is executed
    * only when a message is received via `send` method and only if the receiver is defined for that
    * particular message. Therefore there is no need to provide the default `case _ =>` option in
    * receivers - unsupported messages will be ignored. */
  protected def receive: PartialFunction[Message, Unit]
}

/** Extend this trait to create custom listeners. */
trait Listener extends BasicListener with Dependant[Listener]

/** Contains factory method for inline creation of listeners. */
object Listener {
  /** Enables inline creation of data listeners. */
  def apply(action: PartialFunction[Message, Unit]) = new Listener {
    def dependencies = Empty
    def receive = action
  }
}
