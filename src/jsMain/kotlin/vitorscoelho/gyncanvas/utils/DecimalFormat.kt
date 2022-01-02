package vitorscoelho.gyncanvas.utils

//internal actual class DecimalFormat actual constructor(
//    actual val pattern: String,
//    actual val decimalFormatSymbols: DecimalFormatSymbols
//) {
//    actual fun format(value: Double): String {
//        val number = 10.56
//        val formatter = js("new Intl.NumberFormat('en-US', { useGrouping: false })")
//        val formattedNumber = formatter.format(number)
//        TODO("Not yet implemented")
//    }
//}

//internal actual class DecimalFormat actual constructor(
//    notation: Notation,//
//    private val suppressLeadingZeros: Boolean,
//    suppressTrailingZeros: Boolean,//
//    precision: Int,//
//    private val decimalSeparator: Char,
//) {
//    private val jsFormatter: dynamic
//
//    init {
//        val stringNotation: String = when (notation) {
//            Notation.DECIMAL -> "standard"
//            Notation.ENGINEERING -> "engineering"
//            Notation.SCIENTIFIC -> "scientific"
//        }
//        val maximumFractionDigits = precision
//        val minimumFractionDigits = if (suppressTrailingZeros) 0 else precision
//        this.jsFormatter =
//            js("new Intl.NumberFormat('en-US', { useGrouping: false,notation: stringNotation, maximumFractionDigits: maximumFractionDigits, minimumFractionDigits: minimumFractionDigits})")
//    }
//
//    actual fun format(valor: Double): String {
//        var valorFormatado = (this.jsFormatter.format(valor) as String)
//        if (suppressLeadingZeros) {
//            valorFormatado = when {
//                valorFormatado == "0" -> valorFormatado
//                valorFormatado.startsWith("-0") -> valorFormatado.removeRange(1, 2)
//                else -> valorFormatado.removePrefix("0")
//            }
//        }
//        return valorFormatado.replace('.', decimalSeparator)
//    }
//}