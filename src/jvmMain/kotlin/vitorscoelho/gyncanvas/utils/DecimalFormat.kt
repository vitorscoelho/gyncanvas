package vitorscoelho.gyncanvas.utils

internal actual class DecimalFormat actual constructor(
    actual val pattern: String,
    actual val decimalFormatSymbols: DecimalFormatSymbols
) {
    private val jvmDecimalFormat: java.text.DecimalFormat

    init {
        val jvmDecimalFormatSymbols = java.text.DecimalFormatSymbols()
        jvmDecimalFormatSymbols.decimalSeparator = decimalFormatSymbols.decimalSeparator
        this.jvmDecimalFormat = java.text.DecimalFormat(pattern)
        this.jvmDecimalFormat.decimalFormatSymbols = jvmDecimalFormatSymbols
    }

    actual fun format(value: Double): String = jvmDecimalFormat.format(value)
}