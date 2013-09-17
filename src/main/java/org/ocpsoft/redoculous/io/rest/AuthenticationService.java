/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.basic.User;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class AuthenticationService
{
   @Inject
   private Identity identity;

   @Inject
   private DefaultLoginCredentials credentials;

   @POST
   @Produces(MediaType.APPLICATION_JSON)
   public Response login(DefaultLoginCredentials credential)
   {
      if (!this.identity.isLoggedIn())
      {
         this.credentials.setUserId(credential.getUserId());
         this.credentials.setPassword(credential.getPassword());
         this.identity.login();
      }

      User user = (User) this.identity.getAccount();

      return Response.ok().entity(user).type(MediaType.APPLICATION_JSON_TYPE).build();
   }

   @POST
   public void logout()
   {
      if (this.identity.isLoggedIn())
      {
         this.identity.logout();
      }
   }
}
