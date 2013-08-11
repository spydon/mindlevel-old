package net.mindlevel.client;

public class Normalizer {
	
	public static String capitalizeName(String name) {
		String nameList[] = name.split(" ");
		String capitalizedName = "";
		for(int x = 0; x<nameList.length; x++) {
			String tmpName = nameList[x];
			if(!tmpName.equals("")) {
				char letter = Character.toUpperCase(tmpName.charAt(0));
				tmpName = Character.toString(letter).concat(tmpName.substring(1));
				capitalizedName = capitalizedName.concat(tmpName);
				if(!(x==nameList.length-1))
					capitalizedName = capitalizedName.concat(" ");
			}
		}
		return capitalizedName;
	}
	
	public static String normalizeDate(String date) {
		int end = date.indexOf(".");
		if (end != -1)
			return date.substring(0, end);
		else
			return date;
	}
}
