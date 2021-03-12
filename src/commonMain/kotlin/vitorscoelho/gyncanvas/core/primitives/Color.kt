package vitorscoelho.gyncanvas.core.primitives

interface Color {
    val red: Float
    val green: Float
    val blue: Float
    val alpha: Float
        get() = 1f
}