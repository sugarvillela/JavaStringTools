package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tokenizer.composite.WordReplace;
import tokenizer.iface.IStringParser;

import java.util.List;

class WordMatchTest {
    @Test
    void givenStringWithTarget_findTargetInString(){
        String needle = "three";
        String haystack = "zero one two three four five six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack).setLimit(1);

        int actual1 = -1, actual2 = -1;
        int expected1 = 13, expected2 = 18;
        if(matchUtil.parse().numeric() > 0){
            List<Integer> indices = matchUtil.numericToList();
            actual1 = indices.get(0);
            actual2 = indices.get(1);
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    void givenStringWithManyTargets_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack);
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 3;
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithQuotedTarget_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two three four \"three\"three six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack).setSkipSymbols('"');
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 2;
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithEscapedChar_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two \\three four \"three\"three six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack).setSkipSymbols('"');
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 1;
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithShortTarget_findTargetInString() {
        String needle = "{";
        String haystack = "zero one {two three four five six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack).setLimit(1);
        int actual1 = -1, actual2 = -1;
        int expected1 = 9, expected2 = 10;
        if (matchUtil.parse().numeric() > 0) {
            List<Integer> indices = matchUtil.numericToList();
            actual1 = indices.get(0);
            actual2 = indices.get(1);
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    void givenStringWithManyTargets_getHitMap(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setText(haystack);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {13,18,24,29,29,34};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }

    @Test
    void givenStringWithMixedCaseTargets_getHitMap(){
        String needle = "three";
        String haystack = "zero one two THRee four THREEthree six seven eight nine ten";
        IStringParser matchUtil = new WordMatch().setDelimiter(needle).setCaseSensitive(false).setText(haystack);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {13,18,24,29,29,34};
        Assertions.assertEquals(expected.length, hitMap.size());
        for(int i = 0; i < expected.length; i++){
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }

    @Test
    void givenStringWithTarget_deleteTarget(){
        String needle = "three";
        String haystack = "zero one two three four five six seven eight nine ten";
        IStringParser replaceUtil = new WordReplace("").setDelimiter(needle).setText(haystack).setLimit(1);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "zero one two  four five six seven eight nine ten";
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithManyTargets_deleteAll(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IStringParser replaceUtil = new WordReplace("").setDelimiter(needle).setText(haystack);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "zero one two  four  six seven eight nine ten";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(3, replaceUtil.numeric());
    }

    @Test
    void givenStringWithManyTargets_replaceAll(){
        String needle = "three";
        String haystack = "three one two three four threethree six seven eight nine three";
        IStringParser replaceUtil = new WordReplace("THREE").setDelimiter(needle).setText(haystack);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "THREE one two THREE four THREETHREE six seven eight nine THREE";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(5, replaceUtil.numeric());
    }

    @Test
    void givenStringWithQuotedTargets_replaceSome(){
        String needle = "three";
        String haystack = "three one two three four \"threethree six seven eight nine three\"";
        IStringParser replaceUtil = new WordReplace("THREE").setDelimiter(needle).setText(haystack).setSkipSymbols('"');

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "THREE one two THREE four \"threethree six seven eight nine three\"";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(2, replaceUtil.numeric());
    }
}