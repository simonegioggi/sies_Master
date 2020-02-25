package siap.sico.assistentegiudiziario.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;


/**
* <p>Title: ActLoadInserisciAssistenteGiudiziario</p>
* <p>Description: Classe Action per la load inserisci di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
  public String processRequest() throws F3BException
  {

    // Inserire Eventuali ComboBOX
      Option lOption = new Option( DecodificheManager.getInstance().getFlagStato());
      setRequestAttribute( "elencoFlagStato", "" + lOption );

      // Imposta Modalità.
      setRequestAttribute("modalita", "I");

  	  // MEV 15 - Revisione SIGE
  	  // Aggiunto parametro per identificare la funzione che richiama la maschera
  	  // di Inserimento di un Assistente Udienza da Funzioni Amministrative.
  	  // Quando viene richiamata da SIGE sulla maschera viene inserito
      // il Calendario in corrispondenza di ogni campo data
      String codFunzione = getCodFunMenuVerticale();
  	  setRequestAttribute("codFunzione", codFunzione);

      return PG_LOAD_INSERISCIASSISTENTEGIUDIZIARIO;  //restituisce la jsp di VIEW

    }
}