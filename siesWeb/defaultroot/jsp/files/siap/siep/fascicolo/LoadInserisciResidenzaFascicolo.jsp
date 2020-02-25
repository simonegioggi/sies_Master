<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="residenzaassociata" scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
  SoggettoModel lSoggetto = lFascicoloAssociato.getSoggetto();
  ResidenzaModel lResidenza = residenzaassociata.getResidenza();
  if(lResidenza == null)
    lResidenza = new ResidenzaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Residenza Fascicolo </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
 function insertIT(id, indirizzo, cap, comune, comestero, stato)
 {
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>.value=id;
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_INDIRIZZO%>.value=indirizzo;
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_CAP%>.value=cap;
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value=comune;
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.value=comestero;
   document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.value=stato;   
 }
  </script>
  	
<script language="JavaScript">
		function FocusMask()
		{
				document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_INDIRIZZO%>.focus();
		}
   	  
		var desktop;
		function ListaComuni(a_formname, a_fieldname)
		{
		  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=400, height=500");
		}

		function pulisciIdResidenza()
		{
		 		document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>.value = '0';
		}

		function Verify()
		{
				if ((document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_INDIRIZZO %>.value.length!=0  ) &&
				(document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039' && (document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value=="-" )))
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE%>.focus();
						alert('Il campo Luogo  è obbligatorio');
						return false;
				}

				if ((document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_INDIRIZZO %>.value.length!=0  ) &&
				(document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039' && (document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value.length==0 || document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value=="-" )))
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
						alert('Il campo Comune Estero  è obbligatorio');
						return false;
				}

				if ((document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value=="-" ) &&
				(document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039'))
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE%>.focus();
						alert('Il campo Luogo è obbligatorio');
						return false;
				}

				if (document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='-')
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_COD_STATO%>.focus();
						alert('Il campo Stato è obbligatorio');
						return false;
				}
 
				if ( (document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
				&&( document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value!="")
				&& document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value!="-")
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.focus();
						alert('Per Stato Estero specificare solo il Comune Estero non il Luogo');
						return false;
				}
      
				if ( (document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicolo.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039')
				&&( document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value!=""))
				{
						document.LoadInserisciResidenzaFascicolo.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
						alert('Specificare Comune Estero solo per Stato Estero');
						return false;
				}
		}
		
		function Deassocia()
		{
				document.LoadInserisciResidenzaFascicolo.Action.value="siap.siep.fascicolo.action.ActDeassociaResidenzaFascicolo";
		}
      
</script>
</head>

<body class="corpo" onLoad="FocusMask();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        //ResidenzaModel lResidenza = null;
        String lAction = new String();
        if( modalita.equals("I") )
        {
         lAction = "siap.siep.fascicolo.action.ActInserisciResidenzaFascicolo";
         //lResidenza = new ResidenzaModel();
%>
    	   <font class="campo">Inserimento Residenza Fascicolo</font>
<%
        }

        else if( modalita.equals("M") )
        {
          lAction = "siap.siep.fascicolo.action.ActModificaResidenzaFascicolo";
          //lResidenza = new ResidenzaModel(****);
%>         
		<font class="campo">Modifica Residenza Fascicolo</font> 
<%
        }
%>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciResidenzaFascicolo">
  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Indirizzo</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>" type="text" name="<%=ICostantiResidenza.CAMPO_INDIRIZZO%>" maxlength="100" size="50" onChange="pulisciIdResidenza()">
      </td>
		</tr>
		<tr>
      <td class="l">Cap</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getCap())%>" Title="CAP" type="text" name="<%=ICostantiResidenza.CAMPO_CAP%>" maxlength="5" size="5" onChange="pulisciIdResidenza()">
      </td>
		</tr>
		<tr>
      <td class="l">Luogo <font class="ob">(*)</font></td>
      <td class="l">
        <input Title="Luogo" name="<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>" value="<%=lResidenza.getDescrComune()%>" type="text" maxlength="35" size="35" onChange="pulisciIdResidenza()">
        <a href="Javascript:ListaComuni('LoadInserisciResidenzaFascicolo','<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
		</tr>
    	<tr>
       <td class="l">Comune Estero</td>
       <td class="l">
       <input Title="Comune Estero" name="<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>" type="text" maxlength="200" size="35" onChange="pulisciIdResidenza()">
       </td>
		</tr>
		<tr>
      <td class="l">Stato</td>
      <td class="l">
        <select title="Stato" name="<%=ICostantiResidenza.CAMPO_COD_STATO%>" onChange="pulisciIdResidenza()">
          <%=nazioni%>
        </select>
      </td>
 		</tr>
		<tr>
      		<td colspan="2">
        		<br>
        		<input type="HIDDEN" name="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" value="<%=StringUtils.toStringJSP(lResidenza.getIdResidenza(), "0")%>">
        		<input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=lSoggetto.getIdSoggetto()%>">
        		<input type="HIDDEN" name="Action" value="<%=lAction%>">
        		<input class="bottone"  type="submit" value="Conferma">
        		<input class="bottone"  type="submit" value="Deassocia" name="De" onclick="javascript:Deassocia();">
      		</td>
    	</tr>
  	</table>
  
<% if (residenze != null && residenze.size() > 0 )
 		{	
 %>	  
	   	<p class=cVerde> Lista Residenze Disponibili</p>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/IncludeElencoResidenzeFascicolo.jsp"/>
<%
	  }
%> 

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciResidenzaFascicolo");

  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "minlen=5", "La lunghezza minima per il CAP è di 5 caratteri");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>