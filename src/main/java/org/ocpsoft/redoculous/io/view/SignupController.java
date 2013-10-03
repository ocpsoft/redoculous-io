package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.redoculous.io.util.Threads;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;

@Named
@RequestScoped
@Stateful
public class SignupController implements Serializable
{
   private static final long serialVersionUID = -8465862410895216951L;

   @Inject
   private PartitionManager partitionManager;

   @Inject
   private LoginController loginController;

   @Inject
   private DefaultLoginCredentials credentials;

   private static final int VALIDATION_FAILURE_DELAY = 200;
   private String username;
   private String email;
   private String password;

   public Object signup()
   {
      User user = new User(username);
      user.setEmail(email);

      IdentityManager identityManager = partitionManager.createIdentityManager();
      identityManager.add(user);
      identityManager.updateCredential(user, new Password(password));

      Group users = BasicModel.getGroup(identityManager, "users");
      RelationshipManager relationshipManager = partitionManager.createRelationshipManager();
      BasicModel.addToGroup(relationshipManager, user, users);

      FacesMessages.addInfo(FacesContext.getCurrentInstance(), "Account created. You are now ready to get Redoculous!");

      credentials.setUserId(username);
      credentials.setPassword(password);
      return loginController.login();
   }

   public void validateUsername(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         if (((String) value).length() < 3)
         {
            Threads.sleep(VALIDATION_FAILURE_DELAY);
            throw new ValidatorException(FacesMessages.error("Username must be at least three characters long."));
         }

         User user = BasicModel.getUser(partitionManager.createIdentityManager(), (String) value);
         if (user != null)
         {
            Threads.sleep(VALIDATION_FAILURE_DELAY);
            throw new ValidatorException(FacesMessages.error("Username is unavailable."));
         }
      }
   }

   public void validateEmail(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         boolean emailExists = partitionManager.createIdentityManager().createIdentityQuery(User.class)
                  .setParameter(User.EMAIL, value).getResultCount() > 0;

         if (emailExists)
         {
            Threads.sleep(VALIDATION_FAILURE_DELAY);
            throw new ValidatorException(FacesMessages.error("Email address is already associated with an account."));
         }

         if (!((String) value).matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                  + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
         {
            Threads.sleep(VALIDATION_FAILURE_DELAY);
            throw new ValidatorException(FacesMessages.error("Email address is not valid."));
         }
      }
   }

   public void validatePassword(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         if (((String) value).length() >= 8)
            return;
      }
      Threads.sleep(VALIDATION_FAILURE_DELAY);
      throw new ValidatorException(FacesMessages.error("Password must be at least eight characters long."));
   }

   /*
    * Getter & Setters
    */
   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }
}