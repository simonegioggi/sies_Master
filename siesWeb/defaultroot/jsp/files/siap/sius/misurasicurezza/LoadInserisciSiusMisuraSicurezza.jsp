<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="misurasicurezza" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.util.Vector"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="idfascicolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="riferimentoTitoloEsecutivo" scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunti riferimenti ad oggetti di tipo "String" --%>
<jsp:useBean id="codTipoUfficio" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="codNatura" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="fasSieIdFascicoloSiepRif" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="numAnni" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="numMesi" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="numGiorni"					scope="request" class="java.lang.String"/>

<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
	String strOggetto ="";
	String lcod = "";
	String lnat = "";
	if(modalita.compareTo("M")==0)
	{	
		if(misurasicurezza != null)
		{	
			lcod = misurasicurezza.getCodTipo();
			lnat = misurasicurezza.getCodNatura();
		}	
	}	

	int element = 0;
	int eledaModificare = 0;
    Iterator itx = tipoMisuraSicurezza.iterator();
    while(itx.hasNext())
    {
	       DecodificheModel lDecMod = (DecodificheModel)itx.next();
	
	       strOggetto += lDecMod.getFiltro() +";";
	       strOggetto += lDecMod.getCode()+";";
	       strOggetto += lDecMod.getDescription()+"#";
	       if(modalita.compareTo("M")==0)
	   	   {
	    	   if(lDecMod.getFiltro().equals(lnat))
	    	   {	
	    		   element++;
		    	   if(lDecMod.getCode().equals(lcod))
		    	   {
		    		   eledaModificare = element;
		    	   }
	    	   }		   
	   	   }
     }
	
%>

<html>
<head>
  <title>[S.I.E.S.] - GestioneMisuraSicurezza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript">
  
  var strOggetto = "<%=strOggetto%>";
  var TipodaModificare ="<%= eledaModificare%>";

  function caricaComboMod (valueTextStr, sep1, sep2, filtro, selField)
  {
	  caricaCombo(valueTextStr, sep1, sep2, filtro, selField);
	  selField.options.selectedIndex = TipodaModificare-1;
  }
  
  function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
	    // valueTextStr = stringa nel formato richiesto
	    // sep1 = separatore interno alla coppia di valori
	    // sep2 = separatore tra coppie
	    // filtro = valore su cui fare il test
	    // selField = oggetto combo da caricare
	
	
	    clearDropDown(selField);
	
	    var aPairs = valueTextStr.split(sep2);
	
	    if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
	    {
	      aPairs[aPairs.length - 1] = null;
	      aPairs.length--;
	    }
	
	
	    for (var i=0; i < aPairs.length; i++)
	    {
	      aValueText = aPairs[i].split(sep1);
	      if (filtro=='null' || filtro==aValueText[0])
	      {
	    		oItem = new Option;
	    		oItem.value = aValueText[1];
	    		oItem.text = aValueText[2];
	    		selField.options[selField.options.length] = oItem;
	      }
	    }
	
	    selField.options.selectedIndex = 0;
	    //se il valore del filtro è "-" disabilito il campo
	    if(filtro=='-'){
	    	selField.disabled=true;
	    }
	    else{
	    	selField.disabled=false;
	    }

  }
  
  function clearDropDown (selField)
  {
  	while (selField.options.length > 0)
  	selField.options[0] = null;
  }

	function Verify() {
		// MERGE v10: aggiunto controllo preventivo
		if (document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA%>.value == '-') {
			alert('Scegliere la Natura della Misura');
        	return false;
      	}

		if (document.LoadInserisciSiusMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO%>.value == '-') {
			alert('Il campo Tipo Misura è obbligatorio');
        	return false;
      	}
     	InserisciAggiorna();
  	}

  </script>

<script language="JavaScript">
<%if(!lTipoFunzione.equals(""))
{%>
   function  Fine()
    {
       document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=idfascicolo%>";
       document.LoadInserisciSiusMisuraSicurezza.F.disabled=true;
       document.LoadInserisciSiusMisuraSicurezza.Inserisci.disabled=true;
    }
<%}%>
   function  InserisciAggiorna()
    {

<%      if( modalita.equals("I") )
        {
%>
          	document.LoadInserisciSiusMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sius.misurasicurezza.action.ActInserisciSiusMisuraSicurezza";
<%        
        } 
		else if( modalita.equals("M") )
        {
%>
          	document.LoadInserisciSiusMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sius.misurasicurezza.action.ActModificaSiusMisuraSicurezza";
 <%     }
%>
<%if(!lTipoFunzione.equals(""))
{%>
       document.LoadInserisciSiusMisuraSicurezza.F.disabled=true;
<%}%>
       document.LoadInserisciSiusMisuraSicurezza.Inserisci.disabled=true;
    }

// MEV10-s3: aggiunta funzione di gestione
function gestisciTipo() {
	var codTipoUff = '<%=codTipoUfficio%>';
	var mod = '<%=modalita%>';
	if( mod == "I" ){
		if ("TDSM" == codTipoUff || "UDSM" == codTipoUff) {
			document.all.<%=IWebConstants.ACTION_FIELD%>.value ="siap.sius.misurasicurezza.action.ActLoadInserisciSiusMisuraSicurezza";
			document.LoadInserisciSiusMisuraSicurezza.submit();
	    	return true;
		}
	} else {
		if ("TDSM" == codTipoUff || "UDSM" == codTipoUff) {
			document.all.<%=IWebConstants.ACTION_FIELD%>.value ="siap.sius.misurasicurezza.action.ActLoadModificaSiusMisuraSicurezza";
			document.LoadInserisciSiusMisuraSicurezza.submit();
	    	return true;
		}
	}
}
</script>
</head>
<body class="corpo" onload="javascript:caricaComboMod(strOggetto,';','#',document.LoadInserisciSiusMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciSiusMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>);">
 <table>
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
     MisuraSicurezzaModel lModel = new MisuraSicurezzaModel();
     //String lAzione = new String();
     if( modalita.equals("I") )
     {
      // lAzione = "siap.sius.misurasicurezza.action.ActInserisciSiusMisuraSicurezza";
%>

   <font class="campo">Inserimento Misura Sicurezza</font>
<%  }
     else if( modalita.equals("M") )
     {
    // lAzione = "siap.sius.misurasicurezza.action.ActModificaSiusMisuraSicurezza";
     lModel = misurasicurezza;
%>
 <font class="campo">Modifica Misura Sicurezza</font>
			  <%}%>
    </td>
  </tr>
</table>

  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSiusMisuraSicurezza">
			<table cellspacing=2 cellpadding=2>
  				<tr>
        			<td class="l">Natura Misura</td>
        			<td class="l">
          				<select title="Natura Misura" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>" onChange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciSiusMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciSiusMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>);">
          				<%=naturaMisuraSicurezza%>
          				</select>
        			</td>
  				<tr id="tipomisura">
        			<td class="l">Tipo Misura  <font class="ob">(*)</font></td>
        			<td class="l">
          			  <select title="Tipo Misura" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO  %>">
          			  <%=tipoMisuraSicurezza%>
          			  </select>
        			</td>
  				</tr>
  				<tr>
        			<td class="l">Durata Misura</td>
        			<td class="l">Anni
          				<input title="Anni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumAnni()) %>" type="text" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI %>"  >
          				Mesi
          				<input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumMesi()) %>" type="text" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI%>"  >
          				Giorni
          				<input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumGiorni()) %>" type="text" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI %>"  >
        			</td>
  				</tr>
    			<tr>
        			<td class="l">Riferimento Titolo Esecutivo</font></td>
        			<td class="l">
          			  <select title="RifTitoloEsecutivo" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF %>">
          			  <%=riferimentoTitoloEsecutivo%>
          			  </select>
        			</td>
  				</tr>
  				<tr>
      				<td colspan=2>
        				<input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      				</td>
					<%if(!lTipoFunzione.equals("")) { %>
      				<td colspan=2>
        				<input type="button"  class="bottone"  name="F" value="  Fine  " onClick="javascript:Fine();">
      				</td>
					<%}%>
				</tr>
			 </table>
   			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   			<input type="HIDDEN" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=lModel.getIdMisuraSicurezza()%>">
   			<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=idfascicolo%>">
   			<input type="HIDDEN" name="lTipoFun" value="<%=lTipoFunzione%>">
		</form>
		<script language="JavaScript" type="text/javascript">
		    var frmvalidator = new Validator("LoadInserisciSiusMisuraSicurezza");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI%>","numeric");
		    frmvalidator.setAddnlValidationFunction("Verify");
		
		    // MEV10-s3: aggiunte impostazioni di proprietà
		    var codNatura = '<%=codNatura%>';
			if (codNatura && codNatura != '') {
				document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA%>.value = codNatura;
			}
			var numAnni 	= '<%=numAnni%>';
			var numMesi 	= '<%=numMesi%>';
			var numGiorni 	= '<%=numGiorni%>';
			if (numAnni && numAnni != '') {
				document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI%>.value = numAnni;
			}
			if (numMesi && numMesi != '') {
				document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI%>.value = numMesi;
			}
			if (numGiorni && numGiorni != '') {
				document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI%>.value = numGiorni;
			}
			var fasSieIdFascicoloSiepRif = '<%=fasSieIdFascicoloSiepRif%>';
			if (fasSieIdFascicoloSiepRif && fasSieIdFascicoloSiepRif != '') {
				document.LoadInserisciSiusMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF%>.value = fasSieIdFascicoloSiepRif;
			}
	  	</script>
	</body>
</html>