import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;

/**
 * The TextAlignment class processes a text file and aligns the text according to specified parameters.
 *
 * @param filename,alignmentType,lineLength.
 * @return regular string printed by the console.
 *
 * author: Zhiqi Pu
 * studentID: 230028496
 * version: 1.0
 * date: 1/10/2023
 */
public class TextAlignment {

	/** The path of the file to be processed. */
	private String filePath;

	/** A list of accepted alignment method strings. */
	private ArrayList<String> list = new ArrayList<>(){ { add("left"); add("right"); add("centre"); add("justify"); } };

	/** The alignment method to be used. */
	private String method;

	/** The length of a line after text alignment. */
	private int lineLength;

	 /** The paragraphs extracted from the file, with each paragraph represented as an array of words. */
	private ArrayList<String[]> paragraphs = new ArrayList<>();

	public static void main(String[] args) {
                TextAlignment ta = new TextAlignment(args);
		try {
			for (int i = 0; i < ta.getParagraphs().size(); i++) {
				if (ta.getMethod().equals("left")) {
					LeftAlign la = new LeftAlign(ta.getParagraphs().get(i), ta.getLineLength());
				} else if (ta.getMethod().equals("right")) {
					RightAlign ra = new RightAlign(ta.getParagraphs().get(i), ta.getLineLength());
				} else if (ta.getMethod().equals("centre")) {
					CentreAlign ca = new CentreAlign(ta.getParagraphs().get(i), ta.getLineLength());
				} else {
					JustifyAlign ja = new JustifyAlign(ta.getParagraphs().get(i), ta.getLineLength());
				}
			}
               } catch (Exception e) {}
	}
	/**
 	* Perform judgment before operating on it, and the constructor runs automatically
 	* 
 	**/ 	
        public TextAlignment(String[] inputs) {
		try {
                        File file = new File(inputs[0]);
			boolean cond1 = file.exists();
                        boolean cond2 = list.contains(inputs[1]);
			Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
                        boolean cond3 = (Double.valueOf(String.valueOf(inputs[2])).intValue() > 0 & pattern.matcher(String.valueOf(inputs[2])).matches());
                        boolean cond4 = inputs.length == 3;
                        if (!cond1) {
				System.out.println("File not found: " + inputs[0] + " (No such file or directory)");
				return;
			} else if (!cond2 || !cond3 || !cond4) {
                                System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
                                return;
                        } else {
                                this.filePath = inputs[0];
                                this.method = inputs[1];
                                this.lineLength = Double.valueOf(String.valueOf(inputs[2])).intValue();
				for (int i = 0; i < FileUtil.readFile(this.filePath).length; i++) {
                                	this.paragraphs.add(FileUtil.readFile(this.filePath)[i].split(" "));
                        	}
                        }
                } catch (Exception e) {
                        System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
                        return;
                }
	}

	public String getMethod() {
		return this.method;
	}

        public ArrayList<String[]> getParagraphs() {
		return this.paragraphs;
	}
	
	public int getLineLength() {
                return this.lineLength;
	}

	public ArrayList<String> getArrayList() {
		return this.list;
	}
}
