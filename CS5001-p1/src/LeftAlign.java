import java.util.ArrayList;

/**
 * The LeftAlign class implements the AlignOperation interface to left-align text.
 */
public class LeftAlign implements AlignOperation {
        private String currentLine = "";
        private String[] paragragh;
        private int lineLength = 0;
        private ArrayList<Integer> idxList = new ArrayList<Integer>();
        private ArrayList<Integer> lineLengthList = new ArrayList<Integer>();

        /**
        * Constructs a LeftAlign object with the specified paragraph and line length.
     	*
     	* @param paragraph the paragraph to be aligned
     	* @param lineLength the maximum length of a line
     	*/
        public LeftAlign(String[] paragragh, int lineLength) {
                this.paragragh = paragragh;
                this.lineLength = lineLength;
                int paraLength = paragragh.length;
                int count = -1;
                for (int i = 0; i < paraLength - 1; i++) {
                        count = count + paragragh[i].length() + 1;
                        if (i < (paraLength - 1) & (count >= lineLength || (count < lineLength & (count + paragragh[i+1].length() + 1) > lineLength))) {
                                this.idxList.add(i);
                                this.lineLengthList.add(count);
                                count = -1;
                        }
                        count = count % lineLength;
                }
                this.idxList.add(paraLength - 1);
                this.lineLengthList.add(count + paragragh[paraLength - 1].length() + 1);
                printText();
        }

        /**
        * Prints the left-aligned text to the console.
     	*/
    	@Override
        public void printText() {
                for (int k = 0; k <= idxList.get(0); k++) {
                        currentLine = currentLine + paragragh[k] + " ";
                }
                currentLine = currentLine.trim();
                System.out.println(currentLine);
                currentLine = "";
                for (int j = 0; j < idxList.size() - 1; j++) {
                        for (int k = idxList.get(j) + 1; k <= idxList.get(j+1); k++) {
                                 currentLine = currentLine + paragragh[k] + " ";
                        }
                        currentLine = currentLine.trim();
                        System.out.println(currentLine);
                        currentLine = "";
                }
        }

        public String generateString(String input, int length, char paddingChar) {
                return String.format("%1$-" + length + "s", input).replace(' ', paddingChar);
        }
}
