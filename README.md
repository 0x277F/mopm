MOPM
====
(Minecraft Open Proxy Monitor)

This is a plugin (designed in the spirit of the classic IRC service BOPM) to query common and configurable DNS blacklists and thus prevent people from connecting to your server via open proxy.
Blacklists are configurable as long as the follow the standard system for querying (if you don't know what this is, then don't add one to the config).
Plugins can hook into a simple event-driven API to perform their own custom actions when an open proxy is detected.

Planned Features
----------------
BungeeCord support (allow installation as a bungee plugin)
Non-standard DNSBL queries