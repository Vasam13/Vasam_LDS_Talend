package alation.talend.mde.extractor.iterators;

import alation.sdk.rdbms.mde.customdb.iterables.DbObjectIterable;
import alation.sdk.rdbms.mde.models.Schema;

/**
 * It's a wrapper around a Talend Schema object that allows it to be used as a DbObjectIterable
 */
public class TalendSchemaIterable implements DbObjectIterable<Schema> {

    private final TalendSchemaIterator iterator;

    public TalendSchemaIterable(Schema[] schemas) {
        this.iterator = new TalendSchemaIterator(schemas);
    }

    /**
     * The `iterator()` function returns an iterator for the `Schema` objects in the database
     *
     * @return An iterator object that can be used to iterate over the schemas in the database.
     */
    public DbObjectIterator<Schema> iterator() {
        return this.iterator;
    }

    @Override
    public void close() {
    }

    /**
     * It's a class that extends DbObjectIterator, and it's a class that implements the Iterator interface
     */
    private static class TalendSchemaIterator extends DbObjectIterator<Schema> {
        private final Schema[] schemas;
        private int current;

        public TalendSchemaIterator(Schema[] schemas) {
            this.schemas = schemas;
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
         * If the number of schemas is greater than the current index, then there is a next schema.
         *
         * @return The next schema in the array.
         */
        @Override
        public boolean hasNext() {
            return schemas.length > this.current;
        }

        /**
         * Return the next schema in the array of schemas.
         *
         * @return The next schema in the array.
         */
        @Override
        public Schema next() {
            return schemas[this.current++];
        }
    }
}
