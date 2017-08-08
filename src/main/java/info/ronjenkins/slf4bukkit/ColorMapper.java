/*
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
package info.ronjenkins.slf4bukkit;

import org.bukkit.ChatColor;

/**
 * Utility class that maps {@link ChatColor} values to their equivalents,
 * so that messages logged to the console are formatted correctly.
 */
public interface ColorMapper {

  /**
   * Translates {@link ChatColor} directives to their equivalents.
   *
   * @param input
   *          null is coerced to the empty string.
   * @return never null.
   */
  String map(String input);

}
