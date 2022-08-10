import dev.darkokoa.pangu.spacingText
import kotlin.test.assertEquals

internal fun assertEqualsSpacingText(input: String, expected: String) {
    return assertEquals(expected, input.spacingText())
}