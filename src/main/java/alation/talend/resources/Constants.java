package alation.talend.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It's a class that contains immutable constants
 */
public class Constants {

    public static final String LOG_LEVEL = "log_level";

    public static final String XML_SOURCE = "xml_source";
    public static final String XML_FOLDER_URL = "xml_folder_URL";
    public static final String IS_NETWORK = "is_network";
    public static final String SERVER_URI = "server_uri";
    public static final String TOKEN = "token";

    public static final String STR_NOT_FOUND = "NOT_FOUND";
    public static final String STR_Input = "Input";
    public static final String STR_Output = "Output";
    public static final String STR_UNIQUE_NAME = "UNIQUE_NAME";
    public static final String STR_DBTABLE = "DBTABLE";
    public static final String STR_TEXT = "TEXT";
    public static final String STR_FILE = "FILE";
    public static final String STR_FILENAME = "FILENAME";
    public static final String STR_LABEL = "LABEL";
    public static final String STR_TABLE = "TABLE";
    public static final String STR_MEMO_SQL = "MEMO_SQL";
    public static final String STR_TFILE = "tFile";
    public static final String STR_T = "t";

  public static final String STR_CONDITIONS = "CONDITIONS";
  public static final String STR_FUNCTION = "FUNCTION";
  public static final String STR_OPERATOR = "OPERATOR";
  public static final String STR_RVALUE = "RVALUE";
  public static final String STR_INPUT_COLUMN = "INPUT_COLUMN";

    public static final String STR_OPEN_BRACKET = "(";
    public static final String STR_CLOSE_BRACKET = ")";

  public static final String SPACE = " ";
  public static final String NEWLINE = "\n";
  public static final String STR_FLOW = "FLOW";
  public static final String STR_MAIN = "MAIN";
  public static final List<String> CONNECTOR_TYPES = Arrays.asList(STR_FLOW, STR_MAIN);
  public static final String STR_SUBSTITUTIONS = "SUBSTITUTIONS";
  public static final String STR_SEARCH_PATTERN = "SEARCH_PATTERN";
  public static final String STR_REPLACE_STRING = "REPLACE_STRING";
  public static final String STR_CASE_SENSITIVE = "CASE_SENSITIVE";
  public static final String GROUP_BY_EXPRESSION = Constants.NEWLINE + "GROUP BY " + Arguments.ARGUMENT_1.value;
  public static final String OPERATIONS_EXPRESSION = Constants.NEWLINE + Arguments.ARGUMENT_1.value + Constants.STR_OPEN_BRACKET + Arguments.ARGUMENT_2.value + Constants.STR_CLOSE_BRACKET;
  public static final String STR_OPERATIONS = "OPERATIONS";
  public static final String STR_GROUPSBY = "GROUPBYS";
  public static final String STR_CRITERIA = "CRITERIA";
  public static final String STR_COLNAME = "COLNAME";
  public static final String STR_ORDER = "ORDER";
  public static final String SORT_EXPRESSION = Constants.NEWLINE + "order by " + Arguments.ARGUMENT_1.value + SPACE + Arguments.ARGUMENT_2.value;
  public static final String CONVERT_TYPE_EXPRESSION = "CAST("+Arguments.ARGUMENT_1.value + " AS " + Arguments.ARGUMENT_2.value + ")" + Constants.NEWLINE;
  public static final String REPLACE_EXPRESSION = "REPLACE"+STR_OPEN_BRACKET+Arguments.ARGUMENT_1.value+","+Arguments.ARGUMENT_2.value+","+Arguments.ARGUMENT_3.value+STR_CLOSE_BRACKET;
  public static final String STR_CASE_INSENSITIVE = "CASE_INSENSITIVE";
  public static final String STR_USE_INNER_JOIN = "USE_INNER_JOIN";
  public static final String STR_JOIN_KEY = "JOIN_KEY";
  public static final String STR_RANGE = "RANGE";
  public static final String STR_LOOKUP_COLUMN = "LOOKUP_COLUMN";
  public static final String STR_LOOKUP_COLS = "LOOKUP_COLS";
  public static final String STR_USE_LOOKUP_COLS = "USE_LOOKUP_COLS";
  public static final String STR_ID = "id_";
    public static final String STR_SELECT = "SELECT";
  public static final String SYMBOL_COMMA = ",";
  public static final String STR_ROW_LINE = " row line ";
  public static final String SYMBOL_COMMA_SPACE = SYMBOL_COMMA + SPACE;
  public static final String STR_AND = SPACE + "AND";
  public static final String JOIN_EXPRESSION = "Main Table: " + Arguments.ARGUMENT_1.value +
          "\n" +
          "Lookup Table: " + Arguments.ARGUMENT_2.value +
          "\n" +
          "Join_Key: " + Arguments.ARGUMENT_3.value +
          "\n" +
          "Lookup Columns: "+ Arguments.ARGUMENT_4.value;
  public static final String STR_TO = SPACE + "to" + SPACE;
  public static final String STR_NA = "N/A";
  public static String STR_MSSQL_Input = "tMSSqlInput";

    public enum Arguments {
        ARGUMENT_1("{1}"), ARGUMENT_2("{2}"), ARGUMENT_3("{3}"), ARGUMENT_4("{4}");
        private final String value;

        private Arguments(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum SUPPORTED_TRANSFORMATIONS {
        tJoin, tMap, tSortRow, tAggregateRow, tFilterRow, tConvertType,
        tReplace, tFilterColumns, tSampleRow
    }

    public static final List<String> SUPPORTED_TRANSFORMATIONS_LIST = Stream.of(SUPPORTED_TRANSFORMATIONS.values())
            .map(Enum::name)
            .collect(Collectors.toList());

    public static final String SYMBOL_QUOT = "\"";

    public static final String SYMBOL_QUOT_COLON = SYMBOL_QUOT + ";";
    public static final String SYMBOL_DOT = "\\.";
    public static final char CHAR_DOT = '.';

    public static final String SYMBOL_UNDERSCORE = "_";

    public static final String MIME_XML = ".xml";

    public static final String FILE_gitignore = ".gitignore";

    public static final String Folder_git = ".git";

    public static final String URL_SPACE_REPLACE = "%20";

    public static final String EMPTY = "";

    public static final String REGEX_NAME_NORMALIZER = "[^a-zA-Z0-9_]";
    public static final String REGEX_SAMPLE_ROW = "[^a-zA-Z0-9_,.]";
    public static final String REGEX_REPLACE_NAME_NORMALIZER = "_";

    public static final String COLUMN_id_String = "id_String";
    public static final String COLUMN_id_Character = "id_Character";

    public static final String COLUMN_id_Integer = "id_Integer";
    public static final String COLUMN_id_Short = "id_Short";
    public static final String COLUMN_id_BigDecimal = "id_BigDecimal";
    public static final String COLUMN_id_Float = "id_Float";

    public static final String COLUMN_id_Date = "id_Date";

    public static final String COLUMN_id_Boolean = "id_Boolean";

    public static final String REGEX_id_ = "id_";

    public static final String COLUMN_id_Object = "id_Object";
    public static final String TRANSFORMATION_REPLACE_NAME = "Transform" + SYMBOL_UNDERSCORE;

    public static final String[] SOURCE_TYPES =
            new String[]{"Local Folder", "Github", "Remote Server"};

    public interface ENUM_SOURCE_TYPES {
        public String Local_Folder = SOURCE_TYPES[0];
        public String Github = SOURCE_TYPES[1];
        public String Remote_Server = SOURCE_TYPES[2];
    }
}
