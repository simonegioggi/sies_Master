package siap.siep.calcolopena.action;

/**
 * <p>Title: ActInserisciCircostanza</p>
 * <p>Description: Classe Action per l'inserimento della Decisione del GE su
 * Depenalizzazione
 * Decisione del GE - Applicazione Benefici - Depenalizzazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciAnnotazioniManualiDepen extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// Controllo sulla Presenza Ordinanza
		boolean lOrdinanzaPresente = false;
		if (getRequestStringParameter("flagOrdinanza").equals("true")) {
			lOrdinanzaPresente = true;
		}

		BigDecimal IdReato = null;

		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		AnnotazioneManualeModel lAnnOrdMod = new AnnotazioneManualeModel();

		String lPage = "";

		if (!lOrdinanzaPresente) {
			if (getRequestStringParameter("annoGe").equals("")) {
				lAnnOrdMod.setAnnoGe(new BigDecimal(0));
			} else
				lAnnOrdMod.setAnnoGe(getRequestBigDecimalParameter("annoGe"));

			lAnnOrdMod.setNumeroGe(getRequestStringParameter("numeroGe"));
		}

		lAnnMod.setFlagValidato("N");

		lAnnMod.setCodFonte(getRequestStringParameter("CodFonte"));
		if (getRequestStringParameter("AnnoFonte").equals("")) {
			lAnnMod.setAnnoFonte(new BigDecimal(0));
		} else
			lAnnMod.setAnnoFonte(getRequestBigDecimalParameter("AnnoFonte"));

		lAnnMod.setNumeroFonte(getRequestStringParameter("NumeroFonte"));
		lAnnMod.setArticolo(getRequestStringParameter("Articolo"));
		lAnnMod.setCodSottonumerazione(getRequestStringParameter("CodSottonumerazione"));
		lAnnMod.setComma(getRequestStringParameter("Comma"));
		lAnnMod.setLettera(getRequestStringParameter("Lettera"));
		lAnnMod.setNumero(getRequestStringParameter("Numero"));

		lAnnMod.setCodDpr("-");
		lAnnMod.setCodCausaleComputo("-");

		/*
		 * if (getRequestStringParameter("annoSCC").equals("")) { lAnnMod.setAnnoCc(new BigDecimal(0)); } else
		 * lAnnMod.setAnnoCc(getRequestBigDecimalParameter("annoSCC"));
		 * lAnnMod.setNumeroCc(getRequestStringParameter("numeroSCC"));
		 */

		if (!lOrdinanzaPresente) {
			if (!getRequestStringParameter("DaAnArr").equals(""))
				lAnnOrdMod.setDataGE(DateUtils.getDate(getRequestStringParameter("DaAnArr"),
						getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr")));
		}

		String PM = getRequestStringParameter("PM");
		lAnnMod.setFlagPiuMeno(PM);

		// String tipoannotazione=getRequestStringParameter("tipoannotazione");
		// MEV 37 - Inizio
		String lCod = "";
		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE)) {
			lCod = getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE);
			lAnnMod.setCodTipoAnnotazione(
					getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE));
		}
		// MEV 37 - Fine

		if (getRequestStringParameter("TipoOrd").equals("Conforme"))
			lAnnMod.setFlagConforme("C");
		else if (getRequestStringParameter("TipoOrd").equals("Difforme"))
			lAnnMod.setFlagConforme("D");
		else
			lAnnMod.setFlagConforme("-");

		String noteRec = getRequestStringParameter("noteRec");
		lAnnMod.setNoteReclusione(noteRec);
		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAnnMod.setFlagAppProvvisoria("-");
		if (!isRequestParameterNullObj("IdReato")) {
			IdReato = getRequestBigDecimalParameter("IdReato");
			lAnnMod.setReaIdReato(IdReato);
		}

		// =========================================================
		// Recupero la Reclusione e la Multa
		// =========================================================
		String GRec = getRequestStringParameter("GRec");
		String MRec = getRequestStringParameter("MRec");
		String ARec = getRequestStringParameter("ARec");
		String Multa = getRequestStringParameter("Multa");
		String Multa_dec = getRequestStringParameter("Mul_dec");

		if (!ARec.equals(""))
			lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
		if (!MRec.equals(""))
			lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
		if (!GRec.equals(""))
			lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

		if (!Multa.equals("")) {
			if (!Multa_dec.equals("")) {
				lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			} else
				lAnnMod.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// =========================================================
		// Recupero la Arresti e Ammenda
		// =========================================================
		String GArr = getRequestStringParameter("GArr");
		String MArr = getRequestStringParameter("MArr");
		String AArr = getRequestStringParameter("AArr");
		String Ammenda = getRequestStringParameter("Ammenda");
		String Ammenda_dec = getRequestStringParameter("Amm_dec");

		if (!AArr.equals(""))
			lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
		if (!MArr.equals(""))
			lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
		if (!GArr.equals(""))
			lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

		lAnnMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		if (!isRequestParameterNullObj("IdReato")) {
			IdReato = getRequestBigDecimalParameter("IdReato");
			lAnnMod.setReaIdReato(IdReato);
		}
		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals("")) {
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			} else
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		// ***********************************
		String lCodTipoUfficioEmittente = "-";
		String lDescrLuogoEmittente = "-";
		String lCodUffEmi = "-";

		if (!lOrdinanzaPresente) {
			lAnnOrdMod.setIdAnnotazioneManuale(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE));

			lAnnOrdMod.setFasSieIdFascicoloSiep(lIdFascicolo);
			lAnnOrdMod
					.setMotivazioni(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI));
			lAnnOrdMod.setFlagValidato("S");
			lAnnOrdMod.setFlagConforme("-");
			lAnnOrdMod.setCodTipoAnnotazione("-");
			lAnnOrdMod.setCodDpr("-");
			lAnnOrdMod.setCodFonte("-");
			lAnnOrdMod.setCodSottonumerazione("-");
			lAnnOrdMod.setCodCausaleComputo("-");
			lAnnOrdMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAnnOrdMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAnnOrdMod.setDataInserimento(DateUtils.getSysDate());
			lAnnOrdMod.setAnnoGe(getRequestBigDecimalParameter("annoGe"));
			lAnnOrdMod.setNumeroGe(getRequestStringParameter("numeroGe"));
			lCodTipoUfficioEmittente = getRequestStringParameter("CodTipoUffEmi");
			lDescrLuogoEmittente = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);

			if (!lCodTipoUfficioEmittente.equals("-") && !lCodTipoUfficioEmittente.equals("")
					&& !lDescrLuogoEmittente.equals("") && !lDescrLuogoEmittente.equals("-")) {
				lCodUffEmi = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmittente,
						lDescrLuogoEmittente);
			}
		}

		boolean lInserireOrdinanza = false;
		if (lAnnOrdMod.getDataGE() != null && lAnnOrdMod.getAnnoGe() != null
				&& lAnnOrdMod.getNumeroGe() != null && lAnnOrdMod.getNumeroGe() != "") {
			lInserireOrdinanza = true;
		}

		/********************* Evento Ordinanza **************************************/

		EventoModel lEveOrdinanzaMod = new EventoModel();

		lEveOrdinanzaMod.setCodTipoEvento("01");
		lEveOrdinanzaMod.setCodTipoProvvedimento("03"); // ORDINANZA
		lEveOrdinanzaMod.setCodMotivo("0285");
		// --- Come devono essere gestiti i documenti di altri uffici simulati?
		// lEveOrdinanzaMod.setFlagDocumentoRegistrato("S");
		// --- il flag come deve essere gestito?
		lEveOrdinanzaMod.setFlagStampaSiep("S");
		lEveOrdinanzaMod.setFlagVideoSiep("S");
		lEveOrdinanzaMod.setCodUfficioEmittente(lCodUffEmi);
		lEveOrdinanzaMod.setCodLuogoEmittente(getCodComuneByDescr(lDescrLuogoEmittente).getCodComune());
		lEveOrdinanzaMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveOrdinanzaMod.setDataEmissione(lAnnOrdMod.getDataGE());

		/********************* Fine Evento Ordinanza *********************************/

		/********************* Evento Provvedmento ***********************************/
		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodTipoProvvedimento("04"); // PROVVEDIMENTO
		lEveProvvedimentoMod.setCodMotivo("0285");
		// ---NO---lEveProvvedimentoMod.setFlagDocumentoRegistrato("N");
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");
		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveProvvedimentoMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("yyyy"),
				DateUtils.getSysDate("MM"), DateUtils.getSysDate("dd")));

		/********************* Fine Evento Ordinanza *********************************/

		// MEV 37 - Inizio
		// carico la lista delle richieste (in realtà è sola UNA) selezionata dalla popup
		Vector lListaRichieste = new Vector();
		int lNumTotRichieste = getRequestIntParameter("NumTotRichieste");

		for (int i = 0; i < lNumTotRichieste; i++) {
			if (!isRequestParameterNullObj("cb_record_" + (i + 1))) {
				AnnotazioneManualeModel lAnnRichiesta = new AnnotazioneManualeModel();
				lAnnRichiesta.setIdAnnotazioneManuale(getRequestBigDecimalParameter("cb_record_" + (i + 1)));
				lListaRichieste.add(lAnnRichiesta);

			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("cb_record_" + (i + 1) + " is null");
		}

		// MEV 37 - Fine

		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();

		if (getRequestStringParameter("operazione").equals("Torna")) {
			lAnnManCtrl.ExInserisciAnnotazioneManuale(lAnnMod);

			lPage = IWebConstants.ROOT_DIR + "Main.jsp?Action=siap.siep.calcolopena.action.ActLoadGEDepen";
			return lPage;
		}

		if (getRequestStringParameter("operazione").equals("Quantum")) {
			// ******************************************************************************
			AnnotazioneManualeModel lAnnManIns = null;

			if (!lOrdinanzaPresente && lInserireOrdinanza) {
				// MEV 37 - Inizio
				if (lCod.equals("004") || lCod.equals("017")) {
					lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeProvvedimentoRichiesta(lAnnMod,
							lEveOrdinanzaMod, lEveProvvedimentoMod, lAnnOrdMod, lListaRichieste);
				} else {
					// 37 - Fine
					lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeProvvedimentoRichiesta(lAnnMod,
							lEveOrdinanzaMod, lEveProvvedimentoMod, lAnnOrdMod, null);
				}
			} else {
				lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveProvvedimentoMod);
			}

			setRequestAttribute("lFlagPage", "DEPEN");

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniManuali&"
					+ ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE + "="
					+ lAnnManIns.getIdAnnotazioneManuale();

			// lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadAnnotazioniManualiBenefici.jsp";
			return lPage;
		}

		return null;
	}

}