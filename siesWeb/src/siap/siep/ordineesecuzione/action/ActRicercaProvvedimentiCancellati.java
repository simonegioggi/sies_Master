package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaProvvedimentiCancellati
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Evento
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
public class ActRicercaProvvedimentiCancellati extends ActionSiap implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

		// Ordinamento di default
		String lOrdinamento = "EM";

		// Lettura ordine data_invio
		if (!isRequestParameterNullObj("lOrdinamento")) {
			lOrdinamento = getRequestStringParameter("lOrdinamento");
		} else if (!isRequestAttributeNullObj("lOrdinamento")) {
			lOrdinamento = (String) getRequestAttribute("lOrdinamento");
		}
		this.setRequestAttribute("ordinamento", lOrdinamento);

		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		// Nell'Elenco Provvedimenti PM anche i Verbali. Luigi 3-2-06
		// Nell'Elenco Provvedimenti PM anche le richieste. Dario 14-3-06
		// Nell'Elenco Provvedimenti PM anche le Pene accessorie. Vincenzo 29-3-06
		String[] lTipoEvento = { "01", "07", "02", "16", "17", "18" };
		String[] lTipoProv = { "02", "03" };
//		String lPage = "";
		Vector lVect = null;

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		try {
			lVect = lCtrl.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(
					lFascicoloModel.getIdFascicoloSiep(),
					// getCodUfficioUtenteConnesso(),
					getUfficioUtenteConnesso(), lTipoEvento, lTipoProv, Integer.parseInt(lPagina),
					lOrdinamento);
		} catch (Exception e) {
		}

		if (lVect != null && lVect.size() > 0) {

			EventoModel lEveMod = lCtrl
					.ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
							lFascicoloModel.getIdFascicoloSiep(), lTipoEvento, lTipoProv, lOrdinamento);
			if (lEveMod == null || lEveMod.getIdEvento() == null) {
				lEveMod = new EventoModel();
			}
			setRequestAttribute("eventocancellareannullare", lEveMod);

			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(
						lFascicoloModel.getIdFascicoloSiep(), getCodUfficioUtenteConnesso(), lTipoEvento,
						lTipoProv);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute("eventi", lVect);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			return PG_RICERCA_PROVVEDIMENTI;
		} else {
			return /*lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ lFascicoloModel.getIdFascicoloSiep();
		}
	}

}