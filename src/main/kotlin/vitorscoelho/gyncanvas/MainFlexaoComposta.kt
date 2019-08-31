package vitorscoelho.gyncanvas

import kotlin.math.pow

fun flexaoCompostaReta(
    b: Double,
    h: Double,
    d: Double,
    dLinha: Double,
    fck: Double,
    fyk: Double,
    moduloElasticidadeAco:Double,
    gamaC: Double,
    gamaS: Double,
    ksiLim:Double,//Usar require?
    nd: Double,
    md: Double
) {
    //Validação dos dados
    require(b > 0)
    require(h > 0)
    require(d > 0)
    require(dLinha > 0)
    require(fck > 0 && fck < 9)
    require(fyk > 0)
    require(moduloElasticidadeAco>0)
    require(gamaC > 0)
    require(gamaS > 0)
    require(md >= 0)

    //Características do concreto
    val ec2: Double
    val ecu: Double
    val lambda: Double
    val alphaC: Double
    if (fck <= 5) {
        ec2 = 2.0 / 1000.0
        ecu = 3.5 / 1000.0
        lambda = 0.8
        alphaC = 0.85
    } else {
        ec2 = (2.0 + 0.085 * (fck * 10.0 - 50.0).pow(0.53)) / 1000.0
        ecu = (2.6 + 35.0 * ((90 - fck * 10) / 100.0).pow(4)) / 1000.0
        lambda = 0.8 - (fck * 10.0 - 50.0) / 400.0
        alphaC = 0.85 * (1.0 - (fck * 10.0 - 50.0) / 200.0)
    }
    val sigmaC = alphaC * fck / gamaC
    val fyd = fyk / gamaS

    //Dados do carregamento
    val e0 = md / nd
    val e1 = (d - dLinha) / 2.0 + e0
    val e2 = (d - dLinha) / 2.0 - e0

}