package phraser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javaSLK.InvalidRecordException;
import javaSLK.InvalidRecordFieldException;
import javaSLK.SLKFile;
import javaSLK.SLKParseMethod;

public class UIAnalyzer {
	public static HashMap<String, String> idToModelPath = new HashMap<String, String>();
	
	public static void analyzeUiFile(File f) {
		try {
			SLKFile slk = new SLKFile(f, SLKParseMethod.BLIZZARD);
			int height = slk.getHeight();
			for (int row =1; row < height ; row++) {
				idToModelPath.put(slk.getCell(0, row), slk.getCell(1, row));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
