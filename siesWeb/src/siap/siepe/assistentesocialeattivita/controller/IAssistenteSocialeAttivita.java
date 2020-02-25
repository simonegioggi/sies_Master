package siap.siepe.assistentesocialeattivita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AssistenteSocialeAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per AssistenteSocialeAttivita
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
public interface IAssistenteSocialeAttivita {

	public AssistenteSocialeAttivitaModel ExInserisciAssistenteSocialeAttivita(
			AssistenteSocialeAttivitaModel aAssistenteSocialeAttivita, Connection aConn) throws F3BException;

	public Vector ExRicercaAssistentiSocialiXAttivita(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAssistentiSocialiAttiiviXAttivita(BigDecimal aKey) throws F3BException;

	/*
	 * public Vector ExRicercaAssistenteSocialeAttivita (AssistenteSocialeAttivitaModel
	 * aAssistenteSocialeAttivita ) throws F3BException; public AssistenteSocialeAttivitaModel
	 * ExRicercaAssistenteSocialeAttivitaByKey (BigDecimal aKey) throws F3BException;
	 */

	public AssistenteSocialeAttivitaModel ExModificaAssistenteSocialeAttivita(
			AssistenteSocialeAttivitaModel aAssistenteSocialeAttivita) throws F3BException;

}