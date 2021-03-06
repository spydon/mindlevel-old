package net.mindlevel.shared;

import java.util.List;

public class Normalizer {

    public static String capitalizeName(String name) {
        name = name.toLowerCase();
        String nameList[] = name.split(" ");
        String capitalizedName = "";
        for(int x = 0; x<nameList.length; x++) {
            String tmpName = nameList[x];
            if(!tmpName.equals("")) {
                char letter = Character.toUpperCase(tmpName.charAt(0));
                tmpName = Character.toString(letter).concat(tmpName.substring(1));
                capitalizedName = capitalizedName.concat(tmpName);
                if(!(x==nameList.length-1)) {
                    capitalizedName = capitalizedName.concat(" ");
                }
            }
        }
        return capitalizedName;
    }

    public static String categoriesToString(List<Category> items) {
        String readable = "";
        if(items.size() > 0) {
            for(Category item : items)
                readable += ", " + capitalizeName(item.toString());
            readable = readable.substring(2);
        }
        return readable;
    }

    public static String listToString(List<String> items) {
        String readable = "";
        if(items.size() > 0) {
            for(String item : items)
                readable += ", " + item;
            readable = readable.substring(2);
        }
        return readable;
    }
}
