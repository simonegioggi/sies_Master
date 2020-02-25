package siap.siep.altrigradigiudizio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AltriGradiGiudizioController
 * </p>
 * <p>
 * Description: Classe Controller per AltriGradiGiudizio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IAltriGradiGiudizio {

	public AltriGradiGiudizioModel ExInserisciAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException;

	public Vector ExRicercaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException;

	public AltriGradiGiudizioModel ExRicercaAltriGradiGiudizioByKey(BigDecimal aKey) throws F3BException;

	public AltriGradiGiudizioModel ExModificaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException;

	public void ExCancellaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio) throws F3BException;

	public AltriGradiGiudizioModel ExInserisciAltriGradiGiudizioFascicoloSiep(
			AltriGradiGiudizioModel aAltriGradiGiudizio, AgdgFascicoloSiepModel aAltriGradiGiudizioFasSiep)
			throws F3BException;

	public void ExAggiornaAltriGradiGiudizioFascicolo(AgdgFascicoloSiepModel aAltriGradiGiudizioFasSiep,
			int modo) throws F3BException;

	public String ExInserisciAltriGradiGiudizioWithoutSequence(ArrayList aAltriGradiGiudizio,
			Connection lConn) throws F3BException;

}