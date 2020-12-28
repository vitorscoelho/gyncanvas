package vitorscoelho.gyncanvas.core

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import tornadofx.onChange
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent

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

    override fun <T : CanvasMouseEvent> addEventListener(mouseEvent: T, function: (event: T) -> Unit) {
        TODO("Not yet implemented")
    }
}