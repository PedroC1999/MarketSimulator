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

            } else {
                try {
                    Thread.sleep(tickDuration);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                market.nextDay();
                for (Trader e:market.getControlledTraders()) {
                    e.Run();
                }
                System.out.println(market);
            }
        }
    }
}
