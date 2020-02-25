package siap.siep.modulocumulo.controller;

/**
* <p>Title: MisuraSicurezzaCumuloController</p>
* <p>Description: Classe Controller per MisuraSicurezzaCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;

@SuppressWarnings("rawtypes")
public interface IMisuraSicurezzaCumulo {

	public MisuraSicurezzaCumuloModel ExInserisciMisuraSicurezzaCumulo(
			MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo) throws F3BException;

	public Vector ExRicercaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException;

	public void ExModificaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException;

	public void ExCancellaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException;

	public BigDecimal ExGetCountMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException;

	public MisuraSicurezzaCumuloModel ExRicercaMisuraSicurezzaCumuloById(BigDecimal aIdMisuraSicurezzaCumulo)
			throws F3BException;

	public Vector ExRicercaMisuraSicurezzaCumuloPaged(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo,
			int aPage) throws F3BException;

	public Vector<MisuraSicurezzaCumuloModel> ExRicercaMisureSicurezzaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, boolean aFlagDatiFinali) throws F3BException;

	public void ExAggiornaMisureDatiFinaliCumulo(Vector<MisuraSicurezzaCumuloModel> aListaMisure,
			BigDecimal aIdFascMS, BigDecimal aIdDatiFinali) throws F3BException;

	// MEV 26 CUMULO Step2
	public String ExInserisciMisuraSicurezzaCumuloWithoutSequence(
			Vector<MisuraSicurezzaCumuloModel> aVecMisure, Connection lConn) throws F3BException;

	public Vector<MisuraSicurezzaCumuloModel> ExRicercaMisureSicurezzaCumuloByIdTitoloCum(
			BigDecimal aIdTitolo) throws F3BException;

}