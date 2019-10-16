package vitorscoelho.gyncanvas

import javafx.scene.text.Font
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.core.dxf.entities.*
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.math.Vector2D

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
    val posicoesEstacas: List<Vector2D>
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
    private val eixoBloco = Vector2D(x = lxBloco / 2.0, y = lyBloco / 2.0)

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

    private val layerPilarContorno = Layer(name = "Pilar contorno", color = Color.INDEX_121)
    private val layerLinhaClara = Layer(name = "Linha clara", color = Color.INDEX_191)
    private val layerFormaContorno = Layer(name = "Fôrma contorno", color = Color.INDEX_1)
    private val layerProjecao = Layer(name = "Projeção", color = Color.INDEX_190)
    private val layerEixo = Layer(name = "Eixo", color = Color.INDEX_3)
    private val layerCota = Layer(name = "Cota", color = Color.INDEX_142)
    private val layerSetaCorte = Layer(name = "Seta corte", color = Color.INDEX_43)
    private val layerLinhaCorte = Layer(name = "Linha corte", color = Color.INDEX_190)
    private val layerTexto = Layer(name = "Texto", color = Color.INDEX_43)

    private val blocoCota: Block = run {
        val circulo1 = Circle(layer = layerCota, centerPoint = Vector2D.ZERO, diameter = 0.5)
        val circulo2 = Circle(layer = layerCota, centerPoint = Vector2D.ZERO, diameter = 1.0)
        val linha =
            Line(layer = layerCota, startPoint = Vector2D(x = -1.0, y = 0.0), endPoint = Vector2D(x = 0.0, y = 0.0))
        return@run Block(name = "_Origin2", entities = listOf(circulo1, circulo2, linha))
    }
    private val dimStyle = DimStyle(
        name = "1_100 TQS",
        textStyle = TextStyle(
            name = "Standard",
            fontName = Font.getDefault().name,
            fontFileName = Font.getDefault().name
        ),
        overallScale = 100.0,
        extensionLinesExtendBeyondDimLines = 0.0,
        firstArrowHead = blocoCota, secondArrowHead = blocoCota
    )

//    private val propriedadeTextoCorte = FillTextAttributes(
//        fillAtributtes = FillAttributes(fillPaint = Color.SADDLEBROWN),
//        textAlign = TextAlignment.LEFT,
//        textBaselinte = VPos.TOP
//    )

//    private val propriedadeCota = object : DrawAttributes {
//        override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
//            gc.fill = Color.DODGERBLUE
//            gc.stroke = Color.DODGERBLUE
//            gc.textAlign = TextAlignment.CENTER
//            gc.textBaseline = VPos.BOTTOM
//            gc.font = Font(gc.font.name, 12.0)
//        }
//    }

//    private val formatoCota = PropriedadesCotas(
//        formatoNumero = DecimalFormat("#.##"),
//        offsetExtensionLine = 5.0,
//        offsetText = 0.0,
//        arrowSize = 2.0
//    )

    private fun pilar(): List<Entity> {
        val pontoInicial = Vector2D(x = lxBloco / 2.0 - hxPilar / 2.0, y = lyBloco / 2.0 - hyPilar / 2.0)
        val contorno = LwPolyline.rectangle(
            layer = layerPilarContorno, startPoint = pontoInicial,
            deltaX = hxPilar, deltaY = hyPilar
        )
        val hachura = Hatch.fromLwPolyline(
            layer = layerLinhaClara,
            lwPolyline = contorno
        )
        return listOf(hachura, contorno)
    }

    private fun contornoBloco(): List<Entity> {
        val contorno = LwPolyline.rectangle(
            layer = layerFormaContorno,
            startPoint = Vector2D.ZERO,
            deltaX = lxBloco,
            deltaY = lyBloco
        )
        return listOf(contorno)
    }

    private fun contornoColarinho(): List<Entity> {
        val contornoExterno = LwPolyline.rectangle(
            layer = layerFormaContorno,
            startPoint = Vector2D(x = lxBloco / 2.0 - hcxExt / 2.0, y = lyBloco / 2.0 - hcyExt / 2.0),
            deltaX = hcxExt, deltaY = hcyExt
        )
        val contornoInterno = LwPolyline.rectangle(
            layer = layerFormaContorno,
            startPoint = Vector2D(x = lxBloco / 2.0 - hcxInt / 2.0, y = lyBloco / 2.0 - hcyInt / 2.0),
            deltaX = hcxInt, deltaY = hcyInt
        )
        return listOf(contornoInterno, contornoExterno)
    }

    private fun estacas(): List<Entity> {
        return posicoesEstacas.map { posicao ->
            val xRealCentro = lxBloco / 2.0 + posicao.x
            val yRealCentro = lyBloco / 2.0 + posicao.y
            val contornoEstaca = Circle(
                layer = layerProjecao,
                centerPoint = Vector2D(x = xRealCentro, y = yRealCentro),
                diameter = 60.0
            )
            return@map contornoEstaca
        }
    }

    private fun linhasDeEixo(): List<Entity> {
        val eixosVerticais = abscissasEixosVerticais.map { x ->
            Line(
                layer = layerEixo,
                startPoint = Vector2D(x = x, y = 0.0),
                endPoint = Vector2D(x = x, y = lyBloco)
            )
        }
        val eixosHorizontais = ordenadasEixosHorizontais.map { y ->
            Line(
                layer = layerEixo,
                startPoint = Vector2D(x = 0.0, y = y),
                endPoint = Vector2D(x = lxBloco, y = y)
            )
        }
        return eixosVerticais + eixosHorizontais
    }

    private fun cotasVerticaisDosEixos(): List<Entity> {
        val xPonto = 0.0
        val sequencia = RotatedDimension.verticalSequence(
            layer = layerCota, dimStyle = dimStyle, xDimensionLine = -distanciaCota
        ).firstPoint(Vector2D.ZERO)
        ordenadasEixosHorizontais.forEach { y -> sequencia.next(x = xPonto, y = y) }
        sequencia.next(x = xPonto, y = lyBloco)
        return sequencia.toList()
    }

    private fun cotasHorizontaisDosEixos(): List<Entity> {
        val yPonto = 0.0
        val sequencia = RotatedDimension.horizontalSequence(
            layer = layerCota, dimStyle = dimStyle, yDimensionLine = -distanciaCota
        ).firstPoint(Vector2D.ZERO)
        abscissasEixosVerticais.forEach { x -> sequencia.next(x = x, y = yPonto) }
        sequencia.next(x = lxBloco, y = yPonto)
        return sequencia.toList()
    }

    private fun cotaVerticalComprimentoDoBloco(): List<Entity> {
        val cota = RotatedDimension.vertical(
            layer = layerCota, dimStyle = dimStyle,
            xPoint1 = Vector2D.ZERO, xPoint2 = Vector2D(x = 0.0, y = lyBloco),
            xDimensionLine = -2.0 * distanciaCota
        )
        return listOf(cota)
    }

    private fun cotaHorizontalComprimentoDoBloco(): List<Entity> {
        val cota = RotatedDimension.horizontal(
            layer = layerCota, dimStyle = dimStyle,
            xPoint1 = Vector2D.ZERO, xPoint2 = Vector2D(x = lxBloco, y = 0.0),
            yDimensionLine = -2.0 * distanciaCota
        )
        return listOf(cota)
    }

    private fun cotasVerticaisDasDimensoesDoColarinho(): List<Entity> {
        val yInferior = lyBloco / 2.0 - hcyExt / 2.0
        val xPonto = lxBloco
        val sequenciaInterna = RotatedDimension.verticalSequence(
            layer = layerCota, dimStyle = dimStyle,
            xDimensionLine = lxBloco + distanciaCota
        ).firstPoint(x = xPonto, y = yInferior)
            .nextDelta(deltaY = hcy)
            .nextDelta(deltaY = hcyInt)
            .nextDelta(deltaY = hcy)
        if (hcyExt == lyBloco) return emptyList()
        val sequenciaExterna = RotatedDimension.verticalSequence(
            layer = layerCota, dimStyle = dimStyle,
            xDimensionLine = lxBloco + 2.0 * distanciaCota
        ).firstPoint(x = xPonto, y = 0.0)
            .next(x = xPonto, y = yInferior)
            .nextDelta(deltaY = hcyExt)
            .next(x = xPonto, y = lyBloco)
        return (sequenciaInterna.toList() + sequenciaExterna.toList())
    }

    private fun cotasHorizontaisDasDimensoesDoColarinho(): List<Entity> {
        val xEsquerda = lxBloco / 2.0 - hcxExt / 2.0
        val yPonto = lyBloco
        val sequenciaInterna = RotatedDimension.horizontalSequence(
            layer = layerCota, dimStyle = dimStyle,
            yDimensionLine = lyBloco + distanciaCota
        ).firstPoint(x = xEsquerda, y = yPonto)
            .nextDelta(deltaX = hcx)
            .nextDelta(deltaX = hcxInt)
            .nextDelta(deltaX = hcx)
        if (hcxExt == lxBloco) return emptyList()
        val sequenciaExterna = RotatedDimension.horizontalSequence(
            layer = layerCota, dimStyle = dimStyle,
            yDimensionLine = lyBloco + 2.0 * distanciaCota
        ).firstPoint(x = 0.0, y = yPonto)
            .next(x = xEsquerda, y = yPonto)
            .nextDelta(deltaX = hcxExt)
            .next(x = lxBloco, y = yPonto)
        return (sequenciaInterna.toList() + sequenciaExterna.toList())
    }

    private fun testeCotaInclinada(): List<Entity> {
        val ponto1 = Vector2D.ZERO
        val ponto2 = Vector2D(lxBloco, lyBloco)
        val cota = RotatedDimension(
            layer = layerCota, dimStyle = dimStyle,
            xPoint1 = ponto1, xPoint2 = ponto2,
            dimensionLineReferencePoint = Vector2D(x = 0.0, y = lyBloco + 50.0), angle = Math.toRadians(30.0)
        )
        return listOf(cota)
    }

//    private fun indicacaoCorte(): List<DesenhoAdicionavel> {
//        val setaBase =
//            Path.initBuilder(fechado = false, pontoInicial = Vetor2D.ZERO).deltaLineTo(deltaX = -distanciaCota)
//                .deltaLineTo(deltaY = -15.0).deltaLineTo(deltaX = 6.0)
//                .deltaLineTo(deltaX = -6.0, deltaY = -17.5).deltaLineTo(deltaX = -6.0, deltaY = 17.5)
//                .deltaLineTo(deltaX = 6.0).build()
//        val deltaXSetaEsquerda = -3.0 * distanciaCota
//        val deltaYSetaCorteA = lyBloco / 2.0 - 5.0
//        val setaEsquerda = setaBase.copiarComTransformacao(
//            transformacoes = Transformacoes().transladar(
//                translacaoX = deltaXSetaEsquerda,
//                translacaoY = deltaYSetaCorteA
//            )
//        )
//        val deltaXSetaDireita = lxBloco + (if (hcyExt == lyBloco) 2.0 * distanciaCota else 3.0 * distanciaCota)
//        val setaDireita = setaBase.copiarComTransformacao(
//            transformacoes = Transformacoes()
//                .espelhar(eixoX = 0.0, eixoY = 1.0, pontoX = 0.0, pontoY = 0.0)
//                .transladar(translacaoX = -deltaXSetaDireita, translacaoY = deltaYSetaCorteA)
//        )
//        val linhaHorizontal = StrokedLine(
//            ponto1 = Vetor2D(x = deltaXSetaEsquerda, y = deltaYSetaCorteA),
//            ponto2 = Vetor2D(x = deltaXSetaDireita, y = deltaYSetaCorteA)
//        )
//        val textoCorteA = FilledText(texto = "A", posicao = Vetor2D.ZERO, angulo = 0.0)
//        return listOf(setaEsquerda, setaDireita).map { DesenhoAdicionavel(it, propriedadeSeta) } +
//                listOf(linhaHorizontal).map { DesenhoAdicionavel(it, propriedadeLinhaCorte) } +
//                listOf(textoCorteA).map { DesenhoAdicionavel(it, propriedadeTextoCorte) }
//    }

    fun listarDesenhos(): List<Entity> =
        pilar() +
                contornoBloco() +
                contornoColarinho() +
                estacas() +
                linhasDeEixo() +
                cotasVerticaisDosEixos() +
                cotasHorizontaisDosEixos() +
                cotaVerticalComprimentoDoBloco() +
                cotaHorizontalComprimentoDoBloco() +
                cotasVerticaisDasDimensoesDoColarinho() +
                cotasHorizontaisDasDimensoesDoColarinho() + testeCotaInclinada()//+
//                indicacaoCorte()
}