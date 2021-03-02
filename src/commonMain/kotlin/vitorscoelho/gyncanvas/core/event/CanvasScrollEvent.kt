package vitorscoelho.gyncanvas.core.event

interface CanvasScrollEvent : CanvasEvent {
    val x: Double
    val y: Double
    val deltaY: Double
}