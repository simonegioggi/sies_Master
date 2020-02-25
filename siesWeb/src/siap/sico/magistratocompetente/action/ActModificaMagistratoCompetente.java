package siap.sico.magistratocompetente.action;


/**
* <p>Title: ActModificaMagistratoCompetente</p>
* <p>Description: Classe Action per la modifica di MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaMagistratoCompetente extends ActionSiap implements ICostantiMagistratoCompetente
{
/**
* Azione di Modifica del MagistratoCompetente
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		// String lId = getRequestStringParameter(CAMPO_ID_MAGISTRATO_COMPETENTE);
 		 // riempie il model
 		 MagistratoCompetenteModel lMagMod = new MagistratoCompetenteModel ();

 		// lMagMod.setIdMagistratoCompetente(new BigDecimal(lId));
		 lMagMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
		 lMagMod.setDataFine( getRequestDateParameter( CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
		 lMagMod.setCodRuoloMagistrato( getRequestStringParameter( CAMPO_COD_RUOLO_MAGISTRATO) );
		 lMagMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lMagMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lMagMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lMagMod.setDataAggiornamento(DateUtils.getSysDate());
		 lMagMod.setDataAggiornamento(DateUtils.getSysDate());
		 lMagMod.setMagCodMagistrato( getRequestStringParameter( CAMPO_MAG_COD_MAGISTRATO) );
		 lMagMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

		 // chiama il controller
		 IMagistratoCompetente lCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		 MagistratoCompetenteModel llMagModRet = lCtrl.ExModificaMagistratoCompetente(lMagMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("magistratocompetente", llMagModRet);

		 String lPage = "";
		//-----GDV lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.magistratocompetente.action.ActLoadDettaglioMagistratoCompetente&"+CAMPO_ID_MAGISTRATO_COMPETENTE+"="+llMagModRet.getIdMagistratoCompetente().toString();
		 return lPage;
	 }



}