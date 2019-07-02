package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes

interface Primitiva {
    fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes)

    companion object {
        val NONE: Primitiva by lazy {
            object : Primitiva {
                override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {}
            }
        }
    }
}

interface PrimitivaStroke : Primitiva

interface PrimitivaText : Primitiva