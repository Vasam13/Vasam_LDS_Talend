package alation.talend.io.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.core.error.ValidationException;
import alation.sdk.rdbms.mde.models.Name;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.io.TalendIOWrapper;
import alation.talend.io.TalendIOWrapperTest;
import alation.talend.mde.types.ProcessType;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;

import org.junit.Ignore;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TalendXMLReaderTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Method under test: {@link TalendXMLReader#getJobsFromGitRepo(List, boolean)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetJobsFromGitRepo() throws ExtractionException, ValidationException {
        TalendConfiguration talendConfiguration = TalendIOWrapperTest.getMockConfig();
        TalendIOWrapper authenticatedInstance = TalendIOWrapper.getAuthenticatedInstance(talendConfiguration);
        assertTrue(authenticatedInstance.extractMD(new ArrayList<>(), false).size() > 1);
        verify(talendConfiguration, atLeast(1)).getSourceType();
    }

    /**
     * Method under test: {@link TalendXMLReader#getJobsFromRemoteServer()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetJobsFromRemoteServer() throws ExtractionException {
        assertTrue((new TalendXMLReader(new TalendConfiguration())).getJobsFromRemoteServer().isEmpty());
    }

    /**
     * Method under test: {@link TalendXMLReader#getJobsFromLocalFolder(List, boolean)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetJobsFromLocalFolder() throws ExtractionException {
        // TODO: Complete this test.
        TalendXMLReader talendXMLReader = new TalendXMLReader(null);
        talendXMLReader.getJobsFromLocalFolder(new ArrayList<>(), true);
    }


}

