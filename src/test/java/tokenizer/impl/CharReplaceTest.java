package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tokenizer.composite.CharReplace;
import tokenizer.iface.IStringParser;
import tokenizer.composite2.TabUtil;

public class CharReplaceTest {
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

    @Test
    void givenStringWithTabs_replaceWithSpaces(){
        String haystack = "Item 1:\teat,\tItem 2:\tsleep,\tItem 3:\teat again";
        IStringParser tabUtil = new TabUtil().setText(haystack);

        String actual1 = tabUtil.parse().getText();
        String expected1 = "Item 1:    eat,    Item 2:    sleep,    Item 3:    eat again";
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(5, tabUtil.numeric());
    }
}
