package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.decorator.PairUnwrap;
import tokenizer.decorator.PairValidator;
import tokenizer.iface.IStringParser;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PairMatchTest {
    @Test
    void givenMatchedSymbols_getHitMap() {
        IStringParser pairMatch = new PairMatch().setDelimiter("[{");
        String text;
        int[] actual, expected;

        text = "[{key1},{key2}]";
        expected = new int[]{14, 6, -1, -1, -1, -1, 1, -1, 13, -1, -1, -1, -1, 8, 0};
        actual = pairMatch.setText(text).parse().numericToArray();
        System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void givenSingleQuotes_getHitMap() {
        IStringParser pairMatch = new PairMatch().setDelimiter("[{'");
        String text;
        int[] actual, expected;

        text = "'1234'";
        expected = new int[]{5, -1, -1, -1, -1, 0};
        actual = pairMatch.setText(text).parse().numericToArray();
        System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }
    @Test
    void givenSkipArea_getHitMap() {
        IStringParser pairMatch = new PairMatch().setDelimiter("[{").setSkipSymbols("\"");
        String text;
        int[] actual, expected;

        text = "[{key1\"},{\"key2}]";
        expected = new int[]{16, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0};
        actual = pairMatch.setText(text).parse().numericToArray();
        System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }
//    @Test
//    void givenLimit_getHitMap() {
//        IStringParser pairMatch = new PairMatch().setDelimiter("[{").setLimit(2);
//        String text;
//        int[] actual, expected;
//
//        text = "[{key1},{key2}]";
//        expected = new int[]{16, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0};
//        actual = pairMatch.setText(text).parse().numericToArray();
//        System.out.println(Arrays.toString(actual));
//        assertArrayEquals(expected, actual);
//    }
    @Test
    void givenOrphanSymbol_setMinus2() {
        IStringParser pairMatch = new PairMatch().setDelimiter("[{").setSkipSymbols("\"");
        String text;
        int[] actual, expected;

        text = "}{text}{";
        expected = new int[]{-2, 6, -1, -1, -1, -1, 1, -2};
        actual = pairMatch.setText(text).parse().numericToArray();
        System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void givenBadString_findErr() {
        IStringParser pairValidator = new PairValidator().setDelimiter("[{'").setSkipSymbols("\"");
        String text;
        boolean actual;

        text = "}{text}{";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertTrue(actual);

        text = "{hey{wow}";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertTrue(actual);

        text = "abcdefghij";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertFalse(actual);

        text = "[{key1\"},{\"key2}]";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertFalse(actual);

        text = "'''";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertTrue(actual);

        text = "''''''";
        actual = pairValidator.setText(text).parse().isError();
        System.out.println(actual);
        assertFalse(actual);
    }
    @Test
    void givenGoodString_CountSymbols() {
        IStringParser pairValidator = new PairValidator().setDelimiter("[{'");
        String text;
        int actual, expected;

        text = "[{key1},{'key2'}]";
        expected = 8;
        actual = pairValidator.setText(text).parse().numeric();
        System.out.println(actual);
        assertEquals(expected, actual);
    }
    @Test
    void givenNestedSymbols_shouldUnwrapCorrectly() {
        IStringParser unwrap = new PairUnwrap().setDelimiter("[{(");
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
        IStringParser unwrap = new PairUnwrap().setDelimiter("[{('\"");
        String text, expected, actual;

        text = "'\"[{([key1:val1,key2:val2])}]\"'";
        expected = "key1:val1,key2:val2";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
    }
    @Test
    void givenLimit_shouldUnwrapCorrectly() {
        IStringParser unwrap = new PairUnwrap().setDelimiter("[{(\"'").setLimit(2);
        String text, expected, actual;

        text = "'[{([key1:val1,key2:val2])}]'";
        expected = "([key1:val1,key2:val2])";
        actual = unwrap.setText(text).parse().getText();
        assertEquals(expected, actual);
    }
}
