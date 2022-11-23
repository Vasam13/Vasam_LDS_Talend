package alation.talend;

import alation.sdk.rdbms.commands.RdbmsCommandExecutor;
import alation.talend.datasource.TalendDatasource;

/**
 * It is a main entry class for Altion
 */
public class Main {

    public static void main(String[] args) throws Exception {
        new RdbmsCommandExecutor(new TalendDatasource()).execute(args);
    }
}
