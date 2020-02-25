package siap.sige.posizionematerialefascsige.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IPosizioneMaterialeFascSige
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneMaterialeFasc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPosizioneMaterialeFascSige {

	public PosizioneMaterialeFascModel ExInserisciPosizioneMaterialeFasc(
			PosizioneMaterialeFascModel aPosizioneMaterialeFasc) throws F3BException;

	public Vector ExRicercaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException;

	public Vector<PosizioneMaterialeFascModel> ExRicercaPosizioneMaterialeFascAttiva(BigDecimal aIdFascicolo)
			throws F3BException;

	public void ExCancellaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException;

}