package com.ontimize.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.ReferenceFieldAttribute;

public class EntityResult extends Hashtable {

    static final Logger logger = LoggerFactory.getLogger(EntityResult.class);

    public static boolean DEBUG = false;

    private static final boolean LIMIT_SPEED = false;

    public static final int OPERATION_SUCCESSFUL = 0;

    public static final int OPERATION_SUCCESSFUL_SHOW_MESSAGE = 2;

    public static final int OPERATION_WRONG = 1;

    public static final int DATA_RESULT = 0;

    public static final int NODATA_RESULT = 1;

    public static final int DEFAULT_COMPRESSION_THRESHOLD = Integer.MAX_VALUE;

    public static final int NO_COMPRESSION = Deflater.NO_COMPRESSION;

    public static final int BEST_COMPRESSION = Deflater.BEST_COMPRESSION;

    public static final int BEST_SPEED = Deflater.BEST_SPEED;

    public static final int DEFAULT_COMPRESSION = Deflater.DEFAULT_COMPRESSION;

    public static final int HUFFMAN_ONLY = Deflater.HUFFMAN_ONLY;

    protected int MIN_BYTE_PROGRESS = 1024 * 50;

    protected static int MIN_PERCENT_PROGRESS = 3;

    private int byteBlock = 40 * 1024;// 40 K

    /**
     * Compression Threshold.
     */
    protected int compressionThreshold = EntityResult.DEFAULT_COMPRESSION_THRESHOLD;

    protected int type = EntityResult.NODATA_RESULT;

    protected int code = EntityResult.OPERATION_SUCCESSFUL;

    protected String message = "";

    protected Object[] messageParameter = null;

    protected String detail = null;

    protected String operationId = null;

    protected List columnsOrder = null;

    /**
     * Object needed for the compression mechanism
     */
    protected transient Hashtable data = new Hashtable();

    protected transient int compressionLevel = Deflater.NO_COMPRESSION;

    protected transient int dataByteNumber = -1;

    protected transient long streamTime = 0;

    protected static class TimeUtil {

        long time = 0;

        public void setTime(long t) {
            this.time = t;
        }

        public long getTime() {
            return this.time;
        }

    }


    /**
     * Creates a EntityResult with code = OPERATION_SUCCESSFUL and type = NODATA_RESULT
     */
    public EntityResult() {
        super(0);
    }

    // 5.2062EN-0.1
    public EntityResult(List columns) {
        super(0);
        if (columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    this.put(columns.get(i), new Vector());
                }
            }
        }
    }

    public EntityResult(Hashtable h) {
        super(0);
        if (h != null) {
            this.data = (Hashtable) h.clone();
        }
    }

    public EntityResult(int operationCode, int resultType) {
        super(0);
        this.code = operationCode;
        this.type = resultType;
    }

    public EntityResult(int operationCode, int resultType, String resultMessage) {
        super(0);
        this.code = operationCode;
        this.type = resultType;
        this.message = resultMessage;
    }

    public int getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.detail;
    }

    public void setType(int operationType) {
        this.type = operationType;
    }

    public void setCode(int operationCode) {
        this.code = operationCode;
    }

    public void setMessage(String operationMessage) {
        this.message = operationMessage;
    }

    public void setMessageParameters(Object[] params) {
        this.messageParameter = params;
    }

    public Object[] getMessageParameter() {
        return this.messageParameter;
    }

    public void setMessage(String operationMessage, String operationDetails) {
        this.message = operationMessage;
        this.detail = operationDetails;
    }

    // ////////// PROXY METHODS (COMPATIBILITY) ///////////////////////////

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public boolean contains(Object value) {
        return this.data.contains(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    @Override
    public Enumeration elements() {
        return this.data.elements();
    }

    @Override
    public Object clone() {
        try {
            return this.deepClone();
        } catch (Exception e) {
            EntityResult.logger.trace(null, e);
            Object o = super.clone();
            ((EntityResult) o).data = (Hashtable) this.data.clone();
            return o;
        }
    }

    public EntityResult deepClone() {
        Object o = super.clone();
        ((EntityResult) o).data = new EntityResult();
        Enumeration eKeys = this.data.keys();
        while (eKeys.hasMoreElements()) {
            Object oKey = eKeys.nextElement();
            Vector vValues = (Vector) this.data.get(oKey);
            if (vValues != null) {
                ((EntityResult) o).data.put(oKey, vValues.clone());
            }
        }

        return (EntityResult) o;
    }

    @Override
    public Set entrySet() {
        return this.data.entrySet();
    }

    @Override
    public Object get(Object key) {
        return this.data.get(key);
    }

    public Object get(Object cod, Object attr) {
        Object oValue = null;
        Enumeration eKeys = this.data.keys();
        while (eKeys.hasMoreElements()) {
            Object oKey = eKeys.nextElement();
            if (oKey instanceof ReferenceFieldAttribute) {
                ReferenceFieldAttribute ar = (ReferenceFieldAttribute) oKey;
                if (ar.getCod().equals(cod) && ar.getAttr().equals(attr)) {
                    oValue = this.data.get(oKey);
                    return oValue;
                }
            }
        }
        return oValue;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public Enumeration keys() {
        if (this.columnsOrder != null) {
            ArrayList sortKeys = new ArrayList();
            // First, search in columns order.
            for (int i = 0; i < this.columnsOrder.size(); i++) {
                if (this.data.containsKey(this.columnsOrder.get(i))) {
                    sortKeys.add(this.columnsOrder.get(i));
                }
            }
            // Now, other data columns not in columns order
            Enumeration eKeys = this.data.keys();
            while (eKeys.hasMoreElements()) {
                Object oKey = eKeys.nextElement();
                if (!sortKeys.contains(oKey)) {
                    sortKeys.add(oKey);
                }
            }
            return Collections.enumeration(sortKeys);
        }
        return this.data.keys();
    }

    @Override
    public Set keySet() {
        return this.data.keySet();
    }

    @Override
    public Object put(Object key, Object value) {
        return this.data.put(key, value);
    }

    @Override
    public void putAll(Map m) {
        this.data.putAll(m);
    }

    @Override
    public Object remove(Object key) {
        return this.data.remove(key);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public String toString() {
        String s = "EntityResult: ";
        if (this.getCode() == EntityResult.OPERATION_WRONG) {
            s = s + " ERROR CODE RETURN: " + this.getMessage();
        }
        s = s + " : " + this.data.toString();
        return s;
    }

    @Override
    public Collection values() {
        return this.data.values();
    }

    public void setCompressionLevel(int level) {
        this.compressionLevel = level;
    }

    public void setCompressionThreshold(int threshold) {
        EntityResult.logger.debug("EntityResult: Compression threshold sets to: {}", threshold);
        this.compressionThreshold = threshold;
        if (this.compressionThreshold < this.MIN_BYTE_PROGRESS) {
            this.MIN_BYTE_PROGRESS = this.compressionThreshold * 2;
        }
        if (this.compressionThreshold < this.byteBlock) {
            this.byteBlock = this.compressionThreshold * 2;
        }
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    /**
     * Calculates the data size (bytes).NO USE
     */
    int sizeOf() {
        long t = System.currentTimeMillis();
        ByteArrayOutputStream out = null;
        ObjectOutputStream outO = null;
        try {
            out = new ByteArrayOutputStream(65536);
            outO = new ObjectOutputStream(out);
            outO.writeObject(this.data);
            long t2 = System.currentTimeMillis();
            int size = out.size();
            EntityResult.logger.debug("Time calculating EntityResult data size = {}  milliseconds. Size = {}  Bytes.",
                    t2 - t, size);
            return size;
        } catch (IOException e) {
            EntityResult.logger.error(null, e);
            return -1;
        } finally {
            try {
                out.close();
                outO.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        // Default serialization. This object is serialized in first place.
        // To serialize the data is sometimes necessary to compress them
        EntityResult.logger.debug("Serializing EntityResult");
        long t = System.currentTimeMillis();
        int thresholdLevel = this.compressionThreshold;
        out.defaultWriteObject();
        EntityResult.logger.debug("Serializing EntityResult: after defaultWriteObject");
        // Now data
        ByteArrayOutputStream bOut = null;
        ObjectOutputStream outAux = null;
        try {
            EntityResult.logger.debug("Serializing EntityResult: entering the try");
            byte[] compressedBytes = null;
            bOut = new ByteArrayOutputStream(512);
            outAux = new ObjectOutputStream(bOut);
            EntityResult.logger.debug("Serializing EntityResult: before outAux");
            outAux.writeObject(this.data);
            outAux.flush();
            int size = bOut.size();
            EntityResult.logger.debug("Object size without compression = {} bytes. Compression threshold = {}", size,
                    thresholdLevel);

            if ((size > thresholdLevel) && (this.compressionLevel == EntityResult.NO_COMPRESSION)) {
                this.compressionLevel = EntityResult.BEST_SPEED;
            }
            // Object is now a byte array, compress it:
            byte[] bytesWithOutCompress = bOut.toByteArray();
            // If compresionLevel is different of NO_COMPRESSION, compress the
            // object
            if (this.compressionLevel != Deflater.NO_COMPRESSION) {
                // Save the object in a byte array
                // Compress the array and write it in the stream
                compressedBytes = this.compressionBytes(bytesWithOutCompress, this.compressionLevel);

                // Evaluate: If compressed byte number are greater than bytes
                // without compress, undo the compression and set the
                // compression
                // level to NO_COMPRESION to save time
                if (compressedBytes.length >= bytesWithOutCompress.length) {
                    compressedBytes = bytesWithOutCompress;
                    this.compressionLevel = Deflater.NO_COMPRESSION;
                }
                EntityResult.logger.debug("Compressed object size = {}  bytes", compressedBytes.length);
            } else {
                // When compression is not necessary.
                compressedBytes = bytesWithOutCompress;
            }

            this.dataByteNumber = compressedBytes.length;
            int priority = Thread.currentThread().getPriority();
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            // Now, we have data bytes.
            // First write the compression, then the bytes number and the bytes
            // themselves
            out.writeInt(this.compressionLevel);
            out.writeInt(this.dataByteNumber);
            out.flush();
            int nBytesToWrite = compressedBytes.length;
            int writedBytes = 0;
            long t4 = System.currentTimeMillis();
            while (writedBytes < nBytesToWrite) {

                if (com.ontimize.db.CancellableOperationManager.existCancellationRequest(this.operationId)) {
                    EntityResult.logger.info("Serializing operation canceled: {} . Written: {}", this.operationId,
                            writedBytes);
                    throw new IOException("Serializing operation canceled: " + this.operationId);
                }

                int currentWritedBytes = Math.min(nBytesToWrite - writedBytes, this.byteBlock);
                out.write(compressedBytes, writedBytes, currentWritedBytes);
                writedBytes = writedBytes + currentWritedBytes;
                if (this.operationId != null) {
                    out.flush();
                }
                EntityResult.logger.debug("EntityResult: Written: {}", currentWritedBytes);
                if (EntityResult.LIMIT_SPEED) {
                    long t5 = System.currentTimeMillis();
                    if ((t5 - t4) < 1000) {
                        EntityResult.logger.info("Serialization sleep: {}", 1000 - (t5 - t4));
                        try {
                            Thread.sleep(1000 - (t5 - t4));
                        } catch (Exception e) {
                            EntityResult.logger.trace(null, e);
                        }
                    }
                }
            }
            out.flush();
            this.streamTime = System.currentTimeMillis() - t4;
            EntityResult.logger.debug("STREAM Time EntityResult {}", this.streamTime);
            Thread.currentThread().setPriority(priority);
            out.writeLong(this.streamTime);
            out.flush();
            // Serialization is finished
            EntityResult.logger.debug("Serializing EntityResult time {}", System.currentTimeMillis() - t);
        } catch (IOException e) {
            EntityResult.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                outAux.close();
                bOut.close();
            } catch (Exception e) {
                EntityResult.logger.error(null, e);
            }
        }

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Particularities:
        // - Make the default deserialization. Get bytes number and compression
        // Makes the specific deserialization
        ObjectInputStream inAux = null;
        try {
            in.defaultReadObject();
            // Read the compression type
            int nCompression = in.readInt();
            // The number of bytes (compressed or not), is the number to read
            int nBytes = in.readInt();
            // Now bytes themselves. nBytes to read. Best option is read and
            // decompress
            byte[] bytes = null;
            // If compression level is different to NO_COMPRESION is necessary
            // undo
            // the compression
            long t = System.currentTimeMillis();
            long elapsedTime = 0;
            if (nCompression != Deflater.NO_COMPRESSION) {
                TimeUtil time = new TimeUtil();
                bytes = this.uncompressionBytes(in, nBytes);
                elapsedTime = time.getTime();
            } else {
                bytes = new byte[nBytes]; // Bytes of the data
                InputStream input = createApplicationInputStream(in, nBytes);

                int read = 0;
                int readProgress = 0;
                long t0 = System.nanoTime();

                int lastPercent = 0;
                while (read < bytes.length) {

                    int res = input.read(bytes, read, Math.min(bytes.length - read, 65536));

                    read += res;
                    readProgress += res;
                    int currentPercent = (int) ((read * 100.0) / bytes.length);

                }

                long t2 = System.currentTimeMillis();
                EntityResult.logger.debug("Time reading EntityResult: {} without compression", t2 - t);
                elapsedTime = t2 - t;
            }
            EntityResult.logger.debug("EntityResult size not serialized: {}", bytes.length);
            inAux = new ObjectInputStream(new ByteArrayInputStream(bytes));
            // Now read the object
            this.data = (Hashtable) inAux.readObject();
            long tStream = in.readLong();
            this.dataByteNumber = nBytes;
            this.compressionLevel = nCompression;
            this.streamTime = tStream;
            if (elapsedTime > tStream) {
                EntityResult.logger.debug("Stream time < deserialized time: {} < {}", tStream, elapsedTime);
                this.streamTime = elapsedTime;
            }
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        } finally {
            try {
                if (inAux != null) {
                    inAux.close();
                }
            } catch (Exception e) {
                EntityResult.logger.error(null, e);
            }
        }
    }

    protected byte[] compressionBytes(byte[] bytesToCompress, int compressionLevel) {
        ZipOutputStream zip = null;
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            zip = new ZipOutputStream(byteStream);
            zip.setLevel(compressionLevel);
            ZipEntry inputZip = new ZipEntry("BytesBlock");
            zip.putNextEntry(inputZip);
            zip.write(bytesToCompress);
            zip.flush();
            zip.closeEntry();
            // Now bytes are in the bytearraystream.
            // Lock at the resultant number of bytes
            return byteStream.toByteArray();
        } catch (Exception e) {
            EntityResult.logger.trace(null, e);
            return null;
        } finally {
            try {
                zip.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
            try {
                byteStream.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
        }
    }

    protected byte[] uncompressionBytes(byte[] bytesADescomprimir) {
        ZipInputStream zip = null;
        ByteArrayInputStream byteStream = null;
        try {
            // Undo the compression
            byteStream = new ByteArrayInputStream(bytesADescomprimir);
            zip = new ZipInputStream(byteStream);
            ZipEntry inputZip = zip.getNextEntry();
            // Uses a vector is inefficient. We use a byte array to improve the
            // use
            // of memory
            // A better option will be read the stream in small pieces
            Vector bytesUncompressed = new Vector();
            int byt = -1;
            while ((byt = zip.read()) != -1) {
                bytesUncompressed.add(bytesUncompressed.size(), new Byte((byte) byt));
            }
            byte[] bytes = new byte[bytesUncompressed.size()];
            for (int i = 0; i < bytesUncompressed.size(); i++) {
                bytes[i] = ((Byte) bytesUncompressed.get(i)).byteValue();
            }
            return bytes;
        } catch (IOException e) {
            EntityResult.logger.trace(null, e);
            return null;
        } finally {
            try {
                zip.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
            try {
                byteStream.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
        }

    }

    private transient static Constructor applicationInputStreamConstructor;

    private boolean checkApplicationClass = false;

    protected InputStream createApplicationInputStream(InputStream in, int size) {

        if (!checkApplicationClass && applicationInputStreamConstructor == null) {
            try {
                Class clazz = Class.forName("com.ontimize.db.ApplicationStatusBarInputStream");
                applicationInputStreamConstructor = clazz.getConstructor(new Class[] { InputStream.class, int.class });
            } catch (Exception e) {
            } finally {
                checkApplicationClass = true;
            }
        }

        InputStream current = null;
        if (applicationInputStreamConstructor != null) {
            try {
                current = (InputStream) applicationInputStreamConstructor.newInstance(new Object[] { in, size });
            } catch (Exception e) {
                logger.error("{}", e.getMessage(), e);
            }
        }

        return current != null ? current : in;
    }

    protected byte[] uncompressionBytes(InputStream in, int byteNumber) {
        ZipInputStream zip = null;

        ByteArrayOutputStream bOut = null;
        EntityResult.logger.debug("EntityResult: Uncompressing : {} bytes", byteNumber);
        try {
            long t = System.currentTimeMillis();
            // Undo the compression
            bOut = new ByteArrayOutputStream(1024 * 50);

            zip = new ZipInputStream(createApplicationInputStream(in, byteNumber)) {
                @Override
                public void close() throws IOException {
                    // super.close();
                }
            };
            ZipEntry inputZip = zip.getNextEntry();

            long uncompressorT = System.nanoTime();;

            int byt = -1;
            byte[] bytes = new byte[50 * 1024];
            while ((byt = zip.read(bytes)) != -1) {
                bOut.write(bytes, 0, byt);
            }
            bOut.flush();
            byte[] res = bOut.toByteArray();

            uncompressorT = System.nanoTime() - uncompressorT;
            EntityResult.logger.debug("Time reading EntityResult: {} COMPRESSED", uncompressorT / 1000000.0);
            EntityResult.logger.trace("Time uncompress: " + (System.currentTimeMillis() - t));
            return res;
        } catch (IOException e) {
            EntityResult.logger.error(null, e);
            return null;
        } finally {
            try {
                bOut.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
            try {
                zip.close();
            } catch (Exception e) {
                EntityResult.logger.trace(null, e);
            }
        }

    }

    public int calculateRecordNumber() {
        int r = 0;
        Enumeration keys = this.keys();
        while (keys.hasMoreElements()) {
            Object oKey = keys.nextElement();
            Object v = this.get(oKey);
            if ((v != null) && (v instanceof Vector)) {
                r = ((Vector) v).size();
                break;
            }
        }
        return r;
    }

    public Hashtable getRecordValues(int i) {
        if (i < 0) {
            return null;
        }
        Hashtable hValues = new Hashtable();
        Enumeration keys = this.keys();
        int r = 0;
        while (keys.hasMoreElements()) {
            Object oKey = keys.nextElement();
            Vector v = (Vector) this.get(oKey);
            r = v.size();
            if (i >= r) {
                EntityResult.logger.debug("The values vector for {} only have {} values", oKey, r);
                continue;
            }
            if (v.get(i) != null) {
                hValues.put(oKey, v.get(i));
            }
        }
        return hValues;
    }

    public Hashtable getRecordValues(int i, Vector vKeys) {
        if (i < 0) {
            return null;
        }
        Hashtable hValues = new Hashtable(vKeys.size() * 2);
        Enumeration keys = this.keys();
        int r = 0;
        while (keys.hasMoreElements()) {
            Object oKey = keys.nextElement();
            if (vKeys.contains(oKey)) {
                Vector v = (Vector) this.get(oKey);
                r = v.size();
                if (i >= r) {
                    EntityResult.logger.debug("The values vector for {} only have {} values", oKey, r);
                    continue;
                }
                if (v.get(i) != null) {
                    hValues.put(oKey, v.get(i));
                }
            }
        }
        return hValues;
    }

    public long getStreamTime() {
        return this.streamTime;
    }

    public int getBytesNumber() {
        return this.dataByteNumber;
    }

    public void addRecord(Hashtable data) {
        this.addRecord(data, 0);
    }

    public void addRecord(Hashtable data, int s) {
        if (this.isEmpty()) {
            if (s > 0) {
                throw new IllegalArgumentException("is empty -> index must be 0");
            }
            Enumeration keys = data.keys();
            while (keys.hasMoreElements()) {
                Object oKey = keys.nextElement();
                Vector v = new Vector();
                v.add(0, data.get(oKey));
                this.put(oKey, v);
            }
        } else {
            Enumeration keys = this.keys();
            int nReg = this.calculateRecordNumber();
            if (s >= nReg) {
                s = nReg;
            }
            if (s < 0) {
                s = 0;
            }
            ArrayList modifiedList = new ArrayList();
            while (keys.hasMoreElements()) {
                Object oKey = keys.nextElement();
                Vector v = (Vector) this.get(oKey);
                if (modifiedList.contains(v)) {
                    continue;
                }
                if (data.containsKey(oKey)) {
                    Object oValue = data.get(oKey);
                    v.add(s, oValue);
                } else {
                    v.add(s, null);
                }
                modifiedList.add(v);
            }
        }
    }

    public void deleteRecord(int index) {
        if ((index >= 0) && (index < this.calculateRecordNumber())) {
            Enumeration eKeys = this.keys();
            while (eKeys.hasMoreElements()) {
                Object oKey = eKeys.nextElement();
                Vector vData = (Vector) this.get(oKey);
                vData.remove(index);
            }
        }
    }

    /**
     * Returns true when code is equals to {@value #OPERATION_WRONG}.
     *
     * @since 5.2068EN-0.1
     * @return condition about successful/wrong state
     */
    public boolean isWrong() {
        return this.code == EntityResult.OPERATION_WRONG;
    }

    public int indexOfData(Hashtable dataKeys) {
        int index = getValuesKeysIndex(this, dataKeys);
        return index;
    }

    public int getRecordIndex(Hashtable kv) {
        Vector vKeys = new Vector();
        Enumeration eKeys = kv.keys();
        while (eKeys.hasMoreElements()) {
            vKeys.add(eKeys.nextElement());
        }
        for (int i = 0; i < this.calculateRecordNumber(); i++) {
            Hashtable recordValues = this.getRecordValues(i);
            boolean found = true;
            for (int j = 0; j < vKeys.size(); j++) {
                Object keyCondition = kv.get(vKeys.get(j));
                if (!keyCondition.equals(recordValues.get(vKeys.get(j)))) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    protected Hashtable columnsSQLTypes = null;

    public Hashtable getColumnSQLTypes() {
        return this.columnsSQLTypes;
    }

    public void setColumnSQLTypes(Hashtable types) {
        this.columnsSQLTypes = types;
    }

    public int getColumnSQLType(String col) {
        if ((this.columnsSQLTypes != null) && this.columnsSQLTypes.containsKey(col)) {
            return ((Number) this.columnsSQLTypes.get(col)).intValue();
        } else {
            return java.sql.Types.OTHER;
        }
    }

    public void setOperationId(String opId) {
        this.operationId = opId;
    }

    public List getOrderColumns() {
        if (this.columnsOrder != null) {
            ArrayList l = new ArrayList();
            l.addAll(this.columnsOrder);
            return l;
        } else {
            return this.columnsOrder;
        }
    }

    public void setColumnOrder(List l) {
        this.columnsOrder = l;
    }

    private static int getValuesKeysIndex(Hashtable entityResult, Hashtable kv) {

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

    public static void main(String[] args) {
        List<String> columns = new ArrayList<String>();
        columns.add("test");
        EntityResult eR = new EntityResult(columns);
        Hashtable record = new Hashtable<String, String>();
        record.put("test", "value");
        int total = 1000000;
        System.out.println("Creating " + total + " records");
        long startTime = System.nanoTime();
        for (int i = 0; i < total; i++) {
            eR.addRecord(record);
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time to create the entity result  ->" + estimatedTime);

    }

}
