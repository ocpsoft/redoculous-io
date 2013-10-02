package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

   private String username;
   private String email;
   private String password;

   public String signup()
   {
      User user = new User(username);
      user.setEmail(email);

      IdentityManager identityManager = this.partitionManager.createIdentityManager();
      identityManager.add(user);
      identityManager.updateCredential(user, new Password(password));

      Group users = BasicModel.getGroup(identityManager, "users");
      RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
      BasicModel.addToGroup(relationshipManager, user, users);

      credentials.setUserId(username);
      credentials.setPassword(password);
      return loginController.login();
   }

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