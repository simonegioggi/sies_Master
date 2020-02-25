package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.datiprovsige.controller.IDatiProvvedimentoSige;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaEsiti
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di TenoreSige
 * </p>
 * La classe apre la finestra per la Gestione degli Oggetti Sige. Non viene effettuata la ricerca degli
 * oggetti perchè quelli visualizzati dalla finestra saranno quelli presenti in session.
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class ActLoadModificaEsiti extends ActLoadDettaglioOggettoProv {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "processRequest : inizio");

		// Si richiama il lock
		lockApplicativo("Modifica_Oggetti");

		Option lEsiti = null;
		TenoreSigeModel lTenore = null;
		String lPage = PG_INSERIMENTO_TENORE_ESITO_SIGE;
		String lTipoEsito = "unico";

		if (this.isRequestParameterNullObj(CAMPO_ID_TENORE_SIGE))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Manca ID");

		if (this.isRequestParameterNullObj(CAMPO_ID_TENORE_SIGE))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Manca ID");

		FascicoloSigeEstesoModel fascicoloEsteso = super.getFascicoloSigeEstesoInSessione();
		BigDecimal idFascicoloSiep = null;
		try {
			idFascicoloSiep = fascicoloEsteso.getFascicoloSiep().getIdFascicoloSiep();
		} catch (NullPointerException ne) {
		} catch (NumberFormatException ne) {
		}

		if (!this.isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIEP_SENTENZA)) {
			try {
				idFascicoloSiep = super.getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP_SENTENZA);
			} catch (NumberFormatException ne) {}
		}

		setLinkRitorno();
		setRequestAttribute("titolo", "Modifica Oggetto");
		setRequestAttribute("modifica", "SI");

		if (idFascicoloSiep != null)
			setRequestAttribute(ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA,
					idFascicoloSiep.toString());

		String codOggetto = super.getRequestStringParameter("codOggettoSige");
		setRequestAttribute("codOggettoSige", codOggetto);
		BigDecimal idProvvedimento = super.getRequestBigDecimalParameter("idProvvedimento");
		setRequestAttribute("idProvvedimento", idProvvedimento.toString());
		// Ricerca Tenori
		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		BigDecimal idTenore = getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE);
		Vector<TenoreSigeEstesoModel> lTenori = lCtrl.ExRicercaTenoreEstesoById(idTenore);
		//20190521: [EC] gestione esiti distinti per reato/sentenza e per oggetto
		if (!isRequestParameterNullEmptyObj("isTitoliEsecutivi") &&
		 !super.getRequestStringParameter("isTitoliEsecutivi").equalsIgnoreCase("false")) {
			 	setRequestAttribute("isTitoliEsecutivi", super.getRequestStringParameter("isTitoliEsecutivi"));
			 		lTenori = lCtrl.ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento(codOggetto, idProvvedimento);
		}

		super.setRequestAttribute("Modificabile", "NO");
		//20190521: [EC] gestione esiti distinti per reato/sentenza e per oggetto
		setRequestAttribute("isTitoliEsecutivi", super.getRequestStringParameter("isTitoliEsecutivi"));

		// Ricerca esiti per lo specifico oggetto
		if (lTenori != null && lTenori.size() > 0) {
			// Estrazione del Codice Oggetto
			TenoreSigeEstesoModel lTenoreEsteso = getSelTenore(lTenori, idTenore);
			lTenore = lTenoreEsteso.getTenoreSige();

			if (lTenore.getCodEsitoSige() != null) {
				super.setRequestAttribute("Modificabile", "SI");
				// Tipo di esito : Unico o differenziato
				// 15/03/2011 Gestione Esito tampone "0000" per Esito diversificato per SENTENZA_REATO (Stesso
				// TENORE_SIGE)
				if (lTenore.getCodEsitoSige().length() > 1
						&& lTenore.getCodEsitoSige().trim().compareTo("0000") != 0)
					lTipoEsito = "unico";
				else
					lTipoEsito = "differenziato";
			}

			// Inserimento Esiti
			lEsiti = getEsito(lTenoreEsteso.getTenoreSige().getCodOggettoSige());
			setRequestAttribute("esiti", lEsiti);

			setRequestAttribute("dati", mDati);
			ricercaDatiProvvedimento();
			try {
				// ricercaAnnotazioneManuale(lTenore);
				super.ricercaAnnotazioneManuale(lTenoreEsteso.getSentenza().getIdSentenza(), lTenoreEsteso
						.getTenoreSige().getIdTenoreSige());
				super.setRequestAttribute("idSenSentenza", lTenoreEsteso.getSentenza().getIdSentenza()
						.toString());
			} catch (F3BException fe) {
				if (isRequestParameterNullEmptyObj("isTitoliEsecutivi"))
					throw fe;
			}

		}
		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("tipo_esito", lTipoEsito);
		setRequestAttribute("id_tenore", getRequestStringParameter(CAMPO_ID_TENORE_SIGE));

		// Passa alla request il parametro che esprime la possibilità di inserire/modificare/cancellare
		// Oggetti
		setModificabileOggettiAtto();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "processRequest : fine");

		BigDecimal lIdFascicoloSiep = null;
		if (!super.isRequestParameterNullEmptyObj(ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA)) {
			try {
				lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA);
			} catch (Exception e) {

			}
		}

		String isTitoliEsecutivi = getRequestStringParameter("isTitoliEsecutivi");
		setRequestAttribute("formname", "inserisciEsitiTenore");

		List<String> idFascicoli = new ArrayList<String>();
		if (lIdFascicoloSiep != null)
			idFascicoli = getIdFascicoli(isTitoliEsecutivi, lIdFascicoloSiep);

		try {
			ricercaRichiesteAlGE(idFascicoli, (String) getRequestAttribute(FLAG_INDULTO));
		} catch (Exception e) {

		}
		return lPage;
	}

	private void ricercaDatiProvvedimento() throws Exception {

		// Ricerca Dati Provvedimento
		IDatiProvvedimentoSige lDatiProvCtrl = SIGELookupRemote.getDatiProvvedimentoSigeRemote();
		Vector<DatiProvvedimentoSigeModel> lDatiProv = lDatiProvCtrl
				.ExRicercaDatiProvvedimentoSigeByIdTenore(getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));

		if (lDatiProv != null) {
			Vector<String> lDatiProvSel = new Vector<String>();
			for (int i = 0; i < lDatiProv.size(); i++) {
				DatiProvvedimentoSigeModel lDato = (DatiProvvedimentoSigeModel) lDatiProv.get(i);
				lDatiProvSel.add(lDato.getCodTipoDatiProv());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num Dati Provv Sige già selezionati :  " + lDatiProvSel.size());
			setRequestAttribute("dati_selezionati", lDatiProvSel);
		}
	}

	private List<String> getIdFascicoli(String isTitoliEsecutivi, BigDecimal lIdFascicoloSiep)
			throws F3BException {

		List<String> idFascicoli = new ArrayList<String>();
		if (isTitoliEsecutivi.equalsIgnoreCase("false")) {
			idFascicoli.add(lIdFascicoloSiep.toString());
			return idFascicoli;
		}

		String idProvvedimento = super.getRequestStringParameter("idProvvedimento");
		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector<TenoreSigeEstesoModel> tenori = lTenCtrl
				.ExRicercaTenoriEstesiByIdProvvedimento(new BigDecimal(idProvvedimento));

		for (TenoreSigeEstesoModel lTenoreEsteso : tenori) {
			BigDecimal idFascicoloSiep = lTenoreEsteso.getSentenzaSige().getFasSieIdFascicoloSiep();
			idFascicoli.add(idFascicoloSiep.toString());
		}
		return idFascicoli;
	}

	protected void ricercaRichiesteAlGE(List<String> idFascicoliSiep, String aCodTipoBeneficio)
			throws F3BException {

		Vector<String> lListaRichieste = new Vector<String>();
		for (String idFascicoloSiep : idFascicoliSiep) {
			AnnotazioneManualeModel lAnnPerRicerca = new AnnotazioneManualeModel();
			lAnnPerRicerca.setFasSieIdFascicoloSiep(new BigDecimal(idFascicoloSiep));
			lAnnPerRicerca.setCodTipoAnnotazione(aCodTipoBeneficio); // ricerca sia 002 che 003
			lAnnPerRicerca.setFlagValidato("S");
			// Ricerca
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lListaRichieste.addAll(IAnn.ExRicercaRichiesteTenoriSige(lAnnPerRicerca));

		}
		setRequestAttribute("RichiesteAlGE1", lListaRichieste);
	}

	private TenoreSigeEstesoModel getSelTenore(Vector<TenoreSigeEstesoModel> lTenori, BigDecimal idTenore) {

		TenoreSigeEstesoModel sel = null;
		for (TenoreSigeEstesoModel tenore : lTenori) {
			if (tenore.getTenoreSige().getIdTenoreSige().compareTo(idTenore) == 0) {
				sel = tenore;
				break;
			}
		}
		return sel;
	}

}