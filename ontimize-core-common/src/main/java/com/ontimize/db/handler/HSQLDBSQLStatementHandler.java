package com.ontimize.db.handler;

import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.SQLStatementBuilder;
import com.ontimize.db.SQLStatementBuilder.SQLStatement;

public class HSQLDBSQLStatementHandler extends DefaultSQLStatementHandler {

    static final Logger logger = LoggerFactory.getLogger(SQLStatementBuilder.class);

    public static final String LIMIT = " LIMIT ";

    public static final String OFFSET = " OFFSET ";

    @Override
    public boolean isPageable() {
        return true;
    }

    @Override
    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, boolean descending,
            boolean forceDistinct) {
        return super.createSelectQuery(table, requestedColumns, conditions, wildcards, columnSorting, recordCount, 0,
                descending, forceDistinct);
    }

    @Override
    public SQLStatement createSelectQuery(String table, Vector requestedColumns, Hashtable conditions, Vector wildcards,
            Vector columnSorting, int recordCount, int offset,
            boolean descending, boolean forceDistinct) {
        StringBuilder sql = new StringBuilder();
        Vector vValues = new Vector();
        if ((columnSorting != null) && !requestedColumns.isEmpty()) {
            for (int i = 0; i < columnSorting.size(); i++) {
                if (!requestedColumns.contains(columnSorting.get(i).toString())) {
                    requestedColumns.add(columnSorting.get(i).toString());
                }
            }
        }

        sql.append(this.createSelectQuery(table, requestedColumns, forceDistinct));

        String cond = this.createQueryConditions(conditions, wildcards, vValues);
        if (cond != null) {
            sql.append(cond);
        }
        if ((columnSorting != null) && (!columnSorting.isEmpty())) {
            String sort = this.createSortStatement(columnSorting, descending);
            sql.append(sort);
        }

        if (recordCount >= 0) {
            sql.append(HSQLDBSQLStatementHandler.LIMIT);
            sql.append(recordCount);
        }

        if (offset >= 0) {
            sql.append(HSQLDBSQLStatementHandler.OFFSET);
            sql.append(offset);
        }

        HSQLDBSQLStatementHandler.logger.debug(sql.toString());
        return new SQLStatement(sql.toString(), vValues);
    }

    @Override
    public SQLStatement createLeftJoinSelectQueryPageable(String mainTable, String subquery, String secondaryTable,
            Vector mainKeys, Vector secondaryKeys,
            Vector mainTableRequestedColumns, Vector secondaryTableRequestedColumns, Hashtable mainTableConditions,
            Hashtable secondaryTableConditions, Vector wildcards,
            Vector columnSorting, boolean forceDistinct, boolean descending, int recordNumber, int startIndex) {

        SQLStatement stSQL = this.createLeftJoinSelectQuery(mainTable, subquery, secondaryTable, mainKeys,
                secondaryKeys, mainTableRequestedColumns, secondaryTableRequestedColumns,
                mainTableConditions, secondaryTableConditions, wildcards, columnSorting, forceDistinct, descending);

        StringBuilder stSQLString = new StringBuilder(stSQL.getSQLStatement());
        Vector vValues = stSQL.getValues();

        if (recordNumber >= 0) {
            stSQLString.append(HSQLDBSQLStatementHandler.LIMIT);
            stSQLString.append(recordNumber);
        }

        if (startIndex >= 0) {
            stSQLString.append(HSQLDBSQLStatementHandler.OFFSET);
            stSQLString.append(startIndex);
        }

        HSQLDBSQLStatementHandler.logger.debug(stSQLString.toString());
        return new SQLStatement(stSQLString.toString(), vValues);
    }

    @Override
    public String convertPaginationStatement(String sqlTemplate, int startIndex, int recordNumber) {
        StringBuilder sql = new StringBuilder(sqlTemplate);

        if (recordNumber >= 0) {
            sql.append(HSQLDBSQLStatementHandler.LIMIT);
            sql.append(recordNumber);
        }

        if (startIndex >= 0) {
            sql.append(HSQLDBSQLStatementHandler.OFFSET);
            sql.append(startIndex);
        }

        return sql.toString();
    }

}
