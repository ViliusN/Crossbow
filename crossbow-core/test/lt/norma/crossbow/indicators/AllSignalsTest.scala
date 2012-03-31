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

package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.messages.EmptyMessage
import org.scalatest.FunSuite

class AllSignalsTest extends FunSuite {

  import Direction._

  test("name - multiple targets") {
    val target1 = new VariableSignal {
      override def name = "T1"
    }
    val target2 = new VariableSignal {
      override def name = "T2"
    }
    val target3 = new VariableSignal {
      override def name = "T3"
    }
    val signal = new AllSignals(target1, target2, target3)
    expect("AllSignals(T1; T2; T3)") {
      signal.name
    }
  }
  test("name - one target") {
    val target = new VariableSignal {
      override def name = "T1"
    }
    val signal = new AllSignals(target)
    expect("AllSignals(T1)") {
      signal.name
    }
  }
  test("name - no targets") {
    val signal = new AllSignals()
    expect("AllSignals()") {
      signal.name
    }
  }

  test("dependencies - multiple targets") {
    val target1 = new VariableSignal
    val target2 = new VariableSignal
    val target3 = new VariableSignal
    val signal = new AllSignals(target1, target2, target3)
    expect(Set(target1, target2, target3)) {
      signal.dependencies
    }
  }
  test("dependencies - one target") {
    val target1 = new VariableSignal
    val signal = new AllSignals(target1)
    expect(Set(target1)) {
      signal.dependencies
    }
  }
  test("dependencies - no targets") {
    val signal = new AllSignals()
    expect(Set()) {
      signal.dependencies
    }
  }

  test("initial value - multiple targets") {
    val target1 = new VariableSignal(Long)
    val target2 = new VariableSignal(Long)
    val target3 = new VariableSignal(Long)
    val signal = new AllSignals(target1, target2, target3)
    val list = new IndicatorList(signal)
    expect(Long) {
      signal.value
    }
  }
  test("initial value - multiple targets, differing values") {
    val target1 = new VariableSignal(Short)
    val target2 = new VariableSignal(Long)
    val target3 = new VariableSignal
    val signal = new AllSignals(target1, target2, target3)
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }
  test("initial value - multiple targets, some empty") {
    val target1 = new VariableSignal(Short)
    val target2 = new VariableSignal(Long)
    val target3 = new VariableSignal
    val signal = new AllSignals(target1, target2, target3)
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }
  test("initial value - multiple empty targets") {
    val target1 = new VariableSignal
    val target2 = new VariableSignal
    val target3 = new VariableSignal
    val signal = new AllSignals(target1, target2, target3)
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }
  test("initial value - one target") {
    val target = new VariableSignal(Long)
    val signal = new AllSignals(target)
    val list = new IndicatorList(signal)
    expect(Long) {
      signal.value
    }
  }
  test("initial value - one empty target") {
    val target = new VariableSignal
    val signal = new AllSignals(target)
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }
  test("initial value - no targets") {
    val signal = new AllSignals()
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }

  test("sending data - multiple targets") {
    val target1 = new VariableSignal
    val target2 = new VariableSignal
    val target3 = new VariableSignal
    val signal = new AllSignals(target1, target2, target3)
    val list = new IndicatorList(signal)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target1.set(Long)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target2.set(Long)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target3.set(Long)
    list.send(EmptyMessage)
    expect(Some(Long)) {
      signal()
    }
    target1.set(Short)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target2.set(Short)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target3.set(Short)
    list.send(EmptyMessage)
    expect(Some(Short)) {
      signal()
    }
    target1.unset()
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target2.unset()
    target3.unset()
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
  }
  test("sending data - one target") {
    val target = new VariableSignal
    val signal = new AllSignals(target)
    val list = new IndicatorList(signal)
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
    target.set(Long)
    list.send(EmptyMessage)
    expect(Some(Long)) {
      signal()
    }
    target.set(Short)
    list.send(EmptyMessage)
    expect(Some(Short)) {
      signal()
    }
    target.unset()
    list.send(EmptyMessage)
    expect(None) {
      signal()
    }
  }
  test("sending data - no targets") {
    val signal = new AllSignals()
    val list = new IndicatorList(signal)
    expect(None) {
      signal()
    }
  }
}
