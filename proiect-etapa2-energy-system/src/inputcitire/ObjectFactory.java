package inputcitire;

import entities.Consumer;
import entities.Distributor;
import entities.MonthlyUpdate;
import entities.Producer;

public class ObjectFactory {

    public enum ObjectType {
        CONSUMER, DISTRIBUTOR, PRODUCER, MONTHLYUPDATE
    }

    public static Object createObject(ObjectType objectType) {
        return switch (objectType) {
            case CONSUMER -> new Consumer();
            case PRODUCER -> new Producer();
            case DISTRIBUTOR -> new Distributor();
            case MONTHLYUPDATE -> new MonthlyUpdate();
        };
    }
}
