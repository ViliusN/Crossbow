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
import lt.norma.crossbow.messages._
import org.scalatest.FunSuite

class FirstValueTest extends FunSuite {
  test("name") {
    val target = new Variable[Int] {
      override def name = "T"
    }
    val indicator = new FirstValue(target)
    expect("First value of T") {
      indicator.name
    }
  }

  test("dependency") {
    val target = new Variable[Int] {
      override def name = "T"
    }
    val indicator = new FirstValue(target)
    expect(2) {
      indicator.dependencies.size
    }
  }

  test("value - Int") {
    val target = new Variable[Int] {
      override def name = "T"
    }
    val indicator = new FirstValue(target)
    val list = new IndicatorList(indicator)
    expect(None) {
      indicator()
    }
    list.send(EmptyMessage)
    expect(None) {
      indicator()
    }
    target.set(5)
    list.send(EmptyMessage)
    expect(Some(5)) {
      indicator()
    }
    target.set(8)
    list.send(EmptyMessage)
    expect(Some(5)) {
      indicator()
    }
    target.set(10)
    list.send(EmptyMessage)
    expect(Some(5)) {
      indicator()
    }
    target.unset()
    list.send(EmptyMessage)
    expect(Some(5)) {
      indicator()
    }
    target.set(1)
    list.send(EmptyMessage)
    expect(Some(5)) {
      indicator()
    }
  }

  test("value - String") {
    val target = new Variable[String] {
      override def name = "T"
    }
    val indicator = new FirstValue(target)
    val list = new IndicatorList(indicator)
    expect(None) {
      indicator()
    }
    list.send(EmptyMessage)
    expect(None) {
      indicator()
    }
    target.set("A")
    list.send(EmptyMessage)
    expect(Some("A")) {
      indicator()
    }
    target.set("B")
    list.send(EmptyMessage)
    expect(Some("A")) {
      indicator()
    }
    target.set("C")
    list.send(EmptyMessage)
    expect(Some("A")) {
      indicator()
    }
    target.unset()
    list.send(EmptyMessage)
    expect(Some("A")) {
      indicator()
    }
    target.set("D")
    list.send(EmptyMessage)
    expect(Some("A")) {
      indicator()
    }
  }

  test("initial value") {
    val target = new Variable(55) {
      override def name = "T"
    }
    val indicator = new FirstValue(target)
    val list = new IndicatorList(indicator)
    expect(Some(55)) {
      indicator()
    }
    target.set(8)
    list.send(EmptyMessage)
    expect(Some(55)) {
      indicator()
    }
  }
}
