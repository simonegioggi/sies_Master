package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioRicDeterminazioneTermini</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */
public class ActDettaglioRicDeterminazioneTermini extends ActDettaglioRicEstinzioneReato
{

  public String processRequest() throws Exception
  {
	super.processRequest();
    // Dettaglio Fascicolo SIEP
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
    BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    DettaglioFascicoloModel lDettaglio = null;
    lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aId);
    if(lDettaglio == null)
     throw new F3BException( F3BException.USER_MESSAGE, "Fascicolo non presente" );
    setRequestAttribute("dettagliofascicolo", lDettaglio);

	
    return PG_DETTAGLIO_RICHIESTADETERMINAZIONETERMINI;
  }
}