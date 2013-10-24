/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.util.jsf;

import javax.faces.context.FacesContext;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FacesUtils
{
   /**
    * Perform an internal Faces-navigation to the 404 page.
    */
   public static void send404()
   {
      FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
               .handleNavigation(FacesContext.getCurrentInstance(), "", "/404");
   }
}
