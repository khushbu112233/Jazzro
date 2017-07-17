package com.jlouistechnology.Jazzro.Helper;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by aipxperts on 8/4/16.
 */
public  class FontCustom {

    static public Typeface setTitleFont(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/FREESCPT.TTF");
        return font;
    }



    static public Typeface setFontOpenSansBold(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Bold.ttf");
        return font;
    }
    static public Typeface setFontOpenSansBoldItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-BoldItalic.ttf");
        return font;
    }
    static public Typeface setFontOpenSansExtraBold(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-ExtraBold.ttf");
        return font;
    }
    static public Typeface setFontOpenSansExtraBoldItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-ExtraBoldItalic.ttf");
        return font;
    }
    static public Typeface setFontOpenSansItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Italic.ttf");
        return font;
    }
    static public Typeface setFontOpenSansLight(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Light.ttf");
        return font;
    }
    static public Typeface setFontOpenSansLightItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-LightItalic.ttf");
        return font;
    }
    static public Typeface setFontOpenSansRegular(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        return font;
    }
    static public Typeface setFontOpenSansSemibold(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Semibold.ttf");
        return font;
    }

    static public Typeface setFontOpenSansSemiboldItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-SemiboldItalic.ttf");
        return font;
    }
   /* static public Typeface setFontLightItalic(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-LightItalic.ttf");
        return font;
    }
*/


}


