package testes

import tornadofx.*
import vitorscoelho.gyncanvas.core.FXDrawingArea
import vitorscoelho.gyncanvas.testes.desenhar

fun main() {
    launch<MeuAppDrawingArea>()
}

class MeuAppDrawingArea : App(MeuViewDrawingArea::class)

class MeuViewDrawingArea : View() {
    private val drawingArea = FXDrawingArea()

    override val root = hbox {
        setPrefSize(1300.0, 600.0)
        this += drawingArea.node
    }

    override fun onDock() {
        super.onDock()
        desenhar(drawingArea = drawingArea)
    }
}