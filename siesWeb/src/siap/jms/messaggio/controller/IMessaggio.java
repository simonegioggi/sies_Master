package siap.jms.messaggio.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.jms.messaggio.model.MessaggioModel;

/**
 * <p>
 * Title: MessaggioController
 * </p>
 * <p>
 * Description: Classe Controller per Messaggio
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
public interface IMessaggio {

	public MessaggioModel ExInserisciMessaggio(MessaggioModel aMessaggio) throws F3BException;

	public MessaggioModel ExInserisciMessaggioEsitoTrasferimento(MessaggioModel aMessaggio)
			throws F3BException;

	public Vector ExRicercaMessaggio(MessaggioModel aMessaggio) throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerUfficio(String aCodUfficio, String aTipoOperazione)
			throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerUffici(String aCodUfficioDestinatario,
			String aCodUfficioMittente, String aTipoOperazione) throws F3BException;

	public Vector ExRicercaMessaggioEsitoPerUfficio(String aMessaggio) throws F3BException;

	public Vector ExRicercaMessaggioEsitoPerUfficio(String aMessaggio, String aTipoOperazione)
			throws F3BException;

	public MessaggioModel ExRicercaMessaggioByKey(BigDecimal aKey) throws F3BException;

	public MessaggioModel ExRicercaMessaggioByCorrelationId(String aKey) throws F3BException;

	public MessaggioModel ExModificaMessaggio(MessaggioModel aMessaggio) throws F3BException;

	public void ExCancellaMessaggio(BigDecimal aMessaggio) throws F3BException;

	public MessaggioModel ExModificaMessaggioVisto(MessaggioModel aMessaggio) throws F3BException;

	public MessaggioModel ExModificaMessaggioJMSId(MessaggioModel aMessaggio) throws F3BException;

	public Vector ExRicercaMessaggioEsitoRicercaPerUfficio(String aCodUfficio, String aTipoOperazione,
			int aPage) throws F3BException;

	public Vector ExRicercaMessaggiPerUffici(String aCodUfficioDestinatario, String aCodUfficioMittente)
			throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerDateUffici(String aCodUfficioDestinatario,
			String aCodUfficioMittente, Date aDataTrasmissioneAtti, Date aDataRicezioneAtti,
			String aIncludeInCarico) throws F3BException;

	public MessaggioModel ExRicercaMessaggioByKeyMultipleBDI(BigDecimal aKey) throws F3BException;

	public void ExRicercaCancellaMessaggioEsitoByCorrelationId(String aCorrelationID, String aBDIMittente)
			throws F3BException;

	// public Vector ExRicercaMessaggioEsitoRicercaSoggettoPerUfficio(String aUfficioMittente, String
	// aCodiceUtente, String aTipoOperazione)
	// throws F3BException,Exception;

	public Vector ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(String aUfficioRicevente,
			String aUfficioMittente, String aTipoOperazione, BigDecimal aAnnoFascicolo,
			BigDecimal aProgrFascicolo, String aIncludeInCarico) throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(MessaggioModel aMessaggio,
			String aIncludeInCarico) throws F3BException;

	/*
	 * 20070615 public Vector ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(String aUfficioRicevente,
	 * String aUfficioMittente, String aTipoOperazione, BigDecimal aAnnoFascicolo, BigDecimal aProgrFascicolo,
	 * String aIncludeInCarico, String aFlagVisto ) throws F3BException;
	 */

	public Vector ExRicercaMessaggiPerUfficioTipoOper(String aUfficioMittente, String aTipoOperazione)
			throws F3BException;

	// public Vector ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged(String aUfficioMittente,String
	// aCodiceUtente, String aTipoOperazione,int aPage)
	// throws F3BException,Exception;

	public Vector ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged(String aUfficioMittente,
			String aCodiceUtente, String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine,
			int aPage) throws F3BException, Exception;

	public BigDecimal ExGetCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged(String aUfficioMittente,
			String aCodiceUtente, String aTipoOperazione) throws F3BException;

	public Vector ExRicercaMessaggioEsitoConFiltri(String aUfficio, String aUtente, String aEsito,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException;

	public Vector ExRicercaMessaggioEsitoRicercaFascConFiltri(String aAnnoSiep, String aProgrSiep,
			String aUfficio, String aUfficioDestinatario, String aUtente, String aEsito,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine, int aPage)
			throws F3BException;

	public Vector ExRicercaMessaggiRichiestaPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException;

	public Vector ExRicercaMessaggioEsitoConFiltri(String aCodTipoOper, String aUfficio, String aUtente,
			String aEsito, String aCodUffDest, String aAnnoSiep, String aProgrSiep, Date dataRicercaInizio,
			Date dataRicercaFine) throws F3BException;

	public boolean ExRicercaMessaggioUgualeNonSpedito(MessaggioModel aMessage) throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerTipoeDate(String aCodUfficioDestinatario,
			String aTipoOperazione, Date aDataTrasmissioneAtti, Date aDataRicezioneAtti, String aIncludeVisto)
			throws F3BException;

	public Vector ExRicercaMessaggioRichiestaPerSoggetto(String aUfficioRicevente, String aCognomeSoggetto,
			String aNomeSoggetto, String aCodComuneNascita, String aCodStatoNascita, Date aDataNascita,
			String aTipoOperazione, String aIncludeVisto) throws F3BException;

	public Vector ExRicercaContatoriXCruscotto(String acodUfficioDestinatario, String aOrd)
			throws F3BException;

	public Vector<MessaggioModel> ExRicercaMessaggiCorrelati(String aIdMessaggio) throws F3BException;

	// String adeliveryMod ?? 00001 = Inviato, 00002 = Ricevuto
	public MessaggioModel ExRicercaMessaggioByIdRichiesta(String aTipoMes // 01 = RICHIESTA
			, String aTipoOperazione // 00066 = TRASFERIMENTO_COMPETENZA
			, String aCodUffMittenete, BigDecimal aIdRichiesta) throws F3BException;

	public Vector<MessaggioModel> ExRicercaSollecitiMessaggioRichiestaAtti(String aTipoMes,
			String aTipoOperazione, String aIdMesSollecitato) throws F3BException;

	// Seguito Atti
	public MessaggioModel ExRicercaMessaggioByCorrelationIdOnly(String aKey) throws F3BException;

	public Vector ExRicercaMessaggiRicevutiPaged(MessaggioModel aMessaggio,
			Vector<String> aListaTipoOperazione, Date aDataInizio, Date aDataFine, int aPage)
			throws F3BException;

	public BigDecimal ExCountMessaggiRicevutiPaged(MessaggioModel aMessaggio,
			Vector<String> aListaTipoOperazione, Date aDataInizio, Date aDataFine, int aPage)
			throws F3BException;

	public Vector ExRicercaMessaggiNelPeriodoPaged(MessaggioModel aMessaggio, Date aDataInizio,
			Date aDataFine, int aPage) throws F3BException;

	public BigDecimal ExCountMessaggiNelPeriodoPaged(MessaggioModel aMessaggio, Date aDataInizio,
			Date aDataFine, int aPage) throws F3BException;

	public Vector<MessaggioModel> ExRicercaMessaggioByIdMessaggioSollecitato(String aTipoOperazione,
			String aIdMesSollecitato) throws F3BException;

}