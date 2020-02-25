package siap.sius.prescrizione.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.prescrizione.model.PrescrizioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PrescrizioneController
 * </p>
 * <p>
 * Description: Classe Controller per Prescrizione
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
public interface IPrescrizione {

	public PrescrizioneModel ExInserisciPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException;

	public PrescrizioneModel ExInserisciPrescrizioni(PrescrizioneModel[] aPrescrizioni,
			BigDecimal aKeyDepOrdPC) throws F3BException;

	public Vector ExRicercaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException;

	public PrescrizioneModel ExRicercaPrescrizioneByKey(BigDecimal aKey) throws F3BException;

	/*
	 * public Vector ExRicercaPrescrizioneByOrdinanza(BigDecimal aKey) throws F3BException;
	 */
	public Vector ExRicercaPrescrizioneByEvento(BigDecimal aKey) throws F3BException;

	public PrescrizioneModel ExModificaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException;

	public void ExCancellaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException;

}