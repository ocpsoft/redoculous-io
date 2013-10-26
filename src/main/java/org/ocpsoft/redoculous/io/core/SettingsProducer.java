/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.core;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ocpsoft.redoculous.io.model.admin.Settings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Stateless
public class SettingsProducer
{
   @PersistenceContext
   private EntityManager em;

   @Produces
   @Named("settings")
   @RequestScoped
   public Settings produceSettings()
   {
      return em.createQuery("FROM Settings ORDER BY id DESC", Settings.class).setMaxResults(1).getSingleResult();
   }
}
