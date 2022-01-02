package vitorscoellho.gyncanvas.utils

import vitorscoelho.gyncanvas.utils.NumberFormatter
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFormatterTest {
    @Test
    fun formatar1() {
        val f = NumberFormatter(
            suppressLeadingZeros = true, suppressTrailingZeros = true, precision = 2, roundoff = 0.25,
            decimalSeparator = '.', prefix = "valor= ", suffix = "cm"
        )
        assertEquals("valor= 5.5cm", f.format(5.5))
        assertEquals("valor= 5.5cm", f.format(5.54))
        assertEquals("valor= 5.5cm", f.format(5.6))
        assertEquals("valor= 5.75cm", f.format(5.65))
        assertEquals("valor= 5.75cm", f.format(5.71))
        assertEquals("valor= 5.75cm", f.format(5.75))
        assertEquals("valor= 5.75cm", f.format(5.77))
        assertEquals("valor= -5.5cm", f.format(-5.5))
        assertEquals("valor= -5.5cm", f.format(-5.54))
        assertEquals("valor= -5.5cm", f.format(-5.6))
        assertEquals("valor= -5.75cm", f.format(-5.65))
        assertEquals("valor= -5.75cm", f.format(-5.71))
        assertEquals("valor= -5.75cm", f.format(-5.75))
        assertEquals("valor= -5.75cm", f.format(-5.77))
        assertEquals("valor= 57506.5cm", f.format(57506.5987))
        assertEquals("valor= 57506.5cm", f.format(57506.5487))
        assertEquals("valor= .5cm", f.format(0.5))
        assertEquals("valor= -.5cm", f.format(-0.5))
        assertEquals("valor= 0cm", f.format(0.0))
    }

    @Test
    fun formatar2() {
        val f = NumberFormatter(
            suppressLeadingZeros = false, suppressTrailingZeros = false, precision = 3, roundoff = 0.1,
            decimalSeparator = '.', prefix = "valor= ", suffix = "cm"
        )
        assertEquals("valor= 5.500cm", f.format(5.5))
        assertEquals("valor= 5.500cm", f.format(5.54))
        assertEquals("valor= 5.600cm", f.format(5.6))
        assertEquals("valor= 5.700cm", f.format(5.65))
        assertEquals("valor= 5.700cm", f.format(5.71))
        assertEquals("valor= 5.800cm", f.format(5.75))
        assertEquals("valor= 5.800cm", f.format(5.77))
        assertEquals("valor= -5.500cm", f.format(-5.5))
        assertEquals("valor= -5.500cm", f.format(-5.54))
        assertEquals("valor= -5.600cm", f.format(-5.6))
        assertEquals("valor= -5.700cm", f.format(-5.65))
        assertEquals("valor= -5.700cm", f.format(-5.71))
        assertEquals("valor= -5.800cm", f.format(-5.75))
        assertEquals("valor= -5.800cm", f.format(-5.77))
        assertEquals("valor= 57506.600cm", f.format(57506.5987))
        assertEquals("valor= 57506.500cm", f.format(57506.5487))
        assertEquals("valor= 0.500cm", f.format(0.5))
        assertEquals("valor= -0.500cm", f.format(-0.5))
        assertEquals("valor= 0.000cm", f.format(0.0))
    }

    @Test
    fun formatar3() {
        val f = NumberFormatter(
            suppressLeadingZeros = true, suppressTrailingZeros = true, precision = 2, roundoff = 0.0,
            decimalSeparator = ',', prefix = "valor= ", suffix = "cm"
        )
        assertEquals("valor= 5,5cm", f.format(5.5))
        assertEquals("valor= 5,54cm", f.format(5.54))
        assertEquals("valor= 5,6cm", f.format(5.6))
        assertEquals("valor= 5,65cm", f.format(5.65))
        assertEquals("valor= 5,71cm", f.format(5.71))
        assertEquals("valor= 5,75cm", f.format(5.75))
        assertEquals("valor= 5,77cm", f.format(5.77))
        assertEquals("valor= -5,5cm", f.format(-5.5))
        assertEquals("valor= -5,54cm", f.format(-5.54))
        assertEquals("valor= -5,6cm", f.format(-5.6))
        assertEquals("valor= -5,65cm", f.format(-5.65))
        assertEquals("valor= -5,71cm", f.format(-5.71))
        assertEquals("valor= -5,75cm", f.format(-5.75))
        assertEquals("valor= -5,77cm", f.format(-5.77))
        assertEquals("valor= 57506,6cm", f.format(57506.5987))
        assertEquals("valor= 57506,55cm", f.format(57506.5487))
        assertEquals("valor= ,5cm", f.format(0.5))
        assertEquals("valor= -,5cm", f.format(-0.5))
        assertEquals("valor= 0cm", f.format(0.0))
    }

    @Test
    fun formatar4() {
        val f = NumberFormatter(
            suppressLeadingZeros = true, suppressTrailingZeros = true, precision = 0, roundoff = 0.0,
            decimalSeparator = ',', prefix = "valor= ", suffix = "cm"
        )
        assertEquals("valor= 6cm", f.format(5.5))
        assertEquals("valor= 6cm", f.format(5.54))
        assertEquals("valor= 6cm", f.format(5.6))
        assertEquals("valor= 6cm", f.format(5.65))
        assertEquals("valor= 6cm", f.format(5.71))
        assertEquals("valor= 6cm", f.format(5.75))
        assertEquals("valor= 6cm", f.format(5.77))
        assertEquals("valor= -6cm", f.format(-5.5))
        assertEquals("valor= -6cm", f.format(-5.54))
        assertEquals("valor= -6cm", f.format(-5.6))
        assertEquals("valor= -6cm", f.format(-5.65))
        assertEquals("valor= -6cm", f.format(-5.71))
        assertEquals("valor= -6cm", f.format(-5.75))
        assertEquals("valor= -6cm", f.format(-5.77))
        assertEquals("valor= 57507cm", f.format(57506.5987))
        assertEquals("valor= 57507cm", f.format(57506.5487))
        assertEquals("valor= 1cm", f.format(0.5))
        assertEquals("valor= -1cm", f.format(-0.5))
        assertEquals("valor= 0cm", f.format(0.0))
    }
}