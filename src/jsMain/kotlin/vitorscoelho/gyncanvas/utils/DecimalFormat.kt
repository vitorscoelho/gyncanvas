package vitorscoelho.gyncanvas.utils

internal actual class DecimalFormat actual constructor(
    actual val pattern: String,
    actual val decimalFormatSymbols: DecimalFormatSymbols
) {
    actual fun format(value: Double): String {
        TODO("Not yet implemented")
    }
}