package siap.regesies.regesoggetto.controller;

import java.util.Vector;

import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeSoggettoController
 * </p>
 * <p>
 * Description: Classe Controller per RegeSoggetto
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
public interface IRegeSoggetto {

	// public RegeSoggettoModel ExInserisciRegeSoggetto (RegeSoggettoModel aRegeSoggetto )
	// throws F3BException;
	public Vector ExRicercaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException;

	public RegeSoggettoModel ExRicercaRegeSoggettoByKey(String aKey) throws F3BException;

	public Vector ExRicercaRegeSoggettoPerProvvedimento(RegeSentenzaModel aRegeSentenza) throws F3BException;

	public RegeSoggettoModel ExModificaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException;

	public void ExCancellaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException;

}