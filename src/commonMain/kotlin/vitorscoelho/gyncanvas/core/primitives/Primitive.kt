package vitorscoelho.gyncanvas.core.primitives

import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.*

/*
Entities que falta fazer:
Hatch
Insert
LwPolyline
MText
 */

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

    fun forEachPrimitive(action: (Primitive) -> Unit)
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

private const val FLOAT_PI = PI.toFloat()
private const val MIN_DELTA_ANGLE_ARC = 2f * FLOAT_PI / 50f
private const val MIN_SEGMENTS_FOR_CURVE = 10

/*
Acessar o site abaixo para fazer um algoritmo que aplica fillet na polyline (já calculando o bulge):
https://forums.autodesk.com/t5/net/how-to-apply-fillet-to-lwpolyline-with-c/td-p/8962322
Para calcular o comprimento e a posição do centro da circunferência descrita pelo bulge:
https://www.afralisp.net/archive/lisp/Bulges1.htm
https://forums.autodesk.com/t5/autocad-lt-forum/bulge-center-dxf-net/td-p/8883298
 */
/**Polilinha com espessura fixa de 1px*/
class Polyline(val path: Path, val color: Color) : Primitive {
    override val type: PrimitiveType get() = if (path.closed) PrimitiveType.LINE_LOOP else PrimitiveType.LINE_STRIP
    override val verticesCount: Int get() = points.size

    private val points: List<Vector>
        get() = path.linearPoints(
            minAngleDiscretization = MIN_DELTA_ANGLE_ARC.toDouble(), minNSegments = MIN_SEGMENTS_FOR_CURVE
        )

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        points.forEachIndexed { index, vector -> actionOnVertex(index, vector, color, action) }
    }
}

/**Polilinha com espessura*/
//class ThickPolyline(val points: List<Vector>, val width: Double, override val color: Color) : Primitive

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