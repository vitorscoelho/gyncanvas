package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import vitorscoelho.gyncanvas.core.primitives.Color
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

private class RGBA(val red: Short, val green: Short, val blue: Short, val alpha: Float) {
    val redWebGL: Float get() = toFloatColor(red)
    val greenWebGL: Float get() = toFloatColor(green)
    val blueWebGL: Float get() = toFloatColor(blue)

    private fun toFloatColor(intValue: Short) = intValue.toFloat() / 255f
}

class WebGLStaticDrawer2D(drawingArea: JSDrawingArea) {
    private val gl = getWebGLContext(drawingArea = drawingArea)

    private val program = Simple2DProgram(gl = gl)

    private val positionBuffer: WebGLBuffer = gl.createEmptyBuffer()
    private val colorIndexBuffer: WebGLBuffer = gl.createEmptyBuffer()


    fun setElements(elements: List<Primitive>) {
        val vertexCount = elements.sumBy { it.verticesCount }

        val vertexPosition = Array<Float>(size = vertexCount * 2) { 0f }
        val vertexColorIndex = Array<Short>(size = vertexCount) { 0 }

        val indexedColorsMap = hashMapOf<RGBA, Short>()

        var currentIndexColor: Short = 0
        var currentVertex = 0
        elements.forEachIndexed { _, element ->
            element.forEachVertex { _, x, y, _, red, green, blue, alpha ->
                vertexPosition[currentVertex * 2] = x
                vertexPosition[currentVertex * 2 + 1] = y

                val color = RGBA(red, green, blue, alpha)
                if (indexedColorsMap.contains(color)) indexedColorsMap[color] = currentIndexColor++
                vertexColorIndex[currentVertex] = indexedColorsMap[color]!!

                currentVertex++
            }
        }

        val colors = indexedColorsMap
            .toList()
            .sortedBy { (_, index) -> index }
            .flatMap { (rgba, _) -> listOf(rgba.redWebGL, rgba.greenWebGL, rgba.blueWebGL, rgba.alpha) }
            .toTypedArray()

        gl.changeArrayBufferStaticData(positionBuffer, vertexPosition)
        gl.changeArrayBufferStaticData(colorIndexBuffer, vertexColorIndex)
        program.setColors(colors)
    }

    fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gl.adjustViewportAndClear(backgroundColor = backgroundColor)

        program.use()

        val positionBuffer: WebGLBuffer = gl.createArrayBufferStaticData(
            data = Float32Array(
                arrayOf(
                    -1f, 0f,
                    0f, 1f,
                    1f, 0f
                )
            )
        )
        program.setAttributes(
            positionBuffer = positionBuffer
        )
        program.setCamera(
            transformMatrix = camera.toWebGLMat3()
        )
        gl.drawArrays(
            mode = GL.TRIANGLES,
            first = 0,
            count = 3
        )
    }
}

