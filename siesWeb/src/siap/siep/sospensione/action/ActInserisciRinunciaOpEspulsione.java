package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciRinunciaOpEspulsione</p>
 * <p>Description: Classe Action per l'inserimento Rinuncia Opposizione di Espulsione</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciRinunciaOpEspulsione extends ActMisuraAlternativa
{
  /**
   * Azione di Inserimento della Rinuncia Opposizione Espulsione
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

//Preparo L'evento Verbale
    EventoModel lEveVer = new EventoModel();

    lEveVer.setCodTipoEvento("07");
    lEveVer.setCodTipoProvvedimento("15");
    lEveVer.setCodMotivo("0924");
    lEveVer.setFlagStampaSiep("S");
    lEveVer.setFlagVideoSiep("S");
    lEveVer.setCodUfficioEmittente(lCodiceUfficio);
    lEveVer.setFasSieIdFascicoloSiep(lIdFascicolo);
    Date lDataEmissione = getRequestDateParameter("VERBALE_"+ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, "VERBALE_"+ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, "VERBALE_"+ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE);
    lEveVer.setDataEmissione(lDataEmissione);
    lEveVer.setCodOperatoreInserimento(lCodiceOperatore);
    lEveVer.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEveVer.setDataInserimento(DateUtils.getSysDate());
    lEveVer.setCodUfficioInserimento(lCodiceUfficio);
    lEveVer.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEveVer.setCodEsito("-");
    lEveVer.setCodLuogoDestinatario("-");
    lEveVer.setCodTipoUfficioDestinatario("-");
    lEveVer.setCodMagistrato("-");

//Verbale
    VerbaleModel lVerMod = new VerbaleModel();
    lVerMod.setCodTipoVerbale("07");
    lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
                                                        ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
                                                        ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));

    lVerMod.setDataEmissione(getRequestDateParameter("VERBALE_"+ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
                                                     "VERBALE_"+ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE,
                                                     "VERBALE_"+ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));

    if(!this.isRequestParameterNullObj("VERBALE_"+ICostantiVerbale.CSS_ID_CSSA))
      lVerMod.setCssIdCssa(this.getRequestBigDecimalParameter("VERBALE_"+ICostantiVerbale.CSS_ID_CSSA));

    if(!this.isRequestParameterNullObj("VERBALE_"+ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE))
      lVerMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter("VERBALE_"+ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE));
    else
      lVerMod.setIstDetIdIstitutoDetenzione("-");

    if(   !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)
       && !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO))
    {
      ComuneModel lComMod = this.getCodComuneByDescr(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO));

      lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
      lVerMod.setCodTipoUfficioFirmatario(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));
    }
    else
    {
      lVerMod.setCodTipoUfficioFirmatario("-");
      lVerMod.setCodLuogoUfficioFirmatario("-");
    }

    lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lVerMod.setDataInserimento(DateUtils.getSysDate());

    if(!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO))
      lVerMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));

    if(!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NOTE))
      lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));

    
   EventoNotificaModel lEveNot = null;
   PenaResiduaModel lPenaRes = null;   
   
//Evento Provvedimento   
	EventoModel lEvePro = new EventoModel();	   
    lEvePro.setCodTipoEvento("01");
    lEvePro.setCodTipoProvvedimento("09");
    lEvePro.setCodMotivo("2140");
    lEvePro.setFlagStampaSiep("S");
    lEvePro.setFlagVideoSiep("S");
    lEvePro.setCodUfficioEmittente(lCodiceUfficio);
    lEvePro.setFasSieIdFascicoloSiep(lIdFascicolo);
    Date lDataEmi = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
    lEvePro.setDataEmissione(lDataEmi);
    lEvePro.setCodOperatoreInserimento(lCodiceOperatore);
    lEvePro.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEvePro.setDataInserimento(DateUtils.getSysDate());
    lEvePro.setCodUfficioInserimento(lCodiceUfficio);
    lEvePro.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEvePro.setCodEsito("-");

    lEvePro.setCodLuogoDestinatario("-");
    lEvePro.setCodTipoUfficioDestinatario("-");
    lEvePro.setCodMagistrato(calcolaMagistrato());
    
    lEveNot = new EventoNotificaModel();
    lEveNot.setEvento(lEvePro);

//notifiche
    NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
    lEveNot.setNotifiche(lNotificheMod);

//Pena Residua
    BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
    lPenaRes = new PenaResiduaModel();     
    lPenaRes.setIdPenaResidua(lIdPenaRes);
    if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
      lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE, ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
          ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
    
    ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
    EventoNotificaModel lEveNotModel = lCtrlSosp.ExInserisciEventoNotificaVerbale(lEveNot,lPenaRes,lEveVer,lVerMod);
    

//Prepara la pagina di destinazione
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sospensione.action.ActDettaglioRinunciaOpEspulsione&" + ICostantiEvento.CAMPO_ID_EVENTO +
            "=" +lEveNotModel.getEvento().getIdEvento().toString();
    return lPage;
  }
}