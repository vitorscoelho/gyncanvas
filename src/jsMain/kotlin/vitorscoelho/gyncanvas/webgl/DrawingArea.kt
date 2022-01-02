package vitorscoelho.gyncanvas.webgl

import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.JSEventManager
import vitorscoelho.gyncanvas.Drawer
import vitorscoelho.gyncanvas.DrawingArea
import vitorscoelho.gyncanvas.core.EventManager

class JSDrawingArea(val canvas: HTMLCanvasElement, val type: JSDrawingAreaType) : DrawingArea() {
    override var width: Int = canvas.clientWidth
        get() = canvas.clientWidth
    override var height: Int = canvas.clientHeight
        get() = canvas.clientHeight
    override val listeners: EventManager = JSEventManager(canvas)
    override val drawer: Drawer by lazy { type.criarDrawer(drawingArea = this) }
}

enum class JSDrawingAreaType {
    WEBGL {
        override fun criarDrawer(drawingArea: JSDrawingArea) = WebGLStaticDrawer2D(drawingArea = drawingArea)
    },
    CANVAS_2D {
        override fun criarDrawer(drawingArea: JSDrawingArea): Drawer {
            TODO("Not yet implemented")
        }
    },
    ;

    internal abstract fun criarDrawer(drawingArea: JSDrawingArea): Drawer
}