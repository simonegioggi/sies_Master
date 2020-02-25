package siap.siep.sollecitoesitotrasmissione.controller;

/**
* <p>Title: SollecitoEsitoTrasmissioneController</p>
* <p>Description: Classe Controller per SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISollecitoEsitoTrasmissione {

	public SollecitoEsitoTrasmissioneModel ExInserisciSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException;

	public Vector ExRicercaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException;

	public void ExModificaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException;

	public void ExCancellaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException;

	public BigDecimal ExGetCountSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException;

	public SollecitoEsitoTrasmissioneModel ExRicercaSollecitoEsitoTrasmissioneById(BigDecimal aIdSollecito)
			throws F3BException;

	public Vector ExRicercaSollecitoEsitoTrasmissionePaged(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione, int aPage) throws F3BException;

	public SollecitoEsitoTrasmissioneModel ExRicercaSollecitoEsitoTrasmissioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

}