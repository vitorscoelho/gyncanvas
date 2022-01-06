package vitorscoelho.gyncanvas.webgl

import org.w3c.dom.CanvasRenderingContext2D
import vitorscoelho.gyncanvas.Drawer
import vitorscoelho.gyncanvas.OrthographicCamera2D
import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.core.primitives.Drawable
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.core.primitives.PrimitiveType


/*
private val mapGlTypes: Map<PrimitiveType, Int> = mapOf(
    PrimitiveType.POINTS to GL.POINTS,
    PrimitiveType.LINES to GL.LINES,
    PrimitiveType.LINE_STRIP to GL.LINE_STRIP,
    PrimitiveType.LINE_LOOP to GL.LINE_LOOP,
    PrimitiveType.TRIANGLES to GL.TRIANGLES,
    PrimitiveType.TRIANGLE_STRIP to GL.TRIANGLE_STRIP,
    PrimitiveType.TRIANGLE_FAN to GL.TRIANGLE_FAN,
)
 */

private class Vector(val x: Double, val y: Double) {
    companion object {
        val ZERO = Vector(x = 0.0, y = 0.0)
    }
}

private val NonePrimitive = object : Primitive {
    override val type: PrimitiveType get() = TODO("Not yet implemented")
    override val verticesCount: Int get() = TODO("Not yet implemented")

    override fun forEachVertex(action: (index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float) -> Unit) {
        TODO("Not yet implemented")
    }
}

private class Dados(val vertices: Array<Vector>, val rgba: String)

private fun dados(primitive: Primitive): Dados {
    var r = ""
    var g = ""
    var b = ""
    var a = ""
    val vertices = Array(size = primitive.verticesCount) { Vector.ZERO }
    primitive.forEachVertex { index, x, y, _, red, green, blue, alpha ->
        vertices[index] = Vector(x = x.toDouble(), y = y.toDouble())
        if (index == 0) {
            r = red.toString()
            g = green.toString()
            b = blue.toString()
            a = alpha.toString()
        }
    }
    return Dados(vertices = vertices, rgba = "rgba($r, $g, $b, $a)")
}

//index: Int, x: Float, y: Float, z: Float, red: Short, green: Short, blue: Short, alpha: Float
private val mapGLTypes: Map<PrimitiveType, (primitive: Primitive, gc: CanvasRenderingContext2D) -> Unit> by lazy {
    mapOf(
//    PrimitiveType.LINES to { primitive, gc ->
//        val dados = dados(primitive = primitive)
//        val vertices = dados.vertices
//        val ultimoIndice = vertices.lastIndex - (vertices.size % 2)
//        var indiceAtual = 0
//        while (indiceAtual <= ultimoIndice) {
//            val ponto1 = vertices[indiceAtual++]
//            val ponto2 = vertices[indiceAtual++]
//            gc.strokeStyle = dados.rgba
//            gc.beginPath()
//            gc.moveTo(x = ponto1.x, y = ponto2.y)
//            gc.lineTo(x = ponto2.x, y = ponto2.y)
//        }
//    },
//    PrimitiveType.LINE_STRIP to { primitive, gc ->
//        val dados = dados(primitive = primitive)
//        val vertices = dados.vertices
//        val ultimoIndice = vertices.lastIndex
//        var indiceAtual = 0
//        val pontoInicial = vertices[indiceAtual++]
//        gc.strokeStyle = dados.rgba
//        gc.beginPath()
//        gc.moveTo(x = pontoInicial.x, y = pontoInicial.y)
//        while (indiceAtual <= ultimoIndice) {
//            val pontoAtual = vertices[indiceAtual++]
//            gc.lineTo(x = pontoAtual.x, y = pontoAtual.y)
//        }
//    },
        PrimitiveType.LINES to { primitive, gc ->
            val dados = dados(primitive = primitive)
            val vertices = dados.vertices
            gc.strokeStyle = dados.rgba
            gc.beginPath()
            vertices.indices.forEach { index ->
                if (index % 2 == 0) {
                    gc.moveTo(x = vertices[index].x, y = vertices[index].y)
                } else {
                    gc.lineTo(x = vertices[index].x, y = vertices[index].y)
                }
            }
            gc.stroke()
        },
        PrimitiveType.LINE_STRIP to { primitive, gc ->
            val dados = dados(primitive = primitive)
            val vertices = dados.vertices
            gc.strokeStyle = dados.rgba
            gc.beginPath()
            gc.moveTo(x = vertices[0].x, y = vertices[0].y)
            if (vertices.size > 1) {
                (1..vertices.lastIndex).forEach { index ->
                    gc.lineTo(x = vertices[index].x, y = vertices[index].y)
                }
            }
            gc.stroke()
        },
        PrimitiveType.LINE_LOOP to { primitive, gc ->
            mapGLTypes[PrimitiveType.LINE_STRIP]!!(primitive, gc)
            gc.closePath()
            gc.stroke()
        },
        PrimitiveType.TRIANGLES to { primitive, gc ->
            val dados = dados(primitive = primitive)
            val vertices = dados.vertices
            gc.fillStyle = dados.rgba
            gc.beginPath()
            vertices.indices.forEach { index ->
                if (index % 3 == 0) {
                    gc.moveTo(x = vertices[index].x, y = vertices[index].y)
                } else {
                    gc.lineTo(x = vertices[index].x, y = vertices[index].y)
                    if ((index + 1) % 3 == 0) {
                        gc.fill()
                    }
                }
            }
        },
    )
}

class ImplementacaoCanvas2D internal constructor(val drawingArea: JSDrawingArea) : Drawer() {
    private val gc: CanvasRenderingContext2D = drawingArea.canvas.getContext("2d") as CanvasRenderingContext2D

    private var elements = emptyList<Drawable>()
    override fun setElements(elements: List<Drawable>) {
        this.elements = elements
    }

    override fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gc.resetTransform()
        gc.clearRect(x = 0.0, y = 0.0, w = drawingArea.width.toDouble(), h = drawingArea.height.toDouble())
        gc.fillStyle = with(backgroundColor) { "rgba($red, $green, $blue, $alpha)" }
        gc.fillRect(x = 0.0, y = 0.0, w = drawingArea.width.toDouble(), h = drawingArea.height.toDouble())

        gc.translate(drawingArea.width.toDouble() * .5, drawingArea.height.toDouble() * .5)
        gc.scale(x = 20.0, y = -20.0)
        gc.lineWidth = 1.0 / 20.0
        elements.forEach { element ->
            element.forEachPrimitive { primitive ->
                val funcaoDesenho = mapGLTypes[primitive.type]!!
                funcaoDesenho(primitive, gc)
            }
        }
    }
}