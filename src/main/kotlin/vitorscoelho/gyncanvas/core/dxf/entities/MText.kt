package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.core.dxf.transformation.ImmutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

data class MText(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val style: TextStyle,
    val size: Double,
    val justify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT,
    val rotation: Double = 0.0,
    val position: Vector2D,
    val content: String
) : Entity {
    override val shapeType: ShapeType
        get() = ShapeType.FILLED

    override fun draw(drawer: Drawer) {
        applyLineWidth(drawer = drawer)
        drawer.setFont(fontName = style.fontFileName, fontSize = size)
        drawer.textJustify = justify
        drawer.fillText(text = content,x = position.x,y = position.y,angle = rotation)
    }

    override fun transform(transformationMatrix: TransformationMatrix): MText =
        copy(
            size = size * transformationMatrix.scale,
            //justify = TODO fazer uma maneira de identificar o reflect
            //rotation = TODO
            position = position.transform(transformationMatrix)
        )
}

enum class AttachmentPoint(val value: Byte) {
    TOP_LEFT(value = 1), TOP_CENTER(value = 2), TOP_RIGHT(value = 3), MIDDLE_LEFT(value = 4), MIDDLE_CENTER(value = 5),
    MIDDLE_RIGHT(value = 6), BOTTOM_LEFT(value = 7), BOTTOM_CENTER(value = 8), BOTTOM_RIGHT(value = 9)
}