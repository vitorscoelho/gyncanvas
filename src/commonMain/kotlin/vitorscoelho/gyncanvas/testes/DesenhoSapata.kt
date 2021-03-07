package vitorscoelho.gyncanvas.testes

import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.LwPolyline
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.js.ExperimentalJsExport

@ExperimentalJsExport
interface DadosDesenhoSapata {
    val lxSapata: Double
    val lySapata: Double
    val alturaRodapeSapata: Double
    val alturaSapata: Double
    val lxPilar: Double
    val lyPilar: Double
}

@ExperimentalJsExport
fun desenharSapata(drawer: Drawer, dados: DadosDesenhoSapata) {
    val layerContornoSapata = Layer(name = "Contorno sapata", color = Color.INDEX_7)
    val layerContornoPilar = Layer(name = "Contorno pilar", color = Color.INDEX_3)

    val sapataCantoIE = Vector2D.ZERO
    val sapataCantoID = sapataCantoIE.createNewWithOffset(deltaX = dados.lxSapata)
    val sapataCantoSE = sapataCantoIE.createNewWithOffset(deltaY = dados.lySapata)
    val sapataCantorSD = sapataCantoSE.createNewWithOffset(deltaX = dados.lxSapata)
    val sapataCG = Vector2D.mid(vector1 = sapataCantoIE, vector2 = sapataCantorSD)
    val contornoPlantaSapata = LwPolyline.rectangle(
        layer = layerContornoSapata, startPoint = sapataCantoIE, deltaX = dados.lxSapata, deltaY = dados.lySapata
    )

    val contornoPlantaPilar = LwPolyline.rectangle(
        layer = layerContornoPilar,
        startPoint = sapataCG.createNewWithOffset(deltaX = -0.5 * dados.lxPilar, deltaY = -0.5 * dados.lyPilar),
        deltaX = dados.lxPilar, deltaY = dados.lyPilar
    )
}