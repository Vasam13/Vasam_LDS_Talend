package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Note {

  private boolean opaque;
  private int posX;
  private int posY;
  private int sizeHeight;
  private int sizeWidth;
  private String text;

  @XmlAttribute(name = "opaque")
  public boolean isOpaque() {
    return opaque;
  }

  public void setOpaque(boolean opaque) {
    this.opaque = opaque;
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

  @XmlAttribute(name = "sizeHeight")
  public int getSizeHeight() {
    return sizeHeight;
  }

  public void setSizeHeight(int sizeHeight) {
    this.sizeHeight = sizeHeight;
  }

  @XmlAttribute(name = "sizeWidth")
  public int getSizeWidth() {
    return sizeWidth;
  }

  public void setSizeWidth(int sizeWidth) {
    this.sizeWidth = sizeWidth;
  }

  @XmlAttribute(name = "text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "Note{"
        + "opaque="
        + opaque
        + ", posX="
        + posX
        + ", posY="
        + posY
        + ", sizeHeight="
        + sizeHeight
        + ", sizeWidth="
        + sizeWidth
        + ", text='"
        + text
        + '\''
        + '}';
  }
}
