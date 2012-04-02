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

package lt.norma.crossbow.ib

import com.ib.client._

trait IbListener extends EWrapper {
  // Connection and Server
  def currentTime(time: Long) { }
  def error(id: Int, errorCode: Int, errorMsg: String) { }
  def error(exception: java.lang.Exception) { }
  def error(string: String) { }
  def connectionClosed() { }

  // Market Data
  def tickPrice(tickerId: Int, field: Int, price: Double, canAutoExecute: Int) { }
  def tickSize(tickerId: Int, field: Int, size: Int) { }
  def tickOptionComputation(tickerId: Int, field: Int, impliedVol: Double, delta: Double,
    optPrice: Double, pvDividend: Double, gamma: Double, vega: Double, theta: Double,
    undPrice: Double) { }
  def tickGeneric(tickerId: Int, tickType: Int, value: Double) { }
  def tickString(tickerId: Int, tickType: Int, value: String) { }
  def tickEFP(tickerId: Int, tickType: Int, basisPoints: Double, formattedBasisPoints: String,
    impliedFuture: Double, holdDays: Int, futureExpiry: String, dividendImpact: Double,
    dividendsToExpiry: Double) { }
  def tickSnapshotEnd(reqId: Int) { }
  def deltaNeutralValidation(reqId: Int, underComp: UnderComp) { }

  // Orders
  def orderStatus(orderId: Int, status: String, filled: Int, remaining: Int, avgFillPrice: Double,
    permId: Int, parentId: Int, lastFillPrice: Double, clientId: Int, whyHeld: String) { }
  def openOrder(orderId: Int, contract: Contract, order: Order, orderState: OrderState) { }
  def nextValidId(orderId: Int) { }
  def openOrderEnd() { }

  // Account and Portfolio
  def updateAccountValue(key: String, value: String, currency: String, accountName: String) { }
  def updatePortfolio(contract: Contract, position: Int, marketPrice: Double, marketValue: Double,
    averageCost: Double, unrealizedPNL: Double, realizedPNL: Double, accountName: String) { }
  def updateAccountTime(timeStamp: String) { }
  def accountDownloadEnd(accountName: String) { }

  // Contract Details
  def contractDetails(reqId: Int, contractDetails: ContractDetails) { }
  def contractDetailsEnd(reqId: Int) { }
  def bondContractDetails(reqId: Int, contractDetails: ContractDetails) { }

  // Executions
  def execDetails(reqId: Int, contract: Contract, execution: Execution) { }
  def execDetailsEnd(reqId: Int) { }

  // Market Depth
  def updateMktDepth(tickerId: Int, position: Int, operation: Int, side: Int, price: Double,
    size: Int) { }
  def updateMktDepthL2(tickerId: Int, position: Int, marketMaker: String, operation: Int, side: Int,
    price: Double, size: Int) { }

  // News Bulletins
  def updateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String) { }

  // Financial Advisors
  def managedAccounts(accountsList: String) { }
  def receiveFA(faDataType: Int, xml: String) { }

  // Historical Data
  def historicalData(reqId: Int, date: String, open: Double, high: Double, low: Double,
    close: Double, volume: Int, count: Int, WAP: Double, hasGaps: Boolean) { }

  // Market Scanners
  def scannerParameters(xml: String) { }
  def scannerData(reqId: Int, rank: Int, contractDetails: ContractDetails, distance: String,
    benchmark: String, projection: String, legsStr: String) { }
  def scannerDataEnd(reqId: Int) { }

  // Real Time Bars
  def realtimeBar(reqId: Int, time: Long, open: Double, high: Double, low: Double, close: Double,
    volume: Long, wap: Double, count: Int) { }

  // Fundamental Data
  def fundamentalData(reqId: Int, data: String) { }
}
