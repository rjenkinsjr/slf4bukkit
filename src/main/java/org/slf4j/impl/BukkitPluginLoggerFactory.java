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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * BukkitPluginLoggerFactory is an implementation of {@link ILoggerFactory}
 * returning the appropriately named {@link BukkitPluginLoggerAdapter} instance.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class BukkitPluginLoggerFactory implements ILoggerFactory {

  // key: name (String), value: a BukkitPluginLoggerAdapter;
  ConcurrentMap<String, Logger> loggerMap;
  // The name of the Bukkit plugin to which this factory belongs.
  private final String          pluginName;

  public BukkitPluginLoggerFactory() {
    this.loggerMap = new ConcurrentHashMap<String, Logger>();
    // ensure jul initialization. see also SLF4J-359
    java.util.logging.LogManager.getLogManager();
    // Get plugin name
    InputStream pluginYmlFile = null;
    try {
      pluginYmlFile = this.getClass().getClassLoader()
                          .getResource("plugin.yml").openStream();
      final Yaml yaml = new Yaml();
      @SuppressWarnings("rawtypes")
      final Map pluginYml = (Map) yaml.load(pluginYmlFile);
      this.pluginName = (String) pluginYml.get("name");
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

  /*
   * (non-Javadoc)
   *
   * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
   */
  @Override
  public Logger getLogger(String name) {
    // the root logger is called "" in JUL
    if (name.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) {
      name = "";
    }

    final Logger slf4jLogger = this.loggerMap.get(name);
    if (slf4jLogger != null) {
      return slf4jLogger;
    } else {
      final Logger newInstance = new BukkitPluginLoggerAdapter(this.pluginName);
      final Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
      return oldInstance == null ? newInstance : oldInstance;
    }
  }

}
