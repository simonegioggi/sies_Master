package siap.sico.magistratocompetente.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoCompetenteController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoCompetente
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
public interface IMagistratoCompetente {

	public MagistratoCompetenteModel ExInserisciMagistratoCompetente(
			MagistratoCompetenteModel aMagistratoCompetente) throws F3BException;

	public Vector ExRicercaMagistratoCompetente(MagistratoCompetenteModel aMagistratoCompetente)
			throws F3BException;

	public MagistratoCompetenteModel ExRicercaMagistratoCompetenteByKey(BigDecimal aKey) throws F3BException;

	public MagistratoCompetenteMagistratoModel ExRicercaMagistratoCompetenteByFascicolo(BigDecimal aKey)
			throws F3BException;

	public MagistratoCompetenteModel ExModificaMagistratoCompetente(
			MagistratoCompetenteModel aMagistratoCompetente) throws F3BException;

	public void ExCancellaMagistratoCompetente(MagistratoCompetenteModel aMagistratoCompetente)
			throws F3BException;

	public MagistratoCompetenteModel ExInserisciAggiornaMagistratoCompetente(
			MagistratoCompetenteMagistratoModel aMagistratoCompetenteMagistrato) throws F3BException;

	public MagistratoCompetenteMagistratoModel ExRicercaMagistratoCompetenteByFascicoloDataFine(
			BigDecimal aKey) throws F3BException;

	public String ExInserisciMagistratoCompetenteWithoutSequence(BigDecimal aIdFascicolo,
			MagistratoCompetenteMagistratoModel aMagCompetente, Connection lConn) throws F3BException;

	/**
	 * Metodo per effettuare l'aggiornamento del magistrato competente su n fascicoli contemporaneamente
	 * 
	 * @param aMagCompModel
	 *            - Nuovo Magistrato Competente
	 * @param aListaFascicoli
	 *            - lista degli IdFascicoli da agiornare
	 * @throws F3BException
	 */
	public void ExModificaMultiplaMagistratoCompetente(MagistratoCompetenteModel aMagCompModel,
			String[] aListaFascicoli) throws F3BException;

}