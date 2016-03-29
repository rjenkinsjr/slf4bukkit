## Copyright (C) 2016 Ronald Jack Jenkins Jr.
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
${project.name} for Server Administrators
---

If you were linked here by documentation for a plugin that you're using on your Bukkit/Spigot server, you can control the logging behavior of the plugin using its `config.yml` file.

Basic documentation of all available options is shown below. Your plugin likely has additional information regarding what portions of the plugin's logging facilities are configurable.

```yaml
# This is the section within which all SLF4Bukkit configuration options is
# contained. Its name is the same regardless of what the plugin's name is.
slf4j:

  # Default log level for all plugin logging activity. Possible values are
  # "trace", "debug", "info", "warn", or "error".
  #
  # If the plugin logs any "trace" or "debug" messages, they will be logged by
  # the plugin as "info" severity, but you'll see the actual severity in the
  # log message. This is due to a Bukkit logging limitation.
  #
  # If not specified, default is "info".
  defaultLogLevel: info
  
  # Shows an "[SLF4J]" header for every message logged through SLF4Bukkit.
  #
  # If not specified, default is "false".
  showHeader: false
  
  # Shows the name of each thread that is logging via SLF4Bukkit. You probably
  # don't want this information unless you're helping troubleshoot a plugin.
  #
  # If not specified, default is "false".
  showThreadName: false
  
  # Shows the full logger name (e.g. "info.ronjenkins.bukkit.MyPlugin").
  #
  # If not specified, default is "false".
  showLogName: false
  
  # Shows the short logger name, which is the short Java package name format
  # (e.g. a logger named "info.ronjenkins.bukkit.MyPlugin" would have a short
  # name of "i.r.b.MyPlugin".
  #
  # If not specified, default is "true".
  showShortLogName: true
  
  # This section controls logging levels for individual loggers.
  log:
  
    # For each element in this section, the key is the full logger name and the
    # value is the logging level for that logger. Possible logging levels are
    # the same as what's available for the "slf4j.defaultLogLevel" property.
    #
    # The documentation for your plugin should elaborate on what logger names
    # are available. As a general rule, you won't need to specify levels for
    # specific loggers.
    info.ronjenkins.bukkit.MyPlugin: debug
    info.ronjenkins.SomeOtherLogger: warn
```
