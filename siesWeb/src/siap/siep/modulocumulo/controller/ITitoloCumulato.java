package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

/**
 * <p>
 * Title: TitoloCumulatoController
 * </p>
 * <p>
 * Description: Classe Controller per TitoloCumulato
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ITitoloCumulato {

	public TitoloCumulatoModel ExInserisciTitoloCumulato(TitoloCumulatoModel aTitoloCumulato)
			throws F3BException;

	public Vector ExRicercaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoloCumulatoPerRevocaBeneficio(BigDecimal aIdTitoloCumulato,
			BigDecimal aIdIstru, String TipoBen) throws F3BException;

	public void ExModificaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException;

	public void ExCancellaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException;

	public BigDecimal ExGetCountTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException;

	public TitoloCumulatoModel ExRicercaTitoloCumulatoById(BigDecimal aIdTitoloCumulato) throws F3BException;

	public Vector ExRicercaTitoloCumulatoPaged(TitoloCumulatoModel aTitoloCumulato, int aPage)
			throws F3BException;

	public void ExIncludiEscludiTitoloDaIstruttoria(TitoloCumulatoModel aTitoloCumulato) throws F3BException;

	public Vector<MisuraAlternativaAggregatoModel> ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged(
			BigDecimal aIdFascicolo, int aPage) throws F3BException;

	public TitoloCumulatoModel ExRicercaTitoloCumulatoByIstrIdOrig(BigDecimal aIdIstruttoria,
			BigDecimal aIdTitoloOrigine) throws F3BException;

	public void ExCancellaTitoloDaIstruttoriaById(BigDecimal aIdTitoloCumulato, String aTipoIscrizione)
			throws F3BException;

	// MEV 26 Cumulo Step2
	public String ExInserisciTitoliCumulatiWithoutSequence(TitoloCumulatoModel aTitCumModel, Connection lConn)
			throws F3BException;

	public String ExInserisciProcedimentoCumulatoWithoutSequence(
			ProcedimentoCumulatoModel aProcedimentoCumulato, Connection lConn) throws F3BException;

	// Metodi per il Soggetto Cumulato
	public SoggettoCumulatoModel ExRicercaSoggettoCumulatoByIdTitolo(BigDecimal aIdTitoloCumulato)
			throws F3BException;

	// Metodi per il Procedimento Cumulato
	public ProcedimentoCumulatoModel ExInserisciProcedimentoCumulato(
			ProcedimentoCumulatoModel aProcedimentoCumulato) throws F3BException;

	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoById(BigDecimal aIdProcedimentoCumulato)
			throws F3BException;

	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoByIdTitolo(BigDecimal aIdTitoloCumulato)
			throws F3BException;

	public void ExModificaProcedimentoCumulato(ProcedimentoCumulatoModel aProcedimentoCumulato)
			throws F3BException;

	public void ExCancellaProcedimentoCumulato(ProcedimentoCumulatoModel aProcedimentoCumulato)
			throws F3BException;

	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoByDatiFascicoloSiep(
			ProcedimentoCumulatoModel aProcedimentoCumulato, BigDecimal aIdIstruttoria) throws F3BException;

	//
	public TitoloCumulatoModel ExRicercaTitoloCumulatoPeneAccessorieCum(BigDecimal aIdTitoloCumulato,
			Vector<String> listaIdPenaAcc) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliCumulatiPeneAccessorieCum(String[] lIdTitoliSelezionati)
			throws F3BException;

	public TitoloCumulatoModel ExRicercaTitoloCumulatoStatoEsecTitoloCum(BigDecimal aIdTitoloCumulato,
			BigDecimal aIdStatoEsecTCum) throws F3BException;

}