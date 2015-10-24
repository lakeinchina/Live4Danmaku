package me.lake.live4danmaku.core;

import java.util.LinkedList;
import java.util.List;

import me.lake.live4danmaku.model.danmaku.BaseDanmaku;

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
public class StandardDanmakuControler implements IDanmakuControler {
    @Override
    public LinkedList<BaseDanmaku> layoutDanmakus(List<BaseDanmaku> dst, List<BaseDanmaku> src, int width, int height) {
        return null;
    }

    @Override
    public void moveDanmakus(List<BaseDanmaku> danmakus) {

    }

    @Override
    public void setDisplayRegion(float startYLine, float endYLine) {

    }


    @Override
    public LinkedList<BaseDanmaku> checkOutDanmakus(List<BaseDanmaku> danmakus, int widht, int height) {
        return null;
    }

}