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

import lt.norma.crossbow.core.Direction._
import org.scalatest.FunSuite

class SignalTest extends FunSuite {
  test("isLong - long") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Long)
    }
    assert(signal.isLong)
  }

  test("isLong - short") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Short)
    }
    assert(!signal.isLong)
  }

  test("isLong - undefined") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = None
    }
    assert(!signal.isLong)
  }

  test("isShort - long") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Long)
    }
    assert(!signal.isShort)
  }

  test("isShort - short") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Short)
    }
    assert(signal.isShort)
  }

  test("isShort- undefined") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = None
    }
    assert(!signal.isShort)
  }

  test("isFlat - long") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Long)
    }
    assert(!signal.isFlat)
  }

  test("isFlat - short") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = Some(Short)
    }
    assert(!signal.isFlat)
  }

  test("isFlat- undefined") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = None
    }
    assert(signal.isFlat)
  }

  test("valueNotSetString") {
    val signal = new Signal {
      def name = ""

      def dependencies = Set[Indicator[_]]()

      def optionalValue = None
    }
    expect("Flat") {
      signal.valueNotSetString
    }
  }
}
