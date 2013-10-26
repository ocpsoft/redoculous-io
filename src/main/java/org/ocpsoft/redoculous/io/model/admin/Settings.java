package org.ocpsoft.redoculous.io.model.admin;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.ocpsoft.redoculous.io.model.PersistentObject;

@Typed()
@Entity
public class Settings extends PersistentObject<Settings>
{
   private static final long serialVersionUID = -263424330273581356L;

   @Column
   private String redoculousURL;

   public String getRedoculousURL()
   {
      return redoculousURL;
   }

   public void setRedoculousURL(String redoculousURL)
   {
      this.redoculousURL = redoculousURL;
   }
}