package vitorscoelho.gyncanvas

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import vitorscoelho.gyncanvas.json.FuncaoJsonDrawing
import vitorscoelho.gyncanvas.json.JsonDrawing
import java.io.*
import java.net.Socket

fun main() {
//    ativarCliente(porta = 12345)
    /*
    O que vai acontecer:
    O programa em C# vai criar um servidor e vai aguardar a conexão com um cliente.
    Depois, o programa C# vai abrir este programa compilado em um jar.
    Já no método main, a função abaixo vai ser chamada e vai conectar o cliente Java ao servidor .NET.
    Conexão feita, o programa C# vai enviar um json da função que deseja invocar.
    O cliente vai receber o json, criar o objeto da função, invocar a função, gerar o json para o desenho e enviá-lo ao servidor.
    O servidor vai receber o json de desenho, criar o objeto JsonDrawing e, a partir dele, criar o desenho no AutoCad.
     */
    invocarFuncaoJsonDrawing(porta = 12345)
}

fun invocarFuncaoJsonDrawing(porta: Int) {
    val socketCliente = criarEConectarClienteAoServidor(porta = porta)
    val jsonVindoDoServidor = socketCliente.receberTextoDoServidor()
    val jacksonMapper = jacksonObjectMapper()
    val funcaoJsonDrawing: FuncaoJsonDrawing = jacksonMapper.readValue(jsonVindoDoServidor, FuncaoJsonDrawing::class.java)
    val jsonDrawing: JsonDrawing = funcaoJsonDrawing.json()
    socketCliente.enviarTextoAoServidor(texto = jacksonMapper.writeValueAsString(jsonDrawing))
    socketCliente.close()
}

fun ativarCliente(porta: Int) {
    val socketCliente = criarEConectarClienteAoServidor(porta = porta)
    val textoEnviadoPeloServidor = socketCliente.receberTextoDoServidor()
    println(textoEnviadoPeloServidor)
    socketCliente.enviarTextoAoServidor("Oi servidor. Sou seu cliente Java/Kotlin.")
    socketCliente.close()
}

private fun criarEConectarClienteAoServidor(porta: Int): Socket {
    val ipMaquinaLocal = "127.0.0.1"
    return Socket(ipMaquinaLocal, porta)
}

private fun Socket.receberTextoDoServidor(): String {
    val reader = BufferedReader(InputStreamReader(this.getInputStream()))
    val retorno = reader.readLine()
//    reader.close()
    return retorno
}

private fun Socket.enviarTextoAoServidor(texto: String) {
    val printer = BufferedWriter(OutputStreamWriter(this.getOutputStream()))
    printer.write(texto)
    printer.flush()
//    printer.close()
}

//fun chamarFuncao(texto: String): String {
//    /*
//    Procedimento
//    1) O programa em C# abre o programa em Java
//    2) O servidor Java é ativado
//    3) A conexão é feita e o C# manda uma string para o Java
//    4) Esta string representa uma função e seus parâmetros (usar regex para separar os termos)
//    5) O nome da função, que estará separado após o uso de regex, será utilizado para ser buscado como chave em um map
//       que terá, como chave, uma string que é o nome da função e, como valor, a função que será invocada
//     */
//    /*
//    Outro procedimento possível
//    1) O programa C# é o servidor. Quando o método é invocado no AutoCad, abre uma porta no servidor e abre o programa Java
//    2) O programa Java cria um cliente e se conecta ao servidor.
//    3) O servidor envia um Json para o cliente. Este Json é uma classe do tipo Funcao que devolve um Json para o servidor.
//     */
//    val funcao = parseFuncao(texto)
//
//    return mapFuncoes[funcao.nome]!!.invoke(funcao.args)
//}

//private fun parseFuncao(texto: String): Funcao {
//
//}
//
//private class Funcao(val nome: String, val args: Array<String>)
//
//private val mapFuncoes = mapOf<String, (Array<String>) -> String>(
//    "funcao1" to { args -> funcao1(args) }
//)
//
//private fun funcao1(args: Array<String>): String {
//}
//
////fun chamarFuncao(nomeFuncao:String,vararg ):String