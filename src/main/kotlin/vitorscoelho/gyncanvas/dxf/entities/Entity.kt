package vitorscoelho.gyncanvas.dxf.entities

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

fun main() {
    val jsonLine = JsonLine(
        layer = JsonLayer(
            name = "Layer do Json",
            replace = false
        ),
        color = JsonColor(),
        startPoint = JsonPoint3d(x = -15.0, y = 20.0, z = 0.0),
        endPoint = JsonPoint3d(x = 20.0, y = 50.0, z = 0.0)
    )
    println(Klaxon().toJsonString(jsonLine))
}

class JsonColor

class JsonPoint3d(
    val x: Double,
    val y: Double,
    val z: Double
)

private fun tipoJson(classeMae: String, subclasse: String) = "jsondxfcsharp.$classeMae.$subclasse, jsondxfcsharp"

interface JsonTableRecord {
    val name: String
    val replace: Boolean
    @Json(name = "$" + "type")
    val tipoCompleto: String
        get() = tipoJson(classeMae = "Tables", subclasse = tipo())

    fun tipo(): String
}

interface JsonEntity {
    val layer: JsonLayer
    val color: JsonColor
    @Json(name = "$" + "type")
    val tipoCompleto: String
        get() = tipoJson(classeMae = "Entities", subclasse = tipo())

    fun tipo(): String
}

class JsonDimStyle(
    override val name: String,
    override val replace: Boolean
) : JsonTableRecord {
    override fun tipo() = "DimStyle"
}

class JsonLayer(
    override val name: String,
    override val replace: Boolean
) : JsonTableRecord {
    override fun tipo() = "Layer"
}

class JsonStyle(
    override val name: String,
    override val replace: Boolean
) : JsonTableRecord {
    override fun tipo() = "Style"
}

class JsonLine(
    override val layer: JsonLayer,
    override val color: JsonColor,
    val startPoint: JsonPoint3d,
    val endPoint: JsonPoint3d
) : JsonEntity {
    override fun tipo() = "Line"
}

class JsonCircle(
    override val layer: JsonLayer,
    override val color: JsonColor,
    val centerPoint: JsonPoint3d,
    val radius: Double
) : JsonEntity {
    override fun tipo() = "Circle"
}

class Point2d(
    var x: Double,
    var y: Double
)

class Point3d(
    var x: Double,
    var y: Double
)

interface Entity {
    var layer: String
}
/*
Line
Polyline
Circle
Text
Dimension
 */

class Line(
    var startPoint: Point3d,
    var endPoint: Point3d,
    override var layer: String
) : Entity

/*
int index, Point2d pt, double bulge, double startWidth, double endWidth
 */
class Polyline(
    var vertices: Array<Point2d>,
    var bulges: Array<Double>,
    var closed: Boolean,
    override var layer: String
) : Entity

class PolylineVertex(var point: Point2d, var bulge: Double, var startWidth: Double, val endWidth: Double)

class Circle(
    var center: Point3d,
    var radius: Double,
    override var layer: String
) : Entity {
    @Json(ignored = true)
    var diameter: Double
        get() = 2.0 * radius
        set(value) {
            radius = value / 2.0
        }
}

class MText(
    var location: Point3d,
//    var Text: String,
    var contents: String,
    var rotation: Double,
    override var layer: String
) : Entity
