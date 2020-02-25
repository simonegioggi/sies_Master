    /**
     * Controllo obbligatorieta' campi combo.
     *
     * La Funzione controlla se il campo/i campi relativo/i all'oggetto lCombo sia stato/siano stati valorizzato/i.
     * In caso che uno solo di essi sia = "-" viene sollevato un messaggio di alert e restituito "false".
     * Altrimenti viene restituito true.
     * @return boolean
     */

    function VerifyCombo(lCombo, nameCombo)
    {
      // Size della Combo = 1
      if (typeof (lCombo[0][0]) =="undefined" )
      {
        if (lCombo.value =="-")
        {
          alert("Il Campo "+nameCombo+" deve essere impostato");
          return false;
        }
      }
      else
      {
        for (j = 0; j < lCombo.length ; j++ )
        {
          for (i = 0; i < lCombo[j].length ; i++ )
          {
            if ( (lCombo[j][i].selected) && (lCombo[j][i].value == '-' ) )
            {
              alert("Tutti i campi "+nameCombo+" devono essere impostati");
              return false;
            }
          }
        }
      }
      return true;
    }