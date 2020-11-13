package vitorscoelho.gyncanvas.core.dxf.transformation

import com.soywiz.korma.geom.*
import vitorscoelho.gyncanvas.math.Vector2D

sealed class TransformationMatrix {
    abstract val mxx: Double
    abstract val mxy: Double
    abstract val tx: Double
    abstract val myx: Double
    abstract val myy: Double
    abstract val ty: Double
    abstract val scale: Double

    fun transform(vector: Vector2D): Vector2D = transform(x = vector.x, y = vector.y)

    fun transform(x: Double, y: Double) =
        Vector2D(
            x = mxx * x + mxy * y + tx,
            y = myx * x + myy * y + ty
        )

    companion object {
        val IDENTITY: TransformationMatrix by lazy {
            ImmutableTransformationMatrix(
                mxx = 1.0, mxy = 0.0, tx = 0.0,
                myx = 0.0, myy = 1.0, ty = 0.0,
            )
        }
    }
}

class ImmutableTransformationMatrix(
    override val mxx: Double,
    override val mxy: Double,
    override val tx: Double,
    override val myx: Double,
    override val myy: Double,
    override val ty: Double,
) : TransformationMatrix() {
    constructor(otherMatrix: TransformationMatrix) : this(
        mxx = otherMatrix.mxx,
        mxy = otherMatrix.mxy,
        tx = otherMatrix.tx,
        myx = otherMatrix.myx,
        myy = otherMatrix.myy,
        ty = otherMatrix.ty,
    )

    //override val scale: Double by lazy { sqrt(mxz * mxz + myz * myz + mzz * mzz) }
    override val scale: Double
        get() = TODO("Not yet implemented")
}

/**
 * As transformações nesta classe devem ser inseridas na ordem inversa a que elas ocorrem de fato, por exemplo,
 * se o desejo for fazer uma transformação que translade e depois rotacione, deve-se fazer da seguinte forma:
 * MutableTransformationMatrix().rotate(....).translate(....)
 */
class MutableTransformationMatrix : TransformationMatrix {
    constructor() {}
    constructor(otherMatrix: TransformationMatrix) {
        this.set(otherMatrix)
    }

    private val kormaMatrix = Matrix()
//    private val jomlScaleVector = Vector3d() //TODO remover este trecho

    override var mxx: Double
        set(value) {
            kormaMatrix.a = value
        }
        get() = kormaMatrix.a
    override var mxy: Double
        set(value) {
            kormaMatrix.c = value
        }
        get() = kormaMatrix.c
    override var tx: Double
        set(value) {
            kormaMatrix.tx = value
        }
        get() = kormaMatrix.tx
    override var myx: Double
        set(value) {
            kormaMatrix.b = value
        }
        get() = kormaMatrix.b
    override var myy: Double
        set(value) {
            kormaMatrix.d = value
        }
        get() = kormaMatrix.d
    override var ty: Double
        set(value) {
            kormaMatrix.ty = value
        }
        get() = kormaMatrix.ty

    override var scale: Double = 1.0
        private set

    /**
     * Aumenta (quando maior do que 1) ou diminui (quando menor do que 1) o
     * desenho apresentado na tela em relação às coordenadas do mundo. A escala
     * sempre acontece em relação ao ponto pivô.
     *
     * @param escala igual a 1 para a situação na qual as coordenadas do mundo
     * possuem a mesma dimensão das coordenadas de tela (pixels).
     */
    fun scale(factor: Double, xOrigin: Double, yOrigin: Double): MutableTransformationMatrix {
        kormaMatrix
            .translate(xOrigin, yOrigin)
            .prescale(factor)
            .translate(-xOrigin, -yOrigin)
        scale = factor
        return this
    }

    fun translate(xOffset: Double, yOffset: Double): MutableTransformationMatrix {
        kormaMatrix.pretranslate(xOffset, yOffset)
        return this
    }

    fun rotate(angle: Double/*, xPivo: Double, yPivo: Double*/): MutableTransformationMatrix {
        kormaMatrix.prerotate(Angle(angle))
        return this
    }

    fun reflect(eixoX: Double, eixoY: Double, pontoX: Double, pontoY: Double): MutableTransformationMatrix {
//        jomlMatrix.reflect(eixoY, eixoX, 0.0, pontoX, pontoY, 0.0)
        TODO("Not yet implemented")
        return this
    }

    fun set(otherMatrix: TransformationMatrix) {
        mxx = otherMatrix.mxx
        mxy = otherMatrix.mxy
        tx = otherMatrix.tx
        myx = otherMatrix.myx
        myy = otherMatrix.myy
        ty = otherMatrix.ty
    }

    fun identity() {
        set(otherMatrix = TransformationMatrix.IDENTITY)
    }

    fun worldCoordinates(xDrawingArea: Double, yDrawingArea: Double): Vector2D {
//        val vetorJOML = Vector3d(xDrawingArea, yDrawingArea, 0.0)
//        val matrizInversa = Matrix4d(jomlMatrix).invert()
//        matrizInversa.transformPosition(vetorJOML)
//        return Vector2D(x = vetorJOML.x, y = vetorJOML.y)
        TODO("Not yet implemented")
    }
}