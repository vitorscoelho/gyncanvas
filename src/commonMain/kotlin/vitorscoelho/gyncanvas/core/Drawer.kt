package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPoint
import vitorscoelho.gyncanvas.core.dxf.entities.CompositeEntity
import vitorscoelho.gyncanvas.core.dxf.entities.Entity
import vitorscoelho.gyncanvas.math.TransformationMatrix
import kotlin.js.JsName

abstract class Drawer {
    val camera: Camera by lazy { Camera(this) }

    abstract var canvasWidth: Double
        protected set
    abstract var canvasHeight: Double
        protected set

    protected abstract var transform: TransformationMatrix

    @JsName("drawerFill")
    abstract var fill: Color

    @JsName("drawerStroke")
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

    @JsName("drawerFillFunction")
    abstract fun fill()

    @JsName("drawerStrokeFunction")
    abstract fun stroke()
    abstract fun moveTo(x: Double, y: Double)
    abstract fun lineTo(x: Double, y: Double)
    abstract fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double)

    fun draw(backgroundColor: Color, entities: Collection<Entity>) {
//        drawer.apllyCameraTransform()
        transform = camera.transformationMatrix
        fill = backgroundColor
        fillBackground()
        entities.forEach { entity -> drawEntity(entity) }
    }

    private fun drawEntity(entity: Entity) {
        if (entity is CompositeEntity) {
            entity.entities.forEach { drawEntity(it) }
        } else {
            entity.shapeType.applyColor(drawer = this, entity = entity)
            entity.draw(drawer = this)
        }//TODO dar um jeito de remover esse 'if'
    }
}