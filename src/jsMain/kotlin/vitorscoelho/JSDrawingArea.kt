package vitorscoelho

import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.DrawingArea
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent

/*
class FXDrawingArea : DrawingArea() {
    private val canvas = Canvas()
    override val drawer = FXDrawer(canvas = canvas)
    val node: Node

    init {
        node = Pane(canvas)
        this.node.parentProperty().addListener {/*observable, oldValue, newValue*/ _, _, _ ->
            this.node.fitToParentSize()
        }
        canvas.widthProperty().bind(node.widthProperty())
        canvas.heightProperty().bind(node.heightProperty())
//        canvas.widthProperty().onChange { draw() }
//        canvas.heightProperty().onChange { draw() }
    }
}
 */

class JSDrawingArea(val canvas: HTMLCanvasElement) : DrawingArea() {
    override val drawer = JSDrawer(canvas = canvas)
    override fun <T : CanvasMouseEvent> addEventListener(mouseEvent: T, function: (event: T) -> Unit) {
        TODO("Not yet implemented")
    }
}