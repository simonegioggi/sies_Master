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
* <p>Title: ActModificaAssistenteSociale</p>
* <p>Description: Classe Action per la modifica di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActModificaAssistenteSociale extends ActionSiap
implements ICostantiAssistenteSociale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Modifica del AssistenteSociale.
* <p>
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* <p>
* @throws Exception
*/
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // Istanzia e riempie il model
    AssistenteSocialeModel lAssSocMod = new AssistenteSocialeModel ();

    lAssSocMod.setIdAssistenteSociale( getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_SOCIALE));
    lAssSocMod.setCognome(getRequestStringParameter( CAMPO_COGNOME).toUpperCase() );
    lAssSocMod.setNome(getRequestStringParameter( CAMPO_NOME ).toUpperCase() );
    lAssSocMod.setCodiceFiscale(getRequestStringParameter( CAMPO_CODICE_FISCALE).toUpperCase());
    lAssSocMod.setIndirizzo(getRequestStringParameter( CAMPO_INDIRIZZO) );
    lAssSocMod.setTelefono(getRequestStringParameter( CAMPO_TELEFONO ) );
    lAssSocMod.setEmail(getRequestStringParameter( CAMPO_EMAIL ) );
    lAssSocMod.setFax(getRequestStringParameter( CAMPO_FAX) );
    lAssSocMod.setCellulare(getRequestStringParameter( CAMPO_CELLULARE) );
    lAssSocMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO ) );
    lAssSocMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
    lAssSocMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

    lAssSocMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lAssSocMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lAssSocMod.setDataAggiornamento(DateUtils.getSysDate());

    // Chiama il controller
    IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
    AssistenteSocialeModel lAssSocModRet = lCtrl.ExModificaAssistenteSociale(lAssSocMod);

    // lAssSocMod = lCtrl.ExModificaAssistenteSociale(lAssSocMod);
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.assistentesociale.action.ActLoadDettaglioAssistenteSociale&"+CAMPO_ID_ASSISTENTE_SOCIALE+"="+lAssSocModRet.getIdAssistenteSociale().toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;
  }
}