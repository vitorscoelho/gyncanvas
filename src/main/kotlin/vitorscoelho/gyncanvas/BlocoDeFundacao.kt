package vitorscoelho.gyncanvas

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import vitorscoelho.gyncanvas.core.GynCanvas
import vitorscoelho.gyncanvas.core.Transformacoes
import vitorscoelho.gyncanvas.core.primitivas.*
import vitorscoelho.gyncanvas.core.primitivas.propriedades.DrawAttributes
import vitorscoelho.gyncanvas.core.primitivas.propriedades.FillAttributes
import vitorscoelho.gyncanvas.core.primitivas.propriedades.StrokeAttributes
import vitorscoelho.gyncanvas.math.Vetor2D
import java.text.DecimalFormat

class Colarinho(
    val hxPilar: Double,
    val hyPilar: Double,
    val cobrimentoInterno: Double,
    val cobrimentoExterno: Double,
    val folgaDeMontagem: Double,
    val hcx: Double,
    val hcy: Double
) {
    fun adicionarDesenho(gynCanvas: GynCanvas) {
        val propriedadeContornoForma = StrokeAttributes(strokePaint = Color.RED)
        val propriedadePilar = FillAttributes(fillPaint = Color.DEEPSKYBLUE)
        val pontoOrigem = Vetor2D.ZERO
        val contornoExternoColarinho = StrokeRect(
            pontoInsercao = pontoOrigem,
            deltaX = hxPilar + 2.0 * (hcx + folgaDeMontagem),
            deltaY = hyPilar + 2.0 * (hcy + folgaDeMontagem)
        )
        val contornoInternoColarinho = StrokeRect(
            pontoInsercao = Vetor2D(
                x = hcx,
                y = hcy
            ),
            deltaX = hxPilar + 2.0 * folgaDeMontagem,
            deltaY = hyPilar + 2.0 * folgaDeMontagem
        )
        val pilar = FillRect(
            pontoInsercao = Vetor2D(
                x = hcx + folgaDeMontagem,
                y = hcy + folgaDeMontagem
            ),
            deltaX = hxPilar,
            deltaY = hyPilar
        )
        val pilarPath = Path.initBuilder(fechado = true, pontoInicial = Vetor2D(x = 20.0, y = -40.0))
            .lineTo(x = 20.0, y = -13.0)
            .lineTo(x = 10.0, y = -11.0)
            .lineTo(x = 10.0, y = 11.0)
            .lineTo(x = 20.0, y = 13.0)
            .lineTo(x = 20.0, y = 40.0)
            .lineTo(x = -20.0, y = 40.0)
            .lineTo(x = -20.0, y = 13.0)
            .lineTo(x = -10.0, y = 11.0)
            .lineTo(x = -10.0, y = -11.0)
            .lineTo(x = -20.0, y = -13.0)
            .lineTo(x = -20.0, y = -40.0)
//            .lineTo(x =, y =)
//            .lineTo(x =, y =)
//            .lineTo(x =, y =)
//            .lineTo(x =, y =)
            .build()
        run {
            val dcCotas = DecimalFormat("#.##")
            val propriedadeCotas = object : DrawAttributes {
                override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
                    gc.fill = Color.GREEN
                    gc.stroke = Color.BLUEVIOLET
                    gc.textAlign = TextAlignment.CENTER
                    gc.textBaseline = VPos.BOTTOM
                    gc.font = Font(gc.font.name, 12.0)
                }
            }
            val y1 = hyPilar + 2.0 * (hcy + folgaDeMontagem)
            val y1Dimension = y1 + 15.0
            val offsetExtensionLine = 5.0
            val offsetText = 0.0
            val arrowSize = 2.0
            val multiplicadorDistancia = 1.0 / 100.0
            val cotas = mutableListOf<Cota>()
            val propriedadesCotas = PropriedadesCotas(
                formatoNumero = dcCotas,
                offsetExtensionLine = offsetExtensionLine,
                offsetText = offsetText,
                arrowSize = arrowSize,
                multiplicadorValor = multiplicadorDistancia
            )
            cotas.add(
                CotaHorizontal(
                    ponto1 = Vetor2D(x = 0.0, y = y1),
                    ponto2 = Vetor2D(x = hcx, y = y1),
                    yDimensionLine = y1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.add(
                CotaHorizontal(
                    ponto1 = Vetor2D(x = hcx, y = y1),
                    ponto2 = Vetor2D(x = hcx + hxPilar + 2.0 * folgaDeMontagem, y = y1),
                    yDimensionLine = y1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.add(
                CotaHorizontal(
                    ponto1 = Vetor2D(x = hcx + hxPilar + 2.0 * folgaDeMontagem, y = y1),
                    ponto2 = Vetor2D(x = hxPilar + 2.0 * (hcx + folgaDeMontagem), y = y1),
                    yDimensionLine = y1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            val x1Dimension = 0.0 - 15.0
            cotas.add(
                CotaVertical(
                    ponto1 = Vetor2D(x = 0.0, y = 0.0),
                    ponto2 = Vetor2D(x = 0.0, y = hcy),
                    xDimensionLine = x1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.add(
                CotaVertical(
                    ponto1 = Vetor2D(x = 0.0, y = hcy),
                    ponto2 = Vetor2D(x = 0.0, y = hcy + hyPilar + 2.0 * folgaDeMontagem),
                    xDimensionLine = x1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.add(
                CotaVertical(
                    ponto1 = Vetor2D(x = 0.0, y = hcy + hyPilar + 2.0 * folgaDeMontagem),
                    ponto2 = Vetor2D(x = 0.0, y = hyPilar + 2.0 * (hcy + folgaDeMontagem)),
                    xDimensionLine = x1Dimension,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.add(
                CotaAlinhada(
                    ponto1 = Vetor2D.ZERO,
                    ponto2 = Vetor2D(x = hxPilar + 2.0 * (hcx + folgaDeMontagem), y = y1),
                    offset = 50.0,
                    propriedadesCotas = propriedadesCotas
                )
            )
            cotas.forEach { gynCanvas.addPrimitiva(it, propriedadeCotas) }
        }
        gynCanvas.addPrimitiva(contornoExternoColarinho, propriedadeContornoForma)
        gynCanvas.addPrimitiva(contornoInternoColarinho, propriedadeContornoForma)
        gynCanvas.addPrimitiva(pilar, propriedadePilar)
        gynCanvas.addPrimitiva(pilarPath, object : DrawAttributes {
            override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
                gc.stroke = Color.DARKORANGE
            }
        })
    }
}