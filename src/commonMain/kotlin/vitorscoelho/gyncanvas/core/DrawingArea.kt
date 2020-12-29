package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.event.CanvasMouseEventType

abstract class DrawingArea() {
    abstract val drawer: Drawer
    abstract fun addEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit)
    abstract fun removeEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit)
}