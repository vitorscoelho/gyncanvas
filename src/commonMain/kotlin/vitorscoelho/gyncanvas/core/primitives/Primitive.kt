package vitorscoelho.gyncanvas.core.primitives

import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Para descrição, acessar o site:
 * https://webglfundamentals.org/webgl/lessons/webgl-points-lines-triangles.html
 */
enum class PrimitiveType {
    POINTS,
    LINES,
    LINE_STRIP,
    LINE_LOOP,
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN,
}

interface Primitive : Drawable {
    val type: PrimitiveType
    val verticesCount: Int
    fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit)
    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        action(this)
    }
}

interface Drawable {
    val primitivesCount: Int
        get() {
            var count = 0
            forEachPrimitive { count++ }
            return count
        }

    fun forEachPrimitive(action: (Primitive) -> Unit) {}
}

private fun actionOnVertex(
    index: Int,
    vector: Vector,
    color: Color,
    action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit
) {
    with(vector) {
        action(
            index,
            x.toFloat(), y.toFloat(), z.toFloat(),
            color.red, color.green, color.blue, color.alpha
        )
        println("${x.toFloat()} ${y.toFloat()} ${z.toFloat()} ${color.red} ${color.green} ${color.blue}")
    }
}

class Line(val startPoint: Vector, val endPoint: Vector, val color: Color) : Primitive {
    override val type: PrimitiveType get() = PrimitiveType.LINES
    override val verticesCount: Int get() = 2

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        actionOnVertex(0, startPoint, color, action)
        actionOnVertex(1, endPoint, color, action)
    }
}

class Triangle(val p1: Vector, val p2: Vector, val p3: Vector, val color: Color) : Primitive {
    override val type: PrimitiveType get() = PrimitiveType.TRIANGLES
    override val verticesCount: Int get() = 3
    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        actionOnVertex(0, p1, color, action)
        actionOnVertex(1, p2, color, action)
        actionOnVertex(2, p3, color, action)
    }
}

class Dummy : Primitive {
    override val type: PrimitiveType
        get() = TODO("Not yet implemented")
    override val verticesCount: Int
        get() = TODO("Not yet implemented")

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        TODO("Not yet implemented")
    }
}

interface Polyline

/**Polilinha aberta com espessura fixa de 1px*/
class OpenedPolyline(val points: List<Vector>, val color: Color) : Primitive by Dummy()

/**Polilinha aberta com espessura*/
//class OpenedThickPolyline(val points: List<Vector>, val width: Double, override val color: Color) : Primitive

/**Polilinha fechada com espessura fixa de 1px*/
class ClosedPolyline(val points: List<Vector>, val color: Color) : Primitive by Dummy()

/**Polilinha fechada com espessura*/
//class ClosedThickPolyline(val points: List<Vector>, val width: Double, override val color: Color) : Primitive

/*
class Triangle(val p1: Vector, val p2: Vector, val p3: Vector, val color: Color) : Primitive {
    override val type: PrimitiveType get() = PrimitiveType.TRIANGLES
    override val verticesCount: Int get() = 3
    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        actionOnVertex(0, p1, color, action)
        actionOnVertex(1, p2, color, action)
        actionOnVertex(2, p3, color, action)
    }
}
 */

class StrokedCircle(val centerPoint: Vector, val radius: Double, val color: Color) : Primitive {
    override val type: PrimitiveType get() = PrimitiveType.LINE_LOOP
    override val verticesCount: Int get() = 40 //TODO Estudar maneiras de poder usar o mínimo de vértices possível

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        val deltaAngle = 2f * PI.toFloat() / verticesCount.toFloat()
        (0 until verticesCount).forEach { index ->
            val angle = index * deltaAngle
            //TODO Mudar para possibilitar 3D, no futuro
            val vector = Vector2D(x = centerPoint.x + radius * cos(angle), y = centerPoint.y + radius * sin(angle))
            actionOnVertex(index, vector, color, action)
        }
    }
}

//class SolidTriangle(val p1: Vector, val p2: Vector, val p3: Double, override val color: Color) : Primitive