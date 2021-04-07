package vitorscoelho.gyncanvas.math

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

interface Vector {
    val x: Double
    val y: Double
    val z: Double

    fun distance(otherVector: Vector): Double = distance(vector1 = this, vector2 = otherVector)

    fun distance(x: Double, y: Double): Double = distance(vector = this, x = x, y = y)

    companion object {
        val ZERO: Vector get() = Vector2D(x = 0.0, y = 0.0)
        val X_AXIS: Vector get() = Vector2D(x = 1.0, y = 0.0)
        val Y_AXIS: Vector get() = Vector2D(x = 0.0, y = 1.0)
        val Z_AXIS: Vector get() = Vector3D(x = 0.0, y = 0.0, z = 1.0)

        fun distance(x1: Double, y1: Double, z1: Double = 0.0, x2: Double, y2: Double, z2: Double = 0.0): Double {
            val deltaX = x1 - x2
            val deltaY = y1 - y2
            val deltaZ = z1 - z2
            return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
        }

        fun distance(vector1: Vector, vector2: Vector): Double = distance(
            x1 = vector1.x, y1 = vector1.y, z1 = vector1.z,
            x2 = vector2.x, y2 = vector2.y, z2 = vector2.z
        )

        fun distance(vector: Vector, x: Double, y: Double, z: Double = 0.0): Double = distance(
            x1 = vector.x, y1 = vector.y, z1 = vector.z,
            x2 = x, y2 = y, z2 = z
        )

        fun mid(x1: Double, y1: Double, z1: Double = 0.0, x2: Double, y2: Double, z2: Double = 0.0): Vector {
            val x = (x1 + x2) / 2.0
            val y = (y1 + y2) / 2.0
            if (z1 == 0.0 && z2 == 0.0) return Vector2D(x = x, y = y)
            val z = (z1 + z2) / 2.0
            return Vector3D(x = x, y = y, z = z)
        }

        fun mid(vector1: Vector, vector2: Vector): Vector =
            mid(x1 = vector1.x, y1 = vector1.y, z1 = vector1.z, x2 = vector2.x, y2 = vector2.y, z2 = vector2.z)

        fun mid(vector: Vector, x: Double, y: Double, z: Double): Vector =
            mid(x1 = vector.x, y1 = vector.y, z1 = vector.z, x2 = x, y2 = y, z2 = z)
    }
}

class Vector3D(override val x: Double, override val y: Double, override val z: Double) : Vector

class Vector2D(override val x: Double, override val y: Double) : Vector {
    override val z: Double get() = 0.0

    fun angle(other: Vector2D): Double {
        val dot = x * other.x + y * other.y
        val det = x * other.y - y * other.x
        return atan2(det, dot)
    }

    val norm: Double get() = sqrt(x * x + y * y)

    fun normalized(length: Double): Vector2D {
        val invLength = length / sqrt(x * x + y * y)
        return Vector2D(x = x * invLength, y = y * invLength)
    }

    fun normalized(): Vector2D = normalized(length = 1.0)

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
    operator fun times(factor: Double) = Vector2D(x = factor * x, y = factor * y)

    fun transform(transformationMatrix: TransformationMatrix): Vector2D =
        transformationMatrix.transform(vector = this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
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