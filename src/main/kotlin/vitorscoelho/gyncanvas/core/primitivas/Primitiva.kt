package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes

interface Primitiva {
    fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes)
    /**
     * Cria uma primitiva com as mesmas características de **this**, porém com os pontos transformados de acordo com [transformacoes]
     */
    fun copiarComTransformacao(transformacoes: Transformacoes): Primitiva

    companion object {
        val NONE: Primitiva by lazy {
            object : Primitiva {
                override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {}
                override fun copiarComTransformacao(transformacoes: Transformacoes) = this
            }
        }
    }
}

interface PrimitivaStroke : Primitiva

interface PrimitivaText : Primitiva