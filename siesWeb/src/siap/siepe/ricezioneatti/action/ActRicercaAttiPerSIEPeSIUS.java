package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siepe.ricezioneatti.model.RicercaMessaggioModel;

/**
 * <p>
 * Title: ActRicercaAttiPerSIEPeSIUS
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
public class ActRicercaAttiPerSIEPeSIUS extends ActionSiap
		implements ICostantiRicezioneAtti, ICostantiMessaggio, ICostantiSoggetto {
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

		// Model per visualizzazione criteri di ricerca.
		RicercaMessaggioModel lRicModel = new RicercaMessaggioModel();

		// Questa classe controlla i MESSAGGI ricevuti in base ai parametri impostati (per Numero SIEP /
		// Numero SIUS / Tipo Atto).
		// Si Valorizza <> "-" se l'ufficio emittente è stato correttamente impostato.
		String lCodUffMittente = "-";
		String lTipoOperazione = "-";
		BigDecimal lAnno = null;
		BigDecimal lProgr = null;
		String lCodTipoAtto = getRequestStringParameter(CAMPO_COD_TIPO_ATTO);
		// String lStatoRicezione = getRequestStringParameter(CAMPO_STATO_RICEZIONE);

		if (!(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO).equalsIgnoreCase("-"))
				&& !(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equalsIgnoreCase(""))) {
			lCodUffMittente = this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
			lRicModel.setCodUfficioMittente(lCodUffMittente);
			lRicModel.setDescrUfficioMittente(
					this.getUfficioByCodUfficio(lCodUffMittente).getDescrTipoUfficio() + " - "
							+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase());

		}

		if (getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP) != null) {
			lTipoOperazione = "SIEP";
			lAnno = getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP);
			lProgr = getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP);
			lRicModel.setChiaveAnnoSiep(lAnno);
			lRicModel.setChiaveProgrSiep(lProgr);
		} else if (getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIUS) != null) {
			lTipoOperazione = "SIUS";
			lAnno = getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIUS);
			lProgr = getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIUS);
			lRicModel.setChiaveAnnoSius(lAnno);
			lRicModel.setChiaveProgrSius(lProgr);
		} else if (lCodTipoAtto.compareTo("-") != 0) {
			// Recupero HIGH_VALUE per associazione TIPO_ATTO a TIPO_OPERAZIONE
			Collection lColTipoAtto = null;
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lModel.setContesto("TIPO_ATTO");
			lColTipoAtto = lDecodifiche.ExRicercaDecodifiche(lModel);
			if (DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto) != null) {
				lTipoOperazione = DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto);
				lRicModel.setCodTipoOperazione(lTipoOperazione);
				lRicModel.setDescrTipoOperazione(DecodificheUtils.getDescbyCode(lColTipoAtto, lCodTipoAtto));
			} else
				lTipoOperazione = "99999";
		}
		String lFlagVisto = getRequestStringParameter(CAMPO_STATO_RICEZIONE);
		lRicModel.setFlagVisto(lFlagVisto);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(
				this.getCodUfficioUtenteConnesso(), lCodUffMittente, lTipoOperazione, lAnno, lProgr,
				lFlagVisto);

		this.setLinkRitorno();

		lRicModel.setCodUfficioMittente(lCodUffMittente);
		lRicModel.setCodTipoOperazione(lCodUffMittente);

		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("FiltroRicerca", lRicModel);

		return PG_LISTA_ATTI_RICEVUTI;
	}
}
