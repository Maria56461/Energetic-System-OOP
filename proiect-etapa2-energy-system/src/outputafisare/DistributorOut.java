package outputafisare;

import java.util.ArrayList;
import java.util.List;

public final class DistributorOut {

    private final Long id;
    private final Long energyNeededKW;
    private final Long contractCost;
    private final Long budget;
    private final String producerStrategy;
    private final boolean isBankrupt;
    private List<Contract> contracts = new ArrayList<>();

    public DistributorOut(final Long id, final Long energyNeededKW, final Long contractCost,
                          final Long budget, final String producerStrategy,
                          final boolean isBankrupt) {
        this.id = id;
        this.energyNeededKW = energyNeededKW;
        this.contractCost = contractCost;
        this.budget = budget;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = isBankrupt;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public Long getId() {
        return id;
    }

    public Long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public Long getContractCost() {
        return contractCost;
    }

    public Long getBudget() {
        return budget;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public boolean getisBankrupt() {
        return isBankrupt;
    }
}
