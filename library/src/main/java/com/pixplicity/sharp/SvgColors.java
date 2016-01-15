/*
    Copyright 2011, 2015 Pixplicity, Larva Labs LLC and Google, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Sharp is heavily based on prior work. It was originally forked from
        https://github.com/pents90/svg-android
    And changes from other forks have been consolidated:
        https://github.com/b2renger/svg-android
        https://github.com/mindon/svg-android
        https://github.com/josefpavlik/svg-android
 */

package com.pixplicity.sharp;

import java.util.HashMap;

public class SvgColors {

    private static HashMap<String, Integer> colors = new HashMap<String, Integer>();

    public static Integer mapColor(String color) {
        return colors.get(color.toLowerCase());
    }

    static {
        colors.put("aliceblue", 0xf0f8ff);
        colors.put("antiquewhite", 0xfaebd7);
        colors.put("aqua", 0x00ffff);
        colors.put("aquamarine", 0x7fffd4);
        colors.put("azure", 0xf0ffff);
        colors.put("beige", 0xf5f5dc);
        colors.put("bisque", 0xffe4c4);
        colors.put("black", 0x000000);
        colors.put("blanchedalmond", 0xffebcd);
        colors.put("blue", 0x0000ff);
        colors.put("blueviolet", 0x8a2be2);
        colors.put("brown", 0xa52a2a);
        colors.put("burlywood", 0xdeb887);
        colors.put("cadetblue", 0x5f9ea0);
        colors.put("chartreuse", 0x7fff00);
        colors.put("chocolate", 0xd2691e);
        colors.put("coral", 0xff7f50);
        colors.put("cornflowerblue", 0x6495ed);
        colors.put("cornsilk", 0xfff8dc);
        colors.put("crimson", 0xdc143c);
        colors.put("cyan", 0x00ffff);
        colors.put("darkblue", 0x00008b);
        colors.put("darkcyan", 0x008b8b);
        colors.put("darkgoldenrod", 0xb8860b);
        colors.put("darkgray", 0xa9a9a9);
        colors.put("darkgreen", 0x006400);
        colors.put("darkgrey", 0xa9a9a9);
        colors.put("darkkhaki", 0xbdb76b);
        colors.put("darkmagenta", 0x8b008b);
        colors.put("darkolivegreen", 0x556b2f);
        colors.put("darkorange", 0xff8c00);
        colors.put("darkorchid", 0x9932cc);
        colors.put("darkred", 0x8b0000);
        colors.put("darksalmon", 0xe9967a);
        colors.put("darkseagreen", 0x8fbc8f);
        colors.put("darkslateblue", 0x483d8b);
        colors.put("darkslategray", 0x2f4f4f);
        colors.put("darkslategrey", 0x2f4f4f);
        colors.put("darkturquoise", 0x00ced1);
        colors.put("darkviolet", 0x9400d3);
        colors.put("deeppink", 0xff1493);
        colors.put("deepskyblue", 0x00bfff);
        colors.put("dimgray", 0x696969);
        colors.put("dimgrey", 0x696969);
        colors.put("dodgerblue", 0x1e90ff);
        colors.put("firebrick", 0xb22222);
        colors.put("floralwhite", 0xfffaf0);
        colors.put("forestgreen", 0x228b22);
        colors.put("fuchsia", 0xff00ff);
        colors.put("gainsboro", 0xdcdcdc);
        colors.put("ghostwhite", 0xf8f8ff);
        colors.put("gold", 0xffd700);
        colors.put("goldenrod", 0xdaa520);
        colors.put("gray", 0x808080);
        colors.put("green", 0x008000);
        colors.put("greenyellow", 0xadff2f);
        colors.put("grey", 0x808080);
        colors.put("honeydew", 0xf0fff0);
        colors.put("hotpink", 0xff69b4);
        colors.put("indianred", 0xcd5c5c);
        colors.put("indigo", 0x4b0082);
        colors.put("ivory", 0xfffff0);
        colors.put("khaki", 0xf0e68c);
        colors.put("lavender", 0xe6e6fa);
        colors.put("lavenderblush", 0xfff0f5);
        colors.put("lawngreen", 0x7cfc00);
        colors.put("lemonchiffon", 0xfffacd);
        colors.put("lightblue", 0xadd8e6);
        colors.put("lightcoral", 0xf08080);
        colors.put("lightcyan", 0xe0ffff);
        colors.put("lightgoldenrodyellow", 0xfafad2);
        colors.put("lightgray", 0xd3d3d3);
        colors.put("lightgreen", 0x90ee90);
        colors.put("lightgrey", 0xd3d3d3);
        colors.put("lightpink", 0xffb6c1);
        colors.put("lightsalmon", 0xffa07a);
        colors.put("lightseagreen", 0x20b2aa);
        colors.put("lightskyblue", 0x87cefa);
        colors.put("lightslategray", 0x778899);
        colors.put("lightslategrey", 0x778899);
        colors.put("lightsteelblue", 0xb0c4de);
        colors.put("lightyellow", 0xffffe0);
        colors.put("lime", 0x00ff00);
        colors.put("limegreen", 0x32cd32);
        colors.put("linen", 0xfaf0e6);
        colors.put("magenta", 0xff00ff);
        colors.put("maroon", 0x800000);
        colors.put("mediumaquamarine", 0x66cdaa);
        colors.put("mediumblue", 0x0000cd);
        colors.put("mediumorchid", 0xba55d3);
        colors.put("mediumpurple", 0x9370db);
        colors.put("mediumseagreen", 0x3cb371);
        colors.put("mediumslateblue", 0x7b68ee);
        colors.put("mediumspringgreen", 0x00fa9a);
        colors.put("mediumturquoise", 0x48d1cc);
        colors.put("mediumvioletred", 0xc71585);
        colors.put("midnightblue", 0x191970);
        colors.put("mintcream", 0xf5fffa);
        colors.put("mistyrose", 0xffe4e1);
        colors.put("moccasin", 0xffe4b5);
        colors.put("navajowhite", 0xffdead);
        colors.put("navy", 0x000080);
        colors.put("oldlace", 0xfdf5e6);
        colors.put("olive", 0x808000);
        colors.put("olivedrab", 0x6b8e23);
        colors.put("orange", 0xffa500);
        colors.put("orangered", 0xff4500);
        colors.put("orchid", 0xda70d6);
        colors.put("palegoldenrod", 0xeee8aa);
        colors.put("palegreen", 0x98fb98);
        colors.put("paleturquoise", 0xafeeee);
        colors.put("palevioletred", 0xdb7093);
        colors.put("papayawhip", 0xffefd5);
        colors.put("peachpuff", 0xffdab9);
        colors.put("peru", 0xcd853f);
        colors.put("pink", 0xffc0cb);
        colors.put("plum", 0xdda0dd);
        colors.put("powderblue", 0xb0e0e6);
        colors.put("purple", 0x800080);
        colors.put("red", 0xff0000);
        colors.put("rosybrown", 0xbc8f8f);
        colors.put("royalblue", 0x4169e1);
        colors.put("saddlebrown", 0x8b4513);
        colors.put("salmon", 0xfa8072);
        colors.put("sandybrown", 0xf4a460);
        colors.put("seagreen", 0x2e8b57);
        colors.put("seashell", 0xfff5ee);
        colors.put("sienna", 0xa0522d);
        colors.put("silver", 0xc0c0c0);
        colors.put("skyblue", 0x87ceeb);
        colors.put("slateblue", 0x6a5acd);
        colors.put("slategray", 0x708090);
        colors.put("slategrey", 0x708090);
        colors.put("snow", 0xfffafa);
        colors.put("springgreen", 0x00ff7f);
        colors.put("steelblue", 0x4682b4);
        colors.put("tan", 0xd2b48c);
        colors.put("teal", 0x008080);
        colors.put("thistle", 0xd8bfd8);
        colors.put("tomato", 0xff6347);
        colors.put("turquoise", 0x40e0d0);
        colors.put("violet", 0xee82ee);
        colors.put("wheat", 0xf5deb3);
        colors.put("white", 0xffffff);
        colors.put("whitesmoke", 0xf5f5f5);
        colors.put("yellow", 0xffff00);
        colors.put("yellowgreen", 0x9acd32);
    }

}
