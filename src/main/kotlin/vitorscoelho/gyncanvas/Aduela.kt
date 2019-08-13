package vitorscoelho.gyncanvas

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import vitorscoelho.gyncanvas.core.GynCanvas
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.core.primitivas.*
import vitorscoelho.gyncanvas.core.primitivas.propriedades.DrawAttributes
import vitorscoelho.gyncanvas.math.Vetor2D
import java.text.DecimalFormat

class FormaAduela(
    val larguraLivre: Double,
    val alturaLivre: Double,
    val misulaHorizontalCobertura: Double,
    val misulaVerticalCobertura: Double,
    val misulaHorizontalFundo: Double,
    val misulaVerticalFundo: Double,
    val espessuraLajeCobertura: Double,
    val espessuraParedeLateral: Double,
    val espessuraLajeFundo: Double,
    val distanciaFuroIcamento: Double,
    val diametroFuroIcamento: Double,
    val comprimentoAduela: Double,
    val comprimentoEncaixe: Double,
    val espessuraMacho: Double,
    val espessuraChanfradoEncaixe: Double
) {
    private val elementos = hashMapOf<Primitiva, DrawAttributes>()
    val larguraExterna = larguraLivre + 2.0 * espessuraParedeLateral
    val alturaExterna = alturaLivre + espessuraLajeFundo + espessuraLajeCobertura
    val larguraEntreMisulasCobertura = larguraLivre - 2.0 * misulaHorizontalCobertura
    val larguraEntreMisulasFundo = larguraLivre - 2.0 * misulaHorizontalFundo
    val alturaEntreMisulas = alturaLivre - misulaVerticalCobertura - misulaVerticalFundo

    private val propContornoConcretoCorte = object : DrawAttributes {
        override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
            gc.stroke = Color.DEEPSKYBLUE
            gc.lineWidth = 1.0
        }
    }

    private val propCota = object : DrawAttributes {
        override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
            gc.stroke = Color.RED
            gc.fill = Color.RED
            gc.lineWidth = 1.0
            gc.textAlign = TextAlignment.CENTER
            gc.textBaseline = VPos.BOTTOM
            gc.font = Font(gc.font.name, 12.0)
        }
    }
    val propriedadesCotas = PropriedadesCotas(
        formatoNumero = DecimalFormat("#.##"),
        offsetExtensionLine = 5.0,
        offsetText = 0.0,
        arrowSize = 2.0
    )

    init {
        corteTransversal()
    }

    private fun corteTransversal() {
        val contornoExterno = StrokedRect(pontoInsercao = Vetor2D.ZERO, deltaX = larguraExterna, deltaY = alturaExterna)
        val contornoInterno = Path.initBuilder(
            fechado = true,
            pontoInicial = Vetor2D(
                x = espessuraParedeLateral + misulaHorizontalFundo,
                y = espessuraLajeFundo
            )
        )
            .deltaLineTo(deltaX = larguraEntreMisulasFundo)
            .deltaLineTo(deltaX = misulaHorizontalFundo, deltaY = misulaVerticalFundo)
            .deltaLineTo(deltaY = alturaEntreMisulas)
            .deltaLineTo(deltaX = -misulaHorizontalCobertura, deltaY = misulaVerticalCobertura)
            .deltaLineTo(deltaX = -larguraEntreMisulasCobertura)
            .deltaLineTo(deltaX = -misulaHorizontalCobertura, deltaY = -misulaVerticalCobertura)
            .deltaLineTo(deltaY = -alturaEntreMisulas)
            .build()
        elementos[contornoExterno] = propContornoConcretoCorte
        elementos[contornoInterno] = propContornoConcretoCorte
        furosCorteTransversal()
        cotasHorizontaisInferioresCorteTransversal()
        cotasVerticaisDireitaCorteTransversal()
    }

    private fun furosCorteTransversal() {
        val yInfLajeFundo = 0.0
        val ySupLajeFundo = espessuraLajeFundo
        linhasFuroCorteTransversal(yInf = yInfLajeFundo, ySup = ySupLajeFundo)
        val yInfLajeCobertura = alturaExterna - espessuraLajeCobertura
        val ySupLajeCobertura = alturaExterna
        linhasFuroCorteTransversal(yInf = yInfLajeCobertura, ySup = ySupLajeCobertura)
    }

    private fun linhasFuroCorteTransversal(yInf: Double, ySup: Double) {
        doubleArrayOf(distanciaFuroIcamento, larguraExterna - distanciaFuroIcamento).forEach { xFuro ->
            val raioFuro = diametroFuroIcamento / 2.0
            val linhaEsquerda = StrokedLine(
                ponto1 = Vetor2D(x = xFuro - raioFuro, y = yInf),
                ponto2 = Vetor2D(x = xFuro - raioFuro, y = ySup)
            )
            val linhaDireita = StrokedLine(
                ponto1 = Vetor2D(x = xFuro + raioFuro, y = yInf),
                ponto2 = Vetor2D(x = xFuro + raioFuro, y = ySup)
            )
            elementos[linhaEsquerda] = propContornoConcretoCorte
            elementos[linhaDireita] = propContornoConcretoCorte
        }
    }

    private fun cotasHorizontaisInferioresCorteTransversal() {
        SequenciaCotaHorizontal(
            pontoInicial = Vetor2D.ZERO,
            yDimensionLine = -20.0,
            propriedadesCotas = propriedadesCotas
        )
            .addDelta(deltaX = espessuraParedeLateral)
            .addDelta(deltaX = misulaHorizontalFundo)
            .addDelta(deltaX = larguraEntreMisulasFundo)
            .addDelta(deltaX = misulaHorizontalFundo)
            .addDelta(deltaX = espessuraParedeLateral)
            .toList()
            .forEach { elementos.put(key = it, value = propCota) }
    }

    private fun cotasVerticaisDireitaCorteTransversal() {
        SequenciaCotaVertical(
            pontoInicial = Vetor2D(x = larguraExterna, y = 0.0),
            xDimensionLine = larguraExterna + 20.0,
            propriedadesCotas = propriedadesCotas
        )
            .addDelta(deltaY = espessuraLajeFundo)
            .addDelta(deltaY = misulaVerticalFundo)
            .addDelta(deltaY = alturaEntreMisulas)
            .addDelta(deltaY = misulaVerticalCobertura)
            .addDelta(deltaY = espessuraLajeCobertura)
            .toList()
            .forEach { elementos.put(key = it, value = propCota) }
    }

    fun adicionarDesenho(gynCanvas: GynCanvas) {
        elementos.forEach { primitiva, propriedade ->
            gynCanvas.addPrimitiva(
                primitiva = primitiva,
                propriedade = propriedade
            )
        }
    }
}