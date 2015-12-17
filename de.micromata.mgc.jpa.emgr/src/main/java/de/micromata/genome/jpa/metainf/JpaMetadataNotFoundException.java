package de.micromata.genome.jpa.metainf;

/**
 * Something was not found.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataNotFoundException extends JpaMetadataException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1042558511238326440L;

  /**
   * Instantiates a new jpa metadata not found exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaMetadataNotFoundException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa metadata not found exception.
   *
   * @param message the message
   */
  public JpaMetadataNotFoundException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa metadata not found exception.
   *
   * @param cause the cause
   */
  public JpaMetadataNotFoundException(Throwable cause)
  {
    super(cause);
  }

}
