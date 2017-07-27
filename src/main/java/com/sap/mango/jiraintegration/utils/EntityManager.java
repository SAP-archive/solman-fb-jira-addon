package com.sap.mango.jiraintegration.utils;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.tx.Transactional;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import net.java.ao.Entity;
import net.java.ao.EntityStreamCallback;
import net.java.ao.Query;
import net.java.ao.RawEntity;

import java.util.*;

/**
 * Utility class, that executes CRUD operations with ActiveObjects
 */
@Transactional
public class EntityManager {

    public static <A extends Entity> Integer saveEntity(ActiveObjects ao, final A entity) {
        entity.save();
        return entity.getID();
    }

    public static <A extends Entity> Boolean saveEntities(ActiveObjects ao, final List<A> entities) {
        for (A entity : entities) {
            entity.save();
        }
        return true;
    }

    public static <A extends Entity> A saveUpdateEntity(final ActiveObjects ao, final A entity, final Function0<Query> createQuery) {
        final Class entityClass = entity.getEntityType();

        Query query = createQuery.apply();

        final RawEntity[] oldEntities = ao.find(entityClass, query);
        if (oldEntities.length == 0) {
            entity.save();
            return entity;
        }
        RawEntity oldEntity = oldEntities[0];
        ao.delete(oldEntity);
        oldEntity = entity;
        oldEntity.save();
        return (A) oldEntity;
    }

    public static <A extends Entity> void deleteEntity(final ActiveObjects ao, final A entity) {
        ao.delete(entity);
    }

    public static <A extends Entity> Boolean deleteEntity(final ActiveObjects ao, final Class resultClass, final Function0<Query> createQuery) {
        final Query query = createQuery.apply();

        final RawEntity[] toDelete = ao.find(resultClass, query);
        if (toDelete.length == 0) {
            return false;
        } else {
            ao.delete(toDelete);
            return true;
        }
    }

    public static <A extends Entity> A[] getEntities(final ActiveObjects ao, final Class<A> resultClass) {
        return ao.find(resultClass);
    }

    public static <A extends Entity> A[] getEntitiesAsArray(final ActiveObjects ao, final Class<A> resultClass,  final Function0<Query> createQuery) {
        return ao.find(resultClass, createQuery.apply());
    }

    public static <A extends Entity> List<A> getEntities(final ActiveObjects ao, final Class<A> resultClass, final Function0<Query> createQuery) {
        final Query query = createQuery.apply();
        final List<A> result = new ArrayList<>();
        ao.stream(resultClass, query, new EntityStreamCallback<A, Integer>() {
            @Override
            public void onRowRead(A a) {
                result.add(a);
            }
        });
        return result;
    }

    public static <A extends Entity> A getReadOnlyEntity(final ActiveObjects ao, final Class resultClass, final Function0<Query> createQuery) {
        final Query query = createQuery.apply();
        final List<A> result = new ArrayList<>();
        ao.stream(resultClass, query, new EntityStreamCallback<A, Integer>() {
            @Override
            public void onRowRead(A a) {
                result.add(a);
            }
        });
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public static <A extends Entity> Map<Integer, A> getEntitiesAsMap(final ActiveObjects ao, final Class<A> resultClass, final Function0<Query> createQuery) {
        final Query query = createQuery.apply();
        final Map<Integer, A> result = new HashMap<>();
        A [] res = ao.find(resultClass, createQuery.apply());
        for (A a : res) {
            result.put(a.getID(), a);
        }
        return result;
    }

    public static <A extends Entity> A getEntity(final ActiveObjects ao, final Class resultClass, final Function0<Query> createQuery) {
        final Query query = createQuery.apply();

        final RawEntity[] result = ao.find(resultClass, query);
        if (result.length == 0) {
            return null;
        } else {
            return (A)result[0];
        }
    }
}
