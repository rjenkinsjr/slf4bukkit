package info.ronjenkins.slf4bukkit;

/**
 * Creates {@code ColorMapper} instances.
 *
 * @see ColorMapper
 */
public final class ColorMapperFactory {

  /**
   * Creates an new {@code ColorMapper} instance.
   *
   * @return a new instance
   */
  public static ColorMapper create() {
    try {
      return new AnsiColorMapper();
    } catch (Throwable throwable) {
      return new NotSupportedColorMapper();
    }
  }

}
