package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyleProperties
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle

class DimStyleOverrides(
    override val dimensionLinesColor: Color?,
    override val dimensionLinesSupressDimLine1: Boolean?,
    override val dimensionLinesSupressDimLine2: Boolean?,
    override val extensionLinesColor: Color?,
    override val extensionLinesSupressExtLine1: Boolean?,
    override val extensionLinesSupressExtLine2: Boolean?,
    override val extensionLinesExtendBeyondDimLines: Double?,
    override val extensionLinesOffsetFromOrigin: Double?,
    override val firstArrowHead: Block?,
    override val secondArrowHead: Block?,
    override val leaderArrowHead: Block?,
    override val arrowSize: Double?,
    override val textStyle: TextStyle?,
    override val textColor: Color?,
    override val textHeight: Double?,
    override val textOffsetFromDimLine: Double?,
    override val overallScale: Double?,
    override val linearDimensionPrecision: Int?,
    override val decimalSeparator: Char?,
    override val unitRound: Double?,
    override val prefix: String?,
    override val suffix: String?,
    override val scaleFactor: Double?,
    override val linearDimensionSuppressLeadingZeros: Boolean?,
    override val linearDimensionSuppressTrailingZeros: Boolean?,
    override val angularDimensionPrecision: Int?
) : DimStyleProperties {
    fun dimensionLinesColor(base: DimStyle): Color = dimensionLinesColor ?: base.dimensionLinesColor
    fun dimensionLinesSupressDimLine1(base: DimStyle): Boolean =
        dimensionLinesSupressDimLine1 ?: base.dimensionLinesSupressDimLine1

    fun dimensionLinesSupressDimLine2(base: DimStyle): Boolean =
        dimensionLinesSupressDimLine2 ?: base.dimensionLinesSupressDimLine2

    fun extensionLinesColor(base: DimStyle): Color = extensionLinesColor ?: base.extensionLinesColor
    fun extensionLinesSupressExtLine1(base: DimStyle): Boolean =
        extensionLinesSupressExtLine1 ?: base.extensionLinesSupressExtLine1

    fun extensionLinesSupressExtLine2(base: DimStyle): Boolean =
        extensionLinesSupressExtLine2 ?: base.extensionLinesSupressExtLine2

    fun extensionLinesExtendBeyondDimLines(base: DimStyle): Double =
        extensionLinesExtendBeyondDimLines ?: base.extensionLinesExtendBeyondDimLines

    fun extensionLinesOffsetFromOrigin(base: DimStyle): Double =
        extensionLinesOffsetFromOrigin ?: base.extensionLinesOffsetFromOrigin

    fun firstArrowHead(base: DimStyle): Block = firstArrowHead ?: base.firstArrowHead
    fun secondArrowHead(base: DimStyle): Block = secondArrowHead ?: base.secondArrowHead
    fun leaderArrowHead(base: DimStyle): Block = leaderArrowHead ?: base.leaderArrowHead
    fun arrowSize(base: DimStyle): Double = arrowSize ?: base.arrowSize
    fun textStyle(base: DimStyle): TextStyle = textStyle ?: base.textStyle
    fun textColor(base: DimStyle): Color = textColor ?: base.textColor
    fun textHeight(base: DimStyle): Double = textHeight ?: base.textHeight
    fun textOffsetFromDimLine(base: DimStyle): Double = textOffsetFromDimLine ?: base.textOffsetFromDimLine
    fun overallScale(base: DimStyle): Double = overallScale ?: base.overallScale
    fun linearDimensionPrecision(base: DimStyle): Int = linearDimensionPrecision ?: base.linearDimensionPrecision
    fun decimalSeparator(base: DimStyle): Char = decimalSeparator ?: base.decimalSeparator
    fun unitRound(base: DimStyle): Double = unitRound ?: base.unitRound
    fun prefix(base: DimStyle): String = prefix ?: base.prefix
    fun suffix(base: DimStyle): String = suffix ?: base.suffix
    fun scaleFactor(base: DimStyle): Double = scaleFactor ?: base.scaleFactor
    fun linearDimensionSuppressLeadingZeros(base: DimStyle): Boolean =
        linearDimensionSuppressLeadingZeros ?: base.linearDimensionSuppressLeadingZeros

    fun linearDimensionSuppressTrailingZeros(base: DimStyle): Boolean =
        linearDimensionSuppressTrailingZeros ?: base.linearDimensionSuppressTrailingZeros

    fun angularDimensionPrecision(base: DimStyle): Int = angularDimensionPrecision ?: base.angularDimensionPrecision

    companion object {
        val NONE: DimStyleOverrides by lazy {
            DimStyleOverrides(
                dimensionLinesColor = null,
                angularDimensionPrecision = null,
                arrowSize = null,
                decimalSeparator = null,
                dimensionLinesSupressDimLine1 = null,
                dimensionLinesSupressDimLine2 = null,
                extensionLinesColor = null,
                extensionLinesExtendBeyondDimLines = null,
                extensionLinesOffsetFromOrigin = null,
                extensionLinesSupressExtLine1 = null,
                extensionLinesSupressExtLine2 = null,
                firstArrowHead = null,
                leaderArrowHead = null,
                linearDimensionPrecision = null,
                linearDimensionSuppressLeadingZeros = null,
                linearDimensionSuppressTrailingZeros = null,
                textOffsetFromDimLine = null,
                overallScale = null,
                prefix = null,
                scaleFactor = null,
                secondArrowHead = null,
                suffix = null,
                textColor = null,
                textHeight = null,
                textStyle = null,
                unitRound = null
            )
        }
    }
}