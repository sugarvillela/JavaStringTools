package tokenizer.decorator;

import tokenizer.iface.IStringParser;

public class PairUnwrap extends BaseDecorator {

    public PairUnwrap(){
        super(new PairValidator());
    }

    @Override
    public IStringParser parse() {
        parser.parse();

        if(!parser.isError()){
            int[] hitMap = parser.numericToArray();
            int i = 0, j = hitMap.length - 1;
            while(hitMap[i] == j && i <= parser.getLimit()){
                i++;
                j--;
            }
            if(i != 0){
                String text = parser.getText();
                parser.setText(text.substring(i, j + 1));
            }
        }

        return this;
    }
}
