package vitorscoelho.gyncanvas.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode

class NumberFormatter(
    private val suppressLeadingZeros: Boolean,
    private val suppressTrailingZeros: Boolean,
    private val precision: Int,
    private val decimalSeparator: Char,
    private val prefix: String,
    private val suffix: String,
    private val roundoff: Double
) {

    init {
        require(precision >= 0)
        require(roundoff >= 0.0)
    }

    private fun BigDecimal.arredondar(casas: Int): BigDecimal {
        return this.roundToDigitPositionAfterDecimalPoint(
            digitPosition = casas.toLong(),
            roundingMode = RoundingMode.ROUND_HALF_AWAY_FROM_ZERO
        )
    }

    private fun roundoff(value: Double, roundoff: Double, precision: Int): String {
        val bdValue = BigDecimal.fromDouble(value)
        val bdRoundoff = BigDecimal.fromDouble(roundoff)

        val retorno: BigDecimal =
            if (roundoff == 0.0) {
                var valor = bdValue
                valor = valor.arredondar(casas = precision)
                valor
            } else {
                var valor = bdValue
                valor = valor.divide(bdRoundoff)
                valor = valor.arredondar(casas = 0)
                valor = valor.multiply(bdRoundoff)
                valor = valor.arredondar(casas = precision)
                valor
            }
        return retorno.toPlainString()
    }

    fun format(valor: Double): String {
        val arredondado = roundoff(
            value = valor,
            roundoff = roundoff,
            precision = precision
        ).replace(oldChar = '.', newChar = decimalSeparator)

        val numeroFormatado = buildString {
            append(arredondado)
            if (arredondado.first() == '0' && suppressLeadingZeros) deleteAt(index = 0)
            if (arredondado.startsWith("-0") && suppressLeadingZeros) deleteAt(index = 1)
            if (!suppressTrailingZeros) {
                if (!contains(char = decimalSeparator)) append(decimalSeparator)
                val stringDepoisDaVirgula = arredondado.substringAfterLast(
                    delimiter = decimalSeparator,
                    missingDelimiterValue = ""
                )
                val zerosADireitaQueFaltam = (precision - stringDepoisDaVirgula.length)
                if (zerosADireitaQueFaltam > 0) repeat(zerosADireitaQueFaltam) { append("0") }
            }
            if (isEmpty()) append("0")
        }

        return "$prefix$numeroFormatado$suffix"
    }
}