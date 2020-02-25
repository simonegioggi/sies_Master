package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Date;

//import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
//import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria;
import siap.sius.penapecuniaria.model.RichiesteConversioniPerOrdinanzaModel;
//import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
//import siap.sius.sanzionesostitutiva.util.InserisciPeriodoAltraSanzioneModificaESS;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

 /**
 * <p>Title: ActInserisciOrdinanzaConversioneRateizzazionePP</p>
 * <p>Description: Classe Action per l'inserimento dell'Emissione di un Ordinanaza, inserimento di SCAMBIO_SANZIONE e modifica delle RICHIESTA_CONVERSIONE afferenti</p>
 * @version 1.0
 */

public class ActInserisciOrdinanzaConversioneRateizzazionePP extends ActInserisciOrdinanzaUDS
implements ICostantiDepositoDecreto
{
	public String processRequest() throws Exception
	{
		return super.processRequest();
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
	throws F3BException
	{

		// Dati relativi alle Richieste Conversioni Pene Pecuniarie.
		RichiesteConversioniPerOrdinanzaModel aRicConModel = new RichiesteConversioniPerOrdinanzaModel();
		int lNumeroRichiesteCPP = 0;
  	String lTipoRichiestaCPP = "";
	  if(!isRequestParameterNullObj("numeroRichiesteCPP"))
	  {
	  	lNumeroRichiesteCPP = this.getRequestIntParameter("numeroRichiesteCPP");
    	// Lettura ID delle Richieste di Conversione.
  		String[] lIdRichiestaConversione = new String[lNumeroRichiesteCPP];
  		String[] lCodTipoRichiesta = new String[lNumeroRichiesteCPP];
  		String[] lCodTipoSanzione = new String[lNumeroRichiesteCPP];
  		lIdRichiestaConversione = this.getRequestStringParameters(ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE);
  		
    	// Si distingue tra i casi di Conversione e Rateizzazione.
    	if(!isRequestParameterNullObj(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE))
    		lTipoRichiestaCPP = this.getRequestStringParameter(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);

  		aRicConModel.setIdRichiestaConversione(lIdRichiestaConversione);

    	// Ordinanza di Conversione Pena Pecuniaria.	
    	if (lTipoRichiestaCPP.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE)==0 ) 
    	{	 
	  		String[] lNumGiorniDurataEsito = new String[lNumeroRichiesteCPP];
	  		String[] lNumMesiDurataEsito = new String[lNumeroRichiesteCPP];
	  		String[] lNumAnniDurataEsito = new String[lNumeroRichiesteCPP];
	  		
	  		lNumGiorniDurataEsito = this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS);
	  		lNumMesiDurataEsito	 = this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS);
	  		lNumAnniDurataEsito	 = this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS);
	  		for(int k=0; k < lNumeroRichiesteCPP; k++)
	  		{
	  			lCodTipoSanzione[k]		= this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE+k);
	  			lCodTipoRichiesta[k]	= lTipoRichiestaCPP;
	  		}
		  		
	  		aRicConModel.setNumGiorniDurataEsito(lNumGiorniDurataEsito);
	  		aRicConModel.setNumMesiDurataEsito(lNumMesiDurataEsito);
	  		aRicConModel.setNumAnniDurataEsito(lNumAnniDurataEsito);
	  		aRicConModel.setCodTipoSanzione(lCodTipoSanzione);
	  		aRicConModel.setCodTipoRichiesta(lCodTipoRichiesta);
			}
      // Ordinanza di Rateizzazione Pena Pecuniaria.	
    	if (lTipoRichiestaCPP.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE)==0 ) 
    	{	 
	  		String[] lNumeroRate 				= new String[lNumeroRichiesteCPP];
	  		String[] lValoreIntRata			= new String[lNumeroRichiesteCPP];
	  		String[] lValoreDecRata			= new String[lNumeroRichiesteCPP];
	  		BigDecimal[] lValoreRata		= new BigDecimal[lNumeroRichiesteCPP];
	  		String[] lValoreIntUltRata 	= new String[lNumeroRichiesteCPP];
	  		String[] lValoreDecUltRata 	= new String[lNumeroRichiesteCPP];
	  		BigDecimal[] lValoreUltRata	= new BigDecimal[lNumeroRichiesteCPP];
	  		Date lDataInizioPagamento 	= null;
        BigDecimal lNumGiorniInizioPagamento = null;

	  		lNumeroRate	 				= this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_NUMERO_RATE);
	  		lValoreIntRata	 		= this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA);
	  		lValoreDecRata	 		= this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA);
	  		lValoreIntUltRata	 	= this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTOFINALE_MULTA);
	  		lValoreDecUltRata	 	= this.getRequestStringParameters( ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTOFINALE_MULTA);

	  		lDataInizioPagamento = this.getRequestDateParameter ( ICostantiSiusPenaPecuniaria.CAMPO_ANNO_DATA_TERMINE_PAG, ICostantiSiusPenaPecuniaria.CAMPO_MESE_DATA_TERMINE_PAG, ICostantiSiusPenaPecuniaria.CAMPO_GIORNO_DATA_TERMINE_PAG);
	  		lNumGiorniInizioPagamento = this.getRequestBigDecimalParameter(ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_PER_PAGAMENTO);
	  		
	  		for(int k=0; k < lNumeroRichiesteCPP; k++)
	  		{
	  			if (lValoreIntRata[k] != null && lValoreIntRata[k].trim().length() > 0 )
	  			{
			  		lValoreRata[k] = new BigDecimal(lValoreIntRata[k]);
			  		if (lValoreDecRata[k] != null && lValoreDecRata[k].trim().length() > 0 )
			  			lValoreRata[k] = lValoreRata[k].add( new BigDecimal("."+lValoreDecRata[k])); 
	  			}else if (lValoreDecRata[k] != null && lValoreDecRata[k].trim().length() > 0 )
		  			lValoreRata[k] = new BigDecimal("."+lValoreDecRata[k]);
	  				
					if (lValoreIntUltRata[k] != null && lValoreIntUltRata[k].trim().length() > 0 )
					{
		  			lValoreUltRata[k] = new BigDecimal(lValoreIntUltRata[k]);
		  			if (lValoreDecUltRata[k] != null && lValoreDecUltRata[k].trim().length() > 0 ) 
		  				lValoreUltRata[k] = lValoreUltRata[k].add( new BigDecimal("."+lValoreDecUltRata[k]));
					} else if (lValoreDecUltRata[k] != null && lValoreDecUltRata[k].trim().length() > 0 )
  					lValoreUltRata[k] = new BigDecimal("."+lValoreDecUltRata[k]);

	  			lCodTipoRichiesta[k]	= lTipoRichiestaCPP;
	  			lCodTipoSanzione[k]		= this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE);
	  		}
	  		aRicConModel.setNumeroRate(lNumeroRate);
	  		aRicConModel.setValoreRata(lValoreRata);
	  		aRicConModel.setValoreUltimaRata(lValoreUltRata);
	  		aRicConModel.setCodTipoRichiesta(lCodTipoRichiesta);
	  		aRicConModel.setDataInizioPagamento(lDataInizioPagamento);
	  		aRicConModel.setNumGiorniInizioPagamento(lNumGiorniInizioPagamento);
	  		aRicConModel.setCodTipoSanzione(lCodTipoSanzione);
			}
	  }
		
		OrdinanzaEventoTenoriGProcModel lModRet = null; 

		// inserimento
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();		
		lModRet = IDepOrdCtrl.ExInserisciOrdinanzaConversioneRateizzazionePP(aOrdEveTenGP, aRicConModel);

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,"NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}
}