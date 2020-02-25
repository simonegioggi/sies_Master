package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciOrdinanzaRinvioUdienza </p>
* <p>Description: Classe Action per l'inserimento dell' Ordinanza di Rinvio Udienza.</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciOrdinanzaRinvioUdienza extends ActionSius implements ICostantiUdienza
{
  public String processRequest() throws Exception
  {
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();
    //String lCodiceUfficioPG = null;
    String lCodComune       = getCodComuneUtenteConnesso();
    //String lDescrComune     = getUfficioUtenteConnesso().getDescrComune();
    String lCodMagistrato   = null;
    BigDecimal lIdFasSius   = null;


    // Preleva DATI dalla request
    Date lDataUdienza     = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA,CAMPO_MESE_DATA_UDIENZA,CAMPO_GIORNO_DATA_UDIENZA);
    Date lDataEmissione   = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE );
    BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);

    // Preleva dalla sessione il model fascicoloSiusGP
    FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    if(lFascicoloGPModel == null || lFascicoloGPModel.getFascicoloSiusModel() == null || lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius() == null)
      throw new SIUSException(SIUSException.USER_MESSAGE,"Fascicolo SIUS non in sessione");

    lIdFasSius = lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius();

    //  Ricerca del Magistrato Relatore
    IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
    MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(lIdFasSius);

    // Preleva il codice Magistrato_Relatore
    if (lMagRel != null && lMagRel.getMagistrato() != null)
      lCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

    GeneraleProcedimentoModel aGeneraleProcedimentoold = new GeneraleProcedimentoModel();
    aGeneraleProcedimentoold.setIdGeneraleProcedimento(lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    aGeneraleProcedimentoold.setUdiIdUdienza(lFascicoloGPModel.getGeneraleProcedimentoModel().getUdiIdUdienza());

    if (lFascicoloGPModel.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null )
      throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

    //Si istanzia un model UdienzaProcedimentoModel.
    UdienzaModel lUdi = new UdienzaModel();
    if (getRequestStringParameter( ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA ).length() >2)
    {
      lUdi.setDataUdienza(lDataUdienza);
      if (lFascicoloGPModel.getGeneraleProcedimentoModel().getDataCameraConsiglio().after(lDataUdienza) )
        throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito.  La data udienza selezionata non può essere precedente all'udienza già fissata.");

      // Verifica se per la data selezionata esiste una udienza.
      IUdienza lUdienzaCRtl = SIUSLookupRemote.getUdienzaRemote();
      UdienzaModel lUdienza = lUdienzaCRtl.ExRicercaUdienzaByDate(lDataUdienza,getCodUfficioUtenteConnesso());
      // Se non esistono udienze solleva un errore di eccezione.
      if (lUdienza == null)
         throw new SIUSException(SIUSException.USER_MESSAGE,"Non esiste nessuna Udienza con questa data.");
       else
         lUdi.setIdUdienza(lId);
    }
    lUdi.setCodOperatoreInserimento(lCodiceOperatore);
    lUdi.setCodUfficioInserimento(lCodiceUfficio);
    lUdi.setDataInserimento(DateUtils.getSysDate());

    // STUB 21/06/2004 Aggiunta la condizione di controllo del CHECK RUOLO.
    if (lUdi.getIdUdienza() == null &&
        isRequestParameterNullObj(CAMPO_CHECK_RUOLO) )
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Per il procedimento non risulta fissata la nuova data udienza.");
      return IWebConstants.PG_MESSAGE;
    }

    //ID Udienza
    //Inserisci Udienza_Procedimento con l'id del generale_Procedimento associato con il FasciolcoSIUS in sessione.
    //Update della data_CAMERA_CONSIGLIO in generale_proceidmento
    FascicoloGPModel lFasc = new FascicoloGPModel();
    //Imposta la data camera di consiglio, con la data udienza.
    lFasc.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius( lIdFasSius );
    lFasc.getGeneraleProcedimentoModel().setDataCameraConsiglio(lDataUdienza);
    lFasc.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    lFasc.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(lCodiceOperatore);
    lFasc.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
    lFasc.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter(ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE));
    lFasc.getGeneraleProcedimentoModel().setCodOggettoProcedimento(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
    // 07/10/2003 Il campo UDI_ID_UDIENZA è impostato nel controller.


    // EventoNotificaModel lEve = new EventoNotificaModel();
    EventoModel lEve = new EventoModel();
    lEve.setCodTipoEvento("01"); //Tipo Evento = Provvedimento
    lEve.setCodTipoProvvedimento("03"); //Tipo Provvedimento = Decreto
    lEve.setCodMotivo("0603");  // Ordinanza Rinvio Udienza
    lEve.setTemIdTemplate("SIUS_OR_603");
    lEve.setDataEmissione( lDataEmissione );
    lEve.setFasSiuIdFascicoloSius( lIdFasSius );
    lEve.setCodOperatoreInserimento(lCodiceOperatore);
    lEve.setCodLuogoEmittente(lCodComune);
    lEve.setCodUfficioEmittente(lCodiceUfficio);
    lEve.setCodUfficioInserimento(lCodiceUfficio);
    lEve.setDataInserimento(DateUtils.getSysDate());
    lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEve.setCodEsito("0603");
    lEve.setCodLuogoDestinatario("-");
    lEve.setCodTipoUfficioDestinatario("-");
    lEve.setFasSieIdFascicoloSiep(lFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    lEve.setFasSiuIdFascicoloSius(lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
    lEve.setCodMagistrato(lCodMagistrato); // Viaggia il CodMagistrato nel Model Evento.
    // lEve.setNomeTemplate("FU1");

    DepositoOrdinanzaPcModel lDepositoOrdinanzaPc = new DepositoOrdinanzaPcModel();
    lDepositoOrdinanzaPc.setDataCameraConsiglio(lFascicoloGPModel.getGeneraleProcedimentoModel().getDataCameraConsiglio());
    lDepositoOrdinanzaPc.setDataUdienza(lDataUdienza);
    lDepositoOrdinanzaPc.setCodMagistrato(lCodMagistrato);
    lDepositoOrdinanzaPc.setGenPridGeneraleProcedimento(lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    lDepositoOrdinanzaPc.setCodOperatoreInserimento(lCodiceOperatore);
    lDepositoOrdinanzaPc.setDataInserimento(DateUtils.getSysDate());
    lDepositoOrdinanzaPc.setCodUfficioInserimento(lCodiceUfficio);
    lDepositoOrdinanzaPc.setCodTipoOrdinanza(ICostantiDepositoOrdinanzaPc.RINVIO_UDIENZA);
    // Correzioni di Umberto
    lDepositoOrdinanzaPc.setIdCssaComp(new BigDecimal ("9999"));
    lDepositoOrdinanzaPc.setCodUfficioMagistratoComp("-");
    lDepositoOrdinanzaPc.setCodUffTdsConcessoRiduzione("-");

    // Gestione oggetti Tenore
    String lCodOggetti = getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
    String lDescOggetti = getRequestStringParameter( ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);

    // STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
    String lCodDettaglioOggetti = getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO);
    TenoreModel[] lTenori = this.parseOggettiTenori(lCodOggetti, lDescOggetti, lCodDettaglioOggetti );

    // STUB 22/06/2004 modificata la condizione di rinvio udienza.
    //if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase("02"))
    if (lFascicoloGPModel.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") != 0   &&
        lFascicoloGPModel.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") != 0 )
    {
      if (lFascicoloGPModel.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
      {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");
        return IWebConstants.PG_MESSAGE;
      }
      else
      {
        IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
        EventoModel lEveModel = lCtrl.ExInserisciOrdinanzaRinvioUdienza(lUdi,lFasc, lEve, lTenori, lDepositoOrdinanzaPc,aGeneraleProcedimentoold);

        // STUB 18/05/2004 Aggiornamento dei dati in sessione.
        IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
        lFascicoloGPModel = lFasCtrl.ExRicercaFascicoloByKey(lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

        setSessionAttribute("fascicoloSiusGP",lFascicoloGPModel);

        // Prepara la pagina di redirezione.
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.udienza.action.ActDettaglioVerbaleRinvioUdienza");
        lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,"" + lEveModel.getIdEvento() );
        lPage.setParameter(ICostantiUdienza.CAMPO_ID_UDIENZA, "" + lUdi.getIdUdienza() );
        lPage.setParameter("modalita", "I");
        lPage.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, "" + lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
        return lPage.toString();

      }
    }
    else
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Procedimento già definito.");
      return IWebConstants.PG_MESSAGE;
    }

  }

}