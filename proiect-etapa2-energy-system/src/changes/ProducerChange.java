package changes;

public final class ProducerChange {

    private Long id;
    private Long energyPerDistributor;

    public ProducerChange(Long id, Long energyPerDistributor) {
        this.id = id;
        this.energyPerDistributor = energyPerDistributor;
    }

    public Long getId() {
        return id;
    }

    public Long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnergyPerDistributor(Long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    @Override
    public String toString() {
        return "ProducerChange: " + "id = " + id + ", energyPerDistributor = "
                + energyPerDistributor;
    }
}
