package vitorscoelho.gyncanvas.core.primitivas

import javafx.scene.canvas.GraphicsContext
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.math.Vetor2D
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

interface Cota : Primitiva

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

private val textoValorCota = "<>"

private fun textoCota(texto: String, dc: DecimalFormat, distancia: Double): String {
    val valorDistancia = dc.format(distancia)
    return texto.replaceFirst(textoValorCota, valorDistancia)
}