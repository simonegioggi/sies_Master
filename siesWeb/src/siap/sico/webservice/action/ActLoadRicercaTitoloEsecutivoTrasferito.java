package siap.sico.webservice.action;

public class ActLoadRicercaTitoloEsecutivoTrasferito extends ActWsBase implements ICostantiNsc
{
    public String processRequest() throws Exception
    {
        return PG_LOAD_RICERCA_TITOLO_ESECUTIVO_TRASF;       //restituisce la jsp di VIEW
    }
}
