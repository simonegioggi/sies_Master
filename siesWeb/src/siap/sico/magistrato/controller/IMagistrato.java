package siap.sico.magistrato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.magistrato.model.MagistratoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per Magistrato
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
public interface IMagistrato {

	public MagistratoModel ExInserisciMagistrato(MagistratoModel aMagistrato) throws F3BException;

	public Vector ExRicercaMagistrato(MagistratoModel aMagistrato) throws F3BException;

	/*
	 * public MagistratoModel ExRicercaMagistratoByKey (BigDecimal aKey) throws F3BException;
	 */

	public MagistratoModel ExRicercaMagistratoByCod(String aCod) throws F3BException;

	public Vector ExRicercaMagistratoByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExElencoCbxMagistratiByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExElencoCbxMagByCodComuneCodTipoUff(String aCodComune, String aCodTipoUfficio)
			throws F3BException;

	public MagistratoModel ExModificaMagistrato(MagistratoModel aMagistrato) throws F3BException;

	public void ExCancellaMagistrato(String aCodMagistrato, String aCodUff) throws F3BException;

	public Vector ExRicercaMagistratoByCognome(String aCognome, String aUfficio) throws F3BException;

	public MagistratoModel ExRicercaMagistratoByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public Vector ExRicercaMagistratoPaged(MagistratoModel aMagistrato, int aPage) throws F3BException;

	public BigDecimal ExGetCountMagistratiPaged(MagistratoModel aMagistrato) throws F3BException;

	public String ExInserisciMagistratoWithoutSequence(MagistratoModel aMagistrato, Connection lConn)
			throws F3BException;

	/**
	 * Effettua la ricerca in like sul cognome
	 * 
	 * @param aCognome
	 * @param aUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMagistratoByCognomeUfficio(String aCognome, String aUfficio) throws F3BException;

	public MagistratoModel ExRicercaMagistratoByEvento(BigDecimal aIdEvento) throws F3BException;

	public Vector ExElencoCbxMagistratiValidiByCodUfficio(String aCodUfficio) throws F3BException;

}