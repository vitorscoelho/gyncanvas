package vitorscoelho

import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.gyncanvas.core.CanvasController
import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.EventManager

class JSCanvasController(val canvas: HTMLCanvasElement) : CanvasController {
    init {
        //Impedindo que abra o menu de contexto quando clicar com o botão direito do mouse no canvas
        canvas.addEventListener(type = "contextmenu", callback = { event -> event.preventDefault() })
        //Impedindo a rolagem da página quando usar o scroll do mouse sobre o canvas
        canvas.addEventListener(type = "wheel", callback = { ev -> ev.preventDefault() })
    }

    override val drawer: Drawer = JSDrawer(canvas = canvas)
    override val listeners: EventManager = JSEventManager(canvas = canvas)
}
