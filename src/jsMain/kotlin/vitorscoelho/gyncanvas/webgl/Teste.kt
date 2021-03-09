package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLContextAttributes
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import org.w3c.dom.HTMLCanvasElement

private val webGLContextAttributes = object : WebGLContextAttributes {
    override var alpha: Boolean? = true
    override var depth: Boolean? = true
    override var stencil: Boolean? = false
    override var antialias: Boolean? = true
    override var premultipliedAlpha: Boolean? = true
    override var preserveDrawingBuffer: Boolean? = true
    override var preferLowPowerToHighPerformance: Boolean? = undefined
    override var failIfMajorPerformanceCaveat: Boolean? = false
}

fun desenharTriangulo(canvas: HTMLCanvasElement) {
    /*
    Baseado na playlist de vídeos:
    https://www.youtube.com/watch?v=bP7_FeP9kU4&list=PL2935W76vRNHFpPUuqmLoGCzwx_8eq5yK&index=2
    */
    val gl: WebGLRenderingContext =
        canvas.getContext("webgl", webGLContextAttributes) as WebGLRenderingContext

    //Dados dos vértices do triângulo
    val vertexData = arrayOf(
        0f, 1f, 0f,
        1f, -1f, 0f,
        -1f, -1f, 0f
    )//TODO ver se não tem jeito de usar floatArrayOf

    //Dados das cores (RGB) dos vértices do triângulo
    val colorData = arrayOf(
        1f, 0f, 0f,
        0f, 1f, 0f,
        0f, 0f, 1f
    )

    //Criação do buffer
    val positionBuffer = gl.createBuffer()
    gl.bindBuffer(GL.ARRAY_BUFFER, positionBuffer)
    gl.bufferData(
        GL.ARRAY_BUFFER,
        Float32Array(vertexData),
        GL.STATIC_DRAW
    )//TODO talvez mudar para Float64Array

    //Criação do buffer de posições dos vértices
    val colorBuffer = gl.createBuffer()
    gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, Float32Array(colorData), GL.STATIC_DRAW)

    //Criação do VertexShader (Programa que roda na GPU)
    val vertexShader = gl.createShader(GL.VERTEX_SHADER)
    val vertexShaderSourceCode = """
        precision mediump float;
        
        attribute vec3 position;
        attribute vec3 color;
        varying vec3 vColor;
        
        uniform mat4 transformMatrix;

        void main(){
            vColor = color;
            gl_Position = transformMatrix * vec4(position, 1);
        }
    """.trimIndent()
    gl.shaderSource(vertexShader, vertexShaderSourceCode)
    gl.compileShader(vertexShader)

    //Criação do FragmentShader (Programa que roda na GPU)
    val fragmentShader = gl.createShader(GL.FRAGMENT_SHADER)
    val fragmentShaderSourceCode = """
        precision mediump float;
        varying vec3 vColor;
        void main(){
            gl_FragColor = vec4(vColor, 1);
        }
    """.trimIndent()
    gl.shaderSource(fragmentShader, fragmentShaderSourceCode)
    gl.compileShader(fragmentShader)

    //Criação do programa de shader
    val program = gl.createProgram()
    gl.attachShader(program, vertexShader)
    gl.attachShader(program, fragmentShader)
    gl.linkProgram(program)

    //Ativando o atributo 'position' do VertexShader
    val positionLocation = gl.getAttribLocation(program, "position")
    gl.enableVertexAttribArray(positionLocation)
    //Refazendo o bind porque, acima, ele foi alterado para o colorBuffer
    gl.bindBuffer(GL.ARRAY_BUFFER, positionBuffer)
    //Descrevendo para o WebGL como o buffer deve ser lido
    gl.vertexAttribPointer(positionLocation, 3, GL.FLOAT, false, 0, 0)

    //Ativando o atributo 'color' do FragmentShader
    val colorLocation = gl.getAttribLocation(program, "color")
    gl.enableVertexAttribArray(colorLocation)
    //Refazendo o bind porque, acima, ele foi alterado para o colorBuffer
    gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer)
    //Descrevendo para o WebGL como o buffer deve ser lido
    gl.vertexAttribPointer(colorLocation, 3, GL.FLOAT, false, 0, 0)

    //Especificando para WebGL qual programa deverá ser utilizado
    gl.useProgram(program)

    //Especificando as variáveis globais do shader
    val uniformLocationMatrixTransform = gl.getUniformLocation(program, "transformMatrix")
    val matrizTransformacao = arrayOf(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0.5f, 0f, 0f, 1f
    )//TODO alterar para matriz 3x3 e (os vetores tbm) para otimizar para 2D
    gl.uniformMatrix4fv(uniformLocationMatrixTransform, false, Float32Array(matrizTransformacao))

    gl.viewport(0, 0, canvas.clientWidth, canvas.clientHeight)
    gl.clearColor(1f, 0f, 0f, 1f)
    gl.clear(GL.COLOR_BUFFER_BIT)
    //Desenhando o array a partir do programa especificado
    gl.drawArrays(GL.TRIANGLES, 0, 3)

    //Testes
//    window.setTimeout({
//        val novaMatrizTransformacao = arrayOf(
//            1f, 0f, 0f, 0f,
//            0f, 1f, 0f, 0f,
//            0f, 0f, 1f, 0f,
//            0f, 0f, 0f, 1f
//        )//TODO alterar para matriz 3x3 e (os vetores tbm) para otimizar para 2D
//        gl.uniformMatrix4fv(uniformLocationMatrixTransform, false, Float32Array(novaMatrizTransformacao))
//        gl.drawArrays(GL.LINE_LOOP, 0, 3)
//    },2000)
    var usarMatrizNova = true
    canvas.addEventListener("click", {
        val novaMatrizTransformacao = arrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )//TODO alterar para matriz 3x3 e (os vetores tbm) para otimizar para 2D
        val matrizAtual = if (usarMatrizNova) novaMatrizTransformacao else matrizTransformacao
        val tipoDesenho = if (usarMatrizNova) GL.LINE_LOOP else GL.TRIANGLES
        usarMatrizNova = !usarMatrizNova
        gl.uniformMatrix4fv(uniformLocationMatrixTransform, false, Float32Array(matrizAtual))
        gl.clearColor(0f, 1f, 0f, 1f)
        gl.clear(GL.COLOR_BUFFER_BIT)
        gl.drawArrays(tipoDesenho, 0, 3)
    })
}