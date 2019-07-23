package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class Path private constructor(
    private val fechado: Boolean,
    private val passos: List<Passo>
) : Primitiva {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.beginPath()
        passos.forEach { it.desenhar(gc, transformacoes) }
        if (fechado) gc.closePath()
        gc.stroke()
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): Path =
        Path(
            fechado = fechado,
            passos = passos.map { it.copiarComTransformacao(transformacoes) }
        )

    companion object {
        fun initBuilder(fechado: Boolean = false, pontoInicial: Vetor2D): Builder =
            Builder(fechado = fechado, pontoInicial = pontoInicial)

        class Builder(private val fechado: Boolean = false, pontoInicial: Vetor2D) {
            private val passos = mutableListOf<Passo>()
            private var pontoFinalAtual: Vetor2D = pontoInicial

            init {
                passos += MoveTo(ponto = pontoInicial)
            }

            fun moveTo(ponto: Vetor2D): Builder {
                pontoFinalAtual = ponto
                passos += MoveTo(ponto = ponto)
                return this
            }

            fun lineTo(pontoFinal: Vetor2D): Builder {
                pontoFinalAtual = pontoFinal
                passos += LineTo(pontoFinal = pontoFinal)
                return this
            }

            fun arcTo(pontoTangente1: Vetor2D, pontoTangente2: Vetor2D, raio: Double): Builder {
                pontoFinalAtual = pontoTangente2
                passos += ArcTo(pontoTangente1 = pontoTangente1, pontoTangente2 = pontoTangente2, raio = raio)
                return this
            }

            fun bezierCurveTo(pontoControle1: Vetor2D, pontoControle2: Vetor2D, pontoFinal: Vetor2D): Builder {
                pontoFinalAtual = pontoFinal
                passos += BezierCurveTo(
                    pontoControle1 = pontoControle1,
                    pontoControle2 = pontoControle2,
                    pontoFinal = pontoFinal
                )
                return this
            }

            fun quadraticCurveTo(pontoControle: Vetor2D, pontoFinal: Vetor2D): Builder {
                pontoFinalAtual = pontoFinal
                passos += QuadraticCurveTo(pontoControle = pontoControle, pontoFinal = pontoFinal)
                return this
            }

            //fun arc(pontoCentro: Vetor2D, raioX: Double, raioY: Double, anguloInicial: Double, comprimento: Double): Builder {}
            //fun rect(pontoInsercao: Vetor2D, deltaX: Double, deltaY: Double): Builder {}

            fun deltaMoveTo(deltaX: Double, deltaY: Double): Builder =
                moveTo(ponto = pontoFinalAtual.somar(deltaX = deltaX, deltaY = deltaY))

            fun moveTo(x: Double, y: Double): Builder =
                moveTo(ponto = Vetor2D(x = x, y = y))

            fun deltaLineTo(deltaX: Double = 0.0, deltaY: Double = 0.0): Builder =
                lineTo(pontoFinal = pontoFinalAtual.somar(deltaX = deltaX, deltaY = deltaY))

            fun lineTo(x: Double, y: Double): Builder =
                lineTo(pontoFinal = Vetor2D(x = x, y = y))

            fun deltaArcTo(
                deltaXTangente1: Double,
                deltaYTangente1: Double,
                deltaXTangente2: Double,
                deltaYTangente2: Double,
                raio: Double
            ): Builder =
                arcTo(
                    pontoTangente1 = pontoFinalAtual.somar(deltaX = deltaXTangente1, deltaY = deltaYTangente1),
                    pontoTangente2 = pontoFinalAtual.somar(deltaX = deltaXTangente2, deltaY = deltaYTangente2),
                    raio = raio
                )

            fun arcTo(
                xTangente1: Double,
                yTangente1: Double,
                xTangente2: Double,
                yTangente2: Double,
                raio: Double
            ): Builder =
                arcTo(
                    pontoTangente1 = Vetor2D(x = xTangente1, y = yTangente1),
                    pontoTangente2 = Vetor2D(x = xTangente2, y = yTangente2),
                    raio = raio
                )

            fun build(): Path = Path(fechado = fechado, passos = passos)
        }
    }
}

private interface Passo {
    fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes)
    fun copiarComTransformacao(transformacoes: Transformacoes): Passo
}

private class MoveTo(val ponto: Vetor2D) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.moveTo(ponto.x, -ponto.y)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): MoveTo =
        MoveTo(ponto = transformacoes.transformar(ponto))
}

private class LineTo(val pontoFinal: Vetor2D) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.lineTo(pontoFinal.x, -pontoFinal.y)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): LineTo =
        LineTo(pontoFinal = transformacoes.transformar(pontoFinal))
}

private class ArcTo(val pontoTangente1: Vetor2D, val pontoTangente2: Vetor2D, val raio: Double) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.arcTo(pontoTangente1.x, -pontoTangente1.y, pontoTangente2.x, -pontoTangente2.y, raio)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): ArcTo =
        ArcTo(
            pontoTangente1 = transformacoes.transformar(pontoTangente1),
            pontoTangente2 = transformacoes.transformar(pontoTangente2),
            raio = raio * transformacoes.escala
        )
}

private class BezierCurveTo(
    val pontoControle1: Vetor2D,
    val pontoControle2: Vetor2D,
    val pontoFinal: Vetor2D
) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.bezierCurveTo(
            pontoControle1.x,
            -pontoControle1.y,
            pontoControle2.x,
            -pontoControle2.y,
            pontoFinal.x,
            pontoFinal.y
        )
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): BezierCurveTo =
        BezierCurveTo(
            pontoControle1 = transformacoes.transformar(pontoControle1),
            pontoControle2 = transformacoes.transformar(pontoControle2),
            pontoFinal = transformacoes.transformar(pontoFinal)
        )
}

private class QuadraticCurveTo(val pontoControle: Vetor2D, val pontoFinal: Vetor2D) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.quadraticCurveTo(pontoControle.x, -pontoControle.y, pontoFinal.x, -pontoFinal.y)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): QuadraticCurveTo =
        QuadraticCurveTo(
            pontoControle = transformacoes.transformar(pontoControle),
            pontoFinal = transformacoes.transformar(pontoFinal)
        )
}

/*private class Arc(
    val pontoCentro: Vetor2D,
    val raioX: Double,
    val raioY: Double,
    val anguloInicial: Double,
    val comprimento: Double
) : Passo {
    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.arc(pontoCentro.x, -pontoCentro.y, raioX, raioY, anguloInicial, comprimento)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): Arc =
        Arc(
            pontoCentro = transformacoes.transformar(pontoCentro),
            raioX = raioX * transformacoes.escala,
            raioY = raioY * transformacoes.escala,
            anguloInicial = anguloInicial,//TODO
            comprimento = comprimento * transformacoes.escala
        )
}

private class Rect(pontoInsercao: Vetor2D, deltaX: Double, deltaY: Double) : Passo {
    val cantoEsquerdoInferior: Vetor2D = run {
        val x = if (deltaX >= 0.0) pontoInsercao.x else pontoInsercao.x + deltaX
        val y = if (deltaY <= 0.0) pontoInsercao.y else pontoInsercao.y + deltaY
        Vetor2D(x = x, y = y)
    }
    val largura = deltaX.absoluteValue
    val altura = deltaY.absoluteValue

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.rect(cantoEsquerdoInferior.x, -cantoEsquerdoInferior.y, largura, altura)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): Rect =
        Rect(
            pontoInsercao = transformacoes.transformar(cantoEsquerdoInferior),
            deltaX = largura * transformacoes.escala,
            deltaY = altura * transformacoes.escala
        )
}*/