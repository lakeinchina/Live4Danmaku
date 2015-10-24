package me.lake.live4danmaku.core;

import android.graphics.Canvas;

import java.util.Iterator;
import java.util.List;

import me.lake.live4danmaku.model.danmaku.BaseDanmaku;
import me.lake.live4danmaku.model.others.Position;
import me.lake.live4danmaku.tools.DanmakuLog;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/23.
 * Live4Danmaku Project
 * just for test
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
public class NoCacheDanmakuDisplayer implements IDanmakuDisplayer {

    public NoCacheDanmakuDisplayer() {

    }

    @Override
    public void drawDanmakus(Canvas canvas, List<BaseDanmaku> danmakus, int width, int height) {
        Iterator<BaseDanmaku> danmakuIterator = danmakus.iterator();
        BaseDanmaku danmakuToShow;
        while (danmakuIterator.hasNext()) {
            danmakuToShow = danmakuIterator.next();
            float x = danmakuToShow.getPoxX();
            float y = danmakuToShow.getPoxY();
            canvas.save();
            canvas.translate(x, y);
            try {
                danmakuToShow.onDraw(canvas);
            } catch (Throwable e) {
                DanmakuLog.trace("onDraw", e);
            }
            canvas.restore();
        }
    }
}
