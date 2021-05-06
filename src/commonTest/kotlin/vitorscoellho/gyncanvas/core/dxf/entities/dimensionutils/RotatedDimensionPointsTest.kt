package vitorscoellho.gyncanvas.core.dxf.entities.dimensionutils

import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionPoints
import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D
import vitorscoelho.gyncanvas.math.toRadians
import kotlin.test.Test
import kotlin.test.assertEquals

class RotatedDimensionPointsTest {
    private val DELTA = 0.0000001

    @Test
    fun test1() {
        val dimensionPoints = RotatedDimensionPoints(
            xPoint1 = Vector2D(x = 20.0, y = 14.0),
            xPoint2 = Vector2D(x = 47.3, y = 28.82),
            dimensionLineReferencePoint = Vector2D(x = -6.0, y = 2.6),
            angle = toRadians(13.0),
            extensionLinesOffsetFromOrigin = 0.625,
            extensionLinesExtendBeyondDimLines = 1.25,
            textOffsetFromDimLine = 0.625,
            overallScale = 1.0
        )
        with(dimensionPoints) {
            assertVector(xPoint1, 20.0, 14.0)
            assertVector(xPoint2, 47.3, 28.82)
            assertVector(point1DimensionLine, 21.18303814, 8.87569884)
            assertVector(point2DimensionLine, 50.34990707, 15.60940111)
            assertVector(midPointDimensionLine, 35.7664726, 12.24254998)
            assertVector(point1ExtensionLine1, 20.14059441, 13.39101871)
            assertVector(point2ExtensionLine1, 21.46422696, 7.65773626)
            assertVector(point1ExtensionLine2, 47.44059441, 28.21101871)
            assertVector(point2ExtensionLine2, 50.63109589, 14.39143853)
            assertVector(mTextPosition, 35.62587819, 12.85153127)
        }
    }

    private fun assertVector(vector: Vector, x: Double, y: Double) {
        assertEquals(x, vector.x, DELTA)
        assertEquals(y, vector.y, DELTA)
    }
}