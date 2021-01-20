package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

/**
 * <p>
 * Title: ActLoadRicercaFascicoloSIEP
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Procedimento SIEP.
 * </p>
 * L'action specializza siap.sius.fascicolo.action.ActRicercaFascicolo solo per cambiare l'intestazione della
 * pagina risultato della ricerca.
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 5.0
 */
public class ActRicercaFascicoloSIEP extends ActionSige implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setRequestAttribute("titolo", "Elenco Procedimenti SIEP");

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
			// Si Utilizza il campo setCodUfficioInserimento come veicolo per
			// trasmettere il codice ufficio recuperato dal tipo ufficio e dalla
			// descr ufficio
			lFasMod.setCodUfficioInserimento(
					getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO),
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
			if (lUffCtrl.verifyUfficioByDescrComune(
					(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase())))
				lFasMod.setDescrComuneUfficio(
						(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase()));
		}

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

		// MEV_57: aggiunto parametro di passaggio
		Vector lVect = lCtrl.ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(lFasMod,
				Integer.parseInt(lPagina), checkMinori());

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) lVect.get(0)).getIdFascicoloSiep().toString();
		} else {
			// Collection di decodifica del COD_STATO
			Collection lCol = DecodificheManager.getInstance().getStatoProcedimento();
			setRequestAttribute("CodStato", lCol);

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				// MEV_57: aggiunto parametro di passaggio
				// CountRisultati = lCtrl.ExGetNumFascicoloSiepByProgrAnnoDescrComune(lFasMod, checkMinori());
				// MEV_6: la count viene eseguita nella query di paginazione
				CountRisultati = ((FascicoloSiepModel) lVect.get(0)).getCountRisultati();
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