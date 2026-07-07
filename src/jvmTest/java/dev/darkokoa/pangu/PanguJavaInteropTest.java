package dev.darkokoa.pangu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PanguJavaInteropTest {

    @Test
    void singletonApiIsCallableFromJava() {
        assertEquals("中文 abc", Pangu.INSTANCE.spacingText("中文abc"));
    }

    @Test
    void extensionFunctionApiIsCallableFromJava() {
        assertEquals("abc 中文", PanguKt.spacingText("abc中文"));
    }
}
