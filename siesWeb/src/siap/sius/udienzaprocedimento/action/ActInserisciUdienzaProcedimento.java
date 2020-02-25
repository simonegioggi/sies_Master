package siap.sius.udienzaprocedimento.action;


/**
* <p>Title: ActInserisciUdienzaProcedimento</p>
* <p>Description: Classe Action per l'inserimento di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciUdienzaProcedimento extends ActionSiap implements ICostantiUdienzaProcedimento
{
/**
* Azione di Inserimento del UdienzaProcedimento
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
 		 {
 		 UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
 		
		 lUdiMod.setIdUdienzaProcedimento( getRequestBigDecimalParameter( CAMPO_ID_UDIENZA_PROCEDIMENTO) );
		 lUdiMod.setFlagRinviata( getRequestStringParameter( CAMPO_FLAG_RINVIATA) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lUdiMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		 lUdiMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lUdiMod.setDataInserimento(DateUtils.getSysDate());
		 lUdiMod.setDataInserimento(DateUtils.getSysDate());
		 lUdiMod.setGenPridGeneraleProcedimento( getRequestBigDecimalParameter( CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO) );
		 lUdiMod.setUdiIdUdienza( getRequestBigDecimalParameter( CAMPO_UDI_ID_UDIENZA) );

		 //---Aggiungere in SIUSLookupRemote il metodo getUdienzaProcedimentoRemote()

		 IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		 UdienzaProcedimentoModel llUdiModRet = lCtrl.ExInserisciUdienzaProcedimento(lUdiMod);		 // setta la risposta nella request
		 setRequestAttribute("udienzaprocedimento", llUdiModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.udienzaprocedimento.action.ActLoadDettaglioUdienzaProcedimento&"+CAMPO_ID_UDIENZA_PROCEDIMENTO+"="+llUdiModRet.getIdUdienzaProcedimento().toString();
		 return lPage;
	 }



}