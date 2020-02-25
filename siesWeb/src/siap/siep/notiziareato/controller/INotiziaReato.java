package siap.siep.notiziareato.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.notiziareato.model.NotiziaReatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NotiziaReatoController
 * </p>
 * <p>
 * Description: Classe Controller per Notizia di Reato
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
public interface INotiziaReato {

	public NotiziaReatoModel ExInserisciNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException;

	public Vector ExRicercaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException;

	public Vector ExRicercaNotiziaReatoByIdFascicoloSiep(BigDecimal aKey) throws F3BException;

	public NotiziaReatoModel ExRicercaNotiziaReatoByKey(BigDecimal aKey) throws F3BException;

	public NotiziaReatoModel ExModificaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException;

	public void ExCancellaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException;

	public Vector ExRicercaNotiziaReatoByIdFascicoloSige(BigDecimal aKey) throws F3BException;

}