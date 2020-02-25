package siap.siep.penapecuniaria.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RichiestaConversioneController
 * </p>
 * <p>
 * Description: Classe Controller per RichiestaConversione
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
public interface IRichiestaConversione {

	public RichiestaConversioneModel ExInserisciRichiestaConversione(
			RichiestaConversioneModel aRichiestaConversione) throws F3BException;

	public RichiestaConversioneModel ExInserisciRichiestaConversione(
			RichiestaConversioneModel aRichiestaConversione, EventoModel aEvento,
			DettaglioFascicoloModel aDettaglioFascicolo) throws F3BException;

	public Vector ExRicercaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public void ExModificaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public void ExModificaRichiestaConversione(RichiestaConversioneModel aRicConvMod,
			FascicoloSiepModel aFasSiepMod) throws F3BException;

	public void ExCancellaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public BigDecimal ExGetCountRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public RichiestaConversioneModel ExRicercaRichiestaConversioneById(BigDecimal aIdRichiestaConversione)
			throws F3BException;

	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdSenzaEvento(
			BigDecimal aIdRichiestaConversione) throws F3BException;

	public Vector ExRicercaRichiestaConversionePaged(RichiestaConversioneModel aRichiestaConversione,
			int aPage) throws F3BException;

	public RichiestaConversioneModel ExInserisciRichiestaConversionedaClasseI(SoggettoModel aSogMod,
			EventoModel aEvento, FascicoloSiepModel aFascicoloSiep,
			DettaglioFascicoloModel aDettaglioFascicolo, RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public ByteArrayOutputStream exStampaCP(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdFascicoloSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException;

	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdFascicoloSiepClasseI(
			BigDecimal aIdFascicoloSiep) throws F3BException;

	public Vector ExRicercaRichiesteConversionePenePecuniarie(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public Vector ExRicercaRichiesteConversioniValide(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public Vector ExRicercaRichiesteConversionePenePecuniarieByIdFascicoloSius(BigDecimal aIdFascicoloSius)
			throws F3BException;

	public Vector ExRicercaRichiestaConversioneEstesa(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException;

	public RichiestaConversioneModel ExInserisciRichiestaEvento(
			RichiestaConversioneModel aRichiestaConversione, EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, String StatoPro) throws F3BException;

	public void inserimentoScadenzario(RichiestaConversioneModel aRichiestaConversione, Connection lConn)
			throws F3BException;

	public String ExInserisciRichiesteConversioniWithoutSequence(ArrayList aRichiesteConversioni,
			Connection lConn) throws F3BException;

	public EventoNotificaModel ExInserisciDecisioneSorveglianza(EventoNotificaModel aEventoNotSIEP,
			EventoModel aEventoSIUS, RichiestaConversioneModel aRichiestaConversioneModel,
			ScambioSanzioneModel aScambiSanzioneModel, TenoreModel aTenoreModel,
			DepositoDecretoModel aDepoDecModel, DepositoOrdinanzaPcModel aDepOrdPcModel,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public FascicoloSiepModel ExInserisciFascicolodaClasseVII(SoggettoModel aSoggetto, EventoModel aEvento,
			FascicoloSiepModel aFascicoloSiep, DettaglioFascicoloModel aDettaglioFascicolo,
			FascicoloSiepModel aFascicoloClasseVIISiep, AnnotazioneManualeModel AnnMan,
			StatoProcedimentoModel lStatoProcMod) throws F3BException;

	public FascicoloSiepModel ExArchiviazioneClasseVII(FascicoloSiepModel aFasClasseVII,
			BigDecimal aIdEvento, String lFlagPenaScaduta) throws F3BException;

	public EventoModel ExRicercaEventoSorvDiRichiestaConversione(String aIdRicConv, EventoModel aModel,
			String[] aTipoProv, String[] aCodMotivo, String[] aCodEsito) throws F3BException;

	// Inizio 01/02/2016 -
	public void ExModificaDataDepositoRichiestaConversione(BigDecimal aIdRichiestaConversione,
			java.util.Date aDataEmissione) throws F3BException;
	// Fine 01/02/2016

}