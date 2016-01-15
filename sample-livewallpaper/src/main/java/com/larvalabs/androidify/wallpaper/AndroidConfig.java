package com.larvalabs.androidify.wallpaper;

import java.util.Collection;
import java.util.HashMap;

/**
 * Stores the full configuration information for an android (skin color, hair color, body part sizes, clothing and
 * accessories).
 *
 * @author John Watkinson
 */
public class AndroidConfig implements Comparable<AndroidConfig> {

    private long id;
    
    private String name;
    
    private String hair;
    private String shirt;
    private String pants;
    private String shoes;
    private String glasses;
    private String beard;

    private HashMap<String,String> accessories = new HashMap<String, String>();

    private float bodyScaleX, bodyScaleY;
    private float headScaleX, headScaleY;
    private float armScaleX, armScaleY;
    private float legScaleX, legScaleY;

    private int hairColor;
    private int skinColor;
    private int pantsColor;

    public AndroidConfig() {
        id = System.currentTimeMillis();
    }

    @Override
    public int compareTo(AndroidConfig o) {
        if (name == null || o.name == null || name.equals(o.name)) {
            return Double.compare(id, o.id);
        } else {
            return name.toUpperCase().compareTo(o.name.toUpperCase());
        }
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getShirt() {
        return shirt;
    }

    public void setShirt(String shirt) {
        this.shirt = shirt;
    }

    public String getPants() {
        return pants;
    }

    public void setPants(String pants) {
        this.pants = pants;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }

    public String getGlasses() {
        return glasses;
    }

    public void setGlasses(String glasses) {
        this.glasses = glasses;
    }

    public String getBeard() {
        return beard;
    }

    public void setBeard(String beard) {
        this.beard = beard;
    }

    public float getBodyScaleX() {
        return bodyScaleX;
    }

    public void setBodyScaleX(float bodyScaleX) {
        this.bodyScaleX = bodyScaleX;
    }

    public float getBodyScaleY() {
        return bodyScaleY;
    }

    public void setBodyScaleY(float bodyScaleY) {
        this.bodyScaleY = bodyScaleY;
    }

    public float getHeadScaleX() {
        return headScaleX;
    }

    public void setHeadScaleX(float headScaleX) {
        this.headScaleX = headScaleX;
    }

    public float getHeadScaleY() {
        return headScaleY;
    }

    public void setHeadScaleY(float headScaleY) {
        this.headScaleY = headScaleY;
    }

    public float getArmScaleX() {
        return armScaleX;
    }

    public void setArmScaleX(float armScaleX) {
        this.armScaleX = armScaleX;
    }

    public float getArmScaleY() {
        return armScaleY;
    }

    public void setArmScaleY(float armScaleY) {
        this.armScaleY = armScaleY;
    }

    public float getLegScaleX() {
        return legScaleX;
    }

    public void setLegScaleX(float legScaleX) {
        this.legScaleX = legScaleX;
    }

    public float getLegScaleY() {
        return legScaleY;
    }

    public void setLegScaleY(float legScaleY) {
        this.legScaleY = legScaleY;
    }

    public int getHairColor() {
        return hairColor;
    }

    public void setHairColor(int hairColor) {
        this.hairColor = hairColor;
    }

    public int getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(int skinColor) {
        this.skinColor = skinColor;
    }

    public int getPantsColor() {
        return pantsColor;
    }

    public void setPantsColor(int pantsColor) {
        this.pantsColor = pantsColor;
    }

    public void clearAccessories() {
        accessories.clear();
    }

    public void addAccessory(Accessory accessory) {
        accessories.put(accessory.getType(), accessory.getName());
    }

    public Collection<String> getAllAccessories() {
        return accessories.values();
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public long getId() {
    	return id;
    }
    
    public void setId(long id) {
    	this.id = id;
    }
}
