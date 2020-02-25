package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciDecretoInammissibilita
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ActLoadInserisciDecretoInammissibilita
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadEmissioneDecretoInammissibilita extends ActRicercaFSigePuntuale
		implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciDecretoInammissibilita: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della pagina di view in caso di assenza provvedimento definitorio.
		String lRetPage = PG_LOAD_EMISSIONE_DECRETO_INAMMISSIBILITA;

		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se si proviene dal menu'.
			super.processRequest();
		}

		if (this.isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Fascicolo Sige non in sessione");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		// Verifica esistenza di un provvedimento per il fascicolo SIGE selezionato.
		// ProvvedimentoSigeModel lProvvedimento = lProvCtrl.ExRicercaProvDefinitorioByFasc(lIdFasSige);
		ProvvedimentoSigeEventoModel lProvvedimento = lProvCtrl
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(
						lFasEsteso.getFascicoloSige().getIdFascicoloSige());

		if (lProvvedimento != null) {
			if (lProvvedimento.getProvvedimento() != null
					&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
					&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
							.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA) != 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Provvedimento: " + lProvvedimento);
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Impossibile emettere il Decreto di Inammissibilità! è stato già emesso un Provvedimento di tipo definitorio!");
			}
		}
		// Modifica del 08/03/2017 *** INIZIO *******

		// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
		if (lFasEsteso.getUdienzaProcedimento() != null
				// && lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
				&& lFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige() != null) {
			IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			UdienzaProcedimentoSigeModel lUdiProSige = lUdiProCtrl.ExRicercaUdienzaProcedimentoSigeByKey(
					lFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
			if (lUdiProSige != null && lUdiProSige.getUdiIdUdienzaSige() != null) {
				BigDecimal lIdUdienzaSige = lUdiProSige.getUdiIdUdienzaSige();
				if (lIdUdienzaSige != null) {
					String sIdUdienzaSige = lIdUdienzaSige.toString();
					setRequestAttribute("IdUdienzaSige", sIdUdienzaSige);
					setRequestAttribute("UdienzaSige", getUdienzaSige(sIdUdienzaSige));

					if (lFasEsteso.getFascicoloSige().getCodTipoGiudizio() != null
							&& !lFasEsteso.getFascicoloSige().getCodTipoGiudizio().equals("")
							&& !lFasEsteso.getFascicoloSige().getCodTipoGiudizio().equals("-")) {
						setRequestAttribute("tipoGiudizioVal",
								lFasEsteso.getFascicoloSige().getCodTipoGiudizio());
					}

				}
			}
		}
		// Modifica del 08/03/2017 *** FINE *******
		// 20190519 [SG]: aggiunta gestione idUdienzaSige
		else if (!isRequestParameterNullEmptyObj("idUdiSig")) {
			UdienzaSigeModel udiSige = getUdienzaSige(getRequestStringParameter("idUdiSig"));
			setRequestAttribute("UdienzaSige", udiSige);
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("AAA cod tipo provvedimento: " +
		// lProvvedimento.getProvvedimento().getCodTipoProvvedimento());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("AAA cod tipo provvedimento SIGE: " +
		// lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("AAA Flag Documento Registrato: " +
		// lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato());
		// In caso di esistenza di Decreto di Inammissibilità non annullato, oppure;
		// In caso di esistenza di Decreto di Inammissibilità svalidato, si accede al suo Dettaglio.
		// Altrimenti si può emettere un altro Decreto di Inammissibilità.
		if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null
				&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
				&& (lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
						.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA) == 0)
				&& lProvvedimento.getEventoNotifica().getEvento() != null
				&& (lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null
						|| (lProvvedimento.getEventoNotifica().getEvento()
								.getFlagDocumentoRegistrato() != null
								&& lProvvedimento.getEventoNotifica().getEvento()
										.getFlagDocumentoRegistrato() != "A"))) {
			// Prepara la pagina di destinazione, il Dettaglio Decreto di Inammissibilità.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);

			lPage.setAction("siap.sige.provvedimento.action.ActDettaglioDecretoInammissibilita");

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
				lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
				ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
				Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

				// 25/05/2010 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta
				// SIGE.
				if (lTenori.size() == 0 && lFasEsteso.getRichiestaSige() != null)
					lTenori = lTenCtrl.ExRicercaTenoreEstesoByRichiesta(
							lFasEsteso.getRichiestaSige().getIdRichiestaSige());

				setSessionAttribute("tenori", lTenori);
			}

			// Avvocati attuali assegnati al fascicolo SIGE.
			FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
			Vector lAvvocati = lFasSigeUtils
					.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			if (lAvvocati.size() > 0)
				setRequestAttribute("avvocato", lAvvocati);

			// Magistrato Assegnatario
			MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
			setRequestAttribute("magistratoassegnatario", lMagAss);

			// Combo per la definizione del tipo Giudizio.
			setComboTipoGiudizio();

			// In caso di Udienza gia fissata si visualizzano i dati del collegio.
			// Eventuale lettura del collegio.
			CollegioModel lColMod = null;
			if (lFasEsteso.getUdienzaProcedimento() != null
					&& lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
				ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
				lColMod = lCtrl.ExRicercaCollegioByIdUdienzaSige(
						lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige());
			}
			setRequestAttribute("collegio", lColMod);

			// Si richiama il lock
			lockApplicativo("Emissione_Provvedimento");
		}

		// Si Imposta l'Ufficio Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"-", "TDS", "UDS", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID",
		// "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "PM", "PMM", "PMPT", "PGCAP",
		// "PGMI", "PGMID", "PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM"} ); //solo le Autorità Emittenti.
		lOption.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP",
				"GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM", "PM",
				"PGCAP" }); // solo le Autorità Emittenti.
		setRequestAttribute("tipoUfficioCompetente", "" + lOption);

		listaMotivi();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciDecretoInammissibilita: -> page: " + lRetPage);
		return lRetPage; // restituisce la jsp di VIEW
	}

	private void listaMotivi() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilitaxSottoSistema(lTipoUff, "SIGE"));
		setRequestAttribute("motivi", lVect);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
	}

}