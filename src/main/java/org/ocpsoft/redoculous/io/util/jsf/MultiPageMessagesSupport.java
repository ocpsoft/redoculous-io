package org.ocpsoft.redoculous.io.util.jsf;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MultiPageMessagesSupport implements PhaseListener
{
   private static final long serialVersionUID = 1250469273857785274L;
   private final FacesMessages messagesUtils = new FacesMessages();

   @Override
   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   @Override
   public void beforePhase(final PhaseEvent event)
   {
      FacesContext facesContext = event.getFacesContext();
      messagesUtils.saveMessages(facesContext, facesContext.getExternalContext().getSessionMap());

      if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
      {
         /*
          * Check to see if we are "naturally" in the RENDER_RESPONSE phase. If
          * we have arrived here and the response is already complete, then the
          * page is not going to show up: don't display messages yet.
          */
         if (!facesContext.getResponseComplete())
         {
            messagesUtils.restoreMessages(facesContext, facesContext.getExternalContext().getSessionMap());
         }
      }
   }

   /*
    * Save messages into the session after every phase.
    */
   @Override
   public void afterPhase(final PhaseEvent event)
   {
      if (!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
      {
         FacesContext facesContext = event.getFacesContext();
         messagesUtils.saveMessages(facesContext, facesContext.getExternalContext().getSessionMap());
      }
   }
}