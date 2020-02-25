package siap.siep.modulocumulo.action;

import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.util.ModuloCumuloUtils;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di inserimento/modifica dei dati del provvedimento finale di cumulo Viene caricata come ultima
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciProvvedimentoCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaModel = */super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();

		// Vector ListaTitoli = super.getListaTitoli();
		// boolean lEscluso = false;
		// Iterator itx = ListaTitoli.iterator();
		// while (itx.hasNext()) {
		// TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) itx.next();
		// if (lTitoloModel != null && lTitoloModel.getIdTitoloCumulato() != null) {
		// if (lTitoloModel.getFlagEscluso() != null
		// && lTitoloModel.getFlagEscluso().compareTo("S") == 0) {
		// lEscluso = true;
		// }
		// }
		// }

		// if (lDatiAggregati.getProvvedimentoCumulo()!=null){
		// // Provvedimento già presente. Provengo del tasto AVANTI. Carico il dettaglio
		// String lPage="";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo&"
		// + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
		// +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		//
		// return lPage;
		// }

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Verifica se Presenti dati minimi per l'emissione del provv di cumulo
		// - pena rideterminata
		// - posizione giuridica
		// - pena residua
		// ==========================================================================
		/*
		 * if (lEscluso) { throw new SIEPException(SIEPException.USER_MESSAGE,
		 * "Uno o più Titoli iscritti in istruttoria risulta momentaneamente escluso. Per procedere alla emissione del provvedimento di Cumulo è necessario prima provvedere ad escludere o includere definitivamente detto Titolo."
		 * ); }
		 */
		if (lDatiAggregati.getPenaRideterminataCumulo() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Per procedere all'emissione del Provvedimento di Cumulo è necessario specificare prima le Pene Rideterminate");
		} else if (lDatiAggregati.getPosizioneGiuridicaCumulo() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Per procedere all'emissione del Provvedimento di Cumulo è necessario specificare prima la Posizione Guridica");
		} else if (lDatiAggregati.getPenaResiduaCumulo() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Per procedere all'emissione del Provvedimento di Cumulo è necessario procedere prima al Calcolo della Pena");
			// setRequestAttribute (IWebConstants.MESSAGE_TEXT,
			// "Per procedere all'emissione del Provvedimento di Cumulo è necessario procedere prima al
			// Calcolo della Pena");
			// return IWebConstants.PG_MESSAGE;
		}
		/*
		 * else if (lEscluso) { throw new SIEPException(SIEPException.USER_MESSAGE,
		 * "Uno o più Titoli iscritti in istruttoria risulta momentaneamente escluso. Per procedere alla emissione del provvedimento di Cumulo è necessario prima provvedere ad escludere o includere definitivamente detto Titolo."
		 * ); }
		 */
		// ==========================================================================
		// Verifica se Modifica o Inserimento
		// ==========================================================================
		// if (!isRequestParameterNullObj(MODALITA) &&
		// MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))){
		// // provengo del dettaglio e voglio andare in modifica
		// setRequestAttribute(MODALITA, MODALITA_MODIFICA);
		// }

		setRequestAttribute(MODALITA, getRequestStringParameter(MODALITA));

		// Combo provvedimenti
		// String lCodPG = lDatiAggregati.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica();

		DatiFinaliCumuloModel lDatiFinaliModel = lDatiAggregati.getDatiFinaliCumulo();
		boolean isGE = false;
		if ("03".equals(lDatiFinaliModel.getTipoUfficioEmissione())) {
			isGE = true;
		}

		boolean isNLP = false;
		PenaRideterminataCumuloModel lPenaResiduaC = lDatiAggregati.getPenaResiduaCumulo();
		if (!lPenaResiduaC.isReclusione() && !lPenaResiduaC.isArresto()) {
			// non c'è detentiva
			isNLP = true;
		}

		ModuloCumuloUtils lModCumUtil = new ModuloCumuloUtils(isGE, isNLP);

		Collection lCodiciProvv = DecodificheManager.getInstance().getMotiviProvvCumuloNew();
		Collection lCodiciDescAlt = lModCumUtil.getMotiviProvvCumuloNewAltDesc(lCodiciProvv);

		Option lComboMotivoProvv = new Option(lCodiciDescAlt);
		// lComboMotivoProvv.setFilter(lModCumUtil.getCodMotivoByPosGiu (lCodPG) );
		lComboMotivoProvv
				.setFilter(lModCumUtil.getCodMotivoByPosGiu(lDatiAggregati.getPosizioneGiuridicaCumulo()));

		if (lDatiAggregati.getProvvedimentoCumulo() != null)
			lComboMotivoProvv.setSelected(lDatiAggregati.getProvvedimentoCumulo().getEvento().getCodMotivo());
		setRequestAttribute("motivoProvvedimento", "" + lComboMotivoProvv);

		// ==========================================================================
		// Magistrato
		// ==========================================================================
		MagistratoModel lMagistrato = null;
		if (lDatiAggregati.getProvvedimentoCumulo() != null) {
			String lCodMag = lDatiAggregati.getProvvedimentoCumulo().getEvento().getCodMagistrato();
			IMagistrato lCtrlMagistrato = SICOLookupRemote.getMagistratoRemote();
			lMagistrato = lCtrlMagistrato.ExRicercaMagistratoByCod(lCodMag);
		} else {
			IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
			MagistratoCompetenteMagistratoModel lMagMod = null;
			lMagMod = lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
			if (lMagMod != null)
				lMagistrato = lMagMod.getMagistrato();
		}

		if (lMagistrato != null)
			setRequestAttribute("magistrato", lMagistrato);

		// ==========================================================================
		// Caricamento combo
		// ==========================================================================
		UfficioModel lUfficioTDS = null;
		UfficioModel lUfficioUDS = null;
		AutoritaEsternaModel lAutEst = null;
		AutoritaEsternaModel lUffRecCred = null;

		if (lDatiAggregati.getProvvedimentoCumulo() != null
				&& lDatiAggregati.getProvvedimentoCumulo().getNotifiche() != null) {
			int count = 0;

			NotificaModel[] lNotifiche = lDatiAggregati.getProvvedimentoCumulo().getNotifiche();

			while (count < lNotifiche.length) {
				NotificaModel lNotMod = lNotifiche[count];

				if (lNotMod.getUfficio() != null && (lNotMod.getUfficio().getCodTipoUfficio().equals("TDS")
						|| lNotMod.getUfficio().getCodTipoUfficio().equals("TDSM"))) {
					lUfficioTDS = lNotMod.getUfficio();
				} else if (lNotMod.getUfficio() != null
						&& (lNotMod.getUfficio().getCodTipoUfficio().equals("UDS")
								|| lNotMod.getUfficio().getCodTipoUfficio().equals("UDSM"))) {
					lUfficioUDS = lNotMod.getUfficio();
				} else if (lNotMod.getAutoritaEsterna() != null
						&& lNotMod.getAvvIdAvvocatoFascicoloSiep() == null) {
					AutoritaEsternaModel lAutModel = lNotMod.getAutoritaEsterna();

					if (lAutModel.getCodTipoAutorita().equals("36")
							|| lAutModel.getCodTipoAutorita().equals("37")
							|| lAutModel.getCodTipoAutorita().equals("57")) { // Ufficio recupero credito
						lUffRecCred = lAutModel;
					} else {
						lAutEst = lAutModel;
					}
				}

				count++;
			}
		}

		// TDS
		Option lComboUfficiTDS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUfficiTDS.setFilter(new String[] { "TDS", "TDSM" });
		if (lUfficioTDS != null)
			lComboUfficiTDS.setSelected(lUfficioTDS.getCodTipoUfficio());
		setRequestAttribute("comboTDS", "" + lComboUfficiTDS);

		// UDS
		Option lComboUfficiUDS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUfficiUDS.setFilter(new String[] { "UDS", "UDSM" });
		if (lUfficioUDS != null)
			lComboUfficiUDS.setSelected(lUfficioUDS.getCodTipoUfficio());
		setRequestAttribute("comboUDS", "" + lComboUfficiUDS);

		// Altra autorità
		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (lAutEst != null)
			lOptionAutoritaN.setSelected(lAutEst.getCodTipoAutorita());
		setRequestAttribute("comboAutoritaN", "" + lOptionAutoritaN);

		// Uffico per la notifica agli Avvocati
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		// setRequestAttribute("autoritaEsternaAvv", "" + lOption);
		// ==========================================================================
		// Ufficio Recupero Credito
		String lSelectedURC = "-";
		String lDescSedeURC = "";
		if (lDatiAggregati.getProvvedimentoCumulo() == null) {
			// Sono in inserimento provo a precaricare i dati dell'ufficio recupero
			// credito competente.
			SentenzaModel lSentenza = lFascMod.getSentenza();

			if (lSentenza.getCodTipoAutoritaEmittente().equals("CAP") // Corte Appello
					|| lSentenza.getCodTipoAutoritaEmittente().equals("CASAP") // Corte Assise Appello
					|| lSentenza.getCodTipoAutoritaEmittente().equals("CAPSM") // Corte Appello sezione
																				// minorenni
			) {
				lSelectedURC = "37"; // 37 - Ufficio Recupero Crediti presso la Corte D'Appello (attenzione
										// presente anche il 55)
			} else if (lSentenza.getCodTipoAutoritaEmittente().equals("CAS") // Corte di Assise
					|| lSentenza.getCodTipoAutoritaEmittente().equals("DIB") // Tribunale Ordinario
					|| lSentenza.getCodTipoAutoritaEmittente().equals("GIP") // Gip Presso il Tribunale
																				// Ordinario
					|| lSentenza.getCodTipoAutoritaEmittente().equals("GUP") // Gup Presso Tribunale Ordinario
			) {
				lSelectedURC = "36"; // 36 - Ufficio Recupero Crediti presso il Tribunale (attenzione presente
										// anche il 54)
			} else if (lSentenza.getCodTipoAutoritaEmittente().equals("DIBM") // Tribunale per i Minorenni
					|| lSentenza.getCodTipoAutoritaEmittente().equals("GIPM") // Gip Presso il Tribunale per i
																				// Minorenni
					|| lSentenza.getCodTipoAutoritaEmittente().equals("GUPM") // Gup Presso il Tribunale per i
																				// Minorenni
					|| lSentenza.getCodTipoAutoritaEmittente().equals("GUP") // Gup Presso Tribunale Ordinario
			) {
				lSelectedURC = "57"; // 57 - Ufficio Recupero Crediti presso il Tribunale dei Minori
			}

			if (!"-".equals(lSelectedURC))
				lDescSedeURC = lSentenza.getDescrLuogoEmittente();
		} else {
			// Sono in modifica, precarico l'ufficio recupero crediti solo se presente
			if (lUffRecCred != null) {
				lSelectedURC = lUffRecCred.getCodTipoAutorita();
				lDescSedeURC = lUffRecCred.getDescrSede();
			}
		}

		Option lComboUffRecCred = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lComboUffRecCred.setFilter(new String[] { "-", "36", "37", "57" });
		lComboUffRecCred.setSelected(lSelectedURC);

		// Ufficio Recupero Crediti
		setRequestAttribute("comboUffRecuperoCrediti", "" + lComboUffRecCred);
		setRequestAttribute("sedeUffRecuperoCrediti", lDescSedeURC);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// =============================
		// Invoco la Action di dettaglio
		// =============================
		return PG_LOAD_INSERISCI_PROVVEDIMENTO_CUMULO;
	}

}