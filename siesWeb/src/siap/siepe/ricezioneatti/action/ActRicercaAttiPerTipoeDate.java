package siap.siepe.ricezioneatti.action;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
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
public class ActRicercaAttiPerTipoeDate extends ActionSiap
		implements ICostantiRicezioneAtti, ICostantiMessaggio {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Questa classe controlla i MESSAGGI ricevuti.
		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// Model per visualizzazione criteri di ricerca.
		RicercaMessaggioModel lRicModel = new RicercaMessaggioModel();

		Date lDataInizioTrasmissioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_INIZIO,
				CAMPO_MESE_DATA_RICEZIONE_INIZIO, CAMPO_GIORNO_DATA_RICEZIONE_INIZIO);
		// Se la data inizio non viene impostata la valorizzo con 01/01/1900.
		if (lDataInizioTrasmissioneAtti == null)
			lDataInizioTrasmissioneAtti = DateUtils.getDate(1900, 01, 01);

		Date lDataFineTrasmissioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_FINE,
				CAMPO_MESE_DATA_RICEZIONE_FINE, CAMPO_GIORNO_DATA_RICEZIONE_FINE);
		// Se la data fine non viene impostata la valorizzo con quella odierna.
		if (lDataFineTrasmissioneAtti == null)
			lDataFineTrasmissioneAtti = DateUtils.getSysDate();

		// String lTipoOperazione = "-";
		String lCodTipoAtto = getRequestStringParameter(CAMPO_COD_TIPO_ATTO);
		String lCodTipoOperazione = "-";

		// Recupero HIGH_VALUE per associazione TIPO_ATTO a TIPO_OPERAZIONE
		if (lCodTipoAtto.compareTo("-") != 0) {
			Collection lColTipoAtto = null;
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lModel.setContesto("TIPO_ATTO");
			lColTipoAtto = lDecodifiche.ExRicercaDecodifiche(lModel);
			if (DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto) != null) {
				lCodTipoOperazione = DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto);
				lRicModel.setCodTipoOperazione(lCodTipoOperazione);
				lRicModel.setDescrTipoOperazione(DecodificheUtils.getDescbyCode(lColTipoAtto, lCodTipoAtto));
			} else
				lCodTipoOperazione = "99999";
		}
		String lFlagVisto = getRequestStringParameter(CAMPO_STATO_RICEZIONE);
		lRicModel.setFlagVisto(lFlagVisto);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoeDate(this.getCodUfficioUtenteConnesso(),
				lCodTipoOperazione, lDataInizioTrasmissioneAtti, lDataFineTrasmissioneAtti, lFlagVisto);

		this.setLinkRitorno();

		lRicModel.setDataIniziale(lDataInizioTrasmissioneAtti);
		lRicModel.setDataFinale(lDataFineTrasmissioneAtti);
		// setRequestAttribute("DataInizioTrasmissioneAtti",
		// DateUtils.getDateToString(lDataInizioTrasmissioneAtti, "dd/MM/yyyy"));
		// setRequestAttribute("DataFineTrasmissioneAtti",
		// DateUtils.getDateToString(lDataFineTrasmissioneAtti, "dd/MM/yyyy"));
		setRequestAttribute("FiltroRicerca", lRicModel);
		setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_ATTI_RICEVUTI;
	}

}