package me.lake.live4danmaku.tools;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/20.
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
public class DanmakuLog {
    protected static final String TAG = "DanmakuLog";
    private static boolean enableLog = true;

    public static boolean isEnableLog() {
        return enableLog;
    }

    public static void setEnableLog(boolean enableLog) {
        DanmakuLog.enableLog = enableLog;
    }

    public static void e(String tag, String content) {
        if (!enableLog) {
            return;
        }
        Log.e(TAG, "tag:" + tag + "," + content + "  time: " + System.currentTimeMillis());
    }

    public static void debug(String content) {
        if (!enableLog) {
            return;
        }
        Log.d(TAG, content + "  " + "time: " + System.currentTimeMillis());
    }

    public static void debug(String tag, String content) {
        if (!enableLog) {
            return;
        }
        Log.d(TAG, "tag:" + tag + "," + content + "  " + "time: " + System.currentTimeMillis());
    }

    public static void trace(String msg) {
        if (!enableLog) {
            return;
        }
        trace(msg, new Throwable());
    }

    public static void trace(Throwable e) {
        if (!enableLog) {
            return;
        }
        trace(null, e);
    }

    public static void trace(String msg, Throwable e) {
        if (!enableLog) {
            return;
        }
        if (null == e || e instanceof UnknownHostException) {
            return;
        }

        final Writer writer = new StringWriter();
        final PrintWriter pWriter = new PrintWriter(writer);
        e.printStackTrace(pWriter);
        String stackTrace = writer.toString();
        if (null == msg || msg.equals("")) {
            msg = "================error!==================";
        }
        Log.e(TAG, "==================================");
        Log.e(TAG, msg);
        Log.e(TAG, stackTrace);
        Log.e(TAG, "-----------------------------------");
    }
}
