/*
 * Copyright 2017 Hex <hex@hex.lc>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lc.hex.mopm.api.common;

import java.util.List;

public class MopmAPI {
    static IMopmPlugin plugin;

    public static void setPlugin(IMopmPlugin plugin) {
        MopmAPI.plugin = plugin;
    }

    public static boolean isEnabled() {
        return plugin.isEnabled();
    }

    public static MopmConfiguration getConfiguration() {
        return plugin.getConfiguration();
    }

    public static List<ProxyBlacklist> getAllBlacklists() {
        return plugin.getAllProxyBlacklists();
    }

    public static ThreadMagic getThreadMagic() {
        return plugin;
    }
}
