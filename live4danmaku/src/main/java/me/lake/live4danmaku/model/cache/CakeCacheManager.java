package me.lake.live4danmaku.model.cache;

import android.graphics.Bitmap;
import android.util.Size;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/20.
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
public class CakeCacheManager implements ICacheManager {
    private Bitmap[] bitmapCakes;
    private Size cakeSize;
    private int extraCacheSize;

    CakeCacheManager(Size cakeSize, int cakeNum) throws OutOfMemoryError {
        init(cakeSize, cakeNum, 0);
    }

    CakeCacheManager(Size cakeSize, int cakeNum, int extraCacheSize) throws OutOfMemoryError {
        init(cakeSize, cakeNum, extraCacheSize);
    }

    private void init(Size cakeSize, int cakeNum, int extraCacheSize) throws OutOfMemoryError {
        this.extraCacheSize = extraCacheSize;
        if (0 == cakeNum || 0 == cakeSize.getHeight() || 0 == cakeSize.getWidth()) {
            bitmapCakes = null;
            this.cakeSize = null;
        } else {
            bitmapCakes = new Bitmap[cakeNum];
            this.cakeSize = cakeSize;
            try {
                for (int i = 0; i < cakeNum; i++) {
                    bitmapCakes[i] = Bitmap.createBitmap(cakeSize.getWidth(), cakeSize.getHeight(), Bitmap.Config.ARGB_8888);
                }
            } catch (OutOfMemoryError outOfMemoryError) {
                for (int i = 0; i < cakeNum; i++) {
                    if (bitmapCakes[i] != null) {
                        bitmapCakes[i].recycle();
                        bitmapCakes[i] = null;
                    }
                }
                throw outOfMemoryError;
            }
        }
    }

    @Override
    public Cache requireCache(Size size) {
        return null;
    }

    @Override
    public void releaseCache(Cache cache) {

    }
}
