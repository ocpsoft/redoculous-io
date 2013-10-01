package org.ocpsoft.redoculous.io.rewrite;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.ocpsoft.redoculous.io.rewrite.elements.AuthenticationStatus;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Not;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.picketlink.Identity;

public class HomePageConfiguration extends HttpConfigurationProvider
{
   @Inject
   private Identity identity;

   @Override
   public Configuration getConfiguration(final ServletContext context)
   {

      return ConfigurationBuilder.begin()

               .addRule(Join.path("/").to("/faces/home.xhtml"))
               .when(Not.any(AuthenticationStatus.isLoggedIn(identity)))

               /*
                * The first rule takes precedence for inbound rewriting, but both will activate on outbound rewrites.
                */
               .addRule(Join.path("/").to("/faces/account/dashboard.xhtml"))
               .when(AuthenticationStatus.isLoggedIn(identity))
               .addRule(Join.path("/").to("/faces/home.xhtml"))
               .when(AuthenticationStatus.isLoggedIn(identity))

               .addRule()
               .when(Direction.isInbound()
                        .and(Path.matches("/login").or(Path.matches("/signup")))
                        .and(AuthenticationStatus.isLoggedIn(identity)))
               .perform(Redirect.temporary(context.getContextPath() + "/"));
   }

   @Override
   public int priority()
   {
      return 0;
   }
}