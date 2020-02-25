

function check_email(email) {

	/*
	LEGENDA DEGLI ERRORI:

	1) La chiocciola e' presente: come primo o ultimo carattere o ne sono state digitate piu' di una;
	2) L'e-mail contiene uno o piu' caratteri non ammessi contenuti nella variabile nochar;
	3) Il punto e' presente: come primo, ultimo o penultimo carattere, prima o dopo la chiocciola;
	4) Ci sono 2 punti (..) oppure due trattini (--) vicini;
	5) Non c'e' nessun punto dopo la chiocciola
	*/

	var errors=""
	var i

	// Posizione della chiocciola.
	var chiocPos=email.indexOf("@")

	// Insieme dei caratteri non ammessi in un e-mail.
	var nochar="\\/^,;:+ìèòàù'<>()%=?!| " + '"'

	// Prima lettera dell'e-mail.
	var first_letter=email.substring(0,1)

	// Ultima lettera dell'e-mail.
	var last_letter=email.substring(email.length-1,email.length)

	// Penultima lettera dell'e-mail.
	var Penultima_letter=email.substring(email.length-2,email.length-1)

	// Lettera a sinistra della chiocciola.
	var sx_chioc=email.substring(chiocPos-1,chiocPos)

	// Lettera a destra della chiocciola.
	var dx_chioc=email.substring(chiocPos+1,chiocPos+2)

	if ((chiocPos<"1") || (chiocPos==(email.length-1)) || (chiocPos!=(email.lastIndexOf("@")))) {
	errors+="\n- Il carattere chiocciola (@) non e' presente \no si trova in una posizione in posizione errata!"
	}
	else {
	  for (var i=0; i<=nochar.length-1; i++) {
	    if (email.indexOf(nochar.substring(i,i+1))!="-1") {
	     errors+="\n- Hai digitato dei caratteri non ammessi!"
	     break
	    }
	  }
	}

	if (errors=="") {
	  if ((first_letter==".") || (sx_chioc==".") || (dx_chioc==".") || (last_letter==".") || (Penultima_letter==".") ) {
	     errors+="\n- Il punto (.) e' in posizione errata!"
	  }
	  else {

	    for (var i=0; i<=email.length-1; i++) {
	      if ((email.substring(i,i+1)==".") && (email.substring(i+1,i+2)==".")) {
	        errors+="\n- Ci sono due caratteri (.) vicini!"
	        break
	      }
	      if ((email.substring(i,i+1)=="-") && (email.substring(i+1,i+2)=="-")) {
	        errors+="\n- Ci sono due caratteri (-) vicini!"
	        break
	      }
	    }
	  }
	}
	PuntoDopoChioc = 0
	if (errors=="") {
	  for (var i=chiocPos+1; i<=email.length-3; i++) {
	    if (email.substring(i,i+1)==".") {
	      PuntoDopoChioc = 1
	      break
	    }
	  }
	  if (PuntoDopoChioc == 0) {
	    errors+="\n- Non hai indicato il dominio (.it .com .net ...)!"
	  }
	}
	return errors
}

function OnlyNumeric(sStr) {
	var OK=true;
	for (var i=0;i<sStr.length;i++)
	{
		if (sStr.charCodeAt(i)>57 || sStr.charCodeAt(i)<48) OK=false
	}
	return OK;
}

function ControllaData(Data)
{
	if (Data.length!=10) return false;

	day=Data.substring(0,2);
	month=Data.substring(3,5);
	year=Data.substring(6,10);
	var Miadata = new Date(year, month-1, day);
	var rgiorno=Miadata.getDate();
	var rmese=Miadata.getMonth();
	var ranno=Miadata.getYear();
	return (((day==rgiorno) && (day>0)) && ((month-1==rmese) && (month>0)) && year>0);
}


function verify_form(aForm)
{
	var nome_campo;
	var campo;
	var OK=true;
	var extraData;
	var data_to_confirm;
	var tmpmese;
	var tmpanno;
	var controlla;

	for (var ele=0;ele<document.forms[aForm].elements.length && OK;ele++)
	{
		controlla=true;
		campo=document.forms[aForm].elements[ele];
		nome_campo=campo.name;
		if (nome_campo.substring(nome_campo.length,nome_campo.length-2)=="OB")
		{
			if (campo.value=='')
			{
				alert ('Il campo ' + campo.title + ' è obbligatorio');
				OK=false;
				campo.focus();
			}
		}
		if (OK && (nome_campo.substring(nome_campo.length,nome_campo.length-2)=="ON" || nome_campo.substring(nome_campo.length-2,nome_campo.length-4)=="ON"))
		{
			OK=OnlyNumeric(campo.value);
			if (!OK) alert ('Il campo ' + campo.title + ' DEVE contenere solo numeri');
				campo.focus();
		}

		if (OK && (nome_campo.substring(0,6).toUpperCase()=="GIORNO"))
		{
			extraData=nome_campo.slice(6);
			tmpmese=eval('document.forms[aForm].Mese'+extraData)
			tmpanno=eval('document.forms[aForm].Anno'+extraData)
			if ((campo.value=='') && (tmpmese.value=='') && (tmpanno.value=='')) controlla=false;
			if (campo.value.length==1) campo.value='0'+ campo.value;
			data_to_confirm=campo.value+'/';
      campo=eval('document.forms[aForm].Mese'+extraData);
			nome_campo=campo.name;
			if (campo.value.length==1) campo.value='0'+ campo.value;
			data_to_confirm=data_to_confirm+campo.value+'/';
			campo=eval('document.forms[aForm].Anno'+extraData);
			nome_campo=campo.name;
			if (campo.value.length==1) campo.value='0'+ campo.value;
			data_to_confirm=data_to_confirm+campo.value;
			if (controlla) OK=ControllaData(data_to_confirm);
			if (!OK) {
			alert ('La data ' + campo.title + ' non è una data valida');
			eval('document.forms[aForm].Giorno'+extraData).focus();
			}
		}
		if (OK && (nome_campo.substring(0,4).toUpperCase()=="MAIL"))
		{
			OK=check_email(campo.value)=="";
			if (!OK) alert (check_email(campo.value));
			campo.focus();
		}
	}
	if (OK) document.forms[aForm].submit();
}


