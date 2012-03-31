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

import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{Indicator, MutableIndicator}

class MutableIndicatorTest extends FunSuite {
  test("initial value") {
    val indicator = new MutableIndicator[String] {
      val name = ""
      val dependencies = Set[Indicator[_]]()
    }
    expect(None) {
      indicator()
    }
  }

  test("set") {
    val indicator = new MutableIndicator[String] {
      val name = ""
      val dependencies = Set[Indicator[_]]()
    }
    indicator.set("ABC")
    expect(Some("ABC")) {
      indicator()
    }
  }

  test("set - optional value") {
    val indicator = new MutableIndicator[String] {
      val name = ""
      val dependencies = Set[Indicator[_]]()
    }
    indicator.set(Some("ABC"))
    expect(Some("ABC")) {
      indicator()
    }
  }

  test("unSet") {
    val indicator = new MutableIndicator[String] {
      val name = ""
      val dependencies = Set[Indicator[_]]()
      set("ABC")
    }
    indicator.unset()
    expect(None) {
      indicator()
    }
  }
}
