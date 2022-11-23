package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;

public class RoutinesParameter {
  private String id;
  private String name;

  @XmlAttribute(name = "id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "RoutinesParameter{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
  }
}
