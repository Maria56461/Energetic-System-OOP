package entities;

import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;
import strategies.StrategyForChoosingProducers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public final class Distributor implements Observer {

    static final double PERCENTAGE = 0.2;
    static final int DIVISION = 10;

    private Long id;
    private Long contractLength;
    private Long budget;
    private Long infrastructureCost;
    private Long energyNeededKW;
    private String producerStrategy;
    private Long profit;
    private Long pretcontract;
    private Integer numarClienti;
    private boolean isBankrupt;
    private List<Producer> producersList;
    private Long cost;

    public Distributor(Long id, Long contractLength, Long budget, Long infrastructureCost,
                       Long energyNeededKW, String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        this.profit = 0L;
        this.pretcontract = 0L;
        this.numarClienti = 0;
        this.isBankrupt = false;
        this.producersList = new ArrayList<>();
        this.cost = 0L;
    }

    public Distributor() {
        this.id = 0L;
        this.contractLength = 0L;
        this.budget = 0L;
        this.infrastructureCost = 0L;
        this.energyNeededKW = 0L;
        this.producerStrategy = null;
        this.profit = 0L;
        this.pretcontract = 0L;
        this.numarClienti = 0;
        this.isBankrupt = false;
        this.producersList = new ArrayList<>();
        this.cost = 0L;
    }

    public Long getId() {
        return id;
    }

    public Long getContractLength() {
        return contractLength;
    }

    public Long getBudget() {
        return budget;
    }

    public Long getInfrastructureCost() {
        return infrastructureCost;
    }

    public Long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public Long getProfit() {
        return profit;
    }

    public Long getPretcontract() {
        return pretcontract;
    }

    public Integer getNumarClienti() {
        return numarClienti;
    }

    public Long getCost() {
        return cost;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public List<Producer> getProducersList() {
        return producersList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContractLength(Long contractLength) {
        this.contractLength = contractLength;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public void setInfrastructureCost(Long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setEnergyNeededKW(Long energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public void setProducerStrategy(String producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public void setProfit(Long profit) {
        this.profit = profit;
    }

    public void setPretcontract(Long pretcontract) {
        this.pretcontract = pretcontract;
    }

    public void setNumarClienti(Integer numarClienti) {
        this.numarClienti = numarClienti;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public void setProducersList(List<Producer> producersList) {
        this.producersList = producersList;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Distributor: " + "id = " + id + ", contractLength = " + contractLength
                + ", budget = " + budget + ", infrastructureCost = " + infrastructureCost
                + ", energyNeededKW = " + energyNeededKW + ", producerStrategy = '"
                + producerStrategy + '\'';
    }

    public List<Producer> chooseProducers(StrategyForChoosingProducers strategy,
                                          List<Producer> inputProducers) {

        return strategy.chooseProducers(inputProducers, this.getEnergyNeededKW());
    }

    @Override
    public void update(Observable o, Object arg) {
        // distribuitorul isi actualizeaza lista de producatori

        for (Producer producer : this.getProducersList()) {
            // scot distribuitorul dintre clientii vechilor producatori
            producer.setNumberOfDistributors(producer.getNumberOfDistributors() - 1);
        }

        if (this.getProducerStrategy().equals("GREEN")) {
            this.setProducersList(this.chooseProducers(new GreenStrategy(),
                    ((entities.Observable) o).getProducersList()));
        }
        if (this.getProducerStrategy().equals("PRICE")) {
            this.setProducersList(this.chooseProducers(new PriceStrategy(),
                    ((entities.Observable) o).getProducersList()));
        }
        if (this.getProducerStrategy().equals("QUANTITY")) {
            this.setProducersList(this.chooseProducers(new QuantityStrategy(),
                    ((entities.Observable) o).getProducersList()));
        }

        for (Producer producer : this.getProducersList()) {
            // adaug distribuitorul printre clientii noilor producatori
            producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);
        }
    }

    public void getProductionCost() {

        double sum = 0L;

        for (Producer producer : this.getProducersList()) {
            sum = sum + producer.getEnergyPerDistributor() * producer.getPriceKW();
        }

        this.setCost(Math.round(Math.floor(sum / DIVISION)));
        this.setProfit(Math.round(Math.floor(PERCENTAGE * this.getCost())));
    }
}
