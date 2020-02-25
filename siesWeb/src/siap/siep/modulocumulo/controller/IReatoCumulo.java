package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.ReatoCumuloModel;

/**
 * <p>
 * Title: IReatoCumulo / ReatoCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Reato_Cumulo
 * </p>
 */
@SuppressWarnings("rawtypes")
public interface IReatoCumulo {

	public ReatoCumuloModel ExInserisciReatiCumulo(ArrayList aReati) throws F3BException;

	public Vector<ReatoCumuloModel> ExRicercaReatoCumulo(ReatoCumuloModel aReatoCum) throws F3BException;

	public ReatoCumuloModel ExRicercaReatoCumuloByKey(BigDecimal aKey) throws F3BException;

	public void ExCancellaReatoCumulo(ReatoCumuloModel aReato) throws F3BException;

	public ReatoCumuloModel ExModificaReatoCumulo(ReatoCumuloModel aReato) throws F3BException;

	public void ExOrganizzaReatiCum(Vector reatiMod, Vector reatiCanc) throws F3BException;

	public ReatoCumuloModel ExModificaPenaReatoCumulo(ReatoCumuloModel aReato, Vector aNorme)
			throws F3BException;

	public Vector ExRicercaReatoCircostanzaCumByTitoloCum(BigDecimal aTitoloKey) throws F3BException;

	// MEV 26 Cumulo Step2
	public String ExInserisciReatiCumulatiWithoutSequence(Vector<ReatoCumuloModel> VecReatiCumu,
			Connection lConn) throws F3BException;

}