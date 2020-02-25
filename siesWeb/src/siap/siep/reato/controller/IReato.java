package siap.siep.reato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.reato.model.ReatoModel;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ReatoController
 * </p>
 * <p>
 * Description: Classe Controller per Reato
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
public interface IReato {

	public ReatoModel ExInserisciReato(ReatoModel aReato) throws F3BException;

	public ReatoModel ExInserisciReati(ArrayList aReati) throws F3BException;

	public void ExInserisciUlterioriReati(ReatoModel aReatoPrincipale, ArrayList aReati) throws F3BException;

	public Vector ExRicercaReato(ReatoModel aReato) throws F3BException;

	public ReatoModel ExRicercaReatoByKey(BigDecimal aKey) throws F3BException;

	public ReatoModel ExRicercaNormaPrincipaleByReatoFascicoloSiep(BigDecimal aProgrReato,
			BigDecimal aKeyFasc) throws F3BException;

	public Vector ExRicercaReatoCircostanzaByFascicolo(BigDecimal aKey) throws F3BException;

	public ReatoModel ExModificaReato(ReatoModel aReato) throws F3BException;

	public ReatoModel ExModificaPenaReato(ReatoModel aReato, Vector aNormeUlteriori) throws F3BException;

	public void ExCancellaReato(ReatoModel aReato) throws F3BException;

	public Vector ExRicercaReatiByFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaReatiByFascicoloNoError(BigDecimal aKey) throws F3BException;

	public String ExInserisciReatiWithoutSequence(ArrayList aReati, Connection lConn) throws F3BException;

	public Vector ExRicercaReatiNoCircostanzaByFascicolo(BigDecimal aKey) throws F3BException;

	public void ExModificaUlterioriNorme(ReatoModel aNormaUno, Vector aNormeUlteriori) throws F3BException;

	public ReatoModel ExInserisciReatiSige(ArrayList aReati, BigDecimal aIdFasSigeSen) throws F3BException;

	public Vector ExRicercaReatoSige(ReatoSentenzaSigeModel aReato) throws F3BException;

	public Vector ExRicercaReatoCircostanzaBySentenzaSige(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaNormeSige(ReatoSentenzaSigeModel aReato) throws F3BException;

	public void ExCancellaReatoSige(ReatoSentenzaSigeModel aReato) throws F3BException;

	public void ExInserisciUlterioriReatiSige(ReatoModel aReatoPrincipale, ArrayList aReati,
			BigDecimal aIdFasSigeSen) throws F3BException;

	public void ExModificaKeyNSCByKey(ReatoModel aReato) throws F3BException;

	public Vector ExRicercaReatoCircostanzaByFascicoloOnlyNsc(long aFascicoloSIEP) throws F3BException;

	// AMBROSINO 04/2011

	public String ExInserisciReatiCopiati(String[] aReati, BigDecimal aIdFasdaCopia, ReatoModel aReato)
			throws F3BException;

}