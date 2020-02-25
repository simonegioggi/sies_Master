package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInserisciSospensioneMisuraSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// recupero ID fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca delle precedenti comunicazioni di esecuzione misura sicurezza
		// per il fascicolo
		IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		List lListaMisureSius = lCtrl1.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasSius);

		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione
		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		if (lListaMisureSius != null && !lListaMisureSius.isEmpty()) {
			lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 1);
			if (lPerMod.getDataScadenza() == null && !lPerMod.getFlagMotivo().equals("03")) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Inserire prima Data Scadenza di Inizio o Ripresa Misura.");
			}
			if (lPerMod.getDataScadenza() != null && lPerMod.getFlagMotivo().equals("03")) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! La misura risulta già sospesa!");
			}
			if (lPerMod.getDataScadenza() == null && lPerMod.getEveIdEvento() != null) {
				EventoModel lEveMod = new EventoModel();
				IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
				lEveMod = lEveCtrl.ExRicercaEventoByKey(lPerMod.getEveIdEvento());

				if (lEveMod.getFlagDocumentoRegistrato() != null
						&& lEveMod.getFlagDocumentoRegistrato().equals("N"))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione. Periodo agganciato ad un decreto o ordinanza non validato impossibile proseguire");
			}
		} else {
			throw new SIUSException(SIUSException.USER_MESSAGE, "Inserire prima Inizio Misura.");
		}

		// ricerca delle autorita disponibili
		Option lOptionAut = new Option();
		if (lPerMod != null && !lPerMod.getCodTipoAutorita().equals("-")) {
			lOptionAut = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDSTrattino(),
					lPerMod.getCodTipoAutorita());
		} else {
			lOptionAut = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDSTrattino());
		}
		// setRequestAttribute("tipoAutorita", lOptionAut.toString());
		setRequestAttribute("tipoAutorita", "-" + lOptionAut);
		setRequestAttribute("listaMisureSius", lListaMisureSius);

		String lRetPage = PG_LOADINSERISCISOSPENSIONEMISURASICUREZZA;

		return lRetPage; // restituisce la jsp di VIEW
	}

}