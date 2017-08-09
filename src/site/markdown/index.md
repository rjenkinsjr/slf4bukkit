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
${project.name}
===
${project.description}

For Server Administrators
---

If you were linked here by documentation for a plugin that you're using on your Bukkit/Spigot server, you can control the logging behavior of the plugin using its `config.yml` file.

Basic documentation of all available options is shown below. Your plugin likely has additional information regarding what portions of the plugin's logging facilities are configurable.

```yaml
# This is the section within which all SLF4Bukkit configuration options are
# contained. Its name is the same regardless of what the plugin's name is.
slf4j:

  # Default log level for all plugin logging activity. Possible values are
  # "trace", "debug", "info", "warn", or "error" (case-insensitive).
  #
  # If the plugin logs any "trace" or "debug" messages, they will be logged by
  # the plugin as "info" severity, but you'll see the actual severity in the
  # log message. This is due to a Bukkit logging limitation.
  #
  # If not specified or given an invalid value, defaults to "info".
  defaultLogLevel: info
  
  # Shows an "[SLF4J]" header for every message logged through SLF4Bukkit.
  #
  # If not specified or given an invalid value, defaults to "false".
  showHeader: false
  
  # Shows the full logger name (e.g. "info.ronjenkins.bukkit.MyPlugin"),
  # wrapped in curly braces.
  #
  # If not specified or given an invalid value, defaults to "false". If true,
  # this overrides "slf4j.showShortLogName".
  showLogName: false
  
  # Shows the short logger name, wrapped in curly braces. The short logger name
  # is the short Java package name format (e.g. a logger named
  # "info.ronjenkins.bukkit.MyPlugin" would have a short name of
  # "i.r.b.MyPlugin").
  #
  # If not specified or given an invalid value, defaults to "true". If
  # "slf4j.showLogName" is true, this option is ignored.
  showShortLogName: true
  
  # Shows the name of the logging thread, wrapped in brackets. You probably
  # don't want this information unless you're helping troubleshoot a plugin.
  #
  # If not specified or given an invalid value, defaults to "false".
  showThreadName: false
  
  # This section controls default colors for logging levels. Each entry in this
  # section maps one of SLF4J's logging levels to one of SLF4Bukkit's
  # ColorMarker values. The possible keys (levels) in this section are the
  # possible values for the "slf4j.defaultLogLevel" property.
  #
  # If either the key (level) name or the value (ColorMarker) name does not
  # match one of the possible values, that config entry is ignored. Keys and
  # values are compared in a case-insensitive fashion.
  #
  # The values in the plugin config are applied on top of the following
  # hardcoded default values:
  #   error: RED
  #   warn: YELLOW
  #   info: NONE
  #   debug: NONE
  #   trace: NONE
  #
  # The ColorMarker values are:
  #   BLACK
  #   DARK_BLUE
  #   DARK_GREEN
  #   DARK_AQUA
  #   DARK_RED
  #   DARK_PURPLE
  #   GOLD
  #   GRAY
  #   DARK_GRAY
  #   BLUE
  #   GREEN
  #   AQUA
  #   RED
  #   LIGHT_PURPLE
  #   YELLOW
  #   WHITE
  #   NONE (default console color)
  #
  # If you are running this plugin on a Bukkit implementation that does not
  # include the JAnsi library (e.g. PaperSpigot), none of these configuration
  # values will be honored and all log output will have no colors.
  colors:
    error: RED
    warn: YELLOW
    info: NONE
    debug: NONE
    trace: NONE
  
  # This section controls logging levels for individual loggers.
  log:
  
    # For each element in this section, the key is the full logger name and the
    # value is the logging level for that logger. Possible logging levels are
    # the same as what's available for the "slf4j.defaultLogLevel" property
    # (case-insensitive).
    #
    # The documentation for your plugin should elaborate on what logger names
    # are available. As a general rule, you won't need to specify levels for
    # specific loggers.
    #
    # For any logger, if the level specified here is invalid, or if the level is
    # not specified at all, the level of the closest parent logger is used. If
    # none of the logger's ancestors have a valid level defined, the
    # value of "slf4j.defaultLogLevel" is used.
    info.ronjenkins.bukkit.MyPlugin: debug
    info.ronjenkins.SomeOtherLogger: warn
```
