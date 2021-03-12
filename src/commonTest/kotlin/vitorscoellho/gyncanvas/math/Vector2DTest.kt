package vitorscoellho.gyncanvas.math

import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D
import vitorscoelho.gyncanvas.math.toRadians
import vitorscoellho.doubleAssertEquals
import kotlin.test.Test

class Vector2DTest {
    private val DELTA = 0.0000001

    @Test
    fun transform() {
        val originalVector1 = Vector2D(x = 20.0, y = 14.0)

        run {
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .translate(tx = 5.0, ty = 12.0)
            val vector = originalVector1.transform(transfomationMatrix)
            doubleAssertEquals(25.0, vector.x, DELTA)
            doubleAssertEquals(26.0, vector.y, DELTA)
        }
        run {
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .translate(tx = -20.0, ty = -14.0)
            val vector = originalVector1.transform(transfomationMatrix)
            doubleAssertEquals(0.0, vector.x, DELTA)
            doubleAssertEquals(0.0, vector.y, DELTA)
        }
        run {
            val transformationMatrix = TransformationMatrix.IDENTITY
                .rotate(toRadians(36.0))
            val vector = originalVector1.transform(transformationMatrix)
            doubleAssertEquals(7.95134636, vector.x, DELTA)
            doubleAssertEquals(23.08194297, vector.y, DELTA)
        }
        run {
            val transformationMatrix = TransformationMatrix.IDENTITY
                .scale(factor = 2.3, xOrigin = -1.0, yOrigin = 2.6)
            val vector = originalVector1.transform(transformationMatrix)
            doubleAssertEquals(47.3, vector.x, DELTA)
            doubleAssertEquals(28.82, vector.y, DELTA)
        }
        run {
            val vectorZero = Vector2D.ZERO
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .rotate(25.0)
            val vector = originalVector1.transform(transfomationMatrix)
            doubleAssertEquals(0.0, vectorZero.x, DELTA)
            doubleAssertEquals(0.0, vectorZero.y, DELTA)
        }
    }
}