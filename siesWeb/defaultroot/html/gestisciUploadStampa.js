 function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
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
