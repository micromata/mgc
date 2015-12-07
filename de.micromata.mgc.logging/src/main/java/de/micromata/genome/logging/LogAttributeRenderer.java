package de.micromata.genome.logging;

import javax.xml.ws.spi.http.HttpContext;

/**
 * Um die {@link LogAttribute} zu rendern.
 *
 * @author lado
 */
public interface LogAttributeRenderer
{

  /**
   * Rendert eine HTML Darestellung des {@link LogAttribute} Wertes. Optional kann {@link HttpContext} übergeben werden,
   * z.B, richige Links zu rendern, etc.
   * 
   * @param attr {@link LogAttribute}
   * @param ctx {@link HttpContext} optionales Element. Die Implementierung sorgt dafür ob die es optinal braucht oder
   *          pflicht ist
   * @return {@link String} Html Representation des {@link LogAttribute} Wert
   */
  public String renderHtml(LogAttribute attr);

  /**
   * Liefert die Text Representation eines {@link LogAttribute} Wertes.
   *
   * @param attr {@link LogAttribute}
   * @return {@link String} Text Representation des {@link LogAttribute} Wertes
   */
  public String renderText(LogAttribute attr);

}
