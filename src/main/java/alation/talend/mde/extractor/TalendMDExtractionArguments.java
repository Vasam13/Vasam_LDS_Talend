package alation.talend.mde.extractor;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.rdbms.mde.customdb.extractor.ExtractionArguments;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.configuration.TalendConfiguration;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * It's a class that implements the ExtractionArguments interface
 */
public class TalendMDExtractionArguments implements ExtractionArguments {

    protected TalendConfiguration configuration;

    /**
     * Determine if the list of filter names to be included or excluded during metadata extraction
     */
    private final boolean isInclusionFilter;
    /**
     * Schema Ids to be extracted/skipped for filtered extraction. For full extraction, it will be
     * empty.
     */
    private final List<SchemaId> filterNames;

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public TalendMDExtractionArguments(
            TalendConfiguration configuration, List<SchemaId> filterNames, boolean isInclusionFilter)
            throws ExtractionException {
        this.configuration = configuration;
        this.filterNames = filterNames;
        this.isInclusionFilter = isInclusionFilter;
    }

    /**
     * Returns the list of filter names that are used to filter the data.
     *
     * @return The list of filter names.
     */
    @Override
    public List<SchemaId> getFilterNames() {
        return this.filterNames;
    }

    /**
     * Returns true if this is an inclusion filter, false if it's an exclusion filter.
     *
     * @return The value of the isInclusionFilter field.
     */
    @Override
    public boolean isInclusionFilter() {
        return this.isInclusionFilter;
    }
}
