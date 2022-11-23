package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class NodeData {
  private VarTables varTables;
  private String type;
  private OutputTables outputTables;
  private InputTables inputTables;

  @XmlElement(name = "varTables")
  public VarTables getVarTables() {
    return varTables;
  }

  public void setVarTables(VarTables varTables) {
    this.varTables = varTables;
  }

  @XmlAttribute(name = "xsi:type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @XmlElement(name = "outputTables")
  public OutputTables getOutputTables() {
    return outputTables;
  }

  public void setOutputTables(OutputTables outputTables) {
    this.outputTables = outputTables;
  }

  @XmlElement(name = "inputTables")
  public InputTables getInputTables() {
    return inputTables;
  }

  public void setInputTables(InputTables inputTables) {
    this.inputTables = inputTables;
  }

  @Override
  public String toString() {
    return "NodeData{"
        + "varTables="
        + varTables
        + ", type='"
        + type
        + '\''
        + ", outputTables="
        + outputTables
        + ", inputTables="
        + inputTables
        + '}';
  }
}
