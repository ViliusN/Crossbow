package lt.norma.crossbow.core

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

import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.scalatest.FunSuite
import lt.norma.crossbow.messages.{Message, Request, Response}

class TwoWayCommunicationTest extends FunSuite {
  test("TwoWayCommunicationTest") {
    case class Start() extends Message
    case class DemoRequest() extends Request
    case class DemoResponse(request: Request) extends Response

    val server = new DataNode {
      def dependencies = Empty

      def receive = {
        case r@DemoRequest() =>
          dispatch(DemoResponse(r))
      }
    }
    val client = new DataNode {
      def dependencies = Empty

      var lastResponse: Option[DemoResponse] = None

      def receive = {
        case Start() =>
          val r = DemoRequest()
          server.send(r)
          assert(r eq lastResponse.get.request, "we should have the response by this time")
        case r: DemoResponse =>
          lastResponse = Some(r)
      }
    }
    server.add(client)
    client.add(server)
    client.send(Start())
  }
}
