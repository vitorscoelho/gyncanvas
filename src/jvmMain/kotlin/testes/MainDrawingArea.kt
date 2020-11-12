package testes

import javafx.scene.Cursor
import javafx.scene.input.MouseButton
import tornadofx.*
import vitorscoelho.gyncanvas.core.dxf.FXDrawingArea
import vitorscoelho.gyncanvas.core.dxf.PanDragged
import vitorscoelho.gyncanvas.core.dxf.ZoomScroll
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

fun main() {
    launch<MeuAppDrawingArea>()
}

class MeuAppDrawingArea : App(MeuViewDrawingArea::class)

class MeuViewDrawingArea : View() {
    val drawingArea = FXDrawingArea().apply {
        addEntities(criarBloco().listarDesenhos())
    }

    init {
        PanDragged(mouseButton = MouseButton.MIDDLE, cursorPan = Cursor.MOVE, drawingArea = drawingArea).enable()
        ZoomScroll(zoomFactor = 1.2, drawingArea = drawingArea).enable()
    }

    val transformacoes = MutableTransformationMatrix()
    override val root = hbox {
        setPrefSize(1300.0, 600.0)
        this += drawingArea.node

//        var pontoZoom: Vector2D? = null
//        setOnMouseClicked { event ->
//            if (event.button == MouseButton.PRIMARY) {
//                println("Clicou ${drawingArea.camera.worldCoordinates(event.x, event.y)}")
////            drawingArea.camera.translate(deltaX = 20.0, deltaY = -20.0)
//                drawingArea.camera.setPosition(x = 3.2, y = 2.05, zoom = 200.0)
//                drawingArea.draw()
//            } else if (event.button == MouseButton.SECONDARY) {
//                if (pontoZoom == null) {
//                    pontoZoom = drawingArea.camera.worldCoordinates(event.x, event.y)
//                } else {
//                    val outroPontoZoom = drawingArea.camera.worldCoordinates(event.x, event.y)
//                    drawingArea.camera.zoomWindow(
//                        x1 = pontoZoom!!.x, y1 = pontoZoom!!.y, x2 = outroPontoZoom.x, y2 = outroPontoZoom.y
//                    )
//                    drawingArea.draw()
//                    pontoZoom = null
//                }
//            }
//        }
    }
}

fun criarBloco(): BlocoDeFundacao {
    val blocoDeFundacao = BlocoDeFundacao(
        hxPilar = 80.0 / 100.0,
        hyPilar = 40.0 / 100.0,
        folgaDeMontagem = 7.5 / 100.0,
        hcx = 35.0 / 100.0,
        hcy = 25.0 / 100.0,
        embutimento = 130.0 / 100.0,
        espessuraNivelamento = 5.0 / 100.0,
        lxBloco = 320.0 / 100.0,
        lyBloco = 205.0 / 100.0,
        nivelPisoAcabado = 50034 / 100,
        diametroEstacas = 60.0 / 100.0,
        posicoesEstacas = listOf(
            Vector2D(x = -115.0 / 100.0, y = 0.0),
            Vector2D(x = 115.0 / 100.0, y = 0.0)
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