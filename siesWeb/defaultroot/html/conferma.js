
	var str;
	function conferma(a_action, a_entityname, a_entityvalue)
	{
  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue;
    // alert("Stringa di conferma0 ->" + str);

    if (window.confirm('Confermi la cancellazione ?'))
    {
    	window.location.href=str;
    }
	}

	/* versioni della funzione con parametri aggiunti */
	/* a_destnname, a_destvalue  : nome e valore del Request parameter che definisce l'azione da eseguire dopo
	  la prima di cancellazione */
	// STUB: ho scoperto che java-script non supporta l'overhead delle funzioni ! Luigi 7-5-2004
	function conferma(a_action, a_entityname, a_entityvalue, a_destnname, a_destvalue )
	{
		str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  "&" + a_destnname + "=" +a_destvalue;
    // alert("Stringa di conferma1 ->" + str);

    if (window.confirm('Confermi la cancellazione ?'))
    {
			window.location.href=str;
    }
	}

	/* versione della funzione con parametri aggiunti */
	/* a_destnname, a_destvalue  : nome e valore del Request parameter che definisce l'azione da eseguire dopo la prima di cancellazione */
	/* a_message : Gestione del messaggio di conferma */
	function confermaMessage(a_action, a_entityname, a_entityvalue, a_destnname, a_destvalue, a_message )
	{
  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  "&" + a_destnname + "=" +a_destvalue;
    // alert("Stringa di conferma1 ->" + str);

    if (window.confirm("" + a_message ))
    {
    	window.location.href=str;
    }
	}

	function conferma1(a_action, a_parameter, a_entityname, a_entityvalue, a_destnname,  a_destvalue )
	{
  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_parameter + "&" + a_entityname + "=" +a_entityvalue +  "&" + a_destnname + "=" +a_destvalue;
    //alert("Stringa di conferma2 ->" + str);

    if (window.confirm('Confermi la cancellazione ?'))
    {
    	window.location.href=str;
    }
	}

	function annulla(a_message ,a_action, a_entityname1, a_entityvalue1 )
	{
  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1;
    // alert("Stringa di annulla ->" + str);

    if (window.confirm("" + a_message))
    {
    	window.location.href=str;
    }
	}

	// Richiamo della finestra di pop-up per inserire motivazione Annullamento
	function confermaAnnullamento(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2)
	{
   	if (window.confirm("Confermi l'annullamento ?"))
   	{
    	var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2, "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
      window.parent.close();
    }
	}

	// Richiamo della finestra di pop-up per inserire motivazione Annullamento con 3 parametri
	function confermaAnnullamento3Param(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2,a_entityname3 ,a_entityvalue3 )
	{
   	if (window.confirm("Confermi l'annullamento ?"))
   	{
    	var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2+ "&" + a_entityname3 + "=" +a_entityvalue3, "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
      window.parent.close();
    }
	}

	function conferma(a_action, a_entityname, a_entityvalue, a_destnname, a_destvalue, a_entitytre, a_valoretre )
	{
  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  "&" + a_destnname + "=" +a_destvalue+  "&" + a_entitytre + "=" +a_valoretre;
    if (window.confirm('Confermi la cancellazione?'))
    {
    	window.location.href=str;
    }
	}

	function conferma(a_action, a_entityname1, a_entityvalue1, a_entity2, a_valore2, a_entity3, a_valore3, a_entity4, a_valore4 )
	{
	  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 +  "&" + a_entity2 + "=" + a_valore2 +  "&" + a_entity3 + "=" + a_valore3 +  "&" + a_entity4 + "=" + a_valore4;
	    if (window.confirm('Confermi la cancellazione?'))
	    {
	    	window.location.href=str;
	    }
	}
