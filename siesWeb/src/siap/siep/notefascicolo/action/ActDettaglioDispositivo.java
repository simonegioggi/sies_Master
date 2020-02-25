package siap.siep.notefascicolo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActDettaglioDispositivo</p>
 * <p>Description: </p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActDettaglioDispositivo extends ActionSiap
{
   public String processRequest() throws F3BException
    {
      this.setRequestAttribute("note",this.getRequestStringParameter("note"));

      return IWebConstants.ROOT_DIR + "files/siap/siep/notefascicolo/RicercaDispositivo.jsp";
    }
}