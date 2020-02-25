
if (document.layers) document.captureEvents(Event.KEYDOWN);
document.onkeydown =
  function (evt) {
    var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
    if (keyCode == 13) {
		for (el=document.forms[0].elements.length-1;el>0;el--) {
			if (document.forms[0].elements[el].type=="button") {
	  			var pulsante=document.forms[0].elements[el];
      			pulsante.click();
			}
	    }
    	return false;
    }
    else
      return true;
  };
