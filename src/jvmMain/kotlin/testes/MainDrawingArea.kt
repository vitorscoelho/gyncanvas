package testes

import tornadofx.*
import vitorscoelho.gyncanvas.core.FXCanvasController
import vitorscoelho.gyncanvas.core.FXDrawingArea
import vitorscoelho.gyncanvas.testes.desenhar

fun main() {
    launch<MeuAppDrawingArea>()
}

class MeuAppDrawingArea : App(MeuViewDrawingArea::class)

class MeuViewDrawingArea : View() {
    private val drawingArea = FXDrawingArea()
    private val controller = FXCanvasController(drawingArea)

    override val root = hbox {
        setPrefSize(1300.0, 600.0)
        this += drawingArea.node
    }

    override fun onDock() {
        super.onDock()
        desenhar(controller)
    }
}