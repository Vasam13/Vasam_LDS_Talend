package alation.talend.io;

import alation.sdk.core.error.ConnectorException;
import alation.sdk.core.error.ExtractionException;
import alation.sdk.core.error.ValidationException;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.enums.TalendErrorCode;
import alation.talend.io.git.GitHelper;
import alation.talend.io.util.TalendXMLReader;
import alation.talend.mde.types.ProcessType;
import alation.talend.resources.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * It creates a new TalendIOWrapper object, checks the configuration, creates a new TalendXMLReader
 * and validates the configuration
 */
public class TalendIOWrapper {

    private static final Logger LOGGER = Logger.getLogger(TalendIOWrapper.class.getName());

    private TalendConfiguration configuration;

    private TalendXMLReader xmlReader;

    @Contract(pure = true)
    private TalendIOWrapper() {}

    /**
     * It creates a new TalendIOWrapper object, checks the configuration, creates a new TalendXMLReader
     * object, and validates the configuration
     *
     * @param configuration This is the TalendConfiguration object that contains the information about the
     *                      Talend server.
     * @return A TalendIOWrapper object
     */
    public static TalendIOWrapper getAuthenticatedInstance(TalendConfiguration configuration)
            throws ValidationException {
        TalendIOWrapper ioWrapper;
        try {
            ioWrapper = new TalendIOWrapper();
            ioWrapper.checkConfigInfo(configuration);
            ioWrapper.xmlReader = new TalendXMLReader(configuration);
            ioWrapper.configuration = configuration;
            ioWrapper.validate();
        } catch (Exception ex) {
            LOGGER.error(TalendErrorCode.ACCESS_DENIED.getDescription(), ex);
            throw new ValidationException(ex);
        }
        return ioWrapper;
    }

    /**
     * This function checks if the configuration is valid
     *
     * @param configuration The configuration object that contains the parameters you set in the UI.
     */
    private void checkConfigInfo(TalendConfiguration configuration) throws ConnectorException {
        String sourceType = configuration.getSourceType();
        if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Local_Folder)) {
            if (StringUtils.isEmpty(configuration.getXMLFolderUrl())) {
                LOGGER.error(TalendErrorCode.NO_FOLDER_PATH.getDescription());
                throw new ConnectorException(TalendErrorCode.NO_FOLDER_PATH);
            }
        }
        if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Github)
                || Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Remote_Server)) {
            if (StringUtils.isEmpty(configuration.getAccessToken())) {
                LOGGER.error(TalendErrorCode.NO_USER_NAME.getDescription());
                throw new ConnectorException(TalendErrorCode.NO_USER_NAME);
            }
            if (StringUtils.isEmpty(configuration.getXMLFolderUrl())) {
                LOGGER.error(TalendErrorCode.NO_SERVER_URL.getDescription());
                throw new ConnectorException(TalendErrorCode.NO_SERVER_URL);
            }
        }
    }

    /**
     * If the source type is Local Folder, validate the local folder path. If the source type is Github,
     * validate the Github configuration. If the source type is Remote Server, validate the Remote Server
     * configuration
     */
    public void validate() throws Exception {
        String sourceType = configuration.getSourceType();
        if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Local_Folder)) {
            validateLocalFolderPath();
        } else if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Github)) {
            validateGithub();
        } else if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Remote_Server)) {
            validateRemoteServer();
        }
    }

    /**
     * If the folder path is not a directory, throw an exception
     */
    private void validateLocalFolderPath() throws ConnectorException {
        if (!Files.isDirectory(Path.of(configuration.getXMLFolderUrl()))) {
            LOGGER.error(TalendErrorCode.INVALID_FOLDER_PATH.getDescription());
            throw new ConnectorException(TalendErrorCode.INVALID_FOLDER_PATH);
        }
    }

    /**
     * Clone the Git repo, then destroy the repo.
     */
    // Cloning the repo, then destroying the repo.
    private void validateGithub() throws Exception {
        File tempFolder = Utils.createUniqueTempFolder();
        GitHelper gitHelper = new GitHelper(configuration, tempFolder);
        gitHelper.cloneRepo();
        gitHelper.destroyRepo();
    }

    /**
     * If the user has selected a remote server, validate that the remote server is valid.
     */
    // This function is not yet implemented.
    private void validateRemoteServer() throws Exception {
        // TODO: Validate Git/Network path
        LOGGER.error("Remote server feature is not yet available!");
        throw new Exception("Remote server feature is not yet available!");
    }

    /**
     * This function extracts the metadata from the source and returns a list of ProcessType objects
     *
     * @param schemaIds       A list of schemaIds that you want to extract.
     * @param isIncludeFilter If true, only the jobs that are in the include filter will be extracted. If
     *                        false, only the jobs that are in the exclude filter will be extracted.
     * @return A list of ProcessType objects.
     */
    public List<ProcessType> extractMD(List<SchemaId> schemaIds, boolean isIncludeFilter) throws ExtractionException {
        List<ProcessType> jobs;
        String sourceType = configuration.getSourceType();
        if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Local_Folder)) {
            jobs = xmlReader.getJobsFromLocalFolder(schemaIds, isIncludeFilter);
        } else if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Github)) {
            jobs = xmlReader.getJobsFromGitRepo(schemaIds, isIncludeFilter);
        } else if (Objects.equals(sourceType, Constants.ENUM_SOURCE_TYPES.Remote_Server)) {
            jobs = xmlReader.getJobsFromRemoteServer();
        } else {
            jobs = new ArrayList<>();
        }
        return jobs;
    }
}
