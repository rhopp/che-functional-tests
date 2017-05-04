package com.redhat.arquillian.che;

import com.redhat.arquillian.che.resource.CheWorkspace;
import java.lang.annotation.Annotation;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class CheWorkspaceProducer implements ResourceProvider {

    @Inject
    private Instance<CheWorkspace> cheWorkspaceInstance;

    @Override
    public boolean canProvide(Class<?> aClass) {
        return aClass.equals(CheWorkspace.class);
    }

    @Override
    public Object lookup(ArquillianResource arquillianResource, Annotation... annotations) {
        return cheWorkspaceInstance.get();
    }
}
