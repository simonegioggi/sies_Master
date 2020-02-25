package siap.sico.assistentegiudiziario.action;


/**
* <p>Title: ActModificaAssistenteGiudiziario</p>
* <p>Description: Classe Action per la modifica di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
/**
* Azione di Modifica del AssistenteGiudiziario
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
    {
      //Controllo che non si stia lavorando su una entità in modifica ad altri
      LockModel lck =
      lockIfNotLocked("assistente", getRequestStringParameter(CAMPO_ID_ASSISTENTE_GIUDIZIARIO), getCodUtenteConnesso());
      if (lck != null)
      {
        setRequestAttribute (IWebConstants.MESSAGE_TEXT, "L'"+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
        return IWebConstants.PG_MESSAGE;
      }


      // istanzia e riempie il model
      AssistenteGiudiziarioModel lAssMod = new AssistenteGiudiziarioModel ();

      lAssMod.setIdAssistenteGiudiziario(getRequestBigDecimalParameter( CAMPO_ID_ASSISTENTE_GIUDIZIARIO) );
      lAssMod.setCognome(getRequestStringParameter( CAMPO_COGNOME).toUpperCase() );
      lAssMod.setNome( getRequestStringParameter( CAMPO_NOME).toUpperCase() );

      lAssMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
      lAssMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
      lAssMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );
      lAssMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lAssMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      lAssMod.setDataAggiornamento(DateUtils.getSysDate());

      // chiama il controller
      IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
      AssistenteGiudiziarioModel llAssModRet = lCtrl.ExModificaAssistenteGiudiziario(lAssMod);

      setRequestAttribute("modalita", "M");
      setRequestAttribute("assistentegiudiziario", llAssModRet);

      String lPage = "";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.assistentegiudiziario.action.ActLoadDettaglioAssistenteGiudiziario&"+CAMPO_ID_ASSISTENTE_GIUDIZIARIO+"="+llAssModRet.getIdAssistenteGiudiziario().toString();
      return lPage;
    }
}