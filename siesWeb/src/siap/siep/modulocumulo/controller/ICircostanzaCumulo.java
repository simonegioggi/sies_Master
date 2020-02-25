package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;

/**
 * <p>
 * Title: CircostanzaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Circostanza Cumulo
 * </p>
 * 
 * @version 1.0
 */
public interface ICircostanzaCumulo {

	// Inserisce solo UN Record CIRCOSTANZA_CUMULO da CircostanzaCumuloModel
	public CircostanzaCumuloModel ExInserisciCircostanzaCumulo(CircostanzaCumuloModel aCircostanza)
			throws F3BException;

	public void ExInserisciCircostanzeCumulo(Vector<CircostanzaCumuloModel> aCircostanze,
			boolean aggiornamento, String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal aIdTitoCum) throws F3BException;

	public CircostanzaCumuloModel ExModificaCircostanzaCumulo(CircostanzaCumuloModel aCircostanza,
			boolean flagAgg, String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal idTitoloCumulato) throws F3BException;

	public void ExCancellaCircostanzaCumulo(CircostanzaCumuloModel aCircostanza) throws F3BException;

	public CircostanzaCumuloModel ExRicercaCircostanzaCumuloByKey(BigDecimal aCircoCumKey)
			throws F3BException;

	public Vector<CircostanzaCumuloModel> ExRicercaCircostanzaCumulobyTitolo(BigDecimal aKeyTitolo)
			throws F3BException;

	// MEV 26 Cumulo Step2
	public String ExInserisciCircostanzeCumulateWithoutSequence(Vector<CircostanzaCumuloModel> VecCircoCumu,
			Connection lConn) throws F3BException;

}