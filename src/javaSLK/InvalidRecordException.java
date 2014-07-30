package javaSLK;

/**
 * Indicates illegal placement or absence of required records of an SLK file.
 * 
 * @author Deaod
 *
 */
public class InvalidRecordException extends SLKParseException {
    
    private static final long serialVersionUID = -892596442473583955L;
    
    /**
     * @param lineNumber - The number of the line which caused the exception.
     * @param description - Description of the Exception raised.
     * 
     */
    InvalidRecordException(int lineNumber, String description) {
        super("Line "+lineNumber+": "+description);
    }
}
