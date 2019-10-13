package vitorscoelho.gyncanvas.core

import javafx.scene.transform.Affine
import org.joml.Matrix4d
import org.joml.Vector3d
import vitorscoelho.gyncanvas.math.Vector2D

class Transformacoes {
    private val matrizJOML = Matrix4d()
    private val vetorScaleJOML = Vector3d()

    var escala: Double = 1.0
        private set
        get() {
            matrizJOML.getScale(vetorScaleJOML)
            return vetorScaleJOML.z
        }

    /**
     * Aumenta (quando maior do que 1) ou diminui (quando menor do que 1) o
     * desenho apresentado na tela em relação às coordenadas do mundo. A escala
     * sempre acontece em relação ao ponto pivô.
     *
     * @param escala igual a 1 para a situação na qual as coordenadas do mundo
     * possuem a mesma dimensão das coordenadas de tela (pixels).
     */
    fun escalar(escala: Double, xPivo: Double, yPivo: Double): Transformacoes {
        matrizJOML.scaleAround(escala, xPivo, yPivo, 0.0)
        return this
    }

    fun transladar(translacaoX: Double, translacaoY: Double): Transformacoes {
        matrizJOML.translate(translacaoX, translacaoY, 0.0)
        return this
    }

    fun rotacionar(angulo: Double/*, xPivo: Double, yPivo: Double*/): Transformacoes {
//        val quaterniond = Quaterniond(0.0, 0.0, 2.0, 0.0)
//        matrizJOML.rotate(rotacao,xPivo,yPivo,0.0)
//        matrizJOML.rotateAroundLocal(quaterniond, xPivo, yPivo, 0.0)
//        matrizJOML.rotate(3.14, 0.0, 0.0, 1.0)
//        matrizJOML.rotate(quaterniond)
        matrizJOML.rotateZ(angulo)
        return this
    }

    fun espelhar(eixoX: Double, eixoY: Double, pontoX: Double, pontoY: Double): Transformacoes {
        matrizJOML.reflect(eixoY, eixoX, 0.0, pontoX, pontoY, 0.0)
        return this
    }

    fun coordenadasMundo(xTela: Double, yTela: Double): Vector2D {
        val vetorJOML = Vector3d(xTela, yTela, 0.0)
//        matrizJOML.transformPosition(vetorJOML)
        val matrizInversa = Matrix4d(matrizJOML).invert()
        matrizInversa.transformPosition(vetorJOML)
        return Vector2D(x = vetorJOML.x, y = vetorJOML.y)
    }

    fun toAffine(): Affine = with(matrizJOML) {
        Affine(
            m00(), m10(), m20(), m30(),
            m01(), m11(), m21(), m31(),
            m02(), m12(), m22(), m32()
        )
    }

    fun preencherAffine(affine: Affine) {
        with(matrizJOML) {
            affine.setToTransform(
                m00(), m10(), m20(), m30(),
                m01(), m11(), m21(), m31(),
                m02(), m12(), m22(), m32()
            )

        }
    }

    fun copy(): Transformacoes {
        val nova = Transformacoes()
        nova.matrizJOML.set(this@Transformacoes.matrizJOML)
        return nova
    }

    fun transformar(vector: Vector2D): Vector2D = transformar(x = vector.x, y = vector.y)

    fun transformar(x: Double, y: Double): Vector2D {
        val vetorJOM = Vector3d(x, y, 0.0)
        this.matrizJOML.transformPosition(vetorJOM)
        return Vector2D(x = vetorJOM.x, y = vetorJOM.y)
    }
}