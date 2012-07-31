package com.larvalabs.androidify.wallpaper;

/**
 * Contains android accessory information (ie. earrings, hats, gloves, held items, etc.).
 *
 * @author Larva Labs, LLC
 */
public class Accessory implements Comparable<Accessory> {

    public static final String TYPE_EARRING = "earring";
    public static final String TYPE_ON_LEFT_HAND = "onlefthand";
    public static final String TYPE_IN_LEFT_HAND = "inlefthand";
    public static final String TYPE_ON_RIGHT_HAND = "onrighthand";
    public static final String TYPE_ON_BOTH_HANDS = "onbothhands";
    public static final String TYPE_LEFT_SHOULDER = "leftshoulder";
    public static final String TYPE_RIGHT_SHOULDER = "rightshoulder";
    public static final String TYPE_IN_RIGHT_HAND = "inrighthand";
    public static final String TYPE_BODY = "body";
    public static final String TYPE_FACE = "face";
    public static final String TYPE_MOUTH = "mouth";
    public static final String TYPE_HEAD = "head";

    private String name;
    private String type;
    
    private int index;	

    public Accessory(int index, String name, String type) {
        this.index = index;
    	this.name = name;
        this.type = type;
    }

    public int getIndex() {
    	return index;
    }
    
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(Accessory o) {
        return name.compareTo(o.name);
    }
}
