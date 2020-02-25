<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel" %>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="misurasicurezza" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>

<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="idfascicolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.util.Vector"/>


<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;

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
    
 // Titolo esecutivo Associato alla Misura
 	Boolean DatiTitolo=false;
 	String lDati="";
 	String TipoUff="";
 	String DesComUff="";
 	RiferimentoFascicoloSiepModel lRifMod = null;
 	if(modalita.compareTo("M")==0)
	{ 	
	 	if(misurasicurezza!=null && misurasicurezza.getIdMisuraSicurezza()!=null)
	 	{
	 		if(misurasicurezza.getRiferimentoFascicoloSiep()!=null && 
	 			misurasicurezza.getRiferimentoFascicoloSiep().getIdRiferimentoFascicoloSiep() != null )
	 		{
	 			DatiTitolo=true;
	 			lRifMod = misurasicurezza.getRiferimentoFascicoloSiep();
	 			
	 			lDati+= lRifMod.getDescrTipoProvvedimento()+" N."+lRifMod.getNumeroProvvedimento()+"/"+lRifMod.getAnnoProvvedimento()+" ";
	 			lDati+= lRifMod.getDescrTipoAutoritaEmittente()+ " di "+lRifMod.getDescrLuogoEmittente()+" del "+DateUtils.getDateToString(lRifMod.getDataProvvedimento(),"dd-MM-yyyy")+" ";
	 			
	 			if(lRifMod.getFlagMS()!= null && lRifMod.getFlagMS().compareTo("M")==0)
	 				lDati+="(Proc. ESECUZIONE M.S. N.";
	 			else if(lRifMod.getFlagMS()!= null && lRifMod.getFlagMS().compareTo("N")==0)
	 				lDati+="(Proc. SIEP N.";
	 			else
	 				lDati+="(Procedimento N.";	
	 				
	 			lDati+=lRifMod.getProgrFascicoloSiep()+"/"+lRifMod.getAnnoFascicoloSiep();
	 			
	 			if(lRifMod.getCodUffFascicoloSiep()!=null)
	 			{
	 				DesComUff = (UfficioUtils.getUfficioByCodUfficio(lRifMod.getCodUffFascicoloSiep())).getDescrComune();
	 				TipoUff = (UfficioUtils.getUfficioByCodUfficio(lRifMod.getCodUffFascicoloSiep())).getCodTipoUfficio();
	 				lDati+=" "+TipoUff+" "+DesComUff;
	 			}
	 			
	 			lDati+=")";
	 			
	 		}
	 	}
	}	 	

%>

<html>
<head>
  <title>[S.I.E.S.] - GestioneMisuraSicurezza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
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
	  var retValue = true;
      if (document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.value=='-')
      {
        alert('Il campo Tipo Misura è obbligatorio');
        return false;
      }
      
      if(document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value.length > 0)
      {	
    	   
		  	if (document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value.length==1)
			  	document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value='0'+document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value;
		  	if (document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_FINE_VALIDITA%>.value.length==1)
			  	document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_FINE_VALIDITA%>.value='0'+document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_FINE_VALIDITA%>.value;
	
		  	var data_to_verify = document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;
	
	    	if (!ControllaData(data_to_verify) )
		  	{
	    		alert('Data Fine Validità Errata');
	    		document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
			   	return false;
		  	}
	    	
	    	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	    	
	    	retValue = confirm("Attenzione: valorizzando la Data Fine Validità, la misura di sicurezza \n non sarà considerata valida per il procedimento!");

            if(retValue)
            {	
		    	// Controllo : data di sistema deve essere >= Data Fine Validità .
			    if( !CompareDate( data_to_verify, data_sistema) )
			    {
		    	  	alert('Data Fine Validità non può essere superiore alla data odierna!');
		      		document.LoadInserisciMisuraSicurezza.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
		      		return false;
		    	}
            }
            else
            {
            	return false;
            }	
      }	

     InserisciAggiorna();
  }

  </script>

<script language="JavaScript">
<%if(!lTipoFunzione.equals(""))
{%>
   function  Fine()
    {
       document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=idfascicolo%>";
       document.LoadInserisciMisuraSicurezza.F.disabled=true;
       document.LoadInserisciMisuraSicurezza.Inserisci.disabled=true;

    }
<%}%>

   function  InserisciAggiorna()
    {

<%      if( modalita.equals("I") )
        {
       	  if( modoSIGE) {
%>      		  
           	document.LoadInserisciMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.misurasicurezza.action.ActInserisciMisuraSicurezzaSige";
 <% 	  }else{ %>
          	document.LoadInserisciMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.misurasicurezza.action.ActInserisciMisuraSicurezza";
<%        }
        } 
		else if( modalita.equals("M") )
        {
			if( modoSIGE) {
%>				
		    document.LoadInserisciMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.misurasicurezza.action.ActModificaMisuraSicurezzaSige";
<% 	  }else{ %>
          	document.LoadInserisciMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.misurasicurezza.action.ActModificaMisuraSicurezza";
 <%     }}
%>
<%if(!lTipoFunzione.equals(""))
{%>
       document.LoadInserisciMisuraSicurezza.F.disabled=true;
<%}%>
       document.LoadInserisciMisuraSicurezza.Inserisci.disabled=true;
    }
   
</script>
</head>
<body class="corpo" onload="javascript:caricaComboMod(strOggetto,';','#',document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciMisuraSicurezza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>);">
 <table>
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
     MisuraSicurezzaModel lModel = new MisuraSicurezzaModel();
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
<%   }%>
    </td>
  </tr>
</table>

	<br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraSicurezza">
  
 <table cellspacing=2 cellpadding=2>
 <% if(modalita.compareTo("M")==0)
	{ %>
 	<tr>
		<td class="l"><font class="label" style="font-size: 10pt"> Titolo Esecutivo Associato: </font></td> 
		<td class="l"  >&nbsp;
<%		if(DatiTitolo)
		{%>	
			<input type="text" maxlength="135" size="135" name="titolo_associato" title="titolo_associato" value="<%=lDati%>" readOnly>
<%		}
		else
		{%>
			<font class="campo">Titolo del Procedimento </font>
<%		} %>							 
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%	} %>			  
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
          &nbsp;<input title="Anni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumAnni()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_ANNI %>"  >
          Mesi
          &nbsp;<input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumMesi()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>"  >
          Giorni
          &nbsp;<input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumGiorni()) %>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI %>"  >
        </td>
  </tr>
 
		<tr>
        <td class="l">Data Fine Validità </td>
        <td class="L" colspan=2 >
          <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lModel.getDataFineValidita(),"dd")) %>"  
          	name="<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_FINE_VALIDITA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lModel.getDataFineValidita(),"MM")) %>" 
          	name="<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_FINE_VALIDITA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lModel.getDataFineValidita(),"yyyy")) %>"
          	name="<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>

      </tr>
  <tr>
      <td colspan=2>
        <input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      </td>

<%if(!lTipoFunzione.equals(""))
  {%>
      <td colspan=2>
        <input type="button"  class="bottone"  name="F" value="  Fine  " onClick="javascript:Fine();">
      </td>
<%}%>


  </tr>
</table>
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
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