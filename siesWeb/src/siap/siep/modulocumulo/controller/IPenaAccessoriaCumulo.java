package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;

/**
 * <p>
 * Title: IPenaAccessoriaCumulo
 * </p>
 * <p>
 * Description: Interfaccia per PenaAccessoriaCumuloController
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPenaAccessoriaCumulo {

	public PenaAccessoriaCumuloModel ExInserisciPenaAccessoriaCumulo(
			PenaAccessoriaCumuloModel aPenaAccessoria) throws F3BException;

	public Vector ExRicercaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException;

	public Vector ExRicercaPeneAccessorieCumulo_Valide(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException;

	public PenaAccessoriaCumuloModel ExRicercaPenaAccessoriaCumuloByKey(BigDecimal aIdPenaCum)
			throws F3BException;

	public PenaAccessoriaCumuloModel ExModificaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException;

	public void ExCancellaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria) throws F3BException;

	public Vector<PenaAccessoriaCumuloModel> ExRicercaPenaAccessoriaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, boolean aFlagDatiFinali) throws F3BException;

	public void ExAggiornaPeneAccessorieDatiFinaliCumulo(
			Vector<PenaAccessoriaCumuloModel> aListaPeneAccessorie) throws F3BException;

	// MEV 26 CUMULO Step2
	public String ExInserisciPeneAccessorieCumuloWithoutSequence(
			Vector<PenaAccessoriaCumuloModel> aPeneAccessorie, Connection lConn) throws F3BException;

	public Vector<PenaAccessoriaCumuloModel> ExRicercaPenaAccessoriaCumuloByIdTitoloCum(BigDecimal aIdTitolo)
			throws F3BException;

}