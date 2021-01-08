package com.ontimize.db;

import com.ontimize.dto.EntityResult;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class EntityResultTools {

    public static int getValuesKeysIndex(Hashtable entityResult, Hashtable kv) {

        // Check fast
        if (kv.isEmpty()) {
            return -1;
        }
        Vector vKeys = new Vector();
        Enumeration enumKeys = kv.keys();
        while (enumKeys.hasMoreElements()) {
            vKeys.add(enumKeys.nextElement());
        }
        // Now get the first data vector. Look for all indexes with the
        // specified key
        // and for each one check the other keys
        Object vData = entityResult.get(vKeys.get(0));
        if ((vData == null) || (!(vData instanceof Vector))) {
            return -1;
        }
        int currentValueIndex = -1;

        if (vKeys.size() == 1) {
            return ((Vector) vData).indexOf(kv.get(vKeys.get(0)));
        }

        while ((currentValueIndex = ((Vector) vData).indexOf(kv.get(vKeys.get(0)), currentValueIndex + 1)) >= 0) {
            boolean allValuesCoincidence = true;
            for (int i = 1; i < vKeys.size(); i++) {
                Object requestValue = kv.get(vKeys.get(i));
                Object vDataAux = entityResult.get(vKeys.get(i));
                if ((vDataAux == null) || (!(vDataAux instanceof Vector))) {
                    return -1;
                }
                if (!requestValue.equals(((Vector) vDataAux).get(currentValueIndex))) {
                    allValuesCoincidence = false;
                    break;
                }
            }

            if (allValuesCoincidence) {
                return currentValueIndex;
            }
        }
        return -1;
    }

    public static void updateRecordValues(com.ontimize.dto.EntityResult entityResult, Hashtable recordValue, int index) {
        Enumeration keysToUpdate = recordValue.keys();
        while (keysToUpdate.hasMoreElements()) {
            Object currentKey = keysToUpdate.nextElement();
            if (entityResult.containsKey(currentKey)) {
                Vector columnRecords = (Vector) entityResult.get(currentKey);
                columnRecords.set(index, recordValue.get(currentKey));
            } else {
                Vector columnRecords = new Vector(entityResult.calculateRecordNumber());
                columnRecords.set(index, recordValue.get(currentKey));
                entityResult.put(currentKey, columnRecords);
            }
        }
    }

    /**
     * Creates an empty <code>EntityResult</code> with structure of columns passed.
     * @param columns columns of <code>EntityResult</code>
     * @return an <code>EntityResult</code> with result or null when <code>columns</code> parameter is
     *         null
     */
    public static com.ontimize.dto.EntityResult createEmptyEntityResult(List columns) {
        if (columns != null) {
            return new EntityResult(columns);
        }
        return null;
    }

}
