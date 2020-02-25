package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInserisciInizioMisuraSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		/*
		 * questa funzione serve a caricare la maschera per l'inserimento della data di inizio della
		 * esecuzione misura sicurezza, per prima cosa occorre ricercare il * fascicolo e inserirlo come
		 * attributo "fascicoloSiusGP". Il fascicolo sarà ricercato con l'identificativo ereditato dalla
		 * maschera precedente prelevato tramite "getRequestBigDecimalParameter "CAMPO_ID_FASCICOLO_SIUS"
		 * Poichè nella maschera sarà necessario indicare l'autorità che ha inviato la comunicazione occorre
		 * caricare anche le autorita come attributo "tipoAutorita". Infine ricerchiamo eventuali precedenti
		 * comunicazione di data inizio misura sicurezza per questo fascicolo. Queste sono necessarie per il
		 * controllo della giusta cronologia delle date (DATA_INIZIO_ESECUZIONE e DATA_SCADENZA) e per le
		 * comunicazioni successive alla prima il campo della maschera "Note" diventerà obbligatorio
		 * 
		 */
		// ricerca del fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca delle autorita disponibili
		Option lOptionMess = new Option();
		lOptionMess = new Option(DecodificheManager.getInstance().getMotivoMisuraSicurezza(), 01);
		setRequestAttribute("MotivoEMS", lOptionMess.toString());

		// ricerca delle precedenti comunicazioni di esecuzione misura sicurezza
		// per il fascicolo
		IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		List lListaMisureSius = lCtrl1.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasSius);
		setRequestAttribute("listaMisureSius", lListaMisureSius);

		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		if (lListaMisureSius != null && lListaMisureSius.size() > 0) {

			lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 1);

			// 01 Inizio
			// 02 Sospensione
			// 03 Ripresa
			// se esiste un periodo inizio/ripresa completo (ossia anche con la data scadenza)
			// non è possibile inserirne un altro. (occorre prima inserire un periodo di sospensione)
			if ((lPerMod.getFlagMotivo().equals("01") || lPerMod.getFlagMotivo().equals("02"))
					&& lPerMod.getDataScadenza() != null) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Ripresa non attivabile la misura non risulta sospesa!");
			}
			if (lPerMod.getFlagMotivo().equals("03") && lPerMod.getDataScadenza() == null) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Ripresa non attivabile la sospensione non è completa!");
			}
			if (lPerMod.getIstDetIdIstitutoDetenzione() != null
					&& !lPerMod.getIstDetIdIstitutoDetenzione().equals("-"))
				caricoIstitutiDetenzione(lPerMod);
		}

		// ricerca delle autorita disponibili
		Option lOptionAut = new Option();
		if (lPerMod != null && !lPerMod.getCodTipoAutorita().equals("-")) {
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lPerMod.getCodTipoAutorita());
		} else {
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita());
		}
		setRequestAttribute("tipoAutorita", lOptionAut.toString());

		// ricerca record esecuzione_misura_sicurezza
		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
		if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");
		setRequestAttribute("lEMSModel", lEMSModel);

		String lRetPage = PG_LOADINSERISCIINIZIOMISURASICUREZZA;

		return lRetPage; // restituisce la jsp di VIEW
	}

}