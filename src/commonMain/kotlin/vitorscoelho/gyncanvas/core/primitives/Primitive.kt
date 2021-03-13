package vitorscoelho.gyncanvas.core.primitives

import vitorscoelho.gyncanvas.math.Vector

//interface NewPrimitive {
//    val type: PrimitiveType
//    val verticesCount: Int
//    val vertices: List<Vertex>
//    val colors: List<Color>
//}

interface Primitive {
    val type: PrimitiveType
    val verticesCount: Int
    fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit)
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

//interface Primitive {
//    val type: PrimitiveType
//    val verticesCount: Int
//    val coordinates: FloatArray
//    val color: Color
//}

//interface Primitive2D {
////    val vertices: List<Vector2D>
//}

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

/**Linha com espessura fixa de 1px*/
//class Line(val startPoint: Vector, val endPoint: Vector, override val color: Color) : Primitive {
//    override val type: PrimitiveType get() = PrimitiveType.LINES
//    override val verticesCount: Int get() = 2
//    override val coordinates: FloatArray
//        get() = floatArrayOf(
//            startPoint.x.toFloat(), startPoint.y.toFloat(), startPoint.z.toFloat(),
//            endPoint.x.toFloat(), endPoint.y.toFloat(), endPoint.y.toFloat()
//        )
//}

//class Triangle(val p1: Vector, val p2: Vector, val p3: Vector, override val color: Color) : Primitive {
//    override val type: PrimitiveType get() = PrimitiveType.TRIANGLES
//    override val verticesCount: Int get() = 3
//    override val coordinates: FloatArray
//        get() = floatArrayOf(
//            p1.x.toFloat(), p1.y.toFloat(), p1.z.toFloat(),
//            p2.x.toFloat(), p2.y.toFloat(), p2.z.toFloat(),
//            p3.x.toFloat(), p3.y.toFloat(), p3.z.toFloat(),
//        )
//}

/**Linha com espessura*/
//class ThickLine(val startPoint: Vector, val endPoint: Vector, val width: Double, override val color: Color) : Primitive

sealed class Polyline : Primitive {
    abstract val points: List<Vector>
}

/**
 * APAGAR DEPOIS
 * Só pra n ter q ficar corrigindo as sublcasses de Primitive toda vez que faço alterações pra teste
 * */
class Dummy : Primitive {
    override val type: PrimitiveType
        get() = TODO("Not yet implemented")
    override val verticesCount: Int
        get() = TODO("Not yet implemented")

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        TODO("Not yet implemented")
    }
}

/**Polilinha aberta com espessura fixa de 1px*/
class OpenedPolyline(val points: List<Vector>, val color: Color) : Primitive by Dummy()

/**Polilinha aberta com espessura*/
//class OpenedThickPolyline(val points: List<Vector>, val width: Double, override val color: Color) : Primitive

/**Polilinha fechada com espessura fixa de 1px*/
class ClosedPolyline(val points: List<Vector>, val color: Color) : Primitive by Dummy()

/**Polilinha fechada com espessura*/
//class ClosedThickPolyline(val points: List<Vector>, val width: Double, override val color: Color) : Primitive

class StrokedCircle(val centerPoint: Vector, val radius: Double, val color: Color) : Primitive by Dummy()

//class SolidTriangle(val p1: Vector, val p2: Vector, val p3: Double, override val color: Color) : Primitive