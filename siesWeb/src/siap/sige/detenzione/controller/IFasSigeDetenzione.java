package siap.sige.detenzione.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FasSigeDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per FasSigeDetenzione
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
public interface IFasSigeDetenzione {

	public FasSigeDetenzioneModel ExInserisciFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione)
			throws F3BException;

	public FasSigeDetenzioneModel ExRicercaUltimaDetenzioneFascicolo(BigDecimal aIdFasSige)
			throws F3BException;

	public Vector exRicercaLuoghiDetenzioneFascicoloSige(BigDecimal aIdFasSige) throws F3BException;

	public FasSigeDetenzioneModel ExRicercaFasSigeDetenzione(BigDecimal aIdFasSigeDet) throws F3BException;

	public FasSigeDetenzioneModel ExModificaFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione)
			throws F3BException;

	public void ExCancellaFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione) throws F3BException;

	public void exCancellaUltimaDetenzioneFascicoloSige(BigDecimal aIdFasSige) throws F3BException;

	/*
	 * public Vector ExRicercaFasSigeDetenzione (FasSigeDetenzioneModel aFasSigeDetenzione ) throws
	 * F3BException; public FasSigeDetenzioneModel ExRicercaFasSigeDetenzioneByKey (BigDecimal aKey) throws
	 * F3BException;
	 */

}