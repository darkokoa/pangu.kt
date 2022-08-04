import dev.ryz3n.pangu.toSpacingText
import kotlin.test.assertEquals

internal fun assertEqualsSpacingText(input: String, expected: String) {
    return assertEquals(expected, input.toSpacingText())
}