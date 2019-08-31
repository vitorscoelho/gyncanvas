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
import vitorscoelho.gyncanvas.core.primitivas.propriedades.FillTextAttributes
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

    private val propriedadeContornoPilar = StrokeAttributes(strokePaint = Color.DEEPSKYBLUE, lineWidth = 5.0)
    private val propriedadeHachura = FillAttributes(fillPaint = Color.MEDIUMPURPLE)
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
    private val propriedadeSeta = StrokeAttributes(strokePaint = Color.SADDLEBROWN)
    private val propriedadeLinhaCorte = StrokeAttributes(strokePaint = Color.MEDIUMPURPLE)
    private val propriedadeTextoCorte = FillTextAttributes(
        fillAtributtes = FillAttributes(fillPaint = Color.SADDLEBROWN),
        textAlign = TextAlignment.LEFT,
        textBaselinte = VPos.TOP
    )
    private val formatoCota = PropriedadesCotas(
        formatoNumero = DecimalFormat("#.##"),
        offsetExtensionLine = 5.0,
        offsetText = 0.0,
        arrowSize = 2.0
    )

    private fun pilar(): List<DesenhoAdicionavel> {
        val pontoInicial = Vetor2D(x = lxBloco / 2.0 - hxPilar / 2.0, y = lyBloco / 2.0 - hyPilar / 2.0)
        val hachura = Path.initBuilder(
            fechado = true,
            preenchido = true,
            pontoInicial = pontoInicial
        ).deltaLineTo(deltaX = hxPilar).deltaLineTo(deltaY = hyPilar).deltaLineTo(deltaX = -hxPilar).build()
        val contorno = StrokedRect(
            pontoInsercao = pontoInicial,
            deltaX = hxPilar, deltaY = hyPilar
        )
        return listOf(
            DesenhoAdicionavel(hachura, propriedadeHachura),
            DesenhoAdicionavel(contorno, propriedadeContornoPilar)
        )
    }

    private fun contornoBloco(): List<DesenhoAdicionavel> {
        val contorno = StrokedRect(pontoInsercao = Vetor2D.ZERO, deltaX = lxBloco, deltaY = lyBloco)
        return listOf(DesenhoAdicionavel(contorno, propriedadeContornoForma))
    }

    private fun contornoColarinho(): List<DesenhoAdicionavel> {
        val contornoExterno = StrokedRect(
            pontoInsercao = Vetor2D(x = lxBloco / 2.0 - hcxExt / 2.0, y = lyBloco / 2.0 - hcyExt / 2.0),
            deltaX = hcxExt, deltaY = hcyExt
        )
        val contornoInterno = StrokedRect(
            pontoInsercao = Vetor2D(x = lxBloco / 2.0 - hcxInt / 2.0, y = lyBloco / 2.0 - hcyInt / 2.0),
            deltaX = hcxInt, deltaY = hcyInt
        )
        return listOf(contornoInterno, contornoExterno).map { DesenhoAdicionavel(it, propriedadeContornoForma) }
    }

    private fun estacas(): List<DesenhoAdicionavel> {
        return posicoesEstacas.map { posicao ->
            val xRealCentro = lxBloco / 2.0 + posicao.x
            val yRealCentro = lyBloco / 2.0 + posicao.y
            val contornoEstaca = StrokedCircle(
                centro = Vetor2D(x = xRealCentro, y = yRealCentro),
                diametro = 60.0
            )
            DesenhoAdicionavel(contornoEstaca, propriedadeContornoEstaca)
        }
    }

    private fun linhasDeEixo(): List<DesenhoAdicionavel> {
        val eixosVerticais = abscissasEixosVerticais.map { x ->
            StrokedLine(
                ponto1 = Vetor2D(x = x, y = 0.0),
                ponto2 = Vetor2D(x = x, y = lyBloco)
            )
        }
        val eixosHorizontais = ordenadasEixosHorizontais.map { y ->
            StrokedLine(
                ponto1 = Vetor2D(x = 0.0, y = y),
                ponto2 = Vetor2D(x = lxBloco, y = y)
            )
        }
        return (eixosVerticais + eixosHorizontais).map { DesenhoAdicionavel(it, propriedadeEixo) }
    }

    private fun cotasVerticaisDosEixos(): List<DesenhoAdicionavel> {
        val xPonto = 0.0
        val sequencia = SequenciaCotaVertical(
            pontoInicial = Vetor2D.ZERO,
            xDimensionLine = -distanciaCota,
            propriedadesCotas = formatoCota
        )
        ordenadasEixosHorizontais.forEach { y -> sequencia.add(x = xPonto, y = y) }
        sequencia.add(x = xPonto, y = lyBloco)
        return sequencia.toList().map { DesenhoAdicionavel(it, propriedadeCota) }
    }

    private fun cotasHorizontaisDosEixos(): List<DesenhoAdicionavel> {
        val yPonto = 0.0
        val sequencia = SequenciaCotaHorizontal(
            pontoInicial = Vetor2D.ZERO,
            yDimensionLine = -distanciaCota,
            propriedadesCotas = formatoCota
        )
        abscissasEixosVerticais.forEach { x -> sequencia.add(x = x, y = yPonto) }
        sequencia.add(x = lxBloco, y = yPonto)
        return sequencia.toList().map { DesenhoAdicionavel(it, propriedadeCota) }
    }

    private fun cotaVerticalComprimentoDoBloco(): List<DesenhoAdicionavel> {
        val cota = CotaVertical(
            ponto1 = Vetor2D.ZERO,
            ponto2 = Vetor2D(x = 0.0, y = lyBloco),
            xDimensionLine = -2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
        return listOf(DesenhoAdicionavel(cota, propriedadeCota))
    }

    private fun cotaHorizontalComprimentoDoBloco(): List<DesenhoAdicionavel> {
        val cota = CotaHorizontal(
            ponto1 = Vetor2D.ZERO,
            ponto2 = Vetor2D(x = lxBloco, y = 0.0),
            yDimensionLine = -2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
        return listOf(DesenhoAdicionavel(cota, propriedadeCota))
    }

    private fun cotasVerticaisDasDimensoesDoColarinho(): List<DesenhoAdicionavel> {
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
        val cotasMedidasInternas = sequenciaInterna.toList().toList()
        if (hcyExt == lyBloco) return emptyList()
        val sequenciaExterna = SequenciaCotaVertical(
            pontoInicial = Vetor2D(x = xPonto, y = 0.0),
            xDimensionLine = lxBloco + 2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
            .add(x = xPonto, y = yInferior)
            .addDelta(deltaY = hcyExt)
            .add(x = xPonto, y = lyBloco)
        val cotasMedidasExternas = sequenciaExterna.toList().toList()
        return (cotasMedidasInternas + cotasMedidasExternas).map { DesenhoAdicionavel(it, propriedadeCota) }
    }

    private fun cotasHorizontaisDasDimensoesDoColarinho(): List<DesenhoAdicionavel> {
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
        val cotasMedidasInternas = sequenciaInterna.toList().toList()
        if (hcxExt == lxBloco) return emptyList()
        val sequenciaExterna = SequenciaCotaHorizontal(
            pontoInicial = Vetor2D(x = 0.0, y = yPonto),
            yDimensionLine = lyBloco + 2.0 * distanciaCota,
            propriedadesCotas = formatoCota
        )
            .add(x = xEsquerda, y = yPonto)
            .addDelta(deltaX = hcxExt)
            .add(x = lxBloco, y = yPonto)
        val cotasMedidasExternas = sequenciaExterna.toList().toList()
        return (cotasMedidasInternas + cotasMedidasExternas).map { DesenhoAdicionavel(it, propriedadeCota) }
    }

    private fun indicacaoCorte(): List<DesenhoAdicionavel> {
        val setaBase =
            Path.initBuilder(fechado = false, pontoInicial = Vetor2D.ZERO).deltaLineTo(deltaX = -distanciaCota)
                .deltaLineTo(deltaY = -15.0).deltaLineTo(deltaX = 6.0)
                .deltaLineTo(deltaX = -6.0, deltaY = -17.5).deltaLineTo(deltaX = -6.0, deltaY = 17.5)
                .deltaLineTo(deltaX = 6.0).build()
        val deltaXSetaEsquerda = -3.0 * distanciaCota
        val deltaYSetaCorteA = lyBloco / 2.0 - 5.0
        val setaEsquerda = setaBase.copiarComTransformacao(
            transformacoes = Transformacoes().transladar(
                translacaoX = deltaXSetaEsquerda,
                translacaoY = deltaYSetaCorteA
            )
        )
        val deltaXSetaDireita = lxBloco + (if (hcyExt == lyBloco) 2.0 * distanciaCota else 3.0 * distanciaCota)
        val setaDireita = setaBase.copiarComTransformacao(
            transformacoes = Transformacoes()
                .espelhar(eixoX = 0.0, eixoY = 1.0, pontoX = 0.0, pontoY = 0.0)
                .transladar(translacaoX = -deltaXSetaDireita, translacaoY = deltaYSetaCorteA)
        )
        val linhaHorizontal = StrokedLine(
            ponto1 = Vetor2D(x = deltaXSetaEsquerda, y = deltaYSetaCorteA),
            ponto2 = Vetor2D(x = deltaXSetaDireita, y = deltaYSetaCorteA)
        )
        val textoCorteA = FilledText(texto = "A", posicao = Vetor2D.ZERO, angulo = 0.0)
        return listOf(setaEsquerda, setaDireita).map { DesenhoAdicionavel(it, propriedadeSeta) } +
                listOf(linhaHorizontal).map { DesenhoAdicionavel(it, propriedadeLinhaCorte) } +
                listOf(textoCorteA).map { DesenhoAdicionavel(it, propriedadeTextoCorte) }
    }

    private class DesenhoAdicionavel(val primitiva: Primitiva, val atributo: DrawAttributes)

    private fun listarDesenhos(): List<DesenhoAdicionavel> =
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
                cotasHorizontaisDasDimensoesDoColarinho() +
                indicacaoCorte()

    fun forEachPrimitiva(acao: (primitiva: Primitiva, atributo: DrawAttributes) -> Unit) {
        listarDesenhos().forEach { acao.invoke(it.primitiva, it.atributo) }
    }

    fun <R> mapPrimitivas(transform: (primitiva: Primitiva, atributo: DrawAttributes) -> R): List<R> {
        return listarDesenhos().map { transform(it.primitiva, it.atributo) }
    }

    fun adicionarDesenho(gynCanvas: GynCanvas) {
        listarDesenhos().forEach { gynCanvas.addPrimitiva(it.primitiva, it.atributo) }
    }
}