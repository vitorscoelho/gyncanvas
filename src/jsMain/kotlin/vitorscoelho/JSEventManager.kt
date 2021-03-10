package vitorscoelho

import kotlinx.browser.document
import org.w3c.dom.DOMRect
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import vitorscoelho.gyncanvas.core.EventManager
import vitorscoelho.gyncanvas.core.event.*

class JSEventManager(val canvas: HTMLCanvasElement) : EventManager() {
    private val listenersMap = hashMapOf<EventWrapper, EventListener>()

    init {
        //Impedindo que abra o menu de contexto quando clicar com o botão direito do mouse no canvas
        canvas.addEventListener(type = "contextmenu", callback = { event -> event.preventDefault() })
        //Impedindo a rolagem da página quando usar o scroll do mouse sobre o canvas
        canvas.addEventListener(type = "wheel", callback = { ev -> ev.preventDefault() })
    }

    override fun add(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit) {
        val wrapper = EventWrapper(canvasEventType = eventType, handler = eventHandler)
        if (listenersMap.contains(wrapper)) return
        val jsCallback: EventListener = wrapper.createJsCallback(canvas)
        listenersMap[wrapper] = jsCallback
        canvas.addEventListener(wrapper.jsEventType, jsCallback)
    }

    override fun remove(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit) {
        val wrapper = EventWrapper(canvasEventType = eventType, handler = eventHandler)
        if (!listenersMap.contains(wrapper)) return
        val jsCallback: EventListener = listenersMap[wrapper]!!
        listenersMap.remove(wrapper)
        canvas.removeEventListener(wrapper.jsEventType, jsCallback)
    }
}

private data class EventWrapper(val canvasEventType: CanvasEventType, val handler: (event: CanvasEvent) -> Unit) {
    val jsEventType: String
        get() = when (canvasEventType) {
            CanvasMouseEventType.MOUSE_PRESSED -> "mousedown"
            CanvasMouseEventType.MOUSE_RELEASED -> "mouseup"
            CanvasMouseEventType.MOUSE_CLICKED -> "mousedown"//Tem um tratamento especial na função addListener, porque "click" só serve pra botão Primary e "contextmenu" para botão Secondary
            CanvasMouseEventType.MOUSE_ENTERED -> "mouseenter"
            CanvasMouseEventType.MOUSE_EXITED -> "mouseleave"
            CanvasMouseEventType.MOUSE_MOVED -> "mousemove"
            CanvasScrollEventType.SCROLL -> "wheel"
            else -> throw IllegalArgumentException("|eventType| not supported")
        }

    fun createJsCallback(canvas: HTMLCanvasElement): EventListener = when (canvasEventType) {
        CanvasMouseEventType.MOUSE_CLICKED -> {
            gerarCallbackParaClick(canvas, handler)
        }
        is CanvasMouseEventType -> {
            EventListener { event ->
                handler(ImplementacaoCanvasMouseEvent(event as MouseEvent, canvas.getBoundingClientRect()))
            }
        }
        is CanvasScrollEventType -> {
            EventListener { event ->
                handler(ImplementacaoCanvasScrollEvent(event as WheelEvent, canvas.getBoundingClientRect()))
            }
        }
        else -> {
            throw IllegalArgumentException("|eventType| not supported")
        }
    }
}

private fun MouseEvent.realX(boundingRect: DOMRect): Float = clientX - boundingRect.left.toFloat()
private fun MouseEvent.realY(boundingRect: DOMRect): Float = clientY - boundingRect.top.toFloat()

private class ImplementacaoCanvasMouseEvent(
    private val mouseEventJS: MouseEvent, private val boundingRect: DOMRect
) : CanvasMouseEvent {
    override val x: Float get() = mouseEventJS.realX(boundingRect)
    override val y: Float get() = mouseEventJS.realY(boundingRect)
    override val button: CanvasMouseButton
        get() = mapMouseButton.getOrElse(mouseEventJS.button, { CanvasMouseButton.NONE })
}

private class ImplementacaoCanvasScrollEvent(
    private val scrollEventJS: WheelEvent, private val boundingRect: DOMRect
) : CanvasScrollEvent {
    override val x: Float get() = scrollEventJS.realX(boundingRect)
    override val y: Float get() = scrollEventJS.realY(boundingRect)
    override val deltaY: Double get() = -scrollEventJS.deltaY
}

private val mapMouseButton by lazy {
    mapOf(
        0.toShort() to CanvasMouseButton.PRIMARY,
        1.toShort() to CanvasMouseButton.MIDDLE,
        2.toShort() to CanvasMouseButton.SECONDARY
    )
}

private fun gerarCallbackParaClick(
    canvas: HTMLCanvasElement,
    eventHandler: (event: CanvasMouseEvent) -> Unit
): EventListener {
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
                val mouseEstaNoCanvasQuandoSoltaOBotao: Boolean = run {
                    val boundingRect = canvas.getBoundingClientRect()
                    val elementoSobOMouse = document.elementFromPoint(
                        x = eventMouseUp.realX(boundingRect).toDouble(),
                        y = eventMouseUp.realY(boundingRect).toDouble()
                    )
                    elementoSobOMouse == canvas
                }
                if (mouseEstaNoCanvasQuandoSoltaOBotao) {
                    eventHandler(ImplementacaoCanvasMouseEvent(eventMouseUp, canvas.getBoundingClientRect()))
                }
            }
        }
        document.addEventListener("mouseup", callbackEventMouseUp)
    }
}