package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class OutputTables {
  private String sizeState;
  private String name;
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

  @XmlElement(name = "mapperTableEntries")
  public List<MapperTableEntries> getMapperTableEntries() {
    return mapperTableEntries;
  }

  public void setMapperTableEntries(List<MapperTableEntries> mapperTableEntries) {
    this.mapperTableEntries = mapperTableEntries;
  }

  @Override
  public String toString() {
    return "OutputTables{"
        + "sizeState='"
        + sizeState
        + '\''
        + ", name='"
        + name
        + '\''
        + ", mapperTableEntries="
        + mapperTableEntries
        + '}';
  }
}
