package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tokenizer.decorator.CharReplace;
import tokenizer.iface.IStringParser;

import java.util.List;

public class CharMatchTest {
    @Test
    void givenStringWithManyTargets_findNumOccurs(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack);
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 6;
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithQuotedTarget_findNumOccurs(){
        String needle = "xyz";
        String haystack = "zero y two \"xy four\" five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack).setSkipSymbols('"');
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 4;
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithEscapedChar_findNumOccurs(){
        String needle = "xyz";
        String haystack = "zero \\y two \"xy four\" five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack).setSkipSymbols('"');
        int actual1 = matchUtil.parse().numeric();
        int expected1 = 3;
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithManyTargets_getHitMap(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {0, 5, 11, 12, 26, 34};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            //System.out.println(hitMap.get(i));
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }
    @Test
    void givenStringWithStartPos_getHitMap(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack).setStartPos(12);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {12, 26, 34};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            //System.out.println(hitMap.get(i));
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }
    @Test
    void givenStringWithLimit_getHitMap(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setDelimiter(needle).setText(haystack).setLimit(5);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {0, 5, 11, 12, 26};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            //System.out.println(hitMap.get(i));
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }
    @Test
    void givenStringWithMixedCaseTargets_getHitMap(){
        String needle = "Xyz";
        String haystack = "zero Y two xZ four five six seven z eight nine ten";
        IStringParser matchUtil = new CharMatch().setCaseSensitive(false).setDelimiter(needle).setText(haystack);
        List<Integer> hitMap = matchUtil.parse().numericToList();
        int[] expected = {0, 5, 11, 12, 26, 34};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            //System.out.println(hitMap.get(i));
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }
    @Test
    void givenStringWithTarget_deleteTarget(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine ten";
        IStringParser replaceUtil = new CharReplace("").setDelimiter(needle).setText(haystack).setLimit(1);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "ero y two xy four five six seven z eight nine ten";
        System.out.println(actual1);
        //Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void givenStringWithManyTargets_deleteAll(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine any";
        IStringParser replaceUtil = new CharReplace("").setDelimiter(needle).setText(haystack);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "ero  two  four five si seven  eight nine an";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(7, replaceUtil.numeric());
    }

    @Test
    void givenStringWithMixedCaseTargets_deleteAll(){
        String needle = "xyz";
        String haystack = "zero Y two xZ four five six seven Z eight nine anY";
        IStringParser replaceUtil = new CharReplace("").setCaseSensitive(false).setDelimiter(needle).setText(haystack);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "ero  two  four five si seven  eight nine an";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(7, replaceUtil.numeric());
    }

    @Test
    void givenStringWithManyTargets_replaceAll(){
        String needle = "xyz";
        String haystack = "zero y two xy four five six seven z eight nine any";
        IStringParser replaceUtil = new CharReplace("|").setDelimiter(needle).setText(haystack);

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "|ero | two || four five si| seven | eight nine an|";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(7, replaceUtil.numeric());
    }

    @Test
    void givenStringWithQuotedTargets_replaceSome(){
        String needle = "xyz";
        String haystack = "zero y two xy four \"five six seven z eight nine any\"";
        IStringParser replaceUtil = new CharReplace("|").setDelimiter(needle).setText(haystack).setSkipSymbols('"');

        String actual1 = replaceUtil.parse().getText();
        String expected1 = "|ero | two || four \"five six seven z eight nine any\"";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(4, replaceUtil.numeric());
    }
}
