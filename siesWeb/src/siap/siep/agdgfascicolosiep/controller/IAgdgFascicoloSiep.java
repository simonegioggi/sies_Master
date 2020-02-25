package siap.siep.agdgfascicolosiep.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AgdgFascicoloSiepController
 * </p>
 * <p>
 * Description: Classe Controller per AgdgFascicoloSiep
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
public interface IAgdgFascicoloSiep {

	public AgdgFascicoloSiepModel ExInserisciAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep)
			throws F3BException;

	public Vector ExRicercaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep) throws F3BException;

	public AgdgFascicoloSiepModel ExRicercaAgdgFascicoloSiepByKey(BigDecimal aKey) throws F3BException;

	public AgdgFascicoloSiepModel ExModificaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep)
			throws F3BException;

	public void ExCancellaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep) throws F3BException;

	public String ExInserisciAGDGFasSiepWithoutSequence(ArrayList aAGDGFasSiep, Connection lConn)
			throws F3BException;

	public Vector ExRicercaAgdgFascicoloSiepByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

}