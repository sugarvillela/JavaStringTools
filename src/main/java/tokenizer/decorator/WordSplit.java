package tokenizer.decorator;

public class WordSplit extends BaseSplit {
    public WordSplit(){
        super(new WordTok());
    }
}
