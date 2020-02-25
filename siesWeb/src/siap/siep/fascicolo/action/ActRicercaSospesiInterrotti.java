package siap.siep.fascicolo.action;

/**
 * <p>Title: ActRicercaSospesiInterrotti</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActRicercaSospesiInterrotti extends ActionSiap implements ICostantiFascicoloSiep {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Istanzio il Model
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		String lMotivo = this.getRequestStringParameter(CAMPO_MOTIVO_INT_SOSP);
		String lCodTipo = getRequestStringParameter(CAMPO_TIPO_INT_SOSP);
		String lTipo = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoIntSosp(),
				lCodTipo);

		StringTokenizer lCodMotivo = new StringTokenizer(lMotivo, ";");
		String[] lMotivoSelect = new String[lCodMotivo.countTokens()];
		int i = 0;
		String lCodMotApp = null;
		String lAggiuntoUnion = "N";
		while (lCodMotivo.hasMoreTokens()) {
			lCodMotApp = lCodMotivo.nextToken();
			// la union viene fatta solo se nei codici del motivo si trova 2141 ossia avvenuta
			// espulsione
			if ("2141".equals(lCodMotApp))
				lAggiuntoUnion = "S";
			else
				lMotivoSelect[i] = lCodMotApp;

			i++;
		}

		/*
		 * per sapere se nei criteri di selezione del motivo sospensione/interruzione è stato selezionato
		 * 'Tutti', eseguo un controllo sul contatore 'i' se è maggiore di 1 vuol dire che sono stati
		 * selezionati più codici
		 */

		String lDescMot = null;
		if (i > 1)
			lDescMot = "Tutti";
		else
			lDescMot = DecodificheUtils
					.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(), lCodMotApp);

		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ACCORPATO)) {
			String ufficioAccorpato = getRequestStringParameter(CAMPO_CHIAVE_ACCORPATO);
			String[] parts = ufficioAccorpato.split("-");
			if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
				lFasMod.setChiaveUfficio(parts[1]);
			}
		}

		String lReturnPage = "";
		Vector lVect = new Vector();

		// ricerca con paginazione
		if (isRequestParameterNullObj("elencocompleto")
				|| !getRequestStringParameter("elencocompleto").equals("S")) {
			String lPagina = "1";
			if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
				lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lVect = lCtrl.ExRicercaFascicoloSospesiInterrotiOnViewPaged(lFasMod, lMotivoSelect,
					Integer.parseInt(lPagina), lAggiuntoUnion);

			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetCountFascicoliSospesiInterrotti(lFasMod, lMotivoSelect,
						lAggiuntoUnion);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			String lAzione = "siap.siep.fascicolo.action.ActRicercaSospesiInterrotti";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
			setRequestAttribute("elencocompleto", "N");
		} else {
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lVect = lCtrl.ExRicercaFascicoloSospesiInterrotiAll(lFasMod, lMotivoSelect, lAggiuntoUnion);
			setRequestAttribute("elencocompleto", getRequestStringParameter("elencocompleto"));
		}

		setRequestAttribute("motivo", lDescMot);
		setRequestAttribute("tipo", lTipo);
		setRequestAttribute("fascicolo", lFasMod);
		setRequestAttribute("fascicoli", lVect);
		setRequestAttribute("lCodMotivo", lMotivo);
		setRequestAttribute("lCodTipo", lCodTipo);

		lReturnPage = PG_RICERCA_FASCICOLI_SIEP_SOSPESI_INTERROTTI;
		return lReturnPage; // restituisce la jsp di VIEW
	}

}