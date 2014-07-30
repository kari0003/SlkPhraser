package phraser;

import java.util.LinkedList;

public class UnitData {
	LinkedList<UnitData> familiars = new LinkedList<UnitData>();
	String unitID;
	//String sort;
	String name;
	String race = "none";
	//String prio;
	//String threat;
	String type;
	//String valid;
	//String deathType;
	//String death;
	//String cargoSize;
	String movetp;
	//String moveHeight;
	//String moveFloor;
	//int launchX;
	//int launchY;
	//int launchZ;
	//String impactZ;
	//String turnRate;
	//String propWin;
	//String orientInterp;
	Integer formation = 0;
	//String castpt;
	//String castbsw;
	String targType;
	//String pathTex;
	//String fatLOS;
	//String collision;
	//String points;
	//String buffType;
	//String buffRadius;
	//String nameCount;
	//String InBeta;
	
	public String print() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	public String createWurstClass(){
		StringBuilder sb = new StringBuilder();
		String wname = name.replace(" ", "_");
		sb.append("\tint " + wname + "_id\n");
		sb.append("\tfor i = 0 to 5\n");
		sb.append("\t\tfor j = 0 to 5\n");
		sb.append("\t\t\tid = UNIT_ID_GEN.next()\n");
		sb.append("\t\t\tif i == 0 and j == 0\n");
		sb.append("\t\t\t\t " + wname + "_id = id\n");
		sb.append("\t\t\tif compTime\n");
		sb.append("\t\t\t\tlet def = new UnitDefinition(id , "+unitID+")\n");
		sb.append("\t\t\t\t..setName(\"Mutant\")..setHitPointsMaximumBase(100)\n");
		sb.append("\t\t\t\t..setModelFile(m.path)..setAttack1AttackType(i castTo AttackType)..setArmorType(j castTo ArmorType)\n");
		sb.append("\tvar " + wname + " = new UnitData("+wname+"_id)\n");
		
		return sb.toString();
	}
	
	public String add_familiars(){
		StringBuilder sb = new StringBuilder();
		String wname = name.replace(" ", "_");
		for (UnitData fam : familiars) {
			sb.append("\t"+wname+".familiars.add("+ fam.name.replace(" ", "_") +")\n");
		}
		if(! familiars.isEmpty())
			sb.append("\t\n");
		return sb.toString();
	}
	
	public static String generateHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("package UnitData\n\n");
		sb.append("class UnitData\n");
		sb.append("\tint uid\n");
		sb.append("\tLList<UnitData> familiars = new LList<UnitData>()\n");
		sb.append("\t\n");
		sb.append("\tconstruct(int id)\n");
		sb.append("\t\tuid = id\n");
		sb.append("\t\t\n");
		return sb.toString();
	}
}
