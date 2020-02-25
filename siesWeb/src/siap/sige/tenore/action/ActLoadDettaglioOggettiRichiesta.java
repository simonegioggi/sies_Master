package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadDettaglioOggettiRichiesta
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di TenoreSige
 * </p>
 * La classe attiva la ricerca degli Oggetti (Tenore_Sige) legati alla richiesta del Fascicolo SIGE ed apre la
 * finestra per la Gestione degli Oggetti legati alla Richiesta/Atto.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioOggettiRichiesta extends ActionSige implements ICostantiTenoreSige {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();

		// ID Richiesta parametro di ingresso e di uscita per passarlo avanti
		BigDecimal lIdRichiesta = getRequestBigDecimalParameter(CAMPO_RIC_SIG_ID_RICHIESTA_SIGE);
		setRequestAttribute("idRichiesta", lIdRichiesta.toString());

		// Ricerca Tenori
		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector lTenori = lCtrl.ExRicercaTenoreEstesoByRichiesta(lIdRichiesta);
		setRequestAttribute("tenori", lTenori);

		// Ricerca Sentenze assegnate al Fascicolo SIGE
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());

		setRequestAttribute("sentenze", lSentenze);

		// Carica la lista dei contenuti
		Option lOption = new Option(DecodificheManager.getInstance().getContenutiSigeTrattino());
		setRequestAttribute("contenuto", lOption.toString());

		// Passa alla request il parametro che esprime la possibilità di inserire/modificare/cancellare
		// Oggetti
		setModificabileOggettiAtto();

		return PG_LOAD_DETTAGLIOTENORISIGE;
	}

}