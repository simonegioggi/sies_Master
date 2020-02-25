package siap.siepe.attivita.action;


/**
* <p>Title: ActInserisciAttivita</p>
* <p>Description: Classe Action per l'inserimento di Attivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.action.ICostantiAssistenteSociale;
import siap.siepe.attivita.controller.IAttivita;
//import siap.sico.utente.model.UtenteModel;
//import siap.sico.security.action.ICostantiSecurity;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.util.SIEPELookupRemote;
//import f3b.web.RedirectTo;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAttivita extends ActionSiap implements ICostantiAttivita
{
/**
* Azione di Inserimento del Attivita
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
    AttivitaModel lAttMod = new AttivitaModel();
    FascicoloSiepeEstesoModel lFasEsteso = (FascicoloSiepeEstesoModel) this.getSessionAttribute("FascicoloSiepeEsteso");

    lAttMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );

   // lAttMod.setDataChiusura( getRequestDateParameter( CAMPO_ANNO_DATA_CHIUSURA,CAMPO_MESE_DATA_CHIUSURA,CAMPO_GIORNO_DATA_CHIUSURA) );

    lAttMod.setCodTipoAttivita( getRequestStringParameter( CAMPO_COD_TIPO_ATTIVITA) );
    lAttMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
    // Imposta il Codice dell'Operatore connesso per l'inserimento.
    lAttMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    // Imposta il Codice ufficio dell'operatore connesso per l'inserimento.
    lAttMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lAttMod.setDataInserimento(DateUtils.getSysDate());
    lAttMod.setFasSieIdFasSiepe( lFasEsteso.getFascicoloSiepe().getIdFascicoloSiepe() );
    lAttMod.setAssSocIdAssSociale( getRequestBigDecimalParameter( ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE ) );

    IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
    AttivitaModel lAttModRet = lCtrl.ExInserisciAttivita(lAttMod);		 // setta la risposta nella request
    setRequestAttribute("attivita", lAttModRet);
    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.attivita.action.ActLoadDettaglioAttivita&"+CAMPO_ID_ATTIVITA+"="+lAttModRet.getIdAttivita().toString();

    return lPage;
	 }



}
