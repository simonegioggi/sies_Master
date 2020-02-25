package siap.siep.tipologiaorario.controller;

/**
* <p>Title: TipologiaOrarioController</p>
* <p>Description: Classe Controller per TipologiaOrario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ITipologiaOrario {

	public TipologiaOrarioModel ExInserisciTipologiaOrario(TipologiaOrarioModel aTipologiaOrario)
			throws F3BException;

	public Vector ExRicercaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException;

	public void ExModificaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException;

	public void ExCancellaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException;

	public BigDecimal ExGetCountTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException;

	public TipologiaOrarioModel ExRicercaTipologiaOrarioById(BigDecimal aIdTipologiaOrario)
			throws F3BException;

	public Vector ExRicercaTipologiaOrarioPaged(TipologiaOrarioModel aTipologiaOrario, int aPage)
			throws F3BException;

	public Vector ExRicercaTipologiaOrarioByIdBeneficio(BigDecimal aIdBeneficio) throws F3BException;

	// MEV26 CUMULO
	public Vector ExRicercaTipologiaOrarioByIdBeneficioCumulo(BigDecimal aIdBeneficioCumulo)
			throws F3BException;

}