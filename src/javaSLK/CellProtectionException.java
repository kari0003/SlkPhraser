package javaSLK;

/**
 * Indicates a protected cell. Raised when trying to change a protected cell.
 * 
 * @author Deaod
 *
 */
public class CellProtectionException extends SLKException {
    
    private static final long serialVersionUID = 559440088650521304L;
    
    /**
     * @param x - The column number of the cell that is protected
     * @param y - The row number of the cell that is protected
     * 
     */
    CellProtectionException(int x, int y) {
        super("Cell at ("+(x+1)+","+(y+1)+") is protected.");
    }
}
