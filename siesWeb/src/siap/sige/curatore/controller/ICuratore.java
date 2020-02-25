package siap.sige.curatore.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.curatore.model.CuratoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICuratore
 * </p>
 * <p>
 * Description: Classe Interfaccia Curatore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ICuratore {

	public CuratoreModel ExInserisciCuratore(CuratoreModel aCuratore) throws F3BException;

	public Vector ExRicercaCuratore(CuratoreModel aCuratore) throws F3BException;

	public CuratoreModel ExRicercaCuratoreByKey(BigDecimal aKey) throws F3BException;

	public CuratoreModel ExModificaCuratore(CuratoreModel aCuratore) throws F3BException;

	public void ExCancellaCuratore(BigDecimal IdCuratore) throws F3BException;

	public Vector ExElencoCbxCuratoriByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExRicercaCuratoreByCodUfficio(String aCodUfficio) throws F3BException;

	public int ExGetNumRicercaCuratore(CuratoreModel aCuratore) throws F3BException;

}