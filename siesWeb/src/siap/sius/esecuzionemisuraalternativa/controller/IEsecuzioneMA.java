package siap.sius.esecuzionemisuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EsecuzioneMAController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneMisuraAlternativa
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
public interface IEsecuzioneMA {

	public EsecuzioneMisuraAlternativaModel ExInserisciEsecuzioneMisuraAlternativa(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMA) throws F3BException;

	public Vector ExRicercaEsecuzioneMisuraAlternativa(EsecuzioneMisuraAlternativaModel aEsecuzioneMA)
			throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByKey(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(BigDecimal aKey,
			Connection aConn) throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExModificaEsecuzioneMisuraAlternativa(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMA) throws F3BException;

	public void ExCancellaEsecuzioneMisuraAlternativa(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaEsecuzioneMisureAlternative(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum) throws F3BException;

	public Vector ExRicercaDettaglioEsecuzioneMA(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector ExRicercaDettaglioEsecuzioneMA(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn) throws F3BException;

	/*
	 * public Vector ExRicercaDettaglioEsecuzioneMAbyFascicolo (BigDecimal aKey, BigDecimal aIdSoggetto,
	 * String lUfficioUtenteConnesso ) throws F3BException;
	 */
	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByAnnoProg(BigDecimal aAnno,
			BigDecimal aProg) throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExModificaEMAbyFascicolo(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMA) throws F3BException;

	public BigDecimal ExGetNumRicercaEsecuzioneMisureAlternative(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector[] ExRicercaDettaglioEMAeCorrelati(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException;

}