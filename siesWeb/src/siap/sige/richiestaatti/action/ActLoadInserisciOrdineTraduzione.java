package siap.sige.richiestaatti.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
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
 * Title: ActLoadInserisciOrdineTraduzione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ActLoadInserisciOrdineTraduzione
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
public class ActLoadInserisciOrdineTraduzione extends ActRicercaFSigePuntuale
		implements ICostantiProvvedimentoSige, ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciOrdineTraduzione: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della pagina di view in caso di assenza provvedimento definitorio.
		String lRetPage = PG_LOAD_ORDINE_TRADUZIONE;

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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Provvedimento: " + lProvvedimento);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Impossibile emettere Ordine di Traduzione! è stato già emesso un Provvedimento di tipo definitorio!");
		}

		else {
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

			// Si Imposta l'Ufficio Competente.
			Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());

			setRequestAttribute("tipoUfficioCompetente", "" + lOption);

			// Si richiama il lock
			lockApplicativo("Emissione_Provvedimento");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciOrdineTraduzione: -> page: " + lRetPage);
		return lRetPage; // restituisce la jsp di VIEW
	}

}