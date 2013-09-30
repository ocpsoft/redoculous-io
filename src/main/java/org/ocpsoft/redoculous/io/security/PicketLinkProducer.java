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

import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.User;

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
   @PersistenceContext(unitName = "primary")
   private EntityManager picketLinkEntityManager;

   @Produces
   @PersistenceContext(unitName = "primary")
   private EntityManager defaultEntityManager;

   @Produces
   @RequestScoped
   public User loggedInUser(Identity identity, IdentityManager manager)
   {
      Account account = identity.getAccount();
      if (identity.isLoggedIn())
      {
         User loggedIn = manager.lookupIdentityById(User.class, account.getId());
         return loggedIn;
      }
      return new User();
   }
}
