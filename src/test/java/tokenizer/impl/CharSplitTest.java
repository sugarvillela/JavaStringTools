package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tokenizer.composite2.CharSplit;
import tokenizer.iface.IStringParser;

class CharSplitTest {
    @Test
    void givenStringWithSpace_split() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"');
        String[] pair;
        String text, expected, actual;

        text = "zero one two three four five six seven eight nine ten";

        pair = splitUtil.setText(text).parse().toArray();
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(15).setText(text).parse().toArray();
        expected = "zero one two three|four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(4).setText(text).parse().toArray();
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(3).setText(text).parse().toArray();
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(100).setText(text).parse().toArray();
        expected = "zero one two three four five six seven eight nine ten|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithSkipArea_splitAfterSkip() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"');
        String[] pair;
        String text, expected, actual;

        text = "zero one two \"three four\" five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).setText(text).parse().toArray();
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(13).setText(text).parse().toArray();
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(24).setText(text).parse().toArray();
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithBorderingSkipArea_splitAfterSkip() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"');
        String[] pair;
        String text, expected, actual;

        text = "nine \"ten\"";
        pair = splitUtil.setStartPos(6).setText(text).parse().toArray();
        expected = "nine \"ten\"|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        text = "\"eight nine\" ten";
        pair = splitUtil.setText(text).parse().toArray();
        expected = "\"eight nine\"|ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        text = "\"eight nine ten\"";
        pair = splitUtil.setText(text).parse().toArray();
        expected = "\"eight nine ten\"|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithEscapeSymbol_splitOnNext() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"');
        String[] pair;
        String text, expected, actual;

        text = "zero one two\\ three four five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).setText(text).parse().toArray();
        expected = "zero one two\\ three|four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithNoSplit_noSplit() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"');
        String[] pair;
        String text, expected, actual;

        text = "zero_one_two_three_four_five_six_seven_eight_nine_ten";

        pair = splitUtil.setText(text).parse().toArray();
        expected = "zero_one_two_three_four_five_six_seven_eight_nine_ten|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenHackUsage_returnRelevantResult() {
        IStringParser splitUtil = new CharSplit().setLimit(3).setDelimiter("_").setSkipSymbols('"').setTokenizeDelimiterOnce(true);
        String[] pair;
        String text, expected, actual;

        text = "zero one two_three four_five six seven eight nine ten";

        pair = splitUtil.setText(text).parse().toArray();
        expected = "zero one two|_|three four_five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenHackUsage2_returnRelevantResult() {
        IStringParser splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols('"').setLimit(5);
        String[] pair;
        String text, expected, actual;

        text = "zero_one_two_three_four_five_six_seven_eight_nine_ten";

        pair = splitUtil.setText(text).parse().toArray();
        expected = "zero_one_two_three_four_five_six_seven_eight_nine_ten|null|null|null|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
}