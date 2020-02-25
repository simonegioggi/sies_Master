package siap.sico.magistrato.action;


/**
* <p>Title: ActInserisciMagistrato</p>
* <p>Description: Classe Action per l'inserimento di Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciMagistrato extends ActionSiap implements ICostantiMagistrato
{
  /**
   * Azione di Inserimento del Magistrato
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
    public String processRequest() throws F3BException
    {
      MagistratoModel lMagMod = new MagistratoModel();

      lMagMod.setCodMagistrato( getRequestStringParameter( CAMPO_COD_MAGISTRATO) );
      lMagMod.setCognome( getRequestStringParameter( CAMPO_COGNOME) );
      lMagMod.setNome( getRequestStringParameter( CAMPO_NOME) );
      lMagMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
      lMagMod.setEMailUfficio(getRequestStringParameter(CAMPO_E_MAIL_UFFICIO));
      lMagMod.setEMailPrivata(getRequestStringParameter(CAMPO_E_MAIL_PRIVATA));
      lMagMod.setNumCellulare(getRequestStringParameter(CAMPO_NUM_CELLULARE));

      lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso() );

      lMagMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
      lMagMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

      lMagMod.setCodOperatoreInserimento(getCodUtenteConnesso());
      lMagMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

     // lMagMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lMagMod.setDataInserimento(DateUtils.getSysDate());
      lMagMod.setDataInserimento(DateUtils.getSysDate());


		 //---Aggiungere in SICOLookupRemote il metodo getMagistratoRemote()

      IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
      MagistratoModel llMagModRet = lCtrl.ExInserisciMagistrato(lMagMod);		 // setta la risposta nella request
      setRequestAttribute("magistrato", llMagModRet);
		  //Prepara la pagina di destinazione
      String lPage = "";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.magistrato.action.ActLoadDettaglioMagistrato&"+CAMPO_COD_MAGISTRATO+"="+llMagModRet.getCodMagistrato().toString();

      return lPage;
  }

}