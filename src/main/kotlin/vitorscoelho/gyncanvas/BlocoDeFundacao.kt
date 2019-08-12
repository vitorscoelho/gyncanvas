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

/**
 * @property hxPilar dimensão em x do pilar, em cm
 * @property hyPilar dimensão em y do pilar, em cm
 * @property embutimento comprimento de embutimento do pilar, em cm
 * @property espessuraNivelamento espessura do graute para nivelamento do pilar, em cm
 * @property folgaDeMontagem distância entre o pilar e a parede do colarinho, em cm
 * @property hcx espessura das paredes da esquerda e direita do colarinho, em cm
 * @property hcy espessura das paredes de "cima" e "baixo" do colarinho, em cm
 * @property lxBloco dimensão em x do bloco, em cm
 * @property lyBloco dimensão em y do bloco, em cm
 * @property nivelPisoAcabado nível do piso acabado, em cm. É um valor inteiro para que não tenha problemas com arredondamento de ponto flutuante na indicação dos níveis
 * @property diametroEstacas diâmetro das estacas presentes no bloco, em cm
 * @property posicoesEstacas posições das estacas do bloco, em cm. Para adotar estes posicionamentos, considerar que a origem do sistema cartesiano está no centro geométrico do bloco
 */
class BlocoDeFundacao(
    val hxPilar: Double,
    val hyPilar: Double,
    val embutimento: Double,
    val espessuraNivelamento: Double,
    val folgaDeMontagem: Double,
    val hcx: Double,
    val hcy: Double,
    val lxBloco: Double,
    val lyBloco: Double,
    val nivelPisoAcabado: Int,
    val diametroEstacas: Double,
    val posicoesEstacas: List<Vetor2D>
) {
    /**Dimensão interna em x do colarinho, em cm*/
    val hcxInt = hxPilar + 2.0 * folgaDeMontagem

    /**Dimensão interna em y do colarinho, em cm*/
    val hcyInt = hyPilar + 2.0 * folgaDeMontagem

    /**Dimensão externa em x do colarinho, em cm*/
    val hcxExt = hcxInt + 2.0 * hcx

    /**Dimensão externa em y do colarinho, em cm*/
    val hcyExt = hcyInt + 2.0 * hcy

    /**Posição do eixo do bloco em planta, em cm*/
    private val eixoBloco = Vetor2D(x = lxBloco / 2.0, y = lyBloco / 2.0)

    /**Abscissas de eixos de estacas e pilar, em cm*/
    private val abscissasEixosVerticais: List<Double> by lazy {
        (posicoesEstacas.map { posicao -> lxBloco / 2.0 + posicao.x } + eixoBloco.x - 0.0 - lxBloco).sorted().distinct()
    }

    /**Ordenadas de eixos de estacas e pilar, em cm*/
    private val ordenadasEixosHorizontais: List<Double> by lazy {
        (posicoesEstacas.map { posicao -> lyBloco / 2.0 + posicao.y } + eixoBloco.y - 0.0 - lyBloco).sorted().distinct()
    }

    /**Distância entre cotas, em cm*/
    private val distanciaCota = 18.0

    private val listaDesenho = mutableListOf<DesenhoAdicionavel>()
    private fun add(atributo: DrawAttributes, vararg primitiva: Primitiva) {
        primitiva.forEach { listaDesenho.add(DesenhoAdicionavel(it, atributo)) }
    }

    private val propriedadeContornoForma = StrokeAttributes(strokePaint = Color.RED)
    private val propriedadeContornoEstaca = StrokeAttributes(strokePaint = Color.MEDIUMPURPLE)
    private val propriedadeEixo = StrokeAttributes(strokePaint = Color.GREEN)
    private val propriedadePilar = FillAttributes(fillPaint = Color.DEEPSKYBLUE)
    private val propriedadeCota = object : DrawAttributes {
        override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
            gc.fill = Color.DODGERBLUE
            gc.stroke = Color.DODGERBLUE
            gc.textAlign = TextAlignment.CENTER
            gc.textBaseline = VPos.BOTTOM
            gc.font = Font(gc.font.name, 12.0)
        }
    }
    private val formatoCota = PropriedadesCotas(
        formatoNumero = DecimalFormat("#.##"),
        offsetExtensionLine = 5.0,
        offsetText = 0.0,
        arrowSize = 2.0
    )

    private fun contornoBloco() {
        val contorno = StrokeRect(pontoInsercao = Vetor2D.ZERO, deltaX = lxBloco, deltaY = lyBloco)
        add(propriedadeContornoForma, contorno)
    }

    private fun contornoColarinho() {
        val contornoExterno = StrokeRect(
            pontoInsercao = Vetor2D(x = lxBloco / 2.0 - hcxExt / 2.0, y = lyBloco / 2.0 - hcyExt / 2.0),
            deltaX = hcxExt, deltaY = hcyExt
        )
        val contornoInterno = StrokeRect(
            pontoInsercao = Vetor2D(x = lxBloco / 2.0 - hcxInt / 2.0, y = lyBloco / 2.0 - hcyInt / 2.0),
            deltaX = hcxInt, deltaY = hcyInt
        )
        add(propriedadeContornoForma, contornoExterno, contornoInterno)
    }

    private fun estacas() {
        posicoesEstacas.forEach { posicao ->
            val xRealCentro = lxBloco / 2.0 + posicao.x
            val yRealCentro = lyBloco / 2.0 + posicao.y
            val contornoEstaca = StrokeCircle(
                centro = Vetor2D(x = xRealCentro, y = yRealCentro),
                diametro = 60.0
            )
            add(propriedadeContornoEstaca, contornoEstaca)
        }
    }

    private fun linhasDeEixo() {
        abscissasEixosVerticais.forEach { x ->
            val eixoVertical = StrokeLine(
                ponto1 = Vetor2D(x = x, y = 0.0),
                ponto2 = Vetor2D(x = x, y = lyBloco)
            )
            add(propriedadeEixo, eixoVertical)
        }
        ordenadasEixosHorizontais.forEach { y ->
            val eixoHorizontal = StrokeLine(
                ponto1 = Vetor2D(x = 0.0, y = y),
                ponto2 = Vetor2D(x = lxBloco, y = y)
            )
            add(propriedadeEixo, eixoHorizontal)
        }
    }

    private fun cotasVerticaisDosEixos() {
        val xPonto = 0.0
        val sequencia = SequenciaCotaVertical(
            pontoInicial = Vetor2D.ZERO,
            xDimensionLine = -distanciaCota,
            propriedadesCotas = formatoCota
        )
        ordenadasEixosHorizontais.forEach { y -> sequencia.add(x = xPonto, y = y) }
        sequencia.add(x = xPonto, y = lyBloco)
        sequencia.toList().forEach { cota -> add(propriedadeCota, cota) }
    }

    private fun cotasHorizontaisDosEixos() {
        val yPonto = 0.0
        val sequencia = SequenciaCotaHorizontal(
            pontoInicial = Vetor2D.ZERO,
            yDimensionLine = -distanciaCota,
            propriedadesCotas = formatoCota
        )
        abscissasEixosVerticais.forEach { x -> sequencia.add(x = x, y = yPonto) }
        sequencia.add(x = lxBloco, y = yPonto)
        sequencia.toList().forEach { cota -> add(propriedadeCota, cota) }
    }

    private fun cotaVerticalComprimentoDoBloco() {
        val cota = CotaVertical(
            ponto1 = Vetor2D.ZERO,
            ponto2 = Vetor2D(x = 0.0, y = lyBloco),
            xDimensionLine = -2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
        add(propriedadeCota, cota)
    }

    private fun cotaHorizontalComprimentoDoBloco() {
        val cota = CotaHorizontal(
            ponto1 = Vetor2D.ZERO,
            ponto2 = Vetor2D(x = lxBloco, y = 0.0),
            yDimensionLine = -2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
        add(propriedadeCota, cota)
    }

    private fun cotasVerticaisDasDimensoesDoColarinho() {
        val yInferior = lyBloco / 2.0 - hcyExt / 2.0
        val xPonto = lxBloco
        val sequenciaInterna = SequenciaCotaVertical(
            pontoInicial = Vetor2D(x = xPonto, y = yInferior),
            xDimensionLine = lxBloco + distanciaCota,
            propriedadesCotas = formatoCota
        )
            .addDelta(deltaY = hcy)
            .addDelta(deltaY = hcyInt)
            .addDelta(deltaY = hcy)
        sequenciaInterna.toList().forEach { cota -> add(propriedadeCota, cota) }
        if (hcyExt == lyBloco) return
        val sequenciaExterna = SequenciaCotaVertical(
            pontoInicial = Vetor2D(x = xPonto, y = 0.0),
            xDimensionLine = lxBloco + 2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
            .add(x = xPonto, y = yInferior)
            .addDelta(deltaY = hcyExt)
            .add(x = xPonto, y = lyBloco)
        sequenciaExterna.toList().forEach { cota -> add(propriedadeCota, cota) }
    }

    private fun cotasHorizontaisDasDimensoesDoColarinho() {
        val xEsquerda = lxBloco / 2.0 - hcxExt / 2.0
        val yPonto = lyBloco
        val sequenciaInterna = SequenciaCotaHorizontal(
            pontoInicial = Vetor2D(x = xEsquerda, y = yPonto),
            yDimensionLine = lyBloco + distanciaCota,
            propriedadesCotas = formatoCota
        )
            .addDelta(deltaX = hcx)
            .addDelta(deltaX = hcxInt)
            .addDelta(deltaX = hcx)
        sequenciaInterna.toList().forEach { cota -> add(propriedadeCota, cota) }
        if (hcxExt == lxBloco) return
        val sequenciaExterna = SequenciaCotaHorizontal(
            pontoInicial = Vetor2D(x = 0.0, y = yPonto),
            yDimensionLine = lyBloco + 2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
            .add(x = xEsquerda, y = yPonto)
            .addDelta(deltaX = hcxExt)
            .add(x = lxBloco, y = yPonto)
        sequenciaExterna.toList().forEach { cota -> add(propriedadeCota, cota) }
    }

    private class DesenhoAdicionavel(val primitiva: Primitiva, val atributo: DrawAttributes)

    fun adicionarDesenho(gynCanvas: GynCanvas) {
        contornoBloco()
        contornoColarinho()
        estacas()
        linhasDeEixo()
        cotasVerticaisDosEixos()
        cotasHorizontaisDosEixos()
        cotaVerticalComprimentoDoBloco()
        cotaHorizontalComprimentoDoBloco()
        cotasVerticaisDasDimensoesDoColarinho()
        cotasHorizontaisDasDimensoesDoColarinho()
        listaDesenho.forEach { gynCanvas.addPrimitiva(it.primitiva, it.atributo) }
        /*val pontoOrigem = Vetor2D.ZERO
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
        })*/
    }
}