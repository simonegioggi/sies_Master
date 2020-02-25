package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciInterruzione</p>
 * <p>Description: Classe Action per l'inserimento di Interruzione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
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

public class ActInserisciInterruzione extends ActionSiap
                                      implements ICostantiSospensione,
                                                 ICostantiDecretoOrdinanzaSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Inserimento del Sospensione Interruzione
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

//l'id dell'eventuale record presente
    lDecMod.setIdDecretoOrdinanzaSiep(getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP));

    lDecMod.setDataRicezioneProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO,
                                                                  CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO,
                                                                  CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO));

    lDecMod.setDataEmissioneProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO,
                                                                  CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO,
                                                                  CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO));
    lDecMod.setCodTipoRegistroOrdinanza("-"); //SIUS
    lDecMod.setCodOggettoProcedimento(getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE));
    lDecMod.setAltraAutorita(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
    // ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
    lDecMod.setAltroLuogo(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
    lDecMod.setDataInterruzionePena(getRequestDateParameter(CAMPO_ANNO_DATA_INTERRUZIONE_PENA,
                                                            CAMPO_MESE_DATA_INTERRUZIONE_PENA,
                                                            CAMPO_GIORNO_DATA_INTERRUZIONE_PENA));
    lDecMod.setProtocollo(getRequestStringParameter(CAMPO_PROTOCOLLO));

//    lDecMod.setDataFineInterruzione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_INTERRUZIONE,CAMPO_MESE_DATA_FINE_INTERRUZIONE,CAMPO_GIORNO_DATA_FINE_INTERRUZIONE) );
//    lDecMod.setDataDepositoIstanza( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,CAMPO_MESE_DATA_DEPOSITO_ISTANZA,CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA) );
//    lDecMod.setDataInterruzionePena( getRequestDateParameter( CAMPO_ANNO_DATA_INTERRUZIONE_PENA,CAMPO_MESE_DATA_INTERRUZIONE_PENA,CAMPO_GIORNO_DATA_INTERRUZIONE_PENA) );
    lDecMod.setCodOggettoDecisione("-");
    lDecMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));
    lDecMod.setCodContenutoDecreto("-");
    lDecMod.setCodTipoProvvedimento("-");
    lDecMod.setCodTipoAutoritaEmittente("-");

    lDecMod.setCodLuogoEmittente("-");
    lDecMod.setFlagElaborato("N");
    lDecMod.setCodEsito("-");

    lDecMod.setFasSieIdFascicoloSiep(lIdFascicolo);

    lDecMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lDecMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lDecMod.setDataInserimento(DateUtils.getSysDate());

    // evento
    EventoModel lEveMod = new EventoModel();
    lEveMod.setCodTipoEvento("01");
    lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE));
    lEveMod.setCodTipoProvvedimento(getCodTipoProvvedimento(lEveMod.getCodMotivo()));
    lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso() );

    // ComuneModel lComuneEmitt =this.getCodComuneByDescr(lDecMod.getAltroLuogo());
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
    lEveMod.setDataEmissione( lDecMod.getDataEmissioneProvvedimento());
    IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

    DecretoOrdinanzaSiepModel lDecOrdModRet = lCtrl.ExInserisciOModificaDecretoSospensione(lDecMod, lEveMod, "I", lCalcoloPenaModel);

//Prepara la pagina di destinazione
    String lPage = "";

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sospensione.action.ActLoadDettaglioInterruzione&" + ICostantiEvento.CAMPO_ID_EVENTO +
            "=" + lDecOrdModRet.getIdEventoGenerato().toString();

    return lPage;
  }

  /**
   * Il tipo di provvedimento emesso dipende dal codice Motivo Luigi 11-10-2005
   *
   * @param aCodMotivo
   */
  private String getCodTipoProvvedimento(String aCodMotivo) throws F3BException
  {
    String lCodTipoProv = "";

    if (aCodMotivo == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo assente");
    else if (aCodMotivo.compareTo("0266") == 0)
      lCodTipoProv = "12";
    else if (aCodMotivo.compareTo("0267") == 0)
      lCodTipoProv = "12";
    else if (aCodMotivo.compareTo("0268") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0269") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0270") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0366") == 0) // new! since 40upd01
      lCodTipoProv = "25";
    else
      throw new F3BException(F3BException.USER_MESSAGE,  "Codice Motivo errato -> " + aCodMotivo);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("CodTipoProvvedimento -> " + lCodTipoProv);
    return lCodTipoProv;
  }
}