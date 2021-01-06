package inputcitire;

import entities.Consumer;
import entities.Distributor;
import entities.MonthlyUpdate;
import entities.Producer;

import java.util.List;

public final class Input {

    private final Long numberOfTurns;
    private final List<Consumer> consumerslist;
    private final List<Distributor> distributorslist;
    private final List<Producer> producerslist;
    private final List<MonthlyUpdate> monthlyupdates;

    public Input(final Long numberOfTurns, final List<Consumer> consumerslist, final
                List<Distributor> distributorslist, final List<Producer> producerslist, final
                List<MonthlyUpdate> monthlyupdates) {
        this.numberOfTurns = numberOfTurns;
        this.consumerslist = consumerslist;
        this.distributorslist = distributorslist;
        this.producerslist = producerslist;
        this.monthlyupdates = monthlyupdates;
    }

    public Long getNumberOfTurns() {
        return numberOfTurns;
    }

    public List<Consumer> getConsumerslist() {
        return consumerslist;
    }

    public List<Distributor> getDistributorslist() {
        return distributorslist;
    }

    public List<Producer> getProducerslist() {
        return producerslist;
    }

    public List<MonthlyUpdate> getMonthlyupdates() {
        return monthlyupdates;
    }
}
