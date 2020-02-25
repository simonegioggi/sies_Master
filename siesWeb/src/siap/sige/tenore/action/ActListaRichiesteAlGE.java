package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

public class ActListaRichiesteAlGE extends ActLoadDettaglioOggettoProv {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Recupera la lista delle annotazioni manuali con richiesta la GE da caricare nella popup con e esenza
	 * anticipazione degli effetti, ma già validate
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		String lCodTipoBeneficio = getRequestStringParameter(FLAG_INDULTO);
		String isTitoliEsecutivi = "false";
		if (!super.isRequestParameterNullObj("isTitoliEsecutivi"))
			isTitoliEsecutivi = getRequestStringParameter("isTitoliEsecutivi");

		List<String> idFascicoli = getIdFascicoli(isTitoliEsecutivi, lIdFascicoloSiep);
		ricercaRichiesteAlGE(idFascicoli, lCodTipoBeneficio);

		// viene passato il nome della form
		setRequestAttribute("formname", this.getRequestStringParameter("formname"));
		return PG_LISTA_RICHIESTE_AL_GE;
	}

	private List<String> getIdFascicoli(String isTitoliEsecutivi, BigDecimal lIdFascicoloSiep)
			throws F3BException {
		List<String> idFascicoli = new ArrayList<>();
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

	@SuppressWarnings("unchecked")
	protected void ricercaRichiesteAlGE(List<String> idFascicoliSiep, String aCodTipoBeneficio)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaRichiesteAlGE");
		Vector<String> lListaRichieste = new Vector<>();
		for (String idFascicoloSiep : idFascicoliSiep) {
			AnnotazioneManualeModel lAnnPerRicerca = new AnnotazioneManualeModel();
			lAnnPerRicerca.setFasSieIdFascicoloSiep(new BigDecimal(idFascicoloSiep));
			lAnnPerRicerca.setCodTipoAnnotazione(aCodTipoBeneficio); // ricerca sia 002 che 003
			lAnnPerRicerca.setFlagValidato("S");
			// Ricerca
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lListaRichieste.addAll(IAnn.ExRicercaRichiesteTenoriSige(lAnnPerRicerca));

		}
		setRequestAttribute("RichiesteAlGE", lListaRichieste);
	}

}