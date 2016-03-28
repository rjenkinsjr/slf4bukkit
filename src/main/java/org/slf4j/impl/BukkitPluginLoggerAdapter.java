/*
 * This entire file is sublicensed to you under GPLv3 or (at your option) any
 * later version. The original copyright notice is retained below.
 */
/*
 * Portions of this file are
 * Copyright (C) 2016 Ronald Jack Jenkins Jr.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Copyright (c) 2004-2012 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.slf4j.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;
import org.yaml.snakeyaml.Yaml;

/**
 * <p>
 * A merger of SLF4J's {@code SimpleLogger} and {@code JDK14LoggerAdapter},
 * wired to log all messages to the enclosing Bukkit plugin. The plugin is
 * identified by reading the "name" attribute from {@code plugin.yml} in the
 * current classloader.
 * </p>
 *
 * <p>
 * Plugins that include SLF4Bukkit can use the following values in
 * {@code config.yml} to configure the behavior of SLF4Bukkit. SLF4Bukkit uses
 * Bukkit's plugin configuration API to retrieve config values, so both on-disk
 * and built-in {@code config.yml} behavior is supported.
 * </p>
 *
 * <ul>
 * <li><code>slf4j.defaultLogLevel</code> - Default log level for all SLF4Bukkit
 * loggers in this plugin. Must be one of "trace", "debug", "info", "warn", or
 * "error". Defaults to "info".</li>
 *
 * <li><code>slf4j.log.<em>a.b.c</em></code> - Logging detail level for an
 * SLF4Bukkit logger instance in this plugin named "a.b.c". Right-side value
 * must be one of "trace", "debug", "info", "warn", or "error". When a logger
 * named "a.b.c" is initialized, its level is assigned from this property. If
 * unspecified, the level of the nearest parent logger will be used. If no
 * parent logger level is set, then the value specified by
 * <code>slf4j.defaultLogLevel</code> for this plugin will be used.</li>
 *
 * <li><code>slf4j.showHeader</code> -Set to <code>true</code> if you want to
 * output the {@code [SLF4J]} header. Defaults to <code>false</code>.</li>
 *
 * <li><code>slf4j.showThreadName</code> -Set to <code>true</code> if you want
 * to output the current thread name. Defaults to <code>false</code>.</li>
 *
 * <li><code>slf4j.showLogName</code> - Set to <code>true</code> if you want the
 * logger instance name to be included in output messages. Defaults to
 * <code>false</code>.</li>
 *
 * <li><code>slf4j.showShortLogName</code> - Set to <code>true</code> if you
 * want the logger instance's short name to be included in output messages. The
 * short name is equal to the full name with every dot-separated portion of the
 * full name (except the last portion) truncated to its first character.
 * Defaults to <code>true</code>.</li>
 * </ul>
 *
 * <p>
 * SLF4J messages at level {@code TRACE} or {@code DEBUG} are logged to Bukkit
 * at level {@code INFO} because Bukkit does not enable any levels higher than
 * {@code INFO}. Therefore, only SLF4J messages at level {@code TRACE} or
 * {@code DEBUG} show their SLF4J level in the message that is logged to the
 * server console.
 * </p>
 *
 * <p>
 * Because SLF4Bukkit's configuration comes from the plugin configuration,
 * SLF4Bukkit supports configuration reloading. To achieve this, call
 * {@link #init()} with argument {@code true} after calling
 * {@link Plugin#reloadConfig()}.
 * </p>
 * 
 * <p>
 * It is possible for SLF4J loggers to be used before the plugin is registered
 * with Bukkit's plugin manager. SLF4Bukkit is considered to be
 * <i>uninitialized</i> as long as the plugin cannot be retrieved from Bukkit's
 * plugin manager. While in the uninitialized state, SLF4Bukkit:
 * </p>
 * 
 * <ul>
 * <li>uses {@link Bukkit#getLogger()} instead of {@link Plugin#getLogger()}.</li>
 * <li>uses the default configuration values (see above).</li>
 * <li>attempts to initialize itself upon every logging call until the plugin is
 * retrievable from Bukkit's plugin manager, at which point SLF4Bukkit is
 * considered to be <i>initialized</i>. Once initialized,
 * {@link Plugin#getLogger()} and {@link Plugin#getConfig() the plugin YAML
 * configuration values} are used.</li>
 * </ul>
 * 
 * <p>
 * For this reason, it is strongly recommended that you not emit any log
 * messages via SLF4Bukkit until your plugin's {@link Plugin#onLoad() onLoad()}
 * method has begun execution. (You can safely log messages inside the
 * {@code onLoad()} method, because your plugin is registered by that time.)
 * Logging inside static initializers, the plugin class constructor and other
 * pre-plugin-registration areas of your code is discouraged.
 * </p>
 *
 * <p>
 * With no configuration, the default output includes the logger short name and
 * the message, followed by the line separator for the host.
 * </p>
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author <a href="mailto:sanders@apache.org">Scott Sanders</a>
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @author C&eacute;drik LIME
 * @author Peter Royal
 * @author Ronald Jack Jenkins Jr.
 */
public final class BukkitPluginLoggerAdapter extends MarkerIgnoringBase
                                                                       implements
                                                                       LocationAwareLogger {

  // Plugin reference.
  private static transient Plugin BUKKIT_PLUGIN;
  private static transient String BUKKIT_PLUGIN_NAME;
  // Constants for JUL record creation.
  private static final String     CLASS_SELF                          = BukkitPluginLoggerAdapter.class.getName();
  private static final String     CLASS_SUPER                         = MarkerIgnoringBase.class.getName();
  // Configuration parameters.
  private static int              CONFIG_DEFAULT_LOG_LEVEL;
  private static final String     CONFIG_FALLBACK_DEFAULT_LOG_LEVEL   = "info";
  private static final boolean    CONFIG_FALLBACK_SHOW_HEADER         = false;
  private static final boolean    CONFIG_FALLBACK_SHOW_LOG_NAME       = false;
  private static final boolean    CONFIG_FALLBACK_SHOW_SHORT_LOG_NAME = true;
  private static final boolean    CONFIG_FALLBACK_SHOW_THREAD_NAME    = false;
  private static final String     CONFIG_KEY_DEFAULT_LOG_LEVEL        = "slf4j.defaultLogLevel";
  private static final String     CONFIG_KEY_PREFIX_LOG               = "slf4j.log.";
  private static final String     CONFIG_KEY_SHOW_HEADER              = "slf4j.showHeader";
  private static final String     CONFIG_KEY_SHOW_LOG_NAME            = "slf4j.showLogName";
  private static final String     CONFIG_KEY_SHOW_SHORT_LOG_NAME      = "slf4j.showShortLogName";
  private static final String     CONFIG_KEY_SHOW_THREAD_NAME         = "slf4j.showThreadName";
  private static boolean          CONFIG_SHOW_HEADER;
  private static boolean          CONFIG_SHOW_LOG_NAME;
  private static boolean          CONFIG_SHOW_SHORT_LOG_NAME;
  private static boolean          CONFIG_SHOW_THREAD_NAME;
  // Initialization lock.
  private static final Object     INITIALIZATION_LOCK                 = new Object();
  // Logging level constants.
  private static final int        LOG_LEVEL_DEBUG                     = LocationAwareLogger.DEBUG_INT;
  private static final int        LOG_LEVEL_ERROR                     = LocationAwareLogger.ERROR_INT;
  private static final int        LOG_LEVEL_INFO                      = LocationAwareLogger.INFO_INT;
  private static final int        LOG_LEVEL_TRACE                     = LocationAwareLogger.TRACE_INT;
  private static final int        LOG_LEVEL_WARN                      = LocationAwareLogger.WARN_INT;
  // serialVersionUID
  private static final long       serialVersionUID                    = -2270127287235697381L;
  /** The current log level */
  protected int                   currentLogLevel                     = BukkitPluginLoggerAdapter.LOG_LEVEL_INFO;
  /** The short name of this simple log instance */
  private transient String        shortLogName                        = null;

  // NOTE: BukkitPluginLoggerAdapter constructor should have only package access
  // so that only BukkitPluginLoggerFactory be able to create one.
  BukkitPluginLoggerAdapter(final String name) {
    this.name = name;
    final String levelString = this.recursivelyComputeLevelString();
    if (levelString != null) {
      this.currentLogLevel = BukkitPluginLoggerAdapter.stringToLevel(levelString);
    } else {
      this.currentLogLevel = BukkitPluginLoggerAdapter.CONFIG_DEFAULT_LOG_LEVEL;
    }
  }

  /**
   * (Re)initializes all SLF4Bukkit loggers, relying on the YAML configuration
   * of the enclosing plugin.
   *
   * @param reinitialize
   *          set to {@code true} to reinitialize all loggers, e.g. after
   *          reloading the plugin config.
   */
  public static void init(final boolean reinitialize) {
    synchronized (BukkitPluginLoggerAdapter.INITIALIZATION_LOCK) {
      // Do not re-initialize unless requested.
      if (reinitialize) {
        BukkitPluginLoggerAdapter.BUKKIT_PLUGIN = null;
        BUKKIT_PLUGIN_NAME = null;
      } else if (BukkitPluginLoggerAdapter.BUKKIT_PLUGIN != null) { return; }
      // Get a reference to the plugin in this classloader.
      if (BUKKIT_PLUGIN_NAME == null) {
        InputStream pluginYmlFile = null;
        try {
          pluginYmlFile = BukkitPluginLoggerAdapter.class.getClassLoader()
                                                         .getResource("plugin.yml")
                                                         .openStream();
          final Yaml yaml = new Yaml();
          @SuppressWarnings("rawtypes")
          final Map pluginYml = (Map) yaml.load(pluginYmlFile);
          BUKKIT_PLUGIN_NAME = (String) pluginYml.get("name");
        } catch (final IOException e) {
          throw new IllegalStateException(e);
        } finally {
          if (pluginYmlFile != null) {
            try {
              pluginYmlFile.close();
            } catch (final IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
      // Try to get the plugin. The logging system will be considered
      // uninitialized until this becomes non-null. While it is null, the Bukkit
      // server logger will be used instead of the plugin logger, and all
      // default configuration options will be used.
      BukkitPluginLoggerAdapter.BUKKIT_PLUGIN = Bukkit.getPluginManager()
                                                      .getPlugin(BUKKIT_PLUGIN_NAME);
      // Get the configuration values.
      // 1. Look in the plugin's on-disk config.
      // 2. If the value is absent, use the plugin's built-in config.
      // 3. If the value is absent, use the default values hardcoded above.
      // (1 and 2 are handled by using the Bukkit API.)
      BukkitPluginLoggerAdapter.CONFIG_DEFAULT_LOG_LEVEL = BukkitPluginLoggerAdapter.stringToLevel(BukkitPluginLoggerAdapter.getStringProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_DEFAULT_LOG_LEVEL,
                                                                                                                                               BukkitPluginLoggerAdapter.CONFIG_FALLBACK_DEFAULT_LOG_LEVEL));
      BukkitPluginLoggerAdapter.CONFIG_SHOW_HEADER = BukkitPluginLoggerAdapter.getBooleanProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_SHOW_HEADER,
                                                                                                  BukkitPluginLoggerAdapter.CONFIG_FALLBACK_SHOW_HEADER);
      BukkitPluginLoggerAdapter.CONFIG_SHOW_LOG_NAME = BukkitPluginLoggerAdapter.getBooleanProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_SHOW_LOG_NAME,
                                                                                                    BukkitPluginLoggerAdapter.CONFIG_FALLBACK_SHOW_LOG_NAME);
      BukkitPluginLoggerAdapter.CONFIG_SHOW_SHORT_LOG_NAME = BukkitPluginLoggerAdapter.getBooleanProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_SHOW_SHORT_LOG_NAME,
                                                                                                          BukkitPluginLoggerAdapter.CONFIG_FALLBACK_SHOW_SHORT_LOG_NAME);
      BukkitPluginLoggerAdapter.CONFIG_SHOW_THREAD_NAME = BukkitPluginLoggerAdapter.getBooleanProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_SHOW_THREAD_NAME,
                                                                                                       BukkitPluginLoggerAdapter.CONFIG_FALLBACK_SHOW_THREAD_NAME);
    }
  }

  private static boolean getBooleanProperty(final String name,
                                            final boolean defaultValue) {
    synchronized (BukkitPluginLoggerAdapter.INITIALIZATION_LOCK) {
      if (BukkitPluginLoggerAdapter.BUKKIT_PLUGIN == null) { return defaultValue; }
      final String prop = BukkitPluginLoggerAdapter.BUKKIT_PLUGIN.getConfig()
                                                                 .getString(name);
      return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }
  }

  /**
   * Returns the most appropriate logger.
   *
   * @return the logger for the plugin if available; otherwise the server
   *         logger. Never null.
   */
  private static Logger getBukkitLogger() {
    synchronized (BukkitPluginLoggerAdapter.INITIALIZATION_LOCK) {
      return BukkitPluginLoggerAdapter.BUKKIT_PLUGIN == null ? Bukkit.getLogger()
                                                            : BukkitPluginLoggerAdapter.BUKKIT_PLUGIN.getLogger();
    }
  }

  private static String getStringProperty(final String name,
                                          final String defaultValue) {
    synchronized (BukkitPluginLoggerAdapter.INITIALIZATION_LOCK) {
      if (BukkitPluginLoggerAdapter.BUKKIT_PLUGIN == null) { return defaultValue; }
      final String prop = BukkitPluginLoggerAdapter.BUKKIT_PLUGIN.getConfig()
                                                                 .getString(name);
      return (prop == null) ? defaultValue : prop;
    }
  }

  /*
   * Logger API implementations
   */

  private static int stringToLevel(final String levelStr) {
    if ("trace".equalsIgnoreCase(levelStr)) {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE;
    } else if ("debug".equalsIgnoreCase(levelStr)) {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG;
    } else if ("info".equalsIgnoreCase(levelStr)) {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_INFO;
    } else if ("warn".equalsIgnoreCase(levelStr)) {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_WARN;
    } else if ("error".equalsIgnoreCase(levelStr)) {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR;
    } else {
      return BukkitPluginLoggerAdapter.LOG_LEVEL_INFO;
    }
  }

  /**
   * A simple implementation which logs messages of level DEBUG according
   * to the format outlined above.
   */
  @Override
  public void debug(final String msg) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG, msg, null);
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * DEBUG according to the format outlined above.
   */
  @Override
  public void debug(final String format, final Object param1) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG, format,
                      param1, null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * DEBUG according to the format outlined above.
   */
  @Override
  public void debug(final String format, final Object... argArray) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG, format,
                      argArray);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * DEBUG according to the format outlined above.
   */
  @Override
  public void debug(final String format, final Object param1,
                    final Object param2) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG, format,
                      param1, param2);
  }

  /** Log a message of level DEBUG, including an exception. */
  @Override
  public void debug(final String msg, final Throwable t) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG, msg, t);
  }

  /**
   * A simple implementation which always logs messages of level ERROR according
   * to the format outlined above.
   */
  @Override
  public void error(final String msg) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR, msg, null);
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  @Override
  public void error(final String format, final Object arg) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR, format, arg,
                      null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  @Override
  public void error(final String format, final Object... argArray) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR, format,
                      argArray);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR, format, arg1,
                      arg2);
  }

  /** Log a message of level ERROR, including an exception. */
  @Override
  public void error(final String msg, final Throwable t) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR, msg, t);
  }

  /**
   * A simple implementation which logs messages of level INFO according
   * to the format outlined above.
   */
  @Override
  public void info(final String msg) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_INFO, msg, null);
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  @Override
  public void info(final String format, final Object arg) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_INFO, format, arg,
                      null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  @Override
  public void info(final String format, final Object... argArray) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_INFO, format,
                      argArray);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_INFO, format, arg1,
                      arg2);
  }

  /** Log a message of level INFO, including an exception. */
  @Override
  public void info(final String msg, final Throwable t) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_INFO, msg, t);
  }

  /** Are {@code debug} messages currently enabled? */
  @Override
  public boolean isDebugEnabled() {
    return this.isLevelEnabled(BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG);
  }

  /** Are {@code error} messages currently enabled? */
  @Override
  public boolean isErrorEnabled() {
    return this.isLevelEnabled(BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR);
  }

  /** Are {@code info} messages currently enabled? */
  @Override
  public boolean isInfoEnabled() {
    return this.isLevelEnabled(BukkitPluginLoggerAdapter.LOG_LEVEL_INFO);
  }

  /** Are {@code trace} messages currently enabled? */
  @Override
  public boolean isTraceEnabled() {
    return this.isLevelEnabled(BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE);
  }

  /** Are {@code warn} messages currently enabled? */
  @Override
  public boolean isWarnEnabled() {
    return this.isLevelEnabled(BukkitPluginLoggerAdapter.LOG_LEVEL_WARN);
  }

  /**
   * Location-aware logging capability. The marker and argArray are ignored.
   */
  @Override
  public void log(final Marker marker, final String callerFQCN,
                  final int level, final String message,
                  final Object[] argArray, final Throwable t) {
    if (!this.isLevelEnabled(level)) { return; }
    this.log(callerFQCN, level, message, t);
  }

  /**
   * A simple implementation which logs messages of level TRACE according
   * to the format outlined above.
   */
  @Override
  public void trace(final String msg) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE, msg, null);
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * TRACE according to the format outlined above.
   */
  @Override
  public void trace(final String format, final Object param1) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE, format,
                      param1, null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * TRACE according to the format outlined above.
   */
  @Override
  public void trace(final String format, final Object... argArray) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE, format,
                      argArray);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * TRACE according to the format outlined above.
   */
  @Override
  public void trace(final String format, final Object param1,
                    final Object param2) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE, format,
                      param1, param2);
  }

  /** Log a message of level TRACE, including an exception. */
  @Override
  public void trace(final String msg, final Throwable t) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE, msg, t);
  }

  /**
   * A simple implementation which always logs messages of level WARN according
   * to the format outlined above.
   */
  @Override
  public void warn(final String msg) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_WARN, msg, null);
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  @Override
  public void warn(final String format, final Object arg) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_WARN, format, arg,
                      null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  @Override
  public void warn(final String format, final Object... argArray) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_WARN, format,
                      argArray);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
    this.formatAndLog(BukkitPluginLoggerAdapter.LOG_LEVEL_WARN, format, arg1,
                      arg2);
  }

  /*
   * Logic from SimpleLogger/JDK14LoggerAdapter
   */

  /** Log a message of level WARN, including an exception. */
  @Override
  public void warn(final String msg, final Throwable t) {
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF,
             BukkitPluginLoggerAdapter.LOG_LEVEL_WARN, msg, t);
  }

  private String computeShortName() {
    final List<String> splitName = new ArrayList<String>();
    splitName.addAll(Arrays.asList(this.name.split("\\.")));
    final int shortNameLength = ((splitName.size() - 1) * 2)
                                + splitName.get(splitName.size() - 1).length();
    final String finalName = splitName.remove(splitName.size() - 1);
    final StringBuffer shortName = new StringBuffer(shortNameLength);
    for (final String part : splitName) {
      shortName.append(part.charAt(0)).append('.');
    }
    shortName.append(finalName);
    return shortName.toString();
  }

  /**
   * For formatted messages, first substitute arguments and then log.
   *
   * @param level
   * @param format
   * @param arguments
   *          a list of 3 ore more arguments
   */
  private void formatAndLog(final int level, final String format,
                            final Object... arguments) {
    BukkitPluginLoggerAdapter.init(false);
    if (!this.isLevelEnabled(level)) { return; }
    final FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF, level, tp.getMessage(),
             tp.getThrowable());
  }

  /**
   * For formatted messages, first substitute arguments and then log.
   *
   * @param level
   * @param format
   * @param arg1
   * @param arg2
   */
  private void formatAndLog(final int level, final String format,
                            final Object arg1, final Object arg2) {
    BukkitPluginLoggerAdapter.init(false);
    if (!this.isLevelEnabled(level)) { return; }
    final FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
    this.log(BukkitPluginLoggerAdapter.CLASS_SELF, level, tp.getMessage(),
             tp.getThrowable());
  }

  /**
   * Is the given log level currently enabled?
   *
   * @param logLevel
   *          is this level enabled?
   */
  private boolean isLevelEnabled(final int logLevel) {
    // log level are numerically ordered so can use simple numeric comparison
    //
    // the PLUGIN.getLogger().isLoggable() check avoids the unconditional
    // construction of location data for disabled log statements. As of
    // 2008-07-31, callers of this method do not perform this check. See also
    // http://jira.qos.ch/browse/SLF4J-81
    return (logLevel >= this.currentLogLevel)
           && (BukkitPluginLoggerAdapter.getBukkitLogger().isLoggable(this.slf4jLevelIntToBukkitJULLevel(logLevel)));
  }

  /**
   * Fill in caller data if possible.
   *
   * @param record
   *          The record to update
   */
  private void
      julFillCallerData(final String callerFQCN, final LogRecord record) {
    final StackTraceElement[] steArray = new Throwable().getStackTrace();

    int selfIndex = -1;
    for (int i = 0; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();
      if (className.equals(callerFQCN)
          || className.equals(BukkitPluginLoggerAdapter.CLASS_SUPER)) {
        selfIndex = i;
        break;
      }
    }

    int found = -1;
    for (int i = selfIndex + 1; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();
      if (!(className.equals(callerFQCN) || className.equals(BukkitPluginLoggerAdapter.CLASS_SUPER))) {
        found = i;
        break;
      }
    }

    if (found != -1) {
      final StackTraceElement ste = steArray[found];
      // setting the class name has the side effect of setting
      // the needToInferCaller variable to false.
      record.setSourceClassName(ste.getClassName());
      record.setSourceMethodName(ste.getMethodName());
    }
  }

  /**
   * Log the message at the specified level with the specified throwable if any.
   * This method creates a LogRecord and fills in caller date before calling
   * this instance's JDK14 logger.
   *
   * See bug report #13 for more details.
   *
   * @param logger
   * @param level
   * @param msg
   * @param t
   */
  private void julLog(final Logger logger, final String callerFQCN,
                      final Level level, final String msg, final Throwable t) {
    // millis and thread are filled by the constructor
    final LogRecord record = new LogRecord(level, msg);
    record.setLoggerName(this.getName());
    record.setThrown(t);
    // Note: parameters in record are not set because SLF4J only
    // supports a single formatting style
    this.julFillCallerData(callerFQCN, record);
    logger.log(record);
  }

  /**
   * This is our internal implementation for logging regular (non-parameterized)
   * log messages.
   *
   * @param callerFQCN
   *          the FQCN of the class that is calling the logger
   * @param level
   *          One of the LOG_LEVEL_XXX constants defining the log level
   * @param message
   *          The message itself
   * @param t
   *          The exception whose stack trace should be logged
   */
  private void log(final String callerFQCN, final int level,
                   final String message, final Throwable t) {
    final Logger logger;
    synchronized (BukkitPluginLoggerAdapter.INITIALIZATION_LOCK) {
      // Ensure that the logger will accept this request.
      BukkitPluginLoggerAdapter.init(false);
      if (!this.isLevelEnabled(level)) { return; }
      // Determine which logger will be used.
      logger = BukkitPluginLoggerAdapter.getBukkitLogger();
    }

    // Prepare message
    final StringBuilder buf = new StringBuilder(32);

    // Indicate that this message comes from SLF4J
    if (BukkitPluginLoggerAdapter.CONFIG_SHOW_HEADER) {
      buf.append("SLF4J");
    }

    // Print a readable representation of the log level (but only for log levels
    // that Bukkit would otherwise eat)
    switch (level) {
      case LOG_LEVEL_TRACE:
        buf.append("[TRACE]");
        break;
      case LOG_LEVEL_DEBUG:
        buf.append("[DEBUG]");
        break;
    }

    // Append current thread name if so configured
    if (BukkitPluginLoggerAdapter.CONFIG_SHOW_THREAD_NAME) {
      buf.append('[');
      buf.append(Thread.currentThread().getName());
      buf.append("]");
    }

    // Buffer the current output with a space, unless there is no output.
    if (buf.length() > 0) {
      buf.append(' ');
    }

    // Append the name of the log instance if so configured
    if (BukkitPluginLoggerAdapter.CONFIG_SHOW_SHORT_LOG_NAME) {
      if (this.shortLogName == null) {
        this.shortLogName = this.computeShortName();
      }
      buf.append(String.valueOf(this.shortLogName)).append(" - ");
    } else if (BukkitPluginLoggerAdapter.CONFIG_SHOW_LOG_NAME) {
      buf.append(String.valueOf(this.name)).append(" - ");
    }

    // Append the message
    buf.append(message);

    // Log to Bukkit
    this.julLog(logger, BukkitPluginLoggerAdapter.CLASS_SELF,
                this.slf4jLevelIntToBukkitJULLevel(level), buf.toString(), t);
  }

  private String recursivelyComputeLevelString() {
    String tempName = this.name;
    String levelString = null;
    int indexOfLastDot = tempName.length();
    while ((levelString == null) && (indexOfLastDot > -1)) {
      tempName = tempName.substring(0, indexOfLastDot);
      levelString = BukkitPluginLoggerAdapter.getStringProperty(BukkitPluginLoggerAdapter.CONFIG_KEY_PREFIX_LOG
                                                                    + tempName,
                                                                null);
      indexOfLastDot = String.valueOf(tempName).lastIndexOf(".");
    }
    return levelString;
  }

  private Level slf4jLevelIntToBukkitJULLevel(final int slf4jLevelInt) {
    Level julLevel;
    switch (slf4jLevelInt) {
    // In Bukkit, Only the SEVERE, WARNING and INFO JUL levels are enabled, so
    // SLF4J's TRACE and DEBUG levels must be logged at Bukkit's INFO level.
      case BukkitPluginLoggerAdapter.LOG_LEVEL_TRACE:
      case BukkitPluginLoggerAdapter.LOG_LEVEL_DEBUG:
      case BukkitPluginLoggerAdapter.LOG_LEVEL_INFO:
        julLevel = Level.INFO;
        break;
      case BukkitPluginLoggerAdapter.LOG_LEVEL_WARN:
        julLevel = Level.WARNING;
        break;
      case BukkitPluginLoggerAdapter.LOG_LEVEL_ERROR:
        julLevel = Level.SEVERE;
        break;
      default:
        throw new IllegalStateException("Level number " + slf4jLevelInt
                                        + " is not recognized.");
    }
    return julLevel;
  }

}
