package alation.talend.mde;

import alation.sdk.rdbms.mde.base.enums.MDType;
import alation.sdk.rdbms.mde.customdb.common.CustomMDESettings;
import alation.talend.configuration.TalendConfiguration;

/**
 * It's a class that implements the Custom Metadata Extraction Settings
 */
public class TalendMDESettings implements CustomMDESettings {

    private final TalendConfiguration configuration;

    public TalendMDESettings(TalendConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * This function returns false because the connector does not support catalogs.
     *
     * @return false
     */
    @Override
    public boolean hasCatalogSupport() {
        return false;
    }

    /**
     * This function returns true if this connector supports schemas.
     *
     * @return A boolean value.
     */
    @Override
    public boolean hasSchemaSupport() {
        return true;
    }

    /**
     * If you want to use the slower mode of extraction, return true here.
     *
     * @return true
     */
    @Override
    public boolean isSlowerModeExtraction() {
        return true;
    }

    // This function is used to determine which metadata types are supported by this connector.
    @Override
    public boolean isSupportedMDType(MDType type) {
        switch (type) {
            case TABLE:
                return true;
            case PK:
                return false;
            // return this.configuration.extractPrimaryKeys();
            case FK:
                return false;
            // return this.configuration.extractForeignKeys();
            case INDEX:
                return false;
            // return this.configuration.extractIndexes();
            case FUNCTION:
                return false;
            case FUNCTION_DEF:
                return false;
            // return this.configuration.extractFunctions();
            case SYNONYM:
                return false;
            // return this.configuration.extractSynonyms();
            default:
                return false;
        }
    }

    /**
     * This function returns false because this connector is not query based.
     *
     * @return The return value is a boolean.
     */
    @Override
    public boolean isExtractionQueryBased() {
        return false;
    }

    /**
     * This function returns false, because Talend doesn't support function extraction.
     *
     * @return The return value is a boolean.
     */
    @Override
    public boolean supportsFunctionExtraction() {
        return false;
    }

    /**
     * Returns the maximum size of a SQL statement that can be sent to the server.
     *
     * @return The maximum size of a SQL statement.
     */
    @Override
    public int getMaxSqlStatementSize() {
        return this.configuration.getMaxSqlStatementSize();
    }
}
