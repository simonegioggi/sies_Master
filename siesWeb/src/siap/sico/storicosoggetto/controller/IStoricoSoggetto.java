package siap.sico.storicosoggetto.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: StoricoSoggettoController
 * </p>
 * <p>
 * Description: Classe Controller per StoricoSoggetto
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
public interface IStoricoSoggetto {

	public StoricoSoggettoModel ExInserisciStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto)
			throws F3BException;

	public Vector ExRicercaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto) throws F3BException;

	public StoricoSoggettoModel ExRicercaStoricoSoggettoByKey(BigDecimal aKey) throws F3BException;

	public StoricoSoggettoModel ExModificaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto)
			throws F3BException;

	public void ExCancellaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto) throws F3BException;

	public Vector ExRicercaStoricoSoggettoByIdSogVariato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaStoricoSoggettoByIdSogNuovo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaStoricoSoggettoSiusByIdSogVariato(BigDecimal aKey) throws F3BException;

}