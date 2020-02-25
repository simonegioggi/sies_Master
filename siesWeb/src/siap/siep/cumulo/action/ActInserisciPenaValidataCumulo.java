package siap.siep.cumulo.action;


/**
* <p>Title: ActInserisciCumulo</p>
* <p>Description: Classe Action per l'inserimento di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActInserisciPenaValidataCumulo extends ActionSiap implements ICostantiCumulo
{
/**
* Azione di Aggiornamento del record pena residua associato al cumulo
* Viene aggiornata la sola data fine pena (fine pena manuale)
* @return Nome della pagina JSP ddi visualizzazione del dettaglio pena
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
    IPenaResidua iPen=SIEPLookupRemote.getPenaResiduaRemote();
    BigDecimal lFascID=((FascicoloSiepModel)(getSessionAttribute("fascicolo"))).getIdFascicoloSiep();
    
    
    
    PenaResiduaModel lPenRes = iPen.ExRicercaPenaResiduaByKey(getRequestBigDecimalParameter("IdPenaResidua"));

    //lPenRes.setDataInizio(getRequestDateParameter("Adatainiziopena","Mdatainiziopena","Gdatainiziopena"));
   // lPenRes.setDataFinePresunta(getRequestDateParameter("Adatafinepenapresunta","Mdatafinepenapresunta","Gdatafinepenapresunta"));
    lPenRes.setDataFine(getRequestDateParameter("APV","MPV","GPV"));

    /*if (isRequestAttributeNullObj("Gdatafinereclusione"))
    {
      lPenRes.setDataInizioArresto(null);
      lPenRes.setDataFineReclusione(null);
       setRequestAttribute("DateIntermedie", "no");
    }else
    {
      lPenRes.setDataInizioArresto(getRequestDateParameter("Adatainizioarresto","Mdatainizioarresto","Gdatainizioarresto"));
      lPenRes.setDataFineReclusione(getRequestDateParameter("Adatafinereclusione","Mdatafinereclusione","Gdatafinereclusione"));
      setRequestAttribute("DateIntermedie", "si");
    }
    lPenRes.setFlagValidato("N");
    lPenRes.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lPenRes.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lPenRes.setDataAggiornamento(DateUtils.getSysDate());
    iPen.ExModificaPenaResidua(lPenRes);*/
    
    iPen.ExModificaPenaResidua(lPenRes);

    setRequestAttribute("MustConfirm","no");
    setRequestAttribute("DataFinePenaManuale",lPenRes.getDataFine());

    PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
    IPosizioneGiuridica iPG=SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPG=iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);

    setRequestAttribute("PosizioneGiuridica",lPG);
    setRequestAttribute("DataInizioPena", lPenRes.getDataInizio());
    setRequestAttribute("DataFinePena", lPenRes.getDataFine());
    setRequestAttribute("DataFineReclusione", lPenRes.getDataFineReclusione());
    setRequestAttribute("DataInizioArresto", lPenRes.getDataInizioArresto());
    setRequestAttribute("PenaResidua", lPenRes);

    //==========================================================================
    // Recupero anche 
    //==========================================================================
    if ( !isRequestParameterNullObj("IdPenaCumulo") ){
      BigDecimal lIdPenaCumulo = getRequestBigDecimalParameter("IdPenaCumulo");
      PenaCumuloModel lPenCumMod = new PenaCumuloModel();
      IPenaCumulo lCtrlPena = SIEPLookupRemote.getPenaCumuloRemote();
      lPenCumMod = lCtrlPena.ExRicercaPenaCumuloByKey(lIdPenaCumulo);
      setRequestAttribute("PenaCumuloMod",lPenCumMod);
    }
    
    
    return PG_DETTAGLIOPENACUMULO;
  }



}