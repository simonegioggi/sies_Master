package siap.siepe.fascicolo.action;

import java.util.Date;
import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siepe.ricezioneatti.model.RicercaMessaggioModel;

/**
 * <p>
 * Title: ActRicercaAttiVistiPerSoggetto
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
public class ActRicercaAttiVistiPerSoggetto extends ActionSiap
		implements ICostantiFascicoloSiepe, ICostantiMessaggio, ICostantiSoggetto {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// Questa classe controlla i MESSAGGI ricevuti.

		// Model per visualizzazione criteri di ricerca.
		RicercaMessaggioModel lRicModel = new RicercaMessaggioModel();

		String lCognomeSoggetto = getRequestStringParameter(CAMPO_COGNOME).toUpperCase();
		lRicModel.setCognomeSoggetto(lCognomeSoggetto);
		String lNomeSoggetto = getRequestStringParameter(CAMPO_NOME).toUpperCase();
		lRicModel.setNomeSoggetto(lNomeSoggetto);

		String lCodComuneNascita = "";
		if (getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
			lCodComuneNascita = lComMod.getCodComune();
			lRicModel.setCodComuneNascita(lCodComuneNascita);
			lRicModel.setDescrComuneNascita(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA));
		}

		String lCodStatoNascita = "-";
		if (!getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("-")) {
			lCodStatoNascita = getRequestStringParameter(CAMPO_COD_STATO_NASCITA);
			lRicModel.setCodStatoNascita(lCodStatoNascita);
		}

		Date lDataNascita = getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA);
		lRicModel.setDataNascita(lDataNascita);

		String lCodTipoOperazione = "-";

		// Solo i messaggi gia presi in visione.
		String lIncludeVisto = "V";

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerSoggetto(this.getCodUfficioUtenteConnesso(),
				lCognomeSoggetto, lNomeSoggetto, lCodComuneNascita, lCodStatoNascita, lDataNascita,
				lCodTipoOperazione, lIncludeVisto);

		this.setLinkRitorno();

		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("FiltroRicerca", lRicModel);

		return PG_LISTA_ATTI_RICEVUTI;
	}
}
