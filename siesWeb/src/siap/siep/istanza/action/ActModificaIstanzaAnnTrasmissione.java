package siap.siep.istanza.action;

/**
* <p>Title: ActModificaIstanzaAnnTrasmissione</p>
* <p>Description: Classe Action per la modifica di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaIstanzaAnnTrasmissione extends ActionSiap implements ICostantiIstanza
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

   // BigDecimal lAnnoFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
     //BigDecimal lProgrFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);
     FascicoloSiepModel lFasRetMod = null;

     FascicoloSiepModel lFasMod = new FascicoloSiepModel();
     //lFasMod.setChiaveAnno(lAnnoFascicoloSiep);
     //lFasMod.setChiaveProgr(lProgrFascicoloSiep);
     lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

     IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
     lFasRetMod = lFasCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

     //Se il fascicolo esiste non archiviato/definito
     if(lFasRetMod != null && !"01".equals(lFasRetMod.getCodStatoFascicolo()))
     {
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
//     modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009       
       //lIstMod.setDataIrrevocabilita( lSentenza.getDataIrrevocabilita() );

       //lIstMod.setCodEsito("R"); //******
       lIstMod.setCodStatoIstanza("R"); // ISTANZA RIFERITA A FASCICOLO
     }
     else
     {
       throw new F3BException(F3BException.USER_MESSAGE, "Il Procedimento non esiste o risulta archiviato");
     }
         
     //Istanza
         lIstMod.setDataPresentazione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESENTAZIONE, CAMPO_MESE_DATA_PRESENTAZIONE, CAMPO_GIORNO_DATA_PRESENTAZIONE));
         lIstMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
         lIstMod.setNote(getRequestStringParameter(CAMPO_NOTE));

         lIstMod.setAnnoRegistro(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
// -- lIstMod.setProgrRegistro( getRequestBigDecimalParameter( CAMPO_PROGR_REGISTRO) ); // Gestito in automatico
         lIstMod.setCodEsito("31"); //******
         lIstMod.setCodStatoIstanza("I"); // ISTANZA
         lIstMod.setCodTipoAutoritaEmittente("-");
         lIstMod.setCodLuogoEmittente("-");

// --- ? lIstMod.setCodTipoUfficioDestinatario( getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_DESTINATARIO) );
         lIstMod.setCodTipoUfficioDestinatario("-"); //!!
// --- ? lIstMod.setCodLuogoDestinatario( getRequestStringParameter( CAMPO_COD_LUOGO_DESTINATARIO) );
         lIstMod.setCodLuogoDestinatario("-"); //!!
// --- ? lIstMod.setCodUfficioDestinatario( getRequestStringParameter( CAMPO_COD_UFFICIO_DESTINATARIO) );
         lIstMod.setCodUfficioDestinatario("-"); //!!

         lIstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
         lIstMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//* Il campo DescrUfficioInserimento viene usato come appoggio al CodComuneUtenteConnesso
//* per inserirlo eventualmente nella tabella evento come CodLuogoEmittente
         lIstMod.setDescrUfficioInserimento(getCodComuneUtenteConnesso());
         lIstMod.setDataInserimento(DateUtils.getSysDate());
         lIstMod.setSogIdSoggetto(lFasRetMod.getSogIdSoggetto());
         String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
         if(lIdEvento != null && !lIdEvento.equals(""))
           lIstMod.setEveIdEvento(new BigDecimal(lIdEvento));


      IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
      IstanzaModel lIstRetMod = new IstanzaModel();

      lIstRetMod = lCtrl.ExModificaIstanzaFascicoloSiep(lIstMod, lFasRetMod);


    String lPage = "";

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istanza.action.ActLoadDettaglioIstanzaAnnTrasmissione&"+CAMPO_ID_ISTANZA+"="+lIstRetMod.getIdIstanza().toString();

    return lPage;
  }
}