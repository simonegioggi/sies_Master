 
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
  
  function VisualizzaEstremiOrdinanza()
  {
 
   	node=document.getElementById("DateDeposito");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("EstremiOrdinanza");
    node.style.visibility='visible';
    node.disabled = false;   
 
  }
 
   
  function VisualizzaDateDeposito()
  {
 
   	node=document.getElementById("EstremiOrdinanza");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("DateDeposito");
    node.style.visibility='visible';
    node.disabled = false;   

  }
  
  function VisualizzaElencoMagistrati()
  {
 
   	node=document.getElementById("ElencoMagistrati");
    node.style.visibility='visible';
    node.disabled = false;
    node=document.getElementById("labElencoMagistrati");
    node.style.visibility='visible';
    node.disabled = false;
    node=document.getElementById("radioMagistrato");
    node.checked = true;
    node=document.getElementById("ElencoEsperti");
    node.style.visibility='hidden';
    node.disabled = true;  
    node=document.getElementById("labElencoEsperti");
    node.style.visibility='hidden';
    node.disabled = true;
 
  }
  
  function VisualizzaElencoEsperti()
  {
 
	   	node=document.getElementById("ElencoMagistrati");
	    node.style.visibility='hidden';
	    node.disabled = true;
	    node=document.getElementById("labElencoMagistrati");
	    node.style.visibility='hidden';
	    node.disabled = true;
	    node=document.getElementById("ElencoEsperti");
	    node.style.visibility='visible';
	    node.disabled = false;  
	    node=document.getElementById("labElencoEsperti");
	    node.style.visibility='visible';
	    node.disabled = false;

  }
  function NascondiRelatore()
  {
 
	node=document.getElementById("radioMagistrato");
	node.checked = true;
    node=document.getElementById("ElencoMagistrati");
    node.style.visibility='hidden';
    node.disabled = false; 
    node=document.getElementById("labElencoMagistrati");
    node.style.visibility='hidden';
    node.disabled = false; 
	node=document.getElementById("ElencoEsperti");
    node.style.visibility='hidden';
    node.disabled = true;
	node=document.getElementById("labElencoEsperti");
    node.style.visibility='hidden';
    node.disabled = true;
     
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
     	AbilitaDiv ("EstremiOrdinanza");
     	DisabilitaDiv("DateDeposito");
     	VisualizzaElencoMagistrati();
     }
     else
     {
     	AbilitaDiv ("DateDeposito");
     	DisabilitaDiv("EstremiOrdinanza");
     	VisualizzaElencoMagistrati();
     }   
  }
  
    function ChiudiRicercaAvanzata()
  {
       	DisabilitaDiv ("DateDeposito");
     	DisabilitaDiv("EstremiOrdinanza");
     	NascondiRelatore();
 
  }
  
  function isEstremiOrdinanza()
  {
  	var ret = false;
  	if ( (document.all.item("TipoIntervallo"))[0].checked)
  		ret = true;
  	return ret;
  }
  

  
  
 