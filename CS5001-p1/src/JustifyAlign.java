import java.util.ArrayList;

/**
 * The JustifyAlign class implements the AlignOperation interface to
 * justify-align text. Text is adjusted to fit within a specified line length.
 * Words that exceed the line length are broken into smaller parts.
 */
public class JustifyAlign implements AlignOperation {

	private String currentLine = "";
        private ArrayList<String> temParagraph = new ArrayList<String>();
        private int lineLength = 0;
	private String[] paragraph;
        private ArrayList<Integer> idxList = new ArrayList<Integer>();
        private ArrayList<Integer> lineLengthList = new ArrayList<Integer>();


	/**
     	* Constructs a JustifyAlign object with the specified paragraph and line length.
     	*
     	* @param paragraph the paragraph to be aligned
     	* @param lineLength the maximum length of a line
     	*/
        public JustifyAlign(String[] paragraph, int lineLength) {

                this.lineLength = lineLength;
		this.paragraph = paragraph;
		int count = -1;

		// Call manipulateStrings() to return the string index when cutting and wrapping, and the corresponding total length.
		manipulateStrings(paragraph, lineLength);
		
		for (int i = 0; i < temParagraph.size() - 1; i++) {
			count = count + temParagraph.get(i).length() + 1;
			if (count <= lineLength & (count + temParagraph.get(i + 1).length() + 1) > lineLength) {
                                this.idxList.add(i);
                                this.lineLengthList.add(count);
                                count = -1;
                        }
                        count = count % lineLength;
		}
                this.idxList.add(temParagraph.size() - 1);
                this.lineLengthList.add(count + temParagraph.get(temParagraph.size() - 1).length() + 1);
		printText();
        }
        

	/**
     	* Prints the justified text to the console.
     	*/
    	@Override
        public void printText() {
		for (int k = 0; k <= idxList.get(0); k++) {
                        currentLine = currentLine + temParagraph.get(k) + " ";
                }
                currentLine = currentLine.trim();
                System.out.println(currentLine);
                currentLine = "";
                for (int j = 0; j < idxList.size() - 1; j++) {
                        for (int k = idxList.get(j) + 1; k <= idxList.get(j + 1); k++) {
                                currentLine = currentLine + temParagraph.get(k) + " ";
                        }
                        currentLine = currentLine.trim();
                        System.out.println(currentLine);
                        currentLine = "";
                }
        }

	
	/**
     	* Manipulates strings to fit within the specified line length.
     	* Words that exceed the line length are broken into smaller parts.
     	*
     	* @param paragraph the paragraph to be manipulated
     	* @param lineLength the maximum length of a line
     	*/
	public void manipulateStrings(String[] paragraph, int lineLength) {
		int paraLength = paragraph.length;
                int count = -1;
                String subStr1 = "";
                String subStr2 = "";

                for (int i = 0; i < paraLength; i++) {
                        count = count + paragraph[i].length() + 1;
                        if (count > lineLength & (paragraph[i].length() - count + lineLength) > 1) {
				do {
                                        subStr1 = paragraph[i].substring(0, paragraph[i].length() - count + lineLength - 1) + "-";
                                        subStr2 = paragraph[i].substring(paragraph[i].length() - count + lineLength - 1, paragraph[i].length());
                                	temParagraph.add(subStr1);
					count = subStr2.length();
					paragraph[i] = subStr2;
				} while (subStr2.length() > lineLength);
				temParagraph.add(subStr2);
                        } else if (paragraph[i].length() > lineLength) {
				do {
                                        subStr1 = paragraph[i].substring(0, lineLength - 1) + "-";
                                        subStr2 = paragraph[i].substring(lineLength - 1, paragraph[i].length());
                                        temParagraph.add(subStr1);
                                        count = subStr2.length();
                                        paragraph[i] = subStr2;
                                } while (subStr2.length() > lineLength);
                                temParagraph.add(subStr2);
			} else {
				if (count / lineLength > 0 & count % lineLength > 0) {
					i -= 1;
					count = -1;
				} else {
                                	temParagraph.add(paragraph[i]);
					count = count % lineLength;
 		                        if (count == 0) {
                		                count = -1;
                        		}
				}
                        }
                }
	}
    

	/**
     	* Generates a string padded to the specified length with a specified character.
     	*
     	* @param input the input string
     	* @param length the target length
     	* @param paddingChar the character to pad with
     	* @return the padded string
     	*/
        public String generateString(String input, int length, char paddingChar) {
                return String.format("%1$-" + length + "s", input).replace(' ', paddingChar);
        }
}
