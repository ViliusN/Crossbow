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

import org.scalatest.FunSuite

class ListenerTest extends FunSuite {
  case class DummyMessage1(value: Int) extends Message
  case class DummyMessage2(value: Int) extends Message

  test("creation of listeners") {
    val bl = new BasicListener { def dependencies = Empty; def receive = Empty }
    assert ( !(bl.isInstanceOf[Dependant[_]]), "BasicListener should not extend Dependant" )
    val l = new Listener { def dependencies = Empty; def receive = Empty }
    assert ( l.isInstanceOf[Dependant[_]], "Listener should extend Dependant" )
  }
  test("receiving a message") {
    class DummyListener extends Listener {
      def dependencies = Empty
      var lastMessage: Option[Message] = None
      def receive = { case m: DummyMessage1 => lastMessage = Some(m) }
    }
    val l = new DummyListener
    expect(None) { l.lastMessage }
    val m1 = DummyMessage1(111)
    l.send(m1)
    expect(Some(m1)) { l.lastMessage }
    val m2 = DummyMessage2(222)
    l.send(m2)
    expect(Some(m1)) { l.lastMessage }
    l.send(EmptyMessage)
    expect(Some(m1)) { l.lastMessage }
  }
  test("testing for supported messages") {
    class DummyListener extends Listener {
      def dependencies = Empty
      def receive = { case m: DummyMessage1 => }
    }
    val l = new DummyListener
    assert ( l supports DummyMessage1(5), "DummyMessage1 should be supported" )
    assert ( !(l supports DummyMessage2(5)), "DummyMessage2 should not be supported" )
    assert ( !(l supports EmptyMessage), "EmptyMessage should not be supported" )
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
    val l = Listener { case m => lastMessage = Some(m) }
    expect(None) { lastMessage }
    l.send(DummyMessage1(55))
    expect(Some(DummyMessage1(55))) { lastMessage }
    l.send(DummyMessage1(66))
    expect(Some(DummyMessage1(66))) { lastMessage }
  }
}
