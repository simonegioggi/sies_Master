package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.BeneficioCumuloModel;

/**
 * <p>
 * Title: BeneficioCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Beneficio
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IBeneficioCumulo {

	public void ExInserisciRevocaBeneficioCumulo(BeneficioCumuloModel aBenefici, BigDecimal aKeyBeneficio)
			throws F3BException;
  
	public BeneficioCumuloModel ExModificaRevocaBeneficioCumulo(BeneficioCumuloModel aBeneficio,
			BigDecimal aKeyBeneficio, String aDeasocia, BigDecimal aIdBeneficioConcesso) throws F3BException;

  public Vector ExRicercaBeneficioCumulo(BeneficioCumuloModel aBeneficio) throws F3BException;

  public BeneficioCumuloModel ExRicercaBeneficioCumuloByKey(BigDecimal aKey) throws F3BException;
  
	public BeneficioCumuloModel ExRicercaBeneficioCumuloByKeyBeneficioOrig(BigDecimal aKey)
			throws F3BException;

  public void ExCancellaBeneficioCumuloRevoca(BeneficioCumuloModel aBeneficioRevoca) throws F3BException;

  // MEV 26 Cumulo Step2
	public String ExInserisciBeneficiCumuloWithoutSequence(Vector<BeneficioCumuloModel> aBenefici,
			Connection lConn) throws F3BException;
  // End MEV 26
  
	public BeneficioCumuloModel ExInserisciBeneficioCumuloTipOrario(BeneficioCumuloModel aBenMod,
			ArrayList aTipologie, String[] aIdPenAcc, BeneficioCumuloModel aBenNMMod) throws F3BException;
 
	public BeneficioCumuloModel ExModificaBeneficioCumuloTipologiaOrario(BeneficioCumuloModel aBeneficio,
			ArrayList aTipologie, String[] aIdPenAcc, BeneficioCumuloModel aBenNMMod) throws F3BException;

  public void ExCancellaBeneficioCumuloTipologiaOrario(BeneficioCumuloModel aBeneficio) throws F3BException;
  
	public BeneficioCumuloModel ExRicercaBeneficioCumuloByBenIdBeneficioCum(BigDecimal aKey)
			throws F3BException;
  // Mev70
	public String ExRicercaBeneficioCumuloByTipoNaturaTitoloCum(BigDecimal aKey, String aCodNat,
			Vector<String> aCodTipoBen) throws F3BException;
  
}