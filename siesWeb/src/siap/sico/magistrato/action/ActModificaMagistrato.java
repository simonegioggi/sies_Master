package siap.sico.magistrato.action;


/**
* <p>Title: ActModificaMagistrato</p>
* <p>Description: Classe Action per la modifica di Magistrato</p>
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

public class ActModificaMagistrato extends ActionSiap implements ICostantiMagistrato
{
/**
* Azione di Modifica del Magistrato
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{


 		 // riempie il model
 		 MagistratoModel lMagMod = new MagistratoModel ();

		 lMagMod.setCodMagistrato( getRequestStringParameter( CAMPO_COD_MAGISTRATO) );
		 lMagMod.setCognome( getRequestStringParameter( CAMPO_COGNOME) );
		 lMagMod.setNome( getRequestStringParameter( CAMPO_NOME) );
		 lMagMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
                 lMagMod.setEMailUfficio(getRequestStringParameter(CAMPO_E_MAIL_UFFICIO));
                 lMagMod.setEMailPrivata(getRequestStringParameter(CAMPO_E_MAIL_PRIVATA));
                 lMagMod.setNumCellulare(getRequestStringParameter(CAMPO_NUM_CELLULARE));
		 lMagMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
		 lMagMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );
        	 lMagMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		 lMagMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
                 lMagMod.setDataAggiornamento(DateUtils.getSysDate());
                 lMagMod.setCodUfficioAppartenenza(lMagMod.getCodUfficioAggiornamento());
		 // chiama il controller
		 IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
		 MagistratoModel llMagModRet = lCtrl.ExModificaMagistrato(lMagMod);

		 //setRequestAttribute("modalita", "M");
		 setRequestAttribute("magistrato", llMagModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.magistrato.action.ActLoadDettaglioMagistrato&"+CAMPO_COD_MAGISTRATO+"="+llMagModRet.getCodMagistrato().toString();
		 return lPage;
	 }



}