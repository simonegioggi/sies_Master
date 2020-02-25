<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="tipoUfficioSige"  scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="azionechiamante" scope="request" class="java.lang.String"/>

<%
	// Valore di default della funzione
	String lNomeFunzione = "Ricerca Procedimento SIGE";
%>
<script language="JavaScript">
  function Init()
  {
  	   if (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>[0].checked)
 	       VisualizzaRicercaBase();
        else
           VisualizzaRicercaAvanzata();

  	   loadUfficiAccorpatiByDesc();
  }
</script>
<script language="JavaScript">
    var desktop;
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
     {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
     }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Ricerca Procedimento SIGE</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
 <script language="JavaScript" src="<%=ICostantiFascicoloSige.JS_RICERCA_FASCICOLO%>"></script>

</head>
  <body class="corpo" onLoad="Init();">
  <form name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActRicercaFascicoloSige">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lNomeFunzione%></font>

      </td>
    </tr>
  </table>
  <br>
   <table width="65%">
      <tr>
       <td class="Titolo" width="50%" > Tipo di Ricerca: </td>
          <td class="Titolo" >
            Ricerca Base <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA %>" value="B"   onClick="VisualizzaRicercaBase();" checked>&nbsp;&nbsp;&nbsp;&nbsp; 
			Ricerca Avanzata <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="A"   onClick="VisualizzaRicercaAvanzata();">&nbsp;</td>
      </tr>
    </table>
    <br>
    
    <table width="65%">   
   <tr>
    <td class="Titolo" > Selezionare Ufficio: </td>
    </tr>
    <tr>
    <table width="65%">
    <tr>
    <td class="l"  width="30%" >
      Tipo Ufficio <font class="ob">(*) </font>
      </td>
      <td class="l">
      <select Title="TipoUfficioSige" name="<%=ICostantiFascicoloSige.CAMPO_TIPO_UFFICIO%>" >
        <%=tipoUfficioSige%>
     </select>
      </td>
  	</tr>
  	<tr>
     <td class="l">
        Sede&nbsp;<font class="ob">(*)</font>
       </td>
      <td class="l">
         <input title="Sede"  type="text" value="<%=ComuneUfficioConnesso%>" name="<%=ICostantiFascicoloSige.CAMPO_SEDE%>"  maxlength="35" size="35" readonly="readonly">
        <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiFascicoloSige.CAMPO_SEDE%>',document.f.<%=ICostantiFascicoloSige.CAMPO_TIPO_UFFICIO%>[document.f.<%=ICostantiFascicoloSige.CAMPO_TIPO_UFFICIO%>.options.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
    </td>
  </tr>
  	<tr>
     <td class="l">Ufficio Accorpato</td>
     <td class="l">
         	<select name="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO %>">
         	<option value="0" >-</option>
         	</select>
    </td>
  </tr>
  </table>
  </tr>
  </table>
   <input type="hidden" value="<%=azionechiamante%>" name="<%=ICostantiFascicoloSige.CAMPO_AZIONE_CHIAMANTE%>" >
   
   <div id="comune" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
    <div id="RicercaBaseDiv" style="position:relative;  top: 0; left: 0;   visibility:visible; " >  
         <jsp:include page="<%=ICostantiFascicoloSige.DIV_RICERCA_FASCICOLO_BASE%>"/>
    </div>
     <div id="RicercaAvanzataDiv" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
        <jsp:include page="<%=ICostantiFascicoloSige.DIV_RICERCA_FASCICOLO_AVANZATA%>"/>
     </div>
 </div>
 </form>
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
   </script>
  <script language="JavaScript" type="text/javascript">
   	// Funzione di Validazione per la Ricerca Base
     function ValidatorBase()
     {
     	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno è obbligatorio");
    	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","numeric");
    	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
     	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>","req","Il campo Numero è obbligatorio");
    	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric");
    	//Chiama la funzione di Verify().
    	frmvalidator.setAddnlValidationFunction("VerificaBase");
    	loadNumProgOrigin();
	}
  </script>
     <script language="JavaScript" type="text/javascript">
	// Funzione di Validazione per la Ricerca Estesa
	// Viene usata questa funzione per definire i campi da controllare che cambiano dalle opzioni scelte.
     function ValidatorEstesa()
     {
     // alert ("ValidatorEstesa");
     	
     	if (isEstremiAnnoNum())
     	{
     		frmvalidator.clearAllValidations();
     		 // alert ("isEstremiAnnoNum");
      		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","req","Il campo Anno Inizio è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","minlen=4","La lunghezza del campo Anno Inizio deve essere di 4 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_INI_ORIGIN%>","req","Il campo Numero Inizio  è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_INI_ORIGIN%>","numeric");
<%--     		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","req","Il campo Anno Fine è obbligatorio"); --%>
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","minlen=4","La lunghezza del campo Anno Fine deve essere di 4 caratteri");
<%--     		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_FINE_ORIGIN%>","req","Il campo Numero Fine  è obbligatorio"); --%>
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_FINE_ORIGIN%>","numeric");  	 	
      	frmvalidator.setAddnlValidationFunction("VerificaBase");
      	loadNumProgIntervalOrigin();    	
     	}
     	else
     	{
     		frmvalidator.clearAllValidations();
     	    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","req","Il campo Giorno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","req","Il campo Giorno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","req","Il campo Mese è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","req","Il campo Mese è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","req","Il campo Anno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","req","Il campo Anno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
      		frmvalidator.setAddnlValidationFunction("VerificaEstremiDate");
      	}
      }
  </script>
  
   <script language="JavaScript" type="text/javascript">
     function VerificaEstremiDate()
  {
    var ritorno = true;
	var data_iniziale = document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.value + '/' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>.value + '/'+ document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>.value;
			
	var data_finale = document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.value + '/' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>.value + '/'+ document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>.value;
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     if (! ControllaData(data_iniziale))
     {
        alert('Data iniziale non valida!');
        ritorno =  false;
     }
     else if (! ControllaData(data_finale))
     {
     	alert('Data finale non valida!');
        ritorno =  false;
     }
     // Controllo data finale >= Data iniziale .
     else if( !CompareDate( data_iniziale, data_finale ) )
     {
        alert('Data finale < Data Iniziale!');
        ritorno =  false;
     }
     else if( !CompareDate( data_iniziale, data_sistema ) )
     {
        alert('Data iniziale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
     else if( !CompareDate( data_finale, data_sistema ) )
     {
     	alert('Data finale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
     return ritorno;
 	}
   </script>

  <script language="JavaScript" type="text/javascript">
  var ufficiAccorpatiArray = new Array();

  <%
  Iterator uaIter = ufficiAccorpati.iterator();
  int uaIndice = 0;
  while (uaIter.hasNext())
  {
  	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
  %>
  ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodTipoUfficio()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getCodUfficio()%>","<%=uaModel.getDescrizioneNewUfficio()%>","<%=uaModel.getCodTipoUfficioNew()%>"); 
  <%
  uaIndice ++;
  }
  %>

  

  function resetUfficiAccorpati(){
		var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO %>;
		ufficioAccorpatoSelect.options[0].setAttribute("selected", "selected");
  }

  function transCoding(cod){
	  var ret = '';
	  if (cod=='DIB'){
		  ret = 'Tribunale Ordinario';
	  } else if (cod=='TRIBSD'){
		  ret = 'Sezione Distaccata Tribunale';
	  } else if (cod=='CAS'){
		  ret = 'Corte Assise';
	  } else if (cod=='GIP'){
		  ret = 'Gip presso Tribunale';
	  }
	  return ret;
  }

  function loadUfficiAccorpati(codUfficio){
		var i=0;
		var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO %>;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[3]==codUfficio){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[2])+")", ufficio[1]+"-"+ufficio[4]);
			}
			i++;
		}
  }

  function loadUfficiAccorpatiByDesc(){
		var i=0;
		var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO %>;
		var ufficioBaseDesc = document.f.<%= ICostantiFascicoloSige.CAMPO_SEDE %>.value;
		var ufficioBaseTipoSelect = document.f.<%= ICostantiFascicoloSige.CAMPO_TIPO_UFFICIO %>;
		var ufficioBaseTipo = ufficioBaseTipoSelect.options[ufficioBaseTipoSelect.selectedIndex].value;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[5]==ufficioBaseDesc && ufficio[6]==ufficioBaseTipo){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[2])+")", ufficio[1]+"-"+ufficio[4]);
			}
			i++;
		}
  }

  function loadNumProgOrigin(){
	  document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value = "";
	  var numProg = document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>.value;
      var ufficioAccorpato = document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>.value;
      var parts=ufficioAccorpato.split("-"); 
      var offSetInt = parseInt(parts[0]);
      if (numProg){
    	  var newProg = parseInt(numProg) + offSetInt;
    	  document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value = newProg;
          }
<%-- 	  alert(document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value); --%>
	  }

  function loadNumProgIntervalOrigin(){
      var ufficioAccorpato = document.f.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>.value;
      var parts=ufficioAccorpato.split("-"); 
      var offSetInt = parseInt(parts[0]);
      document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.value = "";
      var numProgIni = document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_INI_ORIGIN%>.value;
      if (numProgIni){
      	var numProgIniInt = parseInt(numProgIni);
        var newProgIniInt = numProgIniInt + offSetInt;
        document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.value = newProgIniInt;
          }
<%--       alert(document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.value); --%>

      document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.value = "";
      var numProgFin = document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE_ORIGIN%>.value;
      if (numProgFin){
          var numProgFinInt = parseInt(numProgFin);
          var newProgFinInt = numProgFinInt + offSetInt;
          document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.value = newProgFinInt;
          }
<%--       alert(document.f.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.value); --%>
  }
</script>
  </body>
</html>