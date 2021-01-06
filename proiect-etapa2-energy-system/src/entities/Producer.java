package entities;

import java.util.ArrayList;
import java.util.Comparator;

public final class Producer {

    static final int CAPACITY = 100;

    private Long id;
    private String energyType;
    private Long maxDistributors;
    private double priceKW;
    private Long energyPerDistributor;
    private Integer numberOfDistributors;
    private ArrayList<ArrayList<Long>> distributors;

    public Producer() {
        this.id = 0L;
        this.energyType = null;
        this.maxDistributors = 0L;
        this.priceKW = 0.00;
        this.energyPerDistributor = 0L;
        this.numberOfDistributors = 0;
        this.distributors = new ArrayList<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            distributors.add(new ArrayList<>());
        }
    }

    public Producer(Long id, String energyType, Long maxDistributors, double priceKW,
                    Long energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.numberOfDistributors = 0;
        this.distributors = new ArrayList<>();
        for (int i = 0; i < CAPACITY; i++) {
            distributors.add(new ArrayList<>());
        }
    }

    public Long getId() {
        return id;
    }

    public String getEnergyType() {
        return energyType;
    }

    public Long getMaxDistributors() {
        return maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public Long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public Integer getNumberOfDistributors() {
        return numberOfDistributors;
    }

    public ArrayList<ArrayList<Long>> getDistributors() {
        return distributors;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public void setMaxDistributors(Long maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public void setPriceKW(double priceKW) {
        this.priceKW = priceKW;
    }

    public void setEnergyPerDistributor(Long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public void setNumberOfDistributors(Integer numberOfDistributors) {
        this.numberOfDistributors = numberOfDistributors;
    }

    public void setDistributors(ArrayList<ArrayList<Long>> distributors) {
        this.distributors = distributors;
    }

    @Override
    public String toString() {
        return "Producer: " + "id = " + id + ", energyType = '" + energyType + '\''
                + ", maxDistributors = " + maxDistributors + ", priceKW = " + priceKW
                + ", energyPerDistributor = " + energyPerDistributor + "\n";
    }

    public Integer compareEnergyType(Producer p1, Producer p2) {

        if ((p1.getEnergyType().equals("WIND") || p1.getEnergyType().equals("SOLAR")
                || p1.getEnergyType().equals("HYDRO")) && (p2.getEnergyType().equals("COAL")
                || p2.getEnergyType().equals("NUCLEAR"))) {
            // p1 are energie regenerabila si p2 nu
            // p1 este prioritar
            return -1;
        } else if ((p2.getEnergyType().equals("WIND") || p2.getEnergyType().equals("SOLAR")
                || p2.getEnergyType().equals("HYDRO")) && (p1.getEnergyType().equals("COAL")
                || p1.getEnergyType().equals("NUCLEAR"))) {
            // p2 are energie regenerabila si p1 nu
            // p2 este prioritar
            return 1;
        } else {
            // ambii producatori sunt la fel de prioritari
            return 0;
        }
    }

    public static Comparator<Producer> GreenComparator = (o1, o2) -> {
        int comparatie0 = o1.compareEnergyType(o1, o2);
        if (comparatie0 != 0) {
            return comparatie0;
        } else {
            // daca producatorii sunt "egali" din punct de vedere al tipului de energie
            // compar dupa pret
            int comparatie1 = Double.compare(o1.getPriceKW(), o2.getPriceKW());
            if (comparatie1 != 0) {
                return comparatie1;
            } else {
                // daca preturile sunt egale, compar dupa cantitate de energie
                int comparatie2 = o2.getEnergyPerDistributor().
                        compareTo(o1.getEnergyPerDistributor());
                if (comparatie2 != 0) {
                    return comparatie2;
                } else {
                    // daca si cantitatile sunt egale, compar dupa id
                    return o1.getId().compareTo(o2.getId());
                }
            }
        }
    };

    public static Comparator<Producer> PriceComparator = (o1, o2) -> {
        int comparatie1 = Double.compare(o1.getPriceKW(), o2.getPriceKW());
        if (comparatie1 != 0) {
            return comparatie1;
        } else {
            // daca producatorii au acelasi pret
            // compar dupa cantitatea de energie
            return o2.getEnergyPerDistributor().
                    compareTo(o1.getEnergyPerDistributor());
        }
    };

    public static Comparator<Producer> QuantityComparator =
            (o1, o2) -> o2.getEnergyPerDistributor().compareTo(o1.getEnergyPerDistributor());

}
