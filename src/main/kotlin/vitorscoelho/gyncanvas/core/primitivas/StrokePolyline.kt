package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class StrokePolyline(pontos: List<Vetor2D>, private val fechado: Boolean) : Primitiva {
    private val x = DoubleArray(pontos.size) { indice -> pontos[indice].x }
    private val y = DoubleArray(pontos.size) { indice -> -pontos[indice].y }
    private val nPontos: Int = pontos.size

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        if (fechado) {
            gc.strokePolygon(x, y, nPontos)
        } else {
            gc.strokePolyline(x, y, nPontos)
        }
    }
}