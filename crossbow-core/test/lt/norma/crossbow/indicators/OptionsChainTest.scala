package lt.norma.crossbow.indicators

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

import lt.norma.crossbow.core._
import lt.norma.crossbow.core.Exchange._
import lt.norma.crossbow.messages._
import org.scalatest.FunSuite
import org.joda.time.{DateMidnight, DateTime}
import lt.norma.crossbow.indicators.OptionsChain
import lt.norma.crossbow.messages.SessionOpen

class OptionsChainTest extends FunSuite {
  test("name") {
    val underlying = new Instrument {
      def currency = "A"

      def exchange = Nasdaq

      override def toString = "A"
    }
    val dataNode = new DataNode {
      def dependencies = Empty

      def receive = {
        case _ =>
      }
    }
    val chain = new OptionsChain(underlying, dataNode)
    expect("Options chain for A") {
      chain.name
    }
  }
  test("dependencies") {
    val underlying = new Instrument {
      def currency = "A"

      def exchange = Nasdaq

      override def toString = "A"
    }
    val dataNode = new DataNode {
      def dependencies = Empty

      def receive = {
        case _ =>
      }
    }
    val chain = new OptionsChain(underlying, dataNode)
    expect(Set.empty) {
      chain.dependencies
    }
  }
  test("data request") {
    case class DemoOption(underlying: Instrument, id: Int) extends OptionInstrument {
      def strike = 0

      def invert = null

      def right = OptionRight.Call

      def expiration = new DateMidnight(0)

      def currency = "B"

      def exchange = Cboe
    }
    val underlying = new Instrument {
      def currency = "A"

      def exchange = Nasdaq

      override def toString = "A"
    }
    val dataNode = new DataNode {
      def dependencies = Empty

      def receive = {
        case request@OptionsLookupRequest(`underlying`) =>
          dispatch(LookupResult(request, List(DemoOption(underlying, 1), DemoOption(underlying, 2),
            DemoOption(underlying, 3))))
      }
    }
    val chain = new OptionsChain(underlying, dataNode)
    val list = new IndicatorList(chain)
    dataNode.add(list)
    expect(None) {
      chain()
    }
    list.send(new SessionOpen(new DateTime()))
    expect(Some(List(DemoOption(underlying, 1), DemoOption(underlying, 2),
      DemoOption(underlying, 3)))) {
      chain()
    }
  }
  test("options look-up requests should be supported by the provider") {
    val underlying = new Instrument {
      def currency = "A";

      def exchange = Nasdaq
    }
    val dataNode = new DataNode {
      def dependencies = Empty

      def receive = {
        case _ if (false) =>
      }
    }
    intercept[Exception] {
      new OptionsChain(underlying, dataNode)
    }
  }
}
