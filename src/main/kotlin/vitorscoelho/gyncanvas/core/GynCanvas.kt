package vitorscoelho.gyncanvas.core

import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import tornadofx.fitToParentSize
import vitorscoelho.gyncanvas.core.primitivas.Primitiva
import vitorscoelho.gyncanvas.core.primitivas.propriedades.DrawAttributes
import vitorscoelho.gyncanvas.core.primitivas.propriedades.StrokeAttributes

class GynCanvas {
    //Canvas JavaFX
    private val canvasJFX = Canvas()
    private val gc = canvasJFX.graphicsContext2D

    //Primitivas
    private val propriedadesDePrimitivas = hashMapOf<DrawAttributes, MutableList<Primitiva>>()

    private val corFundo = Color.BLACK
    val transformacoes = Transformacoes()
    val node: Node

    init {
        node = Pane(canvasJFX)
        this.node.parentProperty().addListener { observable, oldValue, newValue ->
            this.node.fitToParentSize()
        }
        canvasJFX.widthProperty().bind(node.widthProperty())
        canvasJFX.heightProperty().bind(node.heightProperty())
    }

    fun addPrimitiva(primitiva: Primitiva, propriedade: DrawAttributes) {
        if (!propriedadesDePrimitivas.containsKey(propriedade)) {
            propriedadesDePrimitivas[propriedade] = mutableListOf()
        }
        propriedadesDePrimitivas[propriedade]!! += primitiva
    }

    fun desenhar() {
        resetarAffine()
        gc.fill = corFundo
        gc.fillRect(0.0, 0.0, this.canvasJFX.width, this.canvasJFX.height)
        gc.transform = transformacoes.toAffine()
        propriedadesDePrimitivas.forEach { propriedade, listaPrimitivas ->
            propriedade.aplicar(gc, transformacoes)
            gc.lineWidth = gc.lineWidth / transformacoes.escala //Para que a linha permaneça sempre na mesma espessura, independente do zoom
            listaPrimitivas.forEach { it.desenhar(gc, transformacoes) }
        }
    }

    private val affineIdentidade = Affine()
    private fun resetarAffine() {
        gc.transform = affineIdentidade
    }
}