package ru.sk.test.mt.data.entity.common;

import org.apache.log4j.Logger;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID> {

    private static final Logger LOGGER = Logger.getLogger(AbstractEntity.class);
    private static final long serialVersionUID = -6829139895354409353L;

    private ID id;

    protected AbstractEntity() {
        // JPA
    }

    public AbstractEntity(ID id) {
        setId(id);
    }

    @Transient
    public ID getId() {
        return this.id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        if (!this.getClass().isAssignableFrom(o.getClass())) {
            LOGGER.warn("AbstractEntity class comparing failed: " + this.getClass() + " vs " + o.getClass());
            return false;
        }

        final AbstractEntity that = (AbstractEntity) o;

        if (getId() == null)
            return super.equals(o);

        if (!getId().equals(that.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }
        return getId().hashCode();
    }
}
