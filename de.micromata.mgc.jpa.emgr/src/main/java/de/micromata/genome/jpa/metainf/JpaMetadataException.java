package de.micromata.genome.jpa.metainf;

/**
 * Base class for Metadata Exceptions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataException extends RuntimeException
{

  /**
   * The Constant serialVersionUID.
   */

  private static final long serialVersionUID = -5295894472296106029L;

  /**
   * Instantiates a new jpa metadata exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaMetadataException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa metadata exception.
   *
   * @param message the message
   */
  public JpaMetadataException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa metadata exception.
   *
   * @param cause the cause
   */
  public JpaMetadataException(Throwable cause)
  {
    super(cause);
  }

}
