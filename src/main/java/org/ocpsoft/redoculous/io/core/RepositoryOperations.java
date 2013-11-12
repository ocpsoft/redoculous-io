/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.core;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ProxyFactory;
import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.admin.Settings;
import org.ocpsoft.redoculous.rest.ManagementService;
import org.ocpsoft.redoculous.rest.model.RepositoryStatus;
import org.ocpsoft.redoculous.rest.model.RepositoryStatus.State;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Stateless
public class RepositoryOperations
{
   @Inject
   private Settings settings;

   @Asynchronous
   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public Future<Response> initializeRepository(UserProfile profile, String url)
   {
      ManagementService client = ProxyFactory.create(ManagementService.class, settings.getRedoculousURL());
      RepositoryStatus status = getStatus(profile, url);
      if (State.MISSING.equals(status.getState()) || State.ERROR.equals(status.getState()))
      {
         // TODO How can I make repositories unique per user, so that users can't blow away each other's repos?
         return new AsyncResult<Response>(client.init(profile.getUsername(), url));
      }
      return new AsyncResult<Response>(Response.ok().build());
   }

   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public RepositoryStatus getStatus(UserProfile profile, String url)
   {
      ManagementService client = ProxyFactory.create(ManagementService.class, settings.getRedoculousURL());
      return client.status(profile.getUsername(), url);
   }

   @Asynchronous
   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public void purgeRepository(UserProfile profile, String url)
   {
      ManagementService client = ProxyFactory.create(ManagementService.class, settings.getRedoculousURL());
      client.purgeRepository(profile.getUsername(), url);
   }
}
