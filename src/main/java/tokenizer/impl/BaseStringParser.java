package tokenizer.impl;

import tokenizer.iface.IStringParser;

import java.util.List;
import java.util.Locale;
import java.util.Stack;

public abstract class BaseStringParser implements IStringParser {
    protected static final char escape = '\\';
    protected IWhitespaceTest whitespaceTest; // object for char matchers to test for whitespace
    protected ICaseTest caseTest;             // object for all matchers to test case sensitive or insensitive
    protected String delimiters;              // input text, list of delimiters text
    protected char[] oMap, cMap;              // matched open/close skip char arrays
    protected Stack<Character> cSymbols;      // Closing symbol during skip
    protected boolean caseSensitive;          // affects match utils
    protected boolean tokenizeDelimiter;      // save delimiter to own element
    protected boolean delimiterOnce;          // save delimiter to own element, ignore duplicates
    protected int limit;                      // number of tokens allowed
    protected int startPos;
    protected String text;
    protected boolean escaped;                // state

    protected BaseStringParser(){
        cSymbols = new Stack<>();
        tokenizeDelimiter = false;
        caseSensitive = true;
        limit = 0xFFFFFFF;
        this.initCaseTest();
    }

    /*====Set up======================================================================================================*/

    @Override
    public IStringParser setText(String text) {
        this.text = text;
        return this;
    }

    public IStringParser setSkipSymbols(String openingSymbols) {
        this.setMap(openingSymbols);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char oneOpeningSymbol) {
        this.setMap(String.valueOf(oneOpeningSymbol));
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char openingSymbol, char closingSymbol) {
        this.oMap = new char[]{openingSymbol};
        this.cMap = new char[]{closingSymbol};
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char[] oMap, char[] cMap) {
        this.oMap = oMap;
        this.cMap = cMap;
        return this;
    }

    @Override
    public IStringParser setStartPos(int startPos) {
        this.startPos = startPos;
        return this;
    }

    @Override
    public IStringParser setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public IStringParser setCaseSensitive(boolean caseSensitive){
        this.caseSensitive = caseSensitive;
        this.initCaseTest();
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiter(boolean tokenize) {
        this.tokenizeDelimiter = tokenize;
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiterOnce(boolean tokenizeOnce) {
        this.tokenizeDelimiter = true;
        this.delimiterOnce = tokenizeOnce;
        return this;
    }

    @Override
    public IStringParser setDelimiter(String delimiters) {
        this.delimiters = delimiters;
        this.initWhitespaceTest();
        return this;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<String> toList() {
        return null;
    }

    @Override
    public String[] toArray() {
        return null;
    }

    @Override
    public List<Integer> numericToList() {
        return null;
    }

    @Override
    public int[] numericToArray() {
        return null;
    }

    /*====protected setup utils=======================================================================================*/

    protected abstract void initCaseTest();

    protected void initWhitespaceTest(){
        if(this.delimiters.contains(" ")){
            whitespaceTest = new IWhitespaceTest() {
                @Override
                public boolean eq(char symbol) {
                    return ((int)symbol) < 33;
                }
            };
        }
        else{
            whitespaceTest = new IWhitespaceTest() {};
        }
    }

    protected void setMap(String skips){
        // map openers to closers, using symbols from arg
        // if you want different symbols, pass arrays with Builder
        oMap =  new char[skips.length()];
        cMap =  new char[skips.length()];
        char[] openers = new char[]{'(','{','[','<','"','\''};
        char[] closers = new char[]{')','}',']','>','"','\''};
        int to = 0;
        for (int i = 0; i < openers.length; i++) {
            if(skips.indexOf(openers[i]) != -1){
                oMap[to]=openers[i];
                cMap[to]=closers[i];
                to++;
            }
        }
    }

    /*====protected utility methods===================================================================================*/

    protected boolean isEscape(char symbol){
        return symbol == escape;
    }

    protected boolean haveTextToAdd(int i, int j){
        return  i != j;
    }

    protected boolean tryPushSkipSymbol(char symbol){
        if(oMap != null){
            for(int x = 0; x < oMap.length; x++){
                if(symbol == oMap[x]){
                    this.cSymbols.push(cMap[x]);// important side effect
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isInSkipArea(){
        return !cSymbols.isEmpty();
    }

    protected boolean tryPopSkipSymbol(char symbol){
        if(cSymbols.peek().equals(symbol)){
            cSymbols.pop();
            return true;
        }
        return false;
    }

    /*=====Getters for decorator impl=================================================================================*/

    @Override
    public boolean isTokenizeDelimiter() {
        return tokenizeDelimiter;
    }

    @Override
    public boolean isDelimiterOnce() {
        return delimiterOnce;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int getStartPos() {
        return startPos;
    }

    public interface IWhitespaceTest {
        default boolean eq(char symbol) {
            return false;
        }
    }
    public interface ICaseTest {
        default boolean isMatch(char c, int index) {
            return false;
        }
        default boolean contains(char c) {
            return false;
        }
        default char swapCase(char c){
            return Character.isUpperCase(c)? Character.toLowerCase(c) : Character.toUpperCase(c);
        }
    }
}
