    /**
     * Questo modulo javaScript contiene funzioni di controllo richiamate nelle jsp di immissione Esperto per Attivita' nel sistema SIEPE.
     *
     */


      // Controllo delle date nell'inserimento Esperto
      function controlloDateInserimento(data1, data2)
      {
         var ret = true;

         //alert ("Data ini ->" + data2);
         //alert ("Data di confronto ->" + data1);

         if (data2.length < 10 )
         {
            ret = false;
            alert ("Data di inizio mancante");
         }
         else if (ControllaData (data2) == false)
         {
            ret = false;
            alert ("Errore nella data : " + dataIni);
         }
         else if (CompareDate(data1,data2)== false)
         {
            ret = false;
            alert ("La data di inizio non puo' precedere quella di inizio attivita'");
         }
         return ret;
      }


      // Controllo delle date nella chiusura Esperto
      function controlloDateChiusura(data1, data2)
      {
         var ret = true;

         //alert ("Data ini ->" + data2);
         //alert ("Data di confronto ->" + data1);

         if (data2.length < 10 )
         {
            ret = false;
            alert ("Data di fine mancante");
         }
         else if (ControllaData (data2) == false)
         {
            ret = false;
            alert ("Errore nella data : " + dataIni);
         }
         else if (CompareDate(data1,data2)== false)
         {
            ret = false;
            alert ("La data di fine non puo' precedere quella di inizio");
         }
         return ret;
      }



/**
* La funzione cerca all'interno della lista degli esperti associati all'attivita'
* l'esperto con lo stesso id passato come parametro.
* Se esiste si controlla se la sua data di fine e' null oppure posteriore alla data di inizio che si vuole introdurre.
* In questi casi l'utente risulta gia' presente nella lista e la funzione ritorna true.
* Negli altri casi la funzione restituisce false.
* @return
*/
   function cercaEsperto( id_esperto, dataInizio)
   {
     //alert("cercaEsperto ->" + id_esperto);
     var ret = false;
     var i = 0;
     for (i=0; i<NumTotale; i++)
     {
       if ( lIdEsperto[i] == id_esperto)
       {
           // Se la data fine e' null esiste gia' l'esperto attivo
           if (lDataFineEsperto[i] == "-")
            ret = true;
           // Se la data inizio e' <= dell'esperto trovato
           else if (CompareDate(dataInizio,lDataFineEsperto[i]))
            ret = true;
       }
     }
     return ret;
   }
