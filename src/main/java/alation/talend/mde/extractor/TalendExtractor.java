package alation.talend.mde.extractor;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.dl.mde.models.LineagePath;
import alation.sdk.rdbms.mde.customdb.extractor.DatabaseMetaDataExtractor;
import alation.sdk.rdbms.mde.customdb.extractor.ExtractionArguments;
import alation.sdk.rdbms.mde.customdb.iterables.DbObjectIterable;
import alation.sdk.rdbms.mde.models.Column;
import alation.sdk.rdbms.mde.models.Schema;
import alation.sdk.rdbms.mde.models.Table;
import alation.talend.io.TalendIOWrapper;
import alation.talend.mde.TalendMDEParameters;
import alation.talend.mde.TalendToAlationMDMapper;
import alation.talend.mde.extractor.iterators.TalendColumnIterable;
import alation.talend.mde.extractor.iterators.TalendSchemaIterable;
import alation.talend.mde.extractor.iterators.TalendTableIterable;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * The TalendExtractor class is the main class that the Alation MDE calls to extract metadata from the
 * Talend repository
 */
public class TalendExtractor extends DatabaseMetaDataExtractor {

    private static final int max_schema_log_count = 100;
    private static final int max_table_log_count = 100;
    private static final int max_column_log_count = 100;
    private final Logger LOGGER = Logger.getLogger(this.getClass());
    TalendToAlationMDMapper mdMapper;

    public TalendExtractor(
            TalendIOWrapper ioWrapper,
            TalendMDEParameters parameters,
            ExtractionArguments extractionAgs) {
        super(parameters, extractionAgs);
        mdMapper = new TalendToAlationMDMapper(ioWrapper, extractionAgs.getFilterNames(), extractionAgs.isInclusionFilter());
    }

    /**
     * This method is called by the Alation MDE to get the list of schemas to extract.
     *
     * @return A DbObjectIterable of Schema objects.
     */
    @Override
    public DbObjectIterable<Schema> getSchemas() throws ExtractionException {
        Schema[] schemas = mdMapper.extractSchemas();
        LOGGER.info("Found " + schemas.length + " schemas ready to export");
        for (int i = 0; i < Math.min(schemas.length, max_schema_log_count); i++) {
            LOGGER.info((i + 1) + " Schema=" + schemas[i].getId().getName().getOriginal());
        }
        return (DbObjectIterable) new TalendSchemaIterable(schemas);
    }

    /**
     * This method is called by the Alation MDE to get the list of tables to extract.
     *
     * @return A DbObjectIterable of Table objects.
     */
    @Override
    public DbObjectIterable<Table> getTables() throws ExtractionException {
        Table[] tables = mdMapper.extractTables();
        LOGGER.info("Found " + tables.length + " tables ready to export");
        for (int i = 0; i < Math.min(tables.length, max_table_log_count); i++) {
            LOGGER.info(
                    (i + 1)
                            + " Table="
                            + tables[i].getId().getName().getOriginal()
                            + ", Schema="
                            + tables[i].getId().getSchema().getOriginal());
        }
        return (DbObjectIterable) new TalendTableIterable(tables);
    }

    /**
     * This method is called by the Alation MDE to get the list of columns to extract.
     *
     * @return A list of columns.
     */
    @Override
    public DbObjectIterable<Column> getColumns() throws ExtractionException {
        Column[] columns = mdMapper.extractColumns();
        LOGGER.info("Found " + columns.length + " columns ready to export");
        for (int i = 0; i < Math.min(columns.length, max_column_log_count); i++) {
            LOGGER.info(
                    (i + 1)
                            + " Column="
                            + columns[i].getId().getName()
                            + " ColumnType="
                            + columns[i].getType()
                            + " Position="
                            + columns[i].getPosition()
                            + " Table="
                            + columns[i].getId().getTable().getOriginal()
                            + ", Schema="
                            + columns[i].getId().getSchema().getOriginal());
        }
        return (DbObjectIterable) new TalendColumnIterable(columns);
    }

    public DbObjectIterable<Schema> getFilterNames() throws ExtractionException {
        return getSchemas();
    }

    public List<LineagePath> getLineagePaths() {
        return mdMapper.extractLineagePaths();
    }
}
