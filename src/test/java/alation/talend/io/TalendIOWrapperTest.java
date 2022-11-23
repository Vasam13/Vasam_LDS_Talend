package alation.talend.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.core.error.ValidationException;
import alation.talend.configuration.TalendConfiguration;

import java.util.ArrayList;
import java.util.List;

import alation.talend.enums.TalendErrorCode;
import alation.talend.resources.Constants;

import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TalendIOWrapperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public static TalendConfiguration getMockConfig() {
        TalendConfiguration talendConfiguration = mock(TalendConfiguration.class);
        when(talendConfiguration.getSourceType()).thenReturn(Constants.ENUM_SOURCE_TYPES.Github);
        when(talendConfiguration.getXMLFolderUrl()).thenReturn("https://github.com/Vasam13/Talend_xml.git");
        when(talendConfiguration.getAccessToken()).thenReturn("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");
        return talendConfiguration;
    }

    /**
     * Method under test: {@link TalendIOWrapper#getAuthenticatedInstance(TalendConfiguration)}
     */
    @Test
    public void testGetAuthenticatedInstance() throws ValidationException {
        thrown.expect(ValidationException.class);
        thrown.expectMessage(TalendErrorCode.NO_USER_NAME.getDescription());
        TalendIOWrapper.getAuthenticatedInstance(new TalendConfiguration());
    }

    /**
     * Method under test: {@link TalendIOWrapper#getAuthenticatedInstance(TalendConfiguration)}
     */
    @Test(expected = Test.None.class)
    public void testGetAuthenticatedInstance2() throws ValidationException {
        TalendIOWrapper.getAuthenticatedInstance(getMockConfig());
    }

    /**
     * Method under test: {@link TalendIOWrapper#validate()}
     */
    @Test(expected = Test.None.class)
    public void testValidateGithub() throws Exception {
        TalendIOWrapper.getAuthenticatedInstance(getMockConfig()).validate();
    }

    /**
     * Method under test: {@link TalendIOWrapper#extractMD(List, boolean)}
     */
    @Test
    public void testExtractMD() throws ExtractionException, ValidationException {
        TalendConfiguration talendConfiguration = getMockConfig();
        TalendIOWrapper authenticatedInstance = TalendIOWrapper.getAuthenticatedInstance(talendConfiguration);
        assertTrue(authenticatedInstance.extractMD(new ArrayList<>(), false).size() > 1);
        verify(talendConfiguration, atLeast(1)).getSourceType();
    }

}

