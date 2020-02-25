package siap.siep.beneficio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.beneficio.model.BeneficioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: BeneficioController
 * </p>
 * <p>
 * Description: Classe Controller per Beneficio
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
public interface IBeneficio {

	public void ExInserisciBeneficio(Vector aBenefici) throws F3BException;

	public Vector ExRicercaBeneficio(BeneficioModel aBeneficio) throws F3BException;

	public BeneficioModel ExRicercaBeneficioByKey(BigDecimal aKey) throws F3BException;

	public BeneficioModel ExModificaBeneficio(BeneficioModel aBeneficio) throws F3BException;

	public void ExCancellaBeneficio(BeneficioModel aBeneficio) throws F3BException;

	public String ExInserisciBeneficioWithoutSequence(ArrayList aBenefici, Connection lConn)
			throws F3BException;

	public BeneficioModel ExInserisciBeneficioTipOrario(BeneficioModel aBenMod, ArrayList aTipologie,
			String[] aIdPenAcc, BeneficioModel aBenNMMod) throws F3BException;

	public BeneficioModel ExModificaBeneficioTipologiaOrario(BeneficioModel aBeneficio, ArrayList aTipologie,
			String[] aIdPenAcc, BeneficioModel aBenNMMod) throws F3BException;

	public void ExCancellaBeneficioTipologiaOrario(BeneficioModel aBeneficio) throws F3BException;

	public BeneficioModel ExRicercaBeneficioByBenIdBeneficio(BigDecimal aKey) throws F3BException;

}