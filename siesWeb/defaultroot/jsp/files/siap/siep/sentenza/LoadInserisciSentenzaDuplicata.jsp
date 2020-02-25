<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String" />
<jsp:useBean id="flagSN" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimenti" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiAltro" scope="request" class="java.lang.String" />

<head>
<title>[S.I.E.S.] - Gestione Sentenza</title>
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
    </script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	

    function Verify()
    {
    	var CodTipoProvvAltro= document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value;

      	var TipoSentRif = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value;
	    var GGSentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.value;
	    var MMSentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.value;
	    var AASentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.value;
	    var TipoAutRif  = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;
	    var SedeRif     = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.value;
	    var CodTipoProvv= document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value;

		if(TipoSentRif != '-' || GGSentRif != '' || MMSentRif != '' || AASentRif != '' || 
		   TipoAutRif != '-' || SedeRif != '-' || CodTipoProvv != '-')
    	{

		      if(TipoSentRif == '-')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Tipo Sentenza');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
			        return false;
		      }
		      if(GGSentRif == '')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Giorno Sentenza');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.focus();
			        return false;
		      }
		      if(MMSentRif == '')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Mese Sentenza');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.focus();
			        return false;
		      }
		      if(AASentRif == '')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Anno Sentenza');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.focus();
			        return false;
		      }
		      if(TipoAutRif == '-')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Autorità Emittente');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();
			        return false;
		      }
		      if(SedeRif == '')
		      {
			        alert('Dati della Sentenza di Riferimento Incompleti - Luogo Autorità Emittente');
			        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.focus();
			        return false;
		      }

			       //Data sentenza di riferimento		      
		      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
          			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
        	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
          			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value;

		      var d1=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
		      if (! ControllaData(d1))
		      {
			          alert('Data sentenza di riferimento non valida ');
			          return false;
		      }
	    }

		var d2=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
		var auEmi = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
		var dRif=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value
	      	+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value
	      	+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
	
		var gRif = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	
		if(gRif!=null && gRif!='')
		{
			if( auEmi=="CAP"   || 
				auEmi=="CASAP" || 
				auEmi=="CAPSM" ||
				auEmi=="CAPMID" || 
				auEmi=="CAPMI")
			{
					if (CompareDate(d2,dRif))
					{
				        	alert('La Data Sentenza deve essere successiva alla Data della Sentenza di grado differente');
				        	return false;
			      	}
			}
			else
			{
					if (CompareDate(dRif,d2))
					{
			        		alert('La Data della Sentenza di grado differente deve essere successiva alla Data Sentenza');
			        		return false;
			      	}
			}
		}

    }
    
	function ctrl_autorita(idcmb1, idcmb2, idDiv, idTipoRito) 
	{
	
		// cmb1 è la combo che fa scattare la funzione (in questa pagina è sempre CAMPO_COD_TIPO_AUTORITA_PROVV_RIF)
		var cmb1 = document.getElementById(idcmb1);	
		
		// in questa pagina cmb2 è il campo Hidden CAMPO_COD_TIPO_AUTORITA_EMITTENTE
		var cmb2 = document.getElementById(idcmb2);
			
		var arrCmb = new Array(cmb1, cmb2);
		var arrGrado = new Array();
	
 		for(var i=0;i<arrCmb.length;i++) 
 		{

				if (arrCmb[i].value == "CSS") 
				{
					
					arrGrado[i] = 3;
				}
				else if (arrCmb[i].value == "CAP" || arrCmb[i].value == "CASAP" || arrCmb[i].value == "CAPMI" ||
				         arrCmb[i].value == "CAPSM" || arrCmb[i].value == "CAPMID") 
				{
					
					arrGrado[i] = 2;
				}
				else 
				{
					arrGrado[i] = 1;
				}
		}		
 	
	 	if (cmb1.value != "-" && cmb2.value != "-") 
	 	{
			if (cmb1.value == cmb2.value) 
			{
				alert("Non è consentito selezionare due Autorità Emittenti uguali!");
				cmb1.selectedIndex = 0;
				cmb1.focus();
			}
			else if (arrGrado[0] == arrGrado[1]) 
			{			
				// eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
				if (!(cmb1.value == "GP" && (cmb2.value == "DIB" || cmb2.value == "TRIBSD")) 
				 && !(cmb2.value == "GP" && (cmb1.value == "DIB" || cmb1.value == "TRIBSD"))) 
				 {
					alert("Non è consentito selezionare due Autorità Emittenti dello stesso grado!");
					cmb1.selectedIndex = 0;
					cmb1.focus();
				}
			}
		}	
	
		var node = document.getElementById(idDiv);
		var cmbRito = document.getElementById(idTipoRito);
		
		if (cmb1.value == "DIB" || cmb1.value == "TRIBSD") 
		{
			
			node.style.visibility = "visible";
		}
		else 
		{
			
			node.style.visibility = "hidden";
			cmbRito.selectedIndex = 0;		
		}
	
	}

	function viewDiv(idDiv, aForm, aField, valueHidden){
		var node = document.getElementById(idDiv);
	    var valF = eval('document.'+aForm+'.'+aField+'.value');
	   
	    if(idDiv=='cassazione'){	    
			if (valF==valueHidden) {
				node.style.display = "none";
				document.getElementById('tipoSentenza').style.display="none";				
				document.getElementById('labelSentenza').style.display="none";
				document.getElementById('anSentenza').style.display="none";				
				document.getElementById('labelOrdinanza').style.display="block";
				document.getElementById('anOrdinanza').style.display="block";
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value=""');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
				eval('document.'+aForm+'.ANNOREGECAS.value=""');
				eval('document.'+aForm+'.NUMREGECAS.value=""');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value="-"');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value="-"');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>.value=""');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>.value=""');
			}
			else {
				node.style.display = "block";
				document.getElementById('tipoSentenza').style.display="block";
				document.getElementById('labelSentenza').style.display="block";
				document.getElementById('anSentenza').style.display="block";
				document.getElementById('labelOrdinanza').style.display="none";
				document.getElementById('anOrdinanza').style.display="none";				
			}
		}
		else if(idDiv=='0'){
			if (valF==valueHidden) {
				document.getElementById('anRegGen').style.display="none";				
				document.getElementById('anRacGen').style.display="none";				
				document.getElementById('disp').style.display="none";				
				document.getElementById('lblSentenza').style.display="none";
				document.getElementById('lblOrdinanza').style.display="block";
				//PULISCO I CAMPI
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
				eval('document.'+aForm+'.ANNOREGECAS.value=""');
				eval('document.'+aForm+'.NUMREGECAS.value=""');			
			}
			else {
				document.getElementById('anRegGen').style.display="block";
				document.getElementById('anRacGen').style.display="block";
				document.getElementById('disp').style.display="block";
				document.getElementById('lblSentenza').style.display="block";
				document.getElementById('lblOrdinanza').style.display="none";
			}
		}
	}
    
    function ctrlDiv(){
		if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value=='53'){
			viewDiv('cassazione', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>','53')
		}
		if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value=='53'){
			viewDiv('0', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>','53')
		}
	}
    </script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; 
<%
 	SentenzaModel lSentenza = new SentenzaModel();
 	String lAction = new String();

 // 	lAction = "siap.siep.sentenza.action.ActInserisciSentenza";
 	lSentenza = new SentenzaModel(sentenza);
 %> 
 		<font class="campo">Inserimento Sentenza con Copia</font>
		<td class="LBG"><a href="Javascript:history.go(-1);"> <img
			align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif"
			alt="ritorna su" width="24" height="24" border="0"> </a></td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenza">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sentenza.action.ActInserisciSentenza">
	<table cellspacing=2 cellpadding=2>		
		<tr>
			<td class="l">Numero R.G.N.R.</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>&nbsp;
			/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getNumeroRegePm())%></font>&nbsp;</td>
		</tr>
	
		<tr>
		<%
			String reg = "";
			String anno_reg = "";
			String num_reg = "";
			if (lSentenza.getAnnoRegeCap() != null) {
				reg = "CAP";
				anno_reg = lSentenza.getAnnoRegeCap() + "";
				num_reg = lSentenza.getNumeroRegeCap() + "";
			}
			if (lSentenza.getAnnoRegeCas() != null) {
				reg = "CAS";
				anno_reg = lSentenza.getAnnoRegeCas() + "";
				num_reg = lSentenza.getNumeroRegeCas() + "";
			}
			if (lSentenza.getAnnoRegeDib() != null) {
				reg = "DIB";
				anno_reg = lSentenza.getAnnoRegeDib() + "";
				num_reg = lSentenza.getNumeroRegeDib() + "";
			}
			if (lSentenza.getAnnoRegeCasap() != null) {
				reg = "CASAP";
				anno_reg = lSentenza.getAnnoRegeCasap() + "";
				num_reg = lSentenza.getNumeroRegeCasap() + "";
			}
			if (lSentenza.getAnnoRegeGip() != null) {
				reg = "GIP";
				anno_reg = lSentenza.getAnnoRegeGip() + "";
				num_reg = lSentenza.getNumeroRegeGip() + "";
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (lSentenza.getAnnoRegeGup() != null) {
				reg = "GUP";
				anno_reg = lSentenza.getAnnoRegeGup() + "";
				num_reg = lSentenza.getNumeroRegeGup() + "";
			}
			if (lSentenza.getAnnoRegeCapsm() != null) {
				reg = "CAPSM";
				anno_reg = lSentenza.getAnnoRegeCapsm() + "";
				num_reg = lSentenza.getNumeroRegeCapsm() + "";
		 	}

			if (!reg.equals("")) {
		%>

		<td class="l">Numero Reg.Gen.</td>
		<td class="L"><font class="campo"> <%=anno_reg%> / <%=num_reg%>&nbsp;&nbsp;&nbsp;
		<%=reg%></font></td>
		<td class="l"></td>
		<td class="L"></td>
		<%
		}
		%>
	</tr>
	<tr>
		<td class="l">Sede PM</td>		
		<td class="L">
			<font class="campo"><%=lSentenza.getDescrSedeNotiziaReato()%></font>
		</td>
	</tr>
	<tr>
		<td class="Titolo" colspan=4>Sentenza da Eseguire</td>
	</tr>
	<tr>
		<td class="l">Data Sentenza</td>
		<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getAnnoSentenza())%></font>&nbsp; / <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrTipoAutoritaEmittente())%></font>&nbsp;</td>
	</tr>
	<%
				if (lSentenza.getCodTipoAutoritaEmittente().equals("DIB")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
	%>
	<tr>
		<td class="l">Tipo Rito</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
										.getDescrTipoRito())%></font>&nbsp;</td>
	</tr>
	<%
	}
	%>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrLuogoEmittente())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getNumSezioneAutoritaEmittente())%></font>&nbsp;</td>
	</tr>	
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
		</td>
	</tr>
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Altro Grado di Giudizio</td>
	</tr>	
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<select Title="Tipo Prevvedimento Riferimento"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>"
				OnChange="viewDiv(
					'cassazione', 
					'LoadInserisciSentenza', 
					'<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>',
					'53')">
				<%=tipoProvvedimenti%>
			</select>
		</td>
	</tr>
	<tr id="tipoSentenza" style="display:block">
		<td class="l">Tipo Sentenza</td>
		<td class="L"><select Title="Tipo Sentenza Riferimento"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
			<%=tipoProvvedimentiRif%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Data <div id="labelSentenza">Sentenza</div><div id="labelOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L"><input Title="Giorno Data Sentenza di Riferimento"
			type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Mese Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Anno Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
		<td class="l">Anno/Numero <div id="anSentenza">Sentenza</div><div id="anOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L"><input Title="Anno Sentenza Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getAnnoProvvRif())%>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Sentenza Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroProvvRif() )%>"
			type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF %>"
			maxlength="6" size="6"></td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L"><select Title="Autorità Sentenza Riferimento"
			onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>', '<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', 'D2', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>');"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>">
			<%=autoritaProvRif%>
		</select></td>
		<td colspan=2>
		<%
			String visib2 = new String("hidden");
			if ((lSentenza.getCodTipoAutoritaProvvRif().equals("DIB") || lSentenza
					.getCodTipoAutoritaProvvRif().equals("TRIBSD"))) {

				visib2 = "visible";
			}
		%>
		<div id=D2 STYLE="visibility: <%=visib2%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito Riferimento"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>">
					<%=tipoRito2%>
				</select></td>
			</tr>
		</table>
		</div>
	</tr>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L"><input Title="Luogo Sentenza Riferimento" value="<%=lSentenza.getDescrLuogoProvvRif() %>" type="text" name="<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>" maxlength="35" size="35"> 
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L"><input Title="Sezione Autorità Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaProvvRif()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF %>"
			maxlength="100" size="35"></td>
	</tr>
	</table>
	<!---------------------------DECISIONE CASSAZIONE------------------------------->
	<div id="cassazione" style="display:block">
	<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
		<td class="Titolo" colspan=4>Decisione Cassazione</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<select Title="Tipo Prevvedimento Cassazione"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>"
				onChange="viewDiv(
					'0', 
					'LoadInserisciSentenza', 
					'<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>',
					'53')">
				<%=tipoProvvedimentiAltro%>
			</select>
		</td>
	</tr>
	<tr id="anRegGen">
		<td class="l">Anno/Numero Reg.Gen.</td>
		<td class="L">
		<input Title="Anno Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP( lSentenza.getNote1DecisioneCassazione())%>"
			type="text" name="ANNOREGECAS" maxlength="4" size="4" 
			onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / 
			<input Title="Numero Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP( lSentenza.getNote2DecisioneCassazione())%>"
			type="text" name="NUMREGECAS" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero
		<div id="lblSentenza">Sentenza</div><div id="lblOrdinanza" style="display:none">Ordinanza</div>
		</td>
		<td class="L"><input Title="Anno Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenzaCassazione())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / <input
			Title="Numero Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>"
			maxlength="6" size="6"></td>
	</tr>
	<tr id="anRacGen">
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L"><input Title="Anno Raccolta Generale"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / <input
			Title="Numero Raccolta Generale"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>"
			maxlength="6" size="6"></td>
	</tr>
	<tr id="disp">
		<td class="l">Dispositivo</td>
		<td class="L" colspan=3><select Title="Dispositivo Cassazione"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>">
			<%=tipoDecisioneCassazione%>
		</select></td>
		</table>
	</div>
	<table cellspacing=2 cellpadding=2>
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>"
	value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegePm())%>">
<input type="HIDDEN"
	name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>"
	value="<%=lSentenza.getNumeroRegePm()%>"> <%
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
 	<input type="HIDDEN" name="ARG" value="<%=ARG%>"> 
 	<input type="HIDDEN" name="NRG" value="<%=NRG%>"> 
 	<input type="HIDDEN" name="TipoRG" value="<%=Tipo%>"> 
 	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd"))%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>"
		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM"))%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy"))%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>"
		value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>"
		value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"
		value="<%=StringUtils.toStringJSP(lSentenza.getCodTipoAutoritaEmittente())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>"
		value="<%=StringUtils.toStringJSP(lSentenza.getCodTipoRito())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>"
		value="<%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>"
		value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente())%>">
	<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_NOTE %>" 
		value="<%=StringUtils.toStringJSP(lSentenza.getNote())%>"> 
	 
	<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>"
		value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
	<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>"> 
	<input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
	<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>" 
		value="<%=lSentenza.getDescrSedeNotiziaReato()%>">
	
</form>

<script language="JavaScript" type="text/javascript">
	//verifica iniziale per la sezione cassazione
	ctrlDiv();

	   var frmvalidator  = new Validator("LoadInserisciSentenza");
	   
	//   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","req","Il Giorno della Data sentenza di riferimento è obbligatorio");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","numeric");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","gt=1");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","lt=31");
	
	//   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","req","Il Mese della Data sentenza di riferimento è obbligatorio");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","numeric");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","gt=1");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","lt=12");
	
	//   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","req","L'anno della Data sentenza di riferimento è obbligatorio");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","maxlen=4","La lunghezza massima per l'anno della data Sentenza di riferimento è di 4 caratteri");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","gt=1900");

	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>","numeric");
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>","gt=1900");
	   
	   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>","numeric");
	
	//   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>","req","Il Luogo Sentenza Riferimento è obbligatorio");

    
      //if( lSentenza.isAltroGiudizio())
      //{

        //frmvalidator.addValidation("ANNOREGECAS","req","L'Anno Re.Ge. CASSAZIONE è obbligatorio");

      //}

      frmvalidator.addValidation("ANNOREGECAS","numeric");
      frmvalidator.addValidation("ANNOREGECAS","gt=1900");

      frmvalidator.addValidation("NUMREGECAS","numeric");

      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>","numeric");
      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>","gt=1900");

      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>","numeric");

      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>","numeric");
      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>","gt=1900");

      frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE%>","numeric");
	
		
	frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>