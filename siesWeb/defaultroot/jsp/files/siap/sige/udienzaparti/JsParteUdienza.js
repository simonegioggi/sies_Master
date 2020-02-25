  function VisualizzaPersonaFisica()
  {
 	ChiudiPersonaGiuridica();
   	node=document.getElementById("PersonaGiuridicaDiv");
    node.style.visibility='hidden';
    node.disabled = true;
 	node=document.getElementById("PersonaFisicaDiv");
    node.style.visibility='visible';
    node.disabled = false;
  }
  
  function VisualizzaPersonaGiuridica()
  {
 	ChiudiPersonaFisica();
   	node=document.getElementById("PersonaFisicaDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("PersonaGiuridicaDiv");
    node.style.visibility='visible';
    node.disabled = false;   
  }

  function AbilitaDiv(nomeDiv)
  {
    node=document.getElementById(nomeDiv);
    node.style.visibility='visible';
    node.disabled = false;   
  }
  
  function DisabilitaDiv(nomeDiv)
  {
    node=document.getElementById(nomeDiv);
   	node.style.visibility='hidden';
    node.disabled = true;
  }
  
  function ChiudiPersonaGiuridica()
  {
	  DisabilitaDiv("PersonaGiuridicaDiv");
  }

  function ChiudiPersonaFisica()
  {
	  DisabilitaDiv("PersonaFisicaDiv");
  }
