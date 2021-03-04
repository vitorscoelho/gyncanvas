package vitorscoelho.gyncanvas.core.event

interface CanvasEvent

interface CanvasMouseEvent : CanvasEvent {
    val x: Double
    val y: Double
    val button: CanvasMouseButton
}

interface CanvasScrollEvent : CanvasEvent {
    val x: Double
    val y: Double
    val deltaY: Double
}