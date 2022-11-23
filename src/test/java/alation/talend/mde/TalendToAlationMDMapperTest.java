package alation.talend.mde;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.io.TalendIOWrapper;
import alation.talend.mde.types.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import org.junit.Test;

public class TalendToAlationMDMapperTest {


    /**
     * Method under test: {@link TalendToAlationMDMapper#extractSchemas()}
     */
    @Test
    public void testExtractSchemas() throws ExtractionException {
        Context context = new Context();
        context.setConfirmationNeeded("Confirmation Needed");
        context.setContextparameter(new ArrayList<>());
        context.setName("Name");

        Parameters parameters = new Parameters();
        parameters.setElementParameter(new ArrayList<>());
        parameters.setRoutinesParameter(new ArrayList<>());

        ProcessType processType = new ProcessType();
        processType.setConnection(new ArrayList<>());
        processType.setContext(context);
        processType.setJobName("Job Name");
        processType.setJobType("Job Type");
        processType.setNode(new ArrayList<>());
        processType.setNote(new ArrayList<>());
        processType.setParameters(parameters);
        processType.setSubjob(new ArrayList<>());

        ArrayList<ProcessType> processTypeList = new ArrayList<>();
        processTypeList.add(processType);
        TalendIOWrapper talendIOWrapper = mock(TalendIOWrapper.class);
        when(talendIOWrapper.extractMD((List<SchemaId>) any(), anyBoolean())).thenReturn(processTypeList);
        assertEquals(1, (new TalendToAlationMDMapper(talendIOWrapper, new ArrayList<>(), true)).extractSchemas().length);
        verify(talendIOWrapper).extractMD((List<SchemaId>) any(), anyBoolean());
    }

    /**
     * Method under test: {@link TalendToAlationMDMapper#extractTables()} ()}
     */
    @Test
    public void testExtractTables() throws ExtractionException {
        Context context = new Context();
        context.setConfirmationNeeded("Confirmation Needed");
        context.setContextparameter(new ArrayList<>());
        context.setName("Name");

        NodeData nodeData = new NodeData();
        nodeData.setType("Type");

        Metadata metadata = new Metadata();
        metadata.setColumn(new ArrayList<>());
        metadata.setConnector("Input");
        metadata.setLabel("Input");
        metadata.setName("Input");

        ArrayList<Metadata> metadataList = new ArrayList<>();
        metadataList.add(metadata);

        Node node = new Node();
        node.setComponentName("tFileInputDelimited");
        node.setComponentVersion("1.0.2");
        node.setElementParameter(new ArrayList<>());
        node.setMetadata(metadataList);
        node.setNodeData(nodeData);
        node.setOffsetLabelX(2);
        node.setOffsetLabelY(2);
        node.setPosX(1);
        node.setPosY(1);

        ArrayList<Node> nodeList = new ArrayList<>();
        nodeList.add(node);

        Parameters parameters = new Parameters();
        parameters.setElementParameter(new ArrayList<>());
        parameters.setRoutinesParameter(new ArrayList<>());

        ProcessType processType = new ProcessType();
        processType.setConnection(new ArrayList<>());
        processType.setContext(context);
        processType.setJobName("Job Name");
        processType.setJobType("Job Type");
        processType.setNode(nodeList);
        processType.setNote(new ArrayList<>());
        processType.setParameters(parameters);
        processType.setSubjob(new ArrayList<>());

        ArrayList<ProcessType> processTypeList = new ArrayList<>();
        processTypeList.add(processType);
        TalendIOWrapper talendIOWrapper = mock(TalendIOWrapper.class);
        when(talendIOWrapper.extractMD((List<SchemaId>) any(), anyBoolean())).thenReturn(processTypeList);
        assertEquals(1, (new TalendToAlationMDMapper(talendIOWrapper, new ArrayList<>(), true)).extractTables().length);
        verify(talendIOWrapper).extractMD((List<SchemaId>) any(), anyBoolean());
    }


    /**
     * Method under test: {@link TalendToAlationMDMapper#extractColumns()}
     */
    @Test
    public void testExtractColumns() throws ExtractionException {
        Context context = new Context();
        context.setConfirmationNeeded("Confirmation Needed");
        context.setContextparameter(new ArrayList<>());
        context.setName("Name");

        NodeData nodeData = new NodeData();
        nodeData.setType("Type");

        Metadata metadata = new Metadata();
        metadata.setColumn(new ArrayList<>());
        metadata.setConnector("FLOW");
        metadata.setName("tFileInputDelimited_1");

        Column column1 = new Column();
        column1.setName("products");
        column1.setType("id_String");

        Column column2 = new Column();
        column2.setName("sales");
        column2.setType("id_String");

        List<Column> columnList = new ArrayList<>();

        columnList.add(column1);
        columnList.add(column2);

        metadata.setColumn(columnList);

        ArrayList<Metadata> metadataList = new ArrayList<>();
        metadataList.add(metadata);

        Node node = new Node();
        node.setComponentName("tFileInputDelimited");
        node.setComponentVersion("1.0.2");
        node.setElementParameter(new ArrayList<>());
        node.setMetadata(metadataList);
        node.setNodeData(nodeData);
        node.setOffsetLabelX(2);
        node.setOffsetLabelY(2);
        node.setPosX(1);
        node.setPosY(1);

        ArrayList<Node> nodeList = new ArrayList<>();
        nodeList.add(node);

        Parameters parameters = new Parameters();

        ElementParameter elementParameter = new ElementParameter();
        elementParameter.setName("UNIQUE_NAME");
        elementParameter.setField("TEXT");
        elementParameter.setValue("tFileInputDelimited_1");

        List<ElementParameter> elementParameterList = new ArrayList<>();
        elementParameterList.add(elementParameter);

        parameters.setElementParameter(elementParameterList);
        parameters.setRoutinesParameter(new ArrayList<>());

        ProcessType processType = new ProcessType();
        processType.setConnection(new ArrayList<>());
        processType.setContext(context);
        processType.setJobName("Job Name");
        processType.setJobType("Job Type");
        processType.setNode(nodeList);
        processType.setNote(new ArrayList<>());
        processType.setParameters(parameters);
        processType.setSubjob(new ArrayList<>());

        ArrayList<ProcessType> processTypeList = new ArrayList<>();
        processTypeList.add(processType);
        TalendIOWrapper talendIOWrapper = mock(TalendIOWrapper.class);
        when(talendIOWrapper.extractMD((List<SchemaId>) any(), anyBoolean())).thenReturn(processTypeList);
        assertEquals(2, (new TalendToAlationMDMapper(talendIOWrapper, new ArrayList<>(), true)).extractColumns().length);
        verify(talendIOWrapper).extractMD((List<SchemaId>) any(), anyBoolean());
    }

    /**
     * Method under test: {@link TalendToAlationMDMapper#jobToSchema(ProcessType)}
     */
    @Test
    public void testJobToSchema() throws ExtractionException {
        TalendIOWrapper talendIOWrapper = mock(TalendIOWrapper.class);
        when(talendIOWrapper.extractMD((List<SchemaId>) any(), anyBoolean())).thenReturn(new ArrayList<>());
        TalendToAlationMDMapper talendToAlationMDMapper = new TalendToAlationMDMapper(talendIOWrapper, new ArrayList<>(),
                true);

        Context context = new Context();
        context.setConfirmationNeeded("Confirmation Needed");
        context.setContextparameter(new ArrayList<>());
        context.setName("Name");

        Parameters parameters = new Parameters();
        parameters.setElementParameter(new ArrayList<>());
        parameters.setRoutinesParameter(new ArrayList<>());

        ProcessType processType = new ProcessType();
        processType.setConnection(new ArrayList<>());
        processType.setContext(context);
        processType.setJobName("AlationTalendTestJob");
        processType.setJobType("Job Type");
        processType.setNode(new ArrayList<>());
        processType.setNote(new ArrayList<>());
        processType.setParameters(parameters);
        processType.setSubjob(new ArrayList<>());
        SchemaId id = talendToAlationMDMapper.jobToSchema(processType).getId();
        assertEquals("AlationTalendTestJob", id.getFullyQualifiedName());
        assertEquals("AlationTalendTestJob", id.getName().getOriginal());
        verify(talendIOWrapper).extractMD((List<SchemaId>) any(), anyBoolean());
    }

    /**
     * Method under test: {@link TalendToAlationMDMapper#extractLineagePaths()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testExtractLineagePaths() throws ExtractionException {

        Connection connection1 = new Connection();
        connection1.setConnectorName("Connector Name");
        connection1.setElementParameter(new ArrayList<>());
        connection1.setLabel("Label");
        connection1.setLineStyle(2);
        connection1.setMetaname("Metaname");
        connection1.setOffsetLabelX(2);
        connection1.setOffsetLabelY(2);
        connection1.setSource("SourceTable");
        connection1.setTarget("TargetTable");

        ArrayList<Connection> connectionList = new ArrayList<>();
        connectionList.add(connection1);

        Context context = new Context();
        context.setConfirmationNeeded("Confirmation Needed");
        context.setContextparameter(new ArrayList<>());
        context.setName("Name");

        Parameters parameters = new Parameters();
        parameters.setElementParameter(new ArrayList<>());
        parameters.setRoutinesParameter(new ArrayList<>());

        ProcessType processType = new ProcessType();
        processType.setConnection(connectionList);
        processType.setContext(context);
        processType.setJobName("Job Name");
        processType.setJobType("Job Type");
        processType.setNote(new ArrayList<>());
        processType.setParameters(parameters);
        processType.setSubjob(new ArrayList<>());

        ArrayList<ProcessType> processTypeList = new ArrayList<>();
        processTypeList.add(processType);
        TalendIOWrapper talendIOWrapper = mock(TalendIOWrapper.class);
        when(talendIOWrapper.extractMD((List<SchemaId>) any(), anyBoolean())).thenReturn(processTypeList);
        assertEquals(1, (new TalendToAlationMDMapper(talendIOWrapper, new ArrayList<>(), true)).extractLineagePaths().size());
        verify(talendIOWrapper).extractMD((List<SchemaId>) any(), anyBoolean());
    }

}

