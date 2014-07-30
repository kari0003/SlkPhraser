/**
 * 
 */
package javaSLK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Deaod
 *
 */
public class SLKFile {
    private File slkFile = null;
    private boolean usedFileConstructor = false;
    private BufferedReader slkReader = null;
    private int width = 0;
    private int height = 0;
    private List<List<Object>> rows;
    private List<List<Object>> columns;
    private String writingProgram="JSLK";
    private boolean recordNERedundant=false;
    private boolean useNStyleCellProtection=false;
    private List<List<Boolean>> cellProtected;
    
    private SLKParseMethod parseMethod=SLKParseMethod.STRICT;
    
    /**
     * Returns the width of the SLK.
     * 
     * @return The number of columns of the SLK 
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Changes the width of the SLK.
     * 
     * @param width The new width.
     */
    public void setWidth(int width) {
        if (width<getWidth()) {
            for (int i=getWidth()-1;i>=width;i-=1) {
                columns.remove(i);
                for (int j=0;j<getHeight();j+=1) {
                    rows.get(j).remove(i);
                    cellProtected.get(j).remove(i);
                }
            }
        } else if (width>getWidth()) {
            for (int i=width;i<getWidth();i+=1) {
                columns.add(new ArrayList<Object>(getHeight()));
                for (int j=0;j<getHeight();j+=1) {
                    rows.get(j).add(null);
                    cellProtected.get(j).add(false);
                }
            }
        }
        this.width = width;
    }
    
    /**
     * Returns the height of the SLK.
     * 
     * @return The number of rows of the SLK.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Changes the height of the SLK.
     * 
     * @param height The new height.
     */
    public void setHeight(int height) {
        if (height<getHeight()) {
            for (int i=getHeight()-1;i>=height;i-=1) {
                rows.remove(i);
                cellProtected.remove(i);
                for (int j=0;j<getWidth();j+=1) {
                    columns.get(j).remove(i);
                }
            }
        } else if (height>getHeight()) {
            for (int i=height;i<getHeight();i+=1) {
                rows.add(new ArrayList<Object>(getWidth()));
                cellProtected.add(new ArrayList<Boolean>(getWidth()));
                for (int j=0;j<getWidth();j+=1) {
                    columns.get(j).add(null);
                }
            }
        }
        this.height = height;
    }
    
    /**
     * Combines setWidth(<i>width</i>) and setHeight(<i>height</i>).
     * 
     * @param width The new width of the SLK.
     * @param height The new height of the SLK.
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }
    
    /**
     * Inserts <i>n</i> rows before the <i>y</i><sup>th</sup> row (0 based!). If you want to append rows, use the current height of the SLK for <i>y</i>.
     * 
     * @param y Where to add the new row(s).
     * @param n Number of new rows.
     * @throws IndexOutOfBoundsException If <i>y</i> is less than 0 or greater than the current number of rows.
     */
    public void insertRows(int y, int n) {
        if (y<0 || y>getHeight()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i=0;i<n;i+=1) {
            rows.add(y+i, new ArrayList<Object>(getWidth()));
            cellProtected.add(y+i, new ArrayList<Boolean>(getWidth()));
            for (int j=0;j<getWidth();j+=1) {
                columns.get(j).add(y+i, null);
            }
        }
    }
    
    /**
     * Inserts one row before the <i>y</i><sup>th</sup> row (0 based!). If you want to append a row, use the current height of the SLK for <i>y</i>.
     * 
     * @param y Where to add the new row.
     * @throws IndexOutOfBoundsException If <i>x</i> is less than 0 or greater than the current number of columns.
     */
    public void insertRow(int y) {
        insertRows(y, 1);
    }
    
    /**
     * Inserts <i>n</i> columns before the <i>x</i><sup>th</sup> column (0 based!). If you want to append columns, use the current width of the SLK for <i>x</i>.
     * 
     * @param x Where to add the new column(s).
     * @param n Number of new columns.
     * @throws IndexOutOfBoundsException If <i>x</i> is less than 0 or greater than the current number of columns.
     */
    public void insertColumns(int x, int n) {
        if (x<0 || x>getWidth()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i=0;i<n;i+=1) {
            columns.add(x+i, new ArrayList<Object>(getHeight()));
            for (int j=0;j<getHeight();j+=1) {
                rows.get(j).add(x+i, null);
                cellProtected.get(j).add(x+i, false);
            }
        }
    }
    
    /**
     * Inserts one column before the <i>x</i><sup>th</sup> column (0 based!). If you want to append a column, use the current width of the SLK for <i>x</i>.
     * 
     * @param x Where to add the new column.
     * @throws IndexOutOfBoundsException If <i>x</i> is less than 0 or greater than the current number of columns.
     */
    public void insertColumn(int x) {
        insertColumns(x, 1);
    }
    
    /**
     * Removes <i>n</i> rows starting with the <i>y</i><sup>th</sup> row
     * 
     * @param y Which row to delete first.
     * @param n How many rows to delete.
     * @throws IndexOutOfBoundsException 
     */
    public void deleteRows(int y, int n) {
        if (y<0 || y+n>getHeight()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i=y+n-1;i>=y;i-=1) {
            rows.remove(i);
            cellProtected.remove(i);
            for (int j=0;j<getWidth();j+=1) {
                columns.get(j).remove(i);
            }
        }
    }
    
    /**
     * Deletes row <i>y</i>.
     * 
     * @param y The row to delete.
     * @throws IndexOutOfBoundsException
     */
    public void deleteRow(int y) {
        deleteRows(y, 1);
    }
    
    /**
     * Removes <i>n</i> columns starting with the <i>x</i><sup>th</sup> column.
     * 
     * @param x Which column to delete first.
     * @param n How many columns to delete.
     * @throws IndexOutOfBoundsException
     */
    public void deleteColumns(int x, int n) {
        if (x<0 || x+n>getWidth()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i=x+n-1;i>=x;i-=1) {
            columns.remove(i);
            for (int j=0;j<getWidth();j+=1) {
                rows.get(j).remove(i);
                cellProtected.get(j).remove(i);
            }
        }
    }
    
    /**
     * Deletes column <i>x</i>.
     * 
     * @param x The column to delete.
     * @throws IndexOutOfBoundsException
     */
    public void deleteColumn(int x) {
        deleteColumns(x, 1);
    }
    
    /**
     * Returns the program that initially wrote the SLK.
     * 
     * @return The program that wrote the SLK.
     */
    public String getWritingProgram() {
        return writingProgram;
    }
    
    /**
     * Changes the program that wrote the SLK.
     * 
     * @param writingProgram - A short identifier for the program.
     */
    public void setWritingProgram(String writingProgram) {
        this.writingProgram = writingProgram;
    }
    
    /**
     * Returns a read-only list of the specified row.
     * 
     * @param n Which row to get, 0 based.
     * @return Returns the <i>n</i><sup>th</sup> row of the SLK.
     * @throws IndexOutOfBoundsException.
     */
    public List<Object> getRow(int n) throws IndexOutOfBoundsException {
        if (rows.get(n)==null) {
            return null;
        }
        return Collections.unmodifiableList(rows.get(n));
    }
    
    /**
     * Returns a read-only list of the specified column.
     * 
     * @param n Which column to get, 0 based.
     * @return Returns the <i>n</i><sup>th</sup> column of the SLK.
     * @throws IndexOutOfBoundsException.
     */
    public List<Object> getColumn(int n) throws IndexOutOfBoundsException {
        if (columns.get(n)==null) {
            return null;
        }
        return (ArrayList<Object>) Collections.unmodifiableList(columns.get(n));
    }
    
    /**
     * Returns the content of the specified cell.
     * 
     * @param x 0 based horizontal index.
     * @param y 0 based vertical index.
     * @return Returns the content of the cell of the SLK.
     * @throws IndexOutOfBoundsException.
     */
    public String getCell(int x, int y) throws IndexOutOfBoundsException {
    	Object oj = rows.get(y).get(x);
    	if (oj != null) {
    		return String.valueOf(oj);
    	}
        return "";
    }
    
    /**
     * Changes the content of the cell at the given coordinates into the data passed.
     * 
     * @param x 0 based horizontal index.
     * @param y 0 based vertical index.
     * @param o Data to store in the cell.
     * @throws IndexOutOfBoundsException If the specified cell doesn't exist.
     * @throws CellProtectionException If the specified cell is protected and can't be written.
     */
    public void setCell(int x, int y, Object o) throws IndexOutOfBoundsException, CellProtectionException {
        if (cellProtected.get(y).get(x)) {
            throw new CellProtectionException(x, y);
        }
        rows.get(y).add(x, o);
        columns.get(x).add(y, o);
    }
    
    /**
     * Returns whether the cell is protected.
     * 
     * @param x The column number of the cell.
     * @param y The row number of the cell.
     * @return True if the specified cell is protected, false if the specified cell is not protected.
     * @throws IndexOutOfBoundsException If the specified cell can't be found.
     */
    public boolean isCellProtected(int x, int y) throws IndexOutOfBoundsException {
        Boolean b=cellProtected.get(y).get(x);
        if (b==null) return false;
        return b.booleanValue();
    }
    
    /**
     * Changes the protection of the specified cell.
     * 
     * @param x The column number of the cell.
     * @param y The row number of the cell.
     * @param protect - Whether to protect the cell (true) or not (false).
     * @throws IndexOutOfBoundsException If the cell youre trying to access doesn't exist.
     */
    public void setCellProtected(int x, int y, boolean protect) throws IndexOutOfBoundsException {
        cellProtected.get(y).add(x, protect);
    }
    
    /**
     * Writes the current data of the SLK to a writer.
     * 
     * @param writer The writer to write to.
     * @throws IOException If the specified writer encounters an IOException.
     */
    public void write(Writer writer) throws IOException {
        writer.write("ID;P"+writingProgram);
        if (useNStyleCellProtection) writer.write(";N");
        if (recordNERedundant) writer.write(";E");
        writer.write(System.getProperty("line.separator"));
        
        writer.write("B;X"+getWidth()+";Y"+getHeight());
        writer.write(System.getProperty("line.separator"));
        
        for (int i=0; i<getHeight();i+=1) {
            for (int j=0; j<getWidth(); j+=1) {
                writer.write("C;X"+(j+1)+";Y"+(i+1)+";K");
                
                boolean p=isCellProtected(j, i);
                if (!p&&useNStyleCellProtection) {
                    writer.write(";N");
                } else if (p&&!useNStyleCellProtection) {
                    writer.write(";P");
                }
                
                Object o=getCell(j, i);
                if (o!=null) {
                    if (o.getClass().getName()=="String") {
                        writer.write("\""+o.toString()+"\"");
                    } else {
                        writer.write(o.toString());
                    }
                } else {
                    writer.write("\"\"");
                }
                
                writer.write(System.getProperty("line.separator"));
            }
        }
        writer.write("E");
        writer.write(System.getProperty("line.separator"));
    }
    
    /**
     * Writes the current data of the SLK to the specified stream.
     * 
     * @param slk The stream you want to write the data to. Should end in '.slk'.
     * @throws IOException If the specified file cant be modified or created should it not yet exist.
     */
    public void writeToStream(OutputStream slk) throws IOException {
        PrintWriter pw=new PrintWriter(slk);
        write(pw);
        pw.close();
        
        if (pw.checkError()) {
            throw new IOException();
        }
    }
    
    /**
     * Writes the current data of the SLK to the specified file.
     * 
     * @param slk - The file you want to write the data to. Should end in '.slk'.
     * @throws IOException If the specified file cant be modified or created should it not yet exist.
     */
    public void writeToFile(File slk) throws IOException {
        writeToStream(new FileOutputStream(slk));
    }
    
    /**
     * Writes the data back to the file it was parsed from ie. the file that was passed to the constructor.
     * 
     * @return True if there was a file to be written (ie. you used a constructor taking a file), false if you used another constructor
     * @throws IOException If the specified file cant be modified or created should it not yet exist.
     * 
     */
    public boolean writeToFile() throws IOException {
        if (usedFileConstructor) {
            writeToStream(new FileOutputStream(slkFile));
            return true;
        }
        return false;
    }
    
    /**
     * Opens and parses an SLK file, making its contents accessible and changeable
     * 
     * @param path The path to the SLK file you want to open.
     * @param parseMethod Directive for handling parsing of the file.
     * @throws NullPointerException If the <i>path</i> parameter is null
     * @throws IOException If the <i>path</i> parameter points to a file you can't access, or can't create should the file not yet exist. This exception can also be raised should a general I/O error occur.
     * @throws InvalidRecordFieldException If a field of a record is invalid or missing. May not be raised if the SLK is processed gracefully.
     * @throws InvalidRecordException If a record is in an invalid place. May not be raised if the SLK is processed gracefully.
     * @throws SecurityException If a SecurityManager exists and denies read access to the file
     * 
     */
    public SLKFile(File path, SLKParseMethod parseMethod) throws IOException, InvalidRecordFieldException, InvalidRecordException {
        slkFile=path;
        usedFileConstructor=true;
        slkReader=new BufferedReader(new FileReader(path));
        slkReader.mark(new FileInputStream(path).available()+1);
        this.parseMethod=parseMethod;
        initialize();
    }
    
    /**
     * Opens and parses an SLK file, making its contents accessible and changeable
     * 
     * @param path The path to the SLK file you want to open.
     * @throws NullPointerException If the <i>path</i> parameter is null
     * @throws IOException If the <i>path</i> parameter points to a file you can't access, or can't create should the file not yet exist. This exception can also be raised should a general I/O error occur.
     * @throws InvalidRecordFieldException If a field of a record is invalid or missing. May not be raised if the SLK is processed gracefully.
     * @throws InvalidRecordException If a record is in an invalid place. May not be raised if the SLK is processed gracefully.
     * @throws SecurityException If a SecurityManager exists and denies read access to the file
     * 
     */
    public SLKFile(File path) throws IOException, InvalidRecordFieldException, InvalidRecordException {
        slkFile=path;
        usedFileConstructor=true;
        slkReader=new BufferedReader(new FileReader(path));
        slkReader.mark(new FileInputStream(path).available()+1);
        initialize();
    }
    
    /**
     * Opens and parses an SLK stream, making its contents accessible and changeable
     * 
     * @param stream The stream of data you want to open as an SLK.
     * @param parseMethod Directive for handling parsing of the file.
     * @throws NullPointerException If the <i>path</i> parameter is null
     * @throws IOException If the <i>path</i> parameter points to a file you can't access, or can't create should the file not yet exist. This exception can also be raised should a general I/O error occur.
     * @throws InvalidRecordFieldException If a field of a record is invalid or missing. May not be raised if the SLK is processed gracefully.
     * @throws InvalidRecordException If a record is in an invalid place. May not be raised if the SLK is processed gracefully.
     * @throws SecurityException If a SecurityManager exists and denies read access to the file
     * 
     */
    public SLKFile(InputStream stream, SLKParseMethod parseMethod) throws IOException, InvalidRecordFieldException, InvalidRecordException {
        slkReader=new BufferedReader(new InputStreamReader(stream));
        slkReader.mark(stream.available()+1);
        this.parseMethod=parseMethod;
        initialize();
    }
    
    /**
     * Opens and parses an SLK stream, making its contents accessible and changeable
     * 
     * @param stream The stream of data you want to open as an SLK.
     * @throws NullPointerException If the <i>path</i> parameter is null
     * @throws IOException If the <i>path</i> parameter points to a file you can't access, or can't create should the file not yet exist. This exception can also be raised should a general I/O error occur.
     * @throws InvalidRecordFieldException If a field of a record is invalid or missing. May not be raised if the SLK is processed gracefully.
     * @throws InvalidRecordException If a record is in an invalid place. May not be raised if the SLK is processed gracefully.
     * @throws SecurityException If a SecurityManager exists and denies read access to the file
     * 
     */
    public SLKFile(InputStream stream) throws IOException, InvalidRecordFieldException, InvalidRecordException {
        slkReader=new BufferedReader(new InputStreamReader(stream));
        slkReader.mark(stream.available()+1);
        initialize();
    }
    
    /**
     * Creates an empty SLK with the specified width and height.
     * 
     * @param width The initial width of the SLK file
     * @param height The initial height of the SLK file
     * @throws NullPointerException If the <i>path</i> parameter is null
     * 
     */
    public SLKFile(int width, int height) {
        this.width=width;
        this.height=height;
        
        rows=new ArrayList<List<Object>>(getHeight());
        cellProtected=new ArrayList<List<Boolean>>(getHeight());
        for (int i=0;i<getHeight();i+=1) {
            rows.add(i, new ArrayList<Object>(getWidth()));
            cellProtected.add(i, new ArrayList<Boolean>(getWidth()));
        }
        
        columns=new ArrayList<List<Object>>(getWidth());
        for (int i=0;i<getWidth();i+=1) {
            columns.add(i, new ArrayList<Object>(getHeight()));
        }
    }
    
    /**
     * Creates an empty SLK with a width and height of zero.
     * 
     * @throws NullPointerException If the <i>path</i> parameter is null
     * @throws IOException If the <i>path</i> parameter points to a file you can't access, or can't create should the file not yet exist. This exception can also be raised should a general I/O error occur.
     * @throws InvalidRecordFieldException If a field of a record is invalid or missing. May not be raised if the SLK is processed gracefully.
     * @throws InvalidRecordException If a record is in an invalid place. May not be raised if the SLK is processed gracefully.
     * @throws SecurityException If a SecurityManager exists and denies read access to the file
     */
    public SLKFile() throws InvalidRecordFieldException, InvalidRecordException, IOException {
        initialize();
    }
    
    private void initialize() throws InvalidRecordFieldException, IOException, InvalidRecordException {
        parseSize();
        
        rows=new ArrayList<List<Object>>(getHeight());
        cellProtected=new ArrayList<List<Boolean>>(getHeight());
        for (int i=0;i<getHeight();i+=1) {
            
            rows.add(i, new ArrayList<Object>(getWidth()));
            cellProtected.add(i, new ArrayList<Boolean>(getWidth()));
        }
        
        columns=new ArrayList<List<Object>>(getWidth());
        for (int i=0;i<getWidth();i+=1) {
            
            columns.add(i, new ArrayList<Object>(getHeight()));
        }
        
        if (getWidth()>0 && getHeight()>0) {
            // only parse data if its necessary
            parseMetaData();
            parseCells();
            fillCells();
        }
    }
    
    private void fillCells() {
        for (int i=0; i<getHeight(); i+=1) {
            for (int j=0; j<getWidth(); j+=1) {
                try {
                    getCell(j, i);
                } catch (Exception e) {
                    rows.get(i).add(j, null);
                    cellProtected.get(i).add(j, false);
                    columns.get(j).add(i, null);
                }
            }
        }
    }

    private int getXPosOfCell(String line, int lineNumber, boolean mandatoryField) throws InvalidRecordFieldException {
        if (line.indexOf(";X")>=0) {
            String xparse=line.substring(line.indexOf(";X")+2);
            if (xparse.indexOf(";")>=0) {
                xparse=xparse.substring(0, xparse.indexOf(";"));
            }
            try {
                return Integer.parseInt(xparse)-1;
            } catch (NumberFormatException e) {
                if (parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordFieldException(lineNumber, "Field X has invalid value.");
                } else {
                    return -1;
                }
            }
        } else if (mandatoryField && parseMethod==SLKParseMethod.STRICT) {
            throw new InvalidRecordFieldException(lineNumber, "Mandatory Field X missing.");
        } else {
            return -1;
        }
    }
    
    private int getYPosOfCell(String line, int lineNumber, boolean mandatoryField) throws InvalidRecordFieldException {
        if (line.indexOf(";Y")>=0) {
            String yparse=line.substring(line.indexOf(";Y")+2);
            if (yparse.indexOf(";")>=0) {
                yparse=yparse.substring(0, yparse.indexOf(";"));
            }
            try {
                return Integer.parseInt(yparse)-1;
            } catch (NumberFormatException e) {
                if (parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordFieldException(lineNumber, "Field Y has invalid value.");
                } else {
                    return -1;
                }
            }
        } else if (mandatoryField && parseMethod==SLKParseMethod.STRICT) {
            throw new InvalidRecordFieldException(lineNumber, "Mandatory Field Y missing.");
        } else {
            return -1;
        }
    }
    
    private void parseMetaData() throws IOException, InvalidRecordException, InvalidRecordFieldException {
        String line=slkReader.readLine();
        int lineNumber=1;
        boolean firstDataParsed=false;
        boolean recordEParsed=false;
        while (line!=null) {
            if (line.startsWith("B")) {
                // doesnt contain actual data, ignore
                firstDataParsed=true;
                if (recordEParsed && parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordException(lineNumber, "Record E must be last record.");
                }
            } else if (line.startsWith("C")) {
                // will get parsed later on, ignore for now
                firstDataParsed=true;
                if (recordEParsed && parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordException(lineNumber, "Record E must be last record.");
                }
            } else if (line.startsWith("ID")) {
                if (line.indexOf(";P")>=0) {
                    String programparse=line.substring(line.indexOf(";P")+2);
                    if (programparse.indexOf(";")>=0) {
                        programparse=programparse.substring(0, programparse.indexOf(";"));
                    }
                    writingProgram=programparse;
                } else if (parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordFieldException(lineNumber, "Mandatory field P missing.");
                }
                if (line.indexOf(";N")>=0) {
                    useNStyleCellProtection=true;
                }
                if (line.indexOf(";E")>=0) {
                    recordNERedundant=true;
                }
                if (firstDataParsed && parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordException(lineNumber, "Record ID must be first record.");
                }
                if (recordEParsed && parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordException(lineNumber, "Record E must be last record.");
                }
            } else if (line.startsWith("E")) {
                recordEParsed=true;
                if (!line.equals("E") && parseMethod!=SLKParseMethod.LIBERAL) {
                    throw new InvalidRecordException(lineNumber, "Record E must not have any fields");
                }
            }
            line=slkReader.readLine();
            lineNumber+=1;
        }
        slkReader.reset();
    }

    private int currentX=0;
    private int currentY=0;
    
    private void parseCell(String line, int lineNumber) throws InvalidRecordFieldException {
        
        int newX=getXPosOfCell(line, lineNumber, parseMethod!=SLKParseMethod.BLIZZARD);
        if (newX>=0) {
            currentX=newX;
        }
        int newY=getYPosOfCell(line, lineNumber, parseMethod!=SLKParseMethod.BLIZZARD);
        if (newY>=0) {
            currentY=newY;
        }
        
        // Field K
        if (line.indexOf(";K")>=0) {
            String dataparse=line.substring(line.indexOf(";K")+2);
            if (dataparse.indexOf(";")>=0) {
                dataparse=dataparse.substring(0, dataparse.indexOf(";"));
            }
            Object data=null;
            if (dataparse.startsWith("\"")) {
                // its a string
                data=dataparse.substring(1, dataparse.length()-1);
            } else {
                try {
                    // try reading it as an Integer
                    data=Integer.valueOf(dataparse);
                } catch (NumberFormatException e) {
                    // not an integer
                    try {
                        // try reading as a float
                        data=Float.valueOf(dataparse);
                    } catch (NumberFormatException e2) {
                        // not a float
                        if (dataparse.toLowerCase().equals("true") || dataparse.toLowerCase().equals("false")) {
                            // see if its a boolean
                            data=Boolean.valueOf(dataparse);
                        } else {
                            if (parseMethod==SLKParseMethod.STRICT) {
                                throw new InvalidRecordFieldException(lineNumber, "Ambiguous data found.");
                            }
                            data=dataparse;
                        }
                    }
                }
            }
            // write the data into the tables
            while (true) {
                try {
                    rows.get(currentY).add(currentX, data);
                    break;
                } catch (Exception e) {
                    rows.get(currentY).add(null);
                }
            }
            while (true) {
                try {
                    columns.get(currentX).add(currentY, data);
                    break;
                } catch (Exception e) {
                    columns.get(currentX).add(null);
                }
            }
        }
        // Field K
        
        boolean fieldPPresent=false;
        boolean fieldNPresent=false;
        if (line.indexOf(";P")>=0) {
            fieldPPresent=true;
            if (useNStyleCellProtection && parseMethod!=SLKParseMethod.LIBERAL) {
                throw new InvalidRecordFieldException(lineNumber, "Cell is already protected because field N is present in record ID. Field P must be absent.");
            }
        }
        if (line.indexOf(";N")>=0) {
            fieldNPresent=true;
            if (!useNStyleCellProtection && parseMethod!=SLKParseMethod.LIBERAL) {
                throw new InvalidRecordFieldException(lineNumber, "Cell is already unprotected because field N is absent in record ID. Field N must be absent.");
            }
        }
        if (fieldPPresent && fieldNPresent && parseMethod!=SLKParseMethod.LIBERAL) {
            throw new InvalidRecordFieldException(lineNumber, "Ambiguous cell protection. Field P and field N are present.");
        }
        while (true) {
            try {
                cellProtected.get(currentY).add(currentX, ((useNStyleCellProtection&&!fieldNPresent)||(fieldPPresent&&!fieldNPresent)||(useNStyleCellProtection&&fieldPPresent)));
                break;
            } catch (Exception e) {
                cellProtected.get(currentY).add(false);
            }
        }
        
    }
    
    private void parseCells() throws IOException, InvalidRecordFieldException, InvalidRecordException {
        String line=slkReader.readLine();
        int lineNumber=1;
        currentX=0;
        currentY=0;
        while (line!=null) {
            if (line.startsWith("C")) {
                parseCell(line, lineNumber);
            }
            line=slkReader.readLine();
            lineNumber+=1;
        }
        slkReader.reset();
    }

    private void parseSize() throws IOException, InvalidRecordFieldException {
        String line=slkReader.readLine();
        int lineNumber=1;
        while (line!=null) {
            // gracefully handle the absence of a record of type B
            if (line.startsWith("C")) {
                width=Math.max(width, 1+getXPosOfCell(line, lineNumber, parseMethod!=SLKParseMethod.BLIZZARD));
                height=Math.max(height, 1+getYPosOfCell(line, lineNumber, parseMethod!=SLKParseMethod.BLIZZARD));
            } else if (line.startsWith("B")) {
                width=Math.max(width, 1+getXPosOfCell(line, lineNumber, true));
                height=Math.max(height, 1+getYPosOfCell(line, lineNumber, true));
            }
            line=slkReader.readLine();
            lineNumber+=1;
        }
        slkReader.reset();
    }
}
