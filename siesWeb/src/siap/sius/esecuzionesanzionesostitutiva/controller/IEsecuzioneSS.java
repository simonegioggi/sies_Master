package siap.sius.esecuzionesanzionesostitutiva.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EsecuzioneSSController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneSanzioneSostitutiva
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IEsecuzioneSS {

	public EsecuzioneSanzioneSostitutivaModel ExInserisciEsecuzioneSanzioneSostitutiva(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSS) throws F3BException;

	public Vector ExRicercaEsecuzioneSanzioneSostitutiva(EsecuzioneSanzioneSostitutivaModel aEsecuzioneSS)
			throws F3BException;

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByKey(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
			BigDecimal aKey) throws F3BException;

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
			BigDecimal aKey, Connection aConn) throws F3BException;

	public EsecuzioneSanzioneSostitutivaModel ExModificaEsecuzioneSanzioneSostitutiva(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSS) throws F3BException;

	public void ExCancellaEsecuzioneSanzioneSostitutiva(BigDecimal aKey) throws F3BException;

	// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
	public Vector ExRicercaEsecuzioneSanzioniSostitutive(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum, String lCodContenuto) throws F3BException;

	// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
	public Vector ExRicercaDettaglioEsecuzioneSS(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException;

	// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
	public Vector ExRicercaDettaglioEsecuzioneSS(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn, String lCodContenuto) throws F3BException;

	/*
	 * public Vector ExRicercaDettaglioEsecuzioneSSbyFascicolo (BigDecimal aKey, BigDecimal aIdSoggetto,
	 * String lUfficioUtenteConnesso ) throws F3BException;
	 */
	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByAnnoProg(
			BigDecimal aAnno, BigDecimal aProg) throws F3BException;

	public EsecuzioneSanzioneSostitutivaModel ExModificaESSbyFascicolo(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSS) throws F3BException;
	
	// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
	public BigDecimal ExGetNumRicercaEsecuzioneSanzioniSostitutive(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException;

	// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
	public Vector[] ExRicercaDettaglioESSeCorrelati(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException;

}