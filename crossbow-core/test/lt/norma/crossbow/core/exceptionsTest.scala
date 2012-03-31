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

class exceptionsTest extends FunSuite {
  /*test("Exception") {
    expect("123") { Exception("123").getMessage }
    expect(null) { Exception("123").getCause }
    expect("abc") { Exception("abc", Exception("ccc")).getMessage }
    expect(Exception("ccc")) { Exception("abc", Exception("ccc")).getCause }
  }

  test("throwing - without cause") {
    try {
      throw Exception("eee")
      fail("should throw an exception")
    } catch {
      case Exception("eee", null) => // OK
      case _ => fail("incorrect exception thrown")
    }
    intercept[Exception] { throw Exception("eee") }
  }

  test("throwing - with cause") {
    try {
      throw Exception("eee", Exception("cause"))
      fail("should throw an exception")
    } catch {
      case Exception("eee", Exception("cause", null)) => // OK
      case _ => fail("incorrect exception thrown")
    }
    intercept[Exception] { throw Exception("eee", Exception("cause")) }
  }

  test("NotSupported") {
    try {
      throw Exception.NotSupported("aaa")
      fail("should throw an exception")
    } catch {
      case Exception.NotSupported("aaa") => // OK
      case _ => fail("incorrect exception thrown")
    }
    intercept[Exception.NotSupported] { throw Exception.NotSupported("aaa") }
  }*/
}

// TODO
