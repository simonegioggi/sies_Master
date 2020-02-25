package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.action.ICostantiRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActRichiestaApplicazioneBenefici</p>
* <p>Description: Classe Action per l'inserimento di una Richiesta di Applicazione Benefici</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActRichiestaApplicazioneBenefici extends ActionSiap implements ICostantiRichiesta
{

  public String processRequest() throws Exception
  {
    // ANNOTAZIONE
    AnnotazioneManualeModel lAnnMod = setAnnotazioneManuale();

    // EVENTO
    EventoModel lEveMod = setEvento("0122" );
    lEveMod.setDataEmissione(lAnnMod.getDataRichiesta());

    String lPage = "";
    lPage = eseguiRichiesta(lAnnMod, lEveMod);

    return lPage;
  }
	
  /**
   * Recupera dalla form i dati dell'annotazione manuale e carica il model 
   */
  protected AnnotazioneManualeModel setAnnotazioneManuale() throws Exception
  {

    AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

    lAnnMod.setFlagValidato("N");

    lAnnMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));
    lAnnMod.setCodDpr(getRequestStringParameter("dpr"));
    lAnnMod.setDataRichiesta(getRequestDateParameter( CAMPO_ANNO_DATA_RICHIESTA, CAMPO_MESE_DATA_RICHIESTA, CAMPO_GIORNO_DATA_RICHIESTA) );
    lAnnMod.setFlagPiuMeno(getRequestStringParameter(CAMPO_FLAG_PIU_MENO));

    lAnnMod.setFlagAppProvvisoria("R"); //RICHIESTA SENZA ANTICIPAZIONE
    if (isRequestChecked(CAMPO_FLAG_APP_PROVVISORIA))
    {
      lAnnMod.setFlagAppProvvisoria("A"); //RICHIESTA CON ANTICIPAZIONE
    }
    
    lAnnMod.setFlagBeneficioDetratto("N"); // BISOGNA EFFETTUARE IL CALCOLO DELLA PENA
    if (isRequestChecked(ICostantiAnnotazioneManuale.CAMPO_FLAG_BENEFICIO_DETRATTO))
    {
      lAnnMod.setFlagBeneficioDetratto("S"); //NO CALCOLO PENA
    }

    lAnnMod.setFlagConforme("-");
    lAnnMod.setCodFonte("-");
    lAnnMod.setCodSottonumerazione("-");
    lAnnMod.setCodCausaleComputo("-");

    String GRec=getRequestStringParameter("GRec");
    String MRec=getRequestStringParameter("MRec");
    String ARec=getRequestStringParameter("ARec");
    String Multa=getRequestStringParameter("Multa");
    String Multa_dec=getRequestStringParameter("Mul_dec");

    String noteRec=getRequestStringParameter("noteRec");

    if (!ARec.equals(""))
      lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
    if (!MRec.equals(""))
      lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
    if (!GRec.equals(""))
      lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

    lAnnMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
    
    if (!Multa.equals(""))
    {
      if (!Multa_dec.equals(""))
      {
        lAnnMod.setImportoMulta(new BigDecimal(Multa+"."+Multa_dec));
      }
      else
        lAnnMod.setImportoMulta(new BigDecimal(Multa));
    }
    else if (!Multa_dec.equals(""))
      lAnnMod.setImportoMulta(new BigDecimal("0."+Multa_dec));

    lAnnMod.setNoteReclusione(noteRec);

    String GArr = getRequestStringParameter("GArr");
    String MArr = getRequestStringParameter("MArr");
    String AArr = getRequestStringParameter("AArr");
    String Ammenda     = getRequestStringParameter("Ammenda");
    String Ammenda_dec = getRequestStringParameter("Amm_dec");

    if (!AArr.equals(""))
      lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
    if (!MArr.equals(""))
      lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
    if (!GArr.equals(""))
      lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

    lAnnMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());

    if (!Ammenda.equals(""))
    {
      if (!Ammenda_dec.equals(""))
      {
        lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda+"."+Ammenda_dec));
      }
      else
        lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
    }
    else if  (!Ammenda_dec.equals(""))
      lAnnMod.setImportoAmmenda(new BigDecimal("0."+Ammenda_dec));

    lAnnMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lAnnMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lAnnMod.setDataInserimento         (DateUtils.getSysDate());

    return lAnnMod;
  }
  
  private EventoModel setEvento() throws Exception
  {
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01");
    lEveMod.setFlagDocumentoRegistrato(null);
    lEveMod.setFlagStampaSiep("S");
    lEveMod.setFlagVideoSiep("S");
    lEveMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
    lEveMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
    lEveMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());

    return lEveMod;
  }

  protected EventoModel setEvento( String aCodTipoProvvedimento, String aCodMotivo) throws Exception
  {
    EventoModel lEveMod = setEvento();

    lEveMod.setCodTipoProvvedimento(aCodTipoProvvedimento);
    lEveMod.setCodMotivo(aCodMotivo);

    return lEveMod;
  }

  protected EventoModel setEvento(String aCodMotivo) throws Exception
  {
    EventoModel lEveMod = setEvento();

    lEveMod.setCodTipoProvvedimento("26");
    lEveMod.setCodMotivo(aCodMotivo);
    lEveMod.setFlagDocumentoRegistrato("S");

    return lEveMod;
  }

  /**
   * Effettua l'inserimento della richiesta e delle annotazioni 
   *  
   * @param lAnnMod
   * @param lEveMod
   * @return
   * @throws Exception
   */
  protected String eseguiRichiesta(AnnotazioneManualeModel lAnnMod, EventoModel lEveMod) throws Exception
  {
    String lPage = null;

    IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
    
    AnnotazioneManualeModel lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveMod);
      
    String dataScarcerazioneStr = "";
    if ( !isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE )) {
          Date dataScarcerazione = getRequestDateParameter( ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE, 
                                                            ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE, 
                                                            ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE);
          dataScarcerazioneStr = DateUtils.getDateToString(dataScarcerazione,"dd/MM/yyyy");
          dataScarcerazioneStr = "&dataScarcerazione="+dataScarcerazioneStr;
    }

    // Identificativo della Pena Residua
    BigDecimal lIdPenaResidua = null;
   	lIdPenaResidua = getRequestBigDecimalParameter("IdPenaResidua");

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.cumulo.action.ActLoadDettaglioApplicazioneBenefici&" + ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE +"=" + lAnnManIns.getIdAnnotazioneManuale()+"&lFlagRich="+dataScarcerazioneStr+"&IdPenaResidua="+lIdPenaResidua;

    return lPage;
  }

}