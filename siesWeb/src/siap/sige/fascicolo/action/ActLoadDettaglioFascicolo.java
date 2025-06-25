package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.note.controller.INote;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiestaatti.model.ParereModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.util.SigeMaggiorenniUtil;
import siap.sige.web.ActionSige;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadDettaglioFascicolo - Classe Action per la ricerca e visualizzazione del dettaglio del Fascicolo
 * SIGE
 *
 * @version 5.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioFascicolo extends ActionSige implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected FascicoloSigeEstesoModel mFascicoloEsteso = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): inizio");

		// Bottone di ritorno
		setLinkRitorno();

		// ==========================================================================
		// Verifico che il parametro CAMPO_ID_FASCICOLO_SIGE sia stato passato sulla
		// request.
		// Questo controllo è necessario in quanto questa funzione
		// può essere richiamata anche dal menù di scelta rapida
		// ==========================================================================
		BigDecimal lId = null;

		if (!isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIGE)) {
			lId = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIGE);
		} else if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
				// 20171016: [SG] aggiunto controllo preventivo
				&& ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
						.getFascicoloSige() != null) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			lId = ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
					.getIdFascicoloSige();
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSige.REDIRECT_FASCICOLO_RICERCATO;
		}

		// Ricerca Fascicolo
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		mFascicoloEsteso = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lId);

		if (mFascicoloEsteso == null || mFascicoloEsteso.getFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non trovato !");
		if (mFascicoloEsteso == null || mFascicoloEsteso.getRichiestaSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Richiesta assente !");

		setSessionAttribute("FascicoloSigeEsteso", mFascicoloEsteso);

		// Ricerca fascicoli unificati
		Vector lVectFas = null;
		if (mFascicoloEsteso.getFascicoloSige() != null
				&& mFascicoloEsteso.getFascicoloSige().getNumeroFascicoliUnificati() != null
				&& mFascicoloEsteso.getFascicoloSige().getNumeroFascicoliUnificati().intValue() > 0) {
			FascicoloSigeModel lFasRicModel = new FascicoloSigeModel();
			lFasRicModel.setFasSigIdFascicoloSige(lId);
			lVectFas = lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel);
		}
		setRequestAttribute("elencoFasUnificati", lVectFas);

		// Ricerca fascicolo unificante
		FascicoloSigeEstesoModel lUnificante = null;
		if (mFascicoloEsteso.getFascicoloSige() != null
				&& mFascicoloEsteso.getFascicoloSige().getFasSigIdFascicoloSige() != null) {
			BigDecimal lCodUnificante = mFascicoloEsteso.getFascicoloSige().getFasSigIdFascicoloSige();
			// In Sius per fascicoli ExtraUfficio Il Fascicolo Unificante può anche non esistere in archivio
			// In Sige al momento eseguo lo stesso tipo di ricerca
			if (CAMPO_CHIAVE_UFFICIO.compareTo(mFascicoloEsteso.getFascicoloSige().getChiaveUfficio()) == 0)
				lUnificante = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lCodUnificante);
			else
				lUnificante = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lCodUnificante);
			// lUnificante = lCtrl.ExRicercaFascicoloCollegato(lCodUnificante);
		}
		setRequestAttribute("fascicoloUnificante", lUnificante);

		// Ricerca fascicolo Origine/Primario
		FascicoloSigeModel lFascSigeOrigine = null;
		if (mFascicoloEsteso.getFascicoloSige() != null
				&& mFascicoloEsteso.getFascicoloSige().getIdFascicoloSigeOrigine() != null) {
			BigDecimal idFascSigeOrigine = mFascicoloEsteso.getFascicoloSige().getIdFascicoloSigeOrigine();
			lFascSigeOrigine = lCtrl.ExRicercaFascicoloSigeByKey(idFascSigeOrigine);
		}
		// Verifico se sul Fascicolo in sessione è stato inserito un ricorso
		// a cui è stato collegato un nuovo procedimento
		else if (mFascicoloEsteso.getFascicoloSige() != null
				&& mFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() != null) {
			lFascSigeOrigine = lCtrl
					.ExRicercaFascicoloCollegato(mFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
		}

		setRequestAttribute("fascicoloCollegato", lFascSigeOrigine);

		// Ricerca Tenori
		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector<TenoreSigeEstesoModel> lTenori = lTenCtrl
				.ExRicercaTenoreEstesoByRichiesta(mFascicoloEsteso.getRichiestaSige().getIdRichiestaSige());
		setRequestAttribute("TenoriSige", lTenori);

		// Ricerca Sentenze assegnate al Fascicolo (Ulteriori Titoli Esecutivi)
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(lId);
		setRequestAttribute("sentenze", lSentenze);

		if (lSentenze != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("N.ro Sentenze Assegnate -> " + lSentenze.size());

		// Ricerca Avvocati
		IAvvocato lAvvCtrl = SIGELookupRemote.getAvvocatoRemote();
		ArrayList lVectAvv = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lId);
		setRequestAttribute("avvocato", lVectAvv);

		// Ricerca Provvedimento Definitorio.
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvedimento = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(lId);

		if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null)
			setRequestAttribute("provvedimento", lProvvedimento.getProvvedimento());

		// Ricerca Provvedimenti del Fascicolo.
		// Vector lVectProv = lCtrlProv.ExRicercaProvvSigePerIdFasSigeTipiProvv(lId,
		// ICostantiProvvedimentoSige.TIPI_PROVVEDIMENTI);
		Vector<ProvvedimentoSigeEventoModel> lVectProv = lCtrlProv.ExRicercaProvvSigePerIdFasSigeTipiProvv(
				lId, ICostantiProvvedimentoSige.TIPI_PROVVEDIMENTI_DM);
		setRequestAttribute("provvedimenti", lVectProv);

		try {
			Vector lVectAltri = lCtrlProv.ExRicercaAltriProvvByFascicoloSige(lId,
					ICostantiProvvedimentoSige.COD_EVENTO_PROVV_RICH_ISTRUTTORIA);
			if (lVectAltri != null) {
				setRequestAttribute("provvedimentiAltri", lVectAltri);
			} else {
				siesLogger.info("provvedimentiAltri assente");
			}
		} catch (Exception e) {
			siesLogger.info("Nessun Elemento trovato");
		}

		// String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_ISTRUTTORIE + "'";
		// Vector lVectAtti = lCtrlProv.ExRicercaProvvSigePerIdFasSigeTipiProvv(lId, lTipiProvv);
		// setRequestAttribute("atti", lVectAtti);

		ParereModel parere = new ParereModel();
		parere.setIdFascicoloSige(lId);
		Vector<ParereModel> pareri = lCtrl.ExRicercaPareriPaginata(parere, -1);
		setRequestAttribute("pareri", pareri);

		// Elenco NOTE
		Vector lNoteVect = null;
		INote lNoteCtrl = SIUSLookupRemote.getNoteRemote();
		lNoteVect = lNoteCtrl.ExRicercaNoteFasSige(mFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
		setRequestAttribute("elencoNote", lNoteVect);

		// Ricerca dell'eventuale prima impugnazione valida per ciascun provvedimento
		// Iterator itx = lVectProv.iterator();
		// IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();
		// Vector <ImpugnazioneSigeModel>impugnazioniProvvedimento = new Vector<ImpugnazioneSigeModel>();
		// Vector <ImpugnazioneSigeModel>impugnazioniProvvedimenti = new Vector<ImpugnazioneSigeModel>();
		// while (itx.hasNext()) {
		// ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel) itx.next();
		// impugnazioniProvvedimento =
		// ctrIS.ExRicercaImpugnazioniProvvedimentoSige(lProvEve.getProvvedimento().getIdProvvedimentoSige());
		// if (impugnazioniProvvedimento != null && impugnazioniProvvedimento.size() > 0)
		// impugnazioniProvvedimenti.addElement(impugnazioniProvvedimento.firstElement());
		// }
		// setRequestAttribute("impugnazioni", impugnazioniProvvedimenti);
		// fine Ricerca

		// INIZIO emma@ 25/10/2016
		// DALLA LISTA DEI PROVVEDIMENTI RECUPERARE LE IMPUGNAZIONI DI TIPO RICORSO E
		// LEGGERE LA DATA DI DECISIONE DEL PROVVEDIMENTO IN MODO
		// DA FARLA VISUALIZZARE A VIDEO SULLA PAGINA DI DETTAGLIO PROCEDIMENTO SIGE
		if (mFascicoloEsteso != null && mFascicoloEsteso.getFascicoloSige() != null
				&& mFascicoloEsteso.getFascicoloSige().getCodStatoFascicolo().equals("16")) {
			if (lVectProv != null && !lVectProv.isEmpty()) {
				for (int i = 0; i < lVectProv.size(); i++) {
					ProvvedimentoSigeEventoModel provvSige = lVectProv.get(i);
					if (provvSige.getRicorsi() != null && provvSige.getRicorsi().size() > 0) {
						// se esistono ricorsi, prendo la prima impugnazione e la aggiungo come attributo
						// nella request
						ImpugnazioneSigeModel ultima_impugnazione = provvSige.getRicorsi().get(0);
						setRequestAttribute("ultima_impugnazione", ultima_impugnazione);
					}
				}
			}
		}

		// FINE emma@ 25/10/2016

		// Posizione Materiale Fascicolo
		IPosizioneMaterialeFascSige lPosMatCtrl = SIGELookupRemote.getPosizioneMaterialeFascSigeRemote();
		Vector<PosizioneMaterialeFascModel> lPosizioniMat = lPosMatCtrl
				.ExRicercaPosizioneMaterialeFascAttiva(lId);
		if (lPosizioniMat != null && lPosizioniMat.size() > 0) {
			setRequestAttribute("posizione_materiale", lPosizioniMat.get(0));
		}

		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		EventoModel model = lFasCtrl.ExRicercaDataInvioAtti(lId);

		if (model != null)
			mFascicoloEsteso.setDataInvioAttiInArchivio(model.getDataInvioAtti());

		IDocumentoAllegato docCtrl = SIGELookupRemote.getDocumentoAllegatoController();
		DocumentoAllegatoModel foglioComplementare = docCtrl.ExRicercaFoglioComplementareByFascicolo(lId);
		if (foglioComplementare != null)
			mFascicoloEsteso.setDataCompilazioneFoglioComplementare(foglioComplementare.getDataEmissione());

		// Modificabilità e Cancellabilità
		if (IsFascicoloSigeModificabile()) {
			setRequestAttribute("Modificabile", "SI");
			setRequestAttribute("Cancellabile", "SI");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FASCICOLO MODIFICABILE");

		} else {
			setRequestAttribute("Modificabile", "NO");
			setRequestAttribute("Cancellabile", "NO");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FASCICOLO NON MODIFICABILE");

		}

		// MEV_57: aggiunta gestione etichetta minorenne
		// String etichettaEta = SigeMaggiorenniUtil.checkMinorenne(mFascicoloEsteso.getSoggetto(),
		// mFascicoloEsteso.getFascicoloSige());
		String etichettaEta = SigeMaggiorenniUtil.checkMinorenneEtichetta(mFascicoloEsteso);
		setRequestAttribute("etichettaEta", etichettaEta);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "ActLoadDettaglioFascicolo: fine");

		Vector lProcedimentoSiepDiCumulo = null;
		IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		if (getFascicoloSigeEstesoInSessione() != null
				&& getFascicoloSigeEstesoInSessione().getFascicoloSiep() != null
				&& getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep() != null) {

			// lProcedimentoSiepDiCumulo = lFasSenCtrl
			// .ExRicercaProcedimentoSiepDiCumulo(getFascicoloSigeEstesoInSessione().getFascicoloSiep()
			// .getIdFascicoloSiep());

			// Modifica del 28/11/2016 MEV_15_S4
			// La modifica si è resa necessaria per integrare la funzionalità
			// alla "Nuova Gestione del Cumulo" introdotta con la MEV_26
			lProcedimentoSiepDiCumulo = lFasSigeCtrl.ExRicercaSentenzeAssegnateFascicolo(
					getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
		}
		setRequestAttribute("ListaProcedimentoSiepDiCumulo", lProcedimentoSiepDiCumulo);

		// VERSIONE 11: modificato codice
		// Verifico se sul procedimento Siep è presente un cumulo,
		// in caso affermativo visualizzo il link "Cumulo"
		Vector lCumuloSiep = null;
		ICumulo lCtrCum = SIEPLookupRemote.getCumuloRemote();
		CumuloModel aModelCum = new CumuloModel();
		if (getFascicoloSigeEstesoInSessione().getFascicoloSiep() != null
				&& getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep() != null) {
			aModelCum.setFasSieIdFascicoloSiep(
					getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep());
			// lCumuloSiep = lCtrCum.ExRicercaCumulo(aModelCum);

			// Modifica del 28/11/2016 MEV_15_S4
			// La modifica si è resa necessaria per integrare la funzionalità
			// alla "Nuova Gestione del Cumulo" introdotta con la MEV_26
			// Vengono estratti dalla tabella Evento, tutti gli eventi legati al Fascicolo Siep,
			// che hanno COD_MOTIVO legati al cumulo
			lCumuloSiep = lCtrCum.ExRicercaEventoCumulo(
					getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep());

		}
		setRequestAttribute("lCumuloSiep", lCumuloSiep);

		// restituisce la jsp di VIEW
		return PG_LOAD_DETTAGLIOFASCICOLOSIGE;
	}

}