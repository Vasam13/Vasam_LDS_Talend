package alation.talend.io.git;

import alation.sdk.core.error.ConnectorException;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.io.Utils;
import alation.talend.resources.Constants;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * It clones the repository to a temporary folder, and then returns a list of all the files in the git
 * repository
 */
public class GitHelper {

    private final GitConfig gitConfig;

    private final File folderLocation;

    private Git git;

    public GitHelper(TalendConfiguration configuration, File location) {
        GitConfig gitConfig = new GitConfig();
        gitConfig.setUsePersonalAccessToken(true);
//        gitConfig.setUrl(configuration.getXMLFolderUrl());
//        gitConfig.setPersonalAccessToken(configuration.getAccessToken());
        gitConfig.setUrl("https://github.com/Vasam13/Talend_xml.git");
        gitConfig.setPersonalAccessToken("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");
        this.gitConfig = gitConfig;
        this.folderLocation = location;
    }

    /**
     * If the user has configured the connector to use SSH, then use SSH. If the user has configured the
     * connector to use a personal access token, then use a personal access token. If the user has not
     * configured the connector to use either of these methods, then throw an exception
     */
    public void cloneRepo() throws Exception {
        if (this.gitConfig.isUsePersonalAccessToken()) {
            clonePersonalAccessToken();
            return;
        }
        throw new ConnectorException("No supported connection method for git");
    }

    /**
     * It clones the repository to a temporary folder
     */
    private void clonePersonalAccessToken() throws Exception {
        CredentialsProvider credentialsProvider =
                new UsernamePasswordCredentialsProvider(
                        gitConfig.getPersonalAccessToken(), Constants.EMPTY);
        git = Git.cloneRepository()
                        .setURI(gitConfig.getUrl())
                        .setDirectory(folderLocation)
                        .setCredentialsProvider(credentialsProvider)
                        .call();
    }

    /**
     * If the git object is null, return an empty list, otherwise return a list of all the files in the git
     * repository.
     *
     * @return A list of files in the working tree.
     */
    public List<File> getFiles() {
        if (git == null) return new ArrayList<>();
        File folder = git.getRepository().getWorkTree();
        return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
    }

    /**
     * If the git object is not null, close the repository and delete the folder.
     */
    public void destroyRepo() {
        if (git == null) return;
        File folder = git.getRepository().getWorkTree();
        git.getRepository().close();
        Utils.deleteFolder(folder);
    }
}
