package siap.siep.penaresidua.action;


/**
* <p>Title: ActInserisciPenaResidua</p>
* <p>Description: Classe Action per l'inserimento di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciPenaResidua extends ActionSiap implements ICostantiPenaResidua
{
/**
* Azione di Inserimento del PenaResidua
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
 		 {
 		 PenaResiduaModel lPenMod = new PenaResiduaModel();
 		
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
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lPenMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		 lPenMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lPenMod.setDataInserimento(DateUtils.getSysDate());
		 lPenMod.setDataInserimento(DateUtils.getSysDate());
		 lPenMod.setEveIdEvento( getRequestBigDecimalParameter( CAMPO_EVE_ID_EVENTO) );
		 lPenMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

		 //---Aggiungere in SIEPLookupRemote il metodo getPenaResiduaRemote()

		 IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		 PenaResiduaModel llPenModRet = lCtrl.ExInserisciPenaResidua(lPenMod);		 // setta la risposta nella request
		 setRequestAttribute("penaresidua", llPenModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penaresidua.action.ActLoadDettaglioPenaResidua&"+CAMPO_ID_PENA_RESIDUA+"="+llPenModRet.getIdPenaResidua().toString();
		 return lPage;
	 }



}