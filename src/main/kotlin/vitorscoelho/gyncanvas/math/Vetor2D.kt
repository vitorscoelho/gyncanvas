package vitorscoelho.gyncanvas.math

import org.joml.Vector2d
import vitorscoelho.gyncanvas.core.Transformacoes
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vetor2D(
    x: Double,
    y: Double
) {
    private val vetorJOML = Vector2d(x, y)

    val x: Double
        get() = this.vetorJOML.x

    val y: Double
        get() = this.vetorJOML.y

    fun distancia(outroVetor: Vetor2D): Double {
        return this.distancia(x = outroVetor.x, y = outroVetor.y)
    }

    fun distancia(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    fun createNewWithOffset(deltaX: Double = 0.0, deltaY: Double = 0.0): Vetor2D {
        return Vetor2D(
            x = this.x + deltaX,
            y = this.y + deltaY
        )
    }

    /**
     * @return Um novo vetor que representa 'this' rotacionada com o (0,0) sendo o pivô.
     */
    fun rotate(angulo: Double): Vetor2D {
        return Vetor2D(
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
    fun somar(deltaX: Double = 0.0, deltaY: Double = 0.0) = Vetor2D(x = x + deltaX, y = y + deltaY)

    operator fun plus(outroVetor: Vetor2D) = somar(deltaX = outroVetor.x, deltaY = outroVetor.y)
    operator fun minus(outroVetor: Vetor2D) = somar(deltaX = -outroVetor.x, deltaY = -outroVetor.y)

    fun copiar(transformacoes: Transformacoes):Vetor2D{
        throw NotImplementedError()
    }

    companion object {
        val ZERO = Vetor2D(x = 0.0, y = 0.0)

        fun withRotation(x: Double, y: Double, angulo: Double): Vetor2D {
            return Vetor2D(
                x = x * cos(angulo) - y * sin(angulo),
                y = x * sin(angulo) + y * cos(angulo)
            )
        }

        fun withRotation(posicao: Vetor2D, angulo: Double): Vetor2D {
            return Vetor2D.withRotation(
                x = posicao.x,
                y = posicao.y,
                angulo = angulo
            )
        }
    }
}