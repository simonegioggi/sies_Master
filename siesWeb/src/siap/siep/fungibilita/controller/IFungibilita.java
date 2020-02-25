package siap.siep.fungibilita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.fungibilita.model.FungibilitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FungibilitaController
 * </p>
 * <p>
 * Description: Classe Controller per Fungibilita
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
public interface IFungibilita {

	public FungibilitaModel ExInserisciFungibilita(FungibilitaModel aFungibilita) throws F3BException;

	/*
	 * public Vector ExRicercaFungibilita (FungibilitaModel aFungibilita ) throws F3BException;
	 */

	public FungibilitaModel ExRicercaFungibilitaByKey(BigDecimal aKey) throws F3BException;

	public FungibilitaModel ExRicercaFungibilitaByKeyEvento(BigDecimal aKey) throws F3BException;

	public FungibilitaModel ExModificaFungibilita(FungibilitaModel aFungibilita) throws F3BException;

	public void ExCancellaFungibilita(FungibilitaModel aFungibilita) throws F3BException;

	public void ExValidaFungibilita(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaFungibilitaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public String ExInserisciFungibilitaWithoutSequence(ArrayList aPenaResidua, ArrayList aEventiInseriti,
			Connection lConn) throws F3BException;

}