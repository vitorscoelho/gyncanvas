package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.max
import kotlin.math.min

interface Cota : Primitiva
//TODO Cota não pode ser uma primitiva, mudar isto depois. Primitivas serão somente as opções que dá pra desenhar diretamente no GraphicsContext

class CotaHorizontal(
    ponto1: Vetor2D,
    ponto2: Vetor2D,
    yDimensionLine: Double,
    offsetExtensionLine: Double,
    offsetText: Double,
    texto: String = "<>",
    formatoNumero: DecimalFormat,
    arrowSize: Double,
    multiplicadorValor: Double = 1.0
) : Cota {
    private val dimensionLine = StrokeLine(
        ponto1 = Vetor2D(x = ponto1.x, y = yDimensionLine),
        ponto2 = Vetor2D(x = ponto2.x, y = yDimensionLine)
    )
    private val extensionLine1 = criarExtensionLine(
        definitionPoint = ponto1,
        yDimensionLine = yDimensionLine,
        offsetExtensionLine = offsetExtensionLine
    )
    private val extensionLine2 = criarExtensionLine(
        definitionPoint = ponto2,
        yDimensionLine = yDimensionLine,
        offsetExtensionLine = offsetExtensionLine
    )
    private val texto = FillText(
        texto = textoCota(texto = texto, dc = formatoNumero, distancia = abs(ponto1.x - ponto2.x) * multiplicadorValor),
        posicao = Vetor2D(
            x = (ponto1.x + ponto2.x) / 2.0,
            y = yDimensionLine + offsetText
        ),
        angulo = 0.0
    )

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        dimensionLine.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine1.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine2.desenhar(gc = gc, transformacoes = transformacoes)
        texto.desenhar(gc = gc, transformacoes = transformacoes)
    }

    companion object {
        //fun sequenciaCotas(cotaInicial:CotaHorizontal)
        //Bolar um método que faz algo do tipo: sequenciaCotas(cotaInicial).deltaX(12.0).x(56.0).delta(23.0)
        private fun criarExtensionLine(
            definitionPoint: Vetor2D,
            yDimensionLine: Double,
            offsetExtensionLine: Double
        ): StrokeLine {
            //val deltaY = yDimensionLine - definitionPoint.y
            val yDefinition = if (yDimensionLine > definitionPoint.y) {
                min(definitionPoint.y + offsetExtensionLine, yDimensionLine)
            } else {
                max(definitionPoint.y - offsetExtensionLine, yDimensionLine)
            }
            return StrokeLine(
                ponto1 = Vetor2D(x = definitionPoint.x, y = yDefinition),
                ponto2 = Vetor2D(x = definitionPoint.x, y = yDimensionLine)
            )
        }
    }
}

class CotaVertical(
    ponto1: Vetor2D,
    ponto2: Vetor2D,
    xDimensionLine: Double,
    offsetExtensionLine: Double,
    offsetText: Double,
    texto: String = "<>",
    formatoNumero: DecimalFormat,
    arrowSize: Double,
    multiplicadorValor: Double = 1.0
) : Cota {
    private val dimensionLine = StrokeLine(
        ponto1 = Vetor2D(x = xDimensionLine, y = ponto1.y),
        ponto2 = Vetor2D(x = xDimensionLine, y = ponto2.y)
    )
    private val extensionLine1 = criarExtensionLine(
        definitionPoint = ponto1,
        xDimensionLine = xDimensionLine,
        offsetExtensionLine = offsetExtensionLine
    )
    private val extensionLine2 = criarExtensionLine(
        definitionPoint = ponto2,
        xDimensionLine = xDimensionLine,
        offsetExtensionLine = offsetExtensionLine
    )
    private val texto = FillText(
        texto = textoCota(texto = texto, dc = formatoNumero, distancia = abs(ponto1.y - ponto2.y) * multiplicadorValor),
        posicao = Vetor2D(
            x = xDimensionLine - offsetText,
            y = (ponto1.y + ponto2.y) / 2.0
        ),
        angulo = Math.PI / 2.0
    )

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        dimensionLine.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine1.desenhar(gc = gc, transformacoes = transformacoes)
        extensionLine2.desenhar(gc = gc, transformacoes = transformacoes)
        texto.desenhar(gc = gc, transformacoes = transformacoes)
    }

    companion object {
        //fun sequenciaCotas(cotaInicial:CotaHorizontal)
        //Bolar um método que faz algo do tipo: sequenciaCotas(cotaInicial).deltaX(12.0).x(56.0).delta(23.0)
        private fun criarExtensionLine(
            definitionPoint: Vetor2D,
            xDimensionLine: Double,
            offsetExtensionLine: Double
        ): StrokeLine {
            val xDefinition = if (xDimensionLine > definitionPoint.x) {
                min(definitionPoint.x + offsetExtensionLine, xDimensionLine)
            } else {
                max(definitionPoint.x - offsetExtensionLine, xDimensionLine)
            }
            return StrokeLine(
                ponto1 = Vetor2D(x = xDefinition, y = definitionPoint.y),
                ponto2 = Vetor2D(x = xDimensionLine, y = definitionPoint.y)
            )
        }
    }
}

class CotaAlinhada(
    ponto1: Vetor2D,
    ponto2: Vetor2D,
    offset: Double,
    offsetExtensionLine: Double,
    offsetText: Double,
    texto: String = "<>",
    formatoNumero: DecimalFormat,
    arrowSize: Double,
    multiplicadorValor: Double = 1.0
) : Cota {
    //Até a inclinação de 90° com o eixo X, o texto tem um comportamento. O offset é positivo a esquerda.
    //Entre 90º e 180º com o eixo X, o texto tem outro comportamento. O offset é positivo a direita.
    private val cotaEmbarcada: Cota

    init {
        val delta = ponto2 - ponto1
        if (delta.x == 0.0) {
            cotaEmbarcada = CotaVertical(
                ponto1 = ponto1,
                ponto2 = ponto2,
                xDimensionLine = ponto1.x - offset,
                offsetExtensionLine = offsetExtensionLine,
                offsetText = offsetText,
                texto = texto,
                formatoNumero = formatoNumero,
                arrowSize = arrowSize,
                multiplicadorValor = multiplicadorValor
            )
        } else if (delta.y == 0.0) {
            cotaEmbarcada = CotaHorizontal(
                ponto1 = ponto1,
                ponto2 = ponto2,
                yDimensionLine = ponto1.y + offset,
                offsetExtensionLine = offsetExtensionLine,
                offsetText = offsetText,
                texto = texto,
                formatoNumero = formatoNumero,
                arrowSize = arrowSize,
                multiplicadorValor = multiplicadorValor
            )
        } else {
            cotaEmbarcada = CotaInclinada(
                ponto1 = ponto1,
                ponto2 = ponto2,
                offset = offset,
                offsetExtensionLine = offsetExtensionLine,
                offsetText = offsetText,
                texto = texto,
                formatoNumero = formatoNumero,
                arrowSize = arrowSize,
                multiplicadorValor = multiplicadorValor,
                delta = delta
            )
        }
    }

    override fun desenhar(gc: GraphicsContext, transformacoes: Transformacoes) {
        cotaEmbarcada.desenhar(gc = gc, transformacoes = transformacoes)
    }
}

private class CotaInclinada(
    ponto1: Vetor2D,
    ponto2: Vetor2D,
    delta: Vetor2D,
    offset: Double,
    offsetExtensionLine: Double,
    offsetText: Double,
    texto: String = "<>",
    formatoNumero: DecimalFormat,
    arrowSize: Double,
    multiplicadorValor: Double = 1.0
) : Cota {
    private val dimensionLine: StrokeLine
    private val extensionLine1: StrokeLine
    private val extensionLine2: StrokeLine
    private val texto: FillText

    init {
        require(delta.x != 0.0) { "Não foi possível criar uma cota inclinada, pois os pontos informados formam uma cota vertical (deltaX igual a zero)" }
        require(delta.y != 0.0) { "Não foi possível criar uma cota inclinada, pois os pontos informados formam uma cota horizontal (deltaY igual a zero)" }
        val distancia = ponto1.distancia(ponto2)
        //Considerando que a cota é horizontal, para depois rotacionar e transladar estes pontos para formar a cota
        val cotaHorizontalPonto1 = Vetor2D.ZERO
        val cotaHorizontalPonto2 = cotaHorizontalPonto1.somar(deltaX = distancia)
        val pontoTextoCotaHorizontal = Vetor2D(x = distancia / 2.0, y = offset + offsetText)
        val definitionPoint1CotaHorizontal = cotaHorizontalPonto1.somar(deltaY = offset)
        val definitionPoint2CotaHorizontal = cotaHorizontalPonto2.somar(deltaY = offset)
        val yInicialLinhaDeCota =
            if (offset >= 0.0) min(offset, offsetExtensionLine) else max(offset, -offsetExtensionLine)
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

        dimensionLine = StrokeLine(
            ponto1 = definitionPoint1,
            ponto2 = definitionPoint2
        )
        extensionLine1 = StrokeLine(
            ponto1 = inicioLinhaDeCotaPonto1,
            ponto2 = definitionPoint1
        )
        extensionLine2 = StrokeLine(
            ponto1 = inicioLinhaDeCotaPonto2,
            ponto2 = definitionPoint2
        )
        this.texto = FillText(
            texto = textoCota(
                texto = texto,
                dc = formatoNumero,
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
        texto.desenhar(gc = gc, transformacoes = transformacoes)
    }
}

private val textoValorCota: String by lazy { "<>" }

private fun textoCota(texto: String, dc: DecimalFormat, distancia: Double): String {
    val valorDistancia = dc.format(distancia)
    return texto.replaceFirst(textoValorCota, valorDistancia)
}