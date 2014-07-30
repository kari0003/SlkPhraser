package phraser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class WurstGenerator {
	public String OBJNAME = "ModelPathHandler";
	
	public static void generateWurst(LinkedList<UnitData> allUnits) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("StdUnitData.wurst", "UTF-8");
		writer.println("package StdUnitData");
		writer.println("import LinkedList");
		writer.println("import UnitObjEditing");
		writer.println("import MapStuff");
		writer.print("//Some families are created. I just realized that saving model strings is not enough,\n" +
					"//Their unit definitions/ID-s need to be in the class too. But this will be added in advance.\n");
		
		writer.println("//Stringwrap serves the single prupose to make you able to put strings in a linkedlist");
		writer.println(UnitData.generateHeader());
		writer.println("");
		
		writer.println("");
		writer.println("public function init_ModelPaths(bool compTime)");
		for (UnitData data : allUnits) {
			writer.println(data.createWurstClass());
		}
		for (UnitData data : allUnits) {
			writer.println(data.add_familiars());
		}
		writer.close();
	}
	
}
