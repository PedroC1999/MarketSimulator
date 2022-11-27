public class TraderBasic extends Trader{
    private int daysElapsed = 0;

    public TraderBasic(Market market, double startFunds) {
        super(market, startFunds);
    }

    @Override
    public void Run() {
        if(daysElapsed < 10){
            //wait for more data
        }else{
            //DailyData before = this.market.findCompany("KLAC").getDailyData(market.getCurrentDate().minusDays(10));
        }
        daysElapsed++;
    }
}
