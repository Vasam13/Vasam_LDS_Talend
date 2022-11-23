package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class Context {

  private String confirmationNeeded;
  private String name;
  List<ContextParameter> contextparameter;

  @XmlAttribute(name = "confirmationNeeded")
  public String getConfirmationNeeded() {
    return confirmationNeeded;
  }

  public void setConfirmationNeeded(String confirmationNeeded) {
    this.confirmationNeeded = confirmationNeeded;
  }

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlElement(name = "contextParameter")
  public List<ContextParameter> getContextparameter() {
    return contextparameter;
  }

  public void setContextparameter(List<ContextParameter> contextparameter) {
    this.contextparameter = contextparameter;
  }

  @Override
  public String toString() {
    return "Context{"
        + "confirmationNeeded='"
        + confirmationNeeded
        + '\''
        + ", name='"
        + name
        + '\''
        + ", contextparameter="
        + contextparameter
        + '}';
  }
}
