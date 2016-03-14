package de.micromata.genome.logging;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.validation.ValMessage;

/**
 * Contains a list of validation messages.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValMessageLogAttribute extends LogAttribute
{
  private List<ValMessage> messages;

  public ValMessageLogAttribute(List<ValMessage> messages)
  {
    super(GenomeAttributeType.Validation, "");
    this.messages = messages;
    setValue(renderMessages());
  }

  protected String renderMessages()
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (ValMessage vm : messages) {
      if (first == true) {
        first = false;
      } else {
        sb.append("\n");
      }
      sb.append(vm.getValState().name()).append(": ");
      if (vm.getReference() != null) {
        sb.append(vm.getReference().getClass().getName());
      }
      if (vm.getReference() != null && StringUtils.isNotBlank(vm.getProperty()) == true) {
        sb.append(".");
      }
      if (StringUtils.isNotBlank(vm.getProperty()) == true) {
        sb.append(vm.getProperty());
      }
      sb.append(": ");
      if (StringUtils.isNotBlank(vm.getI18nkey()) == true) {
        sb.append(vm.getI18nkey());
      }
      if (StringUtils.isNotBlank(vm.getMessage()) == true) {
        sb.append("; ").append(vm.getMessage());
      }
    }
    return sb.toString();
  }

  public List<ValMessage> getMessages()
  {
    return messages;
  }

  public void setMessages(List<ValMessage> messages)
  {
    this.messages = messages;
  }
}
