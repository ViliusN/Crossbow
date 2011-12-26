package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._
import org.scalatest.FunSuite
import org.joda.time.{ DateMidnight, DateTime }

class OptionsChainTest extends FunSuite {
  test("name") {
    val underlying = new Instrument {
      def currency = "A"
      def exchange = Nasdaq
      override def toString = "A"
    }
    val dataNode = new DataNode {
      def dependencies = Empty
      def receive = { case _ => }
    }
    val chain = new OptionsChain(underlying, dataNode)
    expect("Options chain for A") { chain.name }
  }
  test("dependencies") {
    val underlying = new Instrument {
      def currency = "A"
      def exchange = Nasdaq
      override def toString = "A"
    }
    val dataNode = new DataNode {
      def dependencies = Empty
      def receive = { case _ => }
    }
    val chain = new OptionsChain(underlying, dataNode)
    expect(Set.empty) { chain.dependencies }
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
        case request @ OptionsLookupRequest(`underlying`) =>
          dispatch(LookupResult(request, List(DemoOption(underlying, 1), DemoOption(underlying, 2),
            DemoOption(underlying, 3))))
      }
    }
    val chain = new OptionsChain(underlying, dataNode)
    val list = new IndicatorList(chain)
    dataNode.add(list)
    expect(None) { chain() }
    list.send(new SessionOpen(new DateTime()))
    expect(Some(List(DemoOption(underlying, 1), DemoOption(underlying, 2),
      DemoOption(underlying, 3)))) { chain() }
  }
  test("options look-up requests should be supported by the provider") {
    val underlying = new Instrument { def currency = "A"; def exchange = Nasdaq }
    val dataNode = new DataNode {
      def dependencies = Empty
      def receive = {
        case _ if(false) =>
      }
    }
    intercept[Exception] { new OptionsChain(underlying, dataNode) }
  }
}
