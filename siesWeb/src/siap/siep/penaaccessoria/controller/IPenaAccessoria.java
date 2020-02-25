package siap.siep.penaaccessoria.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaAccessoriaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaAccessoria
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
public interface IPenaAccessoria {

	public PenaAccessoriaModel ExInserisciPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException;

	public Vector ExRicercaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria) throws F3BException;

	public PenaAccessoriaModel ExRicercaPenaAccessoriaByKey(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException;

	public PenaAccessoriaModel ExModificaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException;

	public void ExCancellaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria) throws F3BException;

	public String ExInserisciPenaAccessoriaWithoutSequence(ArrayList aPene, Connection lConn)
			throws F3BException;

	public PenaAccessoriaModel ExModificaOrdinanzaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException;

	public PenaAccessoriaModel ExModificaCodNuovoTipoPA(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException;

	public boolean ExistPASostitutiva(BigDecimal aIdPenaAccessoria) throws F3BException;

	public Vector ExRicercaPenaAccessoriaNoError(PenaAccessoriaModel aPenaAccessoria) throws F3BException;

	// public EventoModel ExUpdateValidaEsecPA(EventoModel aEvento, FascicoloSiepModel aFascicolo)
	// throws F3BException;

}