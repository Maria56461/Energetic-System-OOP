package entities;

import changes.DistributorChange;
import changes.ProducerChange;

import java.util.ArrayList;
import java.util.List;

public final class MonthlyUpdate {

    private List<Consumer> newConsumers;
    private List<DistributorChange> distributorChanges;
    private List<ProducerChange> producerChanges;

    public MonthlyUpdate() {
        this.newConsumers = new ArrayList<>();
        this.distributorChanges = new ArrayList<>();
        this.producerChanges = new ArrayList<>();
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public List<DistributorChange> getDistributorChanges() {
        return distributorChanges;
    }

    public List<ProducerChange> getProducerChanges() {
        return producerChanges;
    }

    public void setNewConsumers(List<Consumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public void setDistributorChanges(List<DistributorChange> distributorChanges) {
        this.distributorChanges = distributorChanges;
    }

    public void setProducerChanges(List<ProducerChange> producerChanges) {
        this.producerChanges = producerChanges;
    }

    @Override
    public String toString() {
        return "MonthlyUpdate: " + "newConsumers = " + newConsumers + ", distributorChanges = "
                + distributorChanges + ", producerChanges = " + producerChanges;
    }
}
