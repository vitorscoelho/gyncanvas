package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Drawable
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.TransformationMatrix

interface Entity : Drawable {
    val primitives: List<Primitive>
    val shapeType: ShapeType

    val layer: Layer
    val color: Color

//    fun transform(transformationMatrix: TransformationMatrix): Entity
}

internal fun applyLineWidth(drawer: Drawer) {
    drawer.lineWidht = 1.0 / drawer.camera.zoom
}