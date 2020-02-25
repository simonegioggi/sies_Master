package siap.siep.calcolopena.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadEmissioneProvvedimento extends ActionSiap
		implements ICostantiOrdineEsecuzione, ICostantiAnnotazioneManuale {
	/**
	 * Action per la Load della form di emissione ORDINE di ESECUZIONE a seguito rideterminazione pena Questa
	 * action viene invocata dalla: - VediCalcoloPenaValidataAnnotazioni.jsp con flagPage = GE (decisioni del
	 * GE) - VediCalcoloPenaValidataAnnotazioniComputo.jsp con flagPage = RP (ridet pena
	 * Presofferto/Fungibilità) - DettaglioEmissioneProvvedimento.jsp con flagPage = ??
	 * 
	 * Oppure direttamente dalla voce di menu: Ordini di esecuzione/Scarcerazione - Ordine esecuzione per
	 * rideterminazione della pena in questo caso flagPage non viene passato
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Verifico se Archiviato/definito
		// ==========================================================================
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " Non si può chiedere un ordine di esecuzione di carcerazione per una fascicolo archiviato.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Verifico se fascicolo validato
		// ==========================================================================
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " Non si può richiedere un ordine di esecuzione per una fascicolo non Validato.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Controllo Esistenza pena residua per quel fascicolo
		// ==========================================================================
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile eseguire l'ordine di esecuzione.");

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		}

		// ==========================================================================
		// Controllo esistenza almeno un avvocato per fascicolo.
		// ==========================================================================
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE, e.getMessage()
					+ " Impossibile eseguire l'Ordine di Esecuzione. Inserire almeno un avvocato");
		}

		// ==========================================================================
		// Recupero la posizine giuridica
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Recupera l'evento con data emissione più recente (data inserimento/idEvento)
		// indipendentemente dallo stato (validato o meno)
		// Verifico se trattasi di Presofferto o Fungibilità
		// ==========================================================================
		EventoModel lEveMod = new EventoModel();
		IEventoSimeone lCtr = SICOLookupRemote.getEventoSimeoneRemote();
		lEveMod = lCtr.ExRicercaEventoByFascicoloSiepDesc(lFascMod.getIdFascicoloSiep());

		// Se l'evento NON è un Provvedimento (01-04) di computo Presofferto/Fungibilità
		// rilancio l'eccezione
		if (!(lEveMod != null && lEveMod.getIdEvento() != null && lEveMod.getCodMotivo() != null
				&& lEveMod.getCodTipoEvento() != null && lEveMod.getCodTipoEvento().equals("01")
				&& lEveMod.getCodTipoProvvedimento() != null && lEveMod.getCodTipoProvvedimento().equals("04")
				&& (lEveMod.getCodMotivo().equals("0121") // computo Misura Cautelare stesso Reato art. 657
															// c.p.p. (Presofferto)
						|| lEveMod.getCodMotivo().equals("0212") // computo Misura Cautelare Altro Reato art.
																	// 657 c.p.p. (fungibilità altro reato
																	// Misura Cautelare)
						|| lEveMod.getCodMotivo().equals("0213") // computo Pena Detentiva Espiata per Altro
																	// Reato (fungibilità) art. 657 c.p.p.
																	// (fungibilità altro reato Pena
																	// Detentiva)
				// || lEveMod.getCodMotivo().equals("0284") // Applicazione Amnistia / Indulto
				// || lEveMod.getCodMotivo().equals("0285") // Applicazione depenalizzazione
				// || lEveMod.getCodMotivo().equals("0286") // Applicazione incostituzionalita'
				))) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Non esiste alcuna annotazione manuale");
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"L'ultimo provvedimento inserito non è un computo di Presofferto/Fungibilità");
		}

		// Passo l'id dell'evento di computo
		setRequestAttribute("aIdEventoComputo", "" + lEveMod.getIdEvento());

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// ==========================================================================
		//
		// ==========================================================================
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			if (lPos != null && lPos.getAltraCausa() != null
					&& lPos.getAltraCausa().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
		} else {
			if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("16")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("20")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("46")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("47")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos != null && lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}

		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// ==========================================================================
		//
		// ==========================================================================
		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// MAGISTRATO
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// AVVOCATI
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;

		lDataInizioPena = llPenMod.getDataInizio();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		/*
		 * String lAnnotazioni = this.getRequestStringParameter("annotazioni"); IDecodifiche lCtrlDec =
		 * SICOLookupRemote.getDecodificheRemote(); DecodificheModel lDecMod =
		 * lCtrlDec.ExRicercaDecodificheProvvAnnMan(lAnnotazioni); setRequestAttribute("annotazioni",lDecMod);
		 */
		if (!this.isRequestParameterNullObj("flagPage")) {
			this.setRequestAttribute("flagPage", this.getRequestStringParameter("flagPage"));
		} else { // Provengo dalla voce di menù 'Ordine di Esecuzione/Scarcerazione - Ordine esecuzione per
					// rideterminazione pena'
					// in questo caso verifico che l'evento sia validato
			this.isEventoNonValidato();
		}

		return PG_LOAD_EMISSIONE_PROVVEDIMENTO; // restituisce la jsp di VIEW
	}
}