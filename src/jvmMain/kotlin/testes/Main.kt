package testes

import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Line
import javafx.scene.shape.StrokeLineCap
import javafx.scene.text.Text
import javafx.scene.transform.Affine
import org.joml.Matrix3x2d
import org.joml.Matrix4d
import tornadofx.*
import kotlin.random.Random

fun main() {
    launch<MeuApp>()
}

class MeuApp : App(MeuView2::class)

class MeuView2 : View() {
    val canvas: Canvas = Canvas().apply {
        width = 1000.0
        height = 600.0
    }

    override val root = hbox {
        val sass = Line()
        val sdjd = Text("Oi")
        sdjd.translateX = 200.0
        sdjd.translateY = 200.0
        sdjd.scaleX = 3.0
        sdjd.scaleY = 3.0
        sdjd.rotate = -45.0
        this += sdjd
        this += canvas
        val gc = canvas.graphicsContext2D
        gc.strokeRect(20.0, 50.0, 100.0, 100.0)
        gc.strokePolygon(
            doubleArrayOf(100.0, 200.0, 150.0),
            doubleArrayOf(100.0, 100.0, 200.0),
            3
        )
        gc.fill = Color.GREENYELLOW
        gc.beginPath()
        gc.moveTo(500.0, 100.0)
        gc.lineTo(600.0, 100.0)
        gc.lineTo(600.0, 200.0)
        gc.lineTo(500.0, 200.0)
        gc.closePath()
        gc.moveTo(525.0, 125.0)
        gc.lineTo(525.0, 175.0)
        gc.lineTo(575.0, 175.0)
        gc.lineTo(575.0, 125.0)
        gc.closePath()
        val hatch: Image = createHatch()
        val pattern = ImagePattern(hatch, 0.0, 0.0, 20.0, 20.0, false)
        gc.fill = pattern
        gc.fill()

//        val matrizTransformacao = Matrix4d()
//        matrizTransformacao.translate(200.0, 200.0, 0.0)
//        val fatorEscala = 1.0
//        matrizTransformacao.scaleAround(fatorEscala, canvas.width / 2.0, canvas.height / 2.0, 0.0)
//        gc.transform = matrizTransformacao.toAffine()
//        println(matrizTransformacao)
        println("_________")
        println(gc.transform)

        gc.transform.prependScale(2.0, 2.0)
        gc.scale(1.0, 1.0)
        gc.stroke = Color.RED //Cor da linha
        gc.lineCap = StrokeLineCap.BUTT //Propriedade que diz respeito a borda da linha
        gc.lineWidth = 1.0 //Espessura da linha
        gc.strokeLine(0.0, 0.0, canvas.width, canvas.height)
        gc.strokeLine(0.0, canvas.height, canvas.width, 0.0)
        gc.fill = Color.GREEN
        gc.fillOval(
            canvas.width / 2.0 - 40.0, canvas.height / 2.0 - 40.0,
            80.0, 80.0
        )
    }

    private fun createHatch2(): Image {
        val canvas = Canvas(150.0, 150.0)
        val gc = canvas.graphicsContext2D
        gc.stroke = Color.RED
        gc.strokeLine(0.0, 0.0, 200.0, 200.0)
        gc.strokeLine(190.0,0.0,190.0,200.0)

//        val node = Pane(canvas)
//        canvas.widthProperty().bind(node.widthProperty())
//        canvas.heightProperty().bind(node.heightProperty())
//        node.setPrefSize(20.0,20.0)
//        Scene(node)
        val parametros = SnapshotParameters()
        parametros.isDepthBuffer = true
        parametros.fill = Color.TRANSPARENT
        parametros.viewport= Rectangle2D(0.0,0.0,5.0,5.0)
        val pane = Pane(canvas)
        Scene(pane)
        return pane.snapshot(parametros, null)
    }

    private fun createHatch(): Image {
        val pane = Pane()
        pane.setPrefSize(1.0, 1.0)
        val fw = Line(-5.0, -5.0, 25.0, 25.0)
        val bw = Line(-5.0, 25.0, 25.0, -5.0)
        fw.stroke = Color.RED
        bw.stroke = Color.BLUE
        fw.strokeWidth = 5.0
        bw.strokeWidth = 5.0
        pane.children.addAll(fw, bw)
        Scene(pane)
        return pane.snapshot(null, null)
    }

    private fun Matrix3x2d.toAffine() = Affine(m00, m01, m10, m11, m20, m21)
    private fun Matrix4d.toAffine() = //Affine(m00(), m01(), m10(), m11(), m30(), m31())
        Affine(
            m00(), m10(), m20(), m30(),
            m01(), m11(), m21(), m31(),
            m02(), m12(), m22(), m32()
        )
}

class MeuView : View() {
    val canvas = Canvas().apply {
        width = 1000.0
        height = 600.0
    }

    private var xyAleatorios = XYAleatorios(canvas.width, canvas.height)
    override val root = hbox {
        this += canvas
        this.setOnMouseClicked { event ->
            when (event.button) {
                MouseButton.PRIMARY -> configurar()
                else -> xyAleatorios = XYAleatorios(canvas.width, canvas.height)
            }
        }
    }

    private val configuracoes = arrayOf(
        {
            configurarCanvasSemMudancaDeEstado(
                canvas = canvas,
                gc = canvas.graphicsContext2D,
                xyAleatorios = xyAleatorios
            )
        },
        {
            configurarCanvasComMudancaDeEstado(
                canvas = canvas,
                gc = canvas.graphicsContext2D,
                xyAleatorios = xyAleatorios
            )
        }
    )
    private var indiceConfiguracoes = -1
    private fun configurar() {
        indiceConfiguracoes++
        if (indiceConfiguracoes > configuracoes.lastIndex) indiceConfiguracoes = 0
        configuracoes[indiceConfiguracoes].invoke()
    }

    private fun configurarCanvasSemMudancaDeEstado(
        canvas: Canvas,
        gc: GraphicsContext,
        xyAleatorios: XYAleatorios
    ) {
        val tempoInicial = System.currentTimeMillis()
        val listaCor = mutableListOf<Color>()
        gc.stroke = Color.BLACK
        configurarCanvas(canvas = canvas, xyAleatorios = xyAleatorios) { cor ->
            listaCor += cor
        }
        println(listaCor.size)
        val tempoGasto = System.currentTimeMillis() - tempoInicial
        println("${tempoGasto / 1000.0} segundos")
    }

    private fun configurarCanvasComMudancaDeEstado(
        canvas: Canvas,
        gc: GraphicsContext,
        xyAleatorios: XYAleatorios
    ) {
        val tempoInicial = System.currentTimeMillis()
        val listaCor = mutableListOf<Color>()
        configurarCanvas(canvas = canvas, xyAleatorios = xyAleatorios) { cor ->
            listaCor += cor
            gc.stroke = cor
        }
        println(listaCor.size)
        val tempoGasto = System.currentTimeMillis() - tempoInicial
        println("${tempoGasto / 1000.0} segundos")
    }

    private fun configurarCanvas(
        canvas: Canvas,
        xyAleatorios: XYAleatorios,
        funcaoAntesDeDesenharCadaLinha: (cor: Color) -> Unit
    ) {
        var cor: Color
        with(canvas.graphicsContext2D) {
            repeat(1_000) {
                var x = 0.0
                while (x <= canvas.width) {
                    cor = xyAleatorios.pegarCor()
                    funcaoAntesDeDesenharCadaLinha(cor)
                    strokeLine(
                        x, 0.0,
                        xyAleatorios.pegarX(), canvas.height
                    )
                    x += 3.0
                }
                var y = 0.0
                while (y <= canvas.height) {
                    cor = xyAleatorios.pegarCor()
                    funcaoAntesDeDesenharCadaLinha(cor)
                    strokeLine(
                        0.0, y,
                        canvas.width, xyAleatorios.pegarY()
                    )
                    y += 3.0
                }
            }
        }
    }
}
//
//private object Cores {
//    private val cores = arrayOf(
//        Color.BLUEVIOLET,
//        Color.DARKGOLDENROD,
//        Color.RED,
//        Color.AQUAMARINE,
//        Color.LIGHTPINK,
//        Color.YELLOW
//    )
//    private var indiceCor = -1
//    fun pegarCor(): Color {
//        indiceCor++
//        if (indiceCor > cores.lastIndex) indiceCor = 0
//        return cores[indiceCor]
//    }
//}

private class XYAleatorios(width: Double, height: Double) {
    private val arrayX = DoubleArray(width.toInt() + 2) { Random.nextDouble(0.0, width) }
    private val arrayY = DoubleArray(height.toInt() + 2) { Random.nextDouble(0.0, height) }
    private val arrayCores = Array(width.toInt() + 2) {
        Color.color(
            Random.nextDouble(0.0, 1.0),
            Random.nextDouble(0.0, 1.0),
            Random.nextDouble(0.0, 1.0)
        )
    }

    init {
        println("Novas")
    }

    private var indiceX = -1
    fun pegarX(): Double {
        indiceX++
        if (indiceX > arrayX.lastIndex) indiceX = 0
        return arrayX[indiceX]
    }

    private var indiceY = -1
    fun pegarY(): Double {
        indiceY++
        if (indiceY > arrayY.lastIndex) indiceY = 0
        return arrayY[indiceY]
    }

    private var indiceCor = -1
    fun pegarCor(): Color {
        indiceCor++
        if (indiceCor > arrayCores.lastIndex) indiceCor = 0
        return arrayCores[indiceCor]
    }
}