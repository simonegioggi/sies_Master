package siap.siep.annotazionemanuale.controller;

import siap.sico.evento.model.EventoModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.util.F3BException;

public interface IIndulto
{
   public EventoModel ExValidaProvvedimentoIndulto(EventoModel aEvento,
     PenaResiduaModel aPenRes) throws F3BException;

}