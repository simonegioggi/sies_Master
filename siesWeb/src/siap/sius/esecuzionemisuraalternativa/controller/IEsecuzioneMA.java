package siap.sius.esecuzionemisuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;

/**
 * EsecuzioneMAController - Classe Controller per Esecuzione Misura Alternativa
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

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByAnnoProg(BigDecimal aAnno,
			BigDecimal aProg) throws F3BException;

	public EsecuzioneMisuraAlternativaModel ExModificaEMAbyFascicolo(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMA) throws F3BException;

	public BigDecimal ExGetNumRicercaEsecuzioneMisureAlternative(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso) throws F3BException;

	public Vector[] ExRicercaDettaglioEMAeCorrelati(BigDecimal aKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException;

	// MEV_2025-48: aggiunti metodi di ricerca per Scadenzario monitoraggio misure alternative espiate
	public Collection<EMAFascGPModel> ExRicercaDataScadenzaProcEsecMAPaginata(RicercaProcedimentoModel rpm,
			int parseInt) throws F3BException;

	public BigDecimal ExGetNumRicercaDataScadenzaProcEsecMA(RicercaProcedimentoModel rpm) throws F3BException;

}