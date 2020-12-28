package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent

abstract class DrawingArea() {
    abstract val drawer: Drawer
    abstract fun <T : CanvasMouseEvent> addEventListener(mouseEvent: T, function: (event: T) -> Unit)
}