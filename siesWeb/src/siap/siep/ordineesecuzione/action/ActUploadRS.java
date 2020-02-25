package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadRS</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadRS extends ActionSiap implements ICostantiEvento
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoModel lModel = new EventoModel();
    lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
    }

    lModel.setDataAggiornamento( DateUtils.getSysDate());

    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );

    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lModel.setFlagDocumentoRegistrato("S");

      IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
      lCtrl.ExUpdateValidaRS(lModel, lFascMod);
      
      //inizio aggiorna codice maschera nella tabella posizione giuridica
	  //PosizioneGiuridicaDAO lPosDao = new PosizioneGiuridicaDAO(lConn);	      
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
      lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
      String codPosCorrente="";
      if(lPos!=null && lPos.getPosizioneGiuridica()!=null && lPos.getPosizioneGiuridica().getCodPosizioneGiuridica()!=null){
    	  codPosCorrente = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
      }
      if(codPosCorrente!=null && !codPosCorrente.equalsIgnoreCase("")){   
    	  if (codPosCorrente.equals("07") && lPos.getAltraCausa()!=null && 
	    		  (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78") || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79") || 
	    				  lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80") ||
	    				  lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81")))
	      {
	    	  lPos.getPosizioneGiuridica().setCodMaschera("L3");
	    	  //lPos.getPosizioneGiuridica().setAltCauIdAltraCausa(null);
	      }  else if (codPosCorrente.equals("07") && lPos.getAltraCausa()!=null && 
	    		  (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76") || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77") ))
	      {
	    	  lPos.getPosizioneGiuridica().setCodMaschera("L2");
	    	  //lPos.getPosizioneGiuridica().setAltCauIdAltraCausa(null);
	      }		    	  
    	  else if (codPosCorrente.equals("07") || codPosCorrente.equals("10"))
	      {
	    	  lPos.getPosizioneGiuridica().setCodMaschera("L");
	    	  lPos.getPosizioneGiuridica().setAltCauIdAltraCausa(null);
	      }
	      if (codPosCorrente.equals("02") || codPosCorrente.equals("70") || codPosCorrente.equals("71") || codPosCorrente.equals("72"))
	      {
	    	  lPos.getPosizioneGiuridica().setCodMaschera("EA");
	    	  lPos.getPosizioneGiuridica().setAltCauIdAltraCausa(null);
	      }
	      lPos.getPosizioneGiuridica().setIdPosizioneGiuridica(lPos.getPosizioneGiuridica().getIdPosizioneGiuridica());
	      
	      lPosCtrl.ExModificaPosizioneGiuridicaIdPosGiu(lPos.getPosizioneGiuridica());
      }
      
      //aggiorno tabella FASCICOLO_SIEP
      IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();		     
      if(codPosCorrente!=null && codPosCorrente.equalsIgnoreCase("07") && lPos.getAltraCausa()!=null && lPos.getAltraCausa().getCodTipoPosGiuridica()!=null &&
    	  (lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("76") || lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("77") ||  
    	  lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("78") || lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("79") || 
    	  lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("80") || lPos.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("81")) ) {
          //inserisco la nuova posizione giuridica è perfettamente uguale alla precedente, serve per mantenere lo storico di diverse posizioni giuridiche 
          //anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze diverse ==> cambia solo il periodo)
    	  lFascMod.setFlagAltraCausa("S");
      } else {
    	  lFascMod.setFlagAltraCausa(null);
      }
      
      lFasCtrl.ExModificaFascicoloSiep(lFascMod);		    
      //fine aggiorna codice maschera nella tabella posizione giuridica e il FLAG_ALTRA_CAUSA=null
    }
    else
    {
      lModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lModel);
    }

  	//Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

    String titolo = null;
    if(!isRequestParameterNullObj("titolo"))
      titolo = getRequestStringParameter("titolo");

    String sedetribunale = null;
    if(!isRequestParameterNullObj("sedetribunale"))
      sedetribunale = getRequestStringParameter("sedetribunale");


		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)+ "&fc=" +getRequestStringParameter("fc")+ "&titolo=" +titolo+ "&sedetribunale=" +sedetribunale);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

  	return IWebConstants.PG_MESSAGE;
  }
}
