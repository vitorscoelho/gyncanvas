package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.*
import vitorscoelho.gyncanvas.core.primitives.*
import vitorscoelho.gyncanvas.webgl.programs.Simple3DProgram
import org.khronos.webgl.WebGLRenderingContext.Companion as GL

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

/**
 * Drawer com as seguintes características:
 * - Otimizado para casos onde não se acrescenta ou remove elementos com frequência. Ou seja, ideal para casos de desenhos fixos onde só se deseja fazer uma navegação pelo desenho
 * - Sempre que um elemento [Drawable] for adicionado ou removido, toda a lógica de criação de buffer vai acontecer para cada um dos elementos, mesmo os já existentes
 */
class WebGLStaticDrawer(val drawingArea: JSDrawingArea) {
    private val gl: WebGLRenderingContext = getWebGLContext(drawingArea = drawingArea)

    private val simpleProgram = Simple3DProgram(gl = gl)

    private var elementsTypes: IntArray = intArrayOf()
    private var elementsVerticesCount: IntArray = intArrayOf()
    private val positionsBuffer: WebGLBuffer = createEmptyArrayBufferStaticDraw(gl = gl)
    private val colorsBuffer: WebGLBuffer = createEmptyArrayBufferStaticDraw(gl = gl)

    /**Seta a lista de elementos que serão desenhados*/
    fun setElements(elements: List<Primitive>) {
//        elementsTypes = elements.map { it.glType }.toIntArray()
//        elementsVerticesCount = elements.map { it.verticesCount }.toIntArray()
//        val coordinatesData =
//            elements
//                .map { it.coordinates }
//                .flatMap { it.toList() }
//                .toTypedArray()
//        val colorsData =
//            elements
//                .flatMap { element ->
//                    val retorno = mutableListOf<Float>()
//                    repeat(element.verticesCount) {
//                        retorno += element.color.red
//                        retorno += element.color.green
//                        retorno += element.color.blue
//                    }
//                    retorno
//                }
//                .toTypedArray()
        val totalVertices = elements.sumBy { it.verticesCount }
        val coordinatesData = FloatArray(size = totalVertices * 3)//Vezes 3 porque é 3D
        val colorsData = FloatArray(size = totalVertices * 3)//Vezes 3 porque passa o RGB sem alpha, por enquanto
        var coordinateIndex = 0
        var colorIndex = 0
        elementsTypes = IntArray(size = elements.size)
        elementsVerticesCount = IntArray(size = elements.size)
        elements.forEachIndexed { index, element ->
            elementsTypes[index] = element.glType
            elementsVerticesCount[index] = element.verticesCount
            element.forEachVertex { _, x, y, z, color ->
                coordinatesData[coordinateIndex++] = x
                coordinatesData[coordinateIndex++] = y
                coordinatesData[coordinateIndex++] = z
                colorsData[colorIndex++] = color.red
                colorsData[colorIndex++] = color.green
                colorsData[colorIndex++] = color.blue
            }
        }
        println(elementsTypes)
        println(coordinatesData)
        println(colorsData)
        gl.bindBuffer(GL.ARRAY_BUFFER, positionsBuffer)
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(coordinatesData.toTypedArray()), GL.STATIC_DRAW)
        gl.bindBuffer(GL.ARRAY_BUFFER, colorsBuffer)
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(colorsData.toTypedArray()), GL.STATIC_DRAW)
    }

    fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gl.adjustViewportAndClear(backgroundColor=backgroundColor)

        //TODO Atribuir os parâmetros de cada elemento para os shaders
        simpleProgram.use()
        simpleProgram.setUniforms(transformMatrix = camera.toWebGLMat4())
        simpleProgram.setAttributes(
            positionBuffer = positionsBuffer,
            colorBuffer = colorsBuffer
        )

        //TODO Invocar gl.drawArrays para cada elemento
        //TODO ideia de uma implementação específica:
        ///Criar um buffer com as informações de vértices de todos os elementos.
        ///Mapear, para cada elemento, o tipo (GL.LINES, GL.TRIANGLES,..) o índice do primeiro vértice no buffer e a qtd de vértices no buffer
        ///Assim, bastaria ficar chamando o gl.drawArrays várias vezes
        var first = 0
        for (index in 0..elementsTypes.size) {
            gl.drawArrays(mode = elementsTypes[index], first = first, count = elementsVerticesCount[index])
            first += elementsVerticesCount[index]
        }
    }
}