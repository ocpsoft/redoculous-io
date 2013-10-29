/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.proxy;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.ocpsoft.redoculous.io.model.admin.Settings;
import org.ocpsoft.redoculous.io.view.account.repositories.RepositoryController;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.config.Subset;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Query;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.ocpsoft.rewrite.servlet.util.QueryStringBuilder;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@SuppressWarnings("deprecation")
public class ProxyConfig extends HttpConfigurationProvider
{
   @Inject
   private Settings settings;

   @Inject
   private RepositoryController controller;

   @Override
   public Configuration getConfiguration(ServletContext context)
   {
      return ConfigurationBuilder
               .begin()
               .addRule()
               .when(PathAndQuery.matches("/api/{url}").and(Query.parameterExists("repo")
                        .and(Query.parameterExists("key"))))

               /*
                * Subset used to eliminate database overhead.
                */
               .perform(Subset.evaluate(ConfigurationBuilder.begin()
                        .addRule()
                        .when(new Condition()
                        {
                           @Override
                           public boolean evaluate(Rewrite event, EvaluationContext context)
                           {
                              QueryStringBuilder query = QueryStringBuilder.createNew().addParameters(
                                       ((HttpServletRewrite) event).getAddress().getQuery());
                              String repoUrl = query.getParameter("repo");
                              String key = query.getParameter("key");
                              return controller.isRepositoryApiKey(repoUrl, key);
                           }
                        })
                        .perform(new Operation()
                        {
                           @Override
                           public void perform(Rewrite event, EvaluationContext context)
                           {
                              String contextPath = ((HttpServletRewrite) event).getServletContext().getContextPath();
                              String url = ((HttpServletRewrite) event).getAddress().getPathAndQuery()
                                       .replaceFirst(contextPath + "/api", "");
                              Proxy.to(settings.getRedoculousURL()
                                       + url + "&nogzip").perform(event, context);
                           }
                        })));
   }

   @Override
   public int priority()
   {
      return 0;
   }

}
