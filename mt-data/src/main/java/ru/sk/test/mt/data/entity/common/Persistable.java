package ru.sk.test.mt.data.entity.common;

import java.io.Serializable;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public interface Persistable <ID extends Serializable> extends Serializable {

    ID getId();

}
