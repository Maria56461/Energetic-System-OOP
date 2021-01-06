package strategies;

import entities.Producer;

import java.util.ArrayList;
import java.util.List;

public final class QuantityStrategy implements StrategyForChoosingProducers {

    @Override
    public List<Producer> chooseProducers(List<Producer> inputProducers, Long energyNeededKW) {

        List<Producer> result = new ArrayList<>();
        long totalEnergy = 0L;
        inputProducers.sort(Producer.QuantityComparator);

        for (Producer producer : inputProducers) {
            if (producer.getNumberOfDistributors() < producer.getMaxDistributors()) {
                result.add(producer);
                totalEnergy = totalEnergy + producer.getEnergyPerDistributor();
                if (totalEnergy >= energyNeededKW) {
                    // daca se acopera cantitatea de energie necesara
                    return result;
                }
            }
        }

        return null;
    }
}
