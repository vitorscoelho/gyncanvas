package vitorscoelho

import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import vitorscoelho.gyncanvas.core.DrawingArea
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.event.CanvasMouseEventType

private val mapMouseButton by lazy {
    mapOf(
        0.toShort() to CanvasMouseButton.PRIMARY,
        1.toShort() to CanvasMouseButton.MIDDLE,
        2.toShort() to CanvasMouseButton.SECONDARY
    )
}

private fun mouseButton(jsMouseButton: Short) = mapMouseButton.getOrElse(
    jsMouseButton, { CanvasMouseButton.NONE }
)

private val mapMouseEvenType by lazy {
    mapOf(
        CanvasMouseEventType.MOUSE_PRESSED to "mousedown",
        CanvasMouseEventType.MOUSE_RELEASED to "mouseup",
        CanvasMouseEventType.MOUSE_CLICKED to "mousedown",//Tem um tratamento especial na função addListener, porque "click" só serve pra botão Primary e "contextmenu" para botão Secondary
        CanvasMouseEventType.MOUSE_ENTERED to "mouseenter",
        CanvasMouseEventType.MOUSE_EXITED to "mouseleave",
        CanvasMouseEventType.MOUSE_MOVED to "mousemove"
    )
}

private fun mouseEventType(canvasMouseEventType: CanvasMouseEventType) = mapMouseEvenType[canvasMouseEventType]!!

class ImplementacaoCanvasMouseEvent(private val mouseEventJS: MouseEvent) : CanvasMouseEvent {
    override val x: Double get() = mouseEventJS.x
    override val y: Double get() = mouseEventJS.y
    override val button: CanvasMouseButton get() = mouseButton(mouseEventJS.button)
}

class JSDrawingArea(val canvas: HTMLCanvasElement) : DrawingArea() {
    init {
        //Impedindo que abra o menu de contexto quando clicar com o botão direito do mouse no canvas
        canvas.addEventListener(type = "contextmenu", callback = { event -> event.preventDefault() })
        //Impedindo a rolagem da página quando usar o scroll do mouse sobre o canvas
        canvas.addEventListener(type = "wheel", callback = { ev -> ev.preventDefault() })
    }

    override val drawer = JSDrawer(canvas = canvas)

    private fun gerarCallbackParaClick(eventHandler: (event: CanvasMouseEvent) -> Unit): EventListener {
        return EventListener { eventMouseDown: Event ->
            //Usa a ideia encontrada em https://medium.com/@MelkorNemesis/handling-javascript-mouseup-event-outside-element-b0a34090bb56
            val botaoPressionado = (eventMouseDown as MouseEvent).button
            var callbackEventMouseUp: (Event) -> Unit = {}
            callbackEventMouseUp = { eventMouseUp: Event ->
                val botaoSolto = (eventMouseUp as MouseEvent).button
                if (botaoSolto == botaoPressionado) {
                    document.removeEventListener(
                        "mouseup",
                        callbackEventMouseUp
                    )//Usa o 'document' ao invés do 'canvas' para possibilitar a captura do botão solto fora do canvas
                    val mouseEstaNoCanvasQuandoSoltaOBotao: Boolean = let {
                        val elementoSobOMouse = document.elementFromPoint(
                            x = eventMouseUp.clientX.toDouble(),
                            y = eventMouseUp.clientY.toDouble()
                        )
                        elementoSobOMouse == canvas
                    }
                    if (mouseEstaNoCanvasQuandoSoltaOBotao) {
                        eventHandler(ImplementacaoCanvasMouseEvent(eventMouseUp))
                    }
                }
            }
            document.addEventListener("mouseup", callbackEventMouseUp)
        }
    }

    override fun addEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        val jsEventType = mouseEventType(eventType)
        if (listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jsCallback: EventListener = if (eventType == CanvasMouseEventType.MOUSE_CLICKED) {
            gerarCallbackParaClick(eventHandler)
        } else {
            EventListener { event -> eventHandler(ImplementacaoCanvasMouseEvent(event as MouseEvent)) }
        }
        listenersManager.add(eventType, eventHandler, jsCallback)
        canvas.addEventListener(jsEventType, jsCallback)
    }

    override fun removeEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        val jsEventType = mouseEventType(eventType)
        if (!listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jsCallback = listenersManager.getJSEventHandler(eventType, eventHandler)
        listenersManager.remove(eventType, eventHandler)
        canvas.removeEventListener(jsEventType, jsCallback)
    }

    private val listenersManager = object {
        private val listenersMap =
            hashMapOf<Pair<CanvasMouseEventType, (event: CanvasMouseEvent) -> Unit>, EventListener>()

        fun listenerJaIncluso(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit
        ): Boolean {
            return listenersMap.containsKey(key = Pair(eventType, eventHandler))
        }

        fun add(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit,
            jsEventHandler: EventListener
        ) {
            listenersMap[Pair(eventType, eventHandler)] = jsEventHandler
        }

        fun remove(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit
        ) {
            listenersMap.remove(Pair(eventType, eventHandler))
        }

        fun getJSEventHandler(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit
        ): EventListener? {
            return listenersMap[Pair(eventType, eventHandler)]
        }
    }
}