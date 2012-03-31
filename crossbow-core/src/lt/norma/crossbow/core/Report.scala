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

import java.io.{ BufferedWriter, FileWriter }
import lt.norma.crossbow.indicators.{ Indicator, History }

/** Extend this trait to create custom report generators. */
trait Report {
  def generate(writer: BufferedWriter, indicators: Indicator[_]*)

  def generate(fileName: String, indicators: Indicator[_]*) {
    generate(new BufferedWriter(new FileWriter(fileName)), indicators: _*)
  }
}

/** Extend this trait to create custom history report generators. */
trait HistoryReport {
  def generate(writer: BufferedWriter, indicators: Indicator[_] with History*)

  def generate(fileName: String, indicators: Indicator[_] with History*) {
    generate(new BufferedWriter(new FileWriter(fileName)), indicators: _*)
  }
}

object CsvReport {
  val delimiter = ","

  /** Writes values of the specified indicators to the specified buffer. */
  object Values extends Report {
    def generate(writer: BufferedWriter, indicators: Indicator[_]*) {
      try {
        writer.write(indicators.map(i => i.name + delimiter + i.valueToString).mkString("\n"))
      } finally {
        writer.close()
      }
    }
  }

  /** Writes historical values of the specified indicators to the specified buffer. Only indicators
    * marked by `History` trait will be included into report. If indicators' history sizes do not
    * match an exception is thrown. */
  object History extends HistoryReport {
    def generate(writer: BufferedWriter, indicators: Indicator[_] with History*) {
      if (indicators.size > 0) {
        val firstSize = indicators.head.history.size
        if (indicators.exists(_.history.size != firstSize)) {
          throw Exception("History sizes of all indicators must be the same")
        }
        try {
          // Write header line
          writer.write(indicators.map(_.name).mkString(delimiter) + "\n")
          // Write data lines
          writer.write(indicators.map(_.history.valuesToStrings).transpose
            .map(_.mkString(delimiter)).mkString("\n"))
        } finally {
          writer.close()
        }
      }
    }
  }
}
