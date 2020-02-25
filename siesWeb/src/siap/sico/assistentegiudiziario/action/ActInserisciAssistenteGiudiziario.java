package siap.sico.assistentegiudiziario.action;


/**
* <p>Title: ActInserisciAssistenteGiudiziario</p>
* <p>Description: Classe Action per l'inserimento di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
/**
* Azione di Inserimento del AssistenteGiudiziario
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
    AssistenteGiudiziarioModel lAssMod = new AssistenteGiudiziarioModel();

    //lAssMod.setIdAssistenteGiudiziario( getRequestBigDecimalParameter( CAMPO_ID_ASSISTENTE_GIUDIZIARIO) );
    lAssMod.setCognome(getRequestStringParameter( CAMPO_COGNOME).toUpperCase() );
    lAssMod.setNome(getRequestStringParameter( CAMPO_NOME).toUpperCase() );

    lAssMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
    lAssMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
    lAssMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

    lAssMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
    lAssMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lAssMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());		 lAssMod.setDataInserimento(DateUtils.getSysDate());
    lAssMod.setDataInserimento(DateUtils.getSysDate());

    //---Richiama il controller

    IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
    AssistenteGiudiziarioModel llAssModRet = lCtrl.ExInserisciAssistenteGiudiziario(lAssMod);		 // setta la risposta nella request
    setRequestAttribute("assistentegiudiziario", llAssModRet);
    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.assistentegiudiziario.action.ActLoadDettaglioAssistenteGiudiziario&"+CAMPO_ID_ASSISTENTE_GIUDIZIARIO+"="+llAssModRet.getIdAssistenteGiudiziario().toString();
    return lPage;
  }



}