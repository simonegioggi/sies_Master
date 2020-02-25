package siap.siep.ripristino.action;

/**
* <p>Title: ActInserisciRipristino</p>
* <p>Description: Classe Action per l'inserimento di Ripristino</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

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

public class ActInserisciRipristino extends ActionSiap
                                    implements ICostantiDecretoOrdinanzaSiep
{
  /**
  * Azione di Inserimento del Ripristino
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

    //l'id dell'eventuale record presente
    //lDecMod.setIdDecretoOrdinanzaSiep( getRequestBigDecimalParameter( CAMPO_ID_DECRETO_ORDINANZA_SIEP) );

    lDecMod.setDataRicezioneProvvedimento( getRequestDateParameter( CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO,CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO,CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO) );
    lDecMod.setDataEmissioneProvvedimento( getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO,CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO,CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO) );

lDecMod.setCodTipoRegistroOrdinanza("-");

//    lDecMod.setAnnoRegistro( getRequestBigDecimalParameter( CAMPO_ANNO_REGISTRO) );
//    lDecMod.setNumRegistro( getRequestBigDecimalParameter( CAMPO_NUM_REGISTRO) );
//    lDecMod.setAnnoProvvedimento( getRequestBigDecimalParameter( CAMPO_ANNO_PROVVEDIMENTO) );
//    lDecMod.setNumProvvedimento( getRequestBigDecimalParameter( CAMPO_NUM_PROVVEDIMENTO) );

lDecMod.setCodTipoProvvedimento("-");

//    lDecMod.setCodTipoAutoritaEmittente( getRequestStringParameter( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
//    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
//    lDecMod.setCodLuogoEmittente( lComMod.getCodComune() );
    lDecMod.setCodTipoAutoritaEmittente("-");
    lDecMod.setCodLuogoEmittente("-");

    //Controllo sull'esistenza dell'ufficio per quel comune
//    String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE) );

//    lDecMod.setDataSospensioneEsecuzione( getRequestDateParameter( CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE,CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE) );

//    lDecMod.setDataDifferimento( getRequestDateParameter( CAMPO_ANNO_DATA_DIFFERIMENTO,CAMPO_MESE_DATA_DIFFERIMENTO,CAMPO_GIORNO_DATA_DIFFERIMENTO) );
//    lDecMod.setDataRinvio( getRequestDateParameter( CAMPO_ANNO_DATA_RINVIO,CAMPO_MESE_DATA_RINVIO,CAMPO_GIORNO_DATA_RINVIO) );
    lDecMod.setDataFineInterruzione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_INTERRUZIONE,CAMPO_MESE_DATA_FINE_INTERRUZIONE,CAMPO_GIORNO_DATA_FINE_INTERRUZIONE) );
//    lDecMod.setDataDepositoIstanza( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,CAMPO_MESE_DATA_DEPOSITO_ISTANZA,CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA) );
//    lDecMod.setDataInterruzionePena( getRequestDateParameter( CAMPO_ANNO_DATA_INTERRUZIONE_PENA,CAMPO_MESE_DATA_INTERRUZIONE_PENA,CAMPO_GIORNO_DATA_INTERRUZIONE_PENA) );
    lDecMod.setCodOggettoDecisione("-");
    lDecMod.setMotivazioni( getRequestStringParameter( CAMPO_MOTIVAZIONI) );
    lDecMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
//    lDecMod.setFlagScarcerareScarcerato( getRequestStringParameter( CAMPO_FLAG_SCARCERARE_SCARCERATO) );
//** A NULL ** lDecMod.setFlagPresentanteIstanza( getRequestStringParameter( CAMPO_FLAG_PRESENTANTE_ISTANZA) );

lDecMod.setCodContenutoDecreto( "-" );

    lDecMod.setCodOggettoProcedimento("-");
//** A NULL ** lDecMod.setFlagDataInterruzioneInvalid( getRequestStringParameter( CAMPO_FLAG_DATA_INTERRUZIONE_INVALID) );
    lDecMod.setProtocollo( getRequestStringParameter( CAMPO_PROTOCOLLO) );
    lDecMod.setAltraAutorita( getRequestStringParameter( CAMPO_ALTRA_AUTORITA) );
    lDecMod.setAltroLuogo( getRequestStringParameter( CAMPO_ALTRO_LUOGO) );

//-----lDecMod.setIdEventoGenerato( new BigDecimal(lIdEventoGenerato) );

lDecMod.setFlagElaborato( "N" );

//    lDecMod.setDataEspulsione( getRequestDateParameter( CAMPO_ANNO_DATA_ESPULSIONE,CAMPO_MESE_DATA_ESPULSIONE,CAMPO_GIORNO_DATA_ESPULSIONE) );
///** A NULL ** lDecMod.setFlagDecisioneTribunale( getRequestStringParameter( CAMPO_FLAG_DECISIONE_TRIBUNALE) );

lDecMod.setCodEsito("-");

//    lDecMod.setNumAnniRinvio( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RINVIO) );
//    lDecMod.setNumMesiRinvio( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RINVIO) );
//    lDecMod.setNumGiorniRinvio( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RINVIO) );
//    lDecMod.setDataRevocaSospensione( getRequestDateParameter( CAMPO_ANNO_DATA_REVOCA_SOSPENSIONE,CAMPO_MESE_DATA_REVOCA_SOSPENSIONE,CAMPO_GIORNO_DATA_REVOCA_SOSPENSIONE) );
    lDecMod.setFasSieIdFascicoloSiep( lIdFascicolo );

    lDecMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lDecMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lDecMod.setDataInserimento(DateUtils.getSysDate());

    boolean lInterruzioneNonValida = false;
    if(isRequestChecked(CAMPO_FLAG_INTERRUZIONE_VALIDA))
      lInterruzioneNonValida = true;

    // EVENTO
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01");
    // Cod Tipo Provvedimento passa da 04 a 09. Luigi 23-09-2005
    lEveMod.setCodTipoProvvedimento("09");
   // lEveMod.setCodTipoProvvedimento("04");
    lEveMod.setCodMotivo("0272");
    lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
    lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodEsito("-");
    lEveMod.setDataEmissione(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setDataTrasmissioneAtti(lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lEveMod.setFlagVideoSiep("S");
    lEveMod.setFlagStampaSiep("S");

    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lEveMod.setDataInserimento(DateUtils.getSysDate());

    IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrdModRet = lCtrl.ExInserisciDecretoOrdinanzaSiepRipristino(lDecMod, lEveMod, lInterruzioneNonValida);

    //Prepara la pagina di destinazione
    String lPage = "";

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ripristino.action.ActLoadDettaglioRipristino&"+CAMPO_ID_DECRETO_ORDINANZA_SIEP+"="+lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString();

    return lPage;
  }
}