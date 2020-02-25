package siap.sius.udienza.action;

import org.apache.log4j.Logger;

import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadRinvioUdienza </p>
* <p>Description: Classe Action per la load della form di Rinvio Udienza
* </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRinvioUdienza extends ActRicercaFSPuntuale
implements ICostantiUdienza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    // Invoca la process Request della superclasse.
    super.processRequest();

    String 	lFascSospeso = "NO";
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") == 0 ||
        lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") == 0 )
      throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito. Procedimento già definito.");

    if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10") == 0 )
      throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito. Procedimento già rinviato a nuovo ruolo!");

    // Viene effettuato il controllo sulla preesistenza di un Provvedimento definitorio
    // già emesso per il Fascicolo SIUS.
    // Se esiste almeno un provvedimento di questo tipo non può esserne emesso un Rinvio Udienza.
    RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
    if (lEsistenzaDoc)
      throw new SIUSException(SIUSException.USER_MESSAGE, "Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");
    
    // Ricerca se esistono udienze per quel GP con stato F o S
    UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
    IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
    lUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() ,"'F','S'");
    if (lUdiMod == null)
      throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");
    
    if (lRicerca.verificaEsistenzaSospensione()) 
	   		lFascSospeso = "SI";
    
    setRequestAttribute("fascSospeso", lFascSospeso);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return PG_LOAD_RINVIOUDIENZA;
  }
}