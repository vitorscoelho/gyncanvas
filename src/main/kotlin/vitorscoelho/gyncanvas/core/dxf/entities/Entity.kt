package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

interface Entity {
    val properties: EntityProperties
    val layer: Layer
        get() = properties.layer
    val color: Color
        get() = properties.color

    fun draw(drawer: Drawer)

    fun applyProperties(drawer: Drawer)

    fun transform(transformationMatrix: TransformationMatrix): Entity
}

internal fun applyLineWidth(drawer: Drawer) {
    drawer.lineWidht = 1.0 / drawer.transform.scale
}

internal fun applyColor(drawer: Drawer, layer: Layer, color: Color) {
    when (color) {
        Color.BY_LAYER -> {
            drawer.stroke = layer.color
            drawer.fill = layer.color
        }
        else -> {
            drawer.stroke = color
            drawer.fill = color
        }
    }
}

class EntityProperties(
    val layer: Layer,
    val color: Color = Color.BY_LAYER
)