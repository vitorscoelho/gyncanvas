package vitorscoelho.gyncanvas.core.primitives

interface Color {
    val red: Short
    val green: Short
    val blue: Short
    val alpha: Float
        get() = 1f
}