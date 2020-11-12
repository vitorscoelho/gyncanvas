package vitorscoelho.gyncanvas.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

class JsonPoint2d(
    val x: Double,
    val y: Double
)

class JsonPoint3d(
    val x: Double,
    val y: Double,
    val z: Double
)

@JsonPropertyOrder(NOME_VARIAVEL_TYPE)
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
) : JsonEntity {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonLine")
}

class JsonCircle(
    override val layerName: String,
    override val colorNumber: Short,
    val centerPoint: JsonPoint3d,
    val radius: Double
) : JsonEntity {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonCircle")
}

class JsonLwPolyline(
    override val layerName: String,
    override val colorNumber: Short,
    val closed: Boolean,
    val vertices: List<JsonPoint2d>,
    val bulges: List<Double>
) : JsonEntity {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonLwPolyline")
}

class JsonMText(
    override val layerName: String,
    override val colorNumber: Short,
    val insertionPoint: JsonPoint3d,
    val nominalInitialTextHeight: Double,
    val referenceRectangleWidth: Double,
    @JsonIgnore
    val attachmentPoint: JsonAttachmentPoint,
    @JsonIgnore
    val drawingDirection: JsonDrawingDirection,
    val textString: String,
    val styleName: String,
    val rotation: Double,
    val lineSpacingFactor: Double
) : JsonEntity {
    @JsonProperty("attachmentPoint")
    val idAttachmentPoint: Byte = this.attachmentPoint.value
    @JsonProperty("drawingDirection")
    val idDrawingDirection: Byte = this.drawingDirection.value

    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonMText")
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
) : JsonDimension {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonAlignedDimension")
}

class JsonRotatedDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val angle: Double,
    val dimLinePoint: JsonPoint3d,
    val xPoint1: JsonPoint3d,
    val xPoint2: JsonPoint3d
) : JsonDimension {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonRotatedDimension")
}

class JsonRadialDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val center: JsonPoint3d,
    val chordPoint: JsonPoint3d
) : JsonDimension {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonRadialDimension")
}

class JsonDiametricDimension(
    override val layerName: String,
    override val colorNumber: Short,
    override val text: String,
    override val dimensionStyleName: String,
    val farChordPoint: JsonPoint3d,
    val chordPoint: JsonPoint3d
) : JsonDimension {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Entities", subclasse = "JsonDiametricDimension")
}

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