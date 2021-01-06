package changes;

public final class DistributorChange {

    private Long id;
    private Long infrastructureCost;

    public DistributorChange(Long id, Long infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    public Long getId() {
        return id;
    }

    public Long getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInfrastructureCost(Long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    @Override
    public String toString() {
        return "DistributorChange: " + "id = " + id + ", infrastructureCost = "
                + infrastructureCost;
    }
}
