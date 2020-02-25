package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadModificaUdienzaMonocraticaSige
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di UdienzaMonocraticaSige
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
public class ActLoadModificaUdienzaMonocraticaSige extends ActUdienzaMonocraticaSige
		implements ICostantiUdienzaMonocraticaSige, ICostantiUdienzaSige, ICostantiCollegio {

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

		String cod_magis = null;
		if (this.getParameter(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS) != null) {
			cod_magis = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS);
			setRequestAttribute(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS, cod_magis);
		}

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
		setRequestAttribute("udienzamonocraticasige", mUdienzaSige);

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();

		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		// devo individuare tutti i fascicoli SIGE che puntanto all'udienza che sto modificando
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		// lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza( lIdUdienzaSige, STATO_FASCICOLO,
		// FLAG_MODIF_BLOCCO);

		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
		if (cod_magis != null) {
			lVect = lCtrlUdPr.ExRicercaProcedimentixUdienzaOrOrdinanza(lIdUdienzaSige, null, "ND", "TUTTI",
					null, cod_magis);
		} else if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// provenienza dalla pop up
			lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(lIdUdienzaSige, STATO_FASCICOLO, null);
		} else {
			// da funzioni di supporto
			lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(lIdUdienzaSige, STATO_FASCICOLO,
					FLAG_MODIF_BLOCCO);
		}
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());
		setRequestAttribute("numProcePerUdienza", Integer.toString(lVect.size()));

		// Se L'utente connesso appartiene ad un ufficio distaccato
		// s'imposta come codice comune i primi 6 caratteri del codice distretto.
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("TRIBSD"))
			lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);

		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		IMagistrato lCtrlM = SIGELookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption.setSelected(mUdienzaSige.getCodGiudice());
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoGiudici", "" + lOption);

		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		// recupero LA LISTA DEI MAGISTRATI ASSEGNATARI
		MagistratoAssegnatarioModel magAssCorrente = null;
		IMagistratoAssegnatario lCtrlA = SIGELookupRemote.getMagistratoAssegnatarioRemote();
		String codMagAss = "-";
		Option lOptionMagAss = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOptionMagAss.setAddBlankItem(Option.BLANK_ITEM);
		lOptionMagAss.setValueBlankItem("-");
		// recupero il magistrato assegnatario legato al fascicolo in SESSIONE solo se NON PROVENGO DA
		// FUNZIONI AMMINISTRATIVE
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
					&& getFascicoloSigeEstesoInSessione().getFascicoloSige() != null) {
				magAssCorrente = lCtrlA.ExRicercaMagAssCorrenteXFascicolo(
						getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
				codMagAss = magAssCorrente.getMagCodMagistrato();
				lOptionMagAss.setSelected(codMagAss);
				setRequestAttribute("giudiceSelezionato", codMagAss);
			}
		}
		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		setRequestAttribute("elencoMagAsseg", "" + lOptionMagAss);

		// Crea lista Elenco procuratori.
		// lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio));
		// 20171003: [EC] aggiunto blank item nella lista dei procuratori
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");

		// il procuratore lo recupero da magistrato_assegnatario
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO) && magAssCorrente.getCodProcuratore() != null) {
			if (magAssCorrente.getCodProcuratore() != null) {
				MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCodETipoUfficio(
						magAssCorrente.getCodProcuratore(), lCodTipoUfficio, lCodComune);
				if (lMagMod != null)
					lOption.setSelected(lMagMod.getCodMagistrato());
			}
		}

		// QUANDO LA SIZE è ZERO , IL CODICE PROCURATORE LO DEVO PRENDERE DALL'OGGETTO UDIENZA
		if (lVect.size() == 0)
			lOption.setSelected(
					mUdienzaSige.getCodProcuratore() != null ? mUdienzaSige.getCodProcuratore() : "");
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// ***** Crea lista elenco assistenti *****
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		// il cancelliere lo recupero da magistrato_assegnatario
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO) && magAssCorrente.getIdAssistente() != null) {
			if (magAssCorrente.getIdAssistente() != null) {
				// chiama il controller
				IAssistenteGiudiziario lCtrlAs = SICOLookupRemote.getAssistenteGiudiziarioRemote();
				AssistenteGiudiziarioModel llAssMod = lCtrlAs
						.ExRicercaAssistenteGiudiziarioByKey(magAssCorrente.getIdAssistente());
				if (llAssMod != null)
					lOption.setSelected(llAssMod.getIdAssistenteGiudiziario().toString());
			}
		}
		// QUANDO LA SIZE è ZERO , IL CODICE del cancelliere LO DEVO PRENDERE DALL'OGGETTO UDIENZA
		if (lVect.size() == 0)
			lOption.setSelected(
					mUdienzaSige.getCodIdAssistente() != null ? mUdienzaSige.getCodIdAssistente().toString()
							: "");
		setRequestAttribute("elencoAssistenti", "" + lOption);

		// Elenco delle sezioni.
		// 20171016: [SG] ricerca puntuale delle sezioni per magistrato ed ufficio
		String lSezioneUdienza = "";

		if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
				&& getFascicoloSigeEstesoInSessione().getFascicoloSige() != null
				&& getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdSezione() != null)
			lSezioneUdienza = "" + getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdSezione();
		else
			lSezioneUdienza = "-";

		lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), lSezioneUdienza,
				Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");

		if (mUdienzaSige.getCodIdSezioneUdienza() != null)
			lOption.setSelected(mUdienzaSige.getCodIdSezioneUdienza().toString());
		setRequestAttribute("elencoSezioni", "" + lOption);

		// imposto anche i dati dell'aula
		if (mUdienzaSige != null && mUdienzaSige.getCodIdAulaUdienza() != null) {
			AulaUdienzaModel aulaModel = mUdienzaSige.getAulaUdienzaModel();
			setRequestAttribute("aulaUdienza", aulaModel);
		}

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return getInsViewJSP();
	}

}