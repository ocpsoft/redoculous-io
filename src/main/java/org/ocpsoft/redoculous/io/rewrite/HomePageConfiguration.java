package org.ocpsoft.redoculous.io.rewrite;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Not;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.picketlink.Identity;

public class HomePageConfiguration extends HttpConfigurationProvider
{
   @Inject
   private Identity identity;

   private Condition loggedIn = new Condition()
   {
      @Override
      public boolean evaluate(Rewrite event, EvaluationContext context)
      {
         return identity.isLoggedIn();
      }
   };

   @Override
   public Configuration getConfiguration(final ServletContext context)
   {

      return ConfigurationBuilder.begin()

               .addRule(Join.path("/").to("/faces/home.xhtml"))
               .when(Not.any(loggedIn))

               /*
                * The first rule takes precedence for inbound rewriting, but both will activate on outbound rewrites.
                */
               .addRule(Join.path("/").to("/faces/account/dashboard.xhtml"))
               .when(loggedIn)
               .addRule(Join.path("/").to("/faces/home.xhtml"))
               .when(loggedIn)

               .addRule()
               .when(Direction.isInbound()
                        .and(Path.matches("/login")
                                 .or(Path.matches("/signup")))
                        .and(loggedIn))
               .perform(Redirect.temporary(context.getContextPath() + "/"));
   }

   @Override
   public int priority()
   {
      return 0;
   }
}