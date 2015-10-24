package me.lake.live4danmaku.model.danmaku;

import android.graphics.Canvas;

import me.lake.live4danmaku.model.others.Position;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/16.
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
public class BaseDanmaku {
    public static final int CROWD_DELETE = 0;
    public static final int CROWD_FORCE = 1;
    public static final int CROWD_WAIT = 2;
    public static final int TYPE_MOTION_FLOW = 0;
    public static final int TYPE_MOTION_TOP = 1;
    public static final int TYPE_MOTION_BOTTOM = 2;
    protected int motionType = TYPE_MOTION_FLOW;
    protected int width;
    protected int height;
    protected long appearTimestamp;
    protected boolean isShowing;
    protected float speedPx;
    protected float  poxX;
    protected float  poxY;
    protected long survivalTime;

    protected BaseDanmaku(long appearTimestamp) {
        this.appearTimestamp = appearTimestamp;
    }

    /**
     * 测量，返回这个弹幕的大小
     */
    public void onMeasure() {
        width = 0;
        height = 0;
        speedPx = 10;
    }


    public void onDraw(Canvas canvas) {

    }

    /**
     * 准备展示，如果不想展示这条弹幕请返回false
     *
     * @return
     */
    public boolean onPrepare() {
        return true;
    }

    /**
     * 即将展示
     */
    public void onAppear() {
    }

    /**
     * 暂时拥挤，没有地方可以插入弹幕
     *
     * @return
     */
    public int onCrowd() {
        return CROWD_DELETE;
    }

    /**
     * 即将消失
     */
    public void onDisappear() {
    }

    /**
     * 销毁
     */
    public void onDestroy() {

    }

    public int getMotionType() {
        return motionType;
    }

    public void setMotionType(int motionType) {
        this.motionType = motionType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getAppearTimestamp() {
        return appearTimestamp;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

    public float getSpeedPx() {
        return speedPx;
    }

    public void setSpeedPx(float speedPx) {
        this.speedPx = speedPx;
    }

    public float getPoxX() {
        return poxX;
    }

    public void setPoxX(float poxX) {
        this.poxX = poxX;
    }

    public float getPoxY() {
        return poxY;
    }

    public void setPoxY(float poxY) {
        this.poxY = poxY;
    }

    public long getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(long survivalTime) {
        this.survivalTime = survivalTime;
    }
}
