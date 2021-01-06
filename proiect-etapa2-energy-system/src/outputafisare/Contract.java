package outputafisare;

import java.util.Comparator;

public final class Contract {

    private final Long consumerId;
    private final Long price;
    private final Long remainedContractMonths;

    public Contract(final Long consumerId, final Long price, final Long remainedContractMonths) {
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public Long getPrice() {
        return price;
    }

    public Long getRemainedContractMonths() {
        return remainedContractMonths;
    }

    private static final Comparator<Contract> remainedmonthscomparator =
            Comparator.comparingLong(Contract::getRemainedContractMonths)
                    .thenComparingLong(Contract::getConsumerId);

    public static Comparator<Contract> getRemainedmonthscomparator() {
        return remainedmonthscomparator;
    }
}

