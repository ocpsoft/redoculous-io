package org.ocpsoft.redoculous.io.security;

import static org.picketlink.idm.model.basic.BasicModel.hasRole;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;

@Named("auth")
@SessionScoped
public class AuthorizationChecker implements Serializable
{
   private static final long serialVersionUID = 4050091154625527527L;

   private final Map<String, Boolean> applicationRoles = new HashMap<String, Boolean>();
   private final Map<String, Boolean> groups = new HashMap<String, Boolean>();
   private final Map<String, Boolean> groupRoles = new HashMap<String, Boolean>();

   @Inject
   private Identity identity;

   @Inject
   private IdentityManager identityManager;

   @Inject
   private RelationshipManager relationshipManager;

   public boolean hasApplicationRole(String roleName)
   {
      if (!applicationRoles.containsKey(roleName))
      {
         Role role = BasicModel.getRole(this.identityManager, roleName);
         applicationRoles.put(roleName, hasRole(this.relationshipManager, this.identity.getAccount(), role));
      }
      return applicationRoles.get(roleName);
   }

   public boolean isMember(String groupName)
   {
      if (!groups.containsKey(groupName))
      {
         Group group = BasicModel.getGroup(this.identityManager, groupName);
         groups.put(groupName, BasicModel.isMember(this.relationshipManager, this.identity.getAccount(), group));
      }
      return groups.get(groupName);
   }

   public boolean hasGroupRole(String groupName, String roleName)
   {
      String key = groupName + "/" + roleName; // good enough
      if (!groupRoles.containsKey(key))
      {
         Group group = BasicModel.getGroup(this.identityManager, groupName);
         Role role = BasicModel.getRole(this.identityManager, roleName);
         groupRoles.put(key, BasicModel.hasGroupRole(this.relationshipManager,
                  this.identity.getAccount(), role, group));
      }
      return groupRoles.get(key);
   }
}
