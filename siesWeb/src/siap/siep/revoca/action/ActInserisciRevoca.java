package siap.siep.revoca.action;

/**
* <p>Title: ActInserisciRevoca</p>
* <p>Description: Classe Action per l'inserimento di Sospensione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciRevoca extends ActionSiap
                                     implements ICostantiRevoca,
                                                ICostantiDecretoOrdinanzaSiep
{
  /**
  * Azione di Inserimento del Sospensione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    //==========================================================================
    //
    //==========================================================================
    DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

    //l'id dell'eventuale record presente
    lDecMod.setIdDecretoOrdinanzaSiep( getRequestBigDecimalParameter( CAMPO_ID_DECRETO_ORDINANZA_SIEP) );

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
    lDecMod.setCodOggettoProcedimento     ( getRequestStringParameter( CAMPO_COD_OGGETTO_PROCEDIMENTO) );
    lDecMod.setDataRevocaSospensione      ( getRequestDateParameter( CAMPO_ANNO_DATA_REVOCA_SOSPENSIONE,CAMPO_MESE_DATA_REVOCA_SOSPENSIONE,CAMPO_GIORNO_DATA_REVOCA_SOSPENSIONE) );
    lDecMod.setCodContenutoDecreto        ( "-" );
    lDecMod.setFlagElaborato              ( "N" );


    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
    lDecMod.setCodLuogoEmittente( lComMod.getCodComune() );


    //lDecMod.setDataSospensioneEsecuzione( getRequestDateParameter( CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE) );
//    lDecMod.setDataDifferimento( getRequestDateParameter( CAMPO_ANNO_DATA_DIFFERIMENTO,CAMPO_MESE_DATA_DIFFERIMENTO,CAMPO_GIORNO_DATA_DIFFERIMENTO) );
//    lDecMod.setDataRinvio( getRequestDateParameter( CAMPO_ANNO_DATA_RINVIO,CAMPO_MESE_DATA_RINVIO,CAMPO_GIORNO_DATA_RINVIO) );
//    lDecMod.setDataFineInterruzione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_INTERRUZIONE,CAMPO_MESE_DATA_FINE_INTERRUZIONE,CAMPO_GIORNO_DATA_FINE_INTERRUZIONE) );
//    lDecMod.setDataDepositoIstanza( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,CAMPO_MESE_DATA_DEPOSITO_ISTANZA,CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA) );
//    lDecMod.setDataInterruzionePena( getRequestDateParameter( CAMPO_ANNO_DATA_INTERRUZIONE_PENA,CAMPO_MESE_DATA_INTERRUZIONE_PENA,CAMPO_GIORNO_DATA_INTERRUZIONE_PENA) );
//    lDecMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
//    lDecMod.setFlagScarcerareScarcerato( getRequestStringParameter( CAMPO_FLAG_SCARCERARE_SCARCERATO) );
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
    //
    //==========================================================================
    EventoModel lEveMod = new EventoModel();

    //Controllo sull'esistenza dell'ufficio per quel comune
    String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE) );

    lEveMod.setCodTipoEvento("01");
    lEveMod.setCodTipoProvvedimento( lDecMod.getCodTipoProvvedimento() );
    //lEveMod.setCodMotivo(lDecMod.getCodOggettoDecisione());
    lEveMod.setCodMotivo(lDecMod.getCodOggettoProcedimento());
    lEveMod.setCodUfficioEmittente(lCodiceUffEmittente);
    lEveMod.setCodLuogoEmittente(lComMod.getCodComune());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodEsito( lDecMod.getCodEsito() );
    lEveMod.setDataEmissione(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setDataTrasmissioneAtti(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
//modifica 15-02-2006 -- Dario -- Viviana
//il FlagStampaSiep viene settato non ad 'N' ma ad 'S' in modo da comparire
//nello stato di esecuzione sui template
//il FlagVideoSiep viene settato ad 'S' per essere visto nella lista
    //lEveMod.setFlagVideoSiep("N");
    //lEve.setFlagStampaSiep("N");
    lEveMod.setFlagStampaSiep("S");
    lEveMod.setFlagVideoSiep("S");

    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lEveMod.setDataInserimento(DateUtils.getSysDate());

    //==========================================================================
    //
    //==========================================================================
    IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrdModRet = lCtrl.ExInserisciRevocaDecretoOrdinanzaSiep(lDecMod, lEveMod);

    //Prepara la pagina di destinazione
    String lPage = "";

    //lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.revoca.action.ActLoadDettaglioRevoca&"+CAMPO_ID_DECRETO_ORDINANZA_SIEP+"="+lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString();
    //	26/11/2010 Sostituita ---> lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.revoca.action.ActLoadInserisciOrdineEsecuzioneRevoca&"+CAMPO_ID_DECRETO_ORDINANZA_SIEP+"="+lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString();
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.revoca.action.ActLoadInserisciOrdineEsecuzioneRevoca&"+CAMPO_ID_DECRETO_ORDINANZA_SIEP+"="+lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString()+"&CodMotivo="+lDecMod.getCodOggettoProcedimento().trim();

    return lPage;
  }
}