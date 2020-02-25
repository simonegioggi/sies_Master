package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAnnullaProvvedimento
 * </p>
 * <p>
 * Description: Effettua l'annullamento logico del Provvedimento SIGE e l'inserimento della Motivazione in
 * CAMPO_NOTA.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActAnnullaProvvedimento extends ActionSige implements ICostantiProvvedimentoSige {

	public String processRequest() throws Exception {

		// Fascicolo SIGE in Sessione.
		FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();

		// Preleva dalla request la chiave dell' Evento come parametro
		BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Setta i dati per l'aggiornamento
		CampoNotaModel lCampoMod = new CampoNotaModel();
		lCampoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lCampoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCampoMod.setDataInserimento(DateUtils.getSysDate());
		lCampoMod.setEveIdEvento(lKeyEvento);
		lCampoMod.setDescr(getRequestStringParameter("motivazioni"));

		// Annullamento del Provvedimento con Inserimento Motivazioni e Update Evento
		IProvvedimentoSige lCtrlPro = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEveModel = lCtrlPro.ExRicercaProvvedimentoByIdEvento(lKeyEvento);
	
		lCtrlPro.ExAnnullaProvvedimento(lCampoMod, lFasEsteso.getFascicoloSige().getIdFascicoloSige(),
				lProvEveModel, lFasEsteso.getFascicoloSige().getCodStatoFascicolo());

		// Si consente di attivare con un parametro la prossima azione da eseguire.
		if (!this.isRequestParameterNullObj("nextAction")) {
			String lPage;
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "="
					+ this.getRequestStringParameter("nextAction");
			return lPage;
		}

		DocumentoAllegatoModel lAllegatoModel = new DocumentoAllegatoModel();

		// Setta i dati per l'aggiornamento
		lAllegatoModel.setDataAggiornamento(DateUtils.getSysDate());
		lAllegatoModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAllegatoModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAllegatoModel.setFlagDocumentoRegistrato("N");
		lAllegatoModel.setEveIdEvento(lKeyEvento);

		// Update
		IDocumentoAllegato lCtrlAllegato = SIUSLookupRemote.getDocumentoAllegatoRemote();
		lCtrlAllegato.ExAggiornaValidazione(lAllegatoModel);

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Provvedimento Annullato!");

		IUdienzaProcedimentoSige ctrlUdi = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel model = ctrlUdi.ExRicercaUdienzaProcedimentoByEve(lKeyEvento);
		/*
		 * ISSUE MEV : La modifica Udienza viene eseguita solo in presenza della stessa Numero MEV : 15_S4
		 * Autore : sessa Data : 29/gen/2016 Branch : MEV_15_S4
		 */
		if (model != null) {
			model.setFlagRinviata("A");
			model.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
			model.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
			ctrlUdi.ExModificaUdienzaProcedimento(model);
		}
		// ***** FINE INTERVENTO MEV_15_S4 *****//
		goToRitorno();
		return IWebConstants.PG_MESSAGE;
	}

}