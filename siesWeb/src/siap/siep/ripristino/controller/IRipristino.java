package siap.siep.ripristino.controller;

import siap.sico.evento.model.EventoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;


/**
* <p>Title: PosizioneGiuridicaLuogoDetenzioneController</p>
* <p>Description: Classe Controller per PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


public interface IRipristino
{
  public EventoModel ExUpdateValidaProvvedimentoRipristino(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
}
