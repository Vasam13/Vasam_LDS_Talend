package alation.talend.mde.types;

import alation.talend.resources.Constants;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ElementParameter {
    private String field;
    private String name;
    private boolean show;
    private String value;
    private List<ElementValue> elementValue;

    @XmlAttribute(name = "field")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "show")
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        if (value != null && Constants.SUPPORTED_TRANSFORMATIONS_LIST.contains(value.contains(Constants.SYMBOL_UNDERSCORE) ? value.substring(0, value.indexOf(Constants.SYMBOL_UNDERSCORE)) : value)) {
            value.replaceFirst(Constants.STR_T, Constants.TRANSFORMATION_REPLACE_NAME);
        }
        return value;
    }

    public String getPureValue() {
        if(value != null) {
            String val = value.replaceAll(Constants.SYMBOL_QUOT, Constants.EMPTY);
            val = val.replaceAll(Constants.SYMBOL_DOT, Constants.SYMBOL_UNDERSCORE);
            if(!StringUtils.isEmpty(val) && val.trim().length() > 0) {
                return val;
            }
        }
        return null;
    }

    public String getFileName(){
        if (StringUtils.isEmpty(value)) return null;
        value = value.replaceAll(Constants.SYMBOL_QUOT, Constants.EMPTY);
        File file = new File(value);
        return file.getName();
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlElement(name="elementValue")
    public List<ElementValue> getElementValue() {
        return elementValue;
    }

    public void setElementValue(List<ElementValue> elementValue) {
        this.elementValue = elementValue;
    }

    public List<ElementValue> filterByElementRef(String filter) {
        return elementValue.stream()
                .filter(elementValue -> filter.equals(elementValue.getElementRef()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ElementParameter{" +
                "field='" + field + '\'' +
                ", name='" + name + '\'' +
                ", show=" + show +
                ", value='" + value + '\'' +
                ", elementValue=" + elementValue +
                '}';
    }
}
