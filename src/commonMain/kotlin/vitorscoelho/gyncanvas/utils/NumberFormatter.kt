package vitorscoelho.gyncanvas.utils

import kotlin.math.round
import kotlin.math.absoluteValue
import kotlin.math.sign

internal enum class Notation {
    DECIMAL,
    SCIENTIFIC,
    ENGINEERING,
    ;
}

internal expect class DecimalFormat(
    notation: Notation,
    suppressLeadingZeros: Boolean,
    suppressTrailingZeros: Boolean,
    precision: Int,
    decimalSeparator: Char,
) {
    fun format(valor: Double): String
}

internal class DecimalFormatSymbols(val decimalSeparator: Char)

class NumberFormatter(
    suppressLeadingZeros: Boolean,
    suppressTrailingZeros: Boolean,
    precision: Int,
    decimalSeparator: Char,
    private val prefix: String,
    private val suffix: String,
    private val roundoff: Double
) {
    private val dc: DecimalFormat = DecimalFormat(
        notation = Notation.DECIMAL,//TODO mudar para permitir outras formas
        suppressLeadingZeros = suppressLeadingZeros,
        suppressTrailingZeros = suppressTrailingZeros,
        precision = precision,
        decimalSeparator = decimalSeparator
    )

    private fun roundoff(valor: Double): Double {
        if (roundoff == 0.0) return valor
        return roundoff * round(valor.absoluteValue / roundoff) * valor.sign
    }

    fun format(valor: Double): String = "$prefix${dc.format(roundoff(valor))}$suffix"
}