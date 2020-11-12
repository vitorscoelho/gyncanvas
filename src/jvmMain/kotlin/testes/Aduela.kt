package testes

import javafx.scene.text.Font
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.*
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.math.Vector2D

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
    val larguraExterna = larguraLivre + 2.0 * espessuraParedeLateral
    val alturaExterna = alturaLivre + espessuraLajeFundo + espessuraLajeCobertura
    val larguraEntreMisulasCobertura = larguraLivre - 2.0 * misulaHorizontalCobertura
    val larguraEntreMisulasFundo = larguraLivre - 2.0 * misulaHorizontalFundo
    val alturaEntreMisulas = alturaLivre - misulaVerticalCobertura - misulaVerticalFundo

    private val layerContornoConcretoCorte = Layer(name = "Forma elemento cortado", color = Color.INDEX_121)
    private val layerCota = Layer(name = "Cota", color = Color.INDEX_142)

    private val dimStyle = DimStyle(
        name = "1_100 TQS",
        textStyle = TextStyle(
            name = "Standard",
            fontName = Font.getDefault().name,
            fontFileName = Font.getDefault().name
        ),
        overallScale = 100.0,
        extensionLinesExtendBeyondDimLines = 0.0
    )

    private fun corteTransversal(): List<Entity> {
        val contornoExterno = LwPolyline.rectangle(
            layer = layerContornoConcretoCorte,
            startPoint = Vector2D.ZERO, deltaX = larguraExterna, deltaY = alturaExterna
        )
        val contornoInterno = LwPolyline.initBuilder(layer = layerContornoConcretoCorte)
            .startPoint(x = espessuraParedeLateral + misulaHorizontalFundo, y = espessuraLajeFundo)
            .deltaLineTo(deltaX = larguraEntreMisulasFundo)
            .deltaLineTo(deltaX = misulaHorizontalFundo, deltaY = misulaVerticalFundo)
            .deltaLineTo(deltaY = alturaEntreMisulas)
            .deltaLineTo(deltaX = -misulaHorizontalCobertura, deltaY = misulaVerticalCobertura)
            .deltaLineTo(deltaX = -larguraEntreMisulasCobertura)
            .deltaLineTo(deltaX = -misulaHorizontalCobertura, deltaY = -misulaVerticalCobertura)
            .deltaLineTo(deltaY = -alturaEntreMisulas)
            .build()
        return listOf(contornoExterno, contornoInterno)
    }

    private fun furosCorteTransversal(): List<Entity> {
        val yInfLajeFundo = 0.0
        val ySupLajeFundo = espessuraLajeFundo
        val furoEsquerda = linhasFuroCorteTransversal(yInf = yInfLajeFundo, ySup = ySupLajeFundo)
        val yInfLajeCobertura = alturaExterna - espessuraLajeCobertura
        val ySupLajeCobertura = alturaExterna
        val furoDireita = linhasFuroCorteTransversal(yInf = yInfLajeCobertura, ySup = ySupLajeCobertura)
        return furoEsquerda + furoDireita
    }

    private fun linhasFuroCorteTransversal(yInf: Double, ySup: Double): List<Entity> {
        val lista = mutableListOf<Entity>()
        doubleArrayOf(distanciaFuroIcamento, larguraExterna - distanciaFuroIcamento).forEach { xFuro ->
            val raioFuro = diametroFuroIcamento / 2.0
            val linhaEsquerda = Line(
                layer = layerContornoConcretoCorte,
                startPoint = Vector2D(x = xFuro - raioFuro, y = yInf),
                endPoint = Vector2D(x = xFuro - raioFuro, y = ySup)
            )
            lista += linhaEsquerda
            val linhaDireita = Line(
                layer = layerContornoConcretoCorte,
                startPoint = Vector2D(x = xFuro + raioFuro, y = yInf),
                endPoint = Vector2D(x = xFuro + raioFuro, y = ySup)
            )
            lista += linhaDireita
        }
        return lista
    }

    private fun cotasHorizontaisInferioresCorteTransversal(): List<Entity> =
        RotatedDimension.horizontalSequence(
            layer = layerCota, dimStyle = dimStyle, yDimensionLine = -20.0
        ).firstPoint(Vector2D.ZERO)
            .nextDelta(deltaX = espessuraParedeLateral)
            .nextDelta(deltaX = misulaHorizontalFundo)
            .nextDelta(deltaX = larguraEntreMisulasFundo)
            .nextDelta(deltaX = misulaHorizontalFundo)
            .nextDelta(deltaX = espessuraParedeLateral)
            .toList()

    private fun cotasVerticaisDireitaCorteTransversal(): List<Entity> =
        RotatedDimension.verticalSequence(
            layer = layerCota, dimStyle = dimStyle, xDimensionLine = larguraExterna + 20.0
        ).firstPoint(x = larguraExterna, y = 0.0)
            .nextDelta(deltaY = espessuraLajeFundo)
            .nextDelta(deltaY = misulaVerticalFundo)
            .nextDelta(deltaY = alturaEntreMisulas)
            .nextDelta(deltaY = misulaVerticalCobertura)
            .nextDelta(deltaY = espessuraLajeCobertura)
            .toList()

}