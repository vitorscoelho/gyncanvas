package vitorscoelho.gyncanvas.core.event

interface CanvasMouseEvent : CanvasEvent {
    val x: Double
    val y: Double
    val button: CanvasMouseButton
}