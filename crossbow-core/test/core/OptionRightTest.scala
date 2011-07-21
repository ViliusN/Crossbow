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

class OptionRightTest extends FunSuite {
  import OptionRight._
  test("invert") {
    expect(Put) { Call.invert }
    expect(Call) { Put.invert }
    expect(Call) { Call.invert.invert }
    expect(Put) { Put.invert.invert }
  }
  test("parse") {
    expect(Call) { parse("C") }
    expect(Call) { parse("c") }
    expect(Call) { parse("Call") }
    expect(Call) { parse("call") }
    expect(Call) { parse("CALL") }
    expect(Put) { parse("P") }
    expect(Put) { parse("p") }
    expect(Put) { parse("Put") }
    expect(Put) { parse("put") }
    expect(Put) { parse("PUT") }
    intercept[Exception] { parse("ABC") }
    intercept[Exception] { parse("ca") }
    intercept[Exception] { parse("pu") }
  }
  test("toString") {
    expect("Call") { Call.toString }
    expect("Put") { Put.toString }
  }
}
