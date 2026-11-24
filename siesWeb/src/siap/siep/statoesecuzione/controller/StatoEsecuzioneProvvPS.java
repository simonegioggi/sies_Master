package siap.siep.statoesecuzione.controller;

import siap.siep.statoesecuzione.model.EventoModel;

// 20261124-MEV_2026-124_SG - Provvedimento/annotazione di pena sostitutiva: aggiunge al Certificato Stato Esecuzione
// i dati della pena sostitutiva residua collegata all'evento
public class StatoEsecuzioneProvvPS extends StatoEsecuzioneProvv {

	public StatoEsecuzioneProvvPS(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	public void elabora(siap.sico.evento.model.EventoModel aEvento) {
		super.elabora(aEvento);
		EventoModel lEvento = this.mEventoStatoEsecuzione;
		if (lEvento == null || aEvento.getIdEvento() == null)
			return;
		String lSanzione = new PenaSostitutivaStatoEsecuzioneDati().getDescrizioneSanzioneResidua(aEvento.getIdEvento());
		if (lSanzione == null)
			return;
		String lPena = lEvento.getStringaPenaResidua();
		String lTesto = "Pena sostitutiva da espiare: " + lSanzione;
		lEvento.setStringaPenaResidua(lPena == null || lPena.trim().length() == 0 ? lTesto : lPena + " - " + lTesto);
	}
}
