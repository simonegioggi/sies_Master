<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<jsp:useBean id="modalita" 					   scope="request" class="java.lang.String"/>
<jsp:useBean id="misurasicurezza" 			   scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="naturaMisuraSicurezza" 	   scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"         	   scope="request" class="java.lang.String"/>
<jsp:useBean id="idfascicolo"       		   scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       				   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" 		   scope="request" class="java.util.Vector"/>
<jsp:useBean id="PosizioneGiuridicaLuogoAltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="penaresidua"          		   scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


<%
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
	
	String strOggetto ="";
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
  <title>[S.I.E.S.] - Gestione Misura Sicurezza </title>
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
  
  
  function Verify()
  {
	  if (document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.value=='-')
      {
         alert('Il campo Tipo Misura è obbligatorio. Selezionare il campo Natura Misura');
         return false;
      }

	  return true;
  }

</script>

</head>
<body class="corpo" onload="javascript:caricaComboMod(strOggetto,';','#',document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>);">
 <table>
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
     MisuraSicurezzaModel lModel = new MisuraSicurezzaModel();
     //String lAzione = new String();
     if( modalita.equals("I") )
     {
%>
   		<font class="campo">Inserimento Misura Sicurezza</font>
<%   }
     else if( modalita.equals("M") )
     {
     	lModel = misurasicurezza;
%>
 		<font class="campo">Modifica Misura Sicurezza</font>
<%   
	 }
%>
   </td>
    
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      	 
  </tr>
</table>

<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<table width="90%">
  	<tr>
    	<td class=l width="25%">Posizione Giuridica</td>
    	<td class=l colspan=5><font class="campo"><%=PosizioneGiuridicaLuogoAltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%></font></td>
  	</tr>


	<tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
	       <td class="l" width="25%">Data Decorrenza Pena</td>
	       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
	   }
	
	   if (penaresidua.getFlagErgastolo() != null)
	   {
	        if(penaresidua.getFlagErgastolo().equals("S"))
	        {
%>
	          <td class="l">Pena Detentiva</td>
	          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	        }
	        else
	        if(penaresidua.getFlagErgastolo().equals("D"))
	        {
%>
	          <td class="l">Pena Detentiva</td>
	          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
	        }
	   }

	   if (penaresidua.getFlagErgastolo() != null && (penaresidua.getFlagErgastolo().equals("S") || penaresidua.getFlagErgastolo().equals("D")))
	   {
%>
	       <td class="l">Data Fine Pena</td>
	       <td class="lRosso"> <font class="lRosso">MAI</font></td>
<%	   }else 
	   if( penaresidua.getDataFine() != null)
	   {
%>
	       <td class="l">Data Fine Pena</td>
	       <td class="L" colspan=2>
	       	   <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy"))%></font>
	       </td>
<%}%>
	</tr>
</table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraSicurezza">
  
 <table cellspacing=2 cellpadding=2>
  <tr>
        <td class="l">Natura Misura</td>
        <td class="l">
          <select title="Natura Misura" name="<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>" onChange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>);">
          	<%=naturaMisuraSicurezza%>
          </select>
        </td>
  <tr>
        <td class="l">Tipo Misura  <font class="ob">(*)</font></td>
        <td class="l">
          <select title="Tipo Misura" name="<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>">
          	<!--  %=tipoMisuraSicurezza% -->
          </select>
        </td>
  </tr>
  <tr>
        <td class="l">Durata Misura</td>

        <td class="l">Anni
          <input title="Anni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumAnni()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_ANNI %>"  >
          Mesi
          <input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumMesi()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>"  >
          Giorni
          <input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumGiorni()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI %>"  >
        </td>
  </tr>
  <tr>
      <td colspan=2>
        <input type="submit" value="Conferma" class="bottone" name="Inserisci">
      </td>
  </tr>

</table>
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciMisuraSicurezza">
   <input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=lModel.getIdMisuraSicurezza()%>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=idfascicolo%>">
   <input type="HIDDEN" name="lTipoFun" value="<%=lTipoFunzione%>">

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciMisuraSicurezza");
    frmvalidator.addValidation("<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

</body>
</html>