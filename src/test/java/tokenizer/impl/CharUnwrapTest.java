package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.simpl.CharUnwrap;

import static org.junit.jupiter.api.Assertions.*;

public class CharUnwrapTest {
    @Test
    void givenNestedSymbols_shouldUnwrapCorrectly() {
        CharUnwrap unwrap = new CharUnwrap().setWrapSymbols("[{(");
        String text, expected, actual;

        text = "[{key1:val1},{key2:val2}]";
        expected = "{key1:val1},{key2:val2}";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);

        text = "[{key1:val1,key2:val2}]";
        expected = "key1:val1,key2:val2";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);

        text = "[{key1:val1}],[{key2:val2}]";
        expected = "[{key1:val1}],[{key2:val2}]";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);

        text = "[{key1:val1,key2:[spoof],key3:val3}]";
        expected = "key1:val1,key2:[spoof],key3:val3";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
    }
    @Test
    void givenMatchedSymbolsAnyDepth_shouldUnwrapCorrectly() {
        CharUnwrap unwrap = new CharUnwrap().setWrapSymbols("[{('\"");
        String text, expected, actual;

        text = "'\"[{([key1:val1,key2:val2])}]\"'";
        expected = "key1:val1,key2:val2";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
    }
    @Test
    void givenLimit_shouldUnwrapCorrectly() {
        CharUnwrap unwrap = new CharUnwrap().setWrapSymbols("[{(\"'").setLimit(2);
        String text, expected, actual;

        text = "'[{([key1:val1,key2:val2])}]'";
        expected = "([key1:val1,key2:val2])";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
    }
    @Test
    void givenUnmatched_shouldReportError() {// not very good validation; use custom validator
        CharUnwrap unwrap = new CharUnwrap().setWrapSymbols("[{(\"'").setLimit(2);
        String text, expected, actual;

        text = "{good},{string}";
        expected = "{good},{string}";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
        assertFalse(unwrap.isError());

        text = "'[{{badString1))]'";
        expected = "'[{{badString1))]'";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
        assertTrue(unwrap.isError());

        // won't catch bad string from closing symbol
        text = "}{badString2}";
        expected = "}{badString2}";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
        assertFalse(unwrap.isError());
    }
}
