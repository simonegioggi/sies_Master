package siap.siep.penacumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.penacumulo.model.PenaCumuloModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per PenaCumulo
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
public interface IPenaCumulo {

	public PenaCumuloModel ExInserisciPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException;

	public Vector ExRicercaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException;

	public PenaCumuloModel ExRicercaPenaCumuloByKey(BigDecimal aKey) throws F3BException;

	public PenaCumuloModel ExModificaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException;

	public void ExCancellaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException;

	public PenaCumuloModel ExRicercaPenaCumuloByIdCumulo(BigDecimal aKey) throws F3BException;

	/**
	 * Ricerca l'ultima PENA_CUMULO inserita per il fascicolo e VALIDATA
	 * 
	 * @param aIdFascicolo
	 *            id del fascicolo
	 * @return ultima pena cumulo o null se non presente
	 * @throws F3BException
	 */
	public PenaCumuloModel ExRicercaUltimaPenaCumuloByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public String ExInserisciPenaCumuloWithoutSequence(ArrayList aPeneCumuli, Connection lConn)
			throws F3BException;

}