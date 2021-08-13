package siap.siep.nuovaistanza.action;


/**
* <p>Title: ActInserisciNuovaIstanza</p>
* <p>Description: Classe Action per l'inserimento di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile S.r.l.</p>
* @version 5.0
*/


import java.math.BigDecimal;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActInserisciNuovaIstanza extends ActionNuovaIstanza implements ICostantiNuovaIstanza {



  public String processRequest() throws Exception 
  {
	BigDecimal lIdSentenza = null;
	BigDecimal lIdFascicolo = null;
   
	if(!this.isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA))	 {
	  lIdSentenza = this.getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA);  
	}    
	else if(!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))	 {
	  lIdFascicolo = this.getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);  
	}

	
	//preparo il model dell'EVENTO
	EventoModel lEveMod = getEvento(lIdFascicolo);
	
	//preparo il model dell'ISTANZA
	NuovaIstanzaModel lNuoMod = getNuovaIstanza(lIdFascicolo);
	
	//20210812 MEV_21 Valorizzazione AVV_ID_AVVOCATO Inserito/modificato/confermato
	lNuoMod.setAvvIdAvvocato(getIdAvvocatoInserito());

	//20210812 MEV_21 Valorizzazione AVV_ID_AVVOCATO_PRESENTANTE Inserito/modificato/confermato
	lNuoMod.setAvvIdAvvocatoPresentante(getIdAvvocatoPresInserito());
	
	//preparo il model della sentenza
	SentenzaModel lSenMod = null;
	
	if(!this.isRequestParameterNullObj("tipoinserimento") &&
		 (this.getRequestStringParameter("tipoinserimento").equals("nuovo") ||
				 this.getRequestStringParameter("tipoinserimento").equals("soggetto")	) )
	{
		String lCodTipoSentenza = this.getRequestStringParameter("TipoProvv");
		
		if("01".equals(lCodTipoSentenza))//sentenza
		{
			lSenMod = getSentenza();
		}
		else if("02".equals(lCodTipoSentenza))//decreto
		{
			lSenMod = getDecreto();
		}
		else if("05".equals(lCodTipoSentenza))//sentenza straniera
		{
			lSenMod = getSentenzaStraniera();
		}		
	}
		
	//preparo il model del soggetto
	SoggettoModel lSogMod = null;
	if(!this.isRequestParameterNullObj(CAMPO_COGNOME))
	{
		// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni.
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}

		lSogMod = getSoggetto();
	}
	
	//preparo il model del fascicolo
	FascicoloSiepModel lFascMod = null;
	if(lIdFascicolo == null)
	{
	  lFascMod = getFascicolo(lIdSentenza); 
	}
	
    
    //inserisci i model richiesti
    INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
    NuovaIstanzaModel lNuoRetMod = new NuovaIstanzaModel();
    lNuoRetMod=lCtrl.ExInserisciNuovaIstanza(lEveMod,lNuoMod,lSenMod,lSogMod,lFascMod);
    
   /** Lancia dettaglio nuova istanza **/
    String lPage="";
    
    /*ANNA
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
    lPage += "&" + "IdEvento" + "=" + lNuoRetMod.getEveIdEvento().toString();
    */
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
    lPage += "&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lNuoRetMod.getFasSieIdFascicoloSiep().toString();
     

    return lPage;
  }
}