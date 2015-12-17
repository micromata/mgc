package de.micromata.genome.jpa.metainf;

/**
 * A column was not found.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataColumnNotFoundException extends JpaMetadataNotFoundException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6454036773188894828L;

  /**
   * Instantiates a new jpa metadata column not found exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaMetadataColumnNotFoundException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa metadata column not found exception.
   *
   * @param message the message
   */
  public JpaMetadataColumnNotFoundException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa metadata column not found exception.
   *
   * @param cause the cause
   */
  public JpaMetadataColumnNotFoundException(Throwable cause)
  {
    super(cause);
  }
}
