package siap.siep.decretoordinanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;

import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;

import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: DecretoOrdinanzaSiepController
 * </p>
 * <p>
 * Description: Classe Controller per DecretoOrdinanzaSiep
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
public interface IDecretoOrdinanzaSiep {

	public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento)
			throws F3BException;

	public EventoModel ExUpdateValidaOrdineEsecuzioneRevoca(EventoModel aEvento) throws F3BException;

	public DecretoOrdinanzaSiepModel ExInserisciDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento,
			CalcoloPenaModel aCalcoloPenaMod) throws F3BException;

	public String ExInserisciDecretoOrdinanzaWithoutSequence(DecretoOrdinanzaSiepModel aDecOrdSiepMod,
			Connection lConn) throws F3BException;

	public DecretoOrdinanzaSiepModel ExInserisciDecretoOrdinanzaSiepRipristino(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento,
			boolean aInterruzioneNonValida) throws F3BException;

	public DecretoOrdinanzaSiepModel ExInserisciRevocaDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento) throws F3BException;

	public Vector ExRicercaDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep)
			throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByKey(BigDecimal aKey) throws F3BException;

	public Vector<DecretoOrdinanzaSiepModel> ExRicercaDecretoOrdinanzaSiepByIdFasc(BigDecimal aIdFascicolo)
			throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByDecretoOrdinanzaIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException;

	public DecretoOrdinanzaSiepModel ExModificaDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep) throws F3BException;

	public void ExCancellaDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep)
			throws F3BException;

	/*
	 * public PenaResiduaModel calcolaPenaResiduaSospensione(PosizioneGiuridicaModel aPosizione,Date
	 * aDataSospensione,BigDecimal aIdEvento,PenaResiduaModel aPenaResidua) throws Exception;
	 */

	/**
	 *
	 * @param aDecreto
	 * @param aEvento
	 * @param aTipo
	 * @return
	 * @throws F3BException
	 */
	public DecretoOrdinanzaSiepModel ExInserisciOModificaDecretoSospensione(
			DecretoOrdinanzaSiepModel aDecreto, EventoModel aEvento, String aTipo,
			CalcoloPenaModel aCalcoloPenaMod) throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByOggettoProcedimento(String[] aOggetto,
			FascicoloSiepModel aFascicolo) throws F3BException;

	/**
	 * Inserisce i dati relativi a un provvedimento generico
	 * 
	 * @param aEvento
	 * @param aCampo
	 * @param aDecreto
	 * @param aOrdinanza
	 * @param aTenore
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciProvvedimentoGenerico(EventoModel aEvento, CampoNotaModel aCampo,
			DepositoDecretoModel aDecreto, DepositoOrdinanzaPcModel aOrdinanza, TenoreModel aTenore)
			throws F3BException;

	public EventoModel ExUpdateValidaProvvedimentoGenerico(EventoModel aEvento, String lCodStato)
			throws F3BException;

	// 01-09-2015 MEV_2 - Misure Sicurezza STEP2-
	public EventoModel ExInserisciProvvedimentoDecisioneCassazioneRiesame(EventoModel aEvento,
			EventoModel aOrdinanzaDecreto, DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep,
			MisuraSicurezzaModel aMisuraSicurezza) throws F3BException;

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByIdEventoSemplice(BigDecimal aIdEvento)
			throws F3BException;

	public void ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame(EventoModel aEvento,
			FascicoloSiepModel lFasMod) throws F3BException;

	public Vector ExRicercaDecretoOrdinanzaSiepGiudiceCassazione(BigDecimal aIdFas, String[] aCodici)
			throws F3BException;

}