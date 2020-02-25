package siap.siepe.assistentesociale.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
* <p>Title: ActInserisciAssistenteSociale</p>
* <p>Description: Classe Action per l'inserimento di AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciAssistenteSociale extends ActionSiap implements ICostantiAssistenteSociale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Effettua Inserimento dell'Assistente Sociale.
  * <p>
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
  * @throws Exception propaga Errore di eccezione.
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    AssistenteSocialeModel lAssSocMod = new AssistenteSocialeModel();

    lAssSocMod.setCognome(getRequestStringParameter( CAMPO_COGNOME).toUpperCase() );
    lAssSocMod.setNome(getRequestStringParameter( CAMPO_NOME).toUpperCase() );
    lAssSocMod.setCodiceFiscale(getRequestStringParameter( CAMPO_CODICE_FISCALE).toUpperCase());
    lAssSocMod.setIndirizzo(getRequestStringParameter( CAMPO_INDIRIZZO) );
    lAssSocMod.setTelefono(getRequestStringParameter( CAMPO_TELEFONO) );
    lAssSocMod.setCodUfficioAppartenenza( getCodUfficioUtenteConnesso());
    lAssSocMod.setEmail(getRequestStringParameter( CAMPO_EMAIL) );
    lAssSocMod.setFax(getRequestStringParameter( CAMPO_FAX) );
    lAssSocMod.setCellulare(getRequestStringParameter( CAMPO_CELLULARE) );
    lAssSocMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lAssSocMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lAssSocMod.setDataInserimento(DateUtils.getSysDate());
    lAssSocMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
    lAssSocMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
    lAssSocMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

    // Chiama il controller.
    IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
    AssistenteSocialeModel lAssSocModRet = lCtrl.ExInserisciAssistenteSociale(lAssSocMod);		 // setta la risposta nella request

    setRequestAttribute("assistentesociale", lAssSocModRet);
    //Prepara la pagina di destinazione.
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.assistentesociale.action.ActLoadDettaglioAssistenteSociale&"+CAMPO_ID_ASSISTENTE_SOCIALE+"="+lAssSocModRet.getIdAssistenteSociale().toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;
  }
}