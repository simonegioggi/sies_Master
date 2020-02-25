package siap.sius.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Fissazione Udienza.
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
public class ActInserisciPreFissazioneUdienza extends ActionSius implements ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	IUdienzaProcedimento mUdiProCtrl;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		String lRetPage;
		FascicoloGPModel lFascicoloGPModel = null;
		IFascicoloSius lCtrFas = SIUSLookupRemote.getFascicoloSiusRemote();
		mUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();

		// Data attuale
		Date lOggi = DateUtils.getSysDate();

		if (isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			// Preleva dalla sessione il model fascicoloSIUSGP
			lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		} else {
			// Caso Prefissazione diretta
			lFascicoloGPModel = lCtrFas.ExRicercaFascicoloByKey(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
			controlloEsistenzaUdienza(
					lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(),
					lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
		}

		if (IsFascicoloSiusModificabile(lFascicoloGPModel) == false)
			throw new SIUSException(SIUSException.USER_MESSAGE, ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

		// Preleva dalla request id Udienza
		BigDecimal lIdUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);

		// Preleva dalla request la data udienza.
		Date lDataUdienza = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA);

		// Si istanzia il model UdienzaProcedimentoModel da inserire.
		UdienzaProcedimentoModel lUdiProc = new UdienzaProcedimentoModel();
		lUdiProc.setUdiIdUdienza(lIdUdienza);
		lUdiProc.setGenPridGeneraleProcedimento(
				lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		lUdiProc.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiProc.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lUdiProc.setDataInserimento(lOggi);
		lUdiProc.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_PREFISSATA);

		// Valorizzazione dell'eventuale UdienzaProcedimento da aggiornare
		UdienzaProcedimentoModel lUdiProcVecchia = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)) {
			BigDecimal lIdUdiProOld = getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO);
			if (lIdUdiProOld != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("UDIENZA_PROCEDIMENTO da aggiornare: " + lIdUdiProOld.toString());
				lUdiProcVecchia = new UdienzaProcedimentoModel();
				lUdiProcVecchia.setIdUdienzaProcedimento(lIdUdiProOld);
				lUdiProcVecchia.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lUdiProcVecchia.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lUdiProcVecchia.setDataAggiornamento(lOggi);
				lUdiProcVecchia.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_MODIFICATA);
			}
		}

		// Update della data_CAMERA_CONSIGLIO in generale_proceidmento
		GeneraleProcedimentoModel lGenProc = new GeneraleProcedimentoModel();
		lGenProc.setDataCameraConsiglio(lDataUdienza);
		lGenProc.setIdGeneraleProcedimento(
				lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		lGenProc.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lGenProc.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lGenProc.setDataAggiornamento(lOggi);
		lGenProc.setUdiIdUdienza(lIdUdienza);
		lGenProc.setFasSiuIdFascicoloSius(
				lFascicoloGPModel.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());

		// Inserimento
		/* UdienzaProcedimentoModel lRetModel = */mUdiProCtrl.ExInserisciPreFissazioneUdienza(lUdiProc,
				lGenProc, lUdiProcVecchia);

		if (isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			// Aggiornamento del Fascicolo in sessione
			lFascicoloGPModel = lCtrFas
					.ExRicercaFascicoloByKey(lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			setSessionAttribute("fascicoloSiusGP", lFascicoloGPModel);

			// Prepara la pagina di Dettaglio.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.udienzaprocedimento.action.ActLoadPreFissazioneUdienza");
			lPage.setParameter("ritorno", "rifissazione");
			lPage.setParameter("dettaglio", "SI");
			lPage.setParameter(IWebConstants.FLAG_RITORNO, "1");
			lRetPage = lPage.toString();
		} else
			// lRetPage = this.ritornoDopoCancellazione("ciao",null);
			lRetPage = goToRitorno();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("pagina di ritorno-> " + lRetPage);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return lRetPage;
	}

	private void controlloEsistenzaUdienza(BigDecimal aIdGenProc, BigDecimal aIdFas) throws Exception {

		if (mUdiProCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(aIdGenProc, "'F','P','S'") != null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Esiste già una udienza per il Procedimento");

		// Lock per evitare la fissazione contemporanea di 2 Udienze per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				aIdFas.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new SIUSException(SIUSException.USER_MESSAGE, "Il  " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR> Riprovare più tardi !");
		}
	}

}