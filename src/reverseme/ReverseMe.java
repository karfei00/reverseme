package reverseme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * this is a simple html parser which does the following:
 * 1. reverse content
 * 2. rotate image 180d
 * 3. preserve and maintain content within script tag
 * 
 * Input - taken from input.html file located in the same location as this java class
 * Output - print processed html content as string
 * @author karfng
 *
 */
public class ReverseMe {

	private static final char OPEN_TAG = '<';
	private static final char END_TAG = '>';
	private static final String IMG_TAG = "<img";
	private static final String SCRIPT_START_TAG = "<script>";
	private static final String ROTATE_STYLE = " style=\"transform:rotate(180deg)\"";
	private static final char NEW_LINE = '\n';

	public static void main(String[] args) {
		ReverseMe r = new ReverseMe();
		r.process();
	}
	
	public void process() {
		StringBuilder reverseHtml = new StringBuilder();
		BufferedReader reader = null;
		try {
			URL path = ReverseMe.class.getResource("input.html");
			reader = new BufferedReader(new FileReader(new File(path.getFile())));
			StringBuilder content = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				content.append(line).append(NEW_LINE);				
			}
			reverseHtml.append(reverse(content.toString()));
		} catch (IOException e) {
			System.out.println("HTML input file not found");
			System.exit(1);
		} 
		System.out.println(reverseHtml.toString());
	}
	
	
	public String reverse(String line) {
		StringBuilder reverseLine = new StringBuilder();
		StringBuilder txtContent = new StringBuilder();
		StringBuilder tagContent = new StringBuilder();		
		boolean isTag = false;
		boolean isImgTag = false;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == OPEN_TAG) {
				if(!isTag && txtContent.length()>0) {
					reverseLine.append(txtContent.reverse().toString());
					txtContent = new StringBuilder();	
				}
				isTag = true;
				tagContent.append(c);
			}else if(c == END_TAG) {
				tagContent.append(c);

				//do not reverse script content
				if(!tagContent.toString().equals(SCRIPT_START_TAG)) {
					isTag = false;	
				}
				
				//rotate image using css style
				if(isImgTag) {
					tagContent.insert(tagContent.length()-2, ROTATE_STYLE);
					isImgTag = false;
				}
				
				reverseLine.append(tagContent);
				tagContent = new StringBuilder();
			}else {
				if(isTag) {
					if(tagContent.toString().equals(IMG_TAG)) {
						isImgTag = true;
					}
					tagContent.append(c);	
				}else {
					txtContent.append(c);
				}
			}			
		}
		if (txtContent.length() > 0) {
			reverseLine.append(txtContent.reverse().toString());
		}
		return reverseLine.toString();
	}
}
