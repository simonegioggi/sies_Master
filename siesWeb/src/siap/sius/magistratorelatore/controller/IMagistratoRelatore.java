package siap.sius.magistratorelatore.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoRelatoreController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoRelatore
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
public interface IMagistratoRelatore {

	public MagistratoRelatoreModel ExInserisciMagistratoRelatore(MagistratoRelatoreModel aMagRelMod)
			throws F3BException;

	public MagistratoRelatoreModel ExRicercaMagRelByFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaMagRelCorrentePrecedenteByFascicolo(BigDecimal aKey) throws F3BException;

	public MagistratoRelatoreModel ExRicercaEstesaMagRelByFascicolo(BigDecimal aKey) throws F3BException;

	/**
	 * Metodo per effettuare l'aggiornamento del magistrato relatore su n fascicoli contemporaneamente
	 * 
	 * @param aMagRelModel
	 *            - Nuovo Magistrato Relatore
	 * @param aListaFascicoli
	 *            - lista degli IdFascicoli da agiornare
	 * @throws F3BException
	 */
	public void ExModificaMultiplaMagistratoRelatore(MagistratoRelatoreModel aMagRelModel,
			String[] aListaFascicoli) throws F3BException;

}