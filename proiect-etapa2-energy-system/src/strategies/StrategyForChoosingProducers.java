package strategies;

import entities.Producer;

import java.util.List;

public interface StrategyForChoosingProducers {

    List<Producer> chooseProducers(List<Producer> inputProducers, Long energyNeededKW);
}
