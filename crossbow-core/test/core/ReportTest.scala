/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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
import java.io._

class CsvHistoryReportTest extends FunSuite {
  test("Generate report") {
    class IndDouble extends Indicator[Double] {
      override def name = "Double Indicator"
      def dependencies = Empty
      def calculate = Empty
    }
    class IndInt extends Indicator[Int] {
      override def name = "Int Indicator"
      def dependencies = Empty
      def calculate = Empty
    }
    class IndCust extends Indicator[String] {
      override def name = "Custom Indicator"
      def dependencies = Empty
      def calculate = Empty
      override def valueToString(v: Option[String]): String =
        v map { "~~~"+_+"~~~" } getOrElse("###")
    }
    val id = new IndDouble with History
    val ii = new IndInt with History
    val ic = new IndCust with History
    val no = new IndInt

    expect("") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out))
      out.toString
    }
    expect("") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), no)
      out.toString
    }
    expect("Double Indicator,Int Indicator,Custom Indicator") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), id, ii, no, ic)
      out.toString
    }

    id.set(123.123)
    id.history.update()
    ii.set(999)
    ii.history.update()
    ic.set("first")
    ic.history.update()
    expect("Custom Indicator,Int Indicator,Custom Indicator,Double Indicator\n"+
           "~~~first~~~,999,~~~first~~~,123.123") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), ic, ii, ic, id, no)
      out.toString
    }

    id.history.update()
    ii.history.update()
    intercept[Exception] {
      CsvReport.History.generate(new BufferedWriter(new StringWriter), ic, ii)
    }
    intercept[Exception] {
      CsvReport.History.generate(new BufferedWriter(new StringWriter), id, ic)
    }
    ic.history.update()
    expect("Int Indicator,Double Indicator\n"+
           "999,123.123\n"+
           "999,123.123") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), ii, id)
      out.toString
    }

    ii.unset()
    ic.unset()
    id.unset()
    id.history.update()
    ii.history.update()
    ic.history.update()
    expect("Int Indicator,Custom Indicator,Double Indicator\n"+
           "999,~~~first~~~,123.123\n"+
           "999,~~~first~~~,123.123\n"+
           "N/A,###,N/A") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), ii, ic, id)
      out.toString
    }

    id.set(0.123)
    ii.set(55)
    ic.set("second")
    id.history.update()
    ii.history.update()
    ic.history.update()
    expect("Int Indicator,Custom Indicator,Double Indicator\n"+
           "999,~~~first~~~,123.123\n"+
           "999,~~~first~~~,123.123\n"+
           "N/A,###,N/A\n"+
           "55,~~~second~~~,0.123") {
      val out = new StringWriter
      CsvReport.History.generate(new BufferedWriter(out), ii, ic, id)
      out.toString
    }
  }
}

class CsvValuesReportTest extends FunSuite {
  test("Generate report") {
    class IndDouble extends Indicator[Double] {
      override def name = "Double Indicator"
      def dependencies = Empty
      def calculate = Empty
    }
    class IndInt extends Indicator[Int] {
      override def name = "Int Indicator"
      def dependencies = Empty
      def calculate = Empty
    }
    class IndCust extends Indicator[String] {
      override def name = "Custom Indicator"
      def dependencies = Empty
      def calculate = Empty
      override def valueToString(v: Option[String]): String =
        v map { "~~~"+_+"~~~" } getOrElse("###")
    }

    val id = new IndDouble
    val ii = new IndInt with History
    val ic = new IndCust
    expect("Double Indicator,N/A\n"+
           "Int Indicator,N/A\n"+
           "Custom Indicator,###") {
      val out = new StringWriter
      CsvReport.Values.generate(new BufferedWriter(out), id, ii, ic)
      out.toString
    }

    id.set(5.01)
    ic.set("Abc")
    expect("Custom Indicator,~~~Abc~~~\n"+
           "Double Indicator,5.01\n"+
           "Int Indicator,N/A\n"+
           "Custom Indicator,~~~Abc~~~") {
      val out = new StringWriter
      CsvReport.Values.generate(new BufferedWriter(out), ic, id, ii, ic)
      out.toString
    }

    ii.set(-88)
    id.unset()
    expect("Custom Indicator,~~~Abc~~~\n"+
           "Double Indicator,N/A\n"+
           "Int Indicator,-88\n"+
           "Custom Indicator,~~~Abc~~~") {
      val out = new StringWriter
      CsvReport.Values.generate(new BufferedWriter(out), ic, id, ii, ic)
      out.toString
    }
  }
}
