package siap.sige.provvInterlocutori.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sige.SIGEException;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciNominaPeriti
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ActLoadInserisciNominaPeriti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciNominaPeriti extends ActRicercaFSigePuntuale
		implements ICostantiProvvInterlocutoriSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciNominaPeriti: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della pagina di view in caso di assenza provvedimento definitorio.
		String lRetPage = PG_LOAD_NOMINA_PERITI;

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
		ProvvedimentoSigeEventoModel lProvvedimento = lProvCtrl
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(
						lFasEsteso.getFascicoloSige().getIdFascicoloSige());

		if (lProvvedimento != null) {
			if (lProvvedimento.getProvvedimento() != null
					&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
					&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
							.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA) != 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Provvedimento: " + lProvvedimento);
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Impossibile emettere La Nomina Periti! è stato già emesso un Provvedimento di tipo definitorio!");
			}
		}

		// In caso di esistenza di Ordinanza di Incompetenza non annullata, oppure;
		// In caso di esistenza di Ordinanza di Incompetenza svalidata, si accede al suo Dettaglio.
		// Altrimenti si può emettere un'altra Ordinanza di Incompetenza.
		if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null
				&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
				&& (lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
						.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA) == 0)
				&& lProvvedimento.getEventoNotifica().getEvento() != null
				&& (lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null
						|| (lProvvedimento.getEventoNotifica().getEvento()
								.getFlagDocumentoRegistrato() != null
								&& lProvvedimento.getEventoNotifica().getEvento()
										.getFlagDocumentoRegistrato() != "A"))) {
			// Prepara la pagina di destinazione, il Dettaglio Nomina Periti.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);

			lPage.setAction("siap.sige.provvInterlocutori.action.ActDettaglioNominaPeriti");

			lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE,
					"" + lProvvedimento.getProvvedimento().getIdProvvedimentoSige());
			lRetPage = lPage.toString();
		} else {
			// Se il Fascicolo non è in stato "iscritto" o equivalente" non è possibile emettere provvedimento
			if (!IsFascicoloSigeIscrittoCompetenza())
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Non è possibile emettere provvedimento per questo Procedimento!");

			// Solo la prima volta vengono messi i Tenori in sessione
			/*
			 * if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
			 */
			// Ricerca tenori attivi
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

			if (lTenori.size() == 0 && lFasEsteso.getRichiestaSige() != null)
				lTenori = lTenCtrl
						.ExRicercaTenoreEstesoByRichiesta(lFasEsteso.getRichiestaSige().getIdRichiestaSige());

			setSessionAttribute("tenori", lTenori);
			// }

			// Preleva elenco degli altri destinatari.
			Option lOptionAut = new Option();
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);
			setRequestAttribute("tipoAutorita", lOptionAut.toString());

			// LISTA UFFICI
			Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
			String[] lStringFilter = { "-", "22" };
			Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
			lOptionAvv.setFilter(lStringFilter);
			setRequestAttribute("TipiIstituti1", "" + lOptionAvv);

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

			// Si Imposta l'Ufficio Competente.
			Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
			setRequestAttribute("tipoUfficioCompetente", "" + lOption);

			// Si richiama il lock
			lockApplicativo("Emissione_Provvedimento");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciNominaPeriti: -> page: " + lRetPage);
		return lRetPage; // restituisce la jsp di VIEW
	}

}