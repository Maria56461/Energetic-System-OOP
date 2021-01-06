import changes.DistributorChange;
import changes.ProducerChange;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Consumer;
import entities.Distributor;
import entities.Observable;
import entities.Producer;
import inputcitire.CitireInput;
import inputcitire.Input;
import outputafisare.ConsumerOut;
import outputafisare.Contract;
import outputafisare.DistributorOut;
import outputafisare.MonthlyStatus;
import outputafisare.Output;
import outputafisare.ProducerOut;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    static final long INF = 1000000L;
    static final double NEW_PERCENTAGE = 1.2;

    /**
     * Main function which reads the input file and starts simulation
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {

        CitireInput citire = new CitireInput(args[0]);
        Input input = citire.readData();
        List<Producer> producersToBeConsidered = new ArrayList<>();
        // lista cu producatorii care inca nu au atins numarul maxim de distribuitori

        for (Producer producer : input.getProducerslist()) {
            producersToBeConsidered.add(producer);
        }

        for (Distributor distributor : input.getDistributorslist()) {
            // fiecare distribuitor isi alege producatorii in functie de strategie
            switch (distributor.getProducerStrategy()) {
                case "GREEN" -> {
                    distributor.setProducersList(distributor.chooseProducers(new GreenStrategy(),
                            producersToBeConsidered));
                    for (Producer producer : distributor.getProducersList()) {
                        producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);

                        producer.getDistributors().get(0).add(distributor.getId());
                        // pe linia 0 am distribuitorii din luna 0
                        if (producer.getNumberOfDistributors() >= producer.getMaxDistributors()) {
                            // daca vreunul din producatorii alesi
                            // a atins numarul maxim de distribuitori pe care ii poate avea
                            // nu il mai iau de acum in considerare
                            producersToBeConsidered.remove(producer);
                        }
                    }
                }
                case "PRICE" -> {
                    distributor.setProducersList(distributor.chooseProducers(new PriceStrategy(),
                            producersToBeConsidered));
                    for (Producer producer : distributor.getProducersList()) {
                        producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);
                        producer.getDistributors().get(0).add(distributor.getId());
                        if (producer.getNumberOfDistributors() >= producer.getMaxDistributors()) {
                            producersToBeConsidered.remove(producer);
                        }
                    }
                }
                case "QUANTITY" -> {
                    distributor.setProducersList(distributor.chooseProducers(new QuantityStrategy(),
                            producersToBeConsidered));
                    for (Producer producer : distributor.getProducersList()) {
                        producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);
                        producer.getDistributors().get(0).add(distributor.getId());
                        if (producer.getNumberOfDistributors() >= producer.getMaxDistributors()) {
                            producersToBeConsidered.remove(producer);
                        }
                    }
                }
            }
            distributor.getProductionCost();
            // setez fiecarui distribuitor costul de productie si profitul
            distributor.setPretcontract(distributor.getCost() + distributor.getInfrastructureCost()
                    + distributor.getProfit());
            // calculez pentru fiecare distribuitor pretul contractului
        }

        Long minCost = INF;
        // aflu cea mai mica rata pentru luna 0
        for (Distributor distribuitor : input.getDistributorslist()) {
            if (distribuitor.getPretcontract() < minCost) {
                minCost = distribuitor.getPretcontract();
            }
        }

        Distributor bestDistributor = null;

        for (Distributor distributor : input.getDistributorslist()) {
            if (distributor.getPretcontract().equals(minCost)) {
                // gasesc distribuitorul cu cel mai mic pret al contractului
                bestDistributor = distributor;
                break;
            }
        }

        for (Consumer consumer : input.getConsumerslist()) {
            // in luna 0, toti consumatorii incep contract la distribuitorul
            // care ofera cel mai bun pret
            consumer.setDistributor(bestDistributor);
            assert bestDistributor != null;
            consumer.setContractPrice(bestDistributor.getPretcontract());
            bestDistributor.setNumarClienti(bestDistributor.getNumarClienti() + 1);
        }

        for (Consumer consumer : input.getConsumerslist()) {
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            // consumatorii si isi primesc venitul lunar
            if (consumer.getBudget() >= consumer.getContractPrice()) {
                // daca clientul are bani sa plateasca factura, o plateste
                consumer.setBudget(consumer.getBudget() - consumer.getContractPrice());
            } else {
                // daca nu are bani sa plateasca factura
                consumer.setIndatorat(true);
                consumer.setDatorie(consumer.getContractPrice());
            }
        }

        for (Distributor distribuitor : input.getDistributorslist()) {
            long sumaClienti = 0L;
            for (Consumer consumer : input.getConsumerslist()) {
                // caut consumatorii care au acest distribuitor
                if (consumer.getDistributor() == distribuitor) {
                    if (!(consumer.isIndatorat())) {
                        sumaClienti = sumaClienti + consumer.getContractPrice();
                        // distribuitorul ia banii de la consumator
                    }
                }
            }
            distribuitor.setBudget(distribuitor.getBudget() - distribuitor.getInfrastructureCost()
                        - (distribuitor.getCost() * distribuitor.getNumarClienti()) + sumaClienti);
            // distribuitorii platesc costurile lor si primesc bani de la consumatori
            if (distribuitor.getBudget() < 0) {
                distribuitor.setBankrupt(true);
                distribuitor.setNumarClienti(0);
                for (Consumer consumer : input.getConsumerslist()) {
                    if (consumer.getDistributor() == distribuitor) {
                        // toti consumatoii care au acest distribuitor
                        // incep alt contract luna urmatoare
                        consumer.setCurrentContractMonth(consumer.getDistributor().
                                getContractLength());
                    }
                }
            }
        }

        for (int i = 1; i <= input.getNumberOfTurns(); i++) {
            //pentru fiecare luna
            // actualizez infrastructura distribuitorilor
            for (DistributorChange schimbare
                    : input.getMonthlyupdates().get(i - 1).getDistributorChanges()) {
                // parcurg lista de schimbari ale infrastructurii anumitor distribuitori
                for (Distributor distributor : input.getDistributorslist()) {
                    // caut distribuitorul cu id-ul din schimbare
                    if (distributor.getId().equals(schimbare.getId())) {
                        distributor.setInfrastructureCost(schimbare.getInfrastructureCost());
                    }
                }
            }
            // actualizez lista de consumatori
            if (input.getMonthlyupdates().get(i - 1).getNewConsumers() != null) {
                for (Consumer newConsumer
                        : input.getMonthlyupdates().get(i - 1).getNewConsumers()) {
                    // parcurg lista de consumatori noi
                    input.getConsumerslist().add(newConsumer);
                }
            }

            for (Distributor distributor : input.getDistributorslist()) {
                if (!distributor.isBankrupt()) {
                    // recalculez pretul contractului
                    if (distributor.getNumarClienti() > 0) {
                        // daca distribuitorul curent are clienti
                        distributor.setPretcontract(Math.round(Math.floor(
                                distributor.getInfrastructureCost() / distributor.getNumarClienti())
                                + distributor.getCost() + distributor.getProfit()));
                    } else if (distributor.getNumarClienti() == 0) {
                        // daca distribuitorul curent nu are clienti
                        distributor.setPretcontract(distributor.getInfrastructureCost()
                                        + distributor.getCost() + distributor.getProfit());
                    }
                }
            }

            Long minimum = INF;
            // aflu cea mai mica rata pentru luna i
            for (Distributor distribuitor : input.getDistributorslist()) {
                if (!distribuitor.isBankrupt()) {
                    if (distribuitor.getPretcontract() < minimum) {
                        minimum = distribuitor.getPretcontract();
                    }
                }
            }
            Distributor best = null;
            for (Distributor distributor : input.getDistributorslist()) {
                if (!distributor.isBankrupt()) {
                    if (distributor.getPretcontract().equals(minimum)) {
                        // gasesc distribuitorul cu cel mai mic pret al contractului
                        best = distributor;
                        break;
                    }
                }
            }

            for (Consumer consumer : input.getConsumerslist()) {
                if (!consumer.isBankrupt()) {
                    if (consumer.getDistributor() == null) {
                        // daca este un consumator nou-introdus, va incepe un
                        // contract cu best_distributor
                        consumer.setCurrentContractMonth(0L);
                        consumer.setDistributor(best);
                        assert best != null;
                        consumer.setContractPrice(best.getPretcontract());
                        best.setNumarClienti(best.getNumarClienti() + 1);
                        consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
                        // isi primeste venitul lunar
                        if (consumer.getBudget() >= consumer.getContractPrice()) {
                            // daca clientul are bani sa plateasca factura, o plateste
                            consumer.setBudget(consumer.getBudget() - consumer.getContractPrice());
                        } else {
                            // daca nu are bani sa plateasca factura
                            consumer.setIndatorat(true);
                            consumer.setDatorie(consumer.getContractPrice());
                        }
                    } else {
                        consumer.setCurrentContractMonth(consumer.getCurrentContractMonth() + 1);
                        if (consumer.getCurrentContractMonth()
                                .equals(consumer.getDistributor().getContractLength())) {
                            // daca pentru un consumator se termina contractul
                            // acesta incepe alt contract
                            consumer.setCurrentContractMonth(0L);
                            if (consumer.isIndatorat()) {
                                // daca are o datorie din ultima luna a contractului anterior
                                if (consumer.getDistributor() != best) {
                                    // poate amana noua factura
                                    // daca are bani doar pentru cea veche plus penalitati
                                    consumer.getDistributor().setNumarClienti(
                                            consumer.getDistributor().getNumarClienti() - 1);
                                    consumer.setDistributor(best);
                                    assert best != null;
                                    consumer.setContractPrice(best.getPretcontract());
                                    best.setNumarClienti(best.getNumarClienti() + 1);
                                    // incepe noul contract
                                    consumer.setBudget(consumer.getBudget()
                                            + consumer.getMonthlyIncome());
                                    // isi primeste venitul lunar
                                    Long datorie = Math.round(Math.floor(NEW_PERCENTAGE
                                            * consumer.getDatorie())) + consumer.getContractPrice();
                                    if (consumer.getBudget() >= datorie) {
                                        consumer.setBudget(consumer.getBudget() - datorie);
                                        consumer.setIndatorat(false);
                                        consumer.setDatorie(0L);
                                    } else {
                                        // daca nu are bani sa platasca tot
                                        Long secondChance = Math.round(Math.floor(NEW_PERCENTAGE
                                                * consumer.getDatorie()));
                                        if (consumer.getBudget() >= secondChance) {
                                            // daca totusi are bani pentru factura veche
                                            consumer.setBudget(consumer.getBudget() - secondChance);
                                            consumer.setDatorie(consumer.getContractPrice());
                                            // ramane indatorat cu factura noua
                                        } else {
                                            // intra in faliment
                                            consumer.setBankrupt(true);
                                        }
                                    }
                                } else {
                                    // daca contractul nou este inceput la acelasi distribuitor
                                    assert best != null;
                                    consumer.setContractPrice(best.getPretcontract());
                                    consumer.setBudget(consumer.getBudget()
                                            + consumer.getMonthlyIncome());
                                    // isi primeste venitul lunar
                                    Long datorie = Math.round(Math.floor(NEW_PERCENTAGE
                                            * consumer.getDatorie())) + consumer.getContractPrice();
                                    if (consumer.getBudget() >= datorie) {
                                        consumer.setBudget(consumer.getBudget() - datorie);
                                        consumer.setIndatorat(false);
                                        consumer.setDatorie(0L);
                                    } else {
                                        // daca nu are bani sa platasca tot
                                        // intra direct in faliment
                                        consumer.setBankrupt(true);
                                    }
                                }
                            } else {
                                // daca incepe un nou contract fara a fi indatorat
                                consumer.getDistributor().setNumarClienti(
                                        consumer.getDistributor().getNumarClienti() - 1);
                                consumer.setDistributor(best);
                                assert best != null;
                                consumer.setContractPrice(best.getPretcontract());
                                best.setNumarClienti(best.getNumarClienti() + 1);
                                // isi primeste venitul lunar
                                consumer.setBudget(consumer.getBudget()
                                        + consumer.getMonthlyIncome());
                                if (consumer.getBudget() >= consumer.getContractPrice()) {
                                    // daca clientul are bani sa plateasca factura, o plateste
                                    consumer.setBudget(consumer.getBudget()
                                            - consumer.getContractPrice());
                                } else {
                                    // daca nu are bani pentru factura, ramane cu datorie
                                    consumer.setIndatorat(true);
                                    consumer.setDatorie(consumer.getContractPrice());
                                }
                            }
                        } else {
                            // daca nu i s-a terminat contractul, mai plateste aceeasi suma
                            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
                            // isi primeste venitul lunar
                            if (consumer.getBudget() >= consumer.getContractPrice()) {
                                // daca clientul are bani sa plateasca factura
                                if (consumer.isIndatorat()) {
                                    // daca este dator cu o luna de plata
                                    Long datorie = Math.round(Math.floor(NEW_PERCENTAGE
                                            * consumer.getDatorie())) + consumer.getContractPrice();
                                    if (consumer.getBudget() >= datorie) {
                                        // daca are bani sa plateasca datoria
                                        consumer.setBudget(consumer.getBudget() - datorie);
                                        consumer.setIndatorat(false);
                                    } else {
                                        consumer.setBankrupt(true);
                                        // da faliment si il scot din joc
                                    }
                                } else {
                                    consumer.setBudget(consumer.getBudget()
                                            - consumer.getContractPrice());
                                }
                            } else {
                                // daca nu are bani sa plateasca factura
                                if (consumer.isIndatorat()) {
                                    // daca are datorie de luna trecuta
                                    consumer.setBankrupt(true);
                                } else {
                                    consumer.setIndatorat(true);
                                    consumer.setDatorie(consumer.getContractPrice());
                                }
                            }
                        }
                    }
                }
            }

            for (Distributor distribuitor : input.getDistributorslist()) {
                if (!distribuitor.isBankrupt()) {
                    long sumaClienti = 0L;
                    for (Consumer consumer : input.getConsumerslist()) {
                        // caut consumatorii care au acest distribuitor
                        if (consumer.getDistributor() == distribuitor) {
                            if (!(consumer.isIndatorat()) && (!consumer.isBankrupt())) {
                                sumaClienti = sumaClienti + consumer.getContractPrice();
                                // distribuitorul ia banii de la consumator
                            }
                        }
                    }
                    distribuitor.setBudget(distribuitor.getBudget()
                            - distribuitor.getInfrastructureCost() - (distribuitor.getCost()
                            * distribuitor.getNumarClienti())
                            + sumaClienti);
                    // distribuitorii platesc costurile lor si primesc bani de la consumatori
                    if (distribuitor.getBudget() < 0) {
                        distribuitor.setBankrupt(true);
                        distribuitor.setNumarClienti(0);
                        for (Consumer consumer : input.getConsumerslist()) {
                            if ((!consumer.isBankrupt()) && (consumer.getDistributor()
                                    == distribuitor)) {
                                // toti consumatoii care au acest distribuitor
                                // incep alt contract luna urmatoare
                                consumer.setCurrentContractMonth(consumer.getDistributor().
                                        getContractLength());
                            }
                        }
                    }
                }
            }

            Observable producers = new Observable();
            producers.setProducersList(input.getProducerslist());
            Collections.reverse(input.getDistributorslist());
            for (Distributor distributor : input.getDistributorslist()) {
                // parcurg lista de distribuitori
                if (!distributor.isBankrupt()) {
                    for (Producer producer : distributor.getProducersList()) {
                        for (ProducerChange producer1
                                : input.getMonthlyupdates().get(i - 1).getProducerChanges()) {
                            if (producer1.getId().equals(producer.getId())) {
                                // distribuitorul trebuie sa isi aplice iar strategia
                                // deci il adaug ca observator
                                producers.addObserver(distributor);
                            }
                        }
                    }
                    // am adaugat ca observatori doar distribuitorii care aveau
                    // printre producatori cel putin unul care trebuie actualizat
                }
            }
            Collections.reverse(input.getDistributorslist());
            producers.setProducersChanges(input.getMonthlyupdates().get(i - 1));
            // se actualizeaza cantitatea de energie a producatorilor
            // si lista de producatori a distribuitorilor afectati de schimbari

            for (Producer producer : input.getProducerslist()) {
                for (Distributor distributor : input.getDistributorslist()) {
                    // caut distribuitorii care au acest producator
                    if (!distributor.isBankrupt()) {
                        for (Producer producer1 : distributor.getProducersList()) {
                            if (producer.getId().equals(producer1.getId())) {
                                producer.getDistributors().get(i).add(distributor.getId());
                                // construiesc lista de distribuitori ai producatorului
                            }
                        }
                    }
                }
            }

            for (Distributor distributor : input.getDistributorslist()) {
                if (!distributor.isBankrupt()) {
                    // recalculez costul de productie si profitul
                    distributor.getProductionCost();
                }
            }

            for (Consumer consumer : input.getConsumerslist()) {
                if (consumer.isBankrupt() && (consumer.getDistributor() != null)) {
                    // scot consumatorii falimentati din listele de clienti ale distribuitorilor
                    consumer.getDistributor().setNumarClienti(consumer.
                            getDistributor().getNumarClienti() - 1);
                    consumer.setDistributor(null);
                }
            }

        }

        Output output = new Output();

        for (Consumer consumer : input.getConsumerslist()) {
            // creez consumatorii cu campurile ce trebuie afisate
            ConsumerOut consumatorOut = new ConsumerOut(consumer.getId(), consumer.isBankrupt(),
                    consumer.getBudget());
            output.getConsumers().add(consumatorOut);
        }

        for (Distributor distributor : input.getDistributorslist()) {
            // creez distribuitorii cu campurile ce trebuie afisate
            DistributorOut distributorOut = new DistributorOut(distributor.getId(),
                    distributor.getEnergyNeededKW(), distributor.getPretcontract(),
                    distributor.getBudget(), distributor.getProducerStrategy(),
                    distributor.isBankrupt());
            for (Consumer consumer : input.getConsumerslist()) {
                // caut consumatorii care au contract cu acest distribuitor
                if (consumer.getDistributor() == distributor) {
                    Contract contract = new Contract(consumer.getId(), consumer.getContractPrice(),
                            consumer.getDistributor().getContractLength()
                                    - consumer.getCurrentContractMonth() - 1);
                    distributorOut.getContracts().add(contract);
                    // construiesc lista de contracte pentru fiecare distribuitor
                }
            }
            distributorOut.getContracts().sort(Contract.getRemainedmonthscomparator());
            output.getDistributors().add(distributorOut);
        }

        for (Producer producer : input.getProducerslist()) {
            // creez producatorii cu campurile ce trebuie afisate
            ProducerOut producerOut = new ProducerOut(producer.getId(),
                    producer.getMaxDistributors(), producer.getPriceKW(), producer.getEnergyType(),
                    producer.getEnergyPerDistributor());
            for (int i = 0; i < input.getNumberOfTurns(); i++) {
                MonthlyStatus monthlyStatus = new MonthlyStatus((long) (i + 1),
                        producer.getDistributors().get(i + 1));
                producerOut.getMonthlyStats().add(monthlyStatus);
            }
            output.getEnergyProducers().add(producerOut);
        }

        output.getEnergyProducers().sort(ProducerOut.IDComparator);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            File fisier = new File(args[1]);
            // scriu in fisier
            writer.writeValue(fisier, output);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
