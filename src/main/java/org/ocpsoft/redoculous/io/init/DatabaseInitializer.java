package org.ocpsoft.redoculous.io.init;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.admin.Settings;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;

@Startup
@Singleton
public class DatabaseInitializer
{

   @Inject
   private PartitionManager partitionManager;

   @Inject
   @PicketLink
   private EntityManager em;

   @PostConstruct
   public void create()
   {
      IdentityManager identityManager = this.partitionManager.createIdentityManager();

      User lincoln = new User("lincoln");
      lincoln.setEmail("lincoln@ocpsoft.org");
      lincoln.setFirstName("Lincoln");
      lincoln.setLastName("Baxter, III");
      identityManager.add(lincoln);
      identityManager.updateCredential(lincoln, new Password("lincoln"));

      Group users = new Group("users");
      identityManager.add(users);
      Group administrators = new Group("administrators");
      identityManager.add(administrators);

      RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
      BasicModel.addToGroup(relationshipManager, lincoln, users);
      BasicModel.addToGroup(relationshipManager, lincoln, administrators);

      /**
       * Business Logic Setup
       */
      UserProfile profile = new UserProfile();
      profile.setUsername(lincoln.getLoginName());
      em.persist(profile);

      Settings settings = new Settings();
      settings.setRedoculousURL("http://localhost:8080/redoculous-server/api");
      em.persist(settings);
   }
}