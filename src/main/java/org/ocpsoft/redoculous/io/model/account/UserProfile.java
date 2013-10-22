package org.ocpsoft.redoculous.io.model.account;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Typed;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.ocpsoft.redoculous.io.model.PersistentObject;
import org.ocpsoft.redoculous.io.model.repositories.SourceRepository;

@Typed()
@Entity
public class UserProfile extends PersistentObject<UserProfile>
{
   private static final long serialVersionUID = 4581044700527131400L;

   @Column
   private String username;

   @OneToMany(mappedBy = "owner", cascade = { CascadeType.REFRESH })
   private Set<SourceRepository> repositories = new HashSet<SourceRepository>();

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public Set<SourceRepository> getRepositories()
   {
      return this.repositories;
   }

   public void setRepositories(final Set<SourceRepository> repositories)
   {
      this.repositories = repositories;
   }

}