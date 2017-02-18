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
package lc.hex.mopm.common;

import java.net.InetAddress;
import java.util.Optional;
import java.util.function.Consumer;

import lc.hex.mopm.api.common.LookupType;
import lc.hex.mopm.api.common.ProxyBlacklist;

public class LookupRequest {
    private final InetAddress address;
    private final LookupType type;
    private final Consumer<Optional<ProxyBlacklist>> callback;

    public LookupRequest(InetAddress address, LookupType type, Consumer<Optional<ProxyBlacklist>> callback) {
        this.address = address;
        this.type = type;
        this.callback = callback;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Consumer<Optional<ProxyBlacklist>> getCallback() {
        return callback;
    }

    public LookupType getType() {
        return type;
    }
}
