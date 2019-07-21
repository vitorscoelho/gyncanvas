package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D
import kotlin.math.absoluteValue

class FillRect(pontoInsercao: Vetor2D, deltaX: Double, deltaY: Double) : Primitiva {
    private val cantoEsquerdoInferior: Vetor2D = run {
        val x = if (deltaX >= 0.0) pontoInsercao.x else pontoInsercao.x + deltaX
        val y = if (deltaY <= 0.0) pontoInsercao.y else pontoInsercao.y + deltaY
        Vetor2D(x = x, y = y)
    }
    val largura = deltaX.absoluteValue
    val altura = deltaY.absoluteValue

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.fillRect(cantoEsquerdoInferior.x, -cantoEsquerdoInferior.y, largura, altura)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): FillRect =
        FillRect(
            pontoInsercao = transformacoes.transformar(this.cantoEsquerdoInferior),
            deltaX = this.largura * transformacoes.escala,
            deltaY = this.altura * transformacoes.escala
        )
}