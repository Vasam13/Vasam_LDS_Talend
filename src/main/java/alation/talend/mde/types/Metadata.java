package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class Metadata {
    private String connector;
    private String label;
    private String name;
    private List<Column> column;

    @XmlAttribute(name = "connector")
    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    @XmlAttribute(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "column")
    public List<Column> getColumn() {
    return column;
    }

    public void setColumn(List<Column> column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "connector='" + connector + '\'' +
                ", label='" + label + '\'' +
                ", name='" + name + '\'' +
                ", column=" + column +
                '}';
    }
}
