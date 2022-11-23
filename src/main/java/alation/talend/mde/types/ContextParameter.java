package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ContextParameter {
  private String comment;
  private String internalId;
  private String name;
  private String prompt;
  private boolean promptNeeded;
  private String repositoryContextId;
  private String type;
  private String value;

  @XmlAttribute(name = "internalId")
  public String getInternalId() {
    return internalId;
  }

  public void setInternalId(String internalId) {
    this.internalId = internalId;
  }

  @XmlAttribute(name = "prompt")
  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public boolean isPromptNeeded() {
    return promptNeeded;
  }

  public void setPromptNeeded(boolean promptNeeded) {
    this.promptNeeded = promptNeeded;
  }

  @XmlAttribute(name = "repositoryContextId")
  public String getRepositoryContextId() {
    return repositoryContextId;
  }

  public void setRepositoryContextId(String repositoryContextId) {
    this.repositoryContextId = repositoryContextId;
  }

  @XmlAttribute(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @XmlAttribute(name = "value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @XmlAttribute(name = "comment")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @XmlAttribute(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "ContextParameter{"
        + "comment='"
        + comment
        + '\''
        + ", internalId='"
        + internalId
        + '\''
        + ", name='"
        + name
        + '\''
        + ", prompt='"
        + prompt
        + '\''
        + ", promptNeeded="
        + promptNeeded
        + ", repositoryContextId='"
        + repositoryContextId
        + '\''
        + ", type='"
        + type
        + '\''
        + ", value='"
        + value
        + '\''
        + '}';
  }
}
