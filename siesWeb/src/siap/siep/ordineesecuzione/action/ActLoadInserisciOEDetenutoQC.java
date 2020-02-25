package siap.siep.ordineesecuzione.action;


/**
* <p>Title: ActLoadInserisciEvento</p>
* <p>Description: Classe Action per la load inserisci di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciOEDetenutoQC extends ActionSiap implements ICostantiOrdineEsecuzione
{
	 public String processRequest() throws F3BException
		{

     if( isSessionAttributeNullObj("fascicolo") )
      {
        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
                   "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                   ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
                   "siap.siep.ordineesecuzione.action.ActLoadInserisciOEDetenutoQC";

                   return lPage;
      }

      isFascicoloSiepDiCompetenza();

      FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
			if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
			    throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile inserire un ordine d'esecuzione!" );

      //Controllo Pena COmplessiva
   /*   IPenaComplessiva lPenComp = SIEPLookupRemote.getPenaComplessivaRemote();

      if (lPenComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep()) == null)
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Sul Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stata inserita una Pena Complessiva. Impossibile inserire un ordine d'esecuzione!" );
      }*/
	    // Riempimento  ComboBoX
	     Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
       setRequestAttribute("autoritaEsterna", "" + lOption );

       EventoModel lEve = new EventoModel();
       lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

       IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();

       MagistratoCompetenteMagistratoModel  lMagi = lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
       setRequestAttribute("magistrato", lMagi);


       IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
       PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
       lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

       setRequestAttribute("posizione", lPos);

       // Imposta Modalità.
       setRequestAttribute("modalita", "I");
       setRequestAttribute("evento", lEve);

       return PG_LOAD_INSERISCI_OE_CONDANNATO_DETENUTO_QC;  //restituisce la jsp di VIEW
		}



}
