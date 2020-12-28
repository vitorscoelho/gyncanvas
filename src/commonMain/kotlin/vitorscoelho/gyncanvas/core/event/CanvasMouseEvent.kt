package vitorscoelho.gyncanvas.core.event

interface CanvasMouseEvent {
    fun getX(): Double
    fun getY(): Double
    fun getButton(): CanvasMouseButton
}