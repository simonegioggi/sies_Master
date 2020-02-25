package siap.siep.istanza.action;

/**
* <p>Title: ActModificaIstanza</p>
* <p>Description: Classe Action per la modifica di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaIstanza extends ActionSiap implements ICostantiIstanza
{
/**
* Azione di Modifica del Istanza
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_ISTANZA);

    //* Istanza *
    IstanzaModel lIstMod = new IstanzaModel();

    lIstMod.setIdIstanza(lId);

    lIstMod.setDataPresentazione( getRequestDateParameter( CAMPO_ANNO_DATA_PRESENTAZIONE, CAMPO_MESE_DATA_PRESENTAZIONE, CAMPO_GIORNO_DATA_PRESENTAZIONE) );
    lIstMod.setCodMotivo( getRequestStringParameter( CAMPO_COD_MOTIVO) );
    lIstMod.setCodEsito( getRequestStringParameter( CAMPO_COD_ESITO) );
    lIstMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
    lIstMod.setCodStatoIstanza("I"); // ISTANZA

    //lIstMod.setAnnoRegistro( getRequestBigDecimalParameter( CAMPO_ANNO_REGISTRO) );   //Non modificabili !!!
    //lIstMod.setProgrRegistro( getRequestBigDecimalParameter( CAMPO_PROGR_REGISTRO) );
/*
    lIstMod.setCodTipoUfficioDestinatario( getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_DESTINATARIO) );
    lIstMod.setCodLuogoDestinatario( getRequestStringParameter( CAMPO_COD_LUOGO_DESTINATARIO) );
    lIstMod.setCodUfficioDestinatario( getRequestStringParameter( CAMPO_COD_UFFICIO_DESTINATARIO) );
*/
    lIstMod.setCodTipoUfficioDestinatario( getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_DESTINATARIO) );
    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_DESTINATARIO )) );
    lIstMod.setCodLuogoDestinatario( lComMod.getCodComune() );
    //Controllo sull'esistenza dell'ufficio per quel comune
    if(!lIstMod.getCodTipoUfficioDestinatario().equals("-"))
      lIstMod.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune( lIstMod.getCodTipoUfficioDestinatario(), lComMod.getDescrizione()) );
    else
      lIstMod.setCodUfficioDestinatario("-");

    //* Sentenza *
    lIstMod.setAnnoSentenza( getRequestBigDecimalParameter( CAMPO_ANNO_SENTENZA) );
    lIstMod.setNumeroSentenza( getRequestStringParameter( CAMPO_NUMERO_SENTENZA) );
    lIstMod.setDataSentenza( getRequestDateParameter( CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA, CAMPO_GIORNO_DATA_SENTENZA) );
    lIstMod.setDataIrrevocabilita( getRequestDateParameter( CAMPO_ANNO_DATA_IRREVOCABILITA, CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA) );
    lIstMod.setCodTipoAutoritaEmittente( getRequestStringParameter( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
    lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
    lIstMod.setCodLuogoEmittente( lComMod.getCodComune() );
    //Controllo sull'esistenza dell'ufficio per quel comune
    if(!lIstMod.getCodTipoAutoritaEmittente().equals("-"))
      getCodUfficioByCodTipoUfficioDescrComune(lIstMod.getCodTipoAutoritaEmittente(), lComMod.getDescrizione());
    lIstMod.setDataIrrevocabilita( getRequestDateParameter( CAMPO_ANNO_DATA_IRREVOCABILITA,CAMPO_MESE_DATA_IRREVOCABILITA,CAMPO_GIORNO_DATA_IRREVOCABILITA) );

    //* Soggetto *
    //Il soggetto dell'istanza non può essere modificato
    //lIstMod.getSogIdSoggetto();

    boolean lPresenzaFascicoloNonArchiviato = false;
    FascicoloSiepModel lFasRetMod = null;

    // * Controllo esistenza fascicolo non archiviato *
    BigDecimal lAnnoFascicoloSiep  = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
    BigDecimal lProgrFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);

    if(lAnnoFascicoloSiep != null && lProgrFascicoloSiep != null)
    {
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();
      lFasMod.setChiaveAnno(lAnnoFascicoloSiep);
      lFasMod.setChiaveProgr(lProgrFascicoloSiep);
      lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

      IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      lFasRetMod = lFasCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      //Se il fascicolo esiste non archiviato/definito
      if(lFasRetMod != null && !"01".equals(lFasRetMod.getCodStatoFascicolo()))
      {
        lPresenzaFascicoloNonArchiviato = true;
        // * Viene associato all'istanza l'id soggetto legato al fascicolo non archiviato,
        // * e non viene preso in considerazione il soggetto indicato nella form
        lIstMod.setSogIdSoggetto( lFasRetMod.getSoggetto().getIdSoggetto() );
        // * Vengono associati all'istanza gli estremi della sentenza legati al fascicolo non archiviato,
        // * e non vengono presi in considerazione i dati della sentenza indicati nella form
        SentenzaModel lSentenza = lFasRetMod.getSentenza();
        lIstMod.setAnnoSentenza( lSentenza.getAnnoSentenza() );
        lIstMod.setNumeroSentenza( lSentenza.getNumeroSentenza() );
        lIstMod.setDataSentenza( lSentenza.getDataProvvedimento() ); //Corrisponde a DATA SENTENZA (perchè?)
        lIstMod.setCodTipoAutoritaEmittente( lSentenza.getCodTipoAutoritaEmittente() );
        lIstMod.setCodLuogoEmittente( lSentenza.getCodLuogoEmittente() );
//      modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
        //lIstMod.setDataIrrevocabilita( lSentenza.getDataIrrevocabilita() );

        //lIstMod.setCodEsito("R"); //******
        lIstMod.setCodStatoIstanza("R"); // ISTANZA RIFERITA A FASCICOLO
      }
      else
        throw new F3BException(F3BException.USER_MESSAGE, "Il Procedimento non esiste o risulta archiviato");
    }

    // * Soggetto Presentante *
    lIstMod.setCognomeSoggettoPresentante( getRequestStringParameter( CAMPO_COGNOME_SOGGETTO_PRESENTANTE) );
    lIstMod.setNomeSoggettoPresentante( getRequestStringParameter( CAMPO_NOME_SOGGETTO_PRESENTANTE) );

    //* Avvocato *
    lIstMod.setCognomeAvvocato( getRequestStringParameter( ICostantiAvvocato.CAMPO_COGNOME ) );
    lIstMod.setNomeAvvocato( getRequestStringParameter( ICostantiAvvocato.CAMPO_NOME ) );
    lIstMod.setForoCompetenza( getRequestStringParameter( ICostantiAvvocato.CAMPO_FORO ) );

    lIstMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lIstMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lIstMod.setDescrUfficioAggiornamento(getCodComuneUtenteConnesso());
    lIstMod.setDataAggiornamento(DateUtils.getSysDate());

    IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
    IstanzaModel lIstRetMod = new IstanzaModel();
    if(lPresenzaFascicoloNonArchiviato)
    {
      String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
      if(lIdEvento != null && !lIdEvento.equals(""))
        lIstMod.setEveIdEvento(new BigDecimal(lIdEvento));
      lIstRetMod = lCtrl.ExModificaIstanzaFascicoloSiep(lIstMod, lFasRetMod);
    }
    else
    {
      lIstRetMod = lCtrl.ExModificaIstanza(lIstMod);
    }

    String lPage = "";

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istanza.action.ActLoadDettaglioIstanza&"+CAMPO_ID_ISTANZA+"="+lIstRetMod.getIdIstanza().toString();

    return lPage;
  }
}