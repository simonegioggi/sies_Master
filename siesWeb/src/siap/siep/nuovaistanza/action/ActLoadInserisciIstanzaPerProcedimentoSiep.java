package siap.siep.nuovaistanza.action;

/**
* <p>Title: ActRicercaFascicolo</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile s.r.l.</p>
* @author Dario Babarini
* @version 5.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciIstanzaPerProcedimentoSiep extends ActionSiap implements ICostantiNuovaIstanza
{
 public String processRequest() throws Exception
 {
      //Istanzio il Model
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();

      BigDecimal lChiaveProgr = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);
      BigDecimal lChiaveAnno = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);

      if (lChiaveAnno.intValue()>= 90000 &&
      		lChiaveAnno.intValue()<= 99999)
        throw new SIEPException(F3BException.USER_MESSAGE,"Iscrizione Istanza non prevista per questo Numero");

      lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
      lFasMod.setChiaveProgr(lChiaveProgr);
      lFasMod.setChiaveAnno(lChiaveAnno);

      IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      if (lFasRet == null)
      {
  			// setta la risposta nella request
  			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun Fascicolo con Anno "+ lChiaveAnno+" e Progressivo " +lChiaveProgr+".Fare Iscrizione Istanza Completa.");

  	    //Prepara la "pagina" di destinAction
  	    RedirectTo lRedirigi = new RedirectTo();
  	    lRedirigi.setPage( IWebConstants.PG_MAIN );
  	    lRedirigi.setAction( "siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerTitoloEsecutivo" );

  	    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

  			return IWebConstants.PG_MESSAGE;
      }
      setSessionAttribute("fascicolo",lFasRet);
      setSessionAttribute("soggetto",lFasRet.getSoggetto());
      setSessionAttribute("sentenza",lFasRet.getSentenza());
      setRequestAttribute("idfascicolo",""+ lFasRet.getIdFascicoloSiep());
      
      //19/04/2010 Autorità Mittente Nuova Istanza
	  	//Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita());
    	Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaMittenteIstanza());
      setRequestAttribute("autorita", "" + lOption );
      
      Option lOptionTA = new Option(DecodificheManager.getInstance().getTipoAvvocato());
      setRequestAttribute("tipoAvvocato", "" + lOptionTA);

      Option lOptionCI = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza());
      setRequestAttribute("contenuto", "" + lOptionCI);   

      return PG_LOAD_INSERISCI_PROCEDIMENTO_ISTANZA;
  }
}