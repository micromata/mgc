package de.micromata.genome.jpa.metainf;

/**
 * Entity was not found in the Metadata Repo.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataEntityNotFoundException extends JpaMetadataNotFoundException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7503202640180469114L;

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaMetadataEntityNotFoundException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param message the message
   */
  public JpaMetadataEntityNotFoundException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param cause the cause
   */
  public JpaMetadataEntityNotFoundException(Throwable cause)
  {
    super(cause);
  }

}
