package siap.sige.richiestaatti.action;

/**
* <p>Title: ActLoadRichiestaParere</p>
* <p>Description: Classe Action per la load di RichiestaCarichiPendenti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.SIUSException;

public class ActLoadRichiestaEsitoParereInamm extends ActionSige implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");
		this.setLinkRitorno();

		// NEW *********************************
		BigDecimal lIdFascicolo = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		} else {
			FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
		}
		ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
		lProvSige.setFasIdFascicoloSige(lIdFascicolo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca Provvedimendi da ID FASCICOLO->" + lIdFascicolo);
		// NEW *********************************

		// Se viene passato ID Fascicolo si risale al Fascicolo e lo si mette in sessione
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			// FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
			FascicoloSigeEstesoModel lFasEsteso = lCtrl.ExRicercaEstesaFascicoloSigeByKey(
					this.getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE));

			// IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			// FascicoloGPModel lFasGPMod =
			// lCtrl.ExRicercaFascicoloByKey(this.getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE));
			setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Fascicolo SIGE : " + lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		}

		BigDecimal lIdProvvedimento = null;

		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// Data Fascicolo SIGE
		// Date lDataInserimento =
		// ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getDataInserimento();
		Date lDataInserimento = ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
				.getFascicoloSige().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveNot = lCtrl
				.ExRicercaEventoByKey(lProvEvento.getProvvedimento().getIdEventoGenerato());
		setRequestAttribute("evento", lEveNot);

		// 04/04/2007 Corretta valorizzazione Tipo Ufficio Destinatario.
		EventoNotificaModel lEveNotMod = lCtrl
				.ExRicercaEventoNotificaByKey(lProvEvento.getProvvedimento().getIdEventoGenerato());
		if (lEveNotMod == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "EventoNotificaModel Assente !");

		String lUfficio = lEveNotMod.getNotifiche()[0].getUffCodUfficio();
		UfficioModel lUffMod = new UfficioModel();
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveNotMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
			setRequestAttribute("codTipoUfficioS", lUffMod.getCodTipoUfficio());
			setRequestAttribute("descTipoUfficioS",
					DecodificheUtils.getDescbyCode(lCol, lUffMod.getCodTipoUfficio()));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Ufficio Destinatario non valorizzato !");

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo", DecodificheUtils.getDescbyCode(lColMot, lEveNot.getCodMotivo()));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return PG_LOAD_INSERISCIESITOPAREREINAMM; // restituisce la jsp di VIEW
	}

}