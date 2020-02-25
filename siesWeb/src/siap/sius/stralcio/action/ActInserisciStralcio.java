package siap.sius.stralcio.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.stralcio.controller.IStralcio;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActInserisciStralcio</p>
 * <p>Description: Classe Action di inserimento Stralcio di Oggetti Procedimento SIUS
 * </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull S.p.A.</p>
 */

public class ActInserisciStralcio extends ActionSiap implements ICostantiStralcio
{
  public String processRequest() throws Exception
  {
    super.processRequest();

    //Recupero l'utente dalla sessione
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    // Recupero Fascicolo SIUS in sessione.
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFasSiusDaStralciare = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    // Recupero della data di Stralcio digitata e del numero eventuale del fascicolo destinazione di stralcio.
    Date dataStralcio = getRequestDateParameter(CAMPO_ANNO_DATA_STRALCIO, CAMPO_MESE_DATA_STRALCIO, CAMPO_GIORNO_DATA_STRALCIO);

    // Recupero della selezione degli Oggetti di Stralcio.
    String[] lArrayCheckBox = new String[lFasGPMod.getTenori().length];

    for (int i=0; i<lArrayCheckBox.length; i++)
    {
      if(!(isRequestParameterNullObj(CAMPO_CHECKBOX+i)))
        lArrayCheckBox[i]="S";
      else
        lArrayCheckBox[i]="N";
    }

    // Recupero del tipo di Stralcio ( nuovoProcedimento /  vecchioProcedimento).
    String lTipoStralcio = getRequestStringParameter(CAMPO_TIPO_STRALCIO);
    String annoProcedimentoStralcio = "";
    String progrProcedimentoStralcio = "";
    Integer numOggettiIniziali = new Integer(0);

    //Caricamento Fascicolo Destinazione di Stralcio.
    IStralcio lCtrl = SIUSLookupRemote.getStralcioRemote();
    FascicoloGPModel lFasDestStralcio = null;
    if (lTipoStralcio.compareTo("vecchioProcedimento")==0)
    {
      annoProcedimentoStralcio = getRequestStringParameter(CAMPO_CHIAVE_ANNO);
      progrProcedimentoStralcio = getRequestStringParameter(CAMPO_CHIAVE_PROGR);
      lFasDestStralcio = lCtrl.ExVerificaFascicoloDestStralcio( lFasGPMod.getFascicoloSiusModel().getSoggetto().getIdSoggetto(), annoProcedimentoStralcio, progrProcedimentoStralcio, lUtenteMod.getUfficioUtente().getCodUfficio() );
      numOggettiIniziali = new Integer(lFasDestStralcio.getTenori().length);

      // Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio già emesso per il Fascicolo Destinazione stralcio.
      // Se esiste almeno un provvedimento di questo tipo non si può procedere allo stralcio.
      RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasDestStralcio.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
      boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
      if (lEsistenzaDoc)
      {
         throw new SIUSException(SIUSException.USER_MESSAGE, "Per il procedimento Destinazione stralcio è già stato emesso un provvedimento. Stralcio impossibile.");
      }

    }else{
      lFasDestStralcio = new FascicoloGPModel();
      //Caricamento Fascicolo SIUS.
      // 10/09/2007 si conserva l'anno e progressivo del procedimento da stralciare.
      annoProcedimentoStralcio = lFasGPMod.getFascicoloSiusModel().getChiaveAnno().toString();
      progrProcedimentoStralcio = lFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString();
      lFasDestStralcio.setFascicoloSiusModel( lFasGPMod.getFascicoloSiusModel());
      lFasDestStralcio.getFascicoloSiusModel().setIdFascicoloSius(null);
      lFasDestStralcio.getFascicoloSiusModel().setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
      lFasDestStralcio.getFascicoloSiusModel().setChiaveUfficio( lUtenteMod.getUfficioUtente().getCodUfficio());
      //Il progressivo PROGR viene calcolato applicativamente nel controller.
      lFasDestStralcio.getFascicoloSiusModel().setCodStatoFascicolo("02");
      lFasDestStralcio.getFascicoloSiusModel().setDataIscrizione( DateUtils.getSysDate() );
      lFasDestStralcio.getFascicoloSiusModel().setDataInserimento( DateUtils.getSysDate() );
      lFasDestStralcio.getFascicoloSiusModel().setCodOperatoreInserimento (lUtenteMod.getUserId());
      lFasDestStralcio.getFascicoloSiusModel().setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
      lFasDestStralcio.getFascicoloSiusModel().setDataAggiornamento( null );
      lFasDestStralcio.getFascicoloSiusModel().setCodOperatoreAggiornamento ("");
      lFasDestStralcio.getFascicoloSiusModel().setCodUfficioAggiornamento("");
      lFasDestStralcio.getFascicoloSiusModel().setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
      lFasDestStralcio.getFascicoloSiusModel().setIdFascicoloSiusOrigine(null);
      lFasDestStralcio.getFascicoloSiusModel().setDataDefinizione(null);
      lFasDestStralcio.getFascicoloSiusModel().setFasSieIdFascicoloSiep(null);
      lFasDestStralcio.getFascicoloSiusModel().setNumeroFascicoliUnificati(new BigDecimal("0"));
      lFasDestStralcio.getFascicoloSiusModel().setChiaveAnnoSIEP(null);
      lFasDestStralcio.getFascicoloSiusModel().setChiaveProgrSIEP(null);
      lFasDestStralcio.getFascicoloSiusModel().setChiaveUfficioSIEP(null);
      //Caricamento Generale Procedimento.
      lFasDestStralcio.setGeneraleProcedimentoModel( lFasGPMod.getGeneraleProcedimentoModel());
      lFasDestStralcio.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(null);
      lFasDestStralcio.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy")));
      //Il progressivo S1 viene calcolato applicativamente nel controller.
      lFasDestStralcio.getGeneraleProcedimentoModel().setDataRichiesta( dataStralcio );
      lFasDestStralcio.getGeneraleProcedimentoModel().setDataArrivoCancelleria( dataStralcio );
      lFasDestStralcio.getGeneraleProcedimentoModel().setCodTipoFoglioComplementare( "-" );
      lFasDestStralcio.getGeneraleProcedimentoModel().setCodTipoAtto( "08" );
      lFasDestStralcio.getGeneraleProcedimentoModel().setCodSedeMittente(lFasGPMod.getGeneraleProcedimentoModel().getCodSedeMittente()) ;
      if (lUtenteMod.getUfficioUtente().getCodTipoUfficio().compareTo("TDS")==0)
        lFasDestStralcio.getGeneraleProcedimentoModel().setCodTipoMittenteAtto("30") ;
      if (lUtenteMod.getUfficioUtente().getCodTipoUfficio().compareTo("UDS")==0)
        lFasDestStralcio.getGeneraleProcedimentoModel().setCodTipoMittenteAtto("05") ;
      lFasDestStralcio.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(null);// Da valorizzare nel Controller.
      lFasDestStralcio.getGeneraleProcedimentoModel().setAnnotazione("");
      lFasDestStralcio.getGeneraleProcedimentoModel().setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio()); //Codice dell'operatore che inserisce
      lFasDestStralcio.getGeneraleProcedimentoModel().setCodOperatoreInserimento (lUtenteMod.getUserId()); //Codice dell'operatore che inserisce
      lFasDestStralcio.getGeneraleProcedimentoModel().setDataInserimento( DateUtils.getSysDate() );
      lFasDestStralcio.getGeneraleProcedimentoModel().setUdiIdUdienza( null );
      lFasDestStralcio.getGeneraleProcedimentoModel().setDescrMittente( annoProcedimentoStralcio+ " / "+progrProcedimentoStralcio );

      lFasDestStralcio.getGeneraleProcedimentoModel().setDataDefinizione(null);
      lFasDestStralcio.getGeneraleProcedimentoModel().setDataCameraConsiglio(null);
    }

    // Raccolta Codici Oggetti gia presenti nel fascicolo Destinazione Stralcio.
    String lCodOggettiPresenti = "";
    int lTenoriDestinazione = 0;
    // Raccolta Codici Oggetti gia presenti nel fascicolo Destinazione Stralcio.
    if (lTipoStralcio.compareTo("vecchioProcedimento")==0)
    {
      lTenoriDestinazione = lFasDestStralcio.getTenori().length;
      for (int z=0; z< lTenoriDestinazione; z++)
        lCodOggettiPresenti += lFasDestStralcio.getTenori()[z].getCodOggettoTenore();
    }

    //Conteggio N.ro dei nuovi Tenori per gli Oggetti stralciati.
    int lTenoriAggiuntivi = 0;
    for(int k=0; k<lArrayCheckBox.length; k++)
    {
      if ( (lArrayCheckBox[k].compareTo("S")==0) && (lCodOggettiPresenti.indexOf(lFasGPMod.getTenori()[k].getCodOggettoTenore()) < 0 ) )
        lTenoriAggiuntivi++;
    }

    //Caricamento dei Tenori per gli Oggetti stralciati.
    TenoreModel lTenore = new TenoreModel();
    TenoreModel[] lTenDest = new TenoreModel[(lTenoriDestinazione+lTenoriAggiuntivi)];
    int lIndice = lTenoriDestinazione;
    for(int k=0; k<lArrayCheckBox.length; k++)
    {
      if (lArrayCheckBox[k].compareTo("S")==0)
      {
        lTenore=lFasGPMod.getTenori()[k];
        if (lCodOggettiPresenti.indexOf(lTenore.getCodOggettoTenore())<0)
        {
          lTenore.setCodOggettoTenore(lFasGPMod.getTenori()[k].getCodOggettoTenore());
          lTenore.setCodEsitoTenore("-");
          lTenore.setCodMagistrato(" ");
          lTenore.setData(dataStralcio);
          lTenore.setDataFine(null);
          lTenore.setProgrTenore(new BigDecimal(lIndice+1));
          lTenore.setCodOperatoreInserimento(lUtenteMod.getUserId());
          lTenore.setDataInserimento(DateUtils.getSysDate());
          lTenore.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
          //lTenore.setNote("Stralcio dal "+ lFasGPMod.getFascicoloSiusModel().getChiaveAnno().toString()+" / "+lFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString() +" (proc. stralciato)");
          lTenore.setGenPridGeneraleProcedimento(null);// Da valorizzare nel Controller.
          if (lTipoStralcio.compareTo("vecchioProcedimento")==0)
          {
            lTenore.setGenPridGeneraleProcedimento(lFasDestStralcio.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
          }
          lTenDest[lIndice]=lTenore;
          lIndice++;
        }
      }
    }
    // Caricamento Tenori nel Fascicolo Destinazione Stralcio.
    lFasDestStralcio.setTenori(lTenDest);

    // Caricamento dell'EVENTO.
    EventoModel lEveMod = new EventoModel();
    lEveMod.setCodTipoEvento("01");
    lEveMod.setCodTipoProvvedimento("48"); // Stralcio Oggetti.
    //lEveMod.setCodMotivo(lFasDestStralcio.getTenori()[numOggettiIniziali.intValue()].getCodOggettoTenore());
    lEveMod.setCodMotivo(lFasGPMod.getTenori()[0].getCodOggettoTenore());
    lEveMod.setCodUfficioEmittente(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEveMod.setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
    lEveMod.setCodLuogoDestinatario("-"); //??
    lEveMod.setCodEsito("0604");
    lEveMod.setDataEmissione(dataStralcio);
    //lEveMod.setTenIdTenore(lFasDestStralcio.getTenori()[numOggettiIniziali.intValue()].getIdTenore());
    lEveMod.setTenIdTenore(lFasGPMod.getTenori()[0].getIdTenore());
    lEveMod.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    lEveMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    lEveMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEveMod.setDataInserimento(DateUtils.getSysDate());
    lEveMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodTipoUfficioDestinatario("-");
    // FasSiuIdFascicoloSiusDest da impostare nel controller.
    // lEveMod.setFasSiuIdFascicoloSiusDest(lFasDestStralcio.getFascicoloSiusModel().getIdFascicoloSius());

    // Stralcio degli oggetti selezionati.
    EventoModel lEveStralcio = lCtrl.ExInserisciStralcio(lIdFasSiusDaStralciare, lFasDestStralcio, dataStralcio, lArrayCheckBox, numOggettiIniziali, lUtenteMod.getUfficioUtente().getCodUfficio(), lEveMod );

    //restituisce la jsp di VIEW.
    // Nella request viaggia l'ID dell'Evento di Stralcio appena inserito.
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.stralcio.action.ActLoadDettaglioStralcio&"+CAMPO_ID_EVENTO_STRALCIO+"="+lEveStralcio.getIdEvento().toString()+"&FlagIns=Y";

  }
}
