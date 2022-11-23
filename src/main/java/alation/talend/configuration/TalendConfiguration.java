package alation.talend.configuration;

import alation.sdk.core.manifest.FeatureEnum;
import alation.sdk.core.manifest.parameter.EncryptedTextParam;
import alation.sdk.core.manifest.parameter.Parameter;
import alation.sdk.core.manifest.parameter.SelectParam;
import alation.sdk.core.manifest.parameter.TextParam;
import alation.sdk.core.request.AbstractConfiguration;
import alation.talend.resources.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class used to build the parameters that are used in the configuration/Mainfest file.
 */
public class TalendConfiguration extends AbstractConfiguration {

    private static final String GROUP_LOGGING_CONFIGURATION = "Logging configuration";
    private static final String GROUP_DS_CONNECTION = "Datasource Connection";

    public TalendConfiguration() {
        super();
    }

    // This function is used to build the parameters that are used in the configuration/Mainfest file.
    @Override
    public List<Parameter> buildParameters() {
        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(
                new SelectParam(
                        Constants.XML_SOURCE,
                        "Source Type",
                        "Source Type",
                        FeatureEnum.RDBMS_GENERAL,
                        Constants.SOURCE_TYPES[1],
                        GROUP_DS_CONNECTION,
                        Arrays.asList(Constants.SOURCE_TYPES)));
        parameterList.add(
                new TextParam(
                        Constants.XML_FOLDER_URL,
                        "Path/URL",
                        "XML Folder URL/Path",
                        FeatureEnum.RDBMS_GENERAL,
                        null,
                        GROUP_DS_CONNECTION));
        parameterList.add(
                new EncryptedTextParam(
                        Constants.TOKEN,
                        "Access Token (GitHub > Settings > Developer settings > Personal access token)",
                        "Enter generated token",
                        FeatureEnum.RDBMS_GENERAL,
                        GROUP_DS_CONNECTION));
        parameterList.add(
                new SelectParam(
                        Constants.LOG_LEVEL,
                        "Log level",
                        "Logging level to be used in the application.",
                        FeatureEnum.RDBMS_GENERAL,
                        "INFO",
                        GROUP_LOGGING_CONFIGURATION,
                        Arrays.asList("INFO", "DEBUG", "WARN", "TRACE", "ERROR", "FATAL", "ALL")));
        return parameterList;
    }

    /**
     * If the user has specified a logging level, use it, otherwise use the default
     *
     * @return The log level.
     */
    @Override
    public Level getLogLevel() {
        String logLevel = getLoggingLevel();
        if (StringUtils.isNotEmpty(logLevel)) {
            return Level.toLevel(logLevel, Level.INFO);
        }
        return super.getLogLevel();
    }

    /**
     * This function returns the value of the `source` parameter in the `select` tag
     *
     * @return The source type of the XML file.
     */
    public String getSourceType() {
        return this.getSelectParam(Constants.XML_SOURCE);
    }

    /**
     * This function returns the value of the XML_FOLDER_URL parameter from the config.properties file
     *
     * @return The value of the XML_FOLDER_URL parameter.
     */
    public String getXMLFolderUrl() {
        return this.getTextParam(Constants.XML_FOLDER_URL);
    }

    /**
     * It returns the access token from the encrypted text parameter
     *
     * @return The access token
     */
    public String getAccessToken() {
        return this.getEncryptedTextParam(Constants.TOKEN);
    }

    /**
     * This function returns the value of the `loggingLevel` parameter
     *
     * @return The value of the select parameter named Constants.LOG_LEVEL.
     */
    public String getLoggingLevel() {
        return this.getSelectParam(Constants.LOG_LEVEL);
    }
}
