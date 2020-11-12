package vitorscoelho.gyncanvas.json

import vitorscoelho.gyncanvas.math.Vector2D

internal fun tipoJson(classeMae: String, subclasse: String) =
    "JsonAutoCad.JsonConversorNovo.$subclasse, JsonAutoCad"//"jsondxfcsharp.$classeMae.$subclasse, jsondxfcsharp"

internal const val NOME_VARIAVEL_TYPE = "$" + "type"

fun Vector2D.toJsonPoint2d() = JsonPoint2d(x = x, y = y)
fun Vector2D.toJsonPoint3d() = JsonPoint3d(x = x, y = y, z = 0.0)

const val COLOR_NUMBER_BY_LAYER: Short = 256
val LAYER_0_NAME: String by lazy { "0" }
val DIM_STYLE_STANDARD_NAME: String by lazy { "Standard" }