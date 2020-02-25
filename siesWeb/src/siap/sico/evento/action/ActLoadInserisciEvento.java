package siap.sico.evento.action;


/**
* <p>Title: ActLoadInserisciEvento</p>
* <p>Description: Classe Action per la load inserisci di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadInserisciEvento extends ActionSiap implements ICostantiEvento
{
	 public String processRequest() throws F3BException
		{

   /*  if( isSessionAttributeNullObj("fascicolo") )
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicolo" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare un procedimento." );
      }
      FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

      if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
			{
				throw new SIEPException( SIEPException.USER_MESSAGE, "Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Capi di Imputazione" );

				// setta la risposta nella request
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Capi di Imputazione");
			}
			if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
			    throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile inserire un ordine d'esecuzione!" );


      //Controllo Pena COmplessiva
      IPenaComplessiva lPenComp = SIEPLookupRemote.getPenaComplessivaRemote();

      if (lPenComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep()) == null)
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Sul Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stata inserita una Pena Complessiva. Impossibile inserire un ordine d'esecuzione!" );
      }
	    // Riempimento  ComboBoX
	     Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
       setRequestAttribute("autoritaEsterna", "" + lOption );

       EventoModel lEve = new EventoModel();
       lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

       IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();

       MagistratoCompetenteMagistratoModel  lMagi = lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
       setRequestAttribute("magistrato", lMagi);

       // Imposta Modalità.
       setRequestAttribute("modalita", "I");
       setRequestAttribute("evento", lEve);*/

       return "";  //restituisce la jsp di VIEW
		}



}
