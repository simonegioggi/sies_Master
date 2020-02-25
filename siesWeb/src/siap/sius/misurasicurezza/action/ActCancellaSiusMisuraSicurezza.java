package siap.sius.misurasicurezza.action;


/**
* <p>Title: ActCancellaSiusMisuraSicurezza</p>
* <p>Description: Classe Action per la cancellazione di Misura Sicurezza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	  /**
	   * Azione di cancellazione di Misura Sicurezza
	   * @return Nome della pagina JSP su cui posizionarsi
	   * al termine dell'elaborazione
	   * @throws F3BException
	   */
	  public String processRequest() throws Exception
	  {
	    // riempie il model
	    MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

	    lMisMod.setIdMisuraSicurezza(getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA));

	    // chiama il controller
	    IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
	    lMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(lMisMod.getIdMisuraSicurezza());
	    
	    // Ricerca il Fascicolo Sius per metterlo in sessione
	    // nel caso la Cancellazione venga richiamata dall'Elenco di procedimenti con Misure Sicurezza
	    IFascicoloSius lCtrlFasc = SIUSLookupRemote.getFascicoloSiusRemote();
	    FascicoloGPModel lFasMod = lCtrlFasc.ExRicercaFascicoloByKey(lMisMod.getFasSiuIdFascicoloSius());

	     // Se sono già stati emessi provvedimenti o il fascicolo non è in stato 'Iscritto' non sono consentite modifiche alle misure
	     RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
	     boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();

	     if ( lEsistenzaDoc || lFasMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("02") != 0 )
	    	 throw new SIUSException(SIUSException.USER_MESSAGE,"Impossibile cancellare misure per questo fascicolo ! ");
	     //

	    if (lFasMod != null)
	    {          
	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	      siesLogger.debug("lFasMod " + lFasMod);

	      setRequestAttribute("fascicoloSiusGP", lFasMod);
	      setSessionAttribute("fascicoloSiusGP", lFasMod);
	    }
	    //---------------------------------------------------------------------------------------------------
	    
	    lCtrl.ExCancellaMisuraSicurezza(lMisMod);
	    
		String lPage="";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza" ;
		return lPage; //restituisce la jsp di VIEW
	    }
}