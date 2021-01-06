package entities;

import changes.ProducerChange;

import java.util.ArrayList;
import java.util.List;

public final class Observable extends java.util.Observable {

    private List<Producer> producersList;

    public Observable(List<Producer> producersList) {
        this.producersList = producersList;
    }

    public Observable() {
        this.producersList = new ArrayList<>();
    }

    public List<Producer> getProducersList() {
        return producersList;
    }

    public void setProducersList(List<Producer> producersList) {
        this.producersList = producersList;
    }

    public void setProducersChanges(MonthlyUpdate monthlyUpdate) {

        if (monthlyUpdate.getProducerChanges() != null) {
            // daca cel putin un producator are schimbari
            for (ProducerChange change : monthlyUpdate.getProducerChanges()) {
                // parcurg lista de schimbari, caut producatorul vizat
                // si il actualizez
                for (Producer producer : this.producersList) {
                    if (producer.getId().equals(change.getId())) {
                        producer.setEnergyPerDistributor(change.getEnergyPerDistributor());
                    }
                }
            }
            setChanged();
            notifyObservers(monthlyUpdate);
        }
    }
}
