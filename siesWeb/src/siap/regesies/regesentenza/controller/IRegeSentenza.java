package siap.regesies.regesentenza.controller;

import java.util.Vector;

import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeSentenzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeSentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings("rawtypes")
public interface IRegeSentenza {

	// public RegeSentenzaModel ExInserisciRegeSentenza (RegeSentenzaModel aRegeSentenza )
	// throws F3BException;
	public ProvvedimentoModel ExRicercaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException;

	public RegeSentenzaModel ExRicercaRegeSentenzaByKey(String aKey) throws F3BException;

	public RegeSentenzaModel ExModificaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException;

	public void ExCancellaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException;

	public Vector ExRicercaElencoProvvedimenti(int aPage, String aCodComune) throws F3BException;

	public int ExGetCountProvvedimenti(String aCodComune) throws F3BException;

	public ProvvedimentoModel ExDettaglioProvvedimento(String aKey) throws F3BException;

	public ProvvedimentoModel ExRicercaRegeSentenzaPerEstremi(RegeSentenzaModel aRegeSentenza)
			throws F3BException;

	public SentenzaModel ExRicercaSentenzaDuplicata(RegeSentenzaModel aRegeSentenza) throws F3BException;

	public Vector ExRicercaRegeSentenzaDaElenco(RegeSentenzaModel aRegeSentenza) throws F3BException;

}