package siap.sige.scadenzario.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import siap.sige.scadenzario.model.ScadenzarioSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ScadenzarioSigeController
 * </p>
 * <p>
 * Description: Classe Controller per ScadenzarioSige
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
public interface IScadenzarioSige {

	public ScadenzarioSigeModel ExInserisciScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException;

	public Vector ExRicercaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige) throws F3BException;

	public ScadenzarioSigeModel ExRicercaScadenzarioSigeByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaScadenzarioSige(Date aData1, Date aData2) throws F3BException;

	public Vector ExRicercaScadenzarioSige(String aTipoScadenzario, Date aData1, Date aData2)
			throws F3BException;

	public Vector ExRicercaScadenzarioSige(String aTipoScadenzario, Date aData1, Date aData2,
			UtenteModel lUteMod) throws F3BException;

	public ScadenzarioSigeModel ExModificaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException;

	public void ExCancellaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige) throws F3BException;

	public ScadenzarioSigeModel ExRicercaScadenzarioSigeByIdFascicoloTipo(BigDecimal aIdFascicolo,
			String aTipo) throws F3BException;

	public boolean ExScadutoScadenzarioSigeByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws F3BException;

	public ScadenzarioSigeModel ExModificaScadenzario(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException;

	public void ExSetVistoScadenzario(ScadenzarioSigeModel aScadenzarioSige) throws F3BException;

	public Vector ExElencoTipiScadenzarioByTipoUfficio(String aTipoUfficio) throws F3BException;

}