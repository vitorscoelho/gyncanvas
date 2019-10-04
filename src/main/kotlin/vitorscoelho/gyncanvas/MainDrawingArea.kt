package vitorscoelho.gyncanvas

import javafx.scene.Cursor
import javafx.scene.input.MouseButton
import tornadofx.*
import vitorscoelho.gyncanvas.core.dxf.DrawingArea
import vitorscoelho.gyncanvas.core.dxf.FXDrawingArea
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D

fun main() {
    launch<MeuAppDrawingArea>()
}

class MeuAppDrawingArea : App(MeuViewDrawingArea::class)

class MeuViewDrawingArea : View() {
    val drawingArea = FXDrawingArea().apply {
        addEntities(criarBloco().listarDesenhos())
    }
    val transformacoes = MutableTransformationMatrix()
    override val root = hbox {
        setPrefSize(1300.0, 600.0)
        this += drawingArea.node
        drawingArea.draw(transformacoes)

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
                val deltaX: Double = (event.x - xPanInicial) / transformacoes.scale
                val deltaY: Double = (event.y - yPanInicial) / transformacoes.scale

                transformacoes.translate(deltaX, deltaY)

                xPanInicial = event.x
                yPanInicial = event.y
            }
            drawingArea.draw(transformacoes)
        }

        val fatorDeEscala = 1.2
        setOnScroll { event ->
            val coordenadasMundoClique = transformacoes.worldCoordinates(xDrawingArea = event.x, yDrawingArea = event.y)
            if (event.deltaY > 0) {
                transformacoes.scale(
                    factor = fatorDeEscala,
                    xOrigin = coordenadasMundoClique.x,
                    yOrigin = coordenadasMundoClique.y
                )
                println("Rolou positivo")
            } else if (event.deltaY < 0) {
                transformacoes.scale(
                    factor = 1.0 / fatorDeEscala,
                    xOrigin = coordenadasMundoClique.x,
                    yOrigin = coordenadasMundoClique.y
                )
                println("Rolou negativo")
            }
            drawingArea.draw(transformacoes)
        }
    }
}

fun criarBloco(): BlocoDeFundacao {
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
//    val listaJson = blocoDeFundacao.mapPrimitivas { primitiva, atributo -> primitiva.toJsonEntity() }
//    val jsonDrawing = JsonDrawing(
//        tablesSection = emptyList(),
//        entitiesSection = listaJson
//    )
//    val jacksonMapper = jacksonObjectMapper()
//    println(jacksonMapper.writeValueAsString(jsonDrawing))
    return blocoDeFundacao
}