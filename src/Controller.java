public class Controller implements Runnable {
    private final Market market;
    private int tickDuration = -1;

    public Controller(Market market) {
        this.market = market;
    }

    public Controller(Market market, int tickDuration) {
        this.market = market;
        this.tickDuration = tickDuration;
    }


    @Override
    public void run() {
        while (this.market.isActive()) {
            if (this.tickDuration != -1) {
                try {
                    Thread.sleep(tickDuration);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                /*
                    Potentially prompt every "agent" to run their algorithms here
                 */
                market.nextDay();
                System.out.println(market);
            } else {

            }
        }
    }
}
