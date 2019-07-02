package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class StrokeCircle(val centro: Vetor2D, val diametro: Double) : PrimitivaStroke {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        val raio = diametro / 2.0
        gc.strokeOval(
            centro.x - raio, -centro.y - raio,
            diametro, diametro
        )
    }
}