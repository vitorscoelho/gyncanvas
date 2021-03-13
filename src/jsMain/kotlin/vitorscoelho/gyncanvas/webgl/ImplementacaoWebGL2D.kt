package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.core.primitives.Drawable
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.core.primitives.PrimitiveType
import vitorscoelho.gyncanvas.webgl.programs.Simple2DProgram

private val mapGlTypes: Map<PrimitiveType, Int> = mapOf(
    PrimitiveType.POINTS to GL.POINTS,
    PrimitiveType.LINES to GL.LINES,
    PrimitiveType.LINE_STRIP to GL.LINE_STRIP,
    PrimitiveType.LINE_LOOP to GL.LINE_LOOP,
    PrimitiveType.TRIANGLES to GL.TRIANGLES,
    PrimitiveType.TRIANGLE_STRIP to GL.TRIANGLE_STRIP,
    PrimitiveType.TRIANGLE_FAN to GL.TRIANGLE_FAN,
)

private val Primitive.glType: Int get() = mapGlTypes[this.type]!!

private fun rgbToFloat(decimal: Short) = decimal.toFloat() / 255f

class WebGLStaticDrawer2D(drawingArea: JSDrawingArea) {
    private val gl = getWebGLContext(drawingArea = drawingArea)

    private val program = Simple2DProgram(gl = gl)

    private var elementsTypes: IntArray = intArrayOf()
    private var elementsVerticesCount: IntArray = intArrayOf()

    private val positionBuffer: WebGLBuffer = gl.createEmptyBuffer()
    private val colorBuffer: WebGLBuffer = gl.createEmptyBuffer()

    fun setElements(elements: List<Drawable>) {
        val lista = mutableListOf<Primitive>()
        elements.forEach { element ->
            element.forEachPrimitive { primitive ->
                lista += primitive
            }
        }
        setElements(lista)
    }

    private fun setElements(elements: List<Primitive>) {
        elementsTypes = IntArray(size = elements.size)
        elementsVerticesCount = IntArray(size = elements.size)

        val vertexCount = elements.sumBy { it.verticesCount }

        val vertexPosition = Array<Float>(size = vertexCount * 2) { 0f }
        val vertexColor = Array<Float>(size = vertexCount * 3) { 0f }

        var positionIndex = 0
        var colorIndex = 0
        elements.forEachIndexed { elementIndex, element ->
            elementsTypes[elementIndex] = element.glType
            elementsVerticesCount[elementIndex] = element.verticesCount
            element.forEachVertex { _, x, y, _, red, green, blue, _ ->
                vertexPosition[positionIndex++] = x
                vertexPosition[positionIndex++] = y
                vertexColor[colorIndex++] = rgbToFloat(red)
                vertexColor[colorIndex++] = rgbToFloat(green)
                vertexColor[colorIndex++] = rgbToFloat(blue)
            }
        }

        println(vertexPosition)
        println(vertexColor)

        gl.changeArrayBufferStaticData(positionBuffer, vertexPosition)
        gl.changeArrayBufferStaticData(colorBuffer, vertexColor)
    }

    fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gl.adjustViewportAndClear(backgroundColor = backgroundColor)
        program.use()
        program.setAttributes(positionBuffer = positionBuffer, colorBuffer = colorBuffer)
        program.setCamera(transformMatrix = camera.toWebGLMat3())
        var first = 0
        for (index in 0..elementsTypes.size) {
            gl.drawArrays(mode = elementsTypes[index], first = first, count = elementsVerticesCount[index])
            first += elementsVerticesCount[index]
        }
    }
}

