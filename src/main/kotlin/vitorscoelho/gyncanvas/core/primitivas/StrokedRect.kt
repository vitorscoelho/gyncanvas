package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.absoluteValue

class StrokedRect(pontoInsercao: Vector2D, deltaX: Double, deltaY: Double) : Primitiva {
    val cantoEsquerdoSuperior: Vector2D = run {
        val x = if (deltaX >= 0.0) pontoInsercao.x else pontoInsercao.x + deltaX
        val y = if (deltaY <= 0.0) pontoInsercao.y else pontoInsercao.y + deltaY
        Vector2D(x = x, y = y)
    }
    val largura = deltaX.absoluteValue
    val altura = deltaY.absoluteValue

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.strokeRect(cantoEsquerdoSuperior.x, -cantoEsquerdoSuperior.y, largura, altura)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): StrokedRect =
        StrokedRect(
            pontoInsercao = transformacoes.transformar(this.cantoEsquerdoSuperior),
            deltaX = this.largura * transformacoes.escala,
            deltaY = this.altura * transformacoes.escala
        )
}