package com.ontimize.db.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.ontimize.db.EntityResultMapImpl;
import com.ontimize.db.LocalePair;
import com.ontimize.db.SQLStatementBuilder.SQLConditionValuesProcessor;
import com.ontimize.db.SQLStatementBuilder.SQLNameEval;
import com.ontimize.db.SQLStatementBuilder.SQLStatement;

public interface SQLStatementHandler {

    public void addSpecialCharacters(char[] c);

    public SQLStatement createCountQuery(String table, Hashtable conditions, Vector wildcards, Vector countColumns);

    public SQLStatement createDeleteQuery(String table, Hashtable keysValues);

    public SQLStatement createInsertQuery(String table, Hashtable attributes);

    public SQLStatement createJoinSelectQuery(String principalTable, String secondaryTable, Vector principalKeys,
            Vector secondaryKeys, Vector principalTableRequestedColumns,
            Vector secondaryTableRequestedColumns, Hashtable principalTableConditions,
            Hashtable secondaryTableConditions, Vector wildcards, Vector columnSorting,
            boolean forceDistinct);

    public SQLStatement createJoinSelectQuery(String mainTable, String secondaryTable, Vector mainKeys,
            Vector secondaryKeys, Vector mainTableRequestedColumns,
            Vector secondaryTableRequestedColumns, Hashtable mainTableConditions, Hashtable secondaryTableConditions,
            Vector wildcards, Vector columnSorting, boolean forceDistinct,
            boolean descending);

    public SQLStatement createJoinFromSubselectsQuery(String primaryAlias, String secondaryAlias, String primaryQuery,
            String secondaryQuery, Vector primaryKeys,
            Vector secondaryKeys, Vector primaryTableRequestedColumns, Vector secondaryTableRequestedColumns,
            Hashtable primaryTableConditions, Hashtable secondaryTableConditions,
            Vector wildcards, Vector columnSorting, boolean forceDistinct, boolean descending);

    public SQLStatement createLeftJoinSelectQuery(String mainTable, String subquery, String secondaryTable,
            Vector mainKeys, Vector secondaryKeys, Vector mainTableRequestedColumns,
            Vector secondaryTableRequestedColumns, Hashtable mainTableConditions, Hashtable secondaryTableConditions,
            Vector wildcards, Vector columnSorting, boolean forceDistinct,
            boolean descending);

    public SQLStatement createLeftJoinSelectQueryPageable(String mainTable, String subquery, String secondaryTable,
            Vector mainKeys, Vector secondaryKeys,
            Vector mainTableRequestedColumns, Vector secondaryTableRequestedColumns, Hashtable mainTableConditions,
            Hashtable secondaryTableConditions, Vector wildcards,
            Vector columnSorting, boolean forceDistinct, boolean descending, int recordNumber, int startIndex);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions,
            Vector wildcards);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, boolean descending);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, boolean descending,
            boolean forceDistinct);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, boolean descending);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, boolean descending,
            boolean forceDistinct);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, int offset);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, int offset,
            boolean descending);

    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, int offset,
            boolean descending, boolean forceDistinct);

    public SQLStatement createUpdateQuery(String table, Hashtable attributesValues, Hashtable keysValues);

    public SQLConditionValuesProcessor getQueryConditionsProcessor();

    public SQLNameEval getSQLNameEval();

    public String qualify(String col, String table);

    public void setSQLConditionValuesProcessor(SQLConditionValuesProcessor processor);

    public void setSQLNameEval(SQLNameEval eval);

    public boolean isUseAsInSubqueries();

    public void setUseAsInSubqueries(boolean useAsInSubqueries);

    public boolean checkColumnName(String columnName);

    public String createQueryConditionsWithoutWhere(Hashtable conditions, Vector wildcard, Vector values);

    public boolean isPageable();

    public boolean isDelimited();

    public void resultSetToEntityResult(ResultSet resultSet, EntityResultMapImpl entityResult, List columnNames)
            throws Exception;

    public void resultSetToEntityResult(ResultSet resultSet, EntityResultMapImpl entityResult, int recordNumber, int offset,
                                        boolean delimited, List columnNames) throws Exception;

    public void generatedKeysToEntityResult(ResultSet resultSet, EntityResultMapImpl entityResult, List generatedKeys)
            throws Exception;

    public void setObject(int index, Object value, PreparedStatement preparedStatement, boolean truncDates)
            throws SQLException;

    public String addMultilanguageLeftJoinTables(String table, Vector tables, LinkedHashMap hOtherLocaleTablesKey,
            LocalePair localeId) throws SQLException;

    public String addInnerMultilanguageColumns(String subSqlQuery, Vector attributtes, Hashtable hLocaleTablesAV);

    public String addOuterMultilanguageColumns(String sqlQuery, String table, Hashtable hLocaleTablesAV);

    public String addOuterMultilanguageColumnsPageable(String sqlQuery, String table, Hashtable hLocaleTablesAV);

    public String createSortStatement(Vector sortColumns);

    public String createSortStatement(Vector sortColumns, boolean b);

    /**
     * Convert a query statement in a pagination query statement
     * @param sqlTemplate
     * @param startIndex
     * @param recordNumber
     * @return
     */
    public String convertPaginationStatement(String statement, int startIndex, int recordNumber);

}
