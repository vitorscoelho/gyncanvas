package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle

interface Dimension : CompositeEntity {
    val dimStyle: DimStyle
    val dimStyleOverrides: DimStyleOverrides
    val measurement: Double
    val text: String

    //LINES PROP
    //Propriedade: Dimensions lines
    val dimensionLinesColor: Color
        get() = dimStyleOverrides.dimensionLinesColor(dimStyle)
    val dimensionLinesSuppressDimLine1: Boolean
        get() = dimStyleOverrides.dimensionLinessuppressDimLine1(dimStyle)
    val dimensionLinesSuppressDimLine2: Boolean
        get() = dimStyleOverrides.dimensionLinessuppressDimLine2(dimStyle)

    //Propriedade: Extension lines
    val extensionLinesColor: Color
        get() = dimStyleOverrides.extensionLinesColor(dimStyle)
    val extensionLinessuppressExtLine1: Boolean
        get() = dimStyleOverrides.extensionLinessuppressExtLine1(dimStyle)
    val extensionLinessuppressExtLine2: Boolean
        get() = dimStyleOverrides.extensionLinessuppressExtLine2(dimStyle)
    val extensionLinesExtendBeyondDimLines: Double
        get() = dimStyleOverrides.extensionLinesExtendBeyondDimLines(dimStyle)
    val extensionLinesOffsetFromOrigin: Double
        get() = dimStyleOverrides.extensionLinesOffsetFromOrigin(dimStyle)

    //SYMBOLS AND ARROWS PROP
    //Propriedade: Arrowheads
    val firstArrowHead: Block
        get() = dimStyleOverrides.firstArrowHead(dimStyle)
    val secondArrowHead: Block
        get() = dimStyleOverrides.secondArrowHead(dimStyle)
    val leaderArrowHead: Block
        get() = dimStyleOverrides.leaderArrowHead(dimStyle)
    val arrowSize: Double
        get() = dimStyleOverrides.arrowSize(dimStyle)

    //TEXT PROP
    //Propriedade: TextAppearance
    val textStyle: TextStyle
        get() = dimStyleOverrides.textStyle(dimStyle)
    val textColor: Color
        get() = dimStyleOverrides.textColor(dimStyle)
    val textHeight: Double
        get() = dimStyleOverrides.textHeight(dimStyle)
    //Propriedade: TextPlacement
    val textOffsetFromDimLine: Double
        get() = dimStyleOverrides.textOffsetFromDimLine(dimStyle)

    //FIT PROP
    //Propriedade: ScaleForDimensionFeatures
    val overallScale: Double
        get() = dimStyleOverrides.overallScale(dimStyle)
}