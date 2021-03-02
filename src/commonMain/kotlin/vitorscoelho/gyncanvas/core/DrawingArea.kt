package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.event.CanvasMouseEventType
import vitorscoelho.gyncanvas.core.event.CanvasScrollEvent
import vitorscoelho.gyncanvas.core.event.CanvasScrollEventType

abstract class DrawingArea() {
    abstract val drawer: Drawer
    abstract fun addEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit)
    abstract fun removeEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit)
    abstract fun addEventListener(eventType: CanvasScrollEventType, eventHandler: (event: CanvasScrollEvent) -> Unit)
    abstract fun removeEventListener(eventType: CanvasScrollEventType, eventHandler: (event: CanvasScrollEvent) -> Unit)
}