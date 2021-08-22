/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.v3.admin;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.glassfish.api.admin.ParameterMap;
import java.util.Properties;


/**
 * junit test to test AdminAdapter class
 */
public class AdminAdapterTest {
    private AdminAdapter aa = null;

    @Test
    public void extractParametersTest() {
        ParameterMap props = aa.extractParameters("uniquetablenames=false&createtables=true&target=server&libraries=foo.jar&dbvendorname=test&deploymentplan=test");
        Properties correctProps = new Properties();
        correctProps.put("uniquetablenames", "false");
        correctProps.put("createtables", "true");
        correctProps.put("target", "server");
        correctProps.put("libraries", "foo.jar");
        correctProps.put("dbvendorname", "test");
        correctProps.put("deploymentplan", "test");
        for (String prop : correctProps.stringPropertyNames()) {
            assertEquals("compare Properties",
                correctProps.getProperty(prop), props.getOne(prop));
        }
    }

    @Before
    public void setup() {
        aa = new PublicAdminAdapter();
    }
}
