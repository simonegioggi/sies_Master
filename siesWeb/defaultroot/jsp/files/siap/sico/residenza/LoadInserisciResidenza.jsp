<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="residenza" scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

  <head>
    <title>[S.I.E.S.] - Inserisci Residenza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
    <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	  <script language="JavaScript" src="/html/ControllaData.js"></script>
	  <script language="JavaScript">
	  
	  function FocusMask()
   	  {
   	  	document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_INDIRIZZO%>.focus();
   	  }
   	  
      function Verify()
      {
        if ((document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_INDIRIZZO %>.value.length!=0  ) &&
        (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039' && (document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value=="-" )))
        {
			document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE%>.focus();
          	alert('Il campo Luogo  è obbligatorio');
         	return false;
        }

		if ((document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_INDIRIZZO %>.value.length!=0  )&&
        (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039' && (document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value.length==0 || document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value=="-" )))
        {
        	document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
          	alert('Il campo Comune Estero  è obbligatorio');
          	return false;
        }

 		if ((document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value=="-" ) &&
        (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039'))
        {
        	document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE%>.focus();
          	alert('Il campo Luogo è obbligatorio');
          	return false;
        }

      	if (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='-')
        {
        	document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_COD_STATO%>.focus();
          	alert('Il campo Stato è obbligatorio');
          	return false;
        }
        
      	if ( (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
        &&( document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value!="")
        && document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value!="-")
      	{
	       document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.focus();
	       alert('Per Stato Estero specificare solo il Comune Estero non il Luogo');
	       return false;
      	}
      	      
      	if ( (document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenza.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039')
      	&&( document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value!=""))
     	{
     		document.LoadInserisciResidenza.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
       		alert('Specificare Comune Estero solo per Stato Estero');
	        return false;
      	}
      	Residenza();
    }

  </script>
<script language="JavaScript">
<%if(!lTipoFunzione.equals(""))
{%>
   function  Domicilio()
    {

       document.LoadInserisciResidenza.D.disabled=true;
       document.LoadInserisciResidenza.R.disabled=true;
       document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActLoadInserisciDomicilio&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=request.getParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO)%>";
    }
<%}%>
   function  Residenza()
    {

<%      if( modalita.equals("I") )
        {%>
          document.LoadInserisciResidenza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.residenza.action.ActInserisciResidenza";
<%      }
        else if( modalita.equals("M") )
        {%>
          document.LoadInserisciResidenza.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.residenza.action.ActModificaResidenza";
 <%     }
%>
<%if(!lTipoFunzione.equals(""))
{%>
       document.LoadInserisciResidenza.D.disabled=true;
<%}%>
       document.LoadInserisciResidenza.R.disabled=true;
 }

      function cancellaCodComuneReale() {
      
      	document.LoadInserisciResidenza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }
  </script>
  </head>
  
  <body class="corpo" onLoad="FocusMask();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;
<%
        ResidenzaModel lResidenza = new ResidenzaModel();
        //String lAction = new String();

        if( modalita.equals("I") )
        {
          //lAction = "siap.sico.residenza.action.ActInserisciResidenza";
%>
          <font class="campo">Inserimento Residenza</font>
<%
        }
        else if( modalita.equals("M") )
        {
          //lAction = "siap.sico.residenza.action.ActModificaResidenza";
          lResidenza = residenza;
%>
          <font class="campo">Modifica residenza</font>
<%
        }
%>
      </td>
    </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/sico/soggetto/DettaglioSoggettoAssociato.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciResidenza">

  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Indirizzo</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>" type="text" name="<%=ICostantiResidenza.CAMPO_INDIRIZZO%>" maxlength="100" size="50">
      </td>
		</tr>
		<tr>
      <td class="l">Cap</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getCap())%>" Title="CAP" type="text" name="<%=ICostantiResidenza.CAMPO_CAP%>" maxlength="5" size="5">
      </td>
		</tr>
		<tr>
      <td class="l">Luogo<font class="ob">(*)</font></td>
      <td class="l">
      <input Title="Luogo" name="<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>" value="<%=lResidenza.getDescrComune()%>" type="text" maxlength="35" size="35" onChange="cancellaCodComuneReale();">
      <a href="Javascript:ListaComuni('LoadInserisciResidenza','<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
      </td>
		</tr>
    	<tr>
       <td class="l">Comune Estero</td>
       <td class="l">
       <input Title="Comune Estero" name="<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>" type="text" maxlength="200" size="35">
       </td>
		</tr>
		<tr>
      <td class="l">Stato</td>
      <td class="l">
        <select title="Stato" name="<%=ICostantiResidenza.CAMPO_COD_STATO%>">
          <%=nazioni%>
        </select>
      </td>
 		</tr>
    <tr>
      <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Conferma">
      </td>
<%if(!lTipoFunzione.equals(""))
{%>
      <td colspan=2>
        <br>
        <input type="button"  class="bottone"  name="D" value="Prosegui" onClick="Javascript:Domicilio();">
      </td>
<%}%>
    </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" value="<%=lResidenza.getIdResidenza()%>">

<%if( modalita.equals("I") )
        {%>
  <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=request.getParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO)%>">
<%      }
        else if( modalita.equals("M") )
        {%>
  <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=lResidenza.getSogIdSoggetto()%>">
 <%     }
%>
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
  <input type="HIDDEN" name="<%=ICostantiResidenza.CAMPO_COD_TIPO_RESIDENZA%>" value="R">
  <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

<br>
  <jsp:include page="/jsp/files/siap/sico/residenza/IncludeElencoResidenze.jsp"/>
<br>  
    
  </form>
    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadInserisciResidenza");

      frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>","numeric");
      frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>","minlen=5","La lunghezza minima per il CAP è di 5 caratteri");
     frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>