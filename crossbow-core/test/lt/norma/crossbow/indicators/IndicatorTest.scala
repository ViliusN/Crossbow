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
import org.joda.time.DateTime
import org.scalatest.FunSuite

class IndicatorTest extends FunSuite {
  test("value - defined") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = Some(5)
    }
    expect(5) {
      indicator.value
    }
  }

  test("value - undefined") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    intercept[Indicator.ValueNotSet] {
      indicator.value
    }
  }

  test("default value") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    intercept[Indicator.ValueNotSet] {
      indicator.default
    }
  }

  test("apply") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = Some(5)
    }
    expect(Some(5)) {
      indicator()
    }
  }

  test("isSet - defined") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = Some(5)
    }
    assert(indicator.isSet)
  }

  test("isSet - undefined") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    assert(!indicator.isSet)
  }

  test("valueToString") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect("5") {
      indicator.valueToString(5)
    }
  }

  test("valueToString - defined optional value") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect("5") {
      indicator.valueToString(Some(5))
    }
  }

  test("valueToString - undefined optional value") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect(indicator.valueNotSetString) {
      indicator.valueToString(None)
    }
  }

  test("valueNotSetString") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect("N/A") {
      indicator.valueNotSetString
    }
  }

  test("valueToString - defined current value") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = Some(88)
    }
    expect("88") {
      indicator.valueToString
    }
  }

  test("valueToString - undefined current value") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect(indicator.valueNotSetString) {
      indicator.valueToString
    }
  }

  test("toString") {
    val indicator = new Indicator[Int] {
      def name = "ABC"

      def dependencies = Empty

      def optionalValue = Some(123)
    }
    expect("ABC: 123") {
      indicator.toString
    }
  }

  test("history - with history") {
    val indicator = new Indicator[Int] with History {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    assert(indicator.history != null)
  }

  test("history - without history") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    intercept[Exception] {
      indicator.history
    }
  }

  test("hasHistory - with history") {
    val indicator = new Indicator[Int] with History {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    assert(indicator.hasHistory)
  }

  test("hasHistory - without history") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    assert(!indicator.hasHistory)
  }

  test("requiredHistory") {
    val indicator = new Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    expect(0) {
      indicator.requiredHistory
    }
  }
}
