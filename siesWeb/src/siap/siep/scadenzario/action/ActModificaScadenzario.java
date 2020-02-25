package siap.siep.scadenzario.action;


/**
* <p>Title: ActModificaScadenzario</p>
* <p>Description: Classe Action per la modifica di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaScadenzario extends ActionSiap implements ICostantiScadenzario
{
/**
* Azione di Modifica del Scadenzario
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException 
{

 		 String lId = getRequestStringParameter(CAMPO_ID_SCADENZARIO);
 		 // riempie il model
 		 ScadenzarioModel lScaMod = new ScadenzarioModel ();

 		 lScaMod.setIdScadenzario(new BigDecimal(lId));
		 lScaMod.setIdScadenzario( getRequestBigDecimalParameter( CAMPO_ID_SCADENZARIO) );
		 lScaMod.setCodTipoScadenzario( getRequestStringParameter( CAMPO_COD_TIPO_SCADENZARIO) );
		 lScaMod.setDataInizioScadenza( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_SCADENZA,CAMPO_MESE_DATA_INIZIO_SCADENZA,CAMPO_GIORNO_DATA_INIZIO_SCADENZA) );
		 lScaMod.setDataFineScadenza( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_SCADENZA,CAMPO_MESE_DATA_FINE_SCADENZA,CAMPO_GIORNO_DATA_FINE_SCADENZA) );
		 lScaMod.setFlagVisto( getRequestStringParameter( CAMPO_FLAG_VISTO) );
		 lScaMod.setDataVisto( getRequestDateParameter( CAMPO_ANNO_DATA_VISTO,CAMPO_MESE_DATA_VISTO,CAMPO_GIORNO_DATA_VISTO) );
		 lScaMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lScaMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lScaMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lScaMod.setDataAggiornamento(DateUtils.getSysDate());
		 lScaMod.setDataAggiornamento(DateUtils.getSysDate());
		 lScaMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

		 // chiama il controller
		 IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		 ScadenzarioModel llScaModRet = lCtrl.ExModificaScadenzario(lScaMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("scadenzario", llScaModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.scadenzario.action.ActLoadDettaglioScadenzario&"+CAMPO_ID_SCADENZARIO+"="+llScaModRet.getIdScadenzario().toString();
		 return lPage;
	 }



}