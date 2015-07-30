package com.larvalabs.androidify.wallpaper;

import android.graphics.Picture;
import com.larvalabs.svgandroid.SVG;

import java.util.Collection;
import java.util.HashMap;

/**
 * Stores the set of accessories associated with an Android. Only one accessory of each type is allowed. (Types are face, body, left hand, right hand, etc.)
 *
 * @author Larva Labs, LLC
 */
public class AccessorySet {

    private HashMap<String,Accessory> accessories = new HashMap<String, Accessory>();
    private HashMap<String, SVG> pictures = new HashMap<String, SVG>();

    public void add(Accessory accessory, SVG svg) {
        accessories.put(accessory.getType(), accessory);
        pictures.put(accessory.getName(), svg);
    }

    public boolean hasAccessory(Accessory a) {
        return (accessories.get(a.getType()) == a);
    }

    public Picture getPictureForType(String type) {
        Accessory a = accessories.get(type);
        if (a != null) {
            SVG svg = pictures.get(a.getName());
            return svg == null ? null : svg.getPicture();
        }
        return null;
    }

    public SVG getSVGForType(String type) {
        Accessory a = accessories.get(type);
        if (a != null) {
            return pictures.get(a.getName());
        }
        return null;
    }

    public int getAccessoryCount() {
    	return accessories.size();
    }
    
    public int[] getIndexArray() {
    	int[] idxArray  = new int[getAccessoryCount()];
    	int i=0;
    	for(Accessory accessory: accessories.values()) {
    		idxArray[i] = accessory.getIndex();
    		i++;
    	}
    	return idxArray;
    }
    
    public Collection<Accessory> getAllAccessories() {
        return accessories.values();
    }

    public void removeAccessory(String type) {
        Accessory a = accessories.remove(type);
        if (a != null) {
            pictures.remove(a.getName());
        }
    }

    public void clear() {
        accessories.clear();
        pictures.clear();
    }
}
