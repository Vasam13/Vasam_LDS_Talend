package alation.talend.io;

import alation.talend.resources.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * A utility class that contains a bunch of static utlity methods.
 */
public class Utils {

    /**
     * It takes a string and replaces all non-alphanumeric characters with a space
     *
     * @param str The string to be normalized.
     * @return A string with all non-alphanumeric characters removed.
     */
    public static String getNormalizedString(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll(Constants.REGEX_NAME_NORMALIZER, Constants.REGEX_REPLACE_NAME_NORMALIZER);
    }

  public static String getPlainString(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }
    return str.replaceAll(Constants.REGEX_NAME_NORMALIZER, Constants.EMPTY);
  }

  public static File createUniqueTempFolder() throws IOException {
    Date date = new Date();
    return Files.createTempDirectory("TALEND_TEMP_" + date.getTime()).toFile();
  }

    /**
     * If the path exists, delete all the files in the path, then delete the path
     *
     * @param path The path to the folder you want to delete.
     */
    public static void deleteFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        path.delete();
    }
}
