package me.lake.live4danmaku.tools;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/23.
 * Live4Danmaku Project
 *
 * Copyright (C) 2015 Po Hu <lakeinchina@hotmail.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
public class PaintTools {
    static TextPaint textPaint;
    static Paint paint;

    static {
        textPaint = new TextPaint();
        textPaint.setTextSize(UITools.getPixels(TypedValue.COMPLEX_UNIT_SP, 14));
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setAntiAlias(true);
        textPaint.setShadowLayer(2.0f, 2, 2, Color.BLACK);

        paint = new Paint();
        paint.setColor(Color.argb(100, 100, 100, 100));
        textPaint.setAntiAlias(true);
    }

    public static TextPaint getStandardTextPaint() {
        return textPaint;
    }

    public static Paint getStandardPaint() {
        return paint;
    }
}
