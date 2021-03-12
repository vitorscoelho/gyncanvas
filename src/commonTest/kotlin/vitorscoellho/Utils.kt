package vitorscoellho

import kotlin.test.assertTrue

fun doubleAssertEquals(expected: Double, actual: Double, delta: Double) {
    val message = "expected:<$expected> but was:<$actual>"
    assertTrue(message) {
        (actual >= expected - delta) && (actual <= expected + delta)
    }
}