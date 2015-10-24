package me.lake.live4danmaku.model.danmaku;

import java.util.Comparator;

/**
 * Created by Lakeinchina(lakeinchina@hotmail.com) on 2015/10/21.
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
public class DanmakuTimeComparator implements Comparator<BaseDanmaku> {
    @Override
    public int compare(BaseDanmaku lhs, BaseDanmaku rhs) {
        long res = lhs.getAppearTimestamp() - rhs.getAppearTimestamp();
        if (res > 0) {
            return 1;
        } else if (res < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
