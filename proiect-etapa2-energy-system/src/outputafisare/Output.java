package outputafisare;

import java.util.ArrayList;
import java.util.List;

public final class Output {

    private final List<ConsumerOut> consumers = new ArrayList<>();
    private final List<DistributorOut> distributors = new ArrayList<>();
    private final List<ProducerOut> energyProducers = new ArrayList<>();

    public List<ConsumerOut> getConsumers() {
        return consumers;
    }

    public List<DistributorOut> getDistributors() {
        return distributors;
    }

    public List<ProducerOut> getEnergyProducers() {
        return energyProducers;
    }
}
