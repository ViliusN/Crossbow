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

/** Stores first known value of the target indicator. */
class FirstValue[Value : Manifest](target: Indicator[Value]) extends Indicator[Value] {
  def name = "First value of "+target.name
  def dependencies = Set(target)
  def calculate = { case _ if(isEmpty && target.isSet) => target() }
}
/*
class Normalized(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "Normalized("+indicator.name+")"
  val firstValue = new Variable[Double]
}

object SpreadNormalization extends App {
  val dataDirectory = "/home/vilius/work/trading/data/options-15-minute/data/"
  val outputDirectory = "research/output/"
  val startDate = new DateMidnight(2010, 1, 1)
  val endDate = new DateMidnight(2015, 1, 10)

  class Study(val symbol: String, initialPrice: Double) {
    val stock = Stock(symbol, Nyse, "USD")

    // Indicators
    val stockQuote = new LastQuote(stock)
    val stockAsk = new Ask(stockQuote)
    val stockBid = new Bid(stockQuote)
    val stockMidprice = new MidPrice(stockAsk, stockBid)
    val stockSpread = new Spread(stockAsk, stockBid)
      with History { override def name = symbol+" spread"}
    val stockSpreadPct = new Multiply(new Divide(stockSpread, stockMidprice), 100)
      with History { override def name = symbol+" spread pct" }
    val normPrice = new Indicator[Double] { // TODO move to core
      def name = "Norm "+symbol
      val firstPrice = new Variable[Double]
      def dependencies = Set(stockMidprice, firstPrice)
      def calculate = {
        case _ if(firstPrice.isEmpty && stockMidprice.isSet) =>
          firstPrice.set(stockMidprice())
          0
        case _ if(firstPrice.isSet && stockMidprice.isSet) =>
          (stockMidprice.value / firstPrice.value - 1) * 100
      }
    }*/
