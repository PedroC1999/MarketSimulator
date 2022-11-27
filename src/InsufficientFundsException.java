/**
 * The Insufficient Funds Exception,
 * represents an error caused by the trader not having enough funds to complete a transaction
 */
public class InsufficientFundsException extends RuntimeException {
    /**
     * Instantiates a new Insufficient funds exception,
     * thrown when the trader not having enough funds to complete a transaction
     *
     * @param errorMessage Error message highlighting the discrepancy in the funds needed.
     */
    public InsufficientFundsException(String errorMessage) {
        super(errorMessage);
    }
}
