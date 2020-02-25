package siap.siep.cumulo.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciSanzioniSostitutive</p>
* <p>Description: Classe Action per la load dettaglio di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciSanzioniSostitutive extends ActionSiap implements ICostantiCumulo
{
  public String processRequest() throws F3BException
  {
    BigDecimal lFascID = null;
    String lStatoFasc  = null;
    String lFlagVal    = null;
    String lFlagCumulante = null;
    
    if(! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))
    {
      FascicoloSiepModel lFascMod = new FascicoloSiepModel();
      lFascID = new BigDecimal(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      lFascMod = lCtrlFasc.ExRicercaFascicoloByKey(lFascID);

      lStatoFasc = lFascMod.getCodStatoFascicolo();
      lFlagVal   = lFascMod.getFlagValidato();

      this.setSessionAttribute("fascicolo",lFascMod);
    }
    else
    {
      if (this.isSessionAttributeNullObj("fascicolo"))
      {
        return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();
      }

      lFascID=((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();
      lStatoFasc=((FascicoloSiepModel)getSessionAttribute("fascicolo")).getCodStatoFascicolo();
      lFlagVal = ((FascicoloSiepModel)getSessionAttribute("fascicolo")).getFlagValidato();
      lFlagCumulante = ((FascicoloSiepModel)getSessionAttribute("fascicolo")).getFlagCumulante();
    }
    
    if (lStatoFasc.equals("01"))
         throw new SIEPException( SIEPException.USER_MESSAGE, "Fascicolo Archiviato/Definito. Impossibile effettuare una operazione di cumulo" );
    if (lFlagVal.equals("N"))
         throw new SIEPException( SIEPException.USER_MESSAGE, "Fascicolo non Validato. Impossibile effettuare una operazione di cumulo" );
    if (!lFlagCumulante.equals("S"))
          throw new F3BException(F3BException.USER_MESSAGE,"Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo.");

    PosizioneGiuridicaModel  lPG=new PosizioneGiuridicaModel();
    IPosizioneGiuridica iPG=SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPG=iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);
    
    if (lPG==null)
    {
       throw new SIEPException( SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente" );
    }

    setRequestAttribute("PosizioneGiuridica",lPG);

    Option lOption = new Option( DecodificheManager.getInstance().getFlagErgastolo());
    setRequestAttribute("FlagErgastolo","" +lOption);

    IMagistratoCompetente IMag=SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagCo=IMag.ExRicercaMagistratoCompetenteByFascicolo(lFascID);

    setRequestAttribute("MagistratoCompetente",lMagCo);
    setSessionAttribute("cumulowiz","");


    return PG_LOAD_INSERIMENTO_PENA_COMPLESSIVA_CUMULO;
  }
}