package me.lake.live4danmaku.core;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Iterator;
import java.util.List;

import me.lake.live4danmaku.model.cache.Cache;
import me.lake.live4danmaku.model.cache.ICacheManager;
import me.lake.live4danmaku.model.danmaku.BaseDanmaku;
import me.lake.live4danmaku.model.danmaku.drawdanmaku.DynamicDrawDanmaku;
import me.lake.live4danmaku.model.danmaku.drawdanmaku.SemiDynamicDrawDanmaku;
import me.lake.live4danmaku.model.danmaku.drawdanmaku.StaticDrawDanmaku;
import me.lake.live4danmaku.model.others.Size;
import me.lake.live4danmaku.tools.DanmakuLog;

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
public class StandardDanmakuDisplayer implements IDanmakuDisplayer {
    ICacheManager cacheManager;
    Rect srcRect;
    Rect dstRect;

    public StandardDanmakuDisplayer(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
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
            if (danmakuToShow instanceof StaticDrawDanmaku) {
                drawStaticDrawDanmaku(canvas, (StaticDrawDanmaku) danmakuToShow);
            } else if (danmakuToShow instanceof SemiDynamicDrawDanmaku) {

            } else if (danmakuToShow instanceof DynamicDrawDanmaku) {
                try {
                    danmakuToShow.onDraw(canvas);
                } catch (Throwable e) {
                    DanmakuLog.trace("onDraw", e);
                }
            }
            canvas.restore();
        }
    }

    private void drawStaticDrawDanmaku(Canvas canvas, StaticDrawDanmaku staticDrawDanmaku) {
        if (!staticDrawDanmaku.hasCache()) {
            Cache cache = cacheManager.requireCache(new Size(staticDrawDanmaku.getWidth(), staticDrawDanmaku.getHeight()));
            if (cache != null) {
                DanmakuLog.debug("buildCache");
                drawCache(cache, staticDrawDanmaku);
                staticDrawDanmaku.setDrawCache(cache);
            }
        }
        if (staticDrawDanmaku.hasCache()) {
            DanmakuLog.debug("drawCache");
            Cache drawCache = staticDrawDanmaku.getDrawCache();
            if (drawCache.getBitmap() != null) {
                canvas.drawBitmap(drawCache.getBitmap(), 0, 0, null);
            } else {
                ICacheManager.BitmapClip bitmapClip = cacheManager.getBitmapClipByCacheId(drawCache.getId());
                srcRect.set(0, 0, bitmapClip.size.getWidth(), bitmapClip.size.getHeight());
                dstRect.set(0, 0, bitmapClip.size.getWidth(), bitmapClip.size.getHeight());
                canvas.drawBitmap(bitmapClip.bitmap, srcRect, dstRect, null);
            }
        } else {
            staticDrawDanmaku.onDraw(canvas);
        }
    }

    private void drawCache(Cache cache, StaticDrawDanmaku staticDrawDanmaku) {
        Canvas canvas = null;
        if (cache.getBitmap() != null) {
            canvas = new Canvas(cache.getBitmap());
        } else {
            ICacheManager.BitmapClip bitmapClip = cacheManager.getBitmapClipByCacheId(cache.getId());
            canvas = new Canvas(bitmapClip.bitmap);
            canvas.translate(bitmapClip.pos.getX(), bitmapClip.pos.getY());
        }
        staticDrawDanmaku.onDraw(canvas);
    }

}
