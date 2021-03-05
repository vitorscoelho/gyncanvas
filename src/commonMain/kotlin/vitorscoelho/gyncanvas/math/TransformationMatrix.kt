package vitorscoelho.gyncanvas.math

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * As transformações nesta classe devem ser inseridas na ordem inversa a que elas ocorrem de fato, por exemplo,
 * se o desejo for fazer uma transformação que translade e depois rotacione, deve-se fazer da seguinte forma:
 * TransformationMatrix.IDENTITY.rotate(....).translate(....)
 */
class TransformationMatrix(
    mxx: Double, mxy: Double, tx: Double,
    myx: Double, myy: Double, ty: Double
) {
    private val array = arrayOf(
        doubleArrayOf(mxx, mxy, tx),
        doubleArrayOf(myx, myy, ty),
    )

    val mxx: Double get() = array[0][0]
    val mxy: Double get() = array[0][1]
    val tx: Double get() = array[0][2]
    val myx: Double get() = array[1][0]
    val myy: Double get() = array[1][1]
    val ty: Double get() = array[1][2]

    val scale: Double by lazy {
        get(0, 0).pow(2) + get(1, 0).pow(2)
    }//TODO melhorar para não dar problema com reflect. Ler sobre em https://math.stackexchange.com/questions/237369/given-this-transformation-matrix-how-do-i-decompose-it-into-translation-rotati

    fun get(row: Int, column: Int): Double {
        require(row in 0..2 && column in 0..2)
        if (row == 2) {
            return if (column == 2) 1.0 else 0.0
        }
        return array[row][column]
    }

    private fun mult(other: TransformationMatrix): TransformationMatrix {
        val finalArray = Array<DoubleArray>(size = matrixSize) { row ->
            DoubleArray(size = matrixSize) { column ->
                (0 until matrixSize).sumByDouble { this.get(row, it) * other.get(it, column) }
            }
        }
        return TransformationMatrix(
            mxx = finalArray[0][0], mxy = finalArray[0][1], tx = finalArray[0][2],
            myx = finalArray[1][0], myy = finalArray[1][1], ty = finalArray[1][2]
        )
    }

    fun translate(tx: Double = 0.0, ty: Double = 0.0): TransformationMatrix = mult(translationMatrix(tx, ty))
    fun scale(factor: Double): TransformationMatrix = mult(scaleMatrix(factor))
    fun rotate(angle: Double): TransformationMatrix = mult(rotationMatrix(angle))

    fun scale(factor: Double, xOrigin: Double, yOrigin: Double): TransformationMatrix {
        return translate(xOrigin, yOrigin).scale(factor).translate(-xOrigin, -yOrigin)
    }


    fun transform(vector: Vector2D): Vector2D = transform(x = vector.x, y = vector.y)

    fun transform(x: Double, y: Double) =
        Vector2D(
            x = mxx * x + mxy * y + tx,
            y = myx * x + myy * y + ty
        )

    companion object {
        private const val matrixSize = 3

        val IDENTITY: TransformationMatrix by lazy {
            TransformationMatrix(
                mxx = 1.0, mxy = 0.0, tx = 0.0,
                myx = 0.0, myy = 1.0, ty = 0.0,
            )
        }
    }
}

private fun translationMatrix(tx: Double, ty: Double) = TransformationMatrix(
    mxx = 1.0, mxy = 0.0, tx = tx,
    myx = 0.0, myy = 1.0, ty = ty
)

private fun scaleMatrix(factor: Double): TransformationMatrix {
    require(factor > 0.0)
    return TransformationMatrix(
        mxx = factor, mxy = 0.0, tx = 0.0,
        myx = 0.0, myy = factor, ty = 0.0
    )
}

private fun rotationMatrix(angle: Double): TransformationMatrix {
    val sin = sin(angle)
    val cos = cos(angle)
    return TransformationMatrix(
        mxx = cos, mxy = -sin, tx = 0.0,
        myx = sin, myy = cos, ty = 0.0
    )
}

//private fun reflectMatrix() //TODO implementar reflect