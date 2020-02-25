package siap.siep.ordineesecuzione.controller;

import java.io.ByteArrayOutputStream;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.util.F3BException;

public interface IOrdineEsecuzioneAlfano
{
  public EventoModel ExUpdateValidaLAlfano(EventoModel aEvento, FascicoloSiepModel aFascicolo)
    throws F3BException;
	public ByteArrayOutputStream ExStampaTrasmissioneProvvedimento ( EventoNotificaModel aEvento, UtenteModel aUtente )
		throws F3BException;
  public EventoNotificaModel ExInserisciOModificaLAlfanoNotifica(EventoNotificaModel aEvento, PenaResiduaModel aPenaResidua)
  	throws F3BException;
  // AMBROS DECRETO LEGGE 78 DEL 2013
  
  public EventoModel ExUpdateValidaL78del2013(EventoModel aEvento, FascicoloSiepModel aFascicolo)
		    throws F3BException;
}