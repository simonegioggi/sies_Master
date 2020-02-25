package siap.sius.rifasiep.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RiferimentoFascicoloSiepController
 * </p>
 * <p>
 * Description: Classe Controller per RiferimentoFascicoloSiep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IRiferimentoFascicoloSiep {

	public RiferimentoFascicoloSiepModel ExInserisciRiferimentoFascicoloSiep(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep) throws F3BException;

	public Vector ExRicercaRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep)
			throws F3BException;

	public RiferimentoFascicoloSiepModel ExRicercaRiferimentoFascicoloSiepByKey(BigDecimal aKey)
			throws F3BException;

	public RiferimentoFascicoloSiepModel ExModificaRiferimentoFascicoloSiep(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep) throws F3BException;

	public void ExCancellaRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep)
			throws F3BException;

	public void ExCancellaRiferimentoFascicoloSiep(BigDecimal idRiferimentoFascicoloSiep) throws F3BException;

	public Vector ExRicercaRiferimentoFascicoloSiepByIdFasSius(BigDecimal idFascicoloSius)
			throws F3BException;

	public void ExCancellaRiferimentoFascicoloSiepByIdFasSius(BigDecimal idFascicoloSius) throws F3BException;

	// 01-2015
	public BigDecimal ExInserisciRiferimentoSiepUpdateMisuraSic(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep, BigDecimal aIdMisura)
			throws F3BException;

	public void ExCancellaRiferimentoFascicoloSiepUpdateMisuraSic(MisuraSicurezzaModel aMisuraMod)
			throws F3BException;

}