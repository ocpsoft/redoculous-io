package org.ocpsoft.redoculous.io.rewrite;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.OperationBuilder;
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

   @Override
   public Configuration getConfiguration(ServletContext context)
   {
      return ConfigurationBuilder.begin()
               .addRule()
               .when(Direction.isInbound().and(Path.matches("/logout")))
               .perform(Session.logout().and(Redirect.temporary(context.getContextPath() + "/")));
   }

   @Override
   public int priority()
   {
      return Integer.MIN_VALUE;
   }

   private static final class Session extends HttpOperation
   {
      // TODO Move to Rewrite proper.
      @Override
      public void performHttp(HttpServletRewrite event, EvaluationContext context)
      {
         event.getRequest().getSession().invalidate();
      }

      public static OperationBuilder logout()
      {
         return new Session();
      }
   }
}
