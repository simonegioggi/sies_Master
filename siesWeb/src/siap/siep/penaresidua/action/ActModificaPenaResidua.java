package siap.siep.penaresidua.action;


/**
* <p>Title: ActModificaPenaResidua</p>
* <p>Description: Classe Action per la modifica di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaPenaResidua extends ActionSiap implements ICostantiPenaResidua
{
/**
* Azione di Modifica del PenaResidua
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException 
{

 		 String lId = getRequestStringParameter(CAMPO_ID_PENA_RESIDUA);
 		 // riempie il model
 		 PenaResiduaModel lPenMod = new PenaResiduaModel ();

 		 lPenMod.setIdPenaResidua(new BigDecimal(lId));
		 lPenMod.setIdPenaResidua( getRequestBigDecimalParameter( CAMPO_ID_PENA_RESIDUA) );
		 lPenMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
		 lPenMod.setDataFine( getRequestDateParameter( CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
		 lPenMod.setNumAnniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
		 lPenMod.setNumMesiReclusione( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
		 lPenMod.setNumGiorniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );
		 lPenMod.setImportoMulta( getRequestBigDecimalParameter( CAMPO_IMPORTO_MULTA) );
		 lPenMod.setNumAnniArresto( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
		 lPenMod.setNumMesiArresto( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
		 lPenMod.setNumGiorniArresto( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
		 lPenMod.setImportoAmmenda( getRequestBigDecimalParameter( CAMPO_IMPORTO_AMMENDA) );
		 lPenMod.setDiesAQuo( getRequestStringParameter( CAMPO_DIES_A_QUO) );
		 lPenMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lPenMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lPenMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lPenMod.setDataAggiornamento(DateUtils.getSysDate());
		 lPenMod.setDataAggiornamento(DateUtils.getSysDate());
		 lPenMod.setEveIdEvento( getRequestBigDecimalParameter( CAMPO_EVE_ID_EVENTO) );
		 lPenMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

		 // chiama il controller
		 IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		 PenaResiduaModel llPenModRet = lCtrl.ExModificaPenaResidua(lPenMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("penaresidua", llPenModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penaresidua.action.ActLoadDettaglioPenaResidua&"+CAMPO_ID_PENA_RESIDUA+"="+llPenModRet.getIdPenaResidua().toString();
		 return lPage;
	 }



}