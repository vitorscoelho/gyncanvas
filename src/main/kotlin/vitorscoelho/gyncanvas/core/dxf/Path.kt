package vitorscoelho.gyncanvas.core.dxf

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

//class Path private constructor(
//    private val closed: Boolean,
//    private val filled: Boolean,
//    private val steps: List<Step>
//) {
//
//    fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
//        gc.beginPath()
//        steps.forEach { it.desenhar(gc, transformacoes) }
//        if (closed) gc.closePath()
//        if (filled) gc.fill() else gc.stroke()
//    }
//
//    companion object {
//        fun initBuilder(closed: Boolean = false, pontoInicial: Vetor2D): Builder =
//            Builder(closed = closed, pontoInicial = pontoInicial)
//
//        class Builder(private val closed: Boolean = false, pontoInicial: Vetor2D) {
//            private val steps = mutableListOf<Step>()
//            private var pontoFinalAtual: Vetor2D = pontoInicial
//
//            init {
//                steps += MoveTo(ponto = pontoInicial)
//            }
//
//            fun lineTo(pontoFinal: Vetor2D): Builder {
//                pontoFinalAtual = pontoFinal
//                steps += LineTo(pontoFinal = pontoFinal)
//                return this
//            }
//
//            fun lineTo(x: Double, y: Double): Builder =
//                lineTo(pontoFinal = Vetor2D(x = x, y = y))
//
//            fun deltaLineTo(deltaX: Double = 0.0, deltaY: Double = 0.0): Builder =
//                lineTo(pontoFinal = pontoFinalAtual.somar(deltaX = deltaX, deltaY = deltaY))
//
//            fun arcTo(pontoTangente1: Vetor2D, pontoTangente2: Vetor2D, raio: Double): Builder {
//                pontoFinalAtual = pontoTangente2
//                steps += ArcTo(pontoTangente1 = pontoTangente1, pontoTangente2 = pontoTangente2, raio = raio)
//                return this
//            }
//
//            fun arcTo(
//                xTangente1: Double,
//                yTangente1: Double,
//                xTangente2: Double,
//                yTangente2: Double,
//                raio: Double
//            ): Builder =
//                arcTo(
//                    pontoTangente1 = Vetor2D(x = xTangente1, y = yTangente1),
//                    pontoTangente2 = Vetor2D(x = xTangente2, y = yTangente2),
//                    raio = raio
//                )
//
//            fun deltaArcTo(
//                deltaXTangente1: Double,
//                deltaYTangente1: Double,
//                deltaXTangente2: Double,
//                deltaYTangente2: Double,
//                raio: Double
//            ): Builder =
//                arcTo(
//                    pontoTangente1 = pontoFinalAtual.somar(deltaX = deltaXTangente1, deltaY = deltaYTangente1),
//                    pontoTangente2 = pontoFinalAtual.somar(deltaX = deltaXTangente2, deltaY = deltaYTangente2),
//                    raio = raio
//                )
//
//            fun build(filled: Boolean): Path = Path(closed = closed, filled = filled, steps = steps)
//        }
//    }
//}
//
//private interface Step {
//    fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes)
//    fun copiarComTransformacao(transformacoes: Transformacoes): Step
//}
//
//private class MoveTo(val ponto: Vetor2D) : Step {
//    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
//        gc.moveTo(ponto.x, -ponto.y)
//    }
//
//    override fun copiarComTransformacao(transformacoes: Transformacoes): MoveTo =
//        MoveTo(ponto = transformacoes.transformar(ponto))
//}
//
//private class LineTo(val pontoFinal: Vetor2D) : Step {
//    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
//        gc.lineTo(pontoFinal.x, -pontoFinal.y)
//    }
//
//    override fun copiarComTransformacao(transformacoes: Transformacoes): LineTo =
//        LineTo(pontoFinal = transformacoes.transformar(pontoFinal))
//}
//
//private class ArcTo(val pontoTangente1: Vetor2D, val pontoTangente2: Vetor2D, val raio: Double) : Step {
//    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
//        gc.arcTo(pontoTangente1.x, -pontoTangente1.y, pontoTangente2.x, -pontoTangente2.y, raio)
//    }
//
//    override fun copiarComTransformacao(transformacoes: Transformacoes): ArcTo =
//        ArcTo(
//            pontoTangente1 = transformacoes.transformar(pontoTangente1),
//            pontoTangente2 = transformacoes.transformar(pontoTangente2),
//            raio = raio * transformacoes.escala
//        )
//}