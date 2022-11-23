package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class Parameters {

    List<ElementParameter> elementParameter;
    List<RoutinesParameter> routinesParameter;

    @XmlElement(name = "elementParameter")
    public List<ElementParameter> getElementParameter() {
        return elementParameter;
    }

    public void setElementParameter(List<ElementParameter> elementParameter) {
        this.elementParameter = elementParameter;
    }

    @XmlElement(name = "routinesParameter")
    public List<RoutinesParameter> getRoutinesParameter() {
        return routinesParameter;
    }

    public void setRoutinesParameter(List<RoutinesParameter> routinesParameter) {
        this.routinesParameter = routinesParameter;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "elementParameter=" + elementParameter +
                ", routinesParameter=" + routinesParameter +
                '}';
    }
}
