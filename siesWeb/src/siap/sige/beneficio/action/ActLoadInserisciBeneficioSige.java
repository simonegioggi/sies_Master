package siap.sige.beneficio.action;

import siap.siep.beneficio.action.ActLoadInserisciBeneficio;


/**
* <p>Title: ActLoadInserisciBeneficioSige</p>
* <p>Description: Classe Action per la creazione e visualizzazione della form di input per il beneficio.
* <p> Poichè la funzione è analoga a quella di SIEP, viene ereditata la funzione corrispondente 
* <p> aggiungendo però  l'attributo modo = "SIGE" per poter specializzare la jsp utilizzata 
* <p> sia nel caso siep che sige.
* load inserisci di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadInserisciBeneficioSige extends ActLoadInserisciBeneficio 
{
   public String processRequest() throws Exception
  {
	   setRequestAttribute("modo", "SIGE");
	   return preparaForm();  //restituisce la jsp di VIEW
  }
 
}