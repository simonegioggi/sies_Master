package siap.siep.notiziareato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciNotiziaReato</p>
 * <p>Description: Classe Action per la load inserisci della Notizia di Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciNotiziaReato
    extends ActionSiap
    implements ICostantiNotiziaReato
{
  public String processRequest() throws F3BException
  {
	// Inserisce una combobox le autorità
	 Option lOptionA = new Option(DecodificheManager.getInstance().getTipoAutorita());
	 setRequestAttribute("autorita", "" + lOptionA);  

    // Inserisce una ComboBOX di tipo (-,S,N)
    Option lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino());
    Option lOptionFoto = new Option(DecodificheManager.getInstance().getFlagSNTrattino());
    Option lOptionArr = new Option(DecodificheManager.getInstance().getFlagSNTrattino());

    // Setta la ComboBOX nella request.
    setRequestAttribute("combosntrat", "" + lOption );
    setRequestAttribute("fotocombosntrat", "" + lOptionFoto );
    setRequestAttribute("arrcombosntrat", "" + lOptionArr );

    // Imposta la Modalità di Inserimento.
    setRequestAttribute("modalita", "I");
    
    //IMPOSTAZIONI PER FUNZIONALITà BACK
    if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){    	
    	setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
    }

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di "Inserimento Notizia di Reato" da Fase Istruttoria.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
	String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);

    // Apre la pagina di inserimento della Notizia di Reato
    return PG_LOAD_INSERISCINOTIZIAREATO;

  }

}