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

package lt.norma.crossbow.core.messages

import org.joda.time.DateTime

/** Session opening data message. */
case class SessionOpen(marketTime: DateTime) extends Data
/** Session closing data message. */
case class SessionClose(marketTime: DateTime) extends Data

/** Bar opening data message. */
case class BarOpen(marketTime: DateTime) extends Data
/** Bar closing data message. */
case class BarClose(marketTime: DateTime) extends Data

/** Message sent during loading of the system. Usually this is the first message the provider sends
  * after starting up. */
case object Load extends Message
/** Message send before closing the system. Usually this is the last message the provider sends
  * before shutting down. */
case object Unload extends Message
