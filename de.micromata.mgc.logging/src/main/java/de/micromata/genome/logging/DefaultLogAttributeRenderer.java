package de.micromata.genome.logging;

/**
 * Default renderer. Als HTML wird der Wert des {@link LogAttribute} in &lt;pre&gt; Tag geliefert. Als Text wird der
 * escaped Wert genommen.
 * 
 * @author lado
 * 
 */
public class DefaultLogAttributeRenderer implements LogAttributeRenderer
{

  /**
   * The instance.
   */
  public static DefaultLogAttributeRenderer INSTANCE = new DefaultLogAttributeRenderer();

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogAttributeRenderer#renderHtml(de.micromata.genome.logging.LogAttribute,
   * de.micromata.genome.web.HttpContext)
   */
  @Override
  public String renderHtml(LogAttribute attr)
  {
    return "<pre><code>" + attr.getEscapedValue() + "</code></pre>";
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogAttributeRenderer#renderText(de.micromata.genome.logging.LogAttribute)
   */
  @Override
  public String renderText(LogAttribute attr)
  {
    return attr.getEscapedValue();
  }
}
