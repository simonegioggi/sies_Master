package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Ordinanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioOrdinanza extends ActLoadEmissioneOrdinanza implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected ProvvedimentoSigeEventoModel mProvEvento = null;

	// Funzione per la costruzione della combo con i template di stampa previsti
	private void gestioneTemplate(BigDecimal aIdEvento, String codTipoProvv) throws Exception {

		Option lOptTemplate = null;

		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimentoSige(codTipoProvv);

		// 01/06/2010 Valorizzazione di FLAG_TEMPLATE come ulteriore filtro di ricerca.
		if (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() != null
				&& mFasEsteso.getFascicoloSige().getCodTipoGiudizio() != "-")
			lTempRic.setFlagTemplate(mFasEsteso.getFascicoloSige().getCodTipoGiudizio());

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

		if (lTemplate != null && lTemplate.size() > 0) {
			lOptTemplate = new Option(lTemplate);
			setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		}
		return;
	}

	private void ModificabileStampabile(ProvvedimentoSigeEventoModel mProvEvento,
			Vector<TenoreSigeEstesoModel> lTenori) throws Exception {

		String lStampabile = "NO";
		String lModificabile = "NO";
		String lCancellabile = "NO";
		String lUpload = "SI";
		// Fascicolo Modificabile e Provvedimento non validato
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ---------------- IsFascicoloSigeModificabile = "+ IsFascicoloSigeModificabile()
		// );
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ---------------- IsFascicoloSigeIscrittoCompetenza = "+
		// IsFascicoloSigeIscrittoCompetenza() );
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ---------------- IsFascicoloSigeDiCompetenza = "+ IsFascicoloSigeDiCompetenza()
		// );
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ---------------- FlagDocumentoRegistrato = "+
		// mProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() );
		if (IsFascicoloSigeDiCompetenza()
				&& (mProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null || mProvEvento
						.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
			lStampabile = "SI";
			lModificabile = "SI";
			// Cancellabile se il provvedimento non è stato mai depositato (possibile sia stato svalidato)
			if ((mProvEvento.getProvvedimento().getDataDeposito() == null))
				lCancellabile = "SI";

			// Solo se tutti gli oggetti sono definiti la stampa è possibile.

			for (TenoreSigeEstesoModel lTenoreEsteso : lTenori) {
				TenoreSigeModel lTenore = lTenoreEsteso.getTenoreSige();
				if (lTenore.getCodEsitoSige() == null) {
					// lStampabile = "NO";
					lUpload = "NO";
					break;
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Modificabile : " + lModificabile);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Stampabile : " + lStampabile);

		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("Cancellabile", lCancellabile);
		setRequestAttribute("Upload", lUpload);
	}

	private boolean IsFascicoloSigeDiCompetenza() throws F3BException {

		boolean lRet = false;
		if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mFasEsteso.getFascicoloSige().getChiaveUfficio()))
			lRet = true;
		return lRet;
	}

	private void ricercaCollegio(BigDecimal aIdCollegio) throws Exception {

		// Chiama il controller.
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColMod = lCtrl.ExRicercaCollegioByKey(aIdCollegio);

		//
		// Decisione del ruolo magistrato in virtù del tipo ufficio.
		//
		String lRuoloMagistrato = "Giudice";

		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAPSM")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIBM"))
			lRuoloMagistrato = "Consigliere";

		// Passaggio alla request.
		setRequestAttribute("collegio", lColMod);
		setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);
	}

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioOrdinanza: inizio");
		BigDecimal lIdProvvedimento = null;

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della jsp.
		String lRetPage = PG_DETTAGLIO_ORDINANZA;

		letturaFascicoloEstesoinSessione();

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		mProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// Modifica del 08/03/2017 INIZIO *******************
	    // preleva i dati della udienza sige
	    if(mProvEvento.getProvvedimento()!=null && mProvEvento.getProvvedimento().getUdiIdUdienzaSige()!=null){
	        IUdienzaSige ctrlUdiSige=SIGELookupRemote.getUdienzaSigeRemote();
	        UdienzaSigeModel udienza=ctrlUdiSige.ExRicercaUdienzaSigeById(mProvEvento.getProvvedimento().getUdiIdUdienzaSige());
	        mProvEvento.getProvvedimento().setUdienzaSige(udienza);
	    }
		// Modifica del 08/03/2017 FINE *******************

		// setRequestAttribute("provvedimento", lProvvedimento);
		setRequestAttribute("ProvvedimentoEvento", mProvEvento);

		// Nel caso di Ordinanza generica verifico la presenza di eventuli sospensioni e ne passo il vettore
		if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_GENERICA)) {

			Vector ordSospensione = lCtrlProv.ExRicercaOrdinanzaSospensioneSigeByProvvId(lIdProvvedimento);
			Iterator itx = ordSospensione.iterator();
			while (itx.hasNext()) {
				ProvvedimentoSigeModel provvSospensione = (ProvvedimentoSigeModel) itx.next();
				if (provvSospensione == null || provvSospensione.getChiaveAnno() == null
						|| provvSospensione.getChiaveProgr() == null
						|| provvSospensione.getDataDeposito() == null)
					continue;
			}
			setRequestAttribute("ordinanzeSospensione", ordSospensione);
		}

		// Ricerca tenori legati al provvedimento
		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setProvIdProvvedimentoSige(lIdProvvedimento);

		// Per Ordinanza di Sospensione si agganciano i tenori dell'ordinanza sospesa
		if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige().compareTo(COD_ORDINANZA_SOSPENSIONE) == 0
				&& mProvEvento.getProvvedimento().getProvvIdProvvedimentoSige() != null) {
			lTenore.setProvIdProvvedimentoSige(mProvEvento.getProvvedimento().getProvvIdProvvedimentoSige());
		}

		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = lTenCtrl
				.ExRicercaTenoriEstesiByIdProvvedimento(lIdProvvedimento);
		// 20190508 [SG]: recupero tenori legati al procedimento
		// if (lTenoriEstesi != null && lTenoriEstesi.size() == 0) {
		// TenoreSigeModel tsm = new TenoreSigeModel();
		// tsm.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// lTenoriEstesi = lTenCtrl.ExRicercaTenoriEstesiAttivi(tsm);
		// }
		TenoriSigeUtil tsu = new TenoriSigeUtil();
		Vector<TenoreSigeModel> lTenori = tsu.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);

		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("tenoriEstesi", lTenoriEstesi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" --------- SIZE TENORI = " + lTenori.size());
		ricercaAvvocatiMagistrato();

		// Imposta gli oggetti nella request.
		setRequestAttribute("IdEvento", mProvEvento.getEventoNotifica().getEvento().getIdEvento());
		if (mProvEvento.getEventoNotifica().getEvento().getCodEsito() != null) {
			gestioneTemplate(mProvEvento.getEventoNotifica().getEvento().getIdEvento(), mProvEvento
					.getProvvedimento().getCodTipoProvvedimentoSige());
		}

		// Valorizzazione eventuale bottone di ritorno.
		if (!this.isRequestParameterNullObj("acdest")) {
			setRequestAttribute("acdest", getRequestStringParameter("acdest"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getRequestStringParameter("acdest"));
		}

		// Impostazione parametri di controllo gestione.
		ModificabileStampabile(mProvEvento, lTenoriEstesi);

		// Collegio
		if (mProvEvento != null && mProvEvento.getProvvedimento() != null
				&& mProvEvento.getProvvedimento().getColIdCollegio() != null) {
			ricercaCollegio(mProvEvento.getProvvedimento().getColIdCollegio());
		}

		if (mFasEsteso.getUdienzaProcedimento() != null
				&& mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige() != null) {
			caricaDatiUdienza(mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());

			// il tipo giudizio va definito quando si definisce l'udienza
			if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
				// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
				lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
						: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
			}
		}
		
		setRequestAttribute("tipoGiudizioVal", (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
				: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim()));
		Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, getUfficioUtenteConnesso().getCodTipoUfficio());
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());
		
		// [EC] - 20171031 : intervento per risoluzione anomalia segnalata da SIGE - TRIBUNALE TERAMO
		// emissione ordinzanza di incompetenza(email Maffucci del 27102017).
		setRequestAttribute("idProvvedimentoTitoliEsecutivi", lIdProvvedimento);

		// Switch su tipo di Ordinanza
		if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_NDPNLP))
			lRetPage = PG_DETTAGLIO_ORDINANZA_NDPNLP;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_INCOMPETENZA))
			lRetPage = PG_DETTAGLIO_ORDINANZA_INCOMPETENZA;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_DECRETO_INAMMISSIBILITA))
			lRetPage = PG_DETTAGLIO_DECRETO_INAMMISSIBILITA;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_SOSPENSIONE))
			lRetPage = PG_DETTAGLIO_ORDINANZA_SOSPENSIONE;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_CONFLITTO_COMPETENZA))
			lRetPage = buildLinkDettaglioOrdinanzaConflittoCompetenza(lIdProvvedimento);

		// Modifica del 08/03/2017
		// Combo per la definizione del tipo Giudizio.
		setComboTipoGiudizio();
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioOrdinanza: -> page: " + lRetPage);
		return lRetPage; // restituisce la jsp di VIEW
	}

	private String buildLinkDettaglioOrdinanzaConflittoCompetenza(BigDecimal idProvvedimento) {

		// siap.sige.provvInterlocutori.action.ActDettaglioOrdinanzaConflittoCompetenza
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvInterlocutori.action.ActDettaglioOrdinanzaConflittoCompetenza");
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE, (idProvvedimento.toString()));
		return lRedirigi.toString();
	}

}