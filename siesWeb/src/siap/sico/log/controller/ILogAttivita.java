package siap.sico.log.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.log.model.LogAttivitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: LogAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per LogAttivita
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
public interface ILogAttivita {

	public LogAttivitaModel ExInserisciLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException;

	public Vector ExRicercaLogAttivita(LogAttivitaModel aLogAttivita, Date aDataInizio, Date aDataFine,
			String FiltroUtenteConnesso) throws F3BException;

	public LogAttivitaModel ExRicercaLogAttivitaByKey(BigDecimal aKey) throws F3BException;

	public LogAttivitaModel ExModificaLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException;

	public void ExCancellaLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException;

}