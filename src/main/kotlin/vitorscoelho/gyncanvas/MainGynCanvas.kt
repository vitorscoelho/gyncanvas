package vitorscoelho.gyncanvas

import com.beust.klaxon.Klaxon
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
import vitorscoelho.gyncanvas.dxf.entities.*
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
    val linha = StrokeLine(
        ponto1 = Vetor2D(0.0, 0.0),
        ponto2 = Vetor2D(200.0, 300.0)
    )
    val circulo = StrokeCircle(centro = Vetor2D(200.0, 300.0), diametro = 200.0)
    val retangulo = StrokeRect(
        pontoInsercao = Vetor2D.ZERO,
        deltaX = 200.0,
        deltaY = -100.0
    )
    val polilinha = StrokePolyline(
        pontos = listOf(Vetor2D(200.0, 200.0), Vetor2D(600.0, 300.0), Vetor2D.ZERO),
        fechado = true
    )
    val texto = FillText(texto = "Texto teste", posicao = Vetor2D(200.0, 300.0), angulo = 0.0, tamanhoFixo = true)
    val textoRotacionado = FillText(
        texto = "Texto rotacionado",
        posicao = Vetor2D(200.0, 300.0),
        angulo = 3.14 / 2.0
    )
    val gynCanvas = GynCanvas().apply {
        //        addPrimitiva(circulo, propriedadeStroke)
//        addPrimitiva(texto, propriedadeText)
//        addPrimitiva(textoRotacionado, propriedadeText)
        addPrimitiva(linha, propriedadeStroke)
//        addPrimitiva(retangulo, propriedadeStroke)
//        addPrimitiva(polilinha, propriedadeStroke)
//        criarAduela(this)
        json()
//        criarBloco(this)

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
    val colarinho = Colarinho(
        hxPilar = 80.0,
        hyPilar = 40.0,
        cobrimentoInterno = 1.0,
        cobrimentoExterno = 5.0,
        folgaDeMontagem = 7.5,
        hcx = 45.0,
        hcy = 25.0
    )
    colarinho.adicionarDesenho(gynCanvas = gynCanvas)
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

fun json() {
    val line = Line(
        StartPoint = Point3d(x = 10.0, y = 15.0),
        EndPoint = Point3d(x = 25.0, y = 45.0),
        Layer = "LayerTeste"
    )
    val polyline = Polyline(
        vertexes = mutableListOf(
            PolylineVertex(
                point = Point2d(x = -10.0, y = 12.0),
                bulge = 0.0, startWidth = 1.0, endWidth = 1.0
            ),
            PolylineVertex(
                point = Point2d(x = 10.0, y = 22.0),
                bulge = 0.0, startWidth = 1.0, endWidth = 1.0
            ),
            PolylineVertex(
                point = Point2d(x = 40.0, y = -15.0),
                bulge = 0.0, startWidth = 1.0, endWidth = 1.0
            )
        ),
        Closed = true
    )
    val circle = Circle(
        Center = Point3d(x = 2.0, y = 5.0),
        Radius = 12.0,
        Layer = "0"
    )
    val mText = MText(
        Location = Point3d(x = -5.0, y = -30.0),
        Contents = "Um texto de teste",
        Rotation = 12.0,
        Layer = "LayerTeste"
    )
    val klaxon = Klaxon()

    println("Line:")
    println(klaxon.toJsonString(line))
    println("Polyline:")
    println(klaxon.toJsonString(polyline))
    println("Circle:")
    println(klaxon.toJsonString(circle))
    println("MText:")
    println(klaxon.toJsonString(mText))
}