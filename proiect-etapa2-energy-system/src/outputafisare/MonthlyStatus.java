package outputafisare;

import java.util.List;

public final class MonthlyStatus {

    private final Long month;
    private final List<Long> distributorsIds;

    public MonthlyStatus(final Long month, final List<Long> distributorsIds) {
        this.month = month;
        this.distributorsIds = distributorsIds;
    }

    public Long getMonth() {
        return month;
    }

    public List<Long> getDistributorsIds() {
        return distributorsIds;
    }

}
