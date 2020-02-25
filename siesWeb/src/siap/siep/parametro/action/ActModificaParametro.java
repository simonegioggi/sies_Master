package siap.siep.parametro.action;


/**
* <p>Title: ActModificaParametro</p>
* <p>Description: Classe Action per la modifica di Parametro</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaParametro extends ActionSiap implements ICostantiParametro
{
/**
* Azione di Modifica del Parametro
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_PARAMETRO);
 		 // riempie il model
 		 ParametroModel lParMod = new ParametroModel ();

 		 lParMod.setIdParametro(new BigDecimal(lId));
		 lParMod.setIdParametro( getRequestBigDecimalParameter( CAMPO_ID_PARAMETRO) );
		 lParMod.setNomeParametro( getRequestStringParameter( CAMPO_NOME_PARAMETRO) );
		 lParMod.setValore( getRequestStringParameter( CAMPO_VALORE) );
		 lParMod.setAnni( getRequestBigDecimalParameter( CAMPO_ANNI) );
		 lParMod.setMesi( getRequestBigDecimalParameter( CAMPO_MESI) );
		 lParMod.setGiorni( getRequestBigDecimalParameter( CAMPO_GIORNI) );
		 lParMod.setImporto( getRequestBigDecimalParameter( CAMPO_IMPORTO) );
		 lParMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA) );
		 lParMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );
		 lParMod.setCodUfficioValidita( getRequestStringParameter( CAMPO_COD_UFFICIO_VALIDITA) );
		 lParMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lParMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 //lParMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lParMod.setDataAggiornamento(DateUtils.getSysDate());
		 lParMod.setCodUfficioAggiormanento( getRequestStringParameter( CAMPO_COD_UFFICIO_AGGIORMANENTO) );

		 // chiama il controller
		 IParametro lCtrl = SIEPLookupRemote.getParametroRemote();
		 ParametroModel llParModRet = lCtrl.ExModificaParametro(lParMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("parametro", llParModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.parametro.action.ActLoadDettaglioParametro&"+CAMPO_ID_PARAMETRO+"="+llParModRet.getIdParametro().toString();
		 return lPage;
	 }



}