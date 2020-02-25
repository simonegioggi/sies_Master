package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciProvvInterlocutori
 * </p>
 * <p>
 * Description: Classe Action generalizzata per la load inserisci dei Provvedimenti di tipo Interlocutorio.
 * </p>
 * <p>
 * Questa classe viene poi specializzati in ogni specifico provvedimento di questo tipo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * Author: Luigi
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
abstract class ActLoadInserisciProvvInterlocutori extends ActRicercaFSigePuntuale {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Impostazione della pagina di view.
	protected String mRetPage = "";

	// Fascicolo Sige Esteso in sessione.
	private FascicoloSigeEstesoModel lFasEsteso = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciProvvInterlocutori: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Invoca la process Request della superclasse per la ricerca del Fascicolo.
		super.processRequest();

		lFasEsteso = getFascicoloSigeEstesoInSessione();

		// @emma 24072018 intervento post COLLAUDO 11.2
		FascicoloSigeEstesoModel lFasEsteso = super.getFascicoloSigeEstesoInSessione();
		if (lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("05") == 0
				|| lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("01") == 0) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non è possibile emettere provvedimento per questo Procedimento!");
			return IWebConstants.PG_MESSAGE;
		}

		// Inserire qui eventuale controlli

		ricercaDestinatari();

		// MERGE v10: lascio i tenori in sessione altrimenti quando torno da cancellazione non li trovo
		// Solo la prima volta vengono messi i Tenori in sessione
		// 20190519 [SG]: gestione tenori
		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO) || isSessionAttributeNullObj("tenori")) {
			// Ricerca tenori attivi
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

			// 15/03/2011 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta SIGE.
			if (lTenori.size() == 0 && lFasEsteso.getRichiestaSige() != null)
				lTenori = lTenCtrl
						.ExRicercaTenoreEstesoByRichiesta(lFasEsteso.getRichiestaSige().getIdRichiestaSige());

			setSessionAttribute("tenori", lTenori);
		}

		// Combo per la definizione del tipo Giudizio.
		setComboTipoGiudizio();

		// Combo per l'Ufficio Competente.
		setComboUfficioCompetente();

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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciProvvInterlocutori: -> page: " + mRetPage);

		return mRetPage; // restituisce la jsp di VIEW
	}

	protected void ricercaDestinatari() throws Exception {

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
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);
	}

}