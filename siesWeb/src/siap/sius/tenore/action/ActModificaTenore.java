package siap.sius.tenore.action;


/**
* <p>Title: ActModificaTenore</p>
* <p>Description: Classe Action per la modifica di Tenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.util.F3BException;

public class ActModificaTenore extends ActionSiap implements ICostantiTenore
{
/**
* Azione di Modifica del Tenore
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws Exception
{

 		 String lId = getRequestStringParameter(CAMPO_ID_TENORE);
 		 // riempie il model
 		 TenoreModel lTenMod = new TenoreModel ();

 		 lTenMod.setIdTenore(new BigDecimal(lId));
		 lTenMod.setIdTenore( getRequestBigDecimalParameter( CAMPO_ID_TENORE) );
		 lTenMod.setCodEsitoTenore( getRequestStringParameter( CAMPO_COD_ESITO_TENORE) );
		 lTenMod.setData( getRequestDateParameter( CAMPO_ANNO_DATA,CAMPO_MESE_DATA,CAMPO_GIORNO_DATA) );
		 lTenMod.setCodMagistrato( getRequestStringParameter( CAMPO_COD_MAGISTRATO) );
		 lTenMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
		 lTenMod.setCodOggettoTenore( getRequestStringParameter( CAMPO_COD_OGGETTO_TENORE) );
		 lTenMod.setProgrTenore( getRequestBigDecimalParameter( CAMPO_PROGR_TENORE) );
		 // 05/11/2003 REWORKFascicoloGPModel.
		 //lTenMod.setFlagEsitoTenore( getRequestStringParameter( CAMPO_FLAG_ESITO_TENORE) );
		 lTenMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lTenMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lTenMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lTenMod.setDataAggiornamento(DateUtils.getSysDate());
		 lTenMod.setDataAggiornamento(DateUtils.getSysDate());
		 lTenMod.setGenPridGeneraleProcedimento( getRequestBigDecimalParameter( CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO) );
		 lTenMod.setDepOpidDepositoOrdinanzaPc( getRequestBigDecimalParameter( CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC) );
		 lTenMod.setImpIdImpugnazione( getRequestBigDecimalParameter( CAMPO_IMP_ID_IMPUGNAZIONE) );
		 lTenMod.setDepDecIdDepositoDecreto( getRequestBigDecimalParameter( CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO) );

		 // chiama il controller
		 ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
		 TenoreModel llTenModRet = lCtrl.ExModificaTenore(lTenMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("tenore", llTenModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.tenore.action.ActLoadDettaglioTenore&"+CAMPO_ID_TENORE+"="+llTenModRet.getIdTenore().toString();
		 return lPage;
	 }
}