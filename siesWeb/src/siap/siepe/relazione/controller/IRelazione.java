package siap.siepe.relazione.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siepe.relazione.model.RelazioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RelazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Relazione
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
public interface IRelazione {

	public RelazioneModel ExInserisciRelazione(RelazioneModel aRelazione) throws F3BException;

	public Vector ExRicercaRelazioniByAttivita(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaRelazioniByRichiesta(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaRelazioniWithBlobByAttivita(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaRelazioniWithBlobByRichiesta(BigDecimal aKey) throws F3BException;

	public RelazioneModel ExRicercaRelazioneByKey(BigDecimal aKey) throws F3BException;

	/*
	 * public Vector ExRicercaRelazione (RelazioneModel aRelazione ) throws F3BException; public
	 * RelazioneModel ExRicercaRelazioneByKey (BigDecimal aKey) throws F3BException; public RelazioneModel
	 * ExModificaRelazione (RelazioneModel aRelazione ) throws F3BException;
	 */
	public Vector ExRicercaRelazione(RelazioneModel aRelazione) throws F3BException;

	public void ExCancellaRelazione(BigDecimal aKey) throws F3BException;

}