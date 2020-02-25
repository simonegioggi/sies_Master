package siap.siep.misuraalternativa.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>Title: ActSospensioneProvvisoria</p>
 * <p>Description: Classe Action Padre per le Sospensioni Provvisorie</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActSospensioneProvvisoria extends ActMisuraAlternativa implements ICostantiMisuraAlternativa
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Metodo utilizzato dall classi di LoadInserisci e che effettua i controlli
   * preliminari e il caricamento dei dati sulla request
   *
   * @return
   * @throws F3BException
   */
  protected String getSospensioni() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    isFascicoloSiepDiCompetenza();

    if (isFascicoloArchiviatoDefinito())
      return IWebConstants.PG_MESSAGE;

//Posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("posizioneluogoaltra", lPos);

//Controllo Esistenza pena residua per quel fascicolo
    PenaResiduaModel lPenaResMod = new PenaResiduaModel();
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
       return IWebConstants.PG_MESSAGE;

    if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
      setRequestAttribute("dataeditabile", "S");

    setRequestAttribute("penaresidua", lPenaResMod);

//ricerca magistrato competente
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

//Autorità esterna E
    Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
    setRequestAttribute("autoritaEsternaAvv", "" + lOption);

    UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getDescrComune();

    setRequestAttribute("distretto", StrCodiceDistrettoUtente);

    return "";
  }

  /**
   * Metodo utilizzato dalla classe di Inserimento e che imposta il codice
   * motivo provvedimento e il motivo in funzione dei parametri in input.
   *
   * @param codiceMotivo
   * @param tipoMisura
   * @param lProcSorv
   * @return
   * @throws F3BException
   */
protected EventoNotificaModel getCodiceMotivoTipoEventoSospensioni(String codiceMotivo,String tipoMisura,String lProcSorv) throws F3BException
 {
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.info("----------------- tipoMisura = "+ tipoMisura +"---------------------");
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.info("----------------- tipoMisura = "+ tipoMisura +"---------------------");
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.info("----------------- tipoMisura = "+ tipoMisura +"---------------------");
   EventoNotificaModel lEve = new EventoNotificaModel();
   if(  tipoMisura.equals("AFFIDAMENTO") || tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA") )
   {
     if( lProcSorv.equals("procura") )
     {
       lEve.getEvento().setCodTipoProvvedimento("06");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
     else if ( lProcSorv.equals("mds") )
     {
       lEve.getEvento().setCodTipoProvvedimento("12");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals("AFFIDAMENTO51BIS"))
   {
     if( codiceMotivo.equals("2205"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0388");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0388");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2282"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0389");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0389");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2281"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0390");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0390");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals("SEMILIBERTA51BIS"))
   {
     if( lProcSorv.equals("procura") )
     {
       lEve.getEvento().setCodTipoProvvedimento("24");
       lEve.getEvento().setCodMotivo("0383");
     }
     else if ( lProcSorv.equals("mds") )
     {
       lEve.getEvento().setCodTipoProvvedimento("12");
       lEve.getEvento().setCodMotivo("0383");
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals("DETENZIONE51BIS"))
   {
     if( codiceMotivo.equals("2286"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0384");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0384");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2285"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0385");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0385");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2288"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0386");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0386");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2284"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         lEve.getEvento().setCodMotivo("0387");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         lEve.getEvento().setCodMotivo("0387");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else if( codiceMotivo.equals("2287"))
     {
       if( lProcSorv.equals("procura") )
       {
         lEve.getEvento().setCodTipoProvvedimento("24");
         // 28/10/2010 lEve.getEvento().setCodMotivo("0380");
         lEve.getEvento().setCodMotivo("0387");
       }
       else if ( lProcSorv.equals("mds") )
       {
         lEve.getEvento().setCodTipoProvvedimento("12");
         // 28/10/2010 lEve.getEvento().setCodMotivo("0380");
         lEve.getEvento().setCodMotivo("0387");
       }
       else
       {
         lEve.getEvento().setCodTipoProvvedimento("04");
         lEve.getEvento().setCodMotivo(codiceMotivo);
       }
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals("INDULTINO51BIS"))
   {
     if(lProcSorv.equals("procura") )
      {
        lEve.getEvento().setCodTipoProvvedimento("24");
        lEve.getEvento().setCodMotivo("0456");
      }
      else if (lProcSorv.equals("mds") )
      {
        lEve.getEvento().setCodTipoProvvedimento("12");
        lEve.getEvento().setCodMotivo("0455");
      }
      else
      {
        lEve.getEvento().setCodTipoProvvedimento("04");
        lEve.getEvento().setCodMotivo(codiceMotivo);
      }
   }
   else if(tipoMisura.equals("INDULTINO"))
   {
     if(lProcSorv.equals("procura"))
     {
       lEve.getEvento().setCodTipoProvvedimento("06");
       lEve.getEvento().setCodMotivo("0457");
     }
     else if (lProcSorv.equals("mds") )
     {
       lEve.getEvento().setCodTipoProvvedimento("12");
       lEve.getEvento().setCodMotivo("0457");
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
   {
     if(lProcSorv.equals("procura"))
     {
       lEve.getEvento().setCodTipoProvvedimento("06");
       lEve.getEvento().setCodMotivo("0471");
     }
     else if (lProcSorv.equals("mds") )
     {
       lEve.getEvento().setCodTipoProvvedimento("12");
       lEve.getEvento().setCodMotivo("0471");
     }
     else
     {
       lEve.getEvento().setCodTipoProvvedimento("04");
       lEve.getEvento().setCodMotivo(codiceMotivo);
     }
   }
   else if(tipoMisura.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
   {
  	 if(lProcSorv.equals("procura") )
      {
        lEve.getEvento().setCodTipoProvvedimento("24");
        lEve.getEvento().setCodMotivo("0472");
      }
      else if (lProcSorv.equals("mds") )
      {
        lEve.getEvento().setCodTipoProvvedimento("12");
        lEve.getEvento().setCodMotivo("0472");
      }
      else
      {
        lEve.getEvento().setCodTipoProvvedimento("04");
        lEve.getEvento().setCodMotivo(codiceMotivo);
      }
   }
   else
   {
     lEve.getEvento().setCodTipoProvvedimento("04");
     lEve.getEvento().setCodMotivo(codiceMotivo);
   }
  return lEve;
 }

}