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
${project.name}
===
${project.description}
---
### Server Owners
If you were linked here by documentation for a plugin that you're using on your Bukkit/Spigot server, [go here](${project.url}) to learn what configuration options you have available (and please ask the plugin developer to fix their links!).

### Plugin Developers
If you wish to use [SLF4J](http://slf4j.org) in your Bukkit plugin, or if your plugin has a dependency on a library that uses SLF4J:

+ [Shade](https://maven.apache.org/plugins/maven-shade-plugin/usage.html) SLF4Bukkit into your plugin project:

```xml
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>${project.artifactId}</artifactId>
  <version>${project.version}</version>
</dependency>
```

+ (Optional) Add your desired default configuration values to your plugin's built-in [config.yml](${project.url}) file. For more details, see the Javadocs for the [BukkitPluginLoggerAdapter](${project.url}/apidocs/org/slf4j/impl/BukkitPluginLoggerAdapter.html) class.
+ (Optional) Use the [SLF4J API](http://www.slf4j.org/api/org/slf4j/Logger.html) in your code. Note that SLF4Bukkit does not support markers.
