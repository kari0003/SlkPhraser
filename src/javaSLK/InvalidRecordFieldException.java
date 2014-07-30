package javaSLK;

/**
 * Indicates a missing or invalid record field of an SLK file.
 * 
 * @author Deaod
 * 
 */
public class InvalidRecordFieldException extends SLKParseException {
    
    private static final long serialVersionUID = -8254680066885720701L;
    
    /**
     * @param lineNumber - The number of the line which caused the exception.
     * @param description - Description of the Exception raised.
     * 
     */
    InvalidRecordFieldException(int lineNumber, String description) {
        super("Line "+lineNumber+": "+description);
    }
    
}
