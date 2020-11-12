package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.entities.Entity
import vitorscoelho.gyncanvas.core.dxf.tables.Layer

enum class ShapeType {
    STROKED {
        override fun applyColor(drawer: Drawer, entity: Entity) {
            drawer.stroke = entity.color.effectiveColor(entity)
        }
    },
    FILLED {
        override fun applyColor(drawer: Drawer, entity: Entity) {
            drawer.fill = entity.color.effectiveColor(entity)
        }
    },
    STROKED_AND_FILLED {
        override fun applyColor(drawer: Drawer, entity: Entity) {
            drawer.stroke = entity.color.effectiveColor(entity)
            drawer.fill = entity.color.effectiveColor(entity)
        }
    },
    NONE {
        override fun applyColor(drawer: Drawer, entity: Entity) {}
    };

    abstract fun applyColor(drawer: Drawer, entity: Entity)
}