package alation.talend.enums;

import sdk.core.ext.error.IOErrorCode;

public enum TalendErrorCode implements IOErrorCode {
  ACCESS_DENIED(4000, "Access denied", "Please verify the login credentials"),
  INTERNAL_SERVER_ERROR(4001, "Internal Server Error", "Please contact your admin!"),
  NO_USER_NAME(
      4002, "Invalid/No user_name, password are specified!", "Provide user_name, password"),
  NO_SERVER_URL(4004, "Invalid/No serverUrl is received!", "Check with administrator"),
  NO_FOLDER_PATH(4005, "No Folder path", "Please provide a valid local/remote path for xml files"),
  INVALID_FOLDER_PATH(
      4006, "Invalid Folder path.", "Please provide a valid local/remote path for xml files"),
  INVALID_XML_FILE(4007, "Skipping entry, as Invalid XML file is provided"),
  ERROR_READING_FOLDER(4008, "Error while reading local XML folder "),
  UNABLE_TO_CLONE_GIT_REPO(4008, "Unable to clone the GIT the repo "),
  NO_TOKEN(
          4002, "Invalid/No token specified!", "Provide valid generated token"),
  NP_CONNECTION(4009, "No connection"),
  LINEAGE_ERROR(4010, "Error while extracting lineage"),
  MDE_EXTRACTION_ERROR(4011, "Error while extracting metadata");

  private int code;
  private String description;
  private String hint;

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getHint() {
    return hint;
  }

  TalendErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  TalendErrorCode(int code, String description, String hint) {
    this.code = code;
    this.description = description;
    this.hint = hint;
  }

  @Override
  public String toString() {
    return this.getDescription();
  }
}
