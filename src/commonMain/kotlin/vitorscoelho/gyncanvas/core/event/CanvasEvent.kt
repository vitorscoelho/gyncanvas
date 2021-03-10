package vitorscoelho.gyncanvas.core.event

interface CanvasEvent

interface CanvasMouseEvent : CanvasEvent {
    val x: Float
    val y: Float
    val button: CanvasMouseButton
}

interface CanvasScrollEvent : CanvasEvent {
    val x: Float
    val y: Float
    val deltaY: Double
}