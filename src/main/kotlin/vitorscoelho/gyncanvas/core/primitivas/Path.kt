package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D
import kotlin.math.sqrt

class Path(
    private val closed: Boolean,
    private val listaDeFuncoes: List<(gc: GraphicsContext, transformacoes: Transformacoes) -> Unit>
) : Primitiva {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.beginPath()
        listaDeFuncoes.forEach { it.invoke(gc, transformacoes) }
//        *gc.moveTo()
//        *gc.lineTo()
//        gc.arcTo()
//        gc.bezierCurveTo()
//        gc.quadraticCurveTo()
//        gc.arc()
//        gc.rect()
        if (closed) gc.closePath()
        gc.stroke()
    }
}

class PathBuilder(private val closed: Boolean = false, pontoInicial: Vetor2D) {
    private val listaFuncoes = mutableListOf<(gc: GraphicsContext, transformacoes: Transformacoes) -> Unit>()
    private val listaPontos = mutableListOf(pontoInicial)
    private var comprimento = 0.0

    init {
        listaFuncoes.add { gc, _ -> gc.moveTo(pontoInicial.x, -pontoInicial.y) }
    }

    fun lineTo(ponto: Vetor2D): PathBuilder {
        comprimento += ponto.distancia(outroVetor = listaPontos.last())
        listaPontos += ponto
        listaFuncoes.add { gc, _ -> gc.lineTo(ponto.x, -ponto.y) }
        return this
    }

    fun deltaLineTo(deltaX: Double = 0.0, deltaY: Double = 0.0): PathBuilder {
        val pontoAnterior = listaPontos.last()
        return lineTo(pontoAnterior.somar(deltaX = deltaX, deltaY = deltaY))
    }

    fun lineTo(x: Double, y: Double): PathBuilder {
        return lineTo(ponto = Vetor2D(x = x, y = y))
    }

    fun build(): Path = Path(closed, listaFuncoes)
}