package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class StrokedLine(
    val ponto1: Vetor2D,
    val ponto2: Vetor2D
) : PrimitivaStroke {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.strokeLine(
            ponto1.x, -ponto1.y,
            ponto2.x, -ponto2.y
        )
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): StrokedLine =
        StrokedLine(
            ponto1 = transformacoes.transformar(this.ponto1),
            ponto2 = transformacoes.transformar(this.ponto2)
        )

}