package me.lake.live4danmaku.model.danmaku.drawdanmaku;

import android.graphics.Canvas;

import me.lake.live4danmaku.model.cache.Cache;
import me.lake.live4danmaku.model.danmaku.BaseDanmaku;

/**
 * Created by Lake on 2015/10/20.
 */
public class StaticDrawDanmaku extends BaseDanmaku {
    private Cache drawCache;

    public Cache getDrawCache() {
        return drawCache;
    }

    public void setDrawCache(Cache drawCache) {
        this.drawCache = drawCache;
    }

    public boolean hasCache() {
        if (drawCache == null) {
            return false;
        } else {
            return true;
        }
    }

    protected StaticDrawDanmaku(long appearTimestamp) {
        super(appearTimestamp);
    }
}
