package vitorscoelho.gyncanvas.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.lang.Math.round
import kotlin.math.absoluteValue
import kotlin.math.sign

class NumberFormatter(
    suppressLeadingZeros: Boolean,
    suppressTrailingZeros: Boolean,
    precision: Int,
    decimalSeparator: Char,
    private val prefix: String,
    private val suffix: String,
    private val roundoff: Double
) {
    private val dc: DecimalFormat

    init {
        val decimalFormatPatternRight = if (suppressTrailingZeros) "#".repeat(precision) else "0".repeat(precision)
        val decimalFormatPatternLeft = if (suppressLeadingZeros) "0" else "#"
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.decimalSeparator = decimalSeparator
        val decimalFormatPattern = "$decimalFormatPatternLeft.$decimalFormatPatternRight"
        this.dc = DecimalFormat(decimalFormatPattern)
        this.dc.decimalFormatSymbols = decimalFormatSymbols
    }

    private fun roundoff(valor: Double):Double{
        if (roundoff==0.0) return valor
        return roundoff * round(valor.absoluteValue / roundoff) * valor.sign
    }

    fun format(valor: Double): String = "$prefix${dc.format(roundoff(valor))}$suffix"
}