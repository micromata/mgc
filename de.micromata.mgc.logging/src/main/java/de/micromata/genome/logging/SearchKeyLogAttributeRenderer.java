package de.micromata.genome.logging;

/**
 * Als HTML rendert die Klasse einen Link für dei Logging Seite. Als Text wird der Escaped Wert geliefert
 * 
 * @author lado
 * 
 */
public class SearchKeyLogAttributeRenderer implements LogAttributeRenderer
{

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogAttributeRenderer#renderHtml(de.micromata.genome.logging.LogAttribute,
   * de.micromata.genome.web.HttpContext)
   */
  @Override
  public String renderHtml(LogAttribute attr)
  {
    return "<a href=\"javascript:setSecondSearchAT('" + attr.getTypeName() + "','" + attr.getValue() + "')\">"
        + attr.getValue() + "</a>";
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
