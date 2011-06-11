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
import org.scalatest.FunSuite

class InstrumentWrapperTest extends FunSuite {
  test("InstrumentWrapper") {
    val s1 = new Stock("AA", Exchange.nasdaq, "USD")
    val s2 = new Stock("AA", Exchange.nyse, "USD")
    val i = new InstrumentWrapper(s1)

    expect("Instrument") { i.name }
    expect(Set.empty) { i.dependencies }
    expect(s1) { i.value }
    i.set(s2)
    expect(s2) { i.value }
  }
}
