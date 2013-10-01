/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.rewrite.elements;

import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.picketlink.Identity;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class AuthenticationStatus extends HttpCondition
{
   private Identity identity;

   private AuthenticationStatus(Identity identity)
   {
      this.identity = identity;
   }

   public static HttpCondition isLoggedIn(Identity identity)
   {
      return new AuthenticationStatus(identity);
   }

   @Override
   public boolean evaluateHttp(HttpServletRewrite event, EvaluationContext context)
   {
      return identity.isLoggedIn();
   }
}