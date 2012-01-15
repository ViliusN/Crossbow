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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import org.scalatest.FunSuite

class HistoryAtTest extends FunSuite {
  test("name") {
    val target = new MutableIndicator[Double] with History {
      def name = "T"
      def dependencies = Empty
    }
    val historyAt = new HistoryAt(0, target)
    expect("HistoryAt(0; T)") { historyAt.name }
  }

  test("dependencies") {
    val target = new MutableIndicator[Double] with History {
      def name = "T"
      def dependencies = Empty
    }
    val historyAt = new HistoryAt(0, target)
    expect(Set(target)) { historyAt.dependencies }
  }

  test("calculation") {
    val target = new MutableIndicator[Int] with History {
      def name = "T"
      def dependencies = Empty
    }
    val historyAt = new HistoryAt(2, target)
    expect(None) { historyAt() }
    target.set(1)
    target.history.update()
    expect(None) { historyAt() }
    target.set(2)
    target.history.update()
    expect(None) { historyAt() }
    target.set(3)
    target.history.update()
    expect(Some(1)) { historyAt() }
    target.set(4)
    target.history.update()
    expect(Some(2)) { historyAt() }
    target.unset()
    target.history.update()
    expect(Some(3)) { historyAt() }
    target.history.update()
    expect(Some(4)) { historyAt() }
    target.history.update()
    expect(None) { historyAt() }
  }

  test("calculation - last value") {
    val target = new MutableIndicator[Int] with History {
      def name = "T"
      def dependencies = Empty
    }
    val historyAt = new HistoryAt(0, target)
    expect(None) { historyAt() }
    target.set(1)
    target.history.update()
    expect(Some(1)) { historyAt() }
    target.set(2)
    target.history.update()
    expect(Some(2)) { historyAt() }
    target.unset()
    target.history.update()
    expect(None) { historyAt() }
  }
}
