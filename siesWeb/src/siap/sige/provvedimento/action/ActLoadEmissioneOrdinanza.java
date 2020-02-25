package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per la load Emissione Ordinanza
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
public class ActLoadEmissioneOrdinanza extends ActRicercaFSigePuntuale implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected FascicoloSigeEstesoModel mFasEsteso;
	String lTipoGiudizio = "-";

	protected boolean letturaFascicoloEstesoinSessione() throws Exception {

		boolean lRet = true;

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();

		if (mFasEsteso.getFascicoloSige() == null
				|| mFasEsteso.getFascicoloSige().getIdFascicoloSige() == null) {
			lRet = false;
		} else {
			// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
			IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
			mFasEsteso = lCtrlFas
					.ExRicercaEstesaFascicoloSigeByKey(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
			setSessionAttribute("FascicoloSigeEsteso", mFasEsteso);
		}

		return lRet;
	}

	protected void ricercaAvvocatiMagistrato() throws Exception {

		// Ricerca Avvocati
		IAvvocato lCtrlAvv = SIGELookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lAvvocati = lCtrlAvv.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);

		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);
	}

	protected void caricaDatiUdienza(BigDecimal idUdienzaProcedimentoSige) throws Exception {

		setRequestAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
				idUdienzaProcedimentoSige.toString());

		// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
		IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel lUdiProSige = lUdiProCtrl
				.ExRicercaUdienzaProcedimentoSigeByKey(idUdienzaProcedimentoSige);

		if (lUdiProSige != null) {

			// Se trovato UDIENZA_PROCEDIMENTO_SIGE si ricava l'ID Udienza e l'ID Evento
			BigDecimal lIdEvento = lUdiProSige.getEveIdEvento();
			setRequestAttribute("IdUdienzaEvento", lIdEvento.toString());

			// IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			// eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

			// IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
			// provvSigeEveMod = lCtrlProv.ExRicercaProvvedimentoByIdEvento(lIdEvento);

			/*
			 * ISSUE MEV : Se esiste un Decreto di Fissazione Udienza non validato, non deve essere possibile
			 * emettere un Ordinanza. (Richiesta fatta da Nunzia) Numero MEV : 15_S4 Autore : Sessa Data :
			 * 01/mar/2017 Branch : MEV_15_S4
			 */
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel eventoModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
			if (eventoModel != null && eventoModel.getFlagDocumentoRegistrato() == null
					|| (eventoModel.getFlagDocumentoRegistrato() != null
							&& eventoModel.getFlagDocumentoRegistrato().equals("N"))) {
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Operazione non consentita, esiste un Decreto di Fissazione Udienza non validato!");
			}
			// ***** FINE INTERVENTO MEV_15_S4 *****//

			// chiama il controller
			IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();
			Vector lPartiC = lCtrl.ExRicercaPartiUdienzaByIdUdienza(idUdienzaProcedimentoSige, "C");
			setRequestAttribute("udienzaPartiC", lPartiC);

			Vector lPartiO = lCtrl.ExRicercaPartiUdienzaByIdUdienza(idUdienzaProcedimentoSige, "O");
			setRequestAttribute("udienzaPartiO", lPartiO);

			BigDecimal lIdUdienzaSige = lUdiProSige.getUdiIdUdienzaSige();
			if (lIdUdienzaSige != null) {
				String sIdUdienzaSige = lIdUdienzaSige.toString();
				setRequestAttribute("IdUdienzaSige", sIdUdienzaSige);
				setRequestAttribute("UdienzaSige", getUdienzaSige(sIdUdienzaSige));
			}

			// nel caso di rifissazione prelevo i dati della sezione e dell'aula dalla Udienza Sige associata
			// lSezioneUdienza = getIdSezioneUdienzaSige();
			// aulaUdienza = getAulaUdienzaSige(lSezioneUdienza);
		}

	}

	protected void loadDatiUdienzaSige() throws Exception {

		if (mFasEsteso.getUdienzaProcedimento() != null
				&& mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige() != null) {
			caricaDatiUdienza(mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
		} else if (!isRequestParameterNullObj(ICostantiCollegio.FORM_DEF_COLLEGIO)) {
			// Se nella request è flaggato il FORM_DEF_COLLEGIO siamo nel caso di un inserimento udienza
			// diretto
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
			String idUdienza[] = super.getRequest()
					.getParameterValues(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
			BigDecimal lIdUdienzaSige = new BigDecimal(idUdienza[idUdienza.length - 1]);

			String sIdUdienzaSige = lIdUdienzaSige.toString();
			UdienzaSigeModel udiSige = getUdienzaSige(sIdUdienzaSige);
			setRequestAttribute("UdienzaSige", udiSige);
		}
		// 20190519 [SG]: aggiunta gestione idUdienzaSige
		else if (!isRequestParameterNullEmptyObj("idUdiSig")) {
			UdienzaSigeModel udiSige = getUdienzaSige(getRequestStringParameter("idUdiSig"));
			setRequestAttribute("UdienzaSige", udiSige);
		}
	}

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneOrdinanza: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della pagina di view in caso di assenza provvedimento definitorio.
		String lRetPage = PG_LOAD_EMISSIONE_ORDINANZA;

		if (isRequestParameterNullObj("ritorno")) {
			// Solo se provengo da menù
			super.processRequest();
		}

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvedimento = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(
						mFasEsteso.getFascicoloSige().getIdFascicoloSige());

		// In caso di esistenza di Provvedimento Definitorio non annullato, oppure;
		// In caso di esistenza di Provvedimento Definitorio svalidato, si accede al suo Dettaglio.
		// Altrimenti si può emettere un altro provvedimento.
		// Modifica del 18/11/2016 MEV_15_S4
		// Aggiunto ulteriore controllo sullo stato del fascicolo: si può emettere un altro
		// provvedimento se lo stato del fascicolo è 14 (Opposizione - Accoglie (fissa l'udienza))
		// oppure 16 (Ricorso convertito in opposizione (Fissa Udienza))
		if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null
				&& lProvvedimento.getEventoNotifica().getEvento() != null
				&& (lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null
						|| (lProvvedimento.getEventoNotifica().getEvento()
								.getFlagDocumentoRegistrato() != null
								&& lProvvedimento.getEventoNotifica().getEvento()
										.getFlagDocumentoRegistrato() != "A"))
				&& (mFasEsteso.getFascicoloSige().getCodStatoFascicolo() != null && !mFasEsteso
						.getFascicoloSige().getCodStatoFascicolo().equals(COD_ACCOGLIE_FISSA_UDIENZA) &&
				// @emma 21082018 intervento post COLLAUDO 11.2 (è possibile emettere un altro provvedimento
				// se lo stato del fascicolo è 20 (Decreto Fissazione Udienza) oppure 21 (Ricorso convertito
				// in opposizione (Fissa Udienza))
						!mFasEsteso.getFascicoloSige().getCodStatoFascicolo()
								.equals(ICostantiFascicoloSige.COD_DECRETO_FISSAZIONE_UDIENZA)
						&& !mFasEsteso.getFascicoloSige().getCodStatoFascicolo()
								.equals(ICostantiFascicoloSige.COD_RICORSO_CONVERTITO_OPPOSIZIONE_UDI)
						&& !mFasEsteso.getFascicoloSige().getCodStatoFascicolo()
								.equals(COD_RICORSO_CONVERTITO_OPPOSIZIONE))) {
			// Prepara la pagina di destinazione, il Dettaglio Ordinanza.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"cod tipo provvedimento: " + lProvvedimento.getProvvedimento().getCodTipoProvvedimento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("tipo ordinanza : " + COD_ORDINANZA_GENERICA);

			if (lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
					&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
							.equalsIgnoreCase(COD_ORDINANZA_GENERICA))
				lPage.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanza");
			else
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Operazione non consentita, esiste già un Provvedimento di tipo definitorio!");

			lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE,
					"" + lProvvedimento.getProvvedimento().getIdProvvedimentoSige());
			lRetPage = lPage.toString();
		} else {
			// Se il Fascicolo non è in stato "iscritto" o equivalente" non è possibile emettere provvedimento
			if (!IsFascicoloSigeIscrittoCompetenza())
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Non è possibile emettere provvedimento per questo Procedimento!");

			// Solo la prima volta vengono messi i Tenori in sessione
			if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
				// Ricerca tenori attivi
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
				ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
				Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

				// 24/05/2010 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta
				// SIGE.
				if (lTenori.size() == 0 && mFasEsteso.getRichiestaSige() != null)
					lTenori = lTenCtrl.ExRicercaTenoreEstesoByRichiesta(
							mFasEsteso.getRichiestaSige().getIdRichiestaSige());

				setSessionAttribute("tenori", lTenori);
			}

			ricercaAvvocatiMagistrato();

			// Combo per la definizione del tipo Giudizio solo
			// se già non definito da una Fissazione Udienza.

			// 06/05/2009 In ogni caso si prepara la setComboTipoGiudizio.
			// if (mFasEsteso.getUdienzaProcedimento()== null ||
			// mFasEsteso.getUdienzaProcedimento().getDataUdienzaSige() == null)
			// setComboTipoGiudizio();

			// preleva i dati della udienza sige
			loadDatiUdienzaSige();

			// il tipo giudizio va definito quando si definisce l'udienza
			if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
				// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
				lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
						: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
			}
			String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
			// 20170907: [SG] aggiunta impostazione RequestAttribute
			setRequestAttribute("ctu", lCodTipoUfficio);

			// Carica Combo x TipoGiudizio.
			Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficio);

			setRequestAttribute("tipoGiudizioVal", lTipoGiudizio);
			setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

			// 06/05/2009 In caso di Udienza gia fissata si visualizzano i dati del collegio.
			// Eventuale lettura del collegio.
			CollegioModel lColMod = null;
			if (mFasEsteso.getUdienzaProcedimento() != null
					&& mFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
				ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
				lColMod = lCtrl.ExRicercaCollegioByIdUdienzaSige(
						mFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige());
			}
			setRequestAttribute("collegio", lColMod);

			// Si richiama il lock
			lockApplicativo("Emissione_Provvedimento");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneOrdinanza: -> page: " + lRetPage);
		// restituisce la jsp di VIEW
		return lRetPage;
	}

}