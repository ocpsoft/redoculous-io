/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.proxy;

import java.util.Set;

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
import org.ocpsoft.rewrite.param.ParameterStore;
import org.ocpsoft.rewrite.param.Parameterized;
import org.ocpsoft.rewrite.param.RegexParameterizedPatternParser;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Query;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
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
                              String repo = Parameters.retrieve(context, "repo");
                              String key = Parameters.retrieve(context, "key");
                              return controller.isRepositoryApiKey(repo, key);
                           }
                        })
                        .perform(new ProxyOperation(settings, "/{url}&nogzip&nogzip")))
               );
   }

   public class ProxyOperation implements Operation, Parameterized
   {
      private final Settings settings;
      private final String toSuffix;
      private ParameterStore store;

      public ProxyOperation(Settings settings, String toSuffix)
      {
         this.settings = settings;
         this.toSuffix = toSuffix;
      }

      @Override
      public void perform(Rewrite event, EvaluationContext context)
      {
         Proxy proxy = Proxy.to(settings.getRedoculousURL() + toSuffix);
         proxy.setParameterStore(store);
         proxy.perform(event, context);
      }

      @Override
      public Set<String> getRequiredParameterNames()
      {
         return new RegexParameterizedPatternParser(toSuffix).getRequiredParameterNames();
      }

      @Override
      public void setParameterStore(ParameterStore store)
      {
         this.store = store;
      }
   }

   @Override
   public int priority()
   {
      return 0;
   }

}
