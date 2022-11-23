package alation.talend.mde.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.rdbms.mde.base.iterables.filter.AcceptAllFilter;
import alation.sdk.rdbms.mde.customdb.extractor.ExtractionArguments;
import alation.sdk.rdbms.mde.customdb.query.iterable.ResultSetMetadataIterable;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.io.TalendIOWrapper;
import alation.talend.mde.TalendMDEParameters;
import alation.talend.mde.TalendToAlationMDMapperTest;
import alation.talend.mde.extractor.iterators.TalendColumnIterable;
import alation.talend.mde.extractor.iterators.TalendSchemaIterable;
import alation.talend.mde.extractor.iterators.TalendTableIterable;
import alation.talend.mde.types.Context;
import alation.talend.mde.types.InputTables;
import alation.talend.mde.types.Node;
import alation.talend.mde.types.NodeData;
import alation.talend.mde.types.OutputTables;
import alation.talend.mde.types.Parameters;
import alation.talend.mde.types.ProcessType;
import alation.talend.mde.types.VarTables;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import org.junit.Test;

public class TalendExtractorTest {


    /**
     * Method under test: {@link TalendExtractor#getSchemas()}
     */
    @Test
    public void testGetSchemas() throws ExtractionException {
        new TalendToAlationMDMapperTest().testExtractSchemas();
    }

    /**
     * Method under test: {@link TalendExtractor#getTables()}
     */
    @Test
    public void testGetTables() throws ExtractionException {
        new TalendToAlationMDMapperTest().testExtractTables();
    }

    /**
     * Method under test: {@link TalendExtractor#getColumns()}
     */
    @Test
    public void testGetColumns() throws ExtractionException {
        new TalendToAlationMDMapperTest().testExtractColumns();
    }

    /**
     * Method under test: {@link TalendExtractor#getFilterNames()}
     */
    @Test
    public void testGetFilterNames() throws ExtractionException {
       new TalendExtractorTest().testGetSchemas();
    }


    /**
     * Method under test: {@link TalendExtractor#getLineagePaths()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetLineagePaths() throws ExtractionException {
        new TalendToAlationMDMapperTest().testExtractLineagePaths();
    }


}

