package entities;

public final class Consumer {

    private Long id;
    private Long budget;
    private Long monthlyIncome;
    private Distributor distributor;
    private boolean isBankrupt;
    private Long currentContractMonth;
    private Long contractPrice;
    private boolean indatorat;
    private Long datorie;

    public Consumer(final Long id, Long budget, final Long monthlyIncome) {
        this.id = id;
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
        this.distributor = null;
        this.isBankrupt = false;
        this.currentContractMonth = 0L;
        this.contractPrice = 0L;
        this.indatorat = false;
        this.datorie = 0L;
    }

    public Consumer() {
        this.id = 0L;
        this.budget = 0L;
        this.monthlyIncome = 0L;
        this.distributor = null;
        this.isBankrupt = false;
        this.currentContractMonth = 0L;
        this.contractPrice = 0L;
        this.indatorat = false;
        this.datorie = 0L;

    }

    public Long getId() {
        return id;
    }

    public Long getBudget() {
        return budget;
    }

    public Long getMonthlyIncome() {
        return monthlyIncome;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public Long getCurrentContractMonth() {
        return currentContractMonth;
    }

    public Long getContractPrice() {
        return contractPrice;
    }

    public boolean isIndatorat() {
        return indatorat;
    }

    public Long getDatorie() {
        return datorie;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public void setMonthlyIncome(Long monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public void setCurrentContractMonth(Long currentContractMonth) {
        this.currentContractMonth = currentContractMonth;
    }

    public void setContractPrice(Long contractPrice) {
        this.contractPrice = contractPrice;
    }

    public void setIndatorat(boolean indatorat) {
        this.indatorat = indatorat;
    }

    public void setDatorie(Long datorie) {
        this.datorie = datorie;
    }

    @Override
    public String toString() {
        return "Consumer: " + "id = " + id + " budget = " + budget + " monthlyIncome = "
                + monthlyIncome;
    }
}
