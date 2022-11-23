package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;

public class VarTables {
  private String sizeState;
  private String name;
  private boolean minimized;

  @XmlAttribute(name = "sizeState")
  public String getSizeState() {
    return sizeState;
  }

  public void setSizeState(String sizeState) {
    this.sizeState = sizeState;
  }

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute(name = "minimized")
  public boolean isMinimized() {
    return minimized;
  }

  public void setMinimized(boolean minimized) {
    this.minimized = minimized;
  }

  @Override
  public String toString() {
    return "VarTables{"
        + "sizeState='"
        + sizeState
        + '\''
        + ", name='"
        + name
        + '\''
        + ", minimized="
        + minimized
        + '}';
  }
}
