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

import java.net.InetAddress;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ProxyBlacklist implements Predicate<InetAddress> {
    private final BiPredicate<String, InetAddress> matcher;
    private String rootAddress;
    private String name;
    private String defaultCommand;

    public ProxyBlacklist(String rootAddress, String name, BiPredicate<String, InetAddress> matcher, String defaultCommand) {
        this.rootAddress = rootAddress;
        this.name = name;
        this.matcher = matcher;
        this.defaultCommand = defaultCommand;
    }

    public String getRootAddress() {
        return rootAddress;
    }

    public boolean test(InetAddress address) {
        return matcher.test(this.rootAddress, address);
    }

    public String getDefaultCommand() {
        return defaultCommand;
    }

    public String getName() {
        return name;
    }
}
