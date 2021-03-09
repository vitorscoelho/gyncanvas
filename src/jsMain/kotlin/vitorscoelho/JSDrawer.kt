package vitorscoelho

import kotlinx.browser.window
import org.w3c.dom.*
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPoint
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointAlign
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointBaseline
import vitorscoelho.gyncanvas.math.TransformationMatrix

private val mapTextBaselineAutocadToCanvasJS: Map<AttachmentPointBaseline, CanvasTextBaseline> by lazy {
    mapOf(
        AttachmentPointBaseline.BOTTOM to CanvasTextBaseline.BOTTOM,
        AttachmentPointBaseline.MIDDLE to CanvasTextBaseline.MIDDLE,
        AttachmentPointBaseline.TOP to CanvasTextBaseline.TOP
    )
}

private val mapTextAlignAutocadToCanvasJS: Map<AttachmentPointAlign, CanvasTextAlign> by lazy {
    mapOf(
        AttachmentPointAlign.LEFT to CanvasTextAlign.LEFT,
        AttachmentPointAlign.CENTER to CanvasTextAlign.CENTER,
        AttachmentPointAlign.RIGHT to CanvasTextAlign.RIGHT
    )
}

private fun getTextBaseline(attachmentPointBaseline: AttachmentPointBaseline): CanvasTextBaseline =
    mapTextBaselineAutocadToCanvasJS[attachmentPointBaseline]!!

private fun getTextAlign(attachmentPointAlign: AttachmentPointAlign): CanvasTextAlign =
    mapTextAlignAutocadToCanvasJS[attachmentPointAlign]!!

private fun stringRgb(color: Color) = "rgb(${color.red},${color.green},${color.blue})"

class JSDrawer(val canvas: HTMLCanvasElement) : Drawer() {
    private val gc: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D

    override var canvasWidth: Double
        get() = canvas.getBoundingClientRect().width
        set(value) {}
    override var canvasHeight: Double
        get() = canvas.getBoundingClientRect().width
        set(value) {}

    init {
        window.addEventListener("resize", { println("Mudou tamanho") })
    }

    override var fill: Color = Color.INDEX_250
        set(value) {
            field = value
            gc.fillStyle = stringRgb(color = value)
        }

    override var stroke: Color = Color.INDEX_250
        set(value) {
            field = value
            gc.strokeStyle = stringRgb(color = value)
        }

    override var lineWidht: Double
        set(value) {
            gc.lineWidth = value
        }
        get() = gc.lineWidth

    override var textJustify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT
        set(value) {
            field = value
            gc.textAlign = getTextAlign(value.align)
            gc.textBaseline = getTextBaseline(value.baseline)
        }

    override var fontName: String = gc.font

    /*
    override fun setFont(fontName: String) {
        this.fontName = fontName
        gc.font = getFXFont(fontName = fontName)
    }
     */
    override fun setFont(fontName: String) {
//        this.fontName //TODO
    }

    override fun fillBackground() = gc.fillRect(
        0.0,
        0.0,
        this.canvas.width.toDouble(),
        this.canvas.height.toDouble()
    )//TODO corrigir, pois, desta maneira,não preenche a tela inteira, já que a matriz pode estar alterada por escalas, translações, etc...

    override fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double) {
        beginPath()
        moveTo(x = x1, y = y1)
        lineTo(x = x2, y = y2)
        stroke()
        closePath()
    }

    override fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double) {
        val radius = diameter / 2.0
        gc.beginPath()
        gc.arc(x = xCenter, y = -yCenter, radius = radius, startAngle = 0.0, endAngle = 6.283185307179586)
        gc.stroke()
        gc.closePath()
    }

    override fun fillText(text: String, size: Double, x: Double, y: Double, angle: Double) {
        TODO("Not yet implemented")
    }

    override fun beginPath() = gc.beginPath()
    override fun closePath() = gc.closePath()
    override fun fill() = gc.fill()
    override fun stroke() = gc.stroke()
    override fun moveTo(x: Double, y: Double) = gc.moveTo(x, -y)
    override fun lineTo(x: Double, y: Double) = gc.lineTo(x, -y)
    override fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double) =
        gc.arcTo(xTangent1, -yTangent1, xTangent2, -yTangent2, radius)

    protected override var transform: TransformationMatrix = TransformationMatrix.IDENTITY
        set(value) {
            field = value
            with(value) {
                gc.setTransform(
                    a = mxx, b = mxy, c = myx,
                    d = myy, e = tx, f = ty
                )
            }
        }
}