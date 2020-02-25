package siap.siep.scadenzario.action;


/**
* <p>Title: ActInserisciScadenzario</p>
* <p>Description: Classe Action per l'inserimento di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciScadenzario extends ActionSiap implements ICostantiScadenzario
{
/**
* Azione di Inserimento del Scadenzario
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws Exception
 		 {
                FascicoloSiepModel lFasc = (FascicoloSiepModel)this.getSessionAttribute("fascicolo");
 		 ScadenzarioModel lScaMod = new ScadenzarioModel();

		// lScaMod.setIdScadenzario( getRequestBigDecimalParameter( CAMPO_ID_SCADENZARIO) );
		 lScaMod.setCodTipoScadenzario("01");
		 lScaMod.setDataInizioScadenza( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_SCADENZA,CAMPO_MESE_DATA_INIZIO_SCADENZA,CAMPO_GIORNO_DATA_INIZIO_SCADENZA) );
		 //lScaMod.setDataFineScadenza( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_SCADENZA,CAMPO_MESE_DATA_FINE_SCADENZA,CAMPO_GIORNO_DATA_FINE_SCADENZA) );

                 lScaMod.setDataFineScadenza(DateUtils.moveDateTo(getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_SCADENZA,CAMPO_MESE_DATA_INIZIO_SCADENZA,CAMPO_GIORNO_DATA_INIZIO_SCADENZA),java.util.Calendar.DAY_OF_MONTH,30));

                 lScaMod.setFlagVisto("N");
		 //lScaMod.setDataVisto( getRequestDateParameter( CAMPO_ANNO_DATA_VISTO,CAMPO_MESE_DATA_VISTO,CAMPO_GIORNO_DATA_VISTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lScaMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		 lScaMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
                 lScaMod.setDataInserimento(DateUtils.getSysDate());
		// lScaMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( lScaMod.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
                 lScaMod.setFasSieIdFascicoloSiep(lFasc.getFasSieIdFascicoloSiep());
		 //---Aggiungere in SIEPLookupRemote il metodo getScadenzarioRemote()

		 IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		 ScadenzarioModel llScaModRet = lCtrl.ExInserisciScadenzario(lScaMod);		 // setta la risposta nella request
		 setRequestAttribute("scadenzario", llScaModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.scadenzario.action.ActLoadDettaglioScadenzario&"+CAMPO_ID_SCADENZARIO+"="+llScaModRet.getIdScadenzario().toString();
		 return lPage;
	 }



}