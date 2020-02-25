package siap.siep.posizionematerialefasc.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PosizioneMaterialeFascController
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneMaterialeFasc
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
public interface IPosizioneMaterialeFasc {

	public PosizioneMaterialeFascModel ExInserisciPosizioneMaterialeFasc(
			PosizioneMaterialeFascModel aPosizioneMaterialeFasc) throws F3BException;

	public Vector ExRicercaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException;

	public Vector ExRicercaPosizioneMaterialeFascAttiva(BigDecimal aIdFascicolo) throws F3BException;

	public void ExCancellaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException;

	// public PosizioneMaterialeFascModel ExRicercaPosizioneMaterialeFascByKey (BigDecimal aKey) throws
	// F3BException;
	// public PosizioneMaterialeFascModel ExModificaPosizioneMaterialeFasc (PosizioneMaterialeFascModel
	// aPosizioneMaterialeFasc ) throws F3BException;
	// public void ExCancellaPosizioneMaterialeFasc (PosizioneMaterialeFascModel aPosizioneMaterialeFasc )
	// throws F3BException;

}