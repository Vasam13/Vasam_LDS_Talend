package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;

public class MapperTableEntries {
  private String name;
  private String expression;
  private String type;
  private boolean nullable;

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute(name = "expression")
  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  @XmlAttribute(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @XmlAttribute(name = "nullable")
  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  @Override
  public String toString() {
    return "MapperTableEntries{"
        + "name='"
        + name
        + '\''
        + ", expression='"
        + expression
        + '\''
        + ", type='"
        + type
        + '\''
        + ", nullable="
        + nullable
        + '}';
  }
}
