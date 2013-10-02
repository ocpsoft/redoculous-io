package org.ocpsoft.redoculous.io.rewrite;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.ocpsoft.redoculous.io.view.LoginController;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.HttpOperation;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class LogoutConfiguration extends HttpConfigurationProvider
{

   @Inject
   private LoginController controller;

   @Override
   public Configuration getConfiguration(ServletContext context)
   {
      return ConfigurationBuilder.begin()
               .addRule()
               .when(Direction.isInbound().and(Path.matches("/logout")))
               .perform(new HttpOperation() {
                  @Override
                  public void performHttp(HttpServletRewrite event, EvaluationContext context)
                  {
                     controller.logout();
                  }
               }.and(Redirect.temporary(context.getContextPath() + "/")));
   }

   @Override
   public int priority()
   {
      return Integer.MIN_VALUE;
   }
}
