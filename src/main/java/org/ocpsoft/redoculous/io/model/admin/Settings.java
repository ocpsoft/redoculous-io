package org.ocpsoft.redoculous.io.model.admin;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.ocpsoft.redoculous.io.model.PersistentObject;
import org.ocpsoft.redoculous.io.model.account.UserProfile;

@Typed()
@Entity
public class Settings extends PersistentObject<Settings>
{
   private static final long serialVersionUID = -263424330273581356L;

   @Column
   private String redoculousURL;

   @OneToOne
   private UserProfile lastUpdatedBy;

   public String getRedoculousURL()
   {
      return redoculousURL;
   }

   public void setRedoculousURL(String redoculousURL)
   {
      this.redoculousURL = redoculousURL;
   }

   public UserProfile getLastUpdatedBy()
   {
      return lastUpdatedBy;
   }

   public void setLastUpdatedBy(UserProfile lastUpdatedBy)
   {
      this.lastUpdatedBy = lastUpdatedBy;
   }
}