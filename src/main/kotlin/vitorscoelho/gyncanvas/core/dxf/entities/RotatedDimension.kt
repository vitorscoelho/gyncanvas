package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionSequence
import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionSequenceStart
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D
import kotlin.math.absoluteValue
import java.lang.Math.toRadians

data class RotatedDimension(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    override val dimStyle: DimStyle,
    override val dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
    override val xPoint1: Vetor2D,
    override val xPoint2: Vetor2D,
    private val dimensionLineReferencePoint: Vetor2D,
    override val angle: Double,
    override val text: String = "<>"
) : LinearDimension {
    override val measurement: Double
    override val entities: List<Entity>

    init {
        /*
        Rotacionar os três pontos (xPoint1, xPoint2, dimensionLineReferencePoint) utilizando um deles como pivô,
        transformando a cota em uma HorizontalDimension.
        Criar todas as entidades necessárias para desenhar a dimension.
        Rotacionar todas as entidades da HorizontalDimension de volta para a posição original.
         */
        val originPoint = xPoint1
        val transformationMatrix: TransformationMatrix = MutableTransformationMatrix()
            .translate(xOffset = originPoint.x, yOffset = originPoint.y)
            .rotate(angle = -angle)
        val rotatedXPoint1 = xPoint1.transform(transformationMatrix)
        val rotatedXPoint2 = xPoint2.transform(transformationMatrix)
        val rotatedDimensionLineReferencePoint = dimensionLineReferencePoint.transform(transformationMatrix)

        val horizontalDimensionPoints = HorizontalDimensionPoints(
            deltaXPoint2 = rotatedXPoint2 - rotatedXPoint1,
            yDimensionLine = rotatedDimensionLineReferencePoint.y,
            extensionLinesOffsetFromOrigin = extensionLinesOffsetFromOrigin,
            extensionLinesExtendBeyondDimLines = extensionLinesExtendBeyondDimLines,
            textOffsetFromDimLine = textOffsetFromDimLine,
            overallScale = overallScale
        )
        val reverseTransformationMatrix: TransformationMatrix = MutableTransformationMatrix()
            .rotate(angle = angle)
            .translate(xOffset = originPoint.x, yOffset = originPoint.y)

        val dimensionLine: Line? = createDimensionLine(
            horizontalDimensionPoints = horizontalDimensionPoints,
            reverseTransformationMatrix = reverseTransformationMatrix
        )
        val extensionLine1: Line? = createExtensionLine(
            supress = extensionLinesSupressExtLine1,
            horizontalDimensionPoint1 = horizontalDimensionPoints.point1ExtensionLine1,
            horizontalDimensionPoint2 = horizontalDimensionPoints.point2ExtensionLine1,
            reverseTransformationMatrix = reverseTransformationMatrix
        )
        val extensionLine2: Line? = createExtensionLine(
            supress = extensionLinesSupressExtLine2,
            horizontalDimensionPoint1 = horizontalDimensionPoints.point1ExtensionLine2,
            horizontalDimensionPoint2 = horizontalDimensionPoints.point2ExtensionLine2,
            reverseTransformationMatrix = reverseTransformationMatrix
        )
        this.measurement = (horizontalDimensionPoints.xPoint1.x - horizontalDimensionPoints.xPoint2.x).absoluteValue
        val mText: MText? = createMText(
            content = text,
            horizontalDimensionPoint = horizontalDimensionPoints.mTextPosition,
            reverseTransformationMatrix = reverseTransformationMatrix,
            measurement = measurement
        )
        this.entities = listOfNotNull(dimensionLine, extensionLine1, extensionLine2, mText)
    }

    override fun dimContinue(point: Vetor2D): RotatedDimension = dimContinueXPoint2(point)

    override fun dimContinueXPoint1(point: Vetor2D): RotatedDimension {
        return copy(xPoint1 = this.xPoint1, xPoint2 = point)
    }

    override fun dimContinueXPoint2(point: Vetor2D): RotatedDimension {
        return copy(xPoint1 = this.xPoint2, xPoint2 = point)
    }

    override fun createSequence(points: List<Vetor2D>): List<RotatedDimension> {
        val dimensions = mutableListOf(this)
        points.forEach { dimensions.add(dimensions.last().dimContinue(it)) }
        return dimensions
    }

    override fun initSequence(): RotatedDimensionSequence = RotatedDimensionSequence.init(
        layer = layer, color = color,
        dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
        dimensionLineReferencePoint = dimensionLineReferencePoint, angle = angle, text = text
    ).firstPoint(xPoint1).next(xPoint2)

    private fun createDimensionLine(
        horizontalDimensionPoints: HorizontalDimensionPoints,
        reverseTransformationMatrix: TransformationMatrix
    ): Line? {
        val point1: Vetor2D
        val point2: Vetor2D
        if (this.dimensionLinesSupressDimLine1 && this.dimensionLinesSupressDimLine2) return null
        if (!this.dimensionLinesSupressDimLine1 && !this.dimensionLinesSupressDimLine2) {
            point1 = horizontalDimensionPoints.point1DimensionLine
            point2 = horizontalDimensionPoints.point2DimensionLine
        } else if (this.dimensionLinesSupressDimLine1) {
            point1 = horizontalDimensionPoints.midPointDimensionLine
            point2 = horizontalDimensionPoints.point2DimensionLine
        } else {
            point1 = horizontalDimensionPoints.point1DimensionLine
            point2 = horizontalDimensionPoints.midPointDimensionLine
        }
        return Line(
            layer = layer,
            color = dimensionLinesColor,
            startPoint = point1.transform(reverseTransformationMatrix),
            endPoint = point2.transform(reverseTransformationMatrix)
        )
    }

    private fun createExtensionLine(
        supress: Boolean,
        horizontalDimensionPoint1: Vetor2D,
        horizontalDimensionPoint2: Vetor2D,
        reverseTransformationMatrix: TransformationMatrix
    ): Line? {
        if (supress) return null
        return Line(
            layer = layer,
            color = extensionLinesColor,
            startPoint = horizontalDimensionPoint1.transform(reverseTransformationMatrix),
            endPoint = horizontalDimensionPoint2.transform(reverseTransformationMatrix)
        )
    }

    private fun createMText(
        content: String,
        horizontalDimensionPoint: Vetor2D,
        reverseTransformationMatrix: TransformationMatrix,
        measurement: Double
    ): MText? {
        return MText(
            layer = layer,
            color = textColor,
            style = textStyle,
            size = textHeight * overallScale,
            justify = AttachmentPoint.BOTTOM_CENTER,
            rotation = angle,
            position = horizontalDimensionPoint.transform(reverseTransformationMatrix),
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
            xPoint1: Vetor2D, xPoint2: Vetor2D,
            yDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimension = RotatedDimension(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            xPoint1 = xPoint1, xPoint2 = xPoint2, angle = 0.0,
            text = text,
            dimensionLineReferencePoint = Vetor2D(x = xPoint1.x, y = yDimensionLine)
        )

        fun vertical(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            xPoint1: Vetor2D, xPoint2: Vetor2D,
            xDimensionLine: Double,
            text: String = "<>"
        ): RotatedDimension = RotatedDimension(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            xPoint1 = xPoint1, xPoint2 = xPoint2, angle = toRadians(90.0),
            text = text,
            dimensionLineReferencePoint = Vetor2D(x = xDimensionLine, y = xPoint1.y)
        )

        fun sequence(
            layer: Layer, color: Color = Color.BY_LAYER,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
            dimensionLineReferencePoint: Vetor2D,
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
            dimensionLineReferencePoint = Vetor2D(x = 0.0, y = yDimensionLine),
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
            dimensionLineReferencePoint = Vetor2D(x = xDimensionLine, y = 0.0),
            angle = toRadians(90.0), text = text
        )
    }
}

/**
 * Representa uma cota horizontal que tem o primeiro ponto na origem (0,0).
 */
private class HorizontalDimensionPoints(
    deltaXPoint2: Vetor2D,
    private val yDimensionLine: Double,
    private val extensionLinesOffsetFromOrigin: Double,
    private val extensionLinesExtendBeyondDimLines: Double,
    private val textOffsetFromDimLine: Double,
    private val overallScale: Double
) {
    val xPoint1 = Vetor2D.ZERO
    val xPoint2 = xPoint1 + deltaXPoint2
    val point1DimensionLine: Vetor2D = Vetor2D(x = xPoint1.x, y = yDimensionLine)
    val point2DimensionLine: Vetor2D = Vetor2D(x = xPoint2.x, y = yDimensionLine)
    val midPointDimensionLine: Vetor2D = Vetor2D(x = (xPoint1.x + xPoint2.x) / 2.0, y = yDimensionLine)
    val point1ExtensionLine1: Vetor2D
    val point2ExtensionLine1: Vetor2D
    val point1ExtensionLine2: Vetor2D
    val point2ExtensionLine2: Vetor2D
    val mTextPosition = Vetor2D(
        x = (xPoint1.x + xPoint2.x) / 2.0,
        y = yDimensionLine + textOffsetFromDimLine * overallScale
    )

    init {
        val (point1Line1, point2Line1) = extensionLinePoints(xPoint = xPoint1)
        this.point1ExtensionLine1 = point1Line1
        this.point2ExtensionLine1 = point2Line1
        val (point1Line2, point2Line2) = extensionLinePoints(xPoint = xPoint2)
        this.point1ExtensionLine2 = point1Line2
        this.point2ExtensionLine2 = point2Line2
    }

    private fun extensionLinePoints(xPoint: Vetor2D): Pair<Vetor2D, Vetor2D> {
        val yPoint1: Double
        val yPoint2: Double
        if (yDimensionLine > xPoint.y) {
            yPoint1 = xPoint.y + extensionLinesOffsetFromOrigin * overallScale
            yPoint2 = yDimensionLine + extensionLinesExtendBeyondDimLines * overallScale
        } else {
            yPoint1 = xPoint.y - extensionLinesOffsetFromOrigin * overallScale
            yPoint2 = yDimensionLine - extensionLinesExtendBeyondDimLines * overallScale
        }
        return Pair(
            first = Vetor2D(x = xPoint.x, y = yPoint1),
            second = Vetor2D(x = xPoint.x, y = yPoint2)
        )
    }
}