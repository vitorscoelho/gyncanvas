package vitorscoelho.gyncanvas.json

import vitorscoelho.gyncanvas.core.primitivas.*
import vitorscoelho.gyncanvas.math.Vector2D

internal fun tipoJson(classeMae: String, subclasse: String) =
    "JsonAutoCad.JsonConversorNovo.$subclasse, JsonAutoCad"//"jsondxfcsharp.$classeMae.$subclasse, jsondxfcsharp"

internal const val NOME_VARIAVEL_TYPE = "$" + "type"

fun Vector2D.toJsonPoint2d() = JsonPoint2d(x = x, y = y)
fun Vector2D.toJsonPoint3d() = JsonPoint3d(x = x, y = y, z = 0.0)

const val COLOR_NUMBER_BY_LAYER: Short = 256
val LAYER_0_NAME: String by lazy { "0" }
val DIM_STYLE_STANDARD_NAME: String by lazy { "Standard" }

fun Primitiva.toJsonEntity(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER,
    dimensionStyleName: String = DIM_STYLE_STANDARD_NAME
): JsonEntity =
    when {
        this is StrokedLine -> toJsonLine(layerName = layerName, colorNumber = colorNumber)
        this is StrokedPolyline -> toJsonLwPolyline(layerName = layerName, colorNumber = colorNumber)
        this is StrokedRect -> toJsonLwPolyline(layerName = layerName, colorNumber = colorNumber)
        this is StrokedCircle -> toJsonCircle(layerName = layerName, colorNumber = colorNumber)
        this is Cota -> toJsonDimension(
            layerName = layerName,
            colorNumber = colorNumber,
            dimensionStyleName = dimensionStyleName
        )
        else -> TODO("Este tipo de primitiva não está implementada ainda para esta conversão ")
    }

fun Cota.toJsonDimension(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER,
    dimensionStyleName: String = DIM_STYLE_STANDARD_NAME
): JsonDimension =
    when {
        this is CotaHorizontal -> toJsonRotatedDimension(
            layerName = layerName,
            colorNumber = colorNumber,
            dimensionStyleName = dimensionStyleName
        )
        this is CotaVertical -> toJsonRotatedDimension(
            layerName = layerName,
            colorNumber = colorNumber,
            dimensionStyleName = dimensionStyleName
        )
        this is CotaAlinhada -> toJsonAlignedDimension(
            layerName = layerName,
            colorNumber = colorNumber,
            dimensionStyleName = dimensionStyleName
        )
        else -> TODO("Este tipo de cota primitiva não está implementada ainda para esta conversão ")
    }

fun CotaHorizontal.toJsonRotatedDimension(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER,
    dimensionStyleName: String = DIM_STYLE_STANDARD_NAME
): JsonRotatedDimension =
    JsonRotatedDimension(
        layerName = layerName,
        colorNumber = colorNumber,
        text = texto,
        dimensionStyleName = dimensionStyleName,
        dimLinePoint = JsonPoint3d(x = ponto1.x, y = yDimensionLine, z = 0.0),
        angle = 0.0,
        xPoint1 = ponto1.toJsonPoint3d(),
        xPoint2 = ponto2.toJsonPoint3d()
    )

fun CotaVertical.toJsonRotatedDimension(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER,
    dimensionStyleName: String = DIM_STYLE_STANDARD_NAME
): JsonRotatedDimension =
    JsonRotatedDimension(
        layerName = layerName,
        colorNumber = colorNumber,
        text = texto,
        dimensionStyleName = dimensionStyleName,
        angle = 90.0,
        dimLinePoint = JsonPoint3d(x = xDimensionLine, y = ponto1.y, z = 0.0),
        xPoint1 = ponto1.toJsonPoint3d(),
        xPoint2 = ponto2.toJsonPoint3d()
    )

fun CotaAlinhada.toJsonAlignedDimension(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER,
    dimensionStyleName: String = DIM_STYLE_STANDARD_NAME
): JsonAlignedDimension {
    TODO("Não implementado")
    /*JsonAlignedDimension(
        layerName = layerName,
        colorNumber = colorNumber,
        text = texto,
        dimensionStyleName = dimensionStyleName,
        dimLinePoint = //Ver melhor maneira de determinar este ponto,
        xPoint1 = ponto1.toJsonPoint3d(),
        xPoint2 = ponto2.toJsonPoint3d()
    )*/
}


fun StrokedRect.toJsonLwPolyline(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER
): JsonLwPolyline =
    JsonLwPolyline(
        layerName = layerName,
        colorNumber = colorNumber,
        closed = true,
        vertices = listOf(
            cantoEsquerdoSuperior.toJsonPoint2d(),
            cantoEsquerdoSuperior.createNewWithOffset(deltaX = largura).toJsonPoint2d(),
            cantoEsquerdoSuperior.createNewWithOffset(deltaX = largura, deltaY = -altura).toJsonPoint2d(),
            cantoEsquerdoSuperior.createNewWithOffset(deltaY = -altura).toJsonPoint2d()
        ),
        bulges = List(size = 4) { 0.0 }
    )

fun StrokedCircle.toJsonCircle(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER
): JsonCircle =
    JsonCircle(
        layerName = layerName,
        colorNumber = colorNumber,
        centerPoint = centro.toJsonPoint3d(),
        radius = diametro / 2.0
    )

fun StrokedLine.toJsonLine(layerName: String = LAYER_0_NAME, colorNumber: Short = COLOR_NUMBER_BY_LAYER): JsonLine =
    JsonLine(
        layerName = layerName,
        colorNumber = colorNumber,
        startPoint = ponto1.toJsonPoint3d(),
        endPoint = ponto2.toJsonPoint3d()
    )

fun StrokedPolyline.toJsonLwPolyline(
    layerName: String = LAYER_0_NAME,
    colorNumber: Short = COLOR_NUMBER_BY_LAYER
): JsonLwPolyline =
    JsonLwPolyline(
        layerName = layerName,
        colorNumber = colorNumber,
        closed = fechado,
        vertices = pontos().map { it.toJsonPoint2d() },
        bulges = List(size = nPontos) { 0.0 }
    )

