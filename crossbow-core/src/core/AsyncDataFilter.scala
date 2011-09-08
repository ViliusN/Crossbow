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

import scala.actors.Actor
import scala.actors.Actor._

/** Receives messages from multiple threads and forwards all messages to it's own listeners in
  * single thread. The main use of this filter is to ensure that only one thread provides messages
  * to all listeners. */
class AsyncDataFilter extends DataNode with Actor {
  def dependencies = Empty
  def act() {
    loop {
      react {
        case m: Message => dispatch(m)
      }
    }
  }
  def receive = {
    case m => this ! m
  }
  start
}
