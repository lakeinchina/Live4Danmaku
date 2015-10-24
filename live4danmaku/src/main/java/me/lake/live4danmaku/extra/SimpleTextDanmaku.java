package me.lake.live4danmaku.extra;

import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.TypedValue;

import me.lake.live4danmaku.model.danmaku.drawdanmaku.StaticDrawDanmaku;
import me.lake.live4danmaku.model.others.SizeF;
import me.lake.live4danmaku.tools.PaintTools;
import me.lake.live4danmaku.tools.TextRuler;
import me.lake.live4danmaku.tools.UITools;

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
public class SimpleTextDanmaku extends StaticDrawDanmaku {
    private String text;

    public SimpleTextDanmaku(String text, long appearTimestamp) {
        super(appearTimestamp);
        this.text = text;
    }

    @Override
    public void onMeasure() {
//        DanmakuLog.debug("onMeasure");
        SizeF sizef = TextRuler.calcTextWH(text, PaintTools.getStandardTextPaint());
        width = Math.round(sizef.getWidth() + 0.5f);
        height = Math.round(sizef.getHeight() + 0.5f);
        speedPx = UITools.getPixels(TypedValue.COMPLEX_UNIT_DIP, 1) + (int) Math.sqrt(width / 100);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        DanmakuLog.debug("onDraw");
        super.onDraw(canvas);
        TextPaint paint = PaintTools.getStandardTextPaint();
        canvas.drawRect(0, 0, width, height, PaintTools.getStandardPaint());
        canvas.drawText(text, 0, -paint.ascent(), paint);
    }

    @Override
    public boolean onPrepare() {
//        DanmakuLog.debug("onPrepare");
        return super.onPrepare();
    }

    @Override
    public int onCrowd() {
//        DanmakuLog.debug("onCrowd");
        return super.onCrowd();
    }

    @Override
    public void onAppear() {
//        DanmakuLog.debug("onAppear");
        super.onAppear();
    }

    @Override
    public void onDestroy() {
//        DanmakuLog.debug("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDisappear() {
//        DanmakuLog.debug("onDisappear");
        super.onDisappear();
    }
}
