package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class FillText(val texto: String, val posicao: Vetor2D, val angulo: Double, tamanhoFixo: Boolean = false) :
    PrimitivaText {
    private val tipoTexto: TipoTexto = TipoTexto.getTipoTexto(angulo = angulo, tamanhoFixo = tamanhoFixo)

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        tipoTexto.desenhar(fillText = this, gc = gc, transformacoes = transformacoes)
    }
}

private enum class TipoTexto {
    TEXTO_HORIZONTAL {
        override fun desenhar(fillText: FillText, gc: GraphicsContext, transformacoes: Transformacoes) {
            gc.fillText(fillText.texto, fillText.posicao.x, -fillText.posicao.y/*,larguraMaxima*/)
        }
    },
    TEXTO_ROTACIONADO {
        override fun desenhar(fillText: FillText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = fillText.posicao.x, translacaoY = -fillText.posicao.y)
                .rotacionar(angulo = -fillText.angulo)
                .toAffine()
            gc.fillText(fillText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    },
    TEXTO_HORIZONTAL_TAMANHO_FIXO {
        override fun desenhar(fillText: FillText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = fillText.posicao.x, translacaoY = -fillText.posicao.y)
                .escalar(escala = 1.0 / transformacoes.escala, xPivo = 0.0, yPivo = 0.0)
                .toAffine()
            gc.fillText(fillText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    },
    TEXTO_ROTACIONADO_TAMANHO_FIXO {
        override fun desenhar(fillText: FillText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = fillText.posicao.x, translacaoY = -fillText.posicao.y)
                .rotacionar(angulo = -fillText.angulo)
                .escalar(escala = 1.0 / transformacoes.escala, xPivo = 0.0, yPivo = 0.0)
                .toAffine()
            gc.fillText(fillText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    };

    abstract fun desenhar(fillText: FillText, gc: GraphicsContext, transformacoes: Transformacoes)

    companion object {
        fun getTipoTexto(angulo: Double, tamanhoFixo: Boolean): TipoTexto {
            if (tamanhoFixo) {
                return when (angulo == 0.0) {
                    true -> TEXTO_HORIZONTAL_TAMANHO_FIXO
                    false -> TEXTO_ROTACIONADO_TAMANHO_FIXO
                }
            } else {
                return when (angulo == 0.0) {
                    true -> TEXTO_HORIZONTAL
                    false -> TEXTO_ROTACIONADO
                }
            }
        }
    }
}