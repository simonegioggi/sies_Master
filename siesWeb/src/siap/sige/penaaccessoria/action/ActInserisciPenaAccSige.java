package siap.sige.penaaccessoria.action;


/**
* <p>Title: ActInserisciPenaAccessoria</p>
* <p>Description: Classe Action per l'inserimento di PenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.penaaccessoria.action.ActInserisciPenaAccessoria;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.util.F3BException;

public class ActInserisciPenaAccSige extends ActInserisciPenaAccessoria
{
 /**
  * Azione di Inserimento del PenaAccessoria
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
	
  public String processRequest() throws Exception
 {
	// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	
	// Pena Accessoria da inserire
	PenaAccSigeModel lPenaAccessoriaSige = null;
	
	mPenMod = letturaDatiPenaAccesoria(null);
	lPenaAccessoriaSige = new PenaAccSigeModel(mPenMod);
	lPenaAccessoriaSige.setFasSigeSenId(lIdFasSigeSen);
	
	// Inserimento Pena Accessoria
    IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
    mPenMod = lCtrl.ExInserisciPenaAccessoria(lPenaAccessoriaSige);

    // Serve ???
    setRequestAttribute("penaaccessoria", mPenMod);
    
    // Eventuale inserimento della Pena Accessoria Sostitutiva.
    mPenModNew = letturaDatiPenaAccesoriaSostitutiva(null);
    if (mPenModNew != null)
    {
    	lPenaAccessoriaSige = new PenaAccSigeModel(mPenModNew);
    	lPenaAccessoriaSige.setFasSigeSenId(lIdFasSigeSen);
    	
    	mPenModNew = lCtrl.ExInserisciPenaAccessoria(lPenaAccessoriaSige);
    }

    // Prepara la pagina di destinazione.
    return paginaDestinazione("siap.sige.penaaccessoria.action.ActLoadDettaglioPenaAccSige");
 }
}
