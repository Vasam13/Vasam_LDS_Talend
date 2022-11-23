package alation.talend.io.git;

/**
 * This is a POJO, represents the configuration of a Git repository
 */
public class GitConfig {
  public String getUrl() {
    return url;
  }

  public void setUrl(String gitURL) {
    this.url = gitURL;
  }

  public boolean isUseSSH() {
    return useSSH;
  }

  public void setUseSSH(boolean useSSH) {
    this.useSSH = useSSH;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String remoteBranch) {
    this.branch = remoteBranch;
  }

  public boolean isUsePersonalAccessToken() {
    return usePersonalAccessToken;
  }

  public void setUsePersonalAccessToken(boolean usePersonalAccessToken) {
    this.usePersonalAccessToken = usePersonalAccessToken;
  }

  public String getPersonalAccessToken() {
    return personalAccessToken;
  }

  public void setPersonalAccessToken(String personalAccessToken) {
    this.personalAccessToken = personalAccessToken;
  }

  private String url;
  private boolean useSSH;
  private String userId;
  private String userPassword;
  private String branch;

  private boolean usePersonalAccessToken;
  private String personalAccessToken;
}
