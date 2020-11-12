package vitorscoelho.gyncanvas.math

import com.soywiz.korma.geom.Point

internal class Vetor2DMutavel(x: Double = 0.0, y: Double = 0.0) {
    private val kormaVector = Point(x, y)

    var x: Double
        get() = this.kormaVector.x
        set(value) {
            this.kormaVector.x = value
        }
    var y: Double
        get() = this.kormaVector.y
        set(value) {
            this.kormaVector.y = value
        }

    fun set(x: Double, y: Double): Vetor2DMutavel {
        this.x = x
        this.y = y
        return this
    }

    fun set(novasCoordenadas: Vetor2DMutavel): Vetor2DMutavel {
        this.x = novasCoordenadas.x
        this.y = novasCoordenadas.y
        return this
    }

    fun normalizar(): Vetor2DMutavel {
        this.kormaVector.normalize()
        return this
    }

    fun normalizar(comprimento: Double): Vetor2DMutavel {
        this.kormaVector.normalize()
        this.kormaVector.mul(comprimento)
        return this
    }

    fun toVetorImutavel(): Vector2D {
        return Vector2D(x = this.x, y = this.y)
    }
}