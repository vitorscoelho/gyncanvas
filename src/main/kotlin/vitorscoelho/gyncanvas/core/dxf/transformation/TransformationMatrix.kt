package vitorscoelho.gyncanvas.core.dxf.transformation

import org.joml.Matrix4d
import org.joml.Vector3d
import vitorscoelho.gyncanvas.math.Vetor2D
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

    fun transform(vector: Vetor2D): Vetor2D = transform(x = vector.x, y = vector.y)

    fun transform(x: Double, y: Double) =
        Vetor2D(
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

class MutableTransformationMatrix : TransformationMatrix {
    constructor() {}
    constructor(otherMatrix: TransformationMatrix) {
        this.set(otherMatrix)
    }

    private val jomlMatrix = Matrix4d()
    private val jomlScaleVector = Vector3d()

    override var mxx: Double
        set(value) {
            jomlMatrix.m00(value)
        }
        get() = jomlMatrix.m00()
    override var mxy: Double
        set(value) {
            jomlMatrix.m10(value)
        }
        get() = jomlMatrix.m10()
    override var mxz: Double
        set(value) {
            jomlMatrix.m20(value)
        }
        get() = jomlMatrix.m20()
    override var tx: Double
        set(value) {
            jomlMatrix.m30(value)
        }
        get() = jomlMatrix.m30()
    override var myx: Double
        set(value) {
            jomlMatrix.m01(value)
        }
        get() = jomlMatrix.m01()
    override var myy: Double
        set(value) {
            jomlMatrix.m11(value)
        }
        get() = jomlMatrix.m11()
    override var myz: Double
        set(value) {
            jomlMatrix.m21(value)
        }
        get() = jomlMatrix.m21()
    override var ty: Double
        set(value) {
            jomlMatrix.m31(value)
        }
        get() = jomlMatrix.m31()
    override var mzx: Double
        set(value) {
            jomlMatrix.m02(value)
        }
        get() = jomlMatrix.m02()
    override var mzy: Double
        set(value) {
            jomlMatrix.m12(value)
        }
        get() = jomlMatrix.m12()
    override var mzz: Double
        set(value) {
            jomlMatrix.m22(value)
        }
        get() = jomlMatrix.m22()
    override var tz: Double
        set(value) {
            jomlMatrix.m32(value)
        }
        get() = jomlMatrix.m32()

    override var scale: Double = 1.0
        private set
        get() {
            jomlMatrix.getScale(jomlScaleVector)
            return jomlScaleVector.z
        }

    /**
     * Aumenta (quando maior do que 1) ou diminui (quando menor do que 1) o
     * desenho apresentado na tela em relação às coordenadas do mundo. A escala
     * sempre acontece em relação ao ponto pivô.
     *
     * @param escala igual a 1 para a situação na qual as coordenadas do mundo
     * possuem a mesma dimensão das coordenadas de tela (pixels).
     */
    fun scale(factor: Double, xOrigin: Double, yOrigin: Double): MutableTransformationMatrix {
        jomlMatrix.scaleAround(factor, xOrigin, yOrigin, 0.0)
        return this
    }

    fun translate(xOffset: Double, yOffset: Double): MutableTransformationMatrix {
        jomlMatrix.translate(xOffset, yOffset, 0.0)
        return this
    }

    fun rotate(angle: Double/*, xPivo: Double, yPivo: Double*/): MutableTransformationMatrix {
//        val quaterniond = Quaterniond(0.0, 0.0, 2.0, 0.0)
//        matrizJOML.rotate(rotacao,xPivo,yPivo,0.0)
//        matrizJOML.rotateAroundLocal(quaterniond, xPivo, yPivo, 0.0)
//        matrizJOML.rotate(3.14, 0.0, 0.0, 1.0)
//        matrizJOML.rotate(quaterniond)
        jomlMatrix.rotateZ(angle)
        return this
    }

    fun reflect(eixoX: Double, eixoY: Double, pontoX: Double, pontoY: Double): MutableTransformationMatrix {
        jomlMatrix.reflect(eixoY, eixoX, 0.0, pontoX, pontoY, 0.0)
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

    fun worldCoordinates(xDrawingArea: Double, yDrawingArea: Double): Vetor2D {
        val vetorJOML = Vector3d(xDrawingArea, yDrawingArea, 0.0)
//        matrizJOML.transformPosition(vetorJOML)
        val matrizInversa = Matrix4d(jomlMatrix).invert()
        matrizInversa.transformPosition(vetorJOML)
        return Vetor2D(x = vetorJOML.x, y = vetorJOML.y)
    }
}