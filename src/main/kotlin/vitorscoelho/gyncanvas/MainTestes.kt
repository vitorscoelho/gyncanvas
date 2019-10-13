package vitorscoelho.gyncanvas

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun main() {
    val leading = true //Se o número for menor do que zero, mostra o zero antes da vírgula
    val supress = true //Suprime os zeros a direita da vírgula
    val precision = 1
    val prefix = "prefix "
    val sufix = " suffix"

    val caracterDepoisDaVirgula = if (supress) "0".repeat(precision) else "#".repeat(precision)
    val caracterAntesDaVirgula = if (leading) "0" else "#"

    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'
//    val decimalFormatPattern = "#.0000000000"
    val decimalFormatPattern = "$prefix$caracterAntesDaVirgula.$caracterDepoisDaVirgula$sufix"
    val decimalFormat = DecimalFormat(decimalFormatPattern)
    decimalFormat.decimalFormatSymbols = decimalFormatSymbols
    println(decimalFormat.format(123456789.123456789))
    println(decimalFormat.format(.123456789))

    val teste: Double? = 3.0
    val teste2: Double = teste ?: 2.0
    print(teste2)
}