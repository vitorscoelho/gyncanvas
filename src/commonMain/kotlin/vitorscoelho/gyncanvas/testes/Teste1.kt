package vitorscoelho.gyncanvas.testes

import vitorscoelho.gyncanvas.core.DrawingArea
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.*
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.math.Vector2D

fun desenhar(drawingArea: DrawingArea) {
    val entitiesList = listOf<Entity>(
        Line(
            layer = Layer(name = "la", color = Color.INDEX_10),
            startPoint = Vector2D(x = 0.0, y = 0.0),
            endPoint = Vector2D(x = 600.0, y = -300.0)
        ),
        Circle(
            layer = Layer(name = "ci", color = Color.INDEX_103),
            centerPoint = Vector2D(x = 600.0, y = -300.0),
            diameter = 200.0
        ),
//        MText(
//            layer = Layer(name = "te", color = Color.INDEX_10),
//            style = TextStyle(name = "ts", fontName = "Ubuntu Light", fontFileName = "Ubuntu Light"),
//            justify = AttachmentPoint.MIDDLE_LEFT,
//            size = 40.0,
//            rotation = 0.0,
//            position = Vector2D(x = 600.0, y = -300.0),
//            content = "Título"
//        )
    )
    val drawer = drawingArea.drawer
    drawer.camera.setPosition(600.0,100.0)
    drawer.draw(backgroundColor = Color.INDEX_250, entities = entitiesList)
}