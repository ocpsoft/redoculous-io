package org.ocpsoft.redoculous.io.rewrite;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.ocpsoft.redoculous.io.rewrite.elements.AuthenticationStatus;
import org.ocpsoft.redoculous.io.rewrite.elements.ValueBinding;
import org.ocpsoft.redoculous.io.security.AuthorizationChecker;
import org.ocpsoft.redoculous.io.view.LoginController;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Not;
import org.ocpsoft.rewrite.config.Subset;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.Response;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.picketlink.Identity;

public class SecurityConfiguration extends HttpConfigurationProvider
{
   @Inject
   private Identity identity;

   @Inject
   private AuthorizationChecker auth;

   @Override
   public Configuration getConfiguration(final ServletContext context)
   {

      return ConfigurationBuilder
               .begin()

               /*
                * Administrative features (Subset used for performance.)
                */
               .addRule()
               .when(Path.matches("/admin/{*}"))
               .perform(Subset.evaluate(ConfigurationBuilder.begin()

                        .addRule()
                        .when(Not.any(administrator).andNot(AuthenticationStatus.isLoggedIn(identity)))
                        .perform(Redirect.temporary(context.getContextPath() + "/login?" +
                                 LoginController.RETURN_TO_PARAM + "={url}")
                        )
                        .where("url").bindsTo(requestedPath)

                        .addRule()
                        .when(Not.any(administrator))
                        .perform(Response.setStatus(401).and(Forward.to("/401"))))
               )

               /*
                * User account page restriction and redirect to login page
                */
               .addRule()
               .when(Path.matches("/account/{*}")
                        .andNot(AuthenticationStatus.isLoggedIn(identity))
               )
               .perform(Redirect.temporary(context.getContextPath() + "/login?" +
                        LoginController.RETURN_TO_PARAM + "={url}")
               )
               .where("url").bindsTo(requestedPath);
   }

   Condition administrator = new Condition() {
      @Override
      public boolean evaluate(Rewrite event, EvaluationContext context)
      {
         return auth.isMember("administrators");
      }
   };

   ValueBinding requestedPath = new ValueBinding() {
      @Override
      public Object retrieve(Rewrite event, EvaluationContext context)
      {
         HttpServletRewrite rewrite = (HttpServletRewrite) event;
         String url = rewrite.getAddress().getPathAndQuery();
         if (url.startsWith(rewrite.getContextPath()))
            url = url.substring(rewrite.getContextPath().length());
         return url;
      }
   };

   @Override
   public int priority()
   {
      return Integer.MIN_VALUE + 1;
   }
}