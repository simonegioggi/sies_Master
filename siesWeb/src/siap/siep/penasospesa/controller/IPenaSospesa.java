package siap.siep.penasospesa.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaAccessoriaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPenaSospesa {

	public EventoModel ExInserisciAnnotazioneRevoca(EventoModel aPenaAccessoria) throws F3BException;

	public FascicoloSiepModel ExInserisciFascicolodaClasseIII(SoggettoModel aSoggetto, EventoModel aEvento,
			FascicoloSiepModel aFascicoloSiep, DettaglioFascicoloModel aDettaglioFascicolo,
			FascicoloSiepModel aFascicoloClasseIIISiep, AnnotazioneManualeModel AnnMan) throws F3BException;

	public EventoNotificaModel ExInserisciRichiestaRevoca(AnnotazioneManualeModel aAnnotazioneManuale,
			EventoNotificaModel aEventoNotifica, CampoNotaModel aCampoNote, Vector aReati,
			DettaglioPenaComplessivaModel aDettaglioPenaComplessiva) throws F3BException;

	public Vector ExRicercaReatiByAnnotazioneMan(BigDecimal aKey) throws F3BException;

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaByAnnotazioneMan(BigDecimal aKey)
			throws F3BException;

	public ByteArrayOutputStream ExStampaRichiestaRevoca(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	public AnnotazioneManualeModel ExInserisciAnnotazioneEventoScadenzario(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento, Date aDataIrrevocabilita,
			boolean aCancellaScadenzario) throws F3BException;

}