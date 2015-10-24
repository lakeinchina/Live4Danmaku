package me.lake.live4danmaku.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.lake.live4danmaku.model.danmaku.BaseDanmaku;

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
public class SimpleDanmakuControler implements IDanmakuControler {
    float startYLine = 0f;
    float endYLine = 1f;
    ComparatorInt comparatorInt;

    public SimpleDanmakuControler() {
        comparatorInt = new ComparatorInt();
    }

    @Override
    public void setDisplayRegion(float startYLine, float endYLine) {
        this.endYLine = endYLine;
        this.startYLine = startYLine;
    }


    @Override
    public LinkedList<BaseDanmaku> layoutDanmakus(List<BaseDanmaku> dst, List<BaseDanmaku> src, int width, int height) {
        LinkedList<BaseDanmaku> res = new LinkedList<BaseDanmaku>();
        Iterator<BaseDanmaku> danmakuIterator = src.iterator();
        while (danmakuIterator.hasNext()) {
            BaseDanmaku danmaku = danmakuIterator.next();
            if (layoutDanmaku(dst, danmaku, width, height)) {
                dst.add(danmaku);
                res.add(danmaku);
                danmakuIterator.remove();
            } else {
                switch (danmaku.onCrowd()) {
                    case BaseDanmaku.CROWD_WAIT:
                        break;
                    case BaseDanmaku.CROWD_DELETE:
                        danmakuIterator.remove();
                        break;
                    case BaseDanmaku.CROWD_FORCE:
                        danmakuIterator.remove();
                        break;
                }
            }
        }
        return res;
    }

    @Override
    public void moveDanmakus(List<BaseDanmaku> danmakus) {
        Iterator<BaseDanmaku> danmakuIterator = danmakus.iterator();
        while (danmakuIterator.hasNext()) {
            BaseDanmaku danmaku = danmakuIterator.next();
            switch (danmaku.getMotionType()) {
                case BaseDanmaku.TYPE_MOTION_FLOW:
                    danmaku.setPoxX(danmaku.getPoxX() - danmaku.getSpeedPx());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public LinkedList<BaseDanmaku> checkOutDanmakus(List<BaseDanmaku> danmakus, int widht, int height) {
        LinkedList<BaseDanmaku> res = new LinkedList<BaseDanmaku>();
        Iterator<BaseDanmaku> danmakuIterator = danmakus.iterator();
        while (danmakuIterator.hasNext()) {
            BaseDanmaku danmaku = danmakuIterator.next();
            switch (danmaku.getMotionType()) {
                case BaseDanmaku.TYPE_MOTION_FLOW:
                    if ((danmaku.getPoxX() + danmaku.getWidth()) <= 0) {
                        danmakuIterator.remove();
                        res.add(danmaku);
                    }
                    break;
                default:
                    break;
            }
        }
        return res;
    }

    /**
     * 将弹幕加入显示弹幕列表中
     *
     * @param bDanmaku 将加入显示弹幕列表的弹幕
     */
    private boolean layoutDanmaku(List<BaseDanmaku> dst, BaseDanmaku bDanmaku, int widht, int height) {
        int YLocation = 0;// 加入弹幕的Y轴位置
        int left = 0, right = 0;// 32位int型左16位值和右16位值
        LinkedList<Integer> testBedList = new LinkedList<Integer>();
        // 将尚未走出右边界线的弹幕位置存入testBedList中，左16位为弹幕上边界y坐标，右16位为弹幕下边界y坐标
        for (BaseDanmaku bd : dst) {
            if (bd.getWidth() + bd.getPoxX() > widht) {
                left = (int) bd.getPoxY();
                right = (int) (left + bd.getHeight());
                testBedList.add(((left << 16) | right));
            }
        }
        testBedList.add((int) (height * startYLine));// 设置弹幕显示上限位置
        // 根据弹幕上边界y坐标排序
        Collections.sort(testBedList, comparatorInt);
        int prey = 0, cury = 0;
        // 获取宽度足够插入弹幕的y坐标
        for (int i = 0; i < testBedList.size(); i++) {
            if (i == 0) {
                prey = testBedList.get(0);
                YLocation = prey;
            } else {
                cury = testBedList.get(i);
                if ((cury >> 16) - (prey & 0x0000ffff) >= bDanmaku.getHeight()) {
                    YLocation = prey & 0x0000ffff;
                    break;
                } else {
                    YLocation = cury & 0x0000ffff;
                }
                prey = cury;
            }
        }
        // 如果y坐标超出弹幕显示下限位置则不加入
        if ((YLocation + bDanmaku.getHeight()) > endYLine * height) {
            return false;
        }
        bDanmaku.setPoxY((float) YLocation);
        bDanmaku.setPoxX((float) widht);
        return true;
    }

    private class ComparatorInt implements Comparator<Integer> {
        @Override
        public int compare(Integer lhs, Integer rhs) {
            return lhs - rhs;
        }
    }
}
