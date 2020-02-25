package siap.siep.notiziareato.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaNotiziaReato</p>
 * <p>Description: Classe Action per la load modifica di NotiziaReato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadModificaNotiziaReato
    extends ActionSiap
    implements ICostantiNotiziaReato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_NOTIZIA_REATO);

    // chiama il controller
    //INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
    NotiziaReatoController lCtrl = new NotiziaReatoController();

    // Instanzia e riempie il model
    NotiziaReatoModel llNotMod = lCtrl.ExRicercaNotiziaReatoByKey(new BigDecimal(lId));

    // setta la risposta nella request
    setRequestAttribute("notiziareato", llNotMod);

    String lSelCombo = "-";
    //03.06.2009 ANGELA if(llNotMod.getFlagArrestato()!=null)
    	lSelCombo = llNotMod.getAcquisizioneDiretta();
    // Inserisce una ComboBOX di tipo (-,S,N) e carica il valore inserito nel campo
    Option lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), lSelCombo);
    

	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.info("MODIFICA REATON: "+lSelCombo);
	 
    
    lSelCombo = "-";
    //03.06.2009 ANGELA if(llNotMod.getFlagArrestato()!=null)
    	lSelCombo = llNotMod.getFlagFotosegnalato();
    Option lOptionFoto = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), lSelCombo);
    
    lSelCombo = "-";
    if(llNotMod.getFlagArrestato()!=null)
    	lSelCombo = llNotMod.getFlagArrestato();
    
    Option lOptionArr = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), lSelCombo);
    
    // Inserisce una combobox le autorità
        
	 Option lOptionA = new Option(DecodificheManager.getInstance().getTipoAutorita(),llNotMod.getCodAutoritaFoto());
	 setRequestAttribute("autorita", "" + lOptionA);  

    // Setta le ComboBOX nella request.
    setRequestAttribute("combosntrat", "" + lOption);    
    setRequestAttribute("fotocombosntrat", "" + lOptionFoto);
    setRequestAttribute("arrcombosntrat", "" + lOptionArr);

    // Imposta la Modalità di Modifica.
    setRequestAttribute("modalita", "M");
    
//  IMPOSTAZIONI PER FUNZIONALITà BACK
    if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){    	
    	setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
    }

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Ricerca Soggetto da Iscrizione Manuale.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
	String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);

    // Apre la pagina di modifica della Notizia di Reato
    return PG_LOAD_INSERISCINOTIZIAREATO;
    
  }

}