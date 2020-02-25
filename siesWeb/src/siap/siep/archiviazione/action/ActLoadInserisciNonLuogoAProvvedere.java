package siap.siep.archiviazione.action;

/**
 * <p>Title: ActLoadInserisciNonLuogoAProvvedere</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciNonLuogoAProvvedere extends ActionSiap
                                                 implements ICostantiArchiviazione
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//Controllo Validazione Fascicolo
    // Paolo Cherubini 15/03/2011 aggiungo un controllo per annullare i procedimenti creati a fronte 
    // di una conversione di una istanza. Se l'utente si accorge di aver sbagliato a fare la conversione 
    // e la annulla sul registro istanza occorre archiviare il procedimento creato in classe I non ancora validato
    // Quindi consento ai procedimenti non validati diversi da registro istanza di essere comunque archiviati,
    // ma solo per motivo  "0353"  archiviazione per fascicolo iscritto per errore
    // vedi sotto ho inserito una setfilter
    
    int NumFasc = lFascMod.getChiaveProgr().intValue();
    // NumFasc > 90000 && NumFasc < 100000 è un registro istanza
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N") && (NumFasc > 90000 && NumFasc < 100000))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

//Controllo Fascicolo definito
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    this.isEventoNonValidato();
    
    if (lFascMod.getFlagValidato().equalsIgnoreCase("S")) //Paolo Cherubini 15/03/2011 leggi sopra
    {
	    
	/******************************* Posizione Giuridica **********************************/
	     IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	
	     PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
	
	     if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null)
	     {
	       RedirectTo lRedirigi = new RedirectTo();
	       lRedirigi.setPage(IWebConstants.PG_MAIN);
	       setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
	       lRedirigi.setAction( "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
	                            ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
	       setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	
	       return IWebConstants.PG_MESSAGE;
	     }
	
	     setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);
	
	/******************************* Pena Complessiva *****************************/
	    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
	    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
	
	    if (lPenComMod == null)
	      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");
	
	    String lFlagErgastolo = "N";
	    // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
	    if(  lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "" )
	    {
	      if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
	      {
	        lFlagErgastolo = "S";
	      }
	      else if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
	      {
	        lFlagErgastolo = "D";
	      }
	    }
	
	    setRequestAttribute("flagergastolo", lFlagErgastolo);
	/******************************* Fine Pena Complessiva ************************/
	
	/***********************************  Pena Residua  ***************************/
	    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
	
	    PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
	    if (lPenaResidua == null && lFascMod.getFlagValidato().equalsIgnoreCase("N")) //Paolo Cherubini 15/03/2011 leggi sopra
	    {
	      RedirectTo lRedirigi = new RedirectTo();
	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
	      lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
	                           ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
	      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	
	      return IWebConstants.PG_MESSAGE;
	    }
	
	    setRequestAttribute("penaresidua", lPenaResidua);
    }
    
/******************************************************************************/
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

    Option lOption = new Option(DecodificheManager.getInstance().getMotivoNLP());
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    	lOption.setFilter( new String[] {"0353"} ); //Paolo Cherubini 15/03/2011 leggi sopra
    setRequestAttribute("oggettodefinzione", "" + lOption);
/*
    String lCodComCas = lFascMod.getSoggetto().getCodComuneCasellario();
    ISedeGiudiziaria lCtrlSede = SIEPLookupRemote.getSedeGiudiziariaRemote();
    SedeGiudiziariaModel lSedGiuMod = lCtrlSede.ExRicercaSedeGiudiziariaByKey(lCodComCas);

    setRequestAttribute("codicecomunecasellario", "" + lSedGiuMod.getCodComune());
*/
    
//  Altra Autorità 
    Option lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutorita", "" + lOptionAut);

//Ufficio recupero crediti
    Option lOptionUffRecCrediti = new Option( DecodificheManager.getInstance().getTipoUfficio());
    lOptionUffRecCrediti.setFilter( new String[] {"-","DIB", "CAP"} );
    setRequestAttribute("uffrecrediti", "" + lOptionUffRecCrediti );   
    
    return PG_LOAD_INSERISCI_NON_LUOGO_A_PROVVEDERE;
  }
}
