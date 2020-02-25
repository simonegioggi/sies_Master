package siap.siep.storicoavvocato.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: StoricoAvvocatoController
 * </p>
 * <p>
 * Description: Classe Controller per StoricoAvvocato
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
public interface IStoricoAvvocato {

	public StoricoAvvocatoModel ExInserisciStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato)
			throws F3BException;

	public Vector ExRicercaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato) throws F3BException;

	public StoricoAvvocatoModel ExRicercaStoricoAvvocatoByKey(BigDecimal aKey) throws F3BException;

	public StoricoAvvocatoModel ExModificaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato)
			throws F3BException;

	public void ExCancellaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato) throws F3BException;

	public Vector ExRicercaStoricoAvvocatoByIdAvvocato(BigDecimal aKey) throws F3BException;

}