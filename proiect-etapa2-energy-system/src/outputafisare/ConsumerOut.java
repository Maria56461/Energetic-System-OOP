package outputafisare;

public final class ConsumerOut {

    private final Long id;
    private final boolean isBankrupt;
    private final Long budget;

    public ConsumerOut(final Long id, final boolean isBankrupt, final Long budget) {
        this.id = id;
        this.isBankrupt = isBankrupt;
        this.budget = budget;
    }

    public Long getId() {
        return id;
    }

    public boolean getisBankrupt() {
        return isBankrupt;
    }

    public Long getBudget() {
        return budget;
    }
}
