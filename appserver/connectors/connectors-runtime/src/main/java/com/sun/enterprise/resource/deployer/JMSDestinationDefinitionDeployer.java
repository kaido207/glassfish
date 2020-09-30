/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.resource.deployer;

import com.sun.appserv.connectors.internal.api.ConnectorConstants;
import com.sun.appserv.connectors.internal.api.ConnectorsUtil;
import com.sun.enterprise.config.serverbeans.Resource;
import com.sun.enterprise.config.serverbeans.Resources;
import com.sun.enterprise.deployment.JMSDestinationDefinitionDescriptor;
import com.sun.logging.LogDomains;
import org.glassfish.connectors.config.AdminObjectResource;
import org.glassfish.resourcebase.resources.api.ResourceConflictException;
import org.glassfish.resourcebase.resources.api.ResourceDeployer;
import org.glassfish.resourcebase.resources.api.ResourceDeployerInfo;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.TransactionFailure;
import org.jvnet.hk2.config.types.Property;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@ResourceDeployerInfo(JMSDestinationDefinitionDescriptor.class)
public class JMSDestinationDefinitionDeployer implements ResourceDeployer {

    @Inject
    private Provider<org.glassfish.resourcebase.resources.util.ResourceManagerFactory> resourceManagerFactoryProvider;

    private static Logger _logger = LogDomains.getLogger(JMSDestinationDefinitionDeployer.class, LogDomains.RSR_LOGGER);
    final static String PROPERTY_PREFIX = "org.glassfish.jms-destination.";

    public void deployResource(Object resource, String applicationName, String moduleName) throws Exception {
        //TODO ASR
    }

    public void deployResource(Object resource) throws Exception {

        final JMSDestinationDefinitionDescriptor desc = (JMSDestinationDefinitionDescriptor) resource;
        String resourceName = ConnectorsUtil.deriveResourceName(desc.getResourceId(), desc.getName(), desc.getResourceType());

        if(_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "JMSDestinationDefinitionDeployer.deployResource() : resource-name [" + resourceName + "]");
        }

        //deploy resource
        MyJMSDestinationResource jmsDestinationResource = new MyJMSDestinationResource(desc, resourceName);
        getDeployer(jmsDestinationResource).deployResource(jmsDestinationResource);

    }

    /**
     * {@inheritDoc}
     */
    public boolean canDeploy(boolean postApplicationDeployment, Collection<Resource> allResources, Resource resource) {
        if (handles(resource)) {
            if (!postApplicationDeployment) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void validatePreservedResource(com.sun.enterprise.config.serverbeans.Application oldApp,
                                          com.sun.enterprise.config.serverbeans.Application newApp,
                                          Resource resource,
                                          Resources allResources)
    throws ResourceConflictException {
        //do nothing.
    }


    private ResourceDeployer getDeployer(Object resource) {
        return resourceManagerFactoryProvider.get().getResourceDeployer(resource);
    }

    private JMSDestinationProperty convertProperty(String name, String value) {
        return new JMSDestinationProperty(name, value);
    }

    public void undeployResource(Object resource, String applicationName, String moduleName) throws Exception {
        //TODO ASR
    }

    public void undeployResource(Object resource) throws Exception {

        final JMSDestinationDefinitionDescriptor desc = (JMSDestinationDefinitionDescriptor)resource;

        String resourceName = ConnectorsUtil.deriveResourceName(desc.getResourceId(), desc.getName(),desc.getResourceType());

        if(_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "JMSDestinationDefinitionDeployer.undeployResource() : resource-name [" + resourceName + "]");
        }

        //undeploy resource
        MyJMSDestinationResource jmsDestinationResource = new MyJMSDestinationResource(desc, resourceName);
        getDeployer(jmsDestinationResource).undeployResource(jmsDestinationResource);

    }

    public void redeployResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("redeploy() not supported for jms-destination-definition type");
    }

    public void enableResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("enable() not supported for jms-destination-definition type");
    }

    public void disableResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("disable() not supported for jms-destination-definition type");
    }

    public boolean handles(Object resource) {
        return resource instanceof JMSDestinationDefinitionDescriptor;
    }

    /**
     * @inheritDoc
     */
    public boolean supportsDynamicReconfiguration() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("rawtypes")
    public Class[] getProxyClassesForDynamicReconfiguration() {
        return new Class[0];
    }

    abstract class FakeConfigBean implements ConfigBeanProxy {

        public ConfigBeanProxy deepCopy(ConfigBeanProxy parent) {
            throw new UnsupportedOperationException();
        }

        public ConfigBeanProxy getParent() {
            return null;
        }

        public <T extends ConfigBeanProxy> T getParent(Class<T> tClass) {
            return null;
        }

        public <T extends ConfigBeanProxy> T createChild(Class<T> tClass) throws TransactionFailure {
            return null;
        }
    }

    class JMSDestinationProperty extends FakeConfigBean implements Property {

        private String name;
        private String value;
        private String description;

        JMSDestinationProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) throws PropertyVetoException {
            this.name = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) throws PropertyVetoException {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String value) throws PropertyVetoException {
            this.description = value;
        }

        public void injectedInto(Object o) {
            //do nothing
        }
    }

    class MyJMSDestinationResource extends FakeConfigBean implements AdminObjectResource {

        private JMSDestinationDefinitionDescriptor desc;
        private String name;

        public MyJMSDestinationResource(JMSDestinationDefinitionDescriptor desc, String name) {
            this.desc = desc;
            this.name = name;
        }

        @Override
        public String getObjectType() {
            return "user";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setObjectType(String value) throws PropertyVetoException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getIdentity() {
            return name;
        }

        public String getResAdapter() {
            String resourceAdapter = desc.getResourceAdapter();
            if (resourceAdapter != null && !resourceAdapter.equals("")) {
                return resourceAdapter;
            }
            return ConnectorConstants.DEFAULT_JMS_ADAPTER;
        }

        public void setResAdapter(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getDescription() {
            return desc.getDescription();
        }

        public void setDescription(String value) throws PropertyVetoException {
            //do nothing
        }

        @Override
        public String getJndiName() {
            return name;
        }

        @Override
        public void setJndiName(String value) throws PropertyVetoException {
            //do nothing
        }

        @Override
        public String getResType() {
            return desc.getInterfaceName();
        }

        @Override
        public void setResType(String value) throws PropertyVetoException {
            //do nothing
        }

        @Override
        public String getClassName() {
            return desc.getClassName();
        }

        @Override
        public void setClassName(String value) throws PropertyVetoException {
            //do nothing
        }

        @Override
        public String getEnabled() {
            return "true";
        }

        @Override
        public void setEnabled(String value) throws PropertyVetoException {
            //do nothing
        }

        public List<Property> getProperty() {
            String destinationName = desc.getDestinationName();
            boolean destinationNameSet = false;
            List<Property> jmsDestinationProperties = new ArrayList<Property>();
            if (destinationName != null && !destinationName.equals("")) {
                JMSDestinationProperty dp = convertProperty("Name", destinationName);
                jmsDestinationProperties.add(dp);
                destinationNameSet = true;
            }

            Properties p = desc.getProperties();
            for (Entry<Object, Object> entry : p.entrySet()) {
                String key = (String) entry.getKey();
                if (key.startsWith(PROPERTY_PREFIX) || key.equalsIgnoreCase("Name") && destinationNameSet) {
                    continue;
                }
                String value = (String) entry.getValue();
                JMSDestinationProperty dp = convertProperty(key, value);
                jmsDestinationProperties.add(dp);
            }

            return jmsDestinationProperties;
        }

        public Property getProperty(String name) {
            String value = desc.getProperty(name);
            return new JMSDestinationProperty(name, value);
        }

        public String getPropertyValue(String name) {
            return desc.getProperty(name);
        }

        public String getPropertyValue(String name, String defaultValue) {
            String value = desc.getProperty(name);
            if (value != null) {
                return value;
            }
            return defaultValue;
        }

        public void injectedInto(Object o) {
            //do nothing
        }

        public String getDeploymentOrder() {
            return null;
        }

        public void setDeploymentOrder(String value) {
            //do nothing
        }

        @Override
        public Property addProperty(Property prprt) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Property lookupProperty(String string) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Property removeProperty(String string) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Property removeProperty(Property prprt) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}