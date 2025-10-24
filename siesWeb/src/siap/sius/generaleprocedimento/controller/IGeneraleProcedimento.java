package siap.sius.generaleprocedimento.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;

/**
 * GeneraleProcedimentoController - Classe Controller per GeneraleProcedimento
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IGeneraleProcedimento {

	// STUB : attivare le funzioni quando vengono usate!!!!
	/*
	 * public GeneraleProcedimentoModel ExInserisciGeneraleProcedimento (GeneraleProcedimentoModel
	 * aGeneraleProcedimento ) throws F3BException;
	 */
	public Vector ExRicercaGeneraleProcedimento(GeneraleProcedimentoModel aGeneraleProcedimento)
			throws F3BException;

	/*
	 * public GeneraleProcedimentoModel ExRicercaGeneraleProcedimentoByKey (BigDecimal aKey) throws
	 * F3BException; public GeneraleProcedimentoModel ExModificaGeneraleProcedimento
	 * (GeneraleProcedimentoModel aGeneraleProcedimento ) throws F3BException; public void
	 * ExCancellaGeneraleProcedimento (GeneraleProcedimentoModel aGeneraleProcedimento ) throws F3BException;
	 */
	public GeneraleProcedimentoModel ExRicercaGeneraleProcedimentoByFascicolo(BigDecimal aIdFasSius)
			throws F3BException;

	public GeneraleProcedimentoModel ExModificaNoteProcedimento(
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException;

	// MEV_2019-09: aggiunta funzione di aggiornamento
	public void ExModificaDatiRestituzioneGeneraleProcedimento(GeneraleProcedimentoModel gpm)
			throws F3BException;

}