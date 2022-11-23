package alation.talend.mde.types;

import alation.talend.io.Utils;
import alation.talend.resources.Constants;
import jakarta.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang3.StringUtils;

public class Column {
  private String comment;
  private boolean key;
  private int length;
  private String name;
  private boolean nullable;
  private String pattern;
  private int precision;
  private String type;
  private boolean usefulColumn;
  private String defaultValue;
  private String sourceType;
  private int originalLength;

  @XmlAttribute(name = "comment")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @XmlAttribute(name = "key")
  public boolean isKey() {
    return key;
  }

  public void setKey(boolean key) {
    this.key = key;
  }

  @XmlAttribute(name = "length")
  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute(name = "nullable")
  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  @XmlAttribute(name = "pattern")
  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  @XmlAttribute(name = "precision")
  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  @XmlAttribute(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @XmlAttribute(name = "usefulColumn")
  public boolean isUsefulColumn() {
    return usefulColumn;
  }

  public void setUsefulColumn(boolean usefulColumn) {
    this.usefulColumn = usefulColumn;
  }

  @XmlAttribute(name = "defaultValue")
  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  @XmlAttribute(name = "sourceType")
  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  @XmlAttribute(name = "originalLength")
  public int getOriginalLength() {
    return originalLength;
  }

  public void setOriginalLength(int originalLength) {
    this.originalLength = originalLength;
  }

  public String getNormalizedName() {
    return Utils.getNormalizedString(getName());
  }

  public String getNormalizedType() {
    if (StringUtils.isEmpty(type)) {
      return "";
    }
    return type.replaceAll(Constants.REGEX_id_, Constants.EMPTY);
  }

  @Override
  public String toString() {
    return "Column{"
        + "comment='"
        + comment
        + '\''
        + ", key="
        + key
        + ", length="
        + length
        + ", name='"
        + name
        + '\''
        + ", nullable="
        + nullable
        + ", pattern='"
        + pattern
        + '\''
        + ", precision="
        + precision
        + ", type='"
        + type
        + '\''
        + ", usefulColumn="
        + usefulColumn
        + ", defaultValue='"
        + defaultValue
        + '\''
        + ", sourceType='"
        + sourceType
        + '\''
        + ", originalLength="
        + originalLength
        + '}';
  }
}
