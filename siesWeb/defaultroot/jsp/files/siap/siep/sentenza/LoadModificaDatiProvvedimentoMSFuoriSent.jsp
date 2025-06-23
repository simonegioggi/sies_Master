<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>

<jsp:useBean id="sentenza" 		scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito"		 	scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="tipoProvvedimenti" scope="request" class="java.lang.String" />
<jsp:useBean id="Fascicolo_di" 		scope="request" class="java.lang.String" />

<!-- 	LoadModificaDatiProvvedimentoMSFuorSent  -->

<%	SentenzaModel lSentenza = new SentenzaModel();
	lSentenza = new SentenzaModel(sentenza);

	String lAction = new String();
 	lAction = "siap.siep.sentenza.action.ActModificaDatiProvvedimentoMSFuoriSent";
 	
 	String lTitolo="";
 	if(Fascicolo_di.compareTo("FS")==0)
 		lTitolo="Modifica Estremi Provvedimento - Fascicolo di Misura Sicurezza disposta fuori sentenza";
 	else if(Fascicolo_di.compareTo("MP")==0)
 		lTitolo="Modifica Estremi Provvedimento - Fascicolo di Misura Sicurezza Provvisoria";
 	else
 		lTitolo="Modifica Estremi Provvedimento - Fascicolo di Misura Sicurezza";
 	
// 	
	String ARG = "";
	String NRG = "";
	String Tipo = "";
	if (lSentenza.getAnnoRegeCap() != null) {
		ARG = lSentenza.getAnnoRegeCap() + "";
		NRG = lSentenza.getNumeroRegeCap() + "";
		Tipo = "cap";
	}
	if (lSentenza.getAnnoRegeCas() != null) {
		ARG = lSentenza.getAnnoRegeCas() + "";
		NRG = lSentenza.getNumeroRegeCas() + "";
		Tipo = "cas";
	}
	if (lSentenza.getAnnoRegeDib() != null) {
		ARG = lSentenza.getAnnoRegeDib() + "";
		NRG = lSentenza.getNumeroRegeDib() + "";
		Tipo = "dib";
	}
	if (lSentenza.getAnnoRegeGip() != null) {
		ARG = lSentenza.getAnnoRegeGip() + "";
		NRG = lSentenza.getNumeroRegeGip() + "";
		Tipo = "gip";
	}
	if (lSentenza.getAnnoRegeCasap() != null) {
		ARG = lSentenza.getAnnoRegeCasap() + "";
		NRG = lSentenza.getNumeroRegeCasap() + "";
		Tipo = "casap";
	}
	// MEV_66: aggiunte quattro nuove proprietà
	if (lSentenza.getAnnoRegeGup() != null) {
		ARG = lSentenza.getAnnoRegeGup() + "";
		NRG = lSentenza.getNumeroRegeGup() + "";
		Tipo = "gup";
	}
	if (lSentenza.getAnnoRegeCapsm() != null) {
		ARG = lSentenza.getAnnoRegeCapsm() + "";
		NRG = lSentenza.getNumeroRegeCapsm() + "";
		Tipo = "capsm";
	}
%>

<head>
<title>[S.I.E.S.] - Gestione Decreto</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript">
        var desktop;
        function ListaComuni(a_formname,a_fieldname)
        {
        	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }

        // 09/06/2010 Lista Uffici per TIPO_UFFICIO
	    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
	    {
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	    }
        
	    function ctrl_autorita(idcmb1, idDiv, idTipoRito)
	    {
	    	
	    	// cmb1 è la combo che fa scattare la funzione
	    	var cmb1 = document.getElementById(idcmb1);	
	    	var node = document.getElementById(idDiv);
	    	var cmbRito = document.getElementById(idTipoRito);
	    	
	    	if (cmb1.value == "DIB" || cmb1.value == "TRIBSD") {
	    		
	    		node.style.visibility = "visible";
	    	}
	    	else 
	    	{
	    		
	    		node.style.visibility = "hidden";
	    		cmbRito.selectedIndex = 0;		
	    	}
	    }
	    
    function Verify()
    {  
    	// Decreto/ordinanza
	   	if(document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value == "-" )
		{
	        alert('Tipo Provvedimento non valido');
	        document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
	        return false;	   		
		}	
	   	
    	// data provvedimento
	      if (document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
	        document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
	      if (document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
	        document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
	      
	      var d2=document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
	      if (! ControllaData(d2))
	      {	
	    	  alert('Data Decreto non valida');
	          return false;
	      }
	     
	     // Anno e numero provvedimento 
		if(document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value=="" || document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value == "")
		{
	        alert('Anno e/o Numero Provvedimento Non ValidI');
	        document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.focus();
	        return false;					
		}
	     
		// Autorità
		if(document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-")
		{
	        alert('Tipo Autorità Emittente non valido');
	        document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
	        return false;				
		}
		
		if(document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>.value == "")
		{
	        alert('Luogo Autorità Emittente non valido');
	        document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
	        return false;				
		}
	
	// Anno, Numero e tipo Reg.Gen.		
		if(document.ModProvvFuoriSent.ARG.value=="" || document.ModProvvFuoriSent.NRG.value == "")
		{
	        alert('Anno e/o Numero Reg. Gen. Non ValidI');
	        document.ModProvvFuoriSent.ARG.focus();
	        return false;					
		}

		if(document.ModProvvFuoriSent.TipoRG.value=="-" )
		{
	        alert('Tipo Reg. Gen. Non Valido');
	        document.ModProvvFuoriSent.TipoRG.focus();
	        return false;					
		}
			
	      return true;
    }
      
	</script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> </a>
		</td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; 
		 <font class="campo"><%=lTitolo%></font> 
		</td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name="ModProvvFuoriSent">

<table cellspacing=2 cellpadding=2>

	<tr>
	
	</tr>

	<tr>
		
	</tr>

	<tr>
		<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
		<td class="L">
			<select Title="Tipo Prevvedimento Riferimento" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
				<%=tipoProvvedimenti%>
			</select>
		</td>
	</tr>
	
	<tr>
		<td class="l">Data Provvedimento <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Data Decreto" type="text"	value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" 
				name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" 
				name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>"
				name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Provvedimento <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno Decreto" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" 
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Decreto" value="<%=lSentenza.getNumeroSentenza() %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<select Title="Autorità Emittente" onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', 'D1', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>');" 
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"> 
				<%=autoritaEmi%>
			</select>
		</td>
		<td colspan=2>
		<%
			String visib1 = new String("hidden");
			if (lSentenza.getCodTipoAutoritaEmittente().equals("DIB") || 
				lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD")) 
			{
				visib1 = "visible";
			}
		%>
		<div id=D1 STYLE="visibility: <%=visib1%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>">
					<%=tipoRito%>
				</select></td>
			</tr>
		</table>
		</div>
		</td>		
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35"> 
        <a href="Javascript:ListaUfficiPerTipo('ModProvvFuoriSent','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.ModProvvFuoriSent.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.ModProvvFuoriSent.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
					<img src="/images/filefolder.gif" border=0>
				</a>
			</td>
	</tr>

<!-- 																											 -->

<%if(Fascicolo_di.compareTo("MP")==0 )
  { %>
  	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=3>
			<input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30">
		</td>
	</tr>
  
  	<tr>
		<td class="l">Sede PM </td>		
		<td class="L">
			<input Title="Sede PM" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>" value="<%=StringUtils.toStringJSP(lSentenza.getDescrSedeNotiziaReato() )%>" type="text" maxlength="35" size="35">
      		<a href="Javascript:ListaUfficiPerTipo('ModProvvFuoriSent','<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>','PM');">
			<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
  
	<tr>
		<td class="l">Anno/Numero R.G.N.R.<font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Re.Ge. PM"
			value="<%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm()) %>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Re.Ge. PM"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%>" type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>" maxlength="6"
			size="6">
		</td>
		<td class="l"></td>
		<td class="L"></td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Reg.Gen. <font class=ob>(*)</font></td>
		<td class="L">
			<input Title="Anno Reg.Gen." value="<%=ARG%>" type="text" name="ARG" maxlength="4" size="4"> /
			<input Title="Numero Reg.Gen." value="<%=NRG%>" type="text" name="NRG" maxlength="6" size="6"> &nbsp; 
			<select name="TipoRG">
				<option value="-">-</option>
			<%	String sel = "";
				if (Tipo.equals("gip"))
					sel = " selected";
			%> 
				<option value="gip" <%=sel%>>GIP</option>
			<%	sel = "";
				if (Tipo.equals("dib"))
					sel = " selected";
			%> 				
				<option value="dib" <%=sel%>>DIB</option>
			<%	sel = "";
				if (Tipo.equals("cas"))
					sel = " selected";
			%> 				
				<option value="cas" <%=sel%>>CAS</option>
			<%	sel = "";
				if (Tipo.equals("cap"))
					sel = " selected";
			%> 				
				<option value="cap" <%=sel%>>CAP</option>
			<%	sel = "";
				if (Tipo.equals("casap"))
					sel = " selected";
			%> 				
				<option value="casap" <%=sel%>>CASAP</option>
	
			</select>
		</td>
	</tr>

<% } %>
	<!---------------------------------------------------------->
	
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
		</td>
	</tr>
	<tr>
		<td colspan=2><br>
		<INPUT onclick="Javascript:return Verify();" class="bottone"
			type="submit" name="INSERISCI" value="Conferma"></td>
	</tr>
</table>

	<input type="HIDDEN" name="Action" value="<%=lAction%>">
	<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>" value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
	<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>">
</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("ModProvvFuoriSent");
  
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=2999");

  <%if(Fascicolo_di.compareTo("MP")==0 )
  { %>
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>","req","Il campo Anno R.G.N.R. è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","lt=2999");
	
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>","req","Il campo Numero R.G.N.R. è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");
<% } %>	  

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>","req","Il campo Anno Provvedimento è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il campo Numero Provvedimento è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","dontselect=0");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
<%--   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic"); --%>

      </script>
</body>
</html>