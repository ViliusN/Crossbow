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

import lt.norma.crossbow.messages._
import lt.norma.crossbow.messages.{ Message, EmptyMessage }
import org.scalatest.FunSuite

class ListenerTest extends FunSuite {

  case class DummyMessage1(value: Int) extends Message

  case class DummyMessage2(value: Int) extends Message

  test("supports") {
    case object DemoMessage1 extends Message
    case object DemoMessage2 extends Message
    case object DemoMessage3 extends Message
    val listener = new BasicListener {
      def receive = {
        case DemoMessage1 =>
        case DemoMessage2 =>
      }
    }
    assert {
      listener.supports(DemoMessage1)
    }
    assert {
      listener.supports(DemoMessage2)
    }
    assert {
      !listener.supports(DemoMessage3)
    }
    assert {
      !listener.supports(new Message {})
    }
  }

  test("supports - should support any message") {
    case object DemoMessage1 extends Message
    case object DemoMessage2 extends Message
    val listener = new BasicListener {
      def receive = {
        case _ =>
      }
    }
    assert {
      listener.supports(DemoMessage1)
    }
    assert {
      listener.supports(DemoMessage2)
    }
    assert {
      listener.supports(new Message {})
    }
  }

  test("supports - empty partial function as receiver") {
    val listener = new BasicListener {
      def receive = new PartialFunction[Message, Unit] {
        def isDefinedAt(x: Message) = false

        def apply(v1: Message) {}
      }
    }
    assert {
      !listener.supports(new Message {})
    }
  }

  test("supports - implicit conversion of Empty receiver") {
    val listener = new BasicListener {
      def receive = Empty
    }
    assert {
      !listener.supports(new Message {})
    }
  }

  test("creation of listeners") {
    val bl = new BasicListener {
      def dependencies = Empty;

      def receive = Empty
    }
    assert(!(bl.isInstanceOf[Dependant[_]]), "BasicListener should not extend Dependant")
    val l = new Listener {
      def dependencies = Empty;

      def receive = Empty
    }
    assert(l.isInstanceOf[Dependant[_]], "Listener should extend Dependant")
  }

  test("receiving a message") {
    class DummyListener extends Listener {
      def dependencies = Empty

      var lastMessage: Option[Message] = None

      def receive = {
        case m: DummyMessage1 => lastMessage = Some(m)
      }
    }
    val l = new DummyListener
    expect(None) {
      l.lastMessage
    }
    val m1 = DummyMessage1(111)
    l.send(m1)
    expect(Some(m1)) {
      l.lastMessage
    }
    val m2 = DummyMessage2(222)
    l.send(m2)
    expect(Some(m1)) {
      l.lastMessage
    }
    l.send(EmptyMessage)
    expect(Some(m1)) {
      l.lastMessage
    }
  }

  test("send - checking for result type") {
    val listenerOk = new Listener {
      def dependencies = Empty

      def receive = {
        case _ =>
      }
    }
    val listenerWrong = new Listener {
      def dependencies = Empty

      def receive = {
        case _ => 8
      }
    }
    listenerOk.send(EmptyMessage)
    intercept[Warning] {
      listenerWrong.send(EmptyMessage)
    }
  }

  test("testing for supported messages") {
    class DummyListener extends Listener {
      def dependencies = Empty

      def receive = {
        case m: DummyMessage1 =>
      }
    }
    val l = new DummyListener
    assert(l supports DummyMessage1(5), "DummyMessage1 should be supported")
    assert(!(l supports DummyMessage2(5)), "DummyMessage2 should not be supported")
    assert(!(l supports EmptyMessage), "EmptyMessage should not be supported")
  }

  test("empty receiver") {
    class DummyListener extends Listener {
      def dependencies = Empty

      def receive = Empty
    }
    val l = new DummyListener
    l.send(DummyMessage1(0))
    l.send(EmptyMessage)
  }

  test("inline creation of listeners") {
    var lastMessage: Option[Message] = None
    val l = Listener {
      case m => lastMessage = Some(m)
    }
    expect(None) {
      lastMessage
    }
    l.send(DummyMessage1(55))
    expect(Some(DummyMessage1(55))) {
      lastMessage
    }
    l.send(DummyMessage1(66))
    expect(Some(DummyMessage1(66))) {
      lastMessage
    }
  }
}
