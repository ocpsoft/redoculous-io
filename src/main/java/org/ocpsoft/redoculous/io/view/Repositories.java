/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ocpsoft.redoculous.io.model.Repository;
import org.picketlink.idm.model.basic.User;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named
public class Repositories
{
   private List<Repository> list;

   @Inject
   private User loggedIn;

   @Inject
   private EntityManager em;

   public List<Repository> getList()
   {
      if (list == null)
      {
         TypedQuery<Repository> query = em.createQuery("from Repository", Repository.class);
         list = query.getResultList();
      }
      return list;
   }

   public void setList(List<Repository> list)
   {
      this.list = list;
   }
}
