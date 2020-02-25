package siap.siep.penapresunta.action;


/**
* <p>Title: ActModificaPenaPresunta</p>
* <p>Description: Classe Action per la modifica di PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penapresunta.model.PenaPresuntaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaPenaPresunta extends ActionSiap implements ICostantiPenaPresunta
{
/**
* Azione di Modifica del PenaPresunta
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException 
{

 		 String lId = getRequestStringParameter(CAMPO_ID_PENA_PRESUNTA);
 		 // riempie il model
 		 PenaPresuntaModel lPenMod = new PenaPresuntaModel ();

 		 lPenMod.setIdPenaPresunta(new BigDecimal(lId));
		 lPenMod.setIdPenaPresunta( getRequestBigDecimalParameter( CAMPO_ID_PENA_PRESUNTA) );
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
		 lPenMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
		 lPenMod.setDataFineReclusione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_RECLUSIONE,CAMPO_MESE_DATA_FINE_RECLUSIONE,CAMPO_GIORNO_DATA_FINE_RECLUSIONE) );
		 lPenMod.setDataInizioArresto( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_ARRESTO,CAMPO_MESE_DATA_INIZIO_ARRESTO,CAMPO_GIORNO_DATA_INIZIO_ARRESTO) );

		 // chiama il controller
		 IPenaPresunta lCtrl = SIEPLookupRemote.getPenaPresuntaRemote();
		 PenaPresuntaModel llPenModRet = lCtrl.ExModificaPenaPresunta(lPenMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("penapresunta", llPenModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penapresunta.action.ActLoadDettaglioPenaPresunta&"+CAMPO_ID_PENA_PRESUNTA+"="+llPenModRet.getIdPenaPresunta().toString();
		 return lPage;
	 }



}