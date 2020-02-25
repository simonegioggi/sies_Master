package siap.siep.istruttoria.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IIstruttoria {

	public ByteArrayOutputStream ExStampaIstruttoria(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExStampaCopertineMultiple(FascicoloSiepModel aFasc, UtenteModel aUtente)
			throws F3BException;

	public boolean ExStampaInizioEsecuzioneMultiple(FascicoloSiepModel aFasc, UtenteModel aUtente,
			UfficioModel aUfficio, BigDecimal lSequence) throws F3BException;

	public Vector ExCercaIntervalloFascicoli(FascicoloSiepModel aFasc, boolean aValidato) throws F3BException;

	public BigDecimal ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple(FascicoloSiepModel aFasc,
			UtenteModel aUtente, UfficioModel aUfficio) throws F3BException;

	public String ExControllaNotizieDiReatoRege(SoggettoModel aSogg, SentenzaModel aSentenza)
			throws F3BException;

}