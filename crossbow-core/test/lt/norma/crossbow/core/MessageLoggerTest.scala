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

import java.io.StringWriter
import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.messages.{Message, Data, ErrorMessage}

class MessageLoggerTest extends FunSuite {

  import LogLevel._

  val mockMessage = new Message {
    override def toString = "mockMessage"
  }
  val mockData = new Data {
    override def toString = "mockData";

    def marketTime = DateTime.now
  }
  val mockErrorMessage = new ErrorMessage {
    def exception = Exception("")

    override def toString = "mockErrorMessage"
  }

  test("LogLevel.Everything") {
    val out = new StringWriter()
    val logger = new MessageLogger(Everything, out)
    logger.send(mockMessage)
    logger.send(mockData)
    logger.send(mockErrorMessage)
    expect("mockMessage\nmockData\nmockErrorMessage\n") {
      out.toString
    }
  }

  test("LogLevel.ExceptData") {
    val out = new StringWriter()
    val logger = new MessageLogger(ExceptData, out)
    logger.send(mockMessage)
    logger.send(mockData)
    logger.send(mockErrorMessage)
    expect("mockMessage\nmockErrorMessage\n") {
      out.toString
    }
  }

  test("LogLevel.ErrorsOnly") {
    val out = new StringWriter()
    val logger = new MessageLogger(ErrorsOnly, out)
    logger.send(mockMessage)
    logger.send(mockData)
    logger.send(mockErrorMessage)
    expect("mockErrorMessage\n") {
      out.toString
    }
  }
}
