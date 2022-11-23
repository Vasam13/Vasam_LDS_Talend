package alation.talend.mde.extractor.iterators;

import alation.sdk.rdbms.mde.customdb.iterables.DbObjectIterable;
import alation.sdk.rdbms.mde.models.Column;

/**
 * It's a wrapper around a Talend Metadata object that allows you to iterate over the columns in a
 * table
 */
public class TalendColumnIterable implements DbObjectIterable<Column> {

    private final TalendColumnIterator iterator;

    public TalendColumnIterable(Column[] columns) {
        this.iterator = new TalendColumnIterator(columns);
    }

    /**
     * Return the iterator for this table's columns.
     *
     * @return An iterator object.
     */
    public DbObjectIterator<Column> iterator() {
        return this.iterator;
    }

    @Override
    public void close() {
    }

    /**
     * It's a class that extends DbObjectIterator<Column> and is used to iterate over the columns of a
     * table
     */
    private static class TalendColumnIterator extends DbObjectIterator<Column> {
        private final Column[] columns;
        private int current;

        public TalendColumnIterator(Column[] columns) {
            this.columns = columns;
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
         * If the number of columns is greater than the current column, then there is a next column.
         *
         * @return The next column in the array.
         */
        @Override
        public boolean hasNext() {
            return columns.length > this.current;
        }

        /**
         * Return the current column and increment the current column by one.
         *
         * @return The next column in the array.
         */
        @Override
        public Column next() {
            return columns[this.current++];
        }
    }
}
