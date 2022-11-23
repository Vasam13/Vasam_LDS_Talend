package alation.talend.mde;

import alation.sdk.core.error.ConnectionException;
import alation.sdk.core.error.ExtractionException;
import alation.sdk.rdbms.mde.base.util.normalizer.CustomMDENormalizer;
import alation.sdk.rdbms.mde.customdb.common.CustomMDEParameters;
import alation.sdk.rdbms.mde.customdb.common.CustomMDESettings;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.io.TalendIOWrapper;

import java.sql.DatabaseMetaData;

/**
 * It's a class that implements the Custom Meta data extraction parameters
 */
public class TalendMDEParameters implements CustomMDEParameters {

    private final TalendConfiguration configuration;
    TalendIOWrapper ioWrapper;

    public TalendMDEParameters(TalendIOWrapper ioWrapper, TalendConfiguration configuration) {
        this.ioWrapper = ioWrapper;
        this.configuration = configuration;
    }

    /**
     * This function returns a java.sql.Connection object, or throws a ConnectionException if it can't.
     *
     * @return null
     */
    @Override
    public java.sql.Connection getConnection() throws ConnectionException {
        return null;
    }

    /**
     * Return a new instance of the TalendMDESettings class, which is a subclass of CustomMDESettings.
     *
     * @return A new instance of TalendMDESettings
     */
    @Override
    public CustomMDESettings getSettings() {
        return new TalendMDESettings(configuration);
    }

    /**
     * Return a new instance of the TalendMDENormalizer class.
     *
     * @return A new instance of the TalendMDENormalizer class.
     */
    @Override
    public CustomMDENormalizer getNormalizer() {
        return new TalendMDENormalizer();
    }

    /**
     * It returns the metadata of the database.
     *
     * @return The return type is java.sql.DatabaseMetaData.
     */
    @Override
    public DatabaseMetaData getMetaData() throws ExtractionException {
        // List<String> mappings = this.apiWrapper.getMappingNames();
        // Schema[] schemaList = InformaticaToAlationMDMapper.mappingToSchema(mappings);
        // TODO: To be discuss on how to create custom java.sql.DatabaseMetaData object
        return CustomMDEParameters.super.getMetaData();
    }
}
