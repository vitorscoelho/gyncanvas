package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class StrokedPolyline(pontos: List<Vetor2D>, val fechado: Boolean) : Primitiva {
    private val x = DoubleArray(pontos.size) { indice -> pontos[indice].x }
    private val y = DoubleArray(pontos.size) { indice -> -pontos[indice].y }
    val nPontos: Int = pontos.size

    fun pontos(): List<Vetor2D> = (0 until nPontos).map { indice -> Vetor2D(x = x[indice], y = y[indice]) }

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        if (fechado) {
            gc.strokePolygon(x, y, nPontos)
        } else {
            gc.strokePolyline(x, y, nPontos)
        }
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): StrokedPolyline =
        StrokedPolyline(
            pontos = (0 until nPontos).map { indice ->
                val pontoTransformado = transformacoes.transformar(x = x[indice], y = y[indice])
                Vetor2D(x = pontoTransformado.x, y = pontoTransformado.y)
            },
            fechado = this.fechado
        )
}