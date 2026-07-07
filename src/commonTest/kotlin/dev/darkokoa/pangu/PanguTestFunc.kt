package dev.darkokoa.pangu

import kotlin.test.assertEquals

internal fun assertEqualsSpacingText(input: String, expected: String) {
    return assertEquals(expected, input.spacingText())
}
