package alation.talend.mde.types;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "talendfile:ProcessType")
public class ProcessType {

  private String jobName;
  private String jobType;
  private Context context;
  private Parameters parameters;
  private List<Node> node;
  private List<Connection> connection;
  private List<Subjob> subjob;
  private List<Note> note;

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  @XmlAttribute(name = "jobType")
  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  @XmlElement(name = "parameters")
  public Parameters getParameters() {
    return parameters;
  }

  public void setParameters(Parameters parameters) {
    this.parameters = parameters;
  }

  @XmlElement(name = "context")
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @XmlElement(name = "node")
  public List<Node> getNode() {
    return node;
  }

  public void setNode(List<Node> node) {
    this.node = node;
  }

  @XmlElement(name = "connection")
  public List<Connection> getConnection() {
    return connection;
  }

  public void setConnection(List<Connection> connection) {
    this.connection = connection;
  }

  @XmlElement(name = "subjob")
  public List<Subjob> getSubjob() {
    return subjob;
  }

  public void setSubjob(List<Subjob> subjob) {
    this.subjob = subjob;
  }

  @XmlElement(name = "note")
  public List<Note> getNote() {
    return note;
  }

  public void setNote(List<Note> note) {
    this.note = note;
  }

  public List<Node> getInputNodes() {
    if (node == null || node.size() == 0) return new ArrayList<>();
    return node.parallelStream().filter(Node::isInputNode).collect(Collectors.toList());
  }

  public List<Node> getOutputNodes() {
    if (node == null || node.size() == 0) return new ArrayList<>();
    return node.parallelStream().filter(Node::isOutputNode).collect(Collectors.toList());
  }

  public List<Node> getTransformationNodes() {
    if (node == null || node.size() == 0) return new ArrayList<>();
    return node.parallelStream().filter(Node::isTransformationNode).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "ProcessType{"
        + "jobType='"
        + jobType
        + '\''
        + ", context="
        + context
        + ", parameters="
        + parameters
        + ", node="
        + node
        + ", connection="
        + connection
        + ", subjob="
        + subjob
        + ", note="
        + note
        + '}';
  }
}
