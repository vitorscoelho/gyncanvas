package vitorscoelho.gyncanvas.math

import org.joml.Vector2d

internal class Vetor2DMutavel(x: Double = 0.0, y: Double = 0.0) {
    private val vetorJOML = Vector2d(x, y)

    var x: Double
        get() = this.vetorJOML.x
        set(value) {
            this.vetorJOML.x = value
        }
    var y: Double
        get() = this.vetorJOML.y
        set(value) {
            this.vetorJOML.y = value
        }

    fun set(x: Double, y: Double):Vetor2DMutavel {
        this.x = x
        this.y = y
        return this
    }

    fun set(novasCoordenadas: Vetor2DMutavel):Vetor2DMutavel {
        this.x = novasCoordenadas.x
        this.y = novasCoordenadas.y
        return this
    }

    fun normalizar(): Vetor2DMutavel {
        this.vetorJOML.normalize()
        return this
    }

    fun normalizar(comprimeto: Double): Vetor2DMutavel {
        this.vetorJOML.normalize(comprimeto)
        return this
    }

    fun toVetorImutavel(): Vector2D {
        return Vector2D(x = this.x, y = this.y)
    }
}