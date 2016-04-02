<!---
  Copyright (C) 2016 Ronald Jack Jenkins Jr.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->
SLF4Bukkit
===
SLF4Bukkit is an SLF4J binding that uses Bukkit's plugin logging system.

### Server Administrators
If you were linked here by documentation for a plugin that you're using on your Bukkit/Spigot server, [go here](http://rjenkinsjr.github.io/slf4bukkit) to learn what configuration options you have available (and please ask the plugin developer to fix their links!).

### Plugin Developers
If you wish to use [SLF4J](http://slf4j.org) in your Bukkit plugin, or if your plugin has a dependency on a library that uses SLF4J:

+ Reference the [rjenkinsjr Maven2 Repository](https://github.com/rjenkinsjr/maven2) in your POM.
+ [Shade](https://maven.apache.org/plugins/maven-shade-plugin/usage.html) SLF4Bukkit into your plugin project:

```xml
<dependency>
  <groupId>info.ronjenkins</groupId>
  <artifactId>slf4bukkit</artifactId>
  <version>0.1.2</version>
</dependency>
```

+ (Optional) Add your desired default configuration values to your plugin's built-in [config.yml](http://rjenkinsjr.github.io/slf4bukkit) file. For more details, see the Javadocs for the [BukkitPluginLoggerAdapter](http://rjenkinsjr.github.io/slf4bukkit/apidocs/org/slf4j/impl/BukkitPluginLoggerAdapter.html) class.
+ (Optional) Use the [SLF4J API](http://www.slf4j.org/api/org/slf4j/Logger.html) in your code.
    + SLF4Bukkit supports only [Bukkit formatting markers](http://rjenkinsjr.github.io/slf4bukkit/apidocs/info/ronjenkins/slf4bukkit/BukkitColorMarker.html), which format the entire message and associated throwable (if any). All other markers are discarded.
    + In addition to using the Bukkit formatting markers, you can use Bukkit's `ChatColor` values to further format your message.
    + SLF4Bukkit issues `ChatColor.RESET` after every log message, so you don't have to worry about resetting after each message.