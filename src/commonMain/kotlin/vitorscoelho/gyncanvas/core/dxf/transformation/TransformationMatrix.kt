package vitorscoelho.gyncanvas.core.dxf.transformation

import com.soywiz.korma.geom.*
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.sqrt

sealed class TransformationMatrix {
    abstract val mxx: Double
    abstract val mxy: Double
    abstract val mxz: Double
    abstract val tx: Double
    abstract val myx: Double
    abstract val myy: Double
    abstract val myz: Double
    abstract val ty: Double
    abstract val mzx: Double
    abstract val mzy: Double
    abstract val mzz: Double
    abstract val tz: Double
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
                mxx = 1.0, mxy = 0.0, mxz = 0.0, tx = 0.0,
                myx = 0.0, myy = 1.0, myz = 0.0, ty = 0.0,
                mzx = 0.0, mzy = 0.0, mzz = 1.0, tz = 0.0
            )
        }
    }
}

class ImmutableTransformationMatrix(
    override val mxx: Double,
    override val mxy: Double,
    override val mxz: Double,
    override val tx: Double,
    override val myx: Double,
    override val myy: Double,
    override val myz: Double,
    override val ty: Double,
    override val mzx: Double,
    override val mzy: Double,
    override val mzz: Double,
    override val tz: Double
) : TransformationMatrix() {
    constructor(otherMatrix: TransformationMatrix) : this(
        mxx = otherMatrix.mxx,
        mxy = otherMatrix.mxy,
        mxz = otherMatrix.mxz,
        tx = otherMatrix.tx,
        myx = otherMatrix.myx,
        myy = otherMatrix.myy,
        myz = otherMatrix.myz,
        ty = otherMatrix.ty,
        mzx = otherMatrix.mzx,
        mzy = otherMatrix.mzy,
        mzz = otherMatrix.mzz,
        tz = otherMatrix.tz
    )

    override val scale: Double by lazy { sqrt(mxz * mxz + myz * myz + mzz * mzz) }
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

    private val jomlMatrix = Matrix3D()
//    private val jomlScaleVector = Vector3d() //TODO remover este trecho

    override var mxx: Double
        set(value) {
            jomlMatrix.v00 = value.toFloat()
        }
        get() = jomlMatrix.v00.toDouble()
    override var mxy: Double
        set(value) {
            jomlMatrix.v10 = value.toFloat()
        }
        get() = jomlMatrix.v10.toDouble()
    override var mxz: Double
        set(value) {
            jomlMatrix.v20 = value.toFloat()
        }
        get() = jomlMatrix.v20.toDouble()
    override var tx: Double
        set(value) {
            jomlMatrix.v30 = value.toFloat()
        }
        get() = jomlMatrix.v30.toDouble()
    override var myx: Double
        set(value) {
            jomlMatrix.v01 = value.toFloat()
        }
        get() = jomlMatrix.v01.toDouble()
    override var myy: Double
        set(value) {
            jomlMatrix.v11 = value.toFloat()
        }
        get() = jomlMatrix.v11.toDouble()
    override var myz: Double
        set(value) {
            jomlMatrix.v21 = value.toFloat()
        }
        get() = jomlMatrix.v21.toDouble()
    override var ty: Double
        set(value) {
            jomlMatrix.v31 = value.toFloat()
        }
        get() = jomlMatrix.v31.toDouble()
    override var mzx: Double
        set(value) {
            jomlMatrix.v02 = value.toFloat()
        }
        get() = jomlMatrix.v02.toDouble()
    override var mzy: Double
        set(value) {
            jomlMatrix.v12 = value.toFloat()
        }
        get() = jomlMatrix.v12.toDouble()
    override var mzz: Double
        set(value) {
            jomlMatrix.v22 = value.toFloat()
        }
        get() = jomlMatrix.v22.toDouble()
    override var tz: Double
        set(value) {
            jomlMatrix.v32 = value.toFloat()
        }
        get() = jomlMatrix.v32.toDouble()

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
//        jomlMatrix.scaleAround(factor, xOrigin, yOrigin, 0.0) //TODO remover este trecho depois de descobrir como escalar na ponto Origin
        jomlMatrix.setToScale(factor, factor, 1.0) // TODO talvez o w seja o fator
        return this
    }

    fun translate(xOffset: Double, yOffset: Double): MutableTransformationMatrix {
        jomlMatrix.translate(xOffset, yOffset, 0.0)
        return this
    }

    fun rotate(angle: Double/*, xPivo: Double, yPivo: Double*/): MutableTransformationMatrix {
        jomlMatrix.rotate(Angle(angle), 0.0, 0.0, 1.0)
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
        mxz = otherMatrix.mxz
        tx = otherMatrix.tx
        myx = otherMatrix.myx
        myy = otherMatrix.myy
        myz = otherMatrix.myz
        ty = otherMatrix.ty
        mzx = otherMatrix.mzx
        mzy = otherMatrix.mzy
        mzz = otherMatrix.mzz
        tz = otherMatrix.tz
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