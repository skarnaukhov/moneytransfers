package ru.sk.test.web.server;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class ImmediateFeature implements Feature {

    @Inject
    public ImmediateFeature(ServiceLocator locator) {
        ServiceLocatorUtilities.enableImmediateScope(locator);
    }

    @Override
    public boolean configure(FeatureContext context) {
        return true;
    }
}