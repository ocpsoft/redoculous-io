package org.ocpsoft.redoculous.io.init;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

@Startup
@Singleton
public class IDMInitializer
{

   @Inject
   private PartitionManager partitionManager;

   @PostConstruct
   public void create()
   {

      // Create user john
      User john = new User("john");
      john.setEmail("john@acme.com");
      john.setFirstName("John");
      john.setLastName("Smith");

      IdentityManager identityManager = this.partitionManager.createIdentityManager();

      identityManager.add(john);
      identityManager.updateCredential(john, new Password("demo"));

      // Create user mary
      User mary = new User("mary");
      mary.setEmail("mary@acme.com");
      mary.setFirstName("Mary");
      mary.setLastName("Jones");
      identityManager.add(mary);
      identityManager.updateCredential(mary, new Password("demo"));

      // Create user jane
      User jane = new User("jane");
      jane.setEmail("jane@acme.com");
      jane.setFirstName("Jane");
      jane.setLastName("Doe");
      identityManager.add(jane);
      identityManager.updateCredential(jane, new Password("demo"));

      // Create user jane
      User lincoln = new User("lincoln");
      lincoln.setEmail("lincoln@ocpsoft.org");
      lincoln.setFirstName("Lincoln");
      lincoln.setLastName("Baxter, III");
      identityManager.add(lincoln);
      identityManager.updateCredential(lincoln, new Password("lincoln"));

      // Create role "manager"
      Role manager = new Role("manager");
      identityManager.add(manager);

      // Create application role "superuser"
      Role superuser = new Role("superuser");
      identityManager.add(superuser);

      // Create group "sales"
      Group customers = new Group("users");
      identityManager.add(customers);

      RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

      // Make john a member of the "sales" group
      BasicModel.addToGroup(relationshipManager, john, customers);

      // Make mary a manager of the "sales" group
      BasicModel.grantGroupRole(relationshipManager, mary, manager, customers);

      // Grant the "superuser" application role to jane
      BasicModel.grantRole(relationshipManager, lincoln, superuser);
   }
}