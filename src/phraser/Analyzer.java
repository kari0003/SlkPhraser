package phraser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import javaSLK.InvalidRecordException;
import javaSLK.InvalidRecordFieldException;
import javaSLK.SLKFile;
import javaSLK.SLKParseMethod;
import distance.LCS;

public class Analyzer {

	public static void main(String[] args) throws InvalidRecordFieldException, InvalidRecordException, IOException {
		SLKFile hero = new SLKFile(new File("UnitData.slk"), SLKParseMethod.BLIZZARD);
		UIAnalyzer.analyzeUiFile(new File("unitUI.slk"));
		//System.out.println("opened.");
		//PrintWriter writer = new PrintWriter("coolmodelz.txt", "UTF-8");
		LinkedList<UnitData> datas = new LinkedList<UnitData>();
		int height = hero.getHeight();
		for (int row =1; row < height; row++) {
			UnitData data = new UnitData();
			data.unitID = hero.getCell(0, row);
			data.name = hero.getCell(2, row);
			data.race = hero.getCell(3, row);
			data.type = hero.getCell(6, row);
			data.movetp = hero.getCell(12, row);
			try {
				data.formation = Integer.parseInt(String.valueOf(hero.getCell(22, row))); // this
			} catch (NumberFormatException ex) {
				data.formation = 0;
			}

			// The first row contains the colun names, so it cant be an int
			data.targType = (String) hero.getCell(25, row);
			if(data.race != null && data.race.contains("creeps") &&
					data.targType != null && data.targType.contains("ground")
					&& data.formation != 2)
				datas.add(data);
		}
		// Sort out duplicates
		HashMap<String, Boolean> table = new HashMap<String, Boolean>();
		LinkedList<UnitData> cleanDatas = new LinkedList<UnitData>();
		for(UnitData data: datas) {
			if(table.get((UIAnalyzer.idToModelPath.get(data.unitID))) == null) {
				table.put(UIAnalyzer.idToModelPath.get(data.unitID), true);
				cleanDatas.add(data);
			}else {
//				System.out.println("duplicate found");
			}
		}
		LinkedList<UnitData> creeps = new LinkedList<UnitData>();
		LinkedList<String> allNames = new LinkedList<String>();
		LinkedList<String> familyNames = new LinkedList<String>();
		for (UnitData unitData : cleanDatas) {
			for (UnitData unitData2 : cleanDatas) {
				if(unitData != unitData2 && LCS.longestSubstr(unitData.name.toLowerCase(), unitData2.name.toLowerCase()) > 4) {
					unitData.familiars.add(unitData2);
				}
			}

		}
		for (UnitData unitData : cleanDatas) {
			System.out.println(unitData.name);
			for (UnitData f : unitData.familiars) {
				System.out.println("\t" + f.name + " : " + LCS.longestSubstr(unitData.name.toLowerCase(), f.name.toLowerCase()));
			}
		}
		WurstGenerator.generateWurst(cleanDatas);
		//		for (UnitData unitData : datas) {
		//			if(unitData.race != null && unitData.race.contains("creeps") &&
		//				unitData.targType != null && unitData.targType.contains("ground")
		//				&& unitData.formation != 2)
		//				System.out.println(unitData.name + " , " + unitData.unitID+ " , " + unitData.type + " , " + unitData.movetp+ " , " + unitData.formation);
		//			
		//		}
	}

}
