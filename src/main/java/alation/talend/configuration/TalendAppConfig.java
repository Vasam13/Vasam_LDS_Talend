package alation.talend.configuration;

import sdk.core.ext.manifest.configuration.IOApplicationConfiguration;

/**
 * It's a class that extends the IOApplicationConfiguration class
 */
public class TalendAppConfig extends IOApplicationConfiguration {

    /**
     * This grammar is an ANSI grammar.
     *
     * @return The ParserGrammarType.ANSI enum value.
     */
    @Override
    public ParserGrammarType getParserGrammarType() {
        return ParserGrammarType.ANSI;
    }

    /**
     * This function returns true if the database supports default logging, false otherwise.
     *
     * @return true
     */
    @Override
    public boolean supportsDefaultDbLogging() {
        return true;
    }

    /**
     * The quote character used to quote identifiers.
     *
     * @return The identifier quote string.
     */
    @Override
    public String getIdentifierQuoteString() {
        return "\"";
    }

    /**
     * If you want to use the rollback feature, you must return true from this function.
     *
     * @return The method is returning a boolean value.
     */
    @Override
    public boolean supportsRollback() {
        return false;
    }

    /**
     * This connector does not support multiple result sets.
     *
     * @return The method supportsMultipleResultSets() is being returned.
     */
    @Override
    public boolean supportsMultipleResultSets() {
        return false;
    }

    /**
     * This connector does not support prepared statements.
     *
     * @return The return type is boolean.
     */
    @Override
    public boolean supportsPreparedStatement() {
        return false;
    }

    /**
     * This function returns null
     *
     * @return null
     */
    @Override
    public String explainQueryPrefix() {
        return null;
    }

    /**
     * This function returns null
     *
     * @return null
     */
    @Override
    public String currentSchemaQuery() {
        return null;
    }

    /**
     * This function returns null
     *
     * @return null
     */
    @Override
    public String checkConnectionQuery() {
        return null;
    }

    /**
     * This function returns false because this connector does not support concurrent queries.
     *
     * @return The method returns a boolean value.
     */
    @Override
    public boolean supportsConcurrentQueries() {
        return false;
    }

    /**
     * This function returns false because this connector does not support batch execution.
     *
     * @return The method returns a boolean value.
     */
    @Override
    public boolean supportsQueryBatchExecution() {
        return false;
    }

    /**
     * This function returns true if this connector supports Impersonation
     *
     * @return true
     */
    @Override
    public boolean supportsImpersonation() {
        return true;
    }

    /**
     * This function returns true if this connector supports incremental metadata extraction.
     *
     * @return true
     */
    @Override
    public boolean supportsIncrementalMetaDataExtraction() {
        return true;
    }
}
