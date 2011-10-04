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

import java.io.Writer

/** Writes received messages to the `out` stream depending on the specified `logLevel`. Useful for
  * testing and debugging. The output stream is flushed following every data message, but is never
  * closed. */
class MessageLogger(logLevel: LogLevel, out: Writer) extends Listener {
  import LogLevel._

  def dependencies = Empty
  def receive = {
    case message => write(message)
  }
  def write(message: Message) {
    (logLevel, message) match {
      case (Everything, m: Message) => writeMessage(m)
      case (ExceptData, _: Data) => // Ignore
      case (ExceptData, m: Message) => writeMessage(m)
      case (ErrorsOnly, e: ErrorMessage) => writeMessage(e)
      case _ => // Ignore
    }
  }
  def writeMessage(message: Message) {
    out.write(message.toString)
    out.write("\n")
    out.flush()
  }
}
