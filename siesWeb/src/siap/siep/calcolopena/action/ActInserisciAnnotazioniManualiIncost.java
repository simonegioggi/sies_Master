package siap.siep.calcolopena.action;

/**
* <p>Title: ActInserisciAnnotazioniManualiIncost</p>
* <p>Description: Classe Action per l'inserimento di Decisione del GE di 
* Incostituzionalità</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciAnnotazioniManualiIncost extends ActionSiap
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

    //Controllo sulla Presenza Ordinanza
    boolean lOrdinanzaPresente = false;
    if (getRequestStringParameter("flagOrdinanza").equals("true"))
    {
      lOrdinanzaPresente = true;
    }

    BigDecimal IdReato = null;

    AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
    AnnotazioneManualeModel lAnnOrdMod = new AnnotazioneManualeModel();

    String lPage="";

    if(!lOrdinanzaPresente)
    {
      if (getRequestStringParameter("annoGe").equals(""))
      {
        lAnnOrdMod.setAnnoGe(new BigDecimal(0));
      }
      else
        lAnnOrdMod.setAnnoGe(getRequestBigDecimalParameter("annoGe"));

      lAnnOrdMod.setNumeroGe(getRequestStringParameter("numeroGe"));
    }

    lAnnMod.setFlagValidato("N");
    lAnnMod.setCodFonte("-");
    lAnnMod.setCodSottonumerazione("-");
    lAnnMod.setCodCausaleComputo("-");

/*
    lAnnMod.setCodFonte(getRequestStringParameter("CodFonte"));
    if (getRequestStringParameter("AnnoFonte").equals(""))
    {
      lAnnMod.setAnnoFonte(new BigDecimal(0));
    }
    else
      lAnnMod.setAnnoFonte(getRequestBigDecimalParameter("AnnoFonte"));

    lAnnMod.setNumeroFonte(getRequestStringParameter("NumeroFonte"));
    lAnnMod.setArticolo(getRequestStringParameter("Articolo"));
    lAnnMod.setCodSottonumerazione(getRequestStringParameter("CodSottonumerazione"));
    lAnnMod.setComma(getRequestStringParameter("Comma"));
    lAnnMod.setLettera(getRequestStringParameter("Lettera"));
    lAnnMod.setNumero(getRequestStringParameter("Numero"));
*/

    lAnnMod.setCodDpr("-");

    if (getRequestStringParameter("annoSCC").equals(""))
    {
      lAnnMod.setAnnoCc(new BigDecimal(0));
    }
    else
      lAnnMod.setAnnoCc(getRequestBigDecimalParameter("annoSCC"));
    lAnnMod.setNumeroCc(getRequestStringParameter("numeroSCC"));

    if (!getRequestStringParameter("ggScc").equals(""))
      lAnnMod.setDataCC(getRequestDateParameter("aaScc","mmScc","ggScc"));

    if(!lOrdinanzaPresente)
    {
      if (!getRequestStringParameter("DaAnArr").equals(""))
        lAnnOrdMod.setDataGE(getRequestDateParameter("DaAnArr", "DaMeArr", "DaGiArr"));
    }

    String PM=getRequestStringParameter("PM");
    lAnnMod.setFlagPiuMeno(PM);

    lAnnMod.setCodTipoAnnotazione("013"); //INCOSTITUZIONALITA'

    if (getRequestStringParameter("TipoOrd").equals("Conforme"))
      lAnnMod.setFlagConforme("C");
    else if(getRequestStringParameter("TipoOrd").equals("Difforme"))
      lAnnMod.setFlagConforme("D");
    else
      lAnnMod.setFlagConforme("-");

/*
    String GiRec=getRequestStringParameter("GiRec");
    String MiRec=getRequestStringParameter("MiRec");
    String AiRec=getRequestStringParameter("AiRec");
    String GfRec=getRequestStringParameter("GfRec");
    String MfRec=getRequestStringParameter("MfRec");
    String AfRec=getRequestStringParameter("AfRec");
*/
      String GRec=getRequestStringParameter("GRec");
      String MRec=getRequestStringParameter("MRec");
      String ARec=getRequestStringParameter("ARec");
      String Multa=getRequestStringParameter("Multa");
      String Multa_dec=getRequestStringParameter("Mul_dec");

      String noteRec=getRequestStringParameter("noteRec");
      /*if (GRec.equals("") && MRec.equals("") & ARec.equals(""))
      {
        CalendarModel lCal=new CalendarModel();
        CalendarUtil lCUt=new CalendarUtil();
        lCal.setDataInizio(DateUtils.getDate(AiRec,MiRec,GiRec));
        lCal.setDataFine(DateUtils.getDate(AfRec,MfRec,GfRec));
        lCal=lCUt.CalcolaNumGiorniMesiAnni(lCal);
      }

      lAnnMod.setDataDa(DateUtils.getDate(AiRec,MiRec,GiRec));
      lAnnMod.setDataA(DateUtils.getDate(AfRec,MfRec,GfRec));*/
      if (!ARec.equals(""))
        lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
      if (!MRec.equals(""))
        lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
      if (!GRec.equals(""))
        lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

      lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      if (!isRequestParameterNullObj("IdReato"))
      {
        IdReato = getRequestBigDecimalParameter("IdReato");
        lAnnMod.setReaIdReato(IdReato);
      }

      if (!Multa.equals(""))
      {
        if (!Multa_dec.equals(""))
        {
          lAnnMod.setImportoMulta(new BigDecimal(Multa+"."+Multa_dec));
        }
        else
          lAnnMod.setImportoMulta(new BigDecimal(Multa));
      }
      else if  (!Multa_dec.equals(""))
        lAnnMod.setImportoMulta(new BigDecimal("0."+Multa_dec));

      lAnnMod.setNoteReclusione(noteRec);

      lAnnMod.setFlagAppProvvisoria("-");

/*
      String GiArr=getRequestStringParameter("GiArr");
      String MiArr=getRequestStringParameter("MiArr");
      String AiArr=getRequestStringParameter("AiArr");
      String GfArr=getRequestStringParameter("GfArr");
      String MfArr=getRequestStringParameter("MfArr");
      String AfArr=getRequestStringParameter("AfArr");
*/
      String GArr=getRequestStringParameter("GArr");
      String MArr=getRequestStringParameter("MArr");
      String AArr=getRequestStringParameter("AArr");
      String Ammenda=getRequestStringParameter("Ammenda");
      String Ammenda_dec=getRequestStringParameter("Amm_dec");

      //String noteArr=getRequestStringParameter("noteArr");
      /*if (GArr.equals("") && MArr.equals("") & AArr.equals(""))
      {
        CalendarModel lCal=new CalendarModel();
        CalendarUtil lCUt=new CalendarUtil();
        lCal.setDataInizio(DateUtils.getDate(AiArr,MiArr,GiArr));
        lCal.setDataFine(DateUtils.getDate(AfArr,MfArr,GfArr));
        lCal=lCUt.CalcolaNumGiorniMesiAnni(lCal);
      }
      lAnnMod.setFlagPiuMeno(PMArr);
      lAnnMod.setDataDa(DateUtils.getDate(AiArr,MiArr,GiArr));
      lAnnMod.setDataA(DateUtils.getDate(AfArr,MfArr,GfArr));*/
      if (!AArr.equals(""))
        lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
      if (!MArr.equals(""))
        lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
      if (!GArr.equals(""))
        lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

/*
      lAnnMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
      if (!isRequestParameterNullObj("IdReato"))
      {
        IdReato = getRequestBigDecimalParameter("IdReato");
        lAnnMod.setReaIdReato(IdReato);
      }
*/
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

    lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lAnnMod.setDataInserimento(DateUtils.getSysDate());

    // ***********************************
    String lCodTipoUfficioEmittente = "-";
    String lDescrLuogoEmittente = "-";
    String lCodUffEmi = "-";

    if(!lOrdinanzaPresente)
    {
      lAnnOrdMod.setIdAnnotazioneManuale(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE));

      lAnnOrdMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      lAnnOrdMod.setMotivazioni(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI));
      lAnnOrdMod.setFlagValidato("S");
      lAnnOrdMod.setFlagConforme("-");
      lAnnOrdMod.setCodTipoAnnotazione("-");
      lAnnOrdMod.setCodDpr("-");
      lAnnOrdMod.setCodFonte("-");
      lAnnOrdMod.setCodSottonumerazione("-");
      lAnnOrdMod.setCodCausaleComputo("-");
      lAnnOrdMod.setCodOperatoreInserimento(getCodUtenteConnesso());
      lAnnOrdMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lAnnOrdMod.setDataInserimento(DateUtils.getSysDate());

      lCodTipoUfficioEmittente = getRequestStringParameter("CodTipoUffEmi");
      lDescrLuogoEmittente = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);

      if(  !lCodTipoUfficioEmittente.equals("-")
        && !lCodTipoUfficioEmittente.equals("")
        && !lDescrLuogoEmittente.equals("")
        && !lDescrLuogoEmittente.equals("-")
        )
      {
        lCodUffEmi = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmittente, lDescrLuogoEmittente);
      }
    }

    boolean lInserireOrdinanza = false;
    if( lAnnOrdMod.getDataGE() != null
        && lAnnOrdMod.getAnnoGe() != null
        && lAnnOrdMod.getNumeroGe() != null
        && lAnnOrdMod.getNumeroGe() != ""
      )
    {
      lInserireOrdinanza = true;
    }

 /********************* Evento Ordinanza **************************************/
   EventoModel lEveOrdinanzaMod = new EventoModel();

   lEveOrdinanzaMod.setCodTipoEvento("01");
   lEveOrdinanzaMod.setCodTipoProvvedimento("03"); // ORDINANZA
   lEveOrdinanzaMod.setCodMotivo("0286");          // INCOSTITUZIONALITA'
   // --- Come devono essere gestiti i documenti di altri uffici simulati?
   //lEveOrdinanzaMod.setFlagDocumentoRegistrato("S");
   // --- il flag come deve essere gestito?
   lEveOrdinanzaMod.setFlagStampaSiep("S");
   lEveOrdinanzaMod.setFlagVideoSiep("S");
   lEveOrdinanzaMod.setCodUfficioEmittente( lCodUffEmi );
   lEveOrdinanzaMod.setCodLuogoEmittente( getCodComuneByDescr(lDescrLuogoEmittente).getCodComune() );
   lEveOrdinanzaMod.setFasSieIdFascicoloSiep(lIdFascicolo);
   lEveOrdinanzaMod.setDataEmissione(lAnnOrdMod.getDataGE());

 /********************* Fine Evento Ordinanza *********************************/

 /********************* Evento Provvedmento ***********************************/
   EventoModel lEveProvvedimentoMod = new EventoModel();

   lEveProvvedimentoMod.setCodTipoEvento("01");
   lEveProvvedimentoMod.setCodTipoProvvedimento("04"); // PROVVEDIMENTO
   lEveProvvedimentoMod.setCodMotivo("0286");          // INCOSTITUZIONALITA'
   //---NO ---lEveProvvedimentoMod.setFlagDocumentoRegistrato("N");
   lEveProvvedimentoMod.setFlagStampaSiep("S");
   lEveProvvedimentoMod.setFlagVideoSiep("S");
   lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
   lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
   lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);
   lEveProvvedimentoMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("yyyy"), DateUtils.getSysDate("MM"), DateUtils.getSysDate("dd")));

   /********************* Fine Evento Ordinanza *********************************/

    IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();

    if (getRequestStringParameter("operazione").equals("Torna"))
    {
      lAnnManCtrl.ExInserisciAnnotazioneManuale(lAnnMod);

      lPage=IWebConstants.ROOT_DIR+"Main.jsp?Action=siap.siep.calcolopena.action.ActLoadGEIncost";

      return lPage;
    }


    if (getRequestStringParameter("operazione").equals("Quantum"))
    {
//DA QUI COPIA E INCOLLA (e modifiche) della LOADCALCOLOPENA
      //******************************************************************************
      AnnotazioneManualeModel lAnnManIns = null;

      if(!lOrdinanzaPresente && lInserireOrdinanza)
      {
        lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeProvvedimentoRichiesta(lAnnMod, lEveOrdinanzaMod, lEveProvvedimentoMod, lAnnOrdMod, null);
      }
      else
      {
        lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveProvvedimentoMod);
      }
//******************************************************************************

      setRequestAttribute("lFlagPage", "INCOST");

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniManuali&"+ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE+"="+lAnnManIns.getIdAnnotazioneManuale();

      //lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadAnnotazioniManualiBenefici.jsp";
      return lPage;
    }

    return null;
  }
}