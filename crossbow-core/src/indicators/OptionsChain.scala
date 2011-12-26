package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._

/** Requests options chain at the opening of trading session. */
class OptionsChain(underlying: Instrument, dataProvider: DataNode)
    extends Indicator[List[OptionInstrument]] {
  def name = "Options chain for "+underlying
  def dependencies = Empty
  def calculate = {
    case SessionOpen(time) =>
      dataProvider.send(OptionsLookupRequest(underlying))
      optionalValue
    case LookupResult(OptionsLookupRequest(`underlying`), chain) =>
      chain.map(_.asInstanceOf[OptionInstrument])
  }
  // Check if options look-up requests are supported by the specified provider
  if(!dataProvider.supports(OptionsLookupRequest(underlying))) {
    throw Exception("Data provider doesn't support options look-up requests")
  }
}
