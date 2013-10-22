package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

@Named
@Stateful
@RequestScoped
public class ProfileController implements Serializable
{
   private static final long serialVersionUID = -2583174292087675121L;

   private UserProfile profile = new UserProfile();

   @Inject
   private Identity identity;

   @Inject
   private EntityManager em;

   @Produces
   @Named("profile")
   public UserProfile getProfile()
   {
      if (identity.isLoggedIn() && !profile.isPersistent())
      {
         TypedQuery<UserProfile> query = em.createQuery("from UserProfile p where p.username = :username",
                  UserProfile.class);

         User user = (User) this.identity.getAccount();
         query.setParameter("username", user.getLoginName());
         profile = query.getSingleResult();
      }
      return profile;
   }
}