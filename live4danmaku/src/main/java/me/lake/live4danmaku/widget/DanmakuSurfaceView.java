package me.lake.live4danmaku.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import me.lake.live4danmaku.core.IDanmakuControler;
import me.lake.live4danmaku.core.IDanmakuDisplayer;
import me.lake.live4danmaku.core.NoCacheDanmakuDisplayer;
import me.lake.live4danmaku.core.SimpleDanmakuControler;
import me.lake.live4danmaku.model.Timepiece.ITimepiece;
import me.lake.live4danmaku.model.Timepiece.UnixTimepiece;
import me.lake.live4danmaku.model.danmaku.BaseDanmaku;
import me.lake.live4danmaku.model.danmaku.DanmakuTimeComparator;
import me.lake.live4danmaku.tools.DanmakuLog;
import me.lake.live4danmaku.tools.FPSTools;

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
public class DanmakuSurfaceView extends SurfaceView implements android.view.SurfaceHolder.Callback {

    public DanmakuSurfaceView(Context context) {
        super(context);
        init();
    }

    public DanmakuSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DanmakuSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private SurfaceHolder surfaceHolder;
    private TreeSet<BaseDanmaku> prepareingDanmakus;
    private LinkedList<BaseDanmaku> displayingDanmakus;
    private LinkedList<BaseDanmaku> destroyingDanmakus;
    private HandlerThread drawThread;
    private DrawHandler drawHandler;
    private HandlerThread prepareThread;
    private PrepareHandler prepareHandler;
    private ITimepiece timepiece;
    private IDanmakuDisplayer danmakuDisplayer;
    private IDanmakuControler danmakuControler;
    private int width;
    private int height;
    private int maxFPS = FPS_STANDARD;
    private boolean showFPS = false;
    private static final int FPS_UPPER_LIMIT = 240;
    private static final int FPS_STANDARD = 60;
    private static final int FPS_LOWER_LIMIT = 1;

    private void init() {
        if (!this.isInEditMode()) {
            this.setZOrderMediaOverlay(true);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
            this.setWillNotCacheDrawing(true);
            this.setDrawingCacheEnabled(false);
            this.setWillNotDraw(true);
            prepareingDanmakus = new TreeSet<BaseDanmaku>(new DanmakuTimeComparator());
            displayingDanmakus = new LinkedList<BaseDanmaku>();
            destroyingDanmakus = new LinkedList<BaseDanmaku>();
            prepareThread = new HandlerThread("DanmakuPrepareThread", Process.THREAD_PRIORITY_LESS_FAVORABLE);
            prepareThread.start();
            drawThread = new HandlerThread("DanmakuDrawThread", android.os.Process.THREAD_PRIORITY_DEFAULT);
            drawThread.start();
            drawHandler = new DrawHandler(drawThread.getLooper());
            prepareHandler = new PrepareHandler(prepareThread.getLooper());
            timepiece = new UnixTimepiece();
        }
    }

    private void create() {
        if (!this.isInEditMode()) {
            drawHandler.sendEmptyMessage(DrawHandler.WHAT_INIT);
        }
    }

    private void destroy() {
        if (prepareHandler != null) {
            prepareHandler.quit();
            prepareHandler = null;
        }
        if (prepareThread != null) {
            prepareThread.quit();
            try {
                prepareThread.join();
            } catch (Exception e) {
                DanmakuLog.trace("prepareThread.join()", e);
            }
            prepareThread = null;
        }
        if (drawHandler != null) {
            drawHandler.quit();
            drawHandler = null;
        }
        if (drawThread != null) {
            drawThread.quit();
            try {
                drawThread.join();
            } catch (Exception e) {
                DanmakuLog.trace("prepareThread.join()", e);
            }
            drawThread = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        create();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroy();
    }

    private void drawDanmakus() {
        Surface surface = surfaceHolder.getSurface();
        if (surface == null || surface.isValid() == false) {
            return;
        }
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
        } catch (Throwable e) {
        }
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (null != danmakuDisplayer) {
            danmakuDisplayer.drawDanmakus(canvas, displayingDanmakus, width, height);
        }
        if (showFPS) {
            FPSTools.drawFPS(canvas);
        }
        try {
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Throwable e) {
        }
    }

    //===============================handler===============================
    public class DrawHandler extends Handler {
        private final static int WHAT_INIT = 1;
        private final static int WHAT_UPDATE = 2;
        private final static int WHAT_INCOMING_FIRE = 3;
        private final static int TIME_INTERVAL_CHECKOUTDANMAKU = 500;
        private long lastCheckOutDanmakuTime = 0;
        private int noneDanmakusFlag = 0;
        private boolean quitFlag = false;
        private LinkedList<BaseDanmaku> danmakusBuffer;

        DrawHandler(Looper looper) {
            super(looper);
            danmakusBuffer = new LinkedList<BaseDanmaku>();
        }

        public void quit() {
            quitFlag = true;
            this.removeCallbacksAndMessages(null);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INIT:
//                    danmakuDisplayer = new StandardDanmakuDisplayer(new PieceCacheManager(1024 * 1024));
//                    danmakuControler = new StandardDanmakuControler();
                    danmakuDisplayer = new NoCacheDanmakuDisplayer();
                    danmakuControler = new SimpleDanmakuControler();
                    danmakuControler.setDisplayRegion(0f, 0.5f);
                    sendEmptyMessage(WHAT_UPDATE);
                    prepareHandler.sendEmptyMessage(PrepareHandler.WHAT_FIRE_DANMAKUS);
                    break;
                case WHAT_UPDATE:
                    long inTime = System.currentTimeMillis();
                    DanmakuLog.debug("WHAT_UPDATE");
                    //绘制弹幕
                    if (displayingDanmakus.size() == 0) {
                        noneDanmakusFlag--;
                    }
                    if (noneDanmakusFlag >= 0) {
                        danmakuControler.moveDanmakus(displayingDanmakus);
                    } else {
                    }
                    drawDanmakus();
                    //取出弹幕缓冲
                    if (!danmakusBuffer.isEmpty()) {
                        LinkedList<BaseDanmaku> firedDanmakus = danmakuControler.layoutDanmakus(displayingDanmakus, danmakusBuffer, width, height);
                        if (null != firedDanmakus && 0 != firedDanmakus.size()) {
                            for (BaseDanmaku firedDanmaku : firedDanmakus) {
                                firedDanmaku.onAppear();
                            }
                            noneDanmakusFlag = 1;
                        }
                    }
                    //检查删除弹幕
                    if ((inTime - lastCheckOutDanmakuTime) > TIME_INTERVAL_CHECKOUTDANMAKU) {
                        LinkedList<BaseDanmaku> destroyDanmakus = danmakuControler.checkOutDanmakus(displayingDanmakus, width, height);
                        if (destroyDanmakus != null) {
                            for (BaseDanmaku baseDanmaku : destroyDanmakus) {
                                try {
                                    baseDanmaku.onDisappear();
                                } catch (Exception e) {
                                    DanmakuLog.trace("CHECK_DESTROY,onDisappear", e);
                                }
                            }
                            prepareHandler.sendMessage(prepareHandler.obtainMessage(PrepareHandler.WHAT_DESTROY_DANMAKUS, destroyDanmakus));
                        }
                        lastCheckOutDanmakuTime = inTime;
                    }
                    int ElapsedTime = (int) (System.currentTimeMillis() - inTime);
                    int tfps = noneDanmakusFlag >= 0 ? maxFPS : FPS_LOWER_LIMIT;
                    int delay = (1000 / tfps) - ElapsedTime;
//                    DanmakuLog.debug("delay=" + delay);
                    if (delay > 0) {
                        this.sendEmptyMessageDelayed(WHAT_UPDATE, delay);
                    } else {
                        this.sendEmptyMessage(WHAT_UPDATE);
                    }
                    break;
                case WHAT_INCOMING_FIRE:
                    List<BaseDanmaku> fireDanmakus = (List<BaseDanmaku>) msg.obj;
                    danmakusBuffer.addAll(fireDanmakus);
                    noneDanmakusFlag = 1;
                    break;
                default:
                    break;
            }
        }
    }

    public class PrepareHandler extends Handler {
        private final static int WHAT_ADD_DANMAKU = 1;
        private final static int WHAT_ADD_DANMAKUS = 2;
        private final static int WHAT_FIRE_DANMAKUS = 3;
        private final static int WHAT_DESTROY_DANMAKUS = 4;
        private final static int TIME_INTERVAL_FIREDANMAKU = 100;
        private final static int TIME_INTERVAL_DESTROYDANMAKU = 500;
        private boolean quitFlag = false;

        PrepareHandler(Looper looper) {
            super(looper);
        }

        public void quit() {
            quitFlag = true;
            this.removeCallbacksAndMessages(null);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_ADD_DANMAKU:
                    BaseDanmaku danmaku = (BaseDanmaku) msg.obj;
                    prepareingDanmakus.add(danmaku);
                    break;
                case WHAT_ADD_DANMAKUS:
                    Collection<BaseDanmaku> danmakus = (Collection<BaseDanmaku>) msg.obj;
                    prepareingDanmakus.addAll(danmakus);
                    break;
                case WHAT_FIRE_DANMAKUS:
                    long now = timepiece.getParallelTimestamp();
                    BaseDanmaku tempDanmaku;
                    LinkedList<BaseDanmaku> fireDanmakus = null;
                    while (null != (tempDanmaku = prepareingDanmakus.pollFirst())) {
                        if (tempDanmaku.getAppearTimestamp() > now) {
                            prepareingDanmakus.add(tempDanmaku);
                            break;
                        } else {
                            if (fireDanmakus == null) {
                                fireDanmakus = new LinkedList<BaseDanmaku>();
                            }
                            boolean shouldShow = false;
                            try {
                                shouldShow = tempDanmaku.onPrepare();
                            } catch (Throwable e) {
                                DanmakuLog.trace("FIRE_DANMAKUS,onPrepare", e);
                            }
                            try {
                                tempDanmaku.onMeasure();
                            } catch (Throwable e) {
                                DanmakuLog.trace("FIRE_DANMAKUS,onMeasure", e);
                            }
                            if (shouldShow) {
                                fireDanmakus.addFirst(tempDanmaku);
                            }
                        }
                    }
                    if (fireDanmakus != null) {
                        Message incomingFireMsg = drawHandler.obtainMessage(DrawHandler.WHAT_INCOMING_FIRE);
                        incomingFireMsg.obj = fireDanmakus;
                        drawHandler.sendMessage(incomingFireMsg);
                    }
                    if (!quitFlag) {
                        this.sendEmptyMessageDelayed(PrepareHandler.WHAT_FIRE_DANMAKUS, PrepareHandler.TIME_INTERVAL_FIREDANMAKU);
                    }
                    break;
                case WHAT_DESTROY_DANMAKUS:
                    LinkedList<BaseDanmaku> destroyDanmakus = (LinkedList<BaseDanmaku>) msg.obj;
                    BaseDanmaku destroyDanmaku;
                    while ((destroyDanmaku = destroyDanmakus.pollLast()) != null) {
                        destroyDanmaku.onDestroy();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //==============================================================
    public void setTimepiece(ITimepiece timepiece) {
        this.timepiece = timepiece;

    }

    public void addDanmaku(BaseDanmaku danmaku) {
        if (danmaku != null && prepareHandler != null) {
            Message msg = Message.obtain(prepareHandler);
            msg.what = PrepareHandler.WHAT_ADD_DANMAKU;
            msg.obj = danmaku;
            prepareHandler.sendMessage(msg);
        }
    }

    public void addDanmakus(Collection<BaseDanmaku> danmakus) {
        if (danmakus != null && prepareHandler != null) {
            Message msg = Message.obtain(prepareHandler);
            msg.what = PrepareHandler.WHAT_ADD_DANMAKUS;
            msg.obj = danmakus;
            prepareHandler.sendMessage(msg);
        }
    }

    public void setMaxFPS(int maxFPS) {
        this.maxFPS = maxFPS >= FPS_LOWER_LIMIT ? (maxFPS <= FPS_UPPER_LIMIT ? maxFPS : FPS_UPPER_LIMIT) : FPS_LOWER_LIMIT;
    }

    public void showFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }
}
