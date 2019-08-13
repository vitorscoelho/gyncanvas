package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D

class FilledText(val texto: String, val posicao: Vetor2D, val angulo: Double, tamanhoFixo: Boolean = false) :
    PrimitivaText {
    private val tipoTexto: TipoTexto = TipoTexto.getTipoTexto(angulo = angulo, tamanhoFixo = tamanhoFixo)

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        tipoTexto.desenhar(filledText = this, gc = gc, transformacoes = transformacoes)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): FilledText =
        FilledText(
            texto = this.texto,
            posicao = transformacoes.transformar(this.posicao),
            angulo = this.angulo,
            tamanhoFixo = (this.tipoTexto == TipoTexto.TEXTO_HORIZONTAL_TAMANHO_FIXO || this.tipoTexto == TipoTexto.TEXTO_ROTACIONADO_TAMANHO_FIXO)
        )
}

private enum class TipoTexto {
    TEXTO_HORIZONTAL {
        override fun desenhar(filledText: FilledText, gc: GraphicsContext, transformacoes: Transformacoes) {
            gc.fillText(filledText.texto, filledText.posicao.x, -filledText.posicao.y/*,larguraMaxima*/)
        }
    },
    TEXTO_ROTACIONADO {
        override fun desenhar(filledText: FilledText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = filledText.posicao.x, translacaoY = -filledText.posicao.y)
                .rotacionar(angulo = -filledText.angulo)
                .toAffine()
            gc.fillText(filledText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    },
    TEXTO_HORIZONTAL_TAMANHO_FIXO {
        override fun desenhar(filledText: FilledText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = filledText.posicao.x, translacaoY = -filledText.posicao.y)
                .escalar(escala = 1.0 / transformacoes.escala, xPivo = 0.0, yPivo = 0.0)
                .toAffine()
            gc.fillText(filledText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    },
    TEXTO_ROTACIONADO_TAMANHO_FIXO {
        override fun desenhar(filledText: FilledText, gc: GraphicsContext, transformacoes: Transformacoes) {
            val affineOriginal = gc.transform
            gc.transform = transformacoes.copy()
                .transladar(translacaoX = filledText.posicao.x, translacaoY = -filledText.posicao.y)
                .rotacionar(angulo = -filledText.angulo)
                .escalar(escala = 1.0 / transformacoes.escala, xPivo = 0.0, yPivo = 0.0)
                .toAffine()
            gc.fillText(filledText.texto, 0.0, 0.0/*,larguraMaxima*/)
            gc.transform = affineOriginal
        }
    };

    abstract fun desenhar(filledText: FilledText, gc: GraphicsContext, transformacoes: Transformacoes)

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