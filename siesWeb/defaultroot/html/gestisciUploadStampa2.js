 function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
      if (node != undefined )
          node.style.visibility='visible';
    }

    function controllaUpload()
    {
     if (!(document.comandi.CampoValida.checked)  &&   ( document.comandi.CampoBlob.value == ""))
          {
       		alert('Inserire il Documento o cliccare il Campo di Validazione');
			 return false;
		  }

      submitonce(document.comandi);

      return true;
    }


 function submitonce(theform)
{
  if (document.all||document.getElementById)
   {
   //screen thru every element in the form, and hunt down "submit" and "reset"
    for (i=0;i<theform.length;i++)
     {
     var tempobj=theform.elements[i];
       if(tempobj.type.toLowerCase()=="submit" ||tempobj.type.toLowerCase()=="reset")
         //disable em
         tempobj.disabled=true;
     }
   }
}

function stampa(hrefStampa)
   {
     var stampaWin = window.open("/html/stampa.htm","stampa","toolbar=yes,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,width=2000,height=2000");
     stampaWin.moveTo(0,0);
     stampaWin.resizeTo(screen.availWidth,screen.availHeight);
     stampaWin.focus();

     var stampaDoc = window.open(hrefStampa+'&\/Documento.rtf',"stampa","toolbar=yes,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,width=2000,titlebar=no,height=2000");
     stampaDoc.moveTo(0,0);
     stampaDoc.resizeTo(screen.availWidth,screen.availHeight);
     stampaDoc.focus();
   }

/*
  La funzione apre la jsp di stampa FileJsp, a questa passa attraverso hrefStampa,
  l'action_field e gli altri parametri necessari alla generazione della stampa.
*/
function stampa2(FileJsp,hrefStampa )
   {
     var stampaWin = window.open(FileJsp + "?" + hrefStampa,"stampa","toolbar=yes,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,width=2000,height=2000");
     stampaWin.moveTo(0,0);
     stampaWin.resizeTo(screen.availWidth,screen.availHeight);
     stampaWin.focus();
   }

function donothing()
{}
