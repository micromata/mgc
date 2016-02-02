package de.micromata.genome.util.types;

/**
 * The Class StringStringPair.
 *
 * @author roger@micromata.de
 * @see Pair
 */
public class StringStringPair extends Pair<String, String>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 567741917066380725L;

  /**
   * Instantiates a new string string pair.
   */
  public StringStringPair()
  {
    super();
  }

  /**
   * Instantiates a new string string pair.
   *
   * @param key the key
   * @param value the value
   */
  public StringStringPair(String key, String value)
  {
    super(key, value);
  }
}
