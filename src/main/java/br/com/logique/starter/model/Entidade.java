package br.com.logique.starter.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public abstract class Entidade implements Serializable {

    public abstract Long getId();

    public abstract void setId(Long id);

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(getId()).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Entidade rhs = (Entidade) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(getId(), rhs.getId())
                .isEquals();
    }
}
