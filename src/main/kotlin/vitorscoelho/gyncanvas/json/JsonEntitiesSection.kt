package vitorscoelho.gyncanvas.json

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

class JsonPoint2d(
    val x: Double,
    val y: Double
)

class JsonPoint3d(
    val x: Double,
    val y: Double,
    val z: Double
)

interface JsonEntity {
    val layerName: String
    val colorNumber: Short
}

/*Necessário */
class JsonLine(
    override val layerName: String,
    override val colorNumber: Short,
    val startPoint: JsonPoint3d,
    val endPoint: JsonPoint3d
) : JsonEntity

class JsonCircle(
    override val layerName: String,
    override val colorNumber: Short,
    val centerPoint: JsonPoint3d,
    val radius: Double
) : JsonEntity

class JsonLwPolyline(
    override val layerName: String,
    override val colorNumber: Short,
    val closed: Boolean,
    val vertices: Array<JsonPoint2d>,
    val bulges: Array<Double>
) : JsonEntity

class JsonMText(
    override val layerName: String,
    override val colorNumber: Short,
    val insertionPoint: JsonPoint3d,
    val nominalInitialTextHeight: Double,
    val referenceRectangleWidth: Double,
    @Json(ignored = true)
    val attachmentPoint: JsonAttachmentPoint,
    @Json(ignored = true)
    val drawingDirection: JsonDrawingDirection,
    val textString: String,
    val styleName: String,
    val rotation: Double,
    val lineSpacingFactor: Double
) : JsonEntity {
    @Json(name = "attachmentPoint")
    val idAttachmentPoint: Byte
        get() = this.attachmentPoint.value
    @Json(name = "drawingDirection")
    val idDrawingDirection: Byte
        get() = this.drawingDirection.value
}

interface JsonDimension : JsonEntity {
    val text: String
    val dimensionStyleName: String
}

class JsonAlignedDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val dimLinePoint: JsonPoint3d,
    val xPoint1: JsonPoint3d,
    val xPoint2: JsonPoint3d
) : JsonDimension

class JsonRotatedDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val dimLinePoint: JsonPoint3d,
    val xPoint1: JsonPoint3d,
    val xPoint2: JsonPoint3d
) : JsonDimension

class JsonRadialDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val center: JsonPoint3d,
    val chordPoint: JsonPoint3d
) : JsonDimension

class JsonDiametricDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val farChordPoint: JsonPoint3d,
    val chordPoint: JsonPoint3d
) : JsonDimension

/*public class JsonAngularDimension : JsonDimension
{
public override string dxfSubtype() { return ""; }
}

public class JsonAngular3PointDimension : JsonDimension
{
public override string dxfSubtype() { return "AcDb3PointAngularDimension"; }
}

public class JsonOrdinateDimension : JsonDimension
{
public override string dxfSubtype() { return "AcDbOrdinateDimension"; }
}*/

enum class JsonAttachmentPoint(val value: Byte) {
    TOP_LEFT(value = 1), TOP_CENTER(value = 2), TOP_RIGHT(value = 3), MIDDLE_LEFT(value = 4), MIDDLE_CENTER(value = 5),
    MIDDLE_RIGHT(value = 6), BOTTOM_LEFT(value = 7), BOTTOM_CENTER(value = 8), BOTTOM_RIGHT(value = 9)
}

enum class JsonDrawingDirection(val value: Byte) {
    LEFT_TO_RIGHT(value = 1), TOP_TO_BOTTOM(value = 3), BY_STYLE(value = 5)
}

fun main() {
    val klaxon = Klaxon()
    val jsonDrawing = JsonDrawing(
        tablesSection = emptyList(),
        entitiesSection = listOf(
            JsonLine(
                layerName = "0",
                colorNumber = 12,
                startPoint = JsonPoint3d(x = 20.0, y = 5.0, z = 10.0),
                endPoint = JsonPoint3d(x = 10.0, y = 23.0, z = 0.0)
            ),
            JsonMText(
                layerName = "0",
                colorNumber = 10,
                insertionPoint = JsonPoint3d(x = 10.0, y = 20.0, z = 0.0),
                nominalInitialTextHeight = 10.0,
                referenceRectangleWidth = 100.0,
                attachmentPoint = JsonAttachmentPoint.BOTTOM_CENTER,
                drawingDirection = JsonDrawingDirection.BY_STYLE,
                textString = "Olá, texto",
                styleName = "Standard",
                rotation = 0.0,
                lineSpacingFactor = 1.0
            )
        )
    )
    println(klaxon.toJsonString(jsonDrawing))
}