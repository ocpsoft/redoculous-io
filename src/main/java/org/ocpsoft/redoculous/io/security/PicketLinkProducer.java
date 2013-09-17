/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.picketlink.annotations.PicketLink;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class PicketLinkProducer
{
   @Produces
   @RequestScoped
   public FacesContext produceFacesContext()
   {
      return FacesContext.getCurrentInstance();
   }

   @Produces
   @PicketLink
   @PersistenceContext(unitName = "picketlink-default")
   private EntityManager picketLinkEntityManager;
}
