package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPoint

abstract class Drawer {
    abstract val camera: Camera
    abstract var fill: Color
    abstract var stroke: Color
    abstract var lineWidht: Double
    abstract var textJustify: AttachmentPoint
    abstract var fontName: String
    abstract fun setFont(fontName: String)
    abstract fun fillBackground()
    abstract fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double)
    abstract fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double)
    abstract fun fillText(text: String, size: Double, x: Double, y: Double, angle: Double)
    abstract fun beginPath()
    abstract fun closePath()
    abstract fun fill()
    abstract fun stroke()
    abstract fun moveTo(x: Double, y: Double)
    abstract fun lineTo(x: Double, y: Double)
    abstract fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double)
    abstract fun apllyCameraTransform()
}