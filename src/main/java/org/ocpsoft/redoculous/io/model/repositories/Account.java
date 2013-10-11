package org.ocpsoft.redoculous.io.model.repositories;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account extends AccountTypeEntity
{
   private static final long serialVersionUID = 4581044700527131400L;

   @OneToMany(mappedBy = "account", cascade = { CascadeType.REFRESH })
   private Set<SourceRepository> repositories = new HashSet<SourceRepository>();

   public Set<SourceRepository> getRepositories()
   {
      return this.repositories;
   }

   public void setRepositories(final Set<SourceRepository> repositories)
   {
      this.repositories = repositories;
   }

}