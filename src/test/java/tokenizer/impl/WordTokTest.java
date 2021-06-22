package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.composite.WordTok;
import tokenizer.iface.IStringParser;

import static org.junit.jupiter.api.Assertions.*;

public class WordTokTest {
    @Test
    void givenDelimiterWord_tokenizeOnWord(){
        String text, expected, actual;
        IStringParser tokenizer = new WordTok().setDelimiter("---").setSkipSymbols("('").setTokenizeDelimiter(true);

        text = "one two---three four---five six seven";
        expected = "one two|---|three four|---|five six seven";
        actual = String.join("|", tokenizer.setText(text).parse().toList());
        System.out.println(actual);
        assertEquals(expected, actual);

        text = "---one two three four---five six seven";
        expected = "---|one two three four|---|five six seven";
        actual = String.join("|", tokenizer.setText(text).parse().toList());
        System.out.println(actual);
        assertEquals(expected, actual);

        text = "one two three four---five six seven---";
        expected = "one two three four|---|five six seven|---";
        actual = String.join("|", tokenizer.setText(text).parse().toList());
        System.out.println(actual);
        assertEquals(expected, actual);
    }
}
