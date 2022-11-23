package alation.talend.io.git;

import alation.talend.configuration.TalendConfiguration;
import alation.talend.io.TalendIOWrapperTest;
import alation.talend.io.Utils;
import alation.talend.resources.Constants;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;

public class GitHelperTest {

    /**
     * Method under test: {@link GitHelper#cloneRepo()}
     */
    @Test
    public void testCloneRepo() throws Exception {
        TalendConfiguration talendConfiguration = TalendIOWrapperTest.getMockConfig();
        File tempFolder = Utils.createUniqueTempFolder();
        GitHelper gitHelper = new GitHelper(talendConfiguration, tempFolder);
        gitHelper.cloneRepo();
        assertTrue(new File(tempFolder, Constants.Folder_git).exists());
    }

    /**
     * Method under test: {@link GitHelper#getFiles()}
     */
    @Test
    public void testGetFiles() throws Exception {
        TalendConfiguration talendConfiguration = TalendIOWrapperTest.getMockConfig();
        File tempFolder = Utils.createUniqueTempFolder();
        GitHelper gitHelper = new GitHelper(talendConfiguration, tempFolder);
        gitHelper.cloneRepo();
        int xmlFileCount = (int) Arrays.stream(Objects.requireNonNull(tempFolder.list()))
                .filter(fileName -> fileName.endsWith(Constants.MIME_XML)).count();
        assertTrue(xmlFileCount > 0);
    }

    /**
     * Method under test: {@link GitHelper#destroyRepo()}
     */
    @Test
    public void testDestroyRepo() throws Exception {
        TalendConfiguration talendConfiguration = TalendIOWrapperTest.getMockConfig();
        File tempFolder = Utils.createUniqueTempFolder();
        GitHelper gitHelper = new GitHelper(talendConfiguration, tempFolder);
        gitHelper.cloneRepo();
        assertTrue(new File(tempFolder, Constants.Folder_git).exists());
        gitHelper.destroyRepo();
        assertFalse(tempFolder.exists());
    }
}

