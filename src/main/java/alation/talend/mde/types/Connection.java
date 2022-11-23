package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class Connection {
  private String connectorName;
  private String label;
  private int lineStyle;
  private String metaname;
  private int offsetLabelX;
  private int offsetLabelY;
  private String source;
  private String target;
  private List<ElementParameter> elementParameter;

  @XmlAttribute(name = "connectorName")
  public String getConnectorName() {
    return connectorName;
  }

  public void setConnectorName(String connectorName) {
    this.connectorName = connectorName;
  }

  @XmlAttribute(name = "label")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @XmlAttribute(name = "lineStyle")
  public int getLineStyle() {
    return lineStyle;
  }

  public void setLineStyle(int lineStyle) {
    this.lineStyle = lineStyle;
  }

  @XmlAttribute(name = "metaname")
  public String getMetaname() {
    return metaname;
  }

  public void setMetaname(String metaname) {
    this.metaname = metaname;
  }

  @XmlAttribute(name = "offsetLabelX")
  public int getOffsetLabelX() {
    return offsetLabelX;
  }

  public void setOffsetLabelX(int offsetLabelX) {
    this.offsetLabelX = offsetLabelX;
  }

  @XmlAttribute(name = "offsetLabelY")
  public int getOffsetLabelY() {
    return offsetLabelY;
  }

  public void setOffsetLabelY(int offsetLabelY) {
    this.offsetLabelY = offsetLabelY;
  }

  @XmlAttribute(name = "source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @XmlAttribute(name = "target")
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  @XmlElement(name = "elementParameter")
  public List<ElementParameter> getElementParameter() {
    return elementParameter;
  }

  public void setElementParameter(List<ElementParameter> elementParameter) {
    this.elementParameter = elementParameter;
  }

  @Override
  public String toString() {
    return "Connection{"
        + "connectorName='"
        + connectorName
        + '\''
        + ", label='"
        + label
        + '\''
        + ", lineStyle="
        + lineStyle
        + ", metaname='"
        + metaname
        + '\''
        + ", offsetLabelX="
        + offsetLabelX
        + ", offsetLabelY="
        + offsetLabelY
        + ", source='"
        + source
        + '\''
        + ", target='"
        + target
        + '\''
        + ", elementParameter="
        + elementParameter
        + '}';
  }
}
