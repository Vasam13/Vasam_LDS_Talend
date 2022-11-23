package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class InputTables {
  private String sizeState;
  private String name;
  private String matchingMode;
  private String lookupMode;
  private List<MapperTableEntries> mapperTableEntries;

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

  @XmlAttribute(name = "matchingMode")
  public String getMatchingMode() {
    return matchingMode;
  }

  public void setMatchingMode(String matchingMode) {
    this.matchingMode = matchingMode;
  }

  @XmlAttribute(name = "lookupMode")
  public String getLookupMode() {
    return lookupMode;
  }

  public void setLookupMode(String lookupMode) {
    this.lookupMode = lookupMode;
  }

  @XmlElement(name = "mapperTableEntries")
  public List<MapperTableEntries> getMapperTableEntries() {
    return mapperTableEntries;
  }

  public void setMapperTableEntries(List<MapperTableEntries> mapperTableEntries) {
    this.mapperTableEntries = mapperTableEntries;
  }

  @Override
  public String toString() {
    return "InputTables{"
        + "sizeState='"
        + sizeState
        + '\''
        + ", name='"
        + name
        + '\''
        + ", matchingMode='"
        + matchingMode
        + '\''
        + ", lookupMode='"
        + lookupMode
        + '\''
        + ", mapperTableEntries="
        + mapperTableEntries
        + '}';
  }
}
