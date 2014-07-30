/**
 * 
 */
package javaSLK;

/**
 * @author Deaod
 *
 */
public enum SLKParseMethod {
    /**
     * Enforces the SLK specs.
     */
    STRICT,
    /**
     * More lenient way of parsing, like relying on context for cell positions.
     */
    BLIZZARD,
    /**
     * Least restrictive way of parsing. Tries to gather as much data as possible. May parse data incorrectly.
     */
    LIBERAL;
    
}
