package vitorscoelho.gyncanvas.core

import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize

class FXDrawingArea {
    internal val canvas = Canvas()
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