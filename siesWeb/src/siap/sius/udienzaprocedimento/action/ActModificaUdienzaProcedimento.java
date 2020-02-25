package siap.sius.udienzaprocedimento.action;


/**
* <p>Title: ActModificaUdienzaProcedimento</p>
* <p>Description: Classe Action per la modifica di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaUdienzaProcedimento extends ActionSiap implements ICostantiUdienzaProcedimento
{
/**
* Azione di Modifica del UdienzaProcedimento
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException 
{

 		 String lId = getRequestStringParameter(CAMPO_ID_UDIENZA_PROCEDIMENTO);
 		 // riempie il model
 		 UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel ();

 		 lUdiMod.setIdUdienzaProcedimento(new BigDecimal(lId));
		 lUdiMod.setIdUdienzaProcedimento( getRequestBigDecimalParameter( CAMPO_ID_UDIENZA_PROCEDIMENTO) );
		 lUdiMod.setFlagRinviata( getRequestStringParameter( CAMPO_FLAG_RINVIATA) );
		 lUdiMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lUdiMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lUdiMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
		 lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
		 lUdiMod.setGenPridGeneraleProcedimento( getRequestBigDecimalParameter( CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO) );
		 lUdiMod.setUdiIdUdienza( getRequestBigDecimalParameter( CAMPO_UDI_ID_UDIENZA) );

		 // chiama il controller
		 IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		 UdienzaProcedimentoModel llUdiModRet = lCtrl.ExModificaUdienzaProcedimento(lUdiMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("udienzaprocedimento", llUdiModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.udienzaprocedimento.action.ActLoadDettaglioUdienzaProcedimento&"+CAMPO_ID_UDIENZA_PROCEDIMENTO+"="+llUdiModRet.getIdUdienzaProcedimento().toString();
		 return lPage;
	 }



}