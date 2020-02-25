package siap.siepe.ricezioneatti.action;

import java.util.Date;
import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;
import siap.siepe.ricezioneatti.model.RicercaMessaggioModel;

/**
 * <p>
 * Title: ActRicercaAttiPerTipoeDate
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActRicercaAttiPerDataPerFlag extends ActionSiap implements ICostantiMessaggio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null
				&& JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim()
						.equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		RicercaMessaggioModel lRicModel = new RicercaMessaggioModel();

		if (!isRequestParameterNullObj(CAMPO_DATA_INVIO)) {
			Date lDataInvio = getRequestDateParameter(CAMPO_DATA_INVIO, "dd-MM-yyyy");
			lRicModel.setDataInvio(lDataInvio);
		}
		if (!isRequestParameterNullObj(CAMPO_FLAG_VISTO)) {
			String lFlag = getRequestStringParameter(CAMPO_FLAG_VISTO);
			lRicModel.setFlagVisto(lFlag);
		}
		lRicModel.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());

		lRicModel.setCodTipoMessaggio("01");

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggio(lRicModel);

		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("FiltroRicerca", lRicModel);

		return ICostantiRicezioneAtti.PG_LISTA_ATTI_RICEVUTI;
	}

}