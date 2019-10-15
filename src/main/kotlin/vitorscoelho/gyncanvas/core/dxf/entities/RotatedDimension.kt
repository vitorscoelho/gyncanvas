package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionPoints
import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionSequence
import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionSequenceStart
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D
import java.lang.Math.toRadians

data class RotatedDimension(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    override val dimStyle: DimStyle,
    override val dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
    override val xPoint1: Vector2D,
    override val xPoint2: Vector2D,
    private val dimensionLineReferencePoint: Vector2D,
    override val angle: Double,
    override val text: String = "<>"
) : LinearDimension {
    override val measurement: Double
    override val entities: List<Entity>

    init {
        val points = RotatedDimensionPoints(
            xPoint1 = xPoint1, xPoint2 = xPoint2, dimensionLineReferencePoint = dimensionLineReferencePoint,
            angle = angle,
            extensionLinesExtendBeyondDimLines = extensionLinesExtendBeyondDimLines,
            extensionLinesOffsetFromOrigin = extensionLinesOffsetFromOrigin,
            textOffsetFromDimLine = textOffsetFromDimLine,
            overallScale = overallScale
        )

        val dimensionLine: Line? = createDimensionLine(points = points)
        val extensionLine1: Line? = createExtensionLine(
            supress = extensionLinesSupressExtLine1,
            dimensionPoint1 = points.point1ExtensionLine1,
            dimensionPoint2 = points.point2ExtensionLine1
        )
        val extensionLine2: Line? = createExtensionLine(
            supress = extensionLinesSupressExtLine2,
            dimensionPoint1 = points.point1ExtensionLine2,
            dimensionPoint2 = points.point2ExtensionLine2
        )
        this.measurement = Vector2D.distance(points.point1DimensionLine, points.point2DimensionLine)
        val mText: MText? = createMText(
            content = text,
            dimensionPoint = points.mTextPosition,
            measurement = measurement
        )
        this.entities = listOfNotNull(dimensionLine, extensionLine1, extensionLine2, mText)
    }

    override fun dimContinue(point: Vector2D): RotatedDimension = dimContinueXPoint2(point)

    override fun dimContinueXPoint1(point: Vector2D): RotatedDimension {
        return copy(xPoint1 = this.xPoint1, xPoint2 = point)
    }

    override fun dimContinueXPoint2(point: Vector2D): RotatedDimension {
        return copy(xPoint1 = this.xPoint2, xPoint2 = point)
    }

    override fun createSequence(points: List<Vector2D>): List<RotatedDimension> {
        val dimensions = mutableListOf(this)
        points.forEach { dimensions.add(dimensions.last().dimContinue(it)) }
        return dimensions
    }

    fun initSequence(): RotatedDimensionSequence = RotatedDimensionSequence.init(
        layer = layer, color = color,
        dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
        dimensionLineReferencePoint = dimensionLineReferencePoint, angle = angle, text = text
    ).firstPoint(xPoint1).next(xPoint2)

    private fun createDimensionLine(points: RotatedDimensionPoints): Line? {
        val point1: Vector2D
        val point2: Vector2D
        if (this.dimensionLinesSupressDimLine1 && this.dimensionLinesSupressDimLine2) return null
        if (!this.dimensionLinesSupressDimLine1 && !this.dimensionLinesSupressDimLine2) {
            point1 = points.point1DimensionLine
            point2 = points.point2DimensionLine
        } else if (this.dimensionLinesSupressDimLine1) {
            point1 = points.midPointDimensionLine
            point2 = points.point2DimensionLine
        } else {
            point1 = points.point1DimensionLine
            point2 = points.midPointDimensionLine
        }
        return Line(layer = layer, color = dimensionLinesColor, startPoint = point1, endPoint = point2)
    }

    private fun createExtensionLine(supress: Boolean, dimensionPoint1: Vector2D, dimensionPoint2: Vector2D): Line? {
        if (supress) return null
        return Line(
            layer = layer,
            color = extensionLinesColor,
            startPoint = dimensionPoint1,
            endPoint = dimensionPoint2
        )
    }

    private fun createMText(content: String, dimensionPoint: Vector2D, measurement: Double): MText? {
        return MText(
            layer = layer,
            color = textColor,
            style = textStyle,
            size = textHeight * overallScale,
            justify = AttachmentPoint.BOTTOM_CENTER,
            rotation = angle,
            position = dimensionPoint,
            content = content.replaceFirst("<>", dimStyle.linearDimensionFormat(measurement))
        )
    }

    override fun transform(transformationMatrix: TransformationMatrix): Entity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun horizontal(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            xPoint1: Vector2D, xPoint2: Vector2D,
            yDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimension = RotatedDimension(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            xPoint1 = xPoint1, xPoint2 = xPoint2, angle = 0.0,
            text = text,
            dimensionLineReferencePoint = Vector2D(x = xPoint1.x, y = yDimensionLine)
        )

        fun vertical(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            xPoint1: Vector2D, xPoint2: Vector2D,
            xDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimension = RotatedDimension(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            xPoint1 = xPoint1, xPoint2 = xPoint2, angle = toRadians(90.0),
            text = text,
            dimensionLineReferencePoint = Vector2D(x = xDimensionLine, y = xPoint1.y)
        )

        fun sequence(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            dimensionLineReferencePoint: Vector2D,
            angle: Double,
            text: String = "<>"
        ): RotatedDimensionSequenceStart = RotatedDimensionSequence.init(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            dimensionLineReferencePoint = dimensionLineReferencePoint,
            angle = angle, text = text
        )

        fun horizontalSequence(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            yDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimensionSequenceStart = RotatedDimensionSequence.init(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            dimensionLineReferencePoint = Vector2D(x = 0.0, y = yDimensionLine),
            angle = 0.0, text = text
        )

        fun verticalSequence(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            xDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimensionSequenceStart = RotatedDimensionSequence.init(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            dimensionLineReferencePoint = Vector2D(x = xDimensionLine, y = 0.0),
            angle = toRadians(90.0), text = text
        )
    }
}