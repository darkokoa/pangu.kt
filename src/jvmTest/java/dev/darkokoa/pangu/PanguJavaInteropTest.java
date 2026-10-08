package dev.darkokoa.pangu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PanguJavaInteropTest {

    @Test
    void singletonApiIsCallableFromJava() {
        assertEquals("中文 abc", Pangu.INSTANCE.spacingText("中文abc"));
    }

    @Test
    void extensionFunctionApiIsCallableFromJava() {
        assertEquals("abc 中文", PanguKt.spacingText("abc中文"));
    }

    @Test
    void hasProperSpacingIsCallableFromJava() {
        assertTrue(Pangu.INSTANCE.hasProperSpacing("中文 abc"));
        assertFalse(Pangu.INSTANCE.hasProperSpacing("中文abc"));
        assertTrue(PanguKt.hasProperSpacing("abc 中文"));
        assertFalse(PanguKt.hasProperSpacing("abc中文"));
    }
}
