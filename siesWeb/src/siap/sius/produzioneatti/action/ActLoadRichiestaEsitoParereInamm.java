package siap.sius.produzioneatti.action;

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
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadRichiestaEsitoParereInamm extends ActRicercaFSPuntuale
		implements ICostantiProduzioneAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");
		this.setLinkRitorno();

		// Se viene passato ID Fascicolo si risale al Fascicolo e lo si mette in sessione
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPMod = lCtrl.ExRicercaFascicoloByKey(
					this.getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Fascicolo SIUS : " + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		}

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKeyForRichiestaAtti( lIdEvento );
		EventoModel lEveNot = lCtrl.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("evento", lEveNot);

		// 04/04/2007 Corretta valorizzazione Tipo Ufficio Destinatario.
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
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