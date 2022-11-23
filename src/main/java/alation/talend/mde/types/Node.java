package alation.talend.mde.types;

import alation.talend.io.Utils;
import alation.talend.resources.Constants;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Node {
  private String componentName;
  private String componentVersion;
  private int offsetLabelX;
  private int offsetLabelY;
  private int posX;
  private int posY;
  private List<ElementParameter> elementParameter;
  private List<Metadata> metadata;
  private NodeData nodeData;

  @XmlAttribute(name = "componentName")
  public String getComponentName() {
    return componentName;
  }

  public void setComponentName(String componentName) {
    this.componentName = componentName;
  }

  @XmlAttribute(name = "componentVersion")
  public String getComponentVersion() {
    return componentVersion;
  }

  public void setComponentVersion(String componentVersion) {
    this.componentVersion = componentVersion;
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

  @XmlAttribute(name = "posX")
  public int getPosX() {
    return posX;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  @XmlAttribute(name = "posY")
  public int getPosY() {
    return posY;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

  @XmlElement(name = "elementParameter")
  public List<ElementParameter> getElementParameter() {
    return elementParameter;
  }

  public void setElementParameter(List<ElementParameter> elementParameter) {
    this.elementParameter = elementParameter;
  }

  @XmlElement(name = "metadata")
  public List<Metadata> getMetadata() {
    return metadata;
  }

  public void setMetadata(List<Metadata> metadata) {
    this.metadata = metadata;
  }

  @XmlElement(name = "nodeData")
  public NodeData getNodeData() {
    return nodeData;
  }

  public void setNodeData(NodeData nodeData) {
    this.nodeData = nodeData;
  }

  public boolean isInputNode() {
    return componentName.contains(Constants.STR_Input) && metadata != null && metadata.size() > 0;
  }

  public boolean isOutputNode() {
    return componentName.contains(Constants.STR_Output) && metadata != null && metadata.size() > 0;
  }

  public boolean isTransformationNode() {
    return Constants.SUPPORTED_TRANSFORMATIONS_LIST.contains(componentName) && metadata != null && metadata.size() > 0;
  }

  public String getUniqueName() {
    if (elementParameter == null || elementParameter.size() == 0) return Constants.STR_NOT_FOUND;
    Optional<ElementParameter> elp =
        elementParameter.stream()
            .filter(
                elementParameter1 -> elementParameter1.getName().equals(Constants.STR_UNIQUE_NAME))
            .findFirst();
    return elp.map(ElementParameter::getValue).orElse(Constants.STR_NOT_FOUND);
  }

  public String getDBTableName() {
    if (elementParameter == null || elementParameter.size() == 0) return null;
    Optional<ElementParameter> elp = elementParameter.stream().filter(
            elementParameter1 -> Constants.STR_DBTABLE.equals(elementParameter1.getField()) &&
                    Constants.STR_TABLE.equals(elementParameter1.getName())
    ).findFirst();
    return elp.map(ElementParameter::getPureValue).orElse(null);
  }

  public String getTableNameFromLabel() {
    if (elementParameter == null || elementParameter.size() == 0) return null;
    Optional<ElementParameter> elp = elementParameter.stream().filter(
            elementParameter1 -> Constants.STR_TEXT.equals(elementParameter1.getField()) &&
                    Constants.STR_LABEL.equals(elementParameter1.getName())
    ).findFirst();
    return elp.map(ElementParameter::getPureValue).orElse(null);
  }

  public String getTableNameFromFile() {
    if (elementParameter == null || elementParameter.size() == 0) return null;
    Optional<ElementParameter> elp = elementParameter.stream().filter(
            elementParameter1 -> componentName.startsWith(Constants.STR_TFILE) && Constants.STR_FILE.equals(elementParameter1.getField()) &&
                    Constants.STR_FILENAME.equals(elementParameter1.getName())
    ).findFirst();
    return elp.map(ElementParameter::getFileName).orElse(null);
  }

  public String getNormalizedTableName() {
    String tableName = getTableNameFromLabel();
    if(tableName != null &&  tableName.trim().length() > 0) {
      return Utils.getNormalizedString(tableName);
    }
    tableName = getDBTableName();
    if(tableName != null &&  tableName.trim().length() > 0) {
      return Utils.getNormalizedString(tableName);
    }
    tableName = getTableNameFromFile();
    if (tableName != null && tableName.trim().length() > 0) {
      return Utils.getNormalizedString(tableName);
    }
    return  Utils.getNormalizedString(getUniqueName());
  }

  public String getRawSQL() {
    if (elementParameter == null || elementParameter.size() == 0) return null;
    Optional<ElementParameter> elp = elementParameter.stream().filter(
            elementParameter1 -> Constants.STR_MEMO_SQL.equals(elementParameter1.getField())
    ).findFirst();
    return elp.map(ElementParameter::getPureValue).orElse(null);
  }

  public Metadata getColumnMetadata() {
    if (metadata == null || metadata.size() == 0) return null;
    String uniqueName = StringUtils.stripToEmpty(getUniqueName());
    return metadata.stream()
        .filter(md -> uniqueName.equals(md.getName()) || Constants.CONNECTOR_TYPES.contains(md.getConnector()))
        .findFirst()
        .orElse(null);
  }

  public List<Column> getColumns() {
    Metadata md = getColumnMetadata();
    if (md == null) return new ArrayList<>();
    return md.getColumn();
  }

  @Override
  public String toString() {
    return "Node{"
        + "componentName='"
        + componentName
        + '\''
        + ", componentVersion='"
        + componentVersion
        + '\''
        + ", offsetLabelX="
        + offsetLabelX
        + ", offsetLabelY="
        + offsetLabelY
        + ", posX="
        + posX
        + ", posY="
        + posY
        + ", elementParameter="
        + elementParameter
        + ", metadata="
        + metadata
        + ", nodeData="
        + nodeData
        + '}';
  }
}
