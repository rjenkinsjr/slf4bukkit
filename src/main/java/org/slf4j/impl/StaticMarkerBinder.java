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

import org.slf4j.IMarkerFactory;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

/**
 * The binding of {@link MarkerFactory} class with an actual instance of
 * {@link IMarkerFactory} is performed using information returned by this class.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class StaticMarkerBinder implements MarkerFactoryBinder {

  /**
   * The unique instance of this class.
   */
  public static final StaticMarkerBinder SINGLETON     = new StaticMarkerBinder();
  final IMarkerFactory                   markerFactory = new BasicMarkerFactory();

  private StaticMarkerBinder() {
  }

  /**
   * Return the singleton of this class.
   *
   * @return the StaticMarkerBinder singleton
   * @since 1.7.14
   */
  public static StaticMarkerBinder getSingleton() {
    return StaticMarkerBinder.SINGLETON;
  }

  /**
   * Currently this method always returns an instance of
   * {@link BasicMarkerFactory}.
   */
  @Override
  public IMarkerFactory getMarkerFactory() {
    return this.markerFactory;
  }

  /**
   * Currently, this method returns the class name of {@link BasicMarkerFactory}
   * .
   */
  @Override
  public String getMarkerFactoryClassStr() {
    return BasicMarkerFactory.class.getName();
  }

}
