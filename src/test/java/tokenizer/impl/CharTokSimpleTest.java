package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.simpl.CharTokSimple;

import static org.junit.jupiter.api.Assertions.*;

class CharTokSimpleTest {
    @Test
    void givenTooManyDelimiters_shouldTokenizeWithoutExtraElements() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new CharTokSimple('_').setText(text).parse().toArray();
        String unTok = String.join("|", tok);
        assertEquals("Sentence|with|(too|many|'delims')|and|quotes", unTok);
    }
}