package me.lake.live4danmaku.model.cache;

import android.graphics.Bitmap;
import android.util.Size;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/23.
 * Live4Danmaku Project
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
public class PieceCacheManager implements ICacheManager {
    int maxSize;
    int currentSize;

    public PieceCacheManager(int maxSize) {
        this.maxSize = maxSize;
        currentSize = 0;
    }

    @Override
    public Cache requireCache(Size size) {
        Bitmap bitmap = null;
        if ((currentSize + size.getWidth() * size.getHeight()) < maxSize) {
            try {
                bitmap = Bitmap.createBitmap((int) size.getWidth(), (int) size.getHeight(), Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                return null;
            }
        }
        if (bitmap != null) {
            return new Cache(bitmap);
        } else {
            return null;
        }
    }

    @Override
    public void releaseCache(Cache cache) {
        if (cache.getBitmap() != null) {
            cache.getBitmap().recycle();
        }
    }
}
