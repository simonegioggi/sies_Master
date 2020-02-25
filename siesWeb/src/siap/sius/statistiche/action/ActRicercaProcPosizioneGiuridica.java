package siap.sius.statistiche.action;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
// import per controller su SIEP
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaProcPosizioneGiuridica extends ActionSiap implements ICostantiStatistiche {

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		RicercaProcedimentoModel lRicercaModel = new RicercaProcedimentoModel();
		Collection<EveFasGepSogCancModel> lElencoPaginato = null;
		IStatisticheSius lCtrl = null;
		BigDecimal lRecords = null;
		String lPagina = "1";
		String lCodPosizioneGiuridica = null;
		String lCodOggettoProcedimento = null;
		String lCodMagistrato = null;

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {

			lRicercaModel.setDataIscrizioneInizio(
					getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
							ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_INIZIALE,
							ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_FINALE)) {

			lRicercaModel.setDataIscrizioneFine(
					getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_FINALE,
							ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_FINALE,
							ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_GIORNO_FINE_PENDENZA)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_MESE_FINE_PENDENZA)
				&& !isRequestParameterNullObj(ICostantiStatistiche.CAMPO_ANNO_FINE_PENDENZA)) {

			lRicercaModel.setDataFinePendenza(
					getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_FINE_PENDENZA,
							ICostantiStatistiche.CAMPO_MESE_FINE_PENDENZA,
							ICostantiStatistiche.CAMPO_GIORNO_FINE_PENDENZA));
			// se la data di fine pendenza è valorizzata, si imposta "forzatamente"
			// la data iscrizione fine = data fine pendenza.
			if (lRicercaModel.getDataFinePendenza() != null) {
				lRicercaModel.setDataIscrizioneFine(lRicercaModel.getDataFinePendenza());
			}
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO)) {
			lRicercaModel
					.setCodTipoUfficio(getRequestStringParameter(ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO));
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO)) {
			lRicercaModel
					.setCodSede(getRequestStringParameter(ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO));
			lRicercaModel.setDescrTipoUfficio(
					getRequestStringParameter(ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO));
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_COD_POSIZIONE_GIURIDICA)) {
			lCodPosizioneGiuridica = getRequestStringParameter(
					ICostantiStatistiche.CAMPO_COD_POSIZIONE_GIURIDICA);
			if (lCodPosizioneGiuridica.compareTo("-") != 0) {
				lRicercaModel.setCodPosizioneGiuridica(lCodPosizioneGiuridica);
				lRicercaModel.setDescrPosizioneGiuridica(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getPosizioneGiuridica(), lCodPosizioneGiuridica));
			}
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_COD_OGGETTO)) {
			lCodOggettoProcedimento = getRequestStringParameter(ICostantiStatistiche.CAMPO_COD_OGGETTO);
			if (lCodOggettoProcedimento.compareTo("-") != 0) {
				lRicercaModel.setCodOggettoProcedimento(lCodOggettoProcedimento);
				lRicercaModel.setDescrOggettoProcedimento(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getOggettoProcedimento(), lCodOggettoProcedimento));
			}
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_COD_MAGISTRATO)) {
			lCodMagistrato = getRequestStringParameter(ICostantiStatistiche.CAMPO_COD_MAGISTRATO);
			if (lCodMagistrato.compareTo("Tutti") != 0) {
				lRicercaModel.setCodMagistrato(lCodMagistrato);
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lCodMagistrato);
				lRicercaModel.setDescrMagistrato(lMagistrato.getCognome() + " " + lMagistrato.getNome());
			}
		}

		if (!isRequestParameterNullObj(ICostantiStatistiche.CAMPO_COD_CANCELLERIA)) {
			lRicercaModel
					.setCodCancelleria(getRequestStringParameter(ICostantiStatistiche.CAMPO_COD_CANCELLERIA));
			if (lRicercaModel.getCodCancelleria().compareTo("-") != 0) {
				ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
				CancelleriaAssegnatariaModel lCancAssRicerca = new CancelleriaAssegnatariaModel();
				lCancAssRicerca.setCodCancelleriaAssegnataria(lRicercaModel.getCodCancelleria());
				Vector<CancelleriaAssegnatariaModel> lElenco = lCancAssCtrl
						.ExRicercaCancelleriaAssegnataria(lCancAssRicerca);
				CancelleriaAssegnatariaModel lCancAss = lElenco.firstElement();
				lRicercaModel.setDescrCancelleria(lCancAss.getDescCancelleriaAssegnataria());
			}
		}

		lRicercaModel.setCodUfficio(getCodUfficioUtenteConnesso());

		lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();

		lElencoPaginato = lCtrl.ExRicercaProcPosizioneGiuridicaPaginata(lRicercaModel,
				Integer.parseInt(lPagina));

		if (isRequestParameterNullObj("CountRisultati"))
			lRecords = lCtrl.ExGetNumRicercaProcPosizioneGiuridica(lRicercaModel);
		else
			lRecords = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", lRecords);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setSessionAttribute("ricercaModel", lRicercaModel);
		setRequestAttribute("elencoProcedimenti", lElencoPaginato);

		// return lReturnPage; //restituisce la jsp di VIEW
		return PG_RICERCA_PROCPOSZIONEGIURIDICA;
	}

}