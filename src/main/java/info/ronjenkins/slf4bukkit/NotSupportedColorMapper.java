package info.ronjenkins.slf4bukkit;

/**
 * Does not do any mapping but simply returns the given string or, if {@code null} is given, an empty string.
 */
final class NotSupportedColorMapper implements ColorMapper {

  @Override
  public String map(final String input) {
    return input == null ? "" : input;
  }
}
