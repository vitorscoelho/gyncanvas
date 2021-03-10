package vitorscoelho.gyncanvas.webgl

import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.JSEventManager
import vitorscoelho.gyncanvas.core.EventManager

abstract class DrawingArea {
    abstract var width: Int
        protected set
    abstract var height: Int
        protected set
    abstract val listeners: EventManager
}

class JSDrawingArea(val canvas: HTMLCanvasElement) : DrawingArea() {
    override var width: Int = canvas.clientWidth
        get() = canvas.clientWidth
    override var height: Int = canvas.clientHeight
        get() = canvas.clientHeight
    override val listeners: EventManager = JSEventManager(canvas)
}