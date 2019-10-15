package vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils

import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

internal class RotatedDimensionPoints(
    val xPoint1: Vector2D,
    val xPoint2: Vector2D,
    val dimensionLineReferencePoint: Vector2D,
    val angle: Double,
    extensionLinesOffsetFromOrigin: Double,
    extensionLinesExtendBeyondDimLines: Double,
    textOffsetFromDimLine: Double,
    overallScale: Double
) {
    val extensionLinesOffsetFromOrigin = extensionLinesOffsetFromOrigin * overallScale
    val extensionLinesExtendBeyondDimLines = extensionLinesExtendBeyondDimLines * overallScale
    val textOffsetFromDimLine = textOffsetFromDimLine * overallScale

    val point1DimensionLine: Vector2D
    val point2DimensionLine: Vector2D
    val midPointDimensionLine: Vector2D
    val point1ExtensionLine1: Vector2D
    val point2ExtensionLine1: Vector2D
    val point1ExtensionLine2: Vector2D
    val point2ExtensionLine2: Vector2D
    val mTextPosition: Vector2D

    init {
        val originPoint = xPoint1
        val transformationMatrix: TransformationMatrix = MutableTransformationMatrix()
            .rotate(angle = -angle)
            .translate(xOffset = -originPoint.x, yOffset = -originPoint.y)
        val localXPoint1 = xPoint1.transform(transformationMatrix)
        val localXPoint2 = xPoint2.transform(transformationMatrix)
        val yDimensionLine = dimensionLineReferencePoint.transform(transformationMatrix).y

        val reverseTransformationMatrix: TransformationMatrix = MutableTransformationMatrix()
            .translate(xOffset = originPoint.x, yOffset = originPoint.y)
            .rotate(angle = angle)

        this.point1DimensionLine =
            Vector2D(x = localXPoint1.x, y = yDimensionLine).transform(reverseTransformationMatrix)
        this.point2DimensionLine =
            Vector2D(x = localXPoint2.x, y = yDimensionLine).transform(reverseTransformationMatrix)
        this.midPointDimensionLine = Vector2D.mid(point1DimensionLine, point2DimensionLine)

        val extensionLine1Points = extensionLinePoints(xPoint = localXPoint1, yDimensionLine = yDimensionLine)
        this.point1ExtensionLine1 = extensionLine1Points.first.transform(reverseTransformationMatrix)
        this.point2ExtensionLine1 = extensionLine1Points.second.transform(reverseTransformationMatrix)

        val extensionLine2Points = extensionLinePoints(xPoint = localXPoint2, yDimensionLine = yDimensionLine)
        this.point1ExtensionLine2 = extensionLine2Points.first.transform(reverseTransformationMatrix)
        this.point2ExtensionLine2 = extensionLine2Points.second.transform(reverseTransformationMatrix)

        this.mTextPosition = Vector2D(
            x = (localXPoint1.x + localXPoint2.x) / 2.0, y = yDimensionLine + textOffsetFromDimLine
        ).transform(reverseTransformationMatrix)
    }

    private fun extensionLinePoints(xPoint: Vector2D, yDimensionLine: Double): Pair<Vector2D, Vector2D> {
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