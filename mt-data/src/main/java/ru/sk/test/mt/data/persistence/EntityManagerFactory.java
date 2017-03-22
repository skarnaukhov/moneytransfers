package ru.sk.test.mt.data.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class EntityManagerFactory {

    public static final String MT_KEY = "MONEY_TRANSFER";

    private Map<String, EntityManager> entityManagerMap = new HashMap<>();

    public synchronized EntityManager getEntityManager(String key) {
        if (!entityManagerMap.containsKey(key)) {
            entityManagerMap.put(key, Persistence.createEntityManagerFactory(key).createEntityManager());
        }
        return entityManagerMap.get(key);
    }

}
