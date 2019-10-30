package com.chronos.erp.util.jpa;

import com.chronos.erp.repository.Filtro;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public abstract class FiltroCriteriaJPA<T> {

    private Filtro filtro;
    private Class<? extends Object> tipo;

    public FiltroCriteriaJPA(Filtro filtro) {
        this.filtro = filtro;
    }

    public Predicate getPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        tipo = getJavaType(root, filtro.getAtributo());

        if (filtro.getOperadorRelacional().equalsIgnoreCase(">=")) {
            return maiorOuIgual(root, criteriaBuilder);
        } else if (filtro.getOperadorRelacional().equalsIgnoreCase("=<")) {
            return menorOuIgual(root, criteriaBuilder);
        } else if (filtro.getOperadorRelacional().equalsIgnoreCase("=")) {
            return igual(root, criteriaBuilder);
        } else if (filtro.getOperadorRelacional().equalsIgnoreCase("<>")) {
            return diferente(root, criteriaBuilder);
        }

        return null;


    }

    private Class<? extends Object> getJavaType(Root<T> root, String key) {
        return root.get(key).getJavaType();
    }

    private Predicate igual(Root<T> root, CriteriaBuilder builder) {
        return tipo == String.class ? contem(root, builder) : builder.equal(getProperty(root), getValueObject());
    }

    private Predicate diferente(Root<T> root, CriteriaBuilder builder) {
        return tipo == String.class ? naoContem(root, builder) : builder.notEqual(getProperty(root), getValueObject());
    }

    private Predicate contem(Root<T> root, CriteriaBuilder builder) {
        return builder.like(getProperty(root), getValue());
    }

    private Predicate naoContem(Root<T> root, CriteriaBuilder builder) {
        return builder.notLike(getProperty(root), getValue());
    }

    private Predicate menorOuIgual(Root<T> root, CriteriaBuilder builder) {
        return tipo == Date.class
                ? builder.lessThanOrEqualTo(root.<Date>get(filtro.getAtributo()), getValueDate())
                : builder.lessThanOrEqualTo(getProperty(root), getValue());
    }

    private Predicate maiorOuIgual(Root<T> root, CriteriaBuilder builder) {
        return tipo == Date.class
                ? builder.greaterThanOrEqualTo(root.<Date>get(filtro.getAtributo()), getValueDate())
                : builder.greaterThanOrEqualTo(getProperty(root), getValue());
    }

    private Path<String> getProperty(Root<T> root) {
        return root.get(filtro.getAtributo());
    }


    private String getValue() {
        return tipo == String.class ? "%" + filtro.getValor().toString() + "%" : filtro.getValor().toString();
    }

    private Date getValueDate() {
        LocalDate localDate = LocalDate.parse(getValue());
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    private Object getValueObject() {
        if (tipo == Date.class) {
            return getValueDate();
        } else if (tipo == Boolean.class) {
            return Boolean.parseBoolean(getValue());
        }
        return getValue();
    }
}
