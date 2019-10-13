package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vector2D

class StrokedCircle(val centro: Vector2D, val diametro: Double) : PrimitivaStroke {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        val raio = diametro / 2.0
        gc.strokeOval(
            centro.x - raio, -centro.y - raio,
            diametro, diametro
        )
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): StrokedCircle =
        StrokedCircle(
            centro = transformacoes.transformar(this.centro),
            diametro = this.diametro * transformacoes.escala
        )
}