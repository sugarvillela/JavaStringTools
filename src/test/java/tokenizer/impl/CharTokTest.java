package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.composite.CharTok;
import tokenizer.iface.IStringParser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharTokTest {

    @Test
    void givenSkipArea_shouldNotTokenizeInSkipArea() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenStartPos_shouldNotTokenizeBeforeStartPos() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setStartPos(10).setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence__with|(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenLimit_shouldNotTokenizeAfterLimit() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setLimit(2).setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with_(too_many_'delims')__and_quotes__", unTok);
    }
    @Test
    void givenNestedSkipAreas_shouldHandleOuterSymbols() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenStartPos_shouldHandleOuterSymbols() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setStartPos(17).setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence__with_(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenSequentialSkipAreas_shouldHandleBothSetsOfSymbols() {
        String text = "Sentence__with_(many_delims)__and_'stuff_in_quotes'";
        String[] tok = new CharTok().setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|(many_delims)|and|'stuff_in_quotes'", unTok);
    }
    @Test
    void givenStartPos_shouldHandleBothSetsOfSymbols() {
        String text = "Sentence__with_(many_delims)__and_'stuff_in_quotes'";
        String[] tok = new CharTok().setStartPos(27).setDelimiter(" _").setSkipSymbols("('").setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence__with_(many_delims)|and|'stuff_in_quotes'", unTok);
    }
    @Test
    void givenTokenizeDelimiter_shouldKeepAllDelimiter() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setDelimiter(" _").setSkipSymbols("('").setTokenizeDelimiter(true).setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|_|_|with|_|(too_many_'delims')|_|_|and|_|quotes|_|_", unTok);
    }
    @Test
    void givenTokenizeDelimiterOnce_shouldKeepDelimiterOnce() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTok().setDelimiter(" _").setSkipSymbols("('").setTokenizeDelimiterOnce(true).setText(text).parse().toArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|_|with|_|(too_many_'delims')|_|and|_|quotes|_", unTok);
    }
    @Test
    void givenSublang_splitOnAssignedDelim() {
        String AND = "&", OR = "|";
        String text = "zero|one&two|three";
        IStringParser tokenizer = new CharTok().setSkipSymbols("('");

        String[] splitAnd = tokenizer.setDelimiter(AND).setText(text).parse().toArray();
        String unSplitAnd = String.join("-", splitAnd);
        System.out.println(unSplitAnd);
        assertEquals("zero|one-two|three", unSplitAnd);

        String[] splitOr = tokenizer.setDelimiter(OR).setText(splitAnd[0]).parse().toArray();
        String unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("zero-one", unSplitOr);

        splitOr = tokenizer.setText(splitAnd[1]).parse().toArray();
        unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("two-three", unSplitOr);
    }
    @Test
    void givenSubLangKeepSkipsAndConnectedChar_leavesCharConnected() {
        String AND = "&", OR = "|";
        String text = "zero|!(one&two)|three";
        IStringParser tokenizer = new CharTok().setSkipSymbols("('");

        String[] splitOr = tokenizer.setDelimiter(OR).setText(text).parse().toArray();
        String unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("zero-!(one&two)-three", unSplitOr);

        splitOr[1] = splitOr[1].substring(2, splitOr[1].length() - 1);// remove parentheses
        assertEquals("one&two", splitOr[1]);

        String[] splitAnd = tokenizer.setDelimiter(AND).setText(splitOr[1]).parse().toArray();
        String unSplitAnd = String.join("-", splitAnd);
        System.out.println(unSplitAnd);
        assertEquals("one-two", unSplitAnd);
//
//        splitOr = tokenizer.parse(splitAnd[1]).getArray();
//        unSplitOr = String.join("-", splitOr);
//        System.out.println(unSplitOr);
//        assertEquals("two-three", unSplitOr);
    }

    @Test
    void givenSpecialCase_shouldNotLoseLastChar() {
        String text = "    Indented text:   three,    four  (five,    six) {";
        String expected, actual;
        IStringParser tokenizer = new CharTok().setDelimiter(" ").setSkipSymbols('(');
        List<String> t = tokenizer.setText(text).parse().toList();//.setStartPos(4)
        actual = String.join(" ", t);
        System.out.println(Arrays.toString(tokenizer.numericToArray()));
        expected = "Indented text: three, four (five,    six) {";
        System.out.println(actual);
        assertEquals(expected, actual);
    }

}