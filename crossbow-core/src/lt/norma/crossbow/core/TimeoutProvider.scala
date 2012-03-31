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

import TimeoutProvider._
import java.util.{ Timer, TimerTask }
import lt.norma.crossbow.messages.{ Request, Response }
import org.joda.time.{ DateTime, Duration }
import scala.actors.Actor._

/** Provides timeout events. */
class TimeoutProvider extends DataNode {
  def dependencies = Empty
  private val timer = new Timer(true)
  private var canceledRequests = List[Start]()

  def receive = {
    case sr @ Start(t) =>
      timer.schedule(new TimerTask { def run { synchronizer ! Timeout(sr) } }, t.toDate)
    case cr: Cancel =>
      synchronizer ! cr
  }

  private val synchronizer = actor {
    loop {
      react {
        case Timeout(sr) if(canceledRequests.exists(_ eq sr)) =>
          canceledRequests = canceledRequests.filterNot(_ eq sr)
        case to: Timeout =>
          dispatch(to)
        case Cancel(sr) =>
          canceledRequests = sr :: canceledRequests
      }
    }
  }
}

/** Contains messages used by `TimeoutProvider`. */
object TimeoutProvider {
  /** Request to start timeout timer. */
  case class Start(endTime: DateTime) extends Request
  object Start {
    /** Factory method to create instance of `Start` with the specified duration. */
    def apply(duration: Duration): Start = Start(DateTime.now.plus(duration))
  }

  /** Request to cancel timeout timer. */
  case class Cancel(request: Start) extends Request

  /** Response sent on timeout. */
  case class Timeout(request: Start) extends Response
}
