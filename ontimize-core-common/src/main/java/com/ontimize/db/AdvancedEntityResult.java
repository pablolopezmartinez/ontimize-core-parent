package com.ontimize.db;


/**
 * Extended <code>EntityResult</code> that adds information about number of records and index of
 * first record of <code>ResultSet</code> contained. This object is returned in
 * {@link TableEntity#query(java.util.Hashtable, java.util.Vector, int, int, int, java.util.Vector)
 * to allow that Table component is pageable.
 *
 * @see Table#QUERY_ROWS
 * @see PageFetcher
 * @author Imatia Innovation
 */
public class AdvancedEntityResult extends EntityResultMapImpl {

    protected int totalQueryRecordsNumber = 0;

    protected int startRecordIndex = 0;

    public int getStartRecordIndex() {
        return this.startRecordIndex;
    }

    public void setStartRecordIndex(int startRecordIndex) {
        this.startRecordIndex = startRecordIndex;
    }

    /**
     * Creates an AdvancedEntityResult with code value 'cod', with type 'type' and the message 'm'
     * @param cod
     * @param type
     * @param m
     */
    public AdvancedEntityResult(int cod, int type, String m) {
        super(cod, type, m);
    }

    /**
     * Creates an AdvancedEntityResult with code 'cod' and type 'type'
     * @param cod
     * @param type
     */
    public AdvancedEntityResult(int cod, int type) {
        super(cod, type);
    }

    /**
     * Creates an AdvancedEntityResult with code 'cod', type 'type' and with the message 'm', and the
     * total query records number equals to 'totalQueryRecords'
     * @param cod
     * @param type
     * @param m
     * @param totalQueryRecords
     */
    public AdvancedEntityResult(int cod, int type, String m, int totalQueryRecords) {
        super(cod, type, m);
        this.totalQueryRecordsNumber = totalQueryRecords;
    }

    /**
     * Creates an AdvancedEntityResult with code 'cod', type 'type', and the total query records number
     * equals to 'totalQueryRecords'
     * @param cod
     * @param type
     * @param totalQueryRecords
     */
    public AdvancedEntityResult(int cod, int type, int totalQueryRecords) {
        super(cod, type);
        this.totalQueryRecordsNumber = totalQueryRecords;
    }

    /**
     * Gets the total query records count
     * @return
     */
    public int getTotalRecordCount() {
        return this.totalQueryRecordsNumber;
    }

    /**
     * Sets the total query records count
     * @param totalQueryRecords
     */
    public void setTotalRecordCount(int totalQueryRecords) {
        this.totalQueryRecordsNumber = totalQueryRecords;
    }

    /**
     * Gets the total records number that the object contains.
     * @return The object records number. When the object is empty return 0
     */
    public int getCurrentRecordCount() {
        return this.calculateRecordNumber();
    }

}
