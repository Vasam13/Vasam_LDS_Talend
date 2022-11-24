package alation.talend.datasource;

import alation.sdk.core.error.ConnectorException;
import alation.sdk.core.error.ExtractionException;
import alation.sdk.core.error.ValidationException;
import alation.sdk.core.request.auth.Auth;
import alation.sdk.core.stream.Stream;
import alation.sdk.core.stream.StreamException;
import alation.sdk.dl.mde.datasource.rdbms.DirectLineageExtraction;
import alation.sdk.dl.mde.models.LineageObjectType;
import alation.sdk.dl.mde.requests.rdbms.ExtractionRequest;
import alation.sdk.dl.mde.streams.DirectLineageExtractionMessage;
import alation.sdk.rdbms.configuration.RdbmsApplicationConfiguration;
import alation.sdk.rdbms.mde.customdb.MetadataStreamer;
import alation.sdk.rdbms.mde.customdb.streamer.FilterStreamer;
import alation.sdk.rdbms.mde.datasource.MetadataExtraction;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.sdk.rdbms.mde.models.TableId;
import alation.sdk.rdbms.mde.requests.TableExtractionRequest;
import alation.sdk.rdbms.mde.streams.FilterExtractionMessage;
import alation.sdk.rdbms.mde.streams.MetadataExtractionMessage;
import alation.sdk.rdbms.mde.streams.TableExtractionMessage;
import alation.talend.configuration.TalendAppConfig;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.enums.TalendErrorCode;
import alation.talend.io.TalendIOWrapper;
import alation.talend.mde.TalendMDEParameters;
import alation.talend.mde.extractor.TalendExtractor;
import alation.talend.mde.extractor.TalendMDExtractionArguments;
import org.apache.log4j.Logger;
import sdk.core.ext.datasource.IODatasource;

import java.util.Collections;
import java.util.List;

/**
 * This class is the entry point for the connector. It implements the IODatasource interface and the
 * MetadataExtraction interface
 */
public class TalendDatasource
        implements IODatasource<TalendConfiguration>, MetadataExtraction<TalendConfiguration>,
        DirectLineageExtraction<TalendConfiguration> {

    private static final Logger LOGGER = Logger.getLogger(TalendDatasource.class.getName());

    /**
     * It tries to get an authenticated instance of the Talend API. If it fails, it throws a
     * ConnectorException
     *
     * @param configuration The configuration object that contains the user's input.
     */
    @Override
    public void validate(TalendConfiguration configuration) throws ConnectorException {
        try {
            TalendIOWrapper.getAuthenticatedInstance(configuration);
        } catch (ValidationException ex) {
            LOGGER.error(TalendErrorCode.ACCESS_DENIED.getDescription(), ex);
        }
    }

    ;

    /**
     * This method called from Alation's gRPC VALIDATE call and performs talend instance authentication
     *
     * @param talendConfiguration This is the TalendConfiguration object that is passed to the plugin.
     * @param auth                The authentication object that contains the talend's source credentials.
     */
    @Override
    public void validate(TalendConfiguration talendConfiguration, Auth auth) {
    }

    /**
     * Returns the source name to include in the Manifest file.
     *
     * @return The name (e.g. Talend) of the datasource supported by this connector.
     */
    @Override
    public String getSourceName() {
        return "Talend";
    }


    /**
     * Returns connector name to include in the Manifest file.
     *
     * @return The connector's user friendly name to be displayed in UI.
     */
    @Override
    public String getConnectorName() {
        return "Talend";
    }

    /**
     * Returns connector version to include in the Manifest file.
     *
     * @return The connector's version string.
     */
    @Override
    public String getConnectorVersion() {
        return "1.0.0";
    }


    @Override
    public String getDescription() {
        return "This Talend connector supports MDE, QE, Compose and profiling.";
    }


    /**
     * Returns connector configuration to include in the Manifest file.
     *
     * @return The connector configuration to include in the manifest file.
     */
    @Override
    public TalendConfiguration getUserConfiguration() {
        return new TalendConfiguration();
    }

    /**
     * Creates a new configuration object initialized with the default values.
     *
     * @return The connector configuration {@link TalendConfiguration} initialized with the default values.
     */
    @Override
    public TalendConfiguration createConfiguration() {
        return new TalendConfiguration();
    }

    /**
     * Returns application configuration to include in the Manifest file.
     *
     * @return The application configuration {@link TalendAppConfig} used to tune Alation.
     */
    @Override
    public RdbmsApplicationConfiguration getApplicationConfiguration() {
        return new TalendAppConfig();
    }


    /**
     * It takes a TalendConfiguration object, which contains the connection information, and a stream of
     * FilterExtractionMessage objects, which contain the filter information, and it uses that information
     * to extract the data from the Talend server
     *
     * @param configuration The configuration object that contains the connection information.
     * @param stream        The stream of FilterExtractionMessage objects that will be used to filter the data.
     */
    @Override
    public void filterExtraction(
            TalendConfiguration configuration, Stream<FilterExtractionMessage> stream)
            throws ConnectorException {
        try {
            TalendIOWrapper ioWrapper = TalendIOWrapper.getAuthenticatedInstance(configuration);
            TalendMDEParameters parameters = new TalendMDEParameters(ioWrapper, configuration);
            TalendMDExtractionArguments extractionArgs =
                    new TalendMDExtractionArguments(configuration, Collections.emptyList(), true);
            new FilterStreamer(
                    parameters, new TalendExtractor(ioWrapper, parameters, extractionArgs),
                    stream).run();
        } catch (ValidationException ex) {
            LOGGER.error(TalendErrorCode.ACCESS_DENIED.getDescription(), ex);
        }
    }


    /**
     * This method called from Alation's gRPC METADATA_EXTRACTION call
     * and stream out the metadata containing Schemas, Tables &#38; Columns
     *
     * @param configuration     The configuration object that contains the connection information to the Talend
     *                          server.
     * @param schemaIds         The list of schemas to extract.
     * @param isInclusionFilter If true, the connector will only return the schemas that are in the
     *                          schemaIds list. If false, the connector will return all schemas except the ones in the schemaIds
     *                          list.
     * @param stream            The stream of messages to be sent to the connector.
     */
    @Override
    public void metadataExtraction(
            TalendConfiguration configuration,
            List<SchemaId> schemaIds,
            boolean isInclusionFilter,
            Stream<MetadataExtractionMessage> stream)
            throws ConnectorException {
        try {
            TalendIOWrapper ioWrapper = TalendIOWrapper.getAuthenticatedInstance(configuration);
            TalendMDEParameters parameters = new TalendMDEParameters(ioWrapper, configuration);
            TalendMDExtractionArguments extractionArgs =
                    new TalendMDExtractionArguments(configuration, schemaIds, isInclusionFilter);
            TalendExtractor talendExtractor = new TalendExtractor(ioWrapper, parameters, extractionArgs);
            new MetadataStreamer(parameters, talendExtractor , stream).streamObjects();
        } catch (ValidationException ex) {
            LOGGER.error(TalendErrorCode.ACCESS_DENIED.getDescription(), ex);
        }
    }

    /**
     * This method called from Alation's gRPC TABLE_EXTRACTION call
     * and stream out the metadata containing Schemas, Tables
     *
     * @param configuration The Talend configuration object.
     * @param tableId       The table ID of the table to extract metadata from.
     * @param stream        A stream of TableExtractionMessage objects. Each object contains the metadata for a
     *                      single table.
     */
    @Override
    public void tableExtraction(
            TalendConfiguration configuration, TableId tableId, Stream<TableExtractionMessage> stream)
            throws ConnectorException {
        MetadataExtraction.super.tableExtraction(configuration, tableId, stream);
    }


    /**
     * This method called from Alation's gRPC TABLE_EXTRACTION call
     * and stream out the metadata containing Schemas, Table
     *
     * @param request The request object that contains the configuration parameters.
     * @param stream  The stream of messages to be sent to the connector.
     */
    @Override
    public void tableExtraction(
            TableExtractionRequest<TalendConfiguration> request, Stream<TableExtractionMessage> stream)
            throws ConnectorException {
        MetadataExtraction.super.tableExtraction(request, stream);
    }

    @Override
    public void directLineageExtraction(
            ExtractionRequest<TalendConfiguration> extractionRequest,
            Stream<DirectLineageExtractionMessage> stream) {
        try {
            TalendConfiguration configuration = extractionRequest.getConfiguration();
            TalendIOWrapper ioWrapper = TalendIOWrapper.getAuthenticatedInstance(configuration);
            TalendMDEParameters parameters = new TalendMDEParameters(ioWrapper, configuration);
            TalendMDExtractionArguments extractionArgs =
                    new TalendMDExtractionArguments(configuration, extractionRequest.getSchemaIds(), extractionRequest.isIncludeFilter());
            new TalendExtractor(ioWrapper, parameters, extractionArgs).getLineagePaths().forEach(lineagePath -> {
                try {
                    stream.stream(lineagePath);
                } catch (StreamException ex) {
                    LOGGER.error(TalendErrorCode.LINEAGE_ERROR, ex);
                }
            });
        } catch (ExtractionException ex) {
            LOGGER.error(TalendErrorCode.MDE_EXTRACTION_ERROR.getDescription(), ex);
        } catch (ValidationException ex) {
            LOGGER.error(TalendErrorCode.ACCESS_DENIED.getDescription(), ex);
        }
    }
}
