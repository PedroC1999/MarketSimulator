/**
 * The Insufficient Resources Exception,
 * represents an error caused by the trader not having enough stocks to complete a transaction
 */
public class InsufficientResourcesException extends RuntimeException {
    /**
     * Instantiates a new Insufficient Resources Exception,
     * thrown when the trader not having enough stocks to complete a transaction
     *
     * @param errorMessage Error message highlighting the discrepancy in the stocks needed.
     */
    public InsufficientResourcesException(String errorMessage) {
        super(errorMessage);
    }
}