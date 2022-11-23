package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class Subjob {

  private List<ElementParameter> elementParameter;

  @XmlElement(name = "elementParameter")
  public List<ElementParameter> getElementParameter() {
    return elementParameter;
  }

  public void setElementParameter(List<ElementParameter> elementParameter) {
    this.elementParameter = elementParameter;
  }

  @Override
  public String toString() {
    return "Subjob{" + "elementParameter=" + elementParameter + '}';
  }
}
