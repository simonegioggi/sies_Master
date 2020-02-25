package siap.siep.posizionemateriale.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascicoliModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PosizioneMaterialeController
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneMateriale
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
public interface IPosizioneMateriale {

	public PosizioneMaterialeModel ExInserisciPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException;

	public Vector ExRicercaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException;

	public PosizioneMaterialeFascicoliModel ExRicercaPosizioneMaterialeFascicoli(
			PosizioneMaterialeModel aPosizioneMateriale) throws F3BException;

	public Vector ExRicercaPosizioneMaterialePagina(PosizioneMaterialeModel aPosizioneMateriale, int aPageNum)
			throws F3BException;

	public BigDecimal ExGetNumRicercaPosizioneMaterialePagina(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException;

	public void ExCancellaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale) throws F3BException;

	public PosizioneMaterialeModel ExModificaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException;

}