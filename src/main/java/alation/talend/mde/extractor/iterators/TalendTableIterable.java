package alation.talend.mde.extractor.iterators;

import alation.sdk.rdbms.mde.customdb.iterables.DbObjectIterable;
import alation.sdk.rdbms.mde.models.Table;

/**
 * It's a class that implements the `DbObjectIterable` interface, and it's a class that is specific to
 * the Talend database
 */
public class TalendTableIterable implements DbObjectIterable<Table> {

    private final TalendTableIterator iterator;

    public TalendTableIterable(Table[] tables) {
        this.iterator = new TalendTableIterator(tables);
    }

    /**
     * Return the iterator for this table.
     *
     * @return An iterator object that can be used to iterate over the tables in the database.
     */
    public DbObjectIterator<Table> iterator() {
        return this.iterator;
    }

    @Override
    public void close() {
    }

    /**
     * This class is a wrapper around the Talend Metadata API that allows you to iterate over all the
     * tables in a database
     */
    private static class TalendTableIterator extends DbObjectIterator<Table> {
        private final Table[] tables;
        private int current;

        public TalendTableIterator(Table[] tables) {
            this.tables = tables;
            this.current = 0;
        }

        /**
         * If the current value is less than the maximum value, increment the current value and return true.
         * Otherwise, return false.
         */
        @Override
        public void close() {
            this.current = 0;
        }

        /**
         * If the number of tables is greater than the current table, then there is a next table.
         *
         * @return The next table in the array.
         */
        @Override
        public boolean hasNext() {
            return tables.length > this.current;
        }

        /**
         * Return the current table and increment the current index.
         *
         * @return The current table.
         */
        @Override
        public Table next() {
            return tables[this.current++];
        }
    }
}
