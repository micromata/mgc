package de.micromata.genome.util.matcher;

/**
 * Thrown if a grammar in a matcher expression is invalid.
 * 
 * @author roger
 * 
 */
public class InvalidMatcherGrammar extends RuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 2572461712815733388L;

  /**
   * Instantiates a new invalid matcher grammar.
   */
  public InvalidMatcherGrammar()
  {
    super();
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param message the message
   * @param cause the cause
   */
  public InvalidMatcherGrammar(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param message the message
   */
  public InvalidMatcherGrammar(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param cause the cause
   */
  public InvalidMatcherGrammar(Throwable cause)
  {
    super(cause);
  }

}
