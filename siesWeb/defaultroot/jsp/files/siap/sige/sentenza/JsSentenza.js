  function VisualizzaSentenza()
  {
 	ChiudiSentenzaStraniera();
 	ChiudiDecreto();
   	node=document.getElementById("SentenzaStranieraDiv");
    node.style.visibility='hidden';
    node.disabled = true;
   	node=document.getElementById("DecretoDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("SentenzaDiv");
    node.style.visibility='visible';
    node.disabled = false;   
  }
  
  function VisualizzaSentenzaStraniera()
  {
 	ChiudiSentenza();
 	ChiudiDecreto();
   	node=document.getElementById("SentenzaDiv");
    node.style.visibility='hidden';
    node.disabled = true;
   	node=document.getElementById("DecretoDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("SentenzaStranieraDiv");
    node.style.visibility='visible';
    node.disabled = false;   
  }

  function VisualizzaDecreto()
  {
 	ChiudiSentenza();
 	ChiudiSentenzaStraniera();
   	node=document.getElementById("SentenzaDiv");
    node.style.visibility='hidden';
    node.disabled = true;
   	node=document.getElementById("SentenzaStranieraDiv");
    node.style.visibility='hidden';
    node.disabled = true;
    node=document.getElementById("DecretoDiv");
    node.style.visibility='visible';
    node.disabled = false;   
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
 
  function ChiudiSentenza()
  {
		DisabilitaDiv("SentenzaDiv");
	}

  function ChiudiSentenzaStraniera()
  {
		DisabilitaDiv("SentenzaStranieraDiv");
	}

  function ChiudiDecreto()
  {
		DisabilitaDiv("DecretoDiv");
	}

	function ctrl_autorita(idcmb1, idcmb2, idDiv, idTipoRito)
	{
		// cmb1 e' la combo che fa scattare la funzione
		var cmb1 = document.getElementById(idcmb1);	
		var cmb2 = document.getElementById(idcmb2);
			
		var arrCmb = new Array(cmb1, cmb2);
		var arrGrado = new Array();
		
	 	for(var i=0;i<arrCmb.length;i++)
	 	{
			if (arrCmb[i].value == "CSS") 
			{
				arrGrado[i] = 3;
			}
			else if (arrCmb[i].value == "CAP" || arrCmb[i].value == "CASAP" || arrCmb[i].value == "CAPSM") 
			{
				arrGrado[i] = 2;
			}
			else
			{
				arrGrado[i] = 1;
			}
		}		
	 	
	 	if (cmb1.value != "-" && cmb2.value != "-")
	 	{
			if (cmb1.value == cmb2.value) 
			{
				alert("Non e' consentito selezionare due Autorita' Emittenti uguali!");
				cmb1.selectedIndex = 0;
				cmb1.focus();
			}
			else if (arrGrado[0] == arrGrado[1])
			{			
				// eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
				if (!( (cmb1.value == "GP" || cmb2.value == "PT") && (cmb2.value == "DIB" || cmb2.value == "TRIBSD")) 
				 && !( (cmb2.value == "GP" || cmb2.value == "PT") && (cmb1.value == "DIB" || cmb1.value == "TRIBSD")))
				{
				
					alert("Non e' consentito selezionare due Autorita' Emittenti dello stesso grado!");
					cmb1.selectedIndex = 0;
					cmb1.focus();
				}
			}
		}	
				
		var node = document.getElementById(idDiv);
		var cmbRito = document.getElementById(idTipoRito);
	
		if (cmb1.value == "DIB" || cmb1.value == "TRIBSD")
		{
			node.style.visibility = "visible";
		}
		else
		{
			node.style.visibility = "hidden";
			cmbRito.selectedIndex = 0;		
		}
	}
  
  function isEstremiAnnoNum()
  {
  	var ret = false;
  	if ( (document.all.item("TipoIntervallo"))[0].checked)
  		ret = true;
  	return ret;
  }