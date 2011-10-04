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

/** Represent any message sent through data nodes. */
trait Message

/** Represent data message. */
trait Data extends Message

/** Represents error message. Should be used to inform listeners about any abnormal conditions which
  * are not serious enough to be thrown as exceptions. */
trait ErrorMessage extends Message { def exception: java.lang.Exception }

/** Represents request message. Every data node should document all supported requests and responses
  * to them. */
trait Request extends Message

/** Represents response to a request. */
trait Response extends Message { def request: Request }

/** Response to a request which could not be fulfilled. */
case class FailedRequest(request: Request, exception: java.lang.Exception)
    extends Response with ErrorMessage

/** Empty message. Should be used for testing purposes only. */
case object EmptyMessage extends Message
