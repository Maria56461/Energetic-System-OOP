package outputafisare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ProducerOut {

    private final Long id;
    private final Long maxDistributors;
    private final double priceKW;
    private final String energyType;
    private final Long energyPerDistributor;
    private List<MonthlyStatus> monthlyStats = new ArrayList<>();

    public ProducerOut(final Long id, final Long maxDistributors, final double priceKW,
                       final String energyType, final Long energyPerDistributor) {
        this.id = id;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyType = energyType;
        this.energyPerDistributor = energyPerDistributor;
    }

    public void setMonthlyStats(List<MonthlyStatus> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public Long getId() {
        return id;
    }

    public Long getMaxDistributors() {
        return maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public String getEnergyType() {
        return energyType;
    }

    public Long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public List<MonthlyStatus> getMonthlyStats() {
        return monthlyStats;
    }

    public static Comparator<ProducerOut> IDComparator =
            Comparator.comparing(ProducerOut::getId);
}
