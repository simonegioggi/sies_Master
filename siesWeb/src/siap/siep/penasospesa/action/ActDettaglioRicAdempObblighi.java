package siap.siep.penasospesa.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ActDettaglioRicDeterminazioneTermini</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */
public class ActDettaglioRicAdempObblighi extends ActDettaglioRicEstinzioneReato
{

  public String processRequest() throws Exception
  {
	super.processRequest();
    //return PG_DETTAGLIO_RICHIESTADETERMINAZIONETERMINI;
    return IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioRicAdempObblighi.jsp";
	
  }
}