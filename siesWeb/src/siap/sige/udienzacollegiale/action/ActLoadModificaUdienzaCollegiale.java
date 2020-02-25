package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.util.SICOLookupRemote;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.util.CollegioUtils;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaUdienzaCollegiale
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di UdienzaCollegialeSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
* @version 1.0
*/
public class ActLoadModificaUdienzaCollegiale extends ActUdienzaCollegiale implements
		ICostantiUdienzaCollegiale, ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected UdienzaSigeModel mUdienzaSige = null;

	/*****************************************************************************
	 * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche di precaricare tutti i dati da
	 * visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// ==========================================
		// Recupera la key del record da modificare
		// ==========================================
		BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================

		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		mUdienzaSige = lCtrl.ExRicercaUdienzaSigeById(lIdUdienzaSige);
		
		// Modifica del 08/03/2017
		String popUp = "";
		if (!isRequestParameterNullObj("PopUp")) {
			popUp = getRequestStringParameter("PopUp");
		}
		setRequestAttribute("PopUp", popUp);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (mUdienzaSige == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /null/frame.htm
			setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("udienzasige", mUdienzaSige);

		// Inserire Eventuali ComboBOX precaricando i dati del model
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();
		Option lOption = null;

		// Inserire Eventuali ComboBOX
		// lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		// 20170926: [SG] aggiunto blank item nella lista dei procuratori
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio),
				Option.BLANK_ITEM);
		if (mUdienzaSige.getCodProcuratore() != null) {
			lOption.setSelected(mUdienzaSige.getCodProcuratore());
		}
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio),
				Option.BLANK_ITEM);
		if (mUdienzaSige.getCodIdAssistente() != null) {
			lOption.setSelected(mUdienzaSige.getCodIdAssistente().toString());
		}
		setRequestAttribute("elencoAssistenti", "" + lOption);

		// Elenco delle sezioni.
		// ripristino per sies 11.2.1
		lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()),
		Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		if (mUdienzaSige.getCollegio() != null && mUdienzaSige.getCollegio().getSezIdSezione() != null) {
			lOption.setSelected(mUdienzaSige.getCollegio().getSezIdSezione().toString());
		}
		else{
			lOption.setSelected("-");
		}		
		setRequestAttribute("elencoSezioni", "" + lOption);

		lOption = new Option(CollegioUtils.getElencoCodiciCollegi(), Option.BLANK_ITEM);
		if (mUdienzaSige.getCollegio() != null && mUdienzaSige.getCollegio().getCodCollegio() != null) {
			lOption.setSelected(mUdienzaSige.getCollegio().getCodCollegio());
		}
		setRequestAttribute("elencoCodiciCollegi", "" + lOption);

		// Elenco giudici popolari.
		GiudicePopolareModel lGiudicePopolare = new GiudicePopolareModel();
		lGiudicePopolare.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lGiudicePopolare.setMessage("dataFineisNull");

		Vector lGiudPopVect = null;
		try {
			IGiudicePopolare lGiudPopCtrl = SIGELookupRemote.getGiudicePopolareRemote();
			lGiudPopVect = lGiudPopCtrl.ExRicercaGiudicePopolare(lGiudicePopolare);
		} catch (Exception e) {
		}
		setRequestAttribute("elencoGiudiciPopolari", lGiudPopVect);
		
		// INTRODUCO PER 11.2.1
		Collection<ProcedimentixUdienzaModel> lVect = new Vector<ProcedimentixUdienzaModel>();		
		// devo individuare tutti i fascicoli SIGE che puntanto all'adienza che sto modificando
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();	
		lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza( lIdUdienzaSige, STATO_FASCICOLO, null);		
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());
		setRequestAttribute("numProcePerUdienza",Integer.toString(lVect.size()));
		setRequestAttribute("listaProcedimenti", lVect);
		
		
		//imposto anche i dati dell'aula
		if (mUdienzaSige != null && mUdienzaSige.getCodIdAulaUdienza() != null) {		
			AulaUdienzaModel aulaModel = mUdienzaSige.getAulaUdienzaModel();
			setRequestAttribute("aulaUdienza", aulaModel);
		}
				
		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// intervento per 11.2.1 recupero il magistrato assegnatario 
		MagistratoAssegnatarioModel lMagAss = null;
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso")){
			lMagAss  = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
	    }
		if(lMagAss != null)
			setRequestAttribute("magAssegnatario", lMagAss);
		
		//imposto anche i dati dell'aula
		if (mUdienzaSige != null && mUdienzaSige.getCodIdAulaUdienza() != null) {		
			AulaUdienzaModel aulaModel = mUdienzaSige.getAulaUdienzaModel();
			setRequestAttribute("aulaUdienza", aulaModel);
		}	
		
		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		
		mUdienzaSige.getColIdCollegio();
//        String page=getInsViewJSP();
		return getInsViewJSP();
	}
	
//	private String getListaSezioni() throws F3BException{
//		CollegioModel collegio = this.mUdienzaSige.getCollegio();
//		Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()),"-",
//				Option.BLANK_ITEM);
//		
//		// intervento per eliminare il messaggio Dati del Procedimento sige non in sessione!
//		MagistratoAssegnatarioModel lMagAss = null;
//		if (!isSessionAttributeNullObj("FascicoloSigeEsteso")){
//			 lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
//			if (collegio==null && lMagAss != null) {
//				IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
//				// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
//				// ufficio differente da quello in cui ha delle udienze poichè trasferito
//				MagistratoModel lMagMod = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
//						getCodUfficioUtenteConnesso());
//				if (lMagMod.getMagistratoSezioni().length > 0) {
//					BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
//					if (idSezMag != null) {
//						lOption.setSelected(idSezMag.toString());
//					}
//				}
//			}
//		}
//		
//		if (collegio != null && lMagAss != null) {
//			BigDecimal idSezione=collegio.getSezIdSezione();
//			if (idSezione == null) {
//				IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
//				// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da
//				// un ufficio differente da quello in cui ha delle udienze poichè trasferito
//				MagistratoModel lMagMod = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
//						getCodUfficioUtenteConnesso());
//				if (lMagMod.getMagistratoSezioni().length > 0) {
//					idSezione = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
//				}
//				
//			}
//			
//			// 20170908: [SG] non tutti gli uffici hanno le sezioni!
//			if (!Utils.isNullObj(idSezione))
//			lOption.setSelected(idSezione.toString());
//		}
//			
//		
//		return lOption.toString();
//	}

}