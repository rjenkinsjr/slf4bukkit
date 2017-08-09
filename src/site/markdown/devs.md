## Copyright (C) 2016-2017 Ronald Jack Jenkins Jr.
## 
## This program is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <http://www.gnu.org/licenses/>.
#set($h1 = '#')
#set($h2 = '##')
#set($h3 = '###')
#set($h4 = '####')
#set($h5 = '#####')
#set($h6 = '######')
${project.name} for Plugin Developers
---

If you wish to use [SLF4J](http://slf4j.org) in your Bukkit plugin, or if your plugin has a dependency on a library that uses SLF4J:

+ Reference the [rjenkinsjr Maven2 Repository](https://github.com/rjenkinsjr/maven2) in your POM.
+ [Shade](https://maven.apache.org/plugins/maven-shade-plugin/usage.html) SLF4Bukkit into your plugin project:

```xml
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>${project.artifactId}</artifactId>
  <version>${project.version}</version>
</dependency>
```

+ (Optional) Add your desired default configuration values to your plugin's built-in [config.yml](index.html) file. For more details, see the Javadocs for the [BukkitLoggerAdapter](apidocs/org/slf4j/impl/BukkitLoggerAdapter.html) class.
+ (Optional) Use the [SLF4J API](http://www.slf4j.org/api/org/slf4j/Logger.html) in your code.
    + SLF4Bukkit supports only [ColorMarkers](apidocs/info/ronjenkins/slf4bukkit/ColorMarker.html), which format the entire message and associated throwable (if any). All other markers are discarded. ColorMarkers always override the default level-specific formatting defined in the plugin config.
    + In addition to using ColorMarkers, you can use Bukkit's `ChatColor` values to further format your message.
    + SLF4Bukkit issues `ChatColor.RESET` after every log message, so you don't have to worry about resetting after each message.
    + You can use the [ColorString](apidocs/info/ronjenkins/slf4bukkit/ColorString.html) class to easily create colored log messages.
    + For a consistent user experience, it's recommended that you perform all logging via SLF4Bukkit and not use `Plugin.getLogger()`.
    + Any color-related features are silently stripped/ignored when SLF4Bukkit runs on a Bukkit implementation where JAnsi is not available (e.g. PaperSpigot).
