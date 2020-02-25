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
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;

public class ActLoadRichiestaEsitoParere extends ActRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");
		this.setLinkRitorno();

		// Se viene passato ID Fascicolo si risale al Fascicolo e lo si mette in sessione
		// if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS))
		// {
		// IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		// FascicoloGPModel lFasGPMod =
		// lCtrl.ExRicercaFascicoloByKey(this.getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
		// setSessionAttribute("fascicoloSiusGP", lFasGPMod);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug( "ID Fascicolo SIUS : " + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		// }

		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		Date lDataInserimento = ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
				.getFascicoloSige().getDataInserimento();
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		BigDecimal lIdProvvedimento = null;
		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		BigDecimal lId = lProvEvento.getEventoNotifica().getEvento().getIdEvento();

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot
				.ExRicercaNotificaByKeyEvento(lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));
		setRequestAttribute("evento", lEveMod);

		String lUfficio = null;
		if (lEveMod.getNotifiche().length > 0) {
			lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		}

		UfficioModel lUffMod = new UfficioModel();
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
			setRequestAttribute("codTipoUfficioS", lUffMod.getCodTipoUfficio());
			setRequestAttribute("descTipoUfficioS",
					DecodificheUtils.getDescbyCode(lCol, lUffMod.getCodTipoUfficio()));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Ufficio Destinatario non valorizzato !");

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo",
				DecodificheUtils.getDescbyCode(lColMot, lEveMod.getEvento().getCodMotivo()));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return PG_LOAD_INSERISCIESITOPARERE; // restituisce la jsp di VIEW
	}

}