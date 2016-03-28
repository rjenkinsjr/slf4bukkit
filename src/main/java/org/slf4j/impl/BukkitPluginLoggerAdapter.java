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
/*
 * This entire file is sublicensed to you under GPLv3 or (at your option) any
 * later version. The original copyright notice is retained below.
 */
/**
 * Copyright (c) 2004-2011 QOS.ch
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

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.EventConstants;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

/**
 * A wrapper over a {@link Plugin Bukkit plugin}'s JUL logger in conformity with
 * the {@link Logger} interface. Note that the logging levels mentioned in this
 * class refer to those defined in the java.util.logging package.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Peter Royal
 * @author Ronald Jack Jenkins Jr.
 */
public final class BukkitPluginLoggerAdapter extends MarkerIgnoringBase
                                                                       implements
                                                                       LocationAwareLogger {

  static String                            SELF             = BukkitPluginLoggerAdapter.class.getName();

  static String                            SUPER            = MarkerIgnoringBase.class.getName();

  private static final long                serialVersionUID = 643332807329452230L;

  transient final java.util.logging.Logger logger;

  // WARN: BukkitPluginLoggerAdapter constructor should have only package access
  // so that only BukkitPluginLoggerFactory be able to create one.
  BukkitPluginLoggerAdapter(final String pluginName) {
    this.logger = Bukkit.getPluginManager().getPlugin(pluginName).getLogger();
    this.name = this.logger.getName();
  }

  /**
   * Log a message object at level FINE.
   *
   * @param msg
   *          - the message object to be logged
   */
  @Override
  public void debug(final String msg) {
    if (this.logger.isLoggable(Level.FINE)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINE, msg, null);
    }
  }

  /**
   * Log a message at level FINE according to the specified format and argument.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for level FINE.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg
   *          the argument
   */
  @Override
  public void debug(final String format, final Object arg) {
    if (this.logger.isLoggable(Level.FINE)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level FINE according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the FINE level.
   * </p>
   *
   * @param format
   *          the format string
   * @param argArray
   *          an array of arguments
   */
  @Override
  public void debug(final String format, final Object... argArray) {
    if (this.logger.isLoggable(Level.FINE)) {
      final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level FINE according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the FINE level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg1
   *          the first argument
   * @param arg2
   *          the second argument
   */
  @Override
  public void debug(final String format, final Object arg1, final Object arg2) {
    if (this.logger.isLoggable(Level.FINE)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log an exception (throwable) at level FINE with an accompanying message.
   *
   * @param msg
   *          the message accompanying the exception
   * @param t
   *          the exception (throwable) to log
   */
  @Override
  public void debug(final String msg, final Throwable t) {
    if (this.logger.isLoggable(Level.FINE)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINE, msg, t);
    }
  }

  /**
   * Log a message object at the SEVERE level.
   *
   * @param msg
   *          - the message object to be logged
   */
  @Override
  public void error(final String msg) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.SEVERE, msg, null);
    }
  }

  /**
   * Log a message at the SEVERE level according to the specified format and
   * argument.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the SEVERE level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg
   *          the argument
   */
  @Override
  public void error(final String format, final Object arg) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.SEVERE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level SEVERE according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the SEVERE level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arguments
   *          an array of arguments
   */
  @Override
  public void error(final String format, final Object... arguments) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.SEVERE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at the SEVERE level according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the SEVERE level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg1
   *          the first argument
   * @param arg2
   *          the second argument
   */
  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.SEVERE, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log an exception (throwable) at the SEVERE level with an accompanying
   * message.
   *
   * @param msg
   *          the message accompanying the exception
   * @param t
   *          the exception (throwable) to log
   */
  @Override
  public void error(final String msg, final Throwable t) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.SEVERE, msg, t);
    }
  }

  /**
   * Log a message object at the INFO level.
   *
   * @param msg
   *          - the message object to be logged
   */
  @Override
  public void info(final String msg) {
    if (this.logger.isLoggable(Level.INFO)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.INFO, msg, null);
    }
  }

  /**
   * Log a message at level INFO according to the specified format and argument.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the INFO level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg
   *          the argument
   */
  @Override
  public void info(final String format, final Object arg) {
    if (this.logger.isLoggable(Level.INFO)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.INFO, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level INFO according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the INFO level.
   * </p>
   *
   * @param format
   *          the format string
   * @param argArray
   *          an array of arguments
   */
  @Override
  public void info(final String format, final Object... argArray) {
    if (this.logger.isLoggable(Level.INFO)) {
      final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.INFO, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at the INFO level according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the INFO level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg1
   *          the first argument
   * @param arg2
   *          the second argument
   */
  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
    if (this.logger.isLoggable(Level.INFO)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.INFO, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log an exception (throwable) at the INFO level with an accompanying
   * message.
   *
   * @param msg
   *          the message accompanying the exception
   * @param t
   *          the exception (throwable) to log
   */
  @Override
  public void info(final String msg, final Throwable t) {
    if (this.logger.isLoggable(Level.INFO)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.INFO, msg, t);
    }
  }

  /**
   * Is this logger instance enabled for the FINE level?
   *
   * @return True if this Logger is enabled for level FINE, false otherwise.
   */
  @Override
  public boolean isDebugEnabled() {
    return this.logger.isLoggable(Level.FINE);
  }

  /**
   * Is this logger instance enabled for level SEVERE?
   *
   * @return True if this Logger is enabled for level SEVERE, false otherwise.
   */
  @Override
  public boolean isErrorEnabled() {
    return this.logger.isLoggable(Level.SEVERE);
  }

  /**
   * Is this logger instance enabled for the INFO level?
   *
   * @return True if this Logger is enabled for the INFO level, false otherwise.
   */
  @Override
  public boolean isInfoEnabled() {
    return this.logger.isLoggable(Level.INFO);
  }

  /**
   * Is this logger instance enabled for the FINEST level?
   *
   * @return True if this Logger is enabled for level FINEST, false otherwise.
   */
  @Override
  public boolean isTraceEnabled() {
    return this.logger.isLoggable(Level.FINEST);
  }

  /**
   * Is this logger instance enabled for the WARNING level?
   *
   * @return True if this Logger is enabled for the WARNING level, false
   *         otherwise.
   */
  @Override
  public boolean isWarnEnabled() {
    return this.logger.isLoggable(Level.WARNING);
  }

  /**
   * @since 1.7.15
   */
  public void log(final LoggingEvent event) {
    final Level julLevel = this.slf4jLevelIntToJULLevel(event.getLevel()
                                                             .toInt());
    if (this.logger.isLoggable(julLevel)) {
      final LogRecord record = this.eventToRecord(event, julLevel);
      this.logger.log(record);
    }
  }

  @Override
  public void log(final Marker marker, final String callerFQCN,
                  final int level, final String message,
                  final Object[] argArray, final Throwable t) {
    final Level julLevel = this.slf4jLevelIntToJULLevel(level);
    // the logger.isLoggable check avoids the unconditional
    // construction of location data for disabled log
    // statements. As of 2008-07-31, callers of this method
    // do not perform this check. See also
    // http://jira.qos.ch/browse/SLF4J-81
    if (this.logger.isLoggable(julLevel)) {
      this.log(callerFQCN, julLevel, message, t);
    }
  }

  /**
   * Log a message object at level FINEST.
   *
   * @param msg
   *          - the message object to be logged
   */
  @Override
  public void trace(final String msg) {
    if (this.logger.isLoggable(Level.FINEST)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINEST, msg, null);
    }
  }

  /**
   * Log a message at level FINEST according to the specified format and
   * argument.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for level FINEST.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg
   *          the argument
   */
  @Override
  public void trace(final String format, final Object arg) {
    if (this.logger.isLoggable(Level.FINEST)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINEST, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level FINEST according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the FINEST level.
   * </p>
   *
   * @param format
   *          the format string
   * @param argArray
   *          an array of arguments
   */
  @Override
  public void trace(final String format, final Object... argArray) {
    if (this.logger.isLoggable(Level.FINEST)) {
      final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINEST, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level FINEST according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the FINEST level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg1
   *          the first argument
   * @param arg2
   *          the second argument
   */
  @Override
  public void trace(final String format, final Object arg1, final Object arg2) {
    if (this.logger.isLoggable(Level.FINEST)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINEST, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log an exception (throwable) at level FINEST with an accompanying message.
   *
   * @param msg
   *          the message accompanying the exception
   * @param t
   *          the exception (throwable) to log
   */
  @Override
  public void trace(final String msg, final Throwable t) {
    if (this.logger.isLoggable(Level.FINEST)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.FINEST, msg, t);
    }
  }

  /**
   * Log a message object at the WARNING level.
   *
   * @param msg
   *          - the message object to be logged
   */
  @Override
  public void warn(final String msg) {
    if (this.logger.isLoggable(Level.WARNING)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.WARNING, msg, null);
    }
  }

  /**
   * Log a message at the WARNING level according to the specified format and
   * argument.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the WARNING level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg
   *          the argument
   */
  @Override
  public void warn(final String format, final Object arg) {
    if (this.logger.isLoggable(Level.WARNING)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.WARNING, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at level WARNING according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the WARNING level.
   * </p>
   *
   * @param format
   *          the format string
   * @param argArray
   *          an array of arguments
   */
  @Override
  public void warn(final String format, final Object... argArray) {
    if (this.logger.isLoggable(Level.WARNING)) {
      final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.WARNING, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log a message at the WARNING level according to the specified format and
   * arguments.
   *
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the WARNING level.
   * </p>
   *
   * @param format
   *          the format string
   * @param arg1
   *          the first argument
   * @param arg2
   *          the second argument
   */
  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
    if (this.logger.isLoggable(Level.WARNING)) {
      final FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      this.log(BukkitPluginLoggerAdapter.SELF, Level.WARNING, ft.getMessage(),
               ft.getThrowable());
    }
  }

  /**
   * Log an exception (throwable) at the WARNING level with an accompanying
   * message.
   *
   * @param msg
   *          the message accompanying the exception
   * @param t
   *          the exception (throwable) to log
   */
  @Override
  public void warn(final String msg, final Throwable t) {
    if (this.logger.isLoggable(Level.WARNING)) {
      this.log(BukkitPluginLoggerAdapter.SELF, Level.WARNING, msg, t);
    }
  }

  private LogRecord
      eventToRecord(final LoggingEvent event, final Level julLevel) {
    final String format = event.getMessage();
    final Object[] arguments = event.getArgumentArray();
    final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
    if ((ft.getThrowable() != null) && (event.getThrowable() != null)) { throw new IllegalArgumentException(
                                                                                                            "both last element in argument array and last argument are of type Throwable"); }

    Throwable t = event.getThrowable();
    if (ft.getThrowable() != null) {
      t = ft.getThrowable();
      throw new IllegalStateException("fix above code");
    }

    final LogRecord record = new LogRecord(julLevel, ft.getMessage());
    record.setLoggerName(event.getLoggerName());
    record.setMillis(event.getTimeStamp());
    record.setSourceClassName(EventConstants.NA_SUBST);
    record.setSourceMethodName(EventConstants.NA_SUBST);

    record.setThrown(t);
    return record;
  }

  /**
   * Fill in caller data if possible.
   *
   * @param record
   *          The record to update
   */
  final private void fillCallerData(final String callerFQCN,
                                    final LogRecord record) {
    final StackTraceElement[] steArray = new Throwable().getStackTrace();

    int selfIndex = -1;
    for (int i = 0; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();
      if (className.equals(callerFQCN)
          || className.equals(BukkitPluginLoggerAdapter.SUPER)) {
        selfIndex = i;
        break;
      }
    }

    int found = -1;
    for (int i = selfIndex + 1; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();
      if (!(className.equals(callerFQCN) || className.equals(BukkitPluginLoggerAdapter.SUPER))) {
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
   * @param level
   * @param msg
   * @param t
   */
  private void log(final String callerFQCN, final Level level,
                   final String msg, final Throwable t) {
    // millis and thread are filled by the constructor
    final LogRecord record = new LogRecord(level, msg);
    record.setLoggerName(this.getName());
    record.setThrown(t);
    // Note: parameters in record are not set because SLF4J only
    // supports a single formatting style
    this.fillCallerData(callerFQCN, record);
    this.logger.log(record);
  }

  private Level slf4jLevelIntToJULLevel(final int slf4jLevelInt) {
    Level julLevel;
    switch (slf4jLevelInt) {
      case LocationAwareLogger.TRACE_INT:
        julLevel = Level.FINEST;
        break;
      case LocationAwareLogger.DEBUG_INT:
        julLevel = Level.FINE;
        break;
      case LocationAwareLogger.INFO_INT:
        julLevel = Level.INFO;
        break;
      case LocationAwareLogger.WARN_INT:
        julLevel = Level.WARNING;
        break;
      case LocationAwareLogger.ERROR_INT:
        julLevel = Level.SEVERE;
        break;
      default:
        throw new IllegalStateException("Level number " + slf4jLevelInt
                                        + " is not recognized.");
    }
    return julLevel;
  }
}
