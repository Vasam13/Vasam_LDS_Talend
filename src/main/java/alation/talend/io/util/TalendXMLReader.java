package alation.talend.io.util;

import alation.sdk.core.error.ExtractionException;
import alation.sdk.rdbms.mde.models.Name;
import alation.sdk.rdbms.mde.models.SchemaId;
import alation.talend.configuration.TalendConfiguration;
import alation.talend.enums.TalendErrorCode;
import alation.talend.io.Utils;
import alation.talend.io.git.GitHelper;
import alation.talend.mde.types.ProcessType;
import alation.talend.resources.Constants;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * It reads the jobs from the given source configuration like git, remote server..., and returns a list of ProcessType objects i.e, Talend Jobs
 */
public class TalendXMLReader {

    private final Logger LOGGER = Logger.getLogger(TalendXMLReader.class);

    private JAXBContext jaxbContext;

    private final TalendConfiguration configuration;

    // A constructor that takes a TalendConfiguration object as a parameter.
    public TalendXMLReader(TalendConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * If the JAXB context is null, create a new one
     */
    private void createJAXBIfNotExists() throws JAXBException {
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(ProcessType.class);
        }
    }

    /**
     * It clones the git repo, reads the jobs from the files, destroys the repo, and returns the jobs
     *
     * @param schemaIds       A list of SchemaId objects. Each SchemaId object contains a schema name and a
     *                        version.
     * @param isIncludeFilter If true, only the jobs that match the schemaIds will be returned. If false,
     *                        only the jobs that do not match the schemaIds will be returned.
     * @return A list of ProcessType objects.
     */
    public List<ProcessType> getJobsFromGitRepo(List<SchemaId> schemaIds, boolean isIncludeFilter) throws ExtractionException {
        List<ProcessType> jobs = new ArrayList<>();
        try {
            File tempFolder = Utils.createUniqueTempFolder();
            GitHelper gitHelper = new GitHelper(configuration, tempFolder);
            gitHelper.cloneRepo();
            readJobsFromFiles(gitHelper.getFiles(), jobs, schemaIds, isIncludeFilter);
            gitHelper.destroyRepo();
        } catch (Exception ex) {
            LOGGER.error(TalendErrorCode.UNABLE_TO_CLONE_GIT_REPO.getDescription(), ex);
            throw new ExtractionException(ex);
        }
        return jobs;
    }

    /**
     * This function returns a list of jobs from a remote server.
     *
     * @return A list of ProcessType objects.
     */

    public List<ProcessType> getJobsFromRemoteServer() throws ExtractionException {
        // TODO: Implement extract MD from
        return new ArrayList<>();
    }

    /**
     * It reads all the files in the folder specified in the configuration, and adds them to the list of
     * jobs and returns the list
     *
     * @param schemaIds       A list of schemaIds that you want to filter on. If you want to get all jobs, pass
     *                        in an empty list.
     * @param isIncludeFilter true if you want to include the jobs that match the schemaIds, false if you
     *                        want to exclude them.
     * @return A list of ProcessType objects.
     */
    public List<ProcessType> getJobsFromLocalFolder(List<SchemaId> schemaIds, boolean isIncludeFilter) throws ExtractionException {
        List<ProcessType> jobs = new ArrayList<>();
        try {
            readJobsFromFiles(
                    List.of(Objects.requireNonNull(new File(configuration.getXMLFolderUrl()).listFiles())),
                    jobs, schemaIds, isIncludeFilter);
        } catch (Exception ex) {
            LOGGER.error(TalendErrorCode.ERROR_READING_FOLDER.getDescription(), ex);
            throw new ExtractionException(String.valueOf(TalendErrorCode.ERROR_READING_FOLDER), ex);
        }
        return jobs;
    }

    /**
     * Reads the jobs from the given files and adds them to the given list of jobs
     *
     * @param files           List of files to read from
     * @param jobs            List of ProcessType objects that will be populated with the jobs read from the files.
     * @param schemaIds       A list of schemaIds to filter the files by.
     * @param isIncludeFilter true if you want to include the files that match the schemaIds, false if you
     *                        want to exclude them.
     */
    private void readJobsFromFiles(List<File> files, List<ProcessType> jobs, List<SchemaId> schemaIds, boolean isIncludeFilter) throws IOException {
        files = files.stream().filter(file -> file.getName().endsWith(Constants.MIME_XML)).collect(Collectors.toList());
        files = CollectionUtils.isEmpty(schemaIds) ? files : filterFiles(files, schemaIds, isIncludeFilter);
        readJobsFromFilesIntrl(files, jobs);
    }

    /**
     * It filters a list of files based on a list of schemaIds
     *
     * @param files           The list of files to filter.
     * @param schemaIds       List of schemaIds to be filtered
     * @param isIncludeFilter If true, the filter will include the files that match the schemaIds. If
     *                        false, the filter will exclude the files that match the schemaIds.
     * @return A list of files that are filtered based on the schemaIds and the isIncludeFilter flag.
     */
    @NotNull
    private static List<File> filterFiles(List<File> files, List<SchemaId> schemaIds, boolean isIncludeFilter) {
        List<String> schemaNames = schemaIds.stream().map(SchemaId::getName).map(Name::getOriginal).collect(Collectors.toList());
        return isIncludeFilter
                ? files.stream().filter(file -> schemaNames.contains(Utils.getNormalizedString(file.getName().replace(Constants.MIME_XML, Constants.EMPTY)))).collect(Collectors.toList())
                : files.parallelStream().filter(file -> !schemaNames.contains(Utils.getNormalizedString(file.getName().replace(Constants.MIME_XML, Constants.EMPTY)))).collect(Collectors.toList());
    }

    /**
     * Reads all the files in the given list of files, and for each file, reads the job from the file, and
     * adds the job to the given list of jobs
     *
     * @param files a list of files to be read
     * @param jobs  The list of jobs to be returned.
     */
    private void readJobsFromFilesIntrl(List<File> files, List<ProcessType> jobs) {
        for (File file : files) {
            try {
                ProcessType job = readJobFromFile(file);
                if (job != null) {
                    String fileName = file.getName();
                    fileName = fileName.replace(Constants.MIME_XML, Constants.EMPTY);
                    job.setJobName(Utils.getNormalizedString(fileName));
                }
                jobs.add(job);
            } catch (Exception ex) {
                LOGGER.error(TalendErrorCode.INVALID_XML_FILE, ex);
            }
        }
    }

    /**
     * It reads a file and returns a ProcessType object
     *
     * @param file The file to read the job from.
     * @return A ProcessType object.
     */
    private ProcessType readJobFromFile(File file) throws XMLStreamException, JAXBException {
        createJAXBIfNotExists();
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        StreamSource streamSource = new StreamSource(file);
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(streamSource);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ProcessType) unmarshaller.unmarshal(streamReader);
    }
}
