package vitorscoelho.gyncanvas.math

import org.junit.Test
import org.junit.Assert.*
import java.lang.Math.toRadians

class Vector2DTest {
    private val DELTA = 0.0000001

    @Test
    fun transform() {
        val originalVector1 = Vector2D(x = 20.0, y = 14.0)

        run {
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .translate(tx = 5.0, ty = 12.0)
            val vector = originalVector1.transform(transfomationMatrix)
            assertEquals(25.0, vector.x, DELTA)
            assertEquals(26.0, vector.y, DELTA)
        }
        run {
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .translate(tx = -20.0, ty = -14.0)
            val vector = originalVector1.transform(transfomationMatrix)
            assertEquals(0.0, vector.x, DELTA)
            assertEquals(0.0, vector.y, DELTA)
        }
        run {
            val transformationMatrix = TransformationMatrix.IDENTITY
                .rotate(toRadians(36.0))
            val vector = originalVector1.transform(transformationMatrix)
            assertEquals(7.95134636, vector.x, DELTA)
            assertEquals(23.08194297, vector.y, DELTA)
        }
        run {
            val transformationMatrix = TransformationMatrix.IDENTITY
                .scale(factor = 2.3, xOrigin = -1.0, yOrigin = 2.6)
            val vector = originalVector1.transform(transformationMatrix)
            assertEquals(47.3, vector.x, DELTA)
            assertEquals(28.82, vector.y, DELTA)
        }
        run {
            val vectorZero = Vector2D.ZERO
            val transfomationMatrix = TransformationMatrix.IDENTITY
                .rotate(25.0)
            val vector = originalVector1.transform(transfomationMatrix)
            assertEquals(0.0, vectorZero.x, DELTA)
            assertEquals(0.0, vectorZero.y, DELTA)
        }
    }
}