package siap.sico.trasmissione.controller;

/**
* <p>Title: TrasmissioniController</p>
* <p>Description: Classe Controller per Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.trasmissione.model.TrasmissioniModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ITrasmissioni {

	public TrasmissioniModel ExInserisciTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException;

	public Vector ExRicercaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException;

	public void ExModificaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException;

	public void ExCancellaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException;

	public BigDecimal ExGetCountTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException;

	public TrasmissioniModel ExRicercaTrasmissioniById(BigDecimal aIdTrasmissione) throws F3BException;

	public Vector ExRicercaTrasmissioniPaged(TrasmissioniModel aTrasmissioni, int aPage) throws F3BException;

	public Vector ExRicercaTrasmissioniPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso)
			throws F3BException;

}