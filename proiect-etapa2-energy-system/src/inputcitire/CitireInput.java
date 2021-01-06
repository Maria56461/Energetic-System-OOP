package inputcitire;
import changes.DistributorChange;
import changes.ProducerChange;
import entities.Consumer;
import entities.Distributor;
import entities.MonthlyUpdate;
import entities.Producer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class CitireInput {

    private final String inputPath;
    //calea spre fisierul de intrare

    public CitireInput(final String inputPath) {
        this.inputPath = inputPath;
    }

    public Input readData() {

        JSONParser jsonParser = new JSONParser();

        Long numberOfTurns = 0L;
        List<Consumer> consumers = new ArrayList<>();
        List<Distributor> distributors = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();
        List<MonthlyUpdate> monthlyUpdates = new ArrayList<>();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));
            numberOfTurns = (Long) jsonObject.get("numberOfTurns");
            JSONObject initialData = (JSONObject) jsonObject.get("initialData");
            JSONArray jsonConsumers = (JSONArray) initialData.get("consumers");
            JSONArray jsonDistributors = (JSONArray) initialData.get("distributors");
            JSONArray jsonProducers = (JSONArray) initialData.get("producers");
            JSONArray jsonMonthlyUpdates = (JSONArray) jsonObject.get("monthlyUpdates");

            if (jsonConsumers != null) {
                for (Object jsonConsumer : jsonConsumers) {
                    Consumer newConsumer = (Consumer) ObjectFactory.createObject(
                                    ObjectFactory.ObjectType.CONSUMER);
                    newConsumer.setId((Long) ((JSONObject) jsonConsumer).get("id"));
                    newConsumer.setBudget((Long) ((JSONObject) jsonConsumer).get("initialBudget"));
                    newConsumer.setMonthlyIncome((Long)
                            ((JSONObject) jsonConsumer).get("monthlyIncome"));
                    consumers.add(newConsumer);
                }
            } else {
                System.out.println("NU EXISTA CONSUMATORI");
            }

            if (jsonDistributors != null) {
                for (Object jsonDistributor : jsonDistributors) {
                    Distributor newDistributor = (Distributor) ObjectFactory.createObject(
                            ObjectFactory.ObjectType.DISTRIBUTOR);
                    newDistributor.setId((Long) ((JSONObject) jsonDistributor).get("id"));
                    newDistributor.setContractLength((Long) ((JSONObject)
                            jsonDistributor).get("contractLength"));
                    newDistributor.setBudget((Long) ((JSONObject)
                            jsonDistributor).get("initialBudget"));
                    newDistributor.setInfrastructureCost((Long) ((JSONObject)
                            jsonDistributor).get("initialInfrastructureCost"));
                    newDistributor.setEnergyNeededKW((Long) ((JSONObject)
                            jsonDistributor).get("energyNeededKW"));
                    newDistributor.setProducerStrategy((String) ((JSONObject)
                            jsonDistributor).get("producerStrategy"));
                    distributors.add(newDistributor);
                }
            } else {
                System.out.println("NU EXISTA DISTRIBUITORI");
            }

            if (jsonProducers != null) {
                for (Object jsonProducer : jsonProducers) {
                    Producer newProducer = (Producer) ObjectFactory.createObject(
                            ObjectFactory.ObjectType.PRODUCER);
                    newProducer.setId((Long) ((JSONObject) jsonProducer).get("id"));
                    newProducer.setEnergyType((String) ((JSONObject)
                            jsonProducer).get("energyType"));
                    newProducer.setMaxDistributors((Long) ((JSONObject)
                            jsonProducer).get("maxDistributors"));
                    newProducer.setPriceKW((Double) ((JSONObject)
                            jsonProducer).get("priceKW"));
                    newProducer.setEnergyPerDistributor((Long) ((JSONObject)
                            jsonProducer).get("energyPerDistributor"));
                    producers.add(newProducer);
                }
            } else {
                System.out.println("NU EXISTA PRODUCATORI");
            }

            if (jsonMonthlyUpdates != null) {
                for (Object jsonIterator : jsonMonthlyUpdates) {

                    List<Consumer> newConsumers = new ArrayList<>();

                    if (((JSONObject) jsonIterator).get("newConsumers") != null) {
                        for (Object iterator : (JSONArray) ((JSONObject) jsonIterator)
                                .get("newConsumers")) {
                            newConsumers.add(new Consumer(
                                    (Long) ((JSONObject) iterator).get("id"),
                                    (Long) ((JSONObject) iterator).get("initialBudget"),
                                    (Long) ((JSONObject) iterator).get("monthlyIncome")
                            ));
                        }
                    } else {
                        newConsumers = null;
                    }

                    List<DistributorChange> distributorChanges = new ArrayList<>();

                    if (((JSONObject) jsonIterator).get("distributorChanges") != null) {
                        for (Object iterator : (JSONArray) ((JSONObject) jsonIterator)
                                .get("distributorChanges")) {
                            distributorChanges.add(new DistributorChange(
                                    (Long) ((JSONObject) iterator).get("id"),
                                    (Long) ((JSONObject) iterator).get("infrastructureCost")
                            ));
                        }
                    } else {
                        distributorChanges = null;
                    }

                    List<ProducerChange> producerChanges = new ArrayList<>();
                    if (((JSONObject) jsonIterator).get("producerChanges") != null) {
                        for (Object iterator : (JSONArray) ((JSONObject) jsonIterator)
                                .get("producerChanges")) {
                            producerChanges.add(new ProducerChange(
                                    (Long) ((JSONObject) iterator).get("id"),
                                    (Long) ((JSONObject) iterator).get("energyPerDistributor")
                            ));
                        }
                    } else {
                        producerChanges = null;
                    }

                    MonthlyUpdate newMonthlyUpdate = (MonthlyUpdate) ObjectFactory.createObject(
                            ObjectFactory.ObjectType.MONTHLYUPDATE);
                    newMonthlyUpdate.setNewConsumers(newConsumers);
                    newMonthlyUpdate.setDistributorChanges(distributorChanges);
                    newMonthlyUpdate.setProducerChanges(producerChanges);
                    monthlyUpdates.add(newMonthlyUpdate);
                }
            } else {
                System.out.println("NU EXISTA SCHIMBARI IN ACEASTA LUNA");
            }

            if (jsonConsumers == null) {
                consumers = null;
            }

            if (jsonDistributors == null) {
                distributors = null;
            }

            if (jsonProducers == null) {
                producers = null;
            }

            if (jsonMonthlyUpdates == null) {
                monthlyUpdates = null;
            }


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return new Input(numberOfTurns, consumers, distributors, producers, monthlyUpdates);
    }
}
