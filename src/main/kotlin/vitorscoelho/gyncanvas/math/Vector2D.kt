package vitorscoelho.gyncanvas.math

import org.joml.Vector2d
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2D(
    x: Double,
    y: Double
) {
    private val jomlVector = Vector2d(x, y)

    val x: Double
        get() = this.jomlVector.x

    val y: Double
        get() = this.jomlVector.y

    fun distance(otherVector: Vector2D): Double = distance(vector1 = this, vector2 = otherVector)

    fun distance(x: Double, y: Double): Double = distance(vector = this, x = x, y = y)

    fun createNewWithOffset(deltaX: Double = 0.0, deltaY: Double = 0.0): Vector2D {
        return Vector2D(
            x = this.x + deltaX,
            y = this.y + deltaY
        )
    }

    /**
     * @return Um novo vetor que representa 'this' rotacionada com o (0,0) sendo o pivô.
     */
    fun rotate(angulo: Double): Vector2D {
        return Vector2D(
            x = this.x * cos(angulo) - this.y * sin(angulo),
            y = this.x * sin(angulo) + this.y * cos(angulo)
        )
    }

    /**
     * Retorna uma instância de um vetor com as coordenadas com os valores:
     * * x = this.x + deltaX
     * * y = this.y + deltaY
     * @param deltaX a variação aplicada à abscissa
     * @param deltaY a variação aplicada à ordenada
     */
    fun plus(deltaX: Double = 0.0, deltaY: Double = 0.0) = Vector2D(x = x + deltaX, y = y + deltaY)

    operator fun plus(otherVector: Vector2D) = plus(deltaX = otherVector.x, deltaY = otherVector.y)
    operator fun minus(otherVector: Vector2D) = plus(deltaX = -otherVector.x, deltaY = -otherVector.y)

    fun transform(transformationMatrix: TransformationMatrix): Vector2D =
        transformationMatrix.transform(vector = this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Vector2D
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String = "{x: $x, y: $y}"

    companion object {
        val ZERO = Vector2D(x = 0.0, y = 0.0)

        fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            val deltaX = x1 - x2
            val deltaY = y1 - y2
            return sqrt(deltaX * deltaX + deltaY * deltaY)
        }

        fun distance(vector1: Vector2D, vector2: Vector2D): Double = distance(
            x1 = vector1.x, y1 = vector1.y,
            x2 = vector2.x, y2 = vector2.y
        )

        fun distance(vector: Vector2D, x: Double, y: Double): Double = distance(
            x1 = vector.x, y1 = vector.y,
            x2 = x, y2 = y
        )

        fun withRotation(x: Double, y: Double, angulo: Double): Vector2D {
            return Vector2D(
                x = x * cos(angulo) - y * sin(angulo),
                y = x * sin(angulo) + y * cos(angulo)
            )
        }

        fun withRotation(posicao: Vector2D, angulo: Double): Vector2D {
            return Vector2D.withRotation(
                x = posicao.x,
                y = posicao.y,
                angulo = angulo
            )
        }
    }
}