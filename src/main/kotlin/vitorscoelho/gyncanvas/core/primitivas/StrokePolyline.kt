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

    override fun copiarComTransformacao(transformacoes: Transformacoes): StrokePolyline =
        StrokePolyline(
            pontos = (0 until nPontos).map { indice ->
                val pontoTransformado = transformacoes.transformar(x = x[indice], y = y[indice])
                Vetor2D(x = pontoTransformado.x, y = pontoTransformado.y)
            },
            fechado = this.fechado
        )
}