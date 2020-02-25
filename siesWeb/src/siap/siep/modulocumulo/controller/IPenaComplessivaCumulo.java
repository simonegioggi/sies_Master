package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;

/**
 * <p>
 * Title: PenaComplessivaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per PenaComplessiva
 * </p>
 * <p>
 * in ambito Cumulo (Pena_complessiva_Cumulo)
 * </p>
 */
@SuppressWarnings("rawtypes")
public interface IPenaComplessivaCumulo {

	public PenaComplessivaCumuloModel ExInserisciPenaComplessivaCumulo(
			PenaComplessivaCumuloModel aPenaComplessiva) throws F3BException;

	public DettaglioPenaComplessivaCumuloModel ExRicercaPenaCompSanzioneSostContinuazioniCumByIdTitolo(
			BigDecimal aIdTitolo) throws F3BException;

	public PenaComplessivaCumuloModel ExRicercaPenaComplessivaCumByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException;

	public void ExCancellaPenaComplessivaCumulo(PenaComplessivaCumuloModel aPenaComplessiva)
			throws F3BException;

	public DettaglioPenaComplessivaCumuloModel ExRicercaPenaCompSanzioneSostContinuazioniCumByKey(
			BigDecimal aIdPenaComplessiva) throws F3BException;

	public PenaComplessivaCumuloModel ExInserisciPenaCompSanzioneSostContinuazioniCum(
			PenaComplessivaCumuloModel aPenaComplessiva, SanzioneSostitutivaCumuloModel aSanzioneSostitutiva,
			List aContinuazioni) throws F3BException;

	public void ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioniCum(
			PenaComplessivaCumuloModel aPenaComplessiva) throws F3BException;

	public PenaComplessivaSanzioneSostitutivaCumuloModel ExRicercaPenaComplessivaSanzioneSostitutivaCumByKey(
			BigDecimal aIdPenaComplessivaCum) throws F3BException;

	public PenaComplessivaSanzioneSostitutivaCumuloModel ExModificaPenaComplessivaSanzioneSostitutivaCum(
			PenaComplessivaCumuloModel aPenaComplessiva, SanzioneSostitutivaCumuloModel aSanzioneSostitutiva,
			boolean aflagSanzioneSostitutiva) throws F3BException;

	public void ExInserisciUlterioriContinuazioniCumulo(List aContinuazioni) throws F3BException;

	// MEV 26 Cumulo Step2
	public String ExInserisciPenaComplessivaSanzioneSostContinuazioniCumuloWithoutSequence(
			PenaComplessivaCumuloModel aPenaComplessiva, Connection lConn) throws F3BException;

}