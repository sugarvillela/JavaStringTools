package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.composite2.IndentUtil;
import tokenizer.composite2.SpaceUtil;
import tokenizer.iface.IStringParser;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariousUtilTest {
    @Test
    void givenIndentedText_shouldTokenizeWithIndentsRecorded() {
        String text = "    Indented text:   three,    four";
        String expected, actual;
        IndentUtil indentUtil = new IndentUtil();
        String[] tok = indentUtil.setText(text).parse().toArray();

        expected = "Indented|text:|three,|four";
        actual = String.join("|", tok);
        System.out.println(actual);
        assertEquals(expected, actual);

//        List<Integer> hitMap = indentUtil.numericToList();
//        actual = hitMap.stream().map(String::valueOf).collect(Collectors.joining(","));
//        System.out.println(actual);

        int[] indentMap = indentUtil.getIndentMap();
        expected = "[4, 1, 3, 4]";
        actual = Arrays.toString(indentMap);
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    void givenTextWithExtraSpaces_shouldRemoveExtraSpaces() {
        String text = "    Indented text:   three,    four";
        String expected, actual;
        IStringParser spaceUtil = new SpaceUtil();
        actual = spaceUtil.setText(text).parse().getText();

        expected = "    Indented text: three, four";
        System.out.println(actual);
        assertEquals(expected, actual);
    }
    @Test
    void givenTextWithSkipArea_shouldIgnoreSkipArea() {
        String text = "    Indented text:   three,    four  (five,    six)";
        String expected, actual;
        IStringParser spaceUtil = new SpaceUtil().setSkipSymbols('(');
        actual = spaceUtil.setText(text).parse().getText();

        expected = "    Indented text: three, four (five,    six)";
        System.out.println(actual);
        assertEquals(expected, actual);
    }
}
