package me.lake.live4danmaku.tools;

import android.graphics.Canvas;
import android.text.TextPaint;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/24.
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
public class FPSTools {
    static long lastTime;
    static String lastFps;
    static long lastUpdateTime;
    static final long timetravel = 100;

    public static void drawFPS(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - lastUpdateTime) > timetravel) {
            lastUpdateTime = now;
            String fps = String.valueOf(1000.0 / (now - lastTime));
            lastFps = fps.substring(0, fps.indexOf(".") + 2);
            DanmakuLog.debug("time="+(1000.0 / (now - lastTime)));
        }
        TextPaint textPaint = PaintTools.getStandardTextPaint();
        canvas.drawText(lastFps, 0, -textPaint.ascent(), textPaint);
        lastTime = now;
    }
}
