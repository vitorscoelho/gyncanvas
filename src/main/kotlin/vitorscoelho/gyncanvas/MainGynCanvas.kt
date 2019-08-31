package vitorscoelho.gyncanvas

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.scene.Cursor
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import tornadofx.*
import vitorscoelho.gyncanvas.core.GynCanvas
import vitorscoelho.gyncanvas.core.primitivas.*
import vitorscoelho.gyncanvas.core.primitivas.propriedades.FillAttributes
import vitorscoelho.gyncanvas.core.primitivas.propriedades.StrokeAttributes
import vitorscoelho.gyncanvas.core.primitivas.propriedades.FillTextAttributes
import vitorscoelho.gyncanvas.json.JsonDrawing
import vitorscoelho.gyncanvas.json.toJsonEntity
import vitorscoelho.gyncanvas.math.Vetor2D

fun main() {
    launch<MeuAppGynCanvas>()
}

class MeuAppGynCanvas : App(MeuViewGynCanvas::class)

class MeuViewGynCanvas : View() {
    val propriedadeStroke = StrokeAttributes(
        strokePaint = Color.WHITE,
        lineWidth = 1.0
    )
    val propriedadeText = FillTextAttributes(
        textAlign = TextAlignment.LEFT,
        fillAtributtes = FillAttributes(fillPaint = Color.YELLOW)
    )
    val linha = StrokedLine(
        ponto1 = Vetor2D(0.0, 0.0),
        ponto2 = Vetor2D(200.0, 300.0)
    )
    val circulo = StrokedCircle(centro = Vetor2D(200.0, 300.0), diametro = 200.0)
    val retangulo = StrokedRect(
        pontoInsercao = Vetor2D.ZERO,
        deltaX = 200.0,
        deltaY = -100.0
    )
    val polilinha = StrokedPolyline(
        pontos = listOf(Vetor2D(200.0, 200.0), Vetor2D(600.0, 300.0), Vetor2D.ZERO),
        fechado = true
    )
    val texto = FilledText(texto = "Texto teste", posicao = Vetor2D(200.0, 300.0), angulo = 0.0, tamanhoFixo = true)
    val textoRotacionado = FilledText(
        texto = "Texto rotacionado",
        posicao = Vetor2D(200.0, 300.0),
        angulo = 3.14 / 2.0
    )
    val path = Path.initBuilder(fechado = true, pontoInicial = Vetor2D.ZERO)
        .deltaLineTo(deltaX = 20.0, deltaY = 15.0)
        .deltaLineTo(deltaX = -10.0, deltaY = 30.0)
        .lineTo(x = -20.0, y = 45.0)
        .build()
    val path2 = Path.initBuilder(fechado = false, pontoInicial = Vetor2D.ZERO)
        .lineTo(x = 20.0, y = 0.0)
        .arcTo(xTangente1 = 25.0, yTangente1 = 0.0, xTangente2 = 25.0, yTangente2 = 5.0, raio = 5.0)
        .deltaLineTo(deltaY = 30.0)
        .build()
    val gynCanvas = GynCanvas().apply {
        /*with(gc){
            fillArc()
            fillOval()
            fillPolygon()
            fillRect()
            fillRoundRect()
            fillText()
            fillText(larguraMaxima)
            strokeArc()
            strokeLine()
            strokeOval()
            strokePolygon()
            strokePolyline()
            strokeRect()
            strokeRoundRect()
            strokeText()
            strokeText(larguraMaxima)
        }*/
        //        addPrimitiva(circulo, propriedadeStroke)
//        addPrimitiva(texto, propriedadeText)
//        addPrimitiva(textoRotacionado, propriedadeText)
//        addPrimitiva(linha, propriedadeStroke)
//        addPrimitiva(path, propriedadeStroke)
//        addPrimitiva(path2, propriedadeStroke)
//        addPrimitiva(retangulo, propriedadeStroke)
//        addPrimitiva(polilinha, propriedadeStroke)
//        criarAduela(this)
//        json()
        criarBloco(this)

//        val passo = 50.0
//        (1..100_000).forEach { iteracao ->
//            val x = iteracao * passo
//            addPrimitiva(
//                primitiva = StrokeLine(
//                    ponto1 = Vetor2D(x, 0.0),
//                    ponto2 = Vetor2D(x, -800.0)
//                ),
//                propriedade = propriedadeStroke
//            )
//        }
//        println("Fim")
    }
    val transformacoes = gynCanvas.transformacoes

    override val root = hbox {
        setPrefSize(1300.0, 600.0)
        this += gynCanvas.node
        gynCanvas.desenhar()

        var xPanInicial: Double = 0.0
        var yPanInicial: Double = 0.0
        setOnMousePressed { event ->
            if (event.button == MouseButton.MIDDLE) {
                xPanInicial = event.x
                yPanInicial = event.y
            }
        }

        setOnMouseReleased { event ->
            if (event.button == MouseButton.MIDDLE) cursor = Cursor.DEFAULT
        }

        setOnMouseDragged { event ->
            if (event.isMiddleButtonDown) {
                cursor = Cursor.MOVE
                val deltaX: Double = (event.x - xPanInicial) / transformacoes.escala
                val deltaY: Double = (event.y - yPanInicial) / transformacoes.escala

                transformacoes.transladar(deltaX, deltaY)

                xPanInicial = event.x
                yPanInicial = event.y
            }
            gynCanvas.desenhar()
        }

        val fatorDeEscala = 1.2
        setOnScroll { event ->
            val coordenadasMundoClique = transformacoes.coordenadasMundo(xTela = event.x, yTela = event.y)
            if (event.deltaY > 0) {
                transformacoes.escalar(
                    escala = fatorDeEscala,
                    xPivo = coordenadasMundoClique.x,
                    yPivo = coordenadasMundoClique.y
                )
                println("Rolou positivo")
            } else {
                transformacoes.escalar(
                    escala = 1.0 / fatorDeEscala,
                    xPivo = coordenadasMundoClique.x,
                    yPivo = coordenadasMundoClique.y
                )
                println("Rolou negativo")
            }
            gynCanvas.desenhar()
        }
    }
}

fun criarBloco(gynCanvas: GynCanvas) {
    val blocoDeFundacao = BlocoDeFundacao(
        hxPilar = 80.0,
        hyPilar = 40.0,
        folgaDeMontagem = 7.5,
        hcx = 35.0,
        hcy = 25.0,
        embutimento = 130.0,
        espessuraNivelamento = 5.0,
        lxBloco = 320.0,
        lyBloco = 205.0,
        nivelPisoAcabado = 50034,
        diametroEstacas = 60.0,
        posicoesEstacas = listOf(
            Vetor2D(x = -115.0, y = 0.0),
            Vetor2D(x = 115.0, y = 0.0)
        )
    )
    blocoDeFundacao.adicionarDesenho(gynCanvas = gynCanvas)
//    val listaJson = blocoDeFundacao.mapPrimitivas { primitiva, atributo -> primitiva.toJsonEntity() }
//    val jsonDrawing = JsonDrawing(
//        tablesSection = emptyList(),
//        entitiesSection = listaJson
//    )
//    val jacksonMapper = jacksonObjectMapper()
//    println(jacksonMapper.writeValueAsString(jsonDrawing))
}

fun criarAduela(gynCanvas: GynCanvas) {
    val formaAduela = FormaAduela(
        larguraLivre = 200.0,
        alturaLivre = 300.0,
        misulaHorizontalCobertura = 20.0,
        misulaVerticalCobertura = 15.0,
        misulaHorizontalFundo = 25.0,
        misulaVerticalFundo = 10.0,
        espessuraLajeCobertura = 25.0,
        espessuraParedeLateral = 15.0,
        espessuraLajeFundo = 20.0,
        distanciaFuroIcamento = 50.0,
        diametroFuroIcamento = 5.0,
        comprimentoAduela = 100.0,
        comprimentoEncaixe = 9.0,
        espessuraMacho = 6.0,
        espessuraChanfradoEncaixe = 2.0
    )
    formaAduela.adicionarDesenho(gynCanvas = gynCanvas)
}