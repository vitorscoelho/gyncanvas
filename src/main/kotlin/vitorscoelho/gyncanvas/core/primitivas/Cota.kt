package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.max
import kotlin.math.min

interface Cota : Primitiva {
    val ponto1: Vetor2D
    val ponto2: Vetor2D
    val texto: String
    val propriedadesCotas: PropriedadesCotas
}
//TODO Cota não pode ser uma primitiva, mudar isto depois. Primitivas serão somente as opções que dá pra desenhar diretamente no GraphicsContext

class CotaHorizontal(
    override val ponto1: Vetor2D,
    override val ponto2: Vetor2D,
    yDimensionLine: Double,
    override val texto: String = "<>",
    override val propriedadesCotas: PropriedadesCotas
) : Cota {
    val yDimensionLine: Double
        get() = this.dimensionLine.ponto1.y

    private val dimensionLine = StrokedLine(
        ponto1 = Vetor2D(x = ponto1.x, y = yDimensionLine),
        ponto2 = Vetor2D(x = ponto2.x, y = yDimensionLine)
    )
    private val extensionLine1 = criarExtensionLine(
        definitionPoint = ponto1,
        yDimensionLine = yDimensionLine,
        offsetExtensionLine = propriedadesCotas.offsetExtensionLine
    )
    private val extensionLine2 = criarExtensionLine(
        definitionPoint = ponto2,
        yDimensionLine = yDimensionLine,
        offsetExtensionLine = propriedadesCotas.offsetExtensionLine
    )
    private val textoDesenho = FilledText(
        texto = textoCota(
            texto = texto,
            dc = propriedadesCotas.formatoNumero,
            distancia = abs(ponto1.x - ponto2.x) * propriedadesCotas.multiplicadorValor
        ),
        posicao = Vetor2D(
            x = (ponto1.x + ponto2.x) / 2.0,
            y = yDimensionLine + propriedadesCotas.offsetText
        ),
        angulo = 0.0
    )

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        dimensionLine.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine1.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine2.desenhar(gc = gc, transformacoes = transformacoes)
        textoDesenho.desenhar(gc = gc, transformacoes = transformacoes)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): CotaHorizontal {
        return CotaHorizontal(
            ponto1 = transformacoes.transformar(this.ponto1),
            ponto2 = transformacoes.transformar(this.ponto2),
            yDimensionLine = transformacoes.transformar(this.dimensionLine.ponto1).y,
            texto = this.texto,
            propriedadesCotas = this.propriedadesCotas
        )
    }

    companion object {
        //fun sequenciaCotas(cotaInicial:CotaHorizontal)
        //Bolar um método que faz algo do tipo: sequenciaCotas(cotaInicial).deltaX(12.0).x(56.0).delta(23.0)
        private fun criarExtensionLine(
            definitionPoint: Vetor2D,
            yDimensionLine: Double,
            offsetExtensionLine: Double
        ): StrokedLine {
            //val deltaY = yDimensionLine - definitionPoint.y
            val yDefinition = if (yDimensionLine > definitionPoint.y) {
                min(definitionPoint.y + offsetExtensionLine, yDimensionLine)
            } else {
                max(definitionPoint.y - offsetExtensionLine, yDimensionLine)
            }
            return StrokedLine(
                ponto1 = Vetor2D(x = definitionPoint.x, y = yDefinition),
                ponto2 = Vetor2D(x = definitionPoint.x, y = yDimensionLine)
            )
        }
    }
}

class CotaVertical(
    override val ponto1: Vetor2D,
    override val ponto2: Vetor2D,
    xDimensionLine: Double,
    override val texto: String = "<>",
    override val propriedadesCotas: PropriedadesCotas
) : Cota {
    val xDimensionLine: Double
        get() = dimensionLine.ponto1.x

    private val dimensionLine = StrokedLine(
        ponto1 = Vetor2D(x = xDimensionLine, y = ponto1.y),
        ponto2 = Vetor2D(x = xDimensionLine, y = ponto2.y)
    )
    private val extensionLine1 = criarExtensionLine(
        definitionPoint = ponto1,
        xDimensionLine = xDimensionLine,
        offsetExtensionLine = propriedadesCotas.offsetExtensionLine
    )
    private val extensionLine2 = criarExtensionLine(
        definitionPoint = ponto2,
        xDimensionLine = xDimensionLine,
        offsetExtensionLine = propriedadesCotas.offsetExtensionLine
    )
    private val textoDesenho = FilledText(
        texto = textoCota(
            texto = texto,
            dc = propriedadesCotas.formatoNumero,
            distancia = abs(ponto1.y - ponto2.y) * propriedadesCotas.multiplicadorValor
        ),
        posicao = Vetor2D(
            x = xDimensionLine - propriedadesCotas.offsetText,
            y = (ponto1.y + ponto2.y) / 2.0
        ),
        angulo = Math.PI / 2.0
    )

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        dimensionLine.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine1.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine2.desenhar(gc = gc, transformacoes = transformacoes)
        textoDesenho.desenhar(gc = gc, transformacoes = transformacoes)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): CotaVertical {
        return CotaVertical(
            ponto1 = transformacoes.transformar(this.ponto1),
            ponto2 = transformacoes.transformar(this.ponto2),
            xDimensionLine = transformacoes.transformar(this.dimensionLine.ponto1).x,
            texto = this.texto,
            propriedadesCotas = this.propriedadesCotas
        )
    }

    companion object {
        //fun sequenciaCotas(cotaInicial:CotaHorizontal)
        //Bolar um método que faz algo do tipo: sequenciaCotas(cotaInicial).deltaX(12.0).x(56.0).delta(23.0)
        private fun criarExtensionLine(
            definitionPoint: Vetor2D,
            xDimensionLine: Double,
            offsetExtensionLine: Double
        ): StrokedLine {
            val xDefinition = if (xDimensionLine > definitionPoint.x) {
                min(definitionPoint.x + offsetExtensionLine, xDimensionLine)
            } else {
                max(definitionPoint.x - offsetExtensionLine, xDimensionLine)
            }
            return StrokedLine(
                ponto1 = Vetor2D(x = xDefinition, y = definitionPoint.y),
                ponto2 = Vetor2D(x = xDimensionLine, y = definitionPoint.y)
            )
        }
    }
}

class CotaAlinhada(
    ponto1: Vetor2D,
    ponto2: Vetor2D,
    val offset: Double,
    texto: String = "<>",
    propriedadesCotas: PropriedadesCotas
) : Cota {
    //Até a inclinação de 90° com o eixo X, o texto tem um comportamento. O offset é positivo a esquerda.
    //Entre 90º e 180º com o eixo X, o texto tem outro comportamento. O offset é positivo a direita.
    private val cotaEmbarcada: Cota

    override val ponto1: Vetor2D
        get() = cotaEmbarcada.ponto1

    override val ponto2: Vetor2D
        get() = cotaEmbarcada.ponto2

    override val texto: String
        get() = cotaEmbarcada.texto

    override val propriedadesCotas: PropriedadesCotas
        get() = cotaEmbarcada.propriedadesCotas

    init {
        val delta = ponto2 - ponto1
        if (delta.x == 0.0) {
            cotaEmbarcada = CotaVertical(
                ponto1 = ponto1,
                ponto2 = ponto2,
                xDimensionLine = ponto1.x - offset,
                texto = texto,
                propriedadesCotas = propriedadesCotas
            )
        } else if (delta.y == 0.0) {
            cotaEmbarcada = CotaHorizontal(
                ponto1 = ponto1,
                ponto2 = ponto2,
                yDimensionLine = ponto1.y + offset,
                texto = texto,
                propriedadesCotas = propriedadesCotas
            )
        } else {
            cotaEmbarcada = CotaInclinada(
                ponto1 = ponto1,
                ponto2 = ponto2,
                offset = offset,
                texto = texto,
                delta = delta,
                propriedadesCotas = propriedadesCotas
            )
        }
    }

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        cotaEmbarcada.desenhar(gc = gc, transformacoes = transformacoes)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): CotaAlinhada {
        return CotaAlinhada(
            ponto1 = transformacoes.transformar(this.ponto1),
            ponto2 = transformacoes.transformar(this.ponto2),
            offset = this.offset * transformacoes.escala,
            texto = this.texto,
            propriedadesCotas = this.propriedadesCotas
        )
    }
}

private class CotaInclinada(
    override val ponto1: Vetor2D,
    override val ponto2: Vetor2D,
    delta: Vetor2D,
    offset: Double,
    override val texto: String = "<>",
    override val propriedadesCotas: PropriedadesCotas
) : Cota {
    private val dimensionLine: StrokedLine
    private val extensionLine1: StrokedLine
    private val extensionLine2: StrokedLine
    private val textoDesenho: FilledText

    init {
        require(delta.x != 0.0) { "Não foi possível criar uma cota inclinada, pois os pontos informados formam uma cota vertical (deltaX igual a zero)" }
        require(delta.y != 0.0) { "Não foi possível criar uma cota inclinada, pois os pontos informados formam uma cota horizontal (deltaY igual a zero)" }
        val distancia = ponto1.distancia(ponto2)
        //Considerando que a cota é horizontal, para depois rotacionar e transladar estes pontos para formar a cota
        val cotaHorizontalPonto1 = Vetor2D.ZERO
        val cotaHorizontalPonto2 = cotaHorizontalPonto1.somar(deltaX = distancia)
        val pontoTextoCotaHorizontal = Vetor2D(x = distancia / 2.0, y = offset + propriedadesCotas.offsetText)
        val definitionPoint1CotaHorizontal = cotaHorizontalPonto1.somar(deltaY = offset)
        val definitionPoint2CotaHorizontal = cotaHorizontalPonto2.somar(deltaY = offset)
        val yInicialLinhaDeCota =
            if (offset >= 0.0) {
                min(offset, propriedadesCotas.offsetExtensionLine)
            } else {
                max(offset, -propriedadesCotas.offsetExtensionLine)
            }
        val inicioLinhaDeCotaHorizontalPonto1 = cotaHorizontalPonto1.somar(deltaY = yInicialLinhaDeCota)
        val inicioLinhaDeCotaHorizontalPonto2 = cotaHorizontalPonto2.somar(deltaY = yInicialLinhaDeCota)
        val pontoDeGiroCotaReal = if (ponto1.x < ponto2.x) ponto1 else ponto2
        val outroPontoCotaReal = if (pontoDeGiroCotaReal === ponto1) ponto2 else ponto1
        val deltaRotacao = outroPontoCotaReal - pontoDeGiroCotaReal
        val anguloRotacao = atan(deltaRotacao.y / deltaRotacao.x)
        val definitionPoint1 = definitionPoint1CotaHorizontal.rotate(angulo = anguloRotacao) + pontoDeGiroCotaReal
        val definitionPoint2 = definitionPoint2CotaHorizontal.rotate(angulo = anguloRotacao) + pontoDeGiroCotaReal
        val inicioLinhaDeCotaPonto1 =
            inicioLinhaDeCotaHorizontalPonto1.rotate(angulo = anguloRotacao) + pontoDeGiroCotaReal
        val inicioLinhaDeCotaPonto2 =
            inicioLinhaDeCotaHorizontalPonto2.rotate(angulo = anguloRotacao) + pontoDeGiroCotaReal
        val pontoTexto = pontoTextoCotaHorizontal.rotate(angulo = anguloRotacao) + pontoDeGiroCotaReal

        dimensionLine = StrokedLine(
            ponto1 = definitionPoint1,
            ponto2 = definitionPoint2
        )
        extensionLine1 = StrokedLine(
            ponto1 = inicioLinhaDeCotaPonto1,
            ponto2 = definitionPoint1
        )
        extensionLine2 = StrokedLine(
            ponto1 = inicioLinhaDeCotaPonto2,
            ponto2 = definitionPoint2
        )
        this.textoDesenho = FilledText(
            texto = textoCota(
                texto = texto,
                dc = propriedadesCotas.formatoNumero,
                distancia = distancia
            ),
            posicao = pontoTexto,
            angulo = anguloRotacao
        )
    }

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        dimensionLine.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine1.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine2.desenhar(gc = gc, transformacoes = transformacoes)
        textoDesenho.desenhar(gc = gc, transformacoes = transformacoes)
    }

    override fun copiarComTransformacao(transformacoes: Transformacoes): CotaInclinada {
        //Desnecessário implementar esta função, já que a classe é privada e só utilizada internamente na classe CotaAlinhada
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private val textoValorCota: String by lazy { "<>" }

private fun textoCota(texto: String, dc: DecimalFormat, distancia: Double): String {
    val valorDistancia = dc.format(distancia)
    return texto.replaceFirst(textoValorCota, valorDistancia)
}

data class PropriedadesCotas(
    val offsetExtensionLine: Double,
    val offsetText: Double,
    val formatoNumero: DecimalFormat,
    val arrowSize: Double,
    val multiplicadorValor: Double = 1.0
)

//class SequenciaCotaHorizontal(
//    pontoInicial: Vetor2D,
//    val yDimensionLine: Double,
//    texto: String = "<>",
//    val propriedadesCotas: PropriedadesCotas
//) {
//    private val pontos = mutableListOf(pontoInicial)
//    private val textos = mutableListOf(texto)
//
//    fun add(ponto: Vetor2D, texto: String = "<>"): SequenciaCotaHorizontal {
//        pontos += ponto
//        textos += texto
//        return this
//    }
//
//    fun addDelta(deltaX: Double = 0.0, deltaY: Double = 0.0, texto: String = "<>"): SequenciaCotaHorizontal {
//        return add(ponto = pontos.last().somar(deltaX = deltaX, deltaY = deltaY), texto = texto)
//    }
//
//    fun add(x: Double, y: Double, texto: String = "<>"): SequenciaCotaHorizontal {
//        return add(ponto = Vetor2D(x = x, y = y), texto = texto)
//    }
//
//    fun toList(): List<CotaHorizontal> {
//        require(pontos.size > 1) { "A sequência de cotas tem apenas um ponto, mas deve ser mais do que um." }
//        val listaCotas = mutableListOf<CotaHorizontal>()
//        (0 until pontos.lastIndex).forEach { indice ->
//            listaCotas += CotaHorizontal(
//                ponto1 = pontos[indice],
//                ponto2 = pontos[indice + 1],
//                yDimensionLine = yDimensionLine,
//                texto = textos[indice],
//                propriedadesCotas = propriedadesCotas
//            )
//        }
//        return listaCotas
//    }
//}

abstract class SequenciaCota<T : Cota>(
    pontoInicial: Vetor2D,
    texto: String = "<>",
    val propriedadesCotas: PropriedadesCotas
) {
    private val pontos = mutableListOf(pontoInicial)
    private val textos = mutableListOf(texto)

    fun add(ponto: Vetor2D, texto: String = "<>"): SequenciaCota<T> {
        pontos += ponto
        textos += texto
        return this
    }

    fun addDelta(deltaX: Double = 0.0, deltaY: Double = 0.0, texto: String = "<>"): SequenciaCota<T> {
        return add(ponto = pontos.last().somar(deltaX = deltaX, deltaY = deltaY), texto = texto)
    }

    fun add(x: Double, y: Double, texto: String = "<>"): SequenciaCota<T> {
        return add(ponto = Vetor2D(x = x, y = y), texto = texto)
    }

    fun toList(): List<T> {
        require(pontos.size > 1) { "A sequência de cotas tem apenas um ponto, mas deveria ter mais do que um." }
        val listaCotas = mutableListOf<T>()
        (0 until pontos.lastIndex).forEach { indice ->
            listaCotas += criarCota(
                ponto1 = pontos[indice],
                ponto2 = pontos[indice + 1],
                texto = textos[indice]
            )
        }
        return listaCotas
    }

    protected abstract fun criarCota(ponto1: Vetor2D, ponto2: Vetor2D, texto: String): T
}

class SequenciaCotaHorizontal(
    pontoInicial: Vetor2D,
    val yDimensionLine: Double,
    texto: String = "<>",
    propriedadesCotas: PropriedadesCotas
) : SequenciaCota<CotaHorizontal>(
    pontoInicial = pontoInicial,
    texto = texto,
    propriedadesCotas = propriedadesCotas
) {
    override fun criarCota(ponto1: Vetor2D, ponto2: Vetor2D, texto: String) =
        CotaHorizontal(
            ponto1 = ponto1,
            ponto2 = ponto2,
            yDimensionLine = yDimensionLine,
            texto = texto,
            propriedadesCotas = propriedadesCotas
        )
}

class SequenciaCotaVertical(
    pontoInicial: Vetor2D,
    val xDimensionLine: Double,
    texto: String = "<>",
    propriedadesCotas: PropriedadesCotas
) : SequenciaCota<CotaVertical>(
    pontoInicial = pontoInicial,
    texto = texto,
    propriedadesCotas = propriedadesCotas
) {
    override fun criarCota(ponto1: Vetor2D, ponto2: Vetor2D, texto: String) =
        CotaVertical(
            ponto1 = ponto1,
            ponto2 = ponto2,
            xDimensionLine = xDimensionLine,
            texto = texto,
            propriedadesCotas = propriedadesCotas
        )
}

class SequenciaCotaAlinhada(
    pontoInicial: Vetor2D,
    val offset: Double,
    texto: String = "<>",
    propriedadesCotas: PropriedadesCotas
) : SequenciaCota<CotaAlinhada>(
    pontoInicial = pontoInicial,
    texto = texto,
    propriedadesCotas = propriedadesCotas
) {
    override fun criarCota(ponto1: Vetor2D, ponto2: Vetor2D, texto: String) =
        CotaAlinhada(
            ponto1 = ponto1,
            ponto2 = ponto2,
            offset = offset,
            texto = texto,
            propriedadesCotas = propriedadesCotas
        )
}