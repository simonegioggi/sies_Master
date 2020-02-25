 
  function VisualizzaRicercaBase()
  {
 	ChiudiRicercaAvanzata();
   	node=document.getElementById("RicercaAvanzataDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("RicercaBaseDiv");
    node.style.visibility='visible';
    node.disabled = false;   

  }

  
   function VisualizzaRicercaAvanzata()
  {
 
   	node=document.getElementById("RicercaBaseDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("RicercaAvanzataDiv");
    node.style.visibility='visible';
    node.disabled = false;   
  	InitRicercaAvanzata();

   }
  
  function VisualizzaEstremiAnnoNum()
  {
 
   	node=document.getElementById("EstremiDate");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("EstremiAnnoNum");
    node.style.visibility='visible';
    node.disabled = false;   
 
  }
 
   
  function VisualizzaEstremiDate()
  {
 
   	node=document.getElementById("EstremiAnnoNum");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("EstremiDate");
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
  
  function VerificaBase()
  {
  	return true;
  }
 
  function InitRicercaAvanzata()
  {
  	if ( (document.all.item("TipoIntervallo"))[0].checked)
  	{
     	AbilitaDiv ("EstremiAnnoNum");
     	DisabilitaDiv("EstremiDate");
     }
     else
     {
     	AbilitaDiv ("EstremiDate");
     	DisabilitaDiv("EstremiAnnoNum");
     }   
  }
  
    function ChiudiRicercaAvanzata()
  {
       	DisabilitaDiv ("EstremiDate");
     	DisabilitaDiv("EstremiAnnoNum");
 
  }
  
  function isEstremiAnnoNum()
  {
  	var ret = false;
  	if ( (document.all.item("TipoIntervallo"))[0].checked)
  		ret = true;
  	return ret;
  }
  

  
  
 