package com.sap.mango.jiraintegration.solman.entities.proxysettings;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ProxySettingsDAO {

    private ActiveObjects ao;

    public ProxySettingsDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveUpdateProxySettings(final SolmanParamsAO solmanParams, final String proxyHost, final Integer proxyPort) {
        ProxySettings proxySettings = ao.create(ProxySettings.class, new DBParam("PROXY_HOST", "-"), new DBParam("PORT", -1));
        proxySettings.setSolmanParams(solmanParams);
        proxySettings.setProxyHost(proxyHost);
        proxySettings.setPort(proxyPort);
        return EntityManager.saveUpdateEntity(ao, proxySettings, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProxySettings.class, "PS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solmanParams.getCustomerGiud());
            }
        }).getID();
    }

    public ProxySettings getProxySettings(final String solManCustGuid) {
        return EntityManager.getEntity(ao, ProxySettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProxySettings.class, "PS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public boolean deleteProxySettings(final Integer id) {
        return EntityManager.deleteEntity(ao, ProxySettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProxySettings.class, "PS")
                        .where("PS.ID = ?", id);
            }
        });
    }

    public List<ProxySettings> getListProxySettings() {
        return Arrays.asList(EntityManager.getEntities(ao, ProxySettings.class));
    }

    public Boolean deleteProxySettings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, ProxySettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProxySettings.class, "PS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

}
