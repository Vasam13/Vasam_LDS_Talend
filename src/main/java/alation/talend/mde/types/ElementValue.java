package alation.talend.mde.types;

import alation.talend.resources.Constants;
import jakarta.xml.bind.annotation.XmlAttribute;

public class ElementValue {
  private String elementRef;
  private String value;

  @XmlAttribute(name = "elementRef")
  public String getElementRef() {
    return elementRef;
  }

  public void setElementRef(String elementRef) {
    this.elementRef = elementRef;
  }

  @XmlAttribute(name = "value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "ElementValue{" + "elementRef='" + elementRef + '\'' + ", value='" + value + '\'' + '}';
  }
}
