package vitorscoelho.gyncanvas.core.dxf.entities.path

import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

sealed class PathStep {
    abstract fun draw(drawer: Drawer)
    abstract fun transform(tranformationMatrix: TransformationMatrix): PathStep
}

class MoveTo(val point: Vector2D) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.moveTo(x = point.x, y = point.y)
    override fun transform(tranformationMatrix: TransformationMatrix): MoveTo =
        MoveTo(point = point.transform(tranformationMatrix))
}

class LineTo(val point: Vector2D) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.lineTo(x = point.x, y = point.y)
    override fun transform(tranformationMatrix: TransformationMatrix): LineTo =
        LineTo(point = point.transform(tranformationMatrix))
}

class ArcTo(val tangentPoint1: Vector2D, val tangentPoint2: Vector2D, val radius: Double) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.arcTo(
        xTangent1 = tangentPoint1.x,
        yTangent1 = tangentPoint1.y,
        xTangent2 = tangentPoint2.x,
        yTangent2 = tangentPoint2.y,
        radius = radius
    )

    override fun transform(tranformationMatrix: TransformationMatrix): ArcTo =
        ArcTo(
            tangentPoint1 = tangentPoint1.transform(tranformationMatrix),
            tangentPoint2 = tangentPoint2.transform(tranformationMatrix),
            radius = radius * tranformationMatrix.scale
        )
}