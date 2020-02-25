package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaFascicolo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Fascicolo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaFascicolo extends ActionSiapMinor implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione bottone di ritorno
		this.setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Istanzio il Model
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
			lFasMod.setSogIdSoggetto(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA))
			lFasMod.setSenIdSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA));

		if ((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-")))
			lFasMod.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO));

		if (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-")
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))) {
			// Si Utilizza il campo setCodUfficioInserimento come veicolo per trasmettere il codice ufficio
			// recuperato dal tipo ufficio e dalla descr ufficio
			lFasMod.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)));
		}

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ACCORPATO)) {
			// Sono nella form Avanzata - Intervallo Procedimenti
			String ufficioAccorpato = getRequestStringParameter(CAMPO_CHIAVE_ACCORPATO);
			String[] parts = ufficioAccorpato.split("-");
			if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
				lFasMod.setCodUfficioAccorpato(parts[1]);
			}
		}

		if (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals("")) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			if (lUffCtrl.verifyUfficioByDescrComune((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)
					.toUpperCase())))
				lFasMod.setDescrComuneUfficio((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)
						.toUpperCase()));
		}

		// STUB 10/06/2004 Nuovi parametri per il range di ANNO/PROGRESSIVO.
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		Vector lVect = lCtrl.ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(lFasMod,
				Integer.parseInt(lPagina), checkMinori());

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP
					+ "=" + ((FascicoloSiepModel) lVect.get(0)).getIdFascicoloSiep().toString();
		} else {
			// Collection di decodifica del COD_STATO
			Collection lCol = DecodificheManager.getInstance().getStatoProcedimento();
			setRequestAttribute("CodStato", lCol);

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				// MEV_57: aggiunto parametro di passaggio
				CountRisultati = lCtrl.ExGetNumFascicoloSiepByProgrAnnoDescrComune(lFasMod, checkMinori());
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("fascicoli", lVect);

			// lReturnPage = PG_RICERCAFASCICOLO_SIEP;
			lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLO;
		}

		// restituisce la jsp di VIEW
		return lReturnPage;
	}

}