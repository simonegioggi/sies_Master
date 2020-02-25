package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInserisciSospensioneSanzioneSostitutivaUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// recupero ID fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca delle precedenti comunicazioni di esecuzione sanzione sostitutiva
		// per il fascicolo
		IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		List lListaSanzioniSius = lCtrl1.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFasSius);

		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione
		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		if (lListaSanzioniSius != null && !lListaSanzioniSius.isEmpty()) {
			lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 1);
			if (lPerMod.getDataScadenza() == null && !lPerMod.getFlagMotivo().equals("03")) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Inserire prima Data Scadenza di Inizio o Ripresa Sanzione.");
			}
			if (lPerMod.getDataScadenza() != null && lPerMod.getFlagMotivo().equals("03")) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! La sanzione risulta già sospesa!");
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
			throw new SIUSException(SIUSException.USER_MESSAGE, "Inserire prima Inizio Sanzione.");
		}

		// ricerca delle autorita disponibili
		Option lOptionAut = new Option();
		if (lPerMod != null && !lPerMod.getCodTipoAutorita().equals("-")) {
			lOptionAut = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS(),
					lPerMod.getCodTipoAutorita());
		} else {
			lOptionAut = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		}
		setRequestAttribute("tipoAutorita", lOptionAut.toString());
		setRequestAttribute("listaSanzioniSius", lListaSanzioniSius);

		String lRetPage = PG_LOADINSERISCISOSPENSIONESANZIONESOSTITUTIVA;

		return lRetPage; // restituisce la jsp di VIEW
	}

}