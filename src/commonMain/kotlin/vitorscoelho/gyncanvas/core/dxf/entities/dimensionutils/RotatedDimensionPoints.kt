package vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils

import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.PI

internal class RotatedDimensionPoints(
    val xPoint1: Vector,
    val xPoint2: Vector,
    val dimensionLineReferencePoint: Vector,
    val angle: Double,
    extensionLinesOffsetFromOrigin: Double,
    extensionLinesExtendBeyondDimLines: Double,
    textOffsetFromDimLine: Double,
    overallScale: Double
) {
    val extensionLinesOffsetFromOrigin = extensionLinesOffsetFromOrigin * overallScale
    val extensionLinesExtendBeyondDimLines = extensionLinesExtendBeyondDimLines * overallScale
    val textOffsetFromDimLine = textOffsetFromDimLine * overallScale

    val point1DimensionLine: Vector
    val point2DimensionLine: Vector
    val midPointDimensionLine: Vector
    val point1ExtensionLine1: Vector
    val point2ExtensionLine1: Vector
    val point1ExtensionLine2: Vector
    val point2ExtensionLine2: Vector
    val mTextPosition: Vector

    val rotationArrowHead1: Double
    val rotationArrowHead2: Double

    init {
        val originPoint = xPoint1
        val transformationMatrix: TransformationMatrix = TransformationMatrix.IDENTITY
            .rotate(angle = -angle)
            .translate(tx = -originPoint.x, ty = -originPoint.y)
        val localXPoint1 = xPoint1.transform(transformationMatrix)
        val localXPoint2 = xPoint2.transform(transformationMatrix)
        val yDimensionLine = dimensionLineReferencePoint.transform(transformationMatrix).y

        val reverseTransformationMatrix: TransformationMatrix = TransformationMatrix.IDENTITY
            .translate(tx = originPoint.x, ty = originPoint.y)
            .rotate(angle = angle)

        this.point1DimensionLine =
            Vector2D(x = localXPoint1.x, y = yDimensionLine).transform(reverseTransformationMatrix)
        this.point2DimensionLine =
            Vector2D(x = localXPoint2.x, y = yDimensionLine).transform(reverseTransformationMatrix)
        this.midPointDimensionLine = Vector.mid(point1DimensionLine, point2DimensionLine)

        val extensionLine1Points = extensionLinePoints(xPoint = localXPoint1, yDimensionLine = yDimensionLine)
        this.point1ExtensionLine1 = extensionLine1Points.first.transform(reverseTransformationMatrix)
        this.point2ExtensionLine1 = extensionLine1Points.second.transform(reverseTransformationMatrix)

        val extensionLine2Points = extensionLinePoints(xPoint = localXPoint2, yDimensionLine = yDimensionLine)
        this.point1ExtensionLine2 = extensionLine2Points.first.transform(reverseTransformationMatrix)
        this.point2ExtensionLine2 = extensionLine2Points.second.transform(reverseTransformationMatrix)

        this.mTextPosition = Vector2D(
            x = (localXPoint1.x + localXPoint2.x) / 2.0, y = yDimensionLine + textOffsetFromDimLine
        ).transform(reverseTransformationMatrix)

        if (extensionLine1Points.second.x <= extensionLine2Points.second.x) {
            this.rotationArrowHead1 = PI + angle
            this.rotationArrowHead2 = 0.0 + angle
        } else {
            this.rotationArrowHead1 = 0.0 + angle
            this.rotationArrowHead2 = PI + angle
        }
    }

    private fun extensionLinePoints(xPoint: Vector, yDimensionLine: Double): Pair<Vector, Vector> {
        val yPoint1: Double
        val yPoint2: Double
        if (yDimensionLine > xPoint.y) {
            yPoint1 = xPoint.y + extensionLinesOffsetFromOrigin
            yPoint2 = yDimensionLine + extensionLinesExtendBeyondDimLines
        } else {
            yPoint1 = xPoint.y - extensionLinesOffsetFromOrigin
            yPoint2 = yDimensionLine - extensionLinesExtendBeyondDimLines
        }
        return Pair(
            first = Vector2D(x = xPoint.x, y = yPoint1),
            second = Vector2D(x = xPoint.x, y = yPoint2)
        )
    }
}