package alation.talend.mde;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.core.models.DataSource;
import alation.sdk.core.models.Self;
import alation.sdk.dl.mde.models.LineageObject;
import alation.sdk.dl.mde.models.LineagePath;
import alation.sdk.dl.mde.models.NativeObject;
import alation.sdk.rdbms.mde.models.*;
import alation.talend.io.TalendIOWrapper;
import alation.talend.mde.transformations.ExpressionEngine;
import alation.talend.mde.types.Node;
import alation.talend.mde.types.ProcessType;
import alation.talend.resources.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * It takes a Talend job and converts it to an Alation's schema, table, and column objects
 */
public class TalendToAlationMDMapper {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private List<ProcessType> jobs;

    public TalendToAlationMDMapper(TalendIOWrapper apiWrapper, List<SchemaId> schemaIds, boolean isIncludeFilter) {
        try {
            jobs = apiWrapper.extractMD(schemaIds, isIncludeFilter);
        } catch (ExtractionException e) {
            LOGGER.error("Error while TalendToAlationMDMapper", e);
        }
    }

    /**
     * Constructs the Alation schemas from the Talend jobs
     *
     * @return An array of schemas.
     */
    public Schema[] extractSchemas() {
        if (jobs == null || jobs.size() == 0) return new Schema[0];
        Schema[] schemas = new Schema[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            schemas[i] = jobToSchema(jobs.get(i));
        }
        return schemas;
    }

    /**
     * For each job, create a schema, create a map of processed table names, and then for each input,
     * output, and transformation node, create a table and add it to the list of tables
     *
     * @return An array of tables.
     */
    public Table[] extractTables() {
        if (jobs == null || jobs.size() == 0) return new Table[0];
        List<Table> tables = new ArrayList<>();
        jobs.forEach(job -> tables.addAll(extractTables(job)));
        return tables.toArray(new Table[0]);
    }

    public List<Table> extractTables(ProcessType job) {
        List<Table> tables = new ArrayList<>();
        Schema schema = jobToSchema(job);
        Map<String, Integer> processedTableNameMap = new HashMap<>();
        job.getInputNodes()
                .forEach(inputNode -> tables.add(nodeToTable(schema, inputNode, processedTableNameMap)));
        job.getOutputNodes()
                .forEach(outputNode -> tables.add(nodeToTable(schema, outputNode, processedTableNameMap)));
        job.getTransformationNodes()
                .forEach(transformationNode -> {
                    new ExpressionEngine(job);
                    tables.add(nodeToTable(schema, transformationNode, processedTableNameMap));
                });
        return tables;
    }

    /**
     * For each job, extract the columns from the input, output and transformation nodes
     *
     * @return Columns
     */
    public Column[] extractColumns() {
        if (jobs == null || jobs.size() == 0) return new Column[0];
        List<Column> columns = new ArrayList<>();
        jobs
                .forEach(
                        job -> {
                            Schema schema = jobToSchema(job);
                            Map<String, Integer> processedTableNames = new HashMap<>();
                            job.getInputNodes()
                                    .forEach(node -> extractColumnsIntrl(schema, node, columns, processedTableNames));
                            job.getOutputNodes()
                                    .forEach(node -> extractColumnsIntrl(schema, node, columns, processedTableNames));
                            job.getTransformationNodes()
                                    .forEach(node -> extractColumnsIntrl(schema, node, columns, processedTableNames));
                        });
        return columns.toArray(new Column[0]);
    }

    /**
     * If the column type is an object, then the source type is used. Otherwise, type is used
     * default Alation's type is used
     *
     * @param _column The column object from the Talend metadata.
     * @return A ColumnType object
     */
    private ColumnType getColumnType(alation.talend.mde.types.Column _column) {
        ColumnType columnType = new ColumnType();
        switch (_column.getType()) {
            case Constants.COLUMN_id_Integer:
            case Constants.COLUMN_id_Short:
                columnType.setNormalized(ColumnType.NormalizedType.INT);
                break;
            case Constants.COLUMN_id_BigDecimal:
            case Constants.COLUMN_id_Float:
                columnType.setNormalized(ColumnType.NormalizedType.FLOAT);
                break;
            case Constants.COLUMN_id_Date:
                columnType.setNormalized(ColumnType.NormalizedType.TIMESTAMP);
                break;
            case Constants.COLUMN_id_Boolean:
                columnType.setNormalized(ColumnType.NormalizedType.BOOL);
                break;
            default:
                columnType.setNormalized(ColumnType.NormalizedType.STRING);
        }

        if (!StringUtils.isEmpty(_column.getType())) {
            if (Constants.COLUMN_id_Object.equals(_column.getType())) {
                columnType.setName(_column.getSourceType());
            } else {
                columnType.setName(_column.getNormalizedType());
            }
        } else {
            columnType.setName(String.valueOf(columnType.getNormalized()));
        }
        return columnType;
    }

    /**
     * This function takes a schema, a node, a list of columns, and a map of processed nodes, and adds
     * the columns of the node to the list of columns
     *
     * @param schema       The schema object that we created earlier
     * @param node         The node object that contains the column information
     * @param columns      The list of columns to be returned
     * @param processedMap This is a map of all the tables that have been processed.
     */
    private void extractColumnsIntrl(Schema schema, Node node, List<Column> columns, Map<String, Integer> processedMap) {
        Table table = nodeToTable(schema, node, processedMap);
        List<alation.talend.mde.types.Column> _columns = node.getColumns();
        int index = 0;
        for (alation.talend.mde.types.Column _column : _columns) {
            Column column = fieldToColumn(schema, table, _column);
            column.setNullable(_column.isNullable());
            column.setType(getColumnType(_column));
            column.setPosition(index);
            // TODO: Set column precision & scale
            columns.add(column);
            index++;
        }
    }

    /**
     * It creates a new Column object, and sets its ID to the schema name, table name, and column name
     *
     * @param schema  The schema object that the table belongs to
     * @param table   The table object that the column belongs to
     * @param _column The column object from the Talend job
     * @return A Column object.
     */
    private Column fieldToColumn(
            Schema schema, Table table, alation.talend.mde.types.Column _column) {
        return new Column(
                new ColumnId(
                        schema.getId().getName(),
                        table.getId().getName(),
                        new Name(_column.getNormalizedName())));
    }

    /**
     * This function takes a node from the talend job and creates a table object from it
     *
     * @param schema              The schema object that is being populated.
     * @param node                The node object that is being processed.
     * @param processedTableNames This is a map of table names to the number of times they have been
     *                            processed. This is used to ensure that the table names are unique.
     * @return A table object is being returned.
     */
    private Table nodeToTable(Schema schema, Node node, Map<String, Integer> processedTableNames) {
        String normalizedTableName = node.getNormalizedTableName();
        int count = 1;
        String modifiedTableName = normalizedTableName;
        if (processedTableNames.containsKey(normalizedTableName)) {
            count = processedTableNames.get(normalizedTableName);
            modifiedTableName += Constants.SYMBOL_UNDERSCORE + count;
            count++;
        }
        processedTableNames.put(normalizedTableName, count);
        Table table =
                new Table(new TableId(schema.getId().getName(), new Name(modifiedTableName)));
        table.setOwner(node.getUniqueName());
        String rawSQL = node.getRawSQL();
        if (rawSQL != null && rawSQL.trim().length() > 0) {
            table.setSourceComment(rawSQL);
        } else {
            table.setSourceComment(node.getComponentName());
        }
        if (node.isTransformationNode()) {
            String str = ExpressionEngine.extractFromTransformation(node);
            table.setSourceComment(str);
            LOGGER.info("Expression extracted ::" + str);
        }
        return table;
    }

    /**
     * It takes a job and returns a schema
     *
     * @param job The job to be converted to a schema.
     * @return A Schema object.
     */
    public Schema jobToSchema(ProcessType job) {
        return new Schema(new SchemaId(new Name(job.getJobName())));
    }

    private Table getTableByUniqueName(ProcessType job, String uniqueName) {
        List<Table> tables = extractTables(job);
        return tables.stream().filter(table -> uniqueName.equals(table.getOwner())).findFirst().orElse(null);
    }

    private LineagePath getLineagePath(Table sourceTable, Table targetTable) {
        List<LineageObject> sources = new ArrayList<>();
        LineageObject source = new LineageObject(new NativeObject(
                new DataSource(DataSource.SourceTypes.RDBMS, new Self()),
                sourceTable.getId()
        ));
        sources.add(source);
        LineageObject target = new LineageObject(new NativeObject(
                new DataSource(DataSource.SourceTypes.RDBMS, new Self()),
                targetTable.getId()
        ));
        return new LineagePath(sources, target);
    }

    public List<LineagePath> extractLineagePaths() {
        if (jobs == null || jobs.size() == 0) return new ArrayList<>();
        List<LineagePath> lineagePaths = new ArrayList<>();
        jobs.forEach(job -> {
            job.getConnection().forEach(connection -> {
                Table sourceTable = getTableByUniqueName(job, connection.getSource());
                Table targetTable = getTableByUniqueName(job, connection.getTarget());
                if(sourceTable != null && targetTable != null) {
                    System.out.println("Creating Lineage For ");
                    System.out.println(" Source Table: " + sourceTable.getId().getName().getOriginal());
                    System.out.println(" Target Table: " + targetTable.getId().getName().getOriginal());
                    lineagePaths.add(getLineagePath(sourceTable, targetTable));
                }
            });
        });
        return lineagePaths;
    }

}
