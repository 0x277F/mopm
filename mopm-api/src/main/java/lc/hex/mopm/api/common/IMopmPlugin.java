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
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMopmPlugin extends ThreadMagic {
    boolean isEnabled();

    @Nonnull
    ILookupBoss getLookupBoss();

    @Nonnull
    List<ProxyBlacklist> getAllProxyBlacklists();

    void addProxyBlacklist(@Nullable ProxyBlacklist blacklist);

    @Nonnull
    Logger getLogger();

    @Nonnull
    MopmConfiguration getConfiguration();

    @Nonnull
    ThreadMagic getThreadMagic();
}
