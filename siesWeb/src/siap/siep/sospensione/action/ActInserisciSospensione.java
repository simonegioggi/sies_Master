package siap.siep.sospensione.action;


/**
* <p>Title: ActInserisciSospensione</p>
* <p>Description: Classe Action per l'inserimento di Sospensione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciSospensione extends ActionSiap
                                     implements ICostantiSospensione,
                                                ICostantiDecretoOrdinanzaSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Azione di Inserimento del Sospensione
  *
  *
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    //==========================================================================
    // Carico il Model del Decreto di Ordinanza Siep con i dati recuperati dalla
    // form
    //==========================================================================
    DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

    //l'id dell'eventuale record presente ???
    lDecMod.setIdDecretoOrdinanzaSiep ( getRequestBigDecimalParameter( CAMPO_ID_DECRETO_ORDINANZA_SIEP) );

    lDecMod.setDataRicezioneProvvedimento ( getRequestDateParameter( CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO,CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO,CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO) );
    lDecMod.setDataEmissioneProvvedimento ( getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO,CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO,CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO) );
    lDecMod.setCodTipoRegistroOrdinanza   ( getRequestStringParameter( CAMPO_COD_TIPO_REGISTRO_ORDINANZA) );
    lDecMod.setAnnoRegistro               ( getRequestBigDecimalParameter( CAMPO_ANNO_REGISTRO) );
    lDecMod.setNumRegistro                ( getRequestBigDecimalParameter( CAMPO_NUM_REGISTRO) );
    lDecMod.setAnnoProvvedimento          ( getRequestBigDecimalParameter( CAMPO_ANNO_PROVVEDIMENTO) );
    lDecMod.setNumProvvedimento           ( getRequestBigDecimalParameter( CAMPO_NUM_PROVVEDIMENTO) );
    lDecMod.setCodTipoProvvedimento       ( getRequestStringParameter( CAMPO_COD_TIPO_PROVVEDIMENTO) );
    lDecMod.setCodTipoAutoritaEmittente   ( getRequestStringParameter( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
    lDecMod.setCodOggettoDecisione        ( getRequestStringParameter( CAMPO_COD_OGGETTO_DECISIONE) );
    lDecMod.setMotivazioni                ( getRequestStringParameter( CAMPO_MOTIVAZIONI) );
    lDecMod.setDataSospensioneEsecuzione  ( getRequestDateParameter( CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE) );
    lDecMod.setCodOggettoProcedimento     ( getRequestStringParameter( CAMPO_COD_OGGETTO_PROCEDIMENTO) );
    lDecMod.setCodContenutoDecreto        ( "-" );
    lDecMod.setFlagElaborato              ( "N" );

    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
    lDecMod.setCodLuogoEmittente( lComMod.getCodComune() );

    if( !isRequestParameterNullObj(CAMPO_FLAG_SCARCERARE_SCARCERATO) )
      lDecMod.setFlagScarcerareScarcerato( getRequestStringParameter( CAMPO_FLAG_SCARCERARE_SCARCERATO) );

    //Controllo sull'esistenza dell'ufficio per quel comune
    String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE) );


//    lDecMod.setDataDifferimento( getRequestDateParameter( CAMPO_ANNO_DATA_DIFFERIMENTO,CAMPO_MESE_DATA_DIFFERIMENTO,CAMPO_GIORNO_DATA_DIFFERIMENTO) );
//    lDecMod.setDataRinvio( getRequestDateParameter( CAMPO_ANNO_DATA_RINVIO,CAMPO_MESE_DATA_RINVIO,CAMPO_GIORNO_DATA_RINVIO) );
//    lDecMod.setDataFineInterruzione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_INTERRUZIONE,CAMPO_MESE_DATA_FINE_INTERRUZIONE,CAMPO_GIORNO_DATA_FINE_INTERRUZIONE) );
//    lDecMod.setDataDepositoIstanza( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,CAMPO_MESE_DATA_DEPOSITO_ISTANZA,CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA) );
//    lDecMod.setDataInterruzionePena( getRequestDateParameter( CAMPO_ANNO_DATA_INTERRUZIONE_PENA,CAMPO_MESE_DATA_INTERRUZIONE_PENA,CAMPO_GIORNO_DATA_INTERRUZIONE_PENA) );
//    lDecMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
//** A NULL ** lDecMod.setFlagPresentanteIstanza( getRequestStringParameter( CAMPO_FLAG_PRESENTANTE_ISTANZA) );
//** A NULL ** lDecMod.setFlagDataInterruzioneInvalid( getRequestStringParameter( CAMPO_FLAG_DATA_INTERRUZIONE_INVALID) );
//    lDecMod.setProtocollo( getRequestStringParameter( CAMPO_PROTOCOLLO) );
//    lDecMod.setAltraAutorita( getRequestStringParameter( CAMPO_ALTRA_AUTORITA) );
//    lDecMod.setAltroLuogo( getRequestStringParameter( CAMPO_ALTRO_LUOGO) );
//-----lDecMod.setIdEventoGenerato( new BigDecimal(lIdEventoGenerato) );
//    lDecMod.setDataEspulsione( getRequestDateParameter( CAMPO_ANNO_DATA_ESPULSIONE,CAMPO_MESE_DATA_ESPULSIONE,CAMPO_GIORNO_DATA_ESPULSIONE) );
///** A NULL ** lDecMod.setFlagDecisioneTribunale( getRequestStringParameter( CAMPO_FLAG_DECISIONE_TRIBUNALE) );
//  lDecMod.setNumAnniRinvio( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RINVIO) );
//  lDecMod.setNumMesiRinvio( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RINVIO) );
//  lDecMod.setNumGiorniRinvio( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RINVIO) );
//  lDecMod.setDataRevocaSospensione( getRequestDateParameter( CAMPO_ANNO_DATA_REVOCA_SOSPENSIONE,CAMPO_MESE_DATA_REVOCA_SOSPENSIONE,CAMPO_GIORNO_DATA_REVOCA_SOSPENSIONE) );


/* GESTIONE COD ESITO :
 *
 * OGGETTO_DECISIONE       COD_ESITO
 * ----------------------------------
 * C005                -->   0011
 * U001                -->   0001
 * U003                -->   0001
 * C019                -->   0001
 */
    String lCodEsito = "-";
    if(!isRequestParameterNullObj(CAMPO_COD_ESITO))
    {
      lCodEsito = getRequestStringParameter( CAMPO_COD_ESITO);
    }
    else
    {
      String lCodOggettoDecisione = lDecMod.getCodOggettoDecisione();

      if(  lCodOggettoDecisione.equals("U001")
        || lCodOggettoDecisione.equals("U003")
        || lCodOggettoDecisione.equals("C019")
        )
      {
        lCodEsito = "0001";
      }
      else if( lCodOggettoDecisione.equals("C005") )
      {
        lCodEsito = "0011";
      }
    }

    lDecMod.setCodEsito(lCodEsito);

    lDecMod.setFasSieIdFascicoloSiep( lIdFascicolo );

    lDecMod.setCodOperatoreInserimento (this.getCodUtenteConnesso());
    lDecMod.setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());
    lDecMod.setDataInserimento         (DateUtils.getSysDate());

    //==========================================================================
    // Carico il model dell'evento
    //==========================================================================
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento        ("01");
    lEveMod.setCodTipoProvvedimento ( lDecMod.getCodTipoProvvedimento() );
    lEveMod.setCodMotivo            (lDecMod.getCodOggettoProcedimento());
    //lEveMod.setCodMotivo(lDecMod.getCodOggettoDecisione());
    lEveMod.setCodUfficioEmittente  (lCodiceUffEmittente);
    lEveMod.setCodLuogoEmittente    (lComMod.getCodComune());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodEsito( lDecMod.getCodEsito() );
    lEveMod.setDataEmissione(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setDataTrasmissioneAtti(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lEveMod.setFlagVideoSiep("S");  //
    lEveMod.setFlagStampaSiep("S"); //
    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

    lEveMod.setCodOperatoreInserimento (this.getCodUtenteConnesso());
    lEveMod.setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());
    lEveMod.setDataInserimento         (DateUtils.getSysDate());

    //==========================================================================
    // Effettua l'inserimento del descreto di ordinanza e il ricalcolo della pena
    // residua e già espiata
    //==========================================================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

    IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrdModRet = lCtrl.ExInserisciDecretoOrdinanzaSiep(lDecMod, lEveMod, lCalcoloPenaModel);

    //Prepara la pagina di destinazione
    String lPage = "";
    if(lDecOrdModRet.getIdEventoGenerato()!= null)
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sospensione.action.ActLoadDettaglioSospensione&IdEvento="+lDecOrdModRet.getIdEventoGenerato().toString();
    }
    else
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sospensione.action.ActLoadDettaglioSospensione&"+CAMPO_ID_DECRETO_ORDINANZA_SIEP+"="+lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString();
    }

    return lPage;
  }
}