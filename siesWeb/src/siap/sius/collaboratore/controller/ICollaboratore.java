package siap.sius.collaboratore.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICollaboratore
 * </p>
 * <p>
 * Description: interface implementata da CollaboratoreController.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */
@SuppressWarnings("rawtypes")
public interface ICollaboratore {

	public boolean ExIsCollaboratore(BigDecimal aIdFascicoloSius, String aCodUfficio) throws F3BException;

	public boolean ExIsPackage() throws F3BException;

	// public Vector ExGetCollaboratore (BigDecimal aIdFascicoloSius, String aCodUfficio ) throws
	// F3BException;
	// public CollaboratoreModel ExGetCollaboratoreById (BigDecimal aIdCollaboratore) throws F3BException;

	public void ExInserisciCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException;

	public void ExAggiornaCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException;

	public void ExCancellaCollaboratore(BigDecimal aIdCollaboratore) throws F3BException;

	public Vector ExRicercaCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException;

	public CollaboratoreModel ExRicercaCollaboratoreById(BigDecimal aId) throws F3BException;

}