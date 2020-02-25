package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActCancellaUdienzaMonocraticaSige</p>
 * <p>Description: Classe Action per la cancellazione di UdienzaMonocraticaSige</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */
public class ActCancellaUdienzaMonocraticaSige extends ActionSige implements ICostantiUdienzaSige, ICostantiCollegio {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione per la cancellazione dei dati. 
	 * <p>
	 * @return PG_MESSAGE di avvenuta cancellazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// ==========================================
		// Recupera la key del record da Cancellare
		// ==========================================
		BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

		// ==========================================
		// Istanzia il model
		// ==========================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setIdUdienzaSige(lIdUdienzaSige);
		
		// ======================================================
		// INIZIO Gestione per la cancellazione di un udienza monocratica
		// ======================================================
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		Collection<ProcedimentixUdienzaModel> lVect = new Vector<ProcedimentixUdienzaModel>();	
		String cod_magis = null;
		if (this.getParameter( ICostantiUdienzaSige.CAMPO_COD_MAG_ASS ) != null) {
			cod_magis = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS);
			setRequestAttribute(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS , cod_magis);
		}	
		if(cod_magis!=null){
			lVect = lCtrlUdPr.ExRicercaProcedimentixUdienzaOrOrdinanza(lIdUdienzaSige, null,
				"ND", "TUTTI", null, cod_magis);
		}
		else if(!isRequestParameterNullObj(FORM_DEF_COLLEGIO)){
			// provenienza dalla pop up 
			lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza( lIdUdienzaSige, STATO_FASCICOLO, null);
		}
		else{
			// da funzioni di supporto
			lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza( lIdUdienzaSige, STATO_FASCICOLO, FLAG_MODIF_BLOCCO);			
		}		
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());	

		if(lVect.size()>0){
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Cancellazione non effettuabile, per l'udienza indicata risultano procedimenti fissati! ");
		}
		// ======================================================
		// FINE Gestione per la cancellazione di un udienza monocratica
		// ======================================================

		// ======================================================
		// Recupera il Controller ed effettua la cancellazione
		// ======================================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		lCtrl.ExCancellaUdienzaSige(lUdiMod);

		// Specificare eventualmente la jump page dove verrà ridirezionata la
		// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
		// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
		// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
		// della root_dir es /null/frame.htm
		// Prepara la "pagina" di destinAction.
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// lRedirigi.setAction("siap.sige.udienza.action.ActLoadInserisciFissazioneUdienza");
			// lRedirigi.setParameter("ritorno", "ok");
			goToRitorno();
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sige.udienzamonocratica.action.ActLoadRicercaUdienzaMonocraticaSige");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		// ===========================================================
		// Restituisce la pagina di Conferma avvenuta Cancellazione.
		// ===========================================================
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione effettuata");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return IWebConstants.PG_MESSAGE;
	}

}