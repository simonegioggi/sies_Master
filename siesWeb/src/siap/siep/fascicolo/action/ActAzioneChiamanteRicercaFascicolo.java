package siap.siep.fascicolo.action;

/**
 * <p>Title: ActAzioneChiamanteRicercaFascicolo</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import org.apache.log4j.Logger;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.sentenza.action.ICostantiSentenza;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
//import siap.siep.fascicolo.controller.FascicoloSiepController;

public class ActAzioneChiamanteRicercaFascicolo extends ActionSiap implements ICostantiFascicoloSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {

    IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
    UfficioModel lUfficio = lCtrlUfficio.getUfficioByCodTipoUffDescrComune(this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).toUpperCase());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ufficio----->"+lUfficio.getCodUfficio());
    if( ! isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO) )
    this.setRequestAttribute("ICostantiSoggetto.CAMPO_ID_SOGGETTO" , getRequestBigDecimalParameter( ICostantiSoggetto.CAMPO_ID_SOGGETTO ));

  if( ! isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA) )
    this.setRequestAttribute("ICostantiSentenza.CAMPO_ID_SENTENZA", getRequestBigDecimalParameter( ICostantiSentenza.CAMPO_ID_SENTENZA ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_ANNO) )
    this.setRequestAttribute("ICAMPO_CHIAVE_ANNO", getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_PROGR) )
    this.setRequestAttribute("CAMPO_CHIAVE_PROGR", getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE)  )
    this.setRequestAttribute("CAMPO_CHIAVE_ANNO_INIZIALE", getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO_INIZIALE ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE) )
    this.setRequestAttribute("CAMPO_CHIAVE_PROGR_INIZIALE", getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR_INIZIALE ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE) )
    this.setRequestAttribute("CAMPO_CHIAVE_ANNO_FINALE", getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO_FINALE ));

  if( ! isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE) )
    this.setRequestAttribute("CAMPO_CHIAVE_PROGR_FINALE", getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR_FINALE ));

  // 26/06/2009 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
  if( ! isRequestParameterNullObj(CAMPO_FLAG_VALIDATO) )
    this.setRequestAttribute("CAMPO_FLAG_VALIDATO", getRequestStringParameter( CAMPO_FLAG_VALIDATO ));
  
/**************************modifica 24 marzo 04************************/

  if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE) && !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE) && !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE))
  {
    this.setRequestAttribute("CAMPO_GIORNO_ISCRIZIONE", getRequestStringParameter( CAMPO_GIORNO_ISCRIZIONE ));
    this.setRequestAttribute("CAMPO_MESE_ISCRIZIONE", getRequestStringParameter( CAMPO_MESE_ISCRIZIONE ));
    this.setRequestAttribute("CAMPO_ANNO_ISCRIZIONE", getRequestStringParameter( CAMPO_ANNO_ISCRIZIONE ));
  }
  if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE) && !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE) &&
      !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE) )
  {
    this.setRequestAttribute("CAMPO_GIORNO_ISCRIZIONE_INIZIALE", getRequestStringParameter( CAMPO_GIORNO_ISCRIZIONE_INIZIALE ));
    this.setRequestAttribute("CAMPO_MESE_ISCRIZIONE_INIZIALE", getRequestStringParameter( CAMPO_MESE_ISCRIZIONE_INIZIALE ));
    this.setRequestAttribute("CAMPO_ANNO_ISCRIZIONE_INIZIALE", getRequestStringParameter( CAMPO_ANNO_ISCRIZIONE_INIZIALE ));

  }
  if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE) && !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE) &&
      !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE) )
  {
    this.setRequestAttribute("CAMPO_GIORNO_ISCRIZIONE_FINALE", getRequestStringParameter( CAMPO_GIORNO_ISCRIZIONE_FINALE ));
    this.setRequestAttribute("CAMPO_MESE_ISCRIZIONE_FINALE", getRequestStringParameter( CAMPO_MESE_ISCRIZIONE_FINALE ));
    this.setRequestAttribute("CAMPO_ANNO_ISCRIZIONE_FINALE", getRequestStringParameter( CAMPO_ANNO_ISCRIZIONE_FINALE ));
  }


/***********************************************************/
 //lFasMod.setChiaveUfficio(lUfficio.getCodUfficio());
 this.setRequestAttribute("ICostantiSiepJMS.CAMPO_TIPO_UFFICIO", getRequestStringParameter( ICostantiSiepJMS.CAMPO_TIPO_UFFICIO));
 this.setRequestAttribute("ICostantiSiepJMS.CAMPO_SEDE_UFFICIO", getRequestStringParameter( ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));

 String lReturnPage = "";

    if(!lUfficio.getCodDistretto().equals(this.getCodDistrettoUtenteConnesso()))
    {

      lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.jms.action.ActRicercaEstesaFascicoloPerTrasferimento";


    }else
    {
      lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActRicercaFascicolo";


    }

    return 	lReturnPage; //restituisce la jsp di VIEW
  }
}