package siap.sius.esecuzionemisurasicurezza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EsecuzioneMSController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneMisuraSicurezza
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
public interface IEsecuzioneMS {

	public EsecuzioneMisuraSicurezzaModel ExInserisciEsecuzioneMisuraSicurezza(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMS) throws F3BException;

	public Vector ExRicercaEsecuzioneMisuraSicurezza(EsecuzioneMisuraSicurezzaModel aEsecuzioneMS)
			throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByKey(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(BigDecimal aKey,
			Connection aConn) throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExModificaEsecuzioneMisuraSicurezza(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMS) throws F3BException;

	public void ExCancellaEsecuzioneMisuraSicurezza(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaEsecuzioneMisureSicurezza(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum) throws F3BException;

	public Vector ExRicercaDettaglioEsecuzioneMS(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector ExRicercaDettaglioEsecuzioneMS(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn) throws F3BException;

	/*
	 * public Vector ExRicercaDettaglioEsecuzioneMSbyFascicolo (BigDecimal aKey, BigDecimal aIdSoggetto,
	 * String lUfficioUtenteConnesso ) throws F3BException;
	 */
	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByAnnoProg(BigDecimal aAnno,
			BigDecimal aProg) throws F3BException;

	public EsecuzioneMisuraSicurezzaModel ExModificaEMSbyFascicolo(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMS) throws F3BException;

	public BigDecimal ExGetNumRicercaEsecuzioneMisureSicurezza(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector[] ExRicercaDettaglioEMSeCorrelati(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector ExRicercaEsecuzioneMisureSicRidByIdOrdinanza(BigDecimal aKey) throws F3BException;

}