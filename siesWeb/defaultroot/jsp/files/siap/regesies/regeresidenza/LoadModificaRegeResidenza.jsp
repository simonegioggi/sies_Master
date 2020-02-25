<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.regesies.regeresidenza.model.RegeResidenzaModel"%>
<%@ page import="siap.regesies.regeresidenza.action.ICostantiRegeResidenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="regeresidenza" scope="request" class="siap.regesies.regeresidenza.model.RegeResidenzaModel"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

  <head>
    <title>[S.I.E.S.] - Gestione Sentenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
    <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	  <script language="JavaScript">
   function Verify()
     {
        if ((document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_INDIRIZZO %>.value.length!=0  ) &&
        (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039' && (document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value=="-" )))
        {
          alert('Il campo Luogo è obbligatorio');
          return false;
        }


if ((document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_INDIRIZZO %>.value.length!=0  )&&
        (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039' && (document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value.length==0 || document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value=="-" )))
        {
          alert('Il campo Comune Estero  è obbligatorio');
          return false;
        }


 if ((document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value.length==0 || document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value=="-" ) &&
        (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039'))
        {
          alert('Il campo Luogo è obbligatorio');
          return false;
        }

 if (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='-')
        {
          alert('Il campo Stato è obbligatorio');
          return false;
        }

 if (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
        {
          document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value="";
        }

if ( (document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>[document.LoadModificaRegeResidenza.<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
      &&( document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value!=""))
      {
       document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESCR_COMUNE %>.value="";
       document.LoadModificaRegeResidenza.<%= ICostantiRegeResidenza.CAMPO_DESC_COMUNE_ESTERO %>.focus();
       alert('Per Stato Estero specificare solo il Comune Estero non il Luogo.');
       return false;
      }
      return true;
    }
  </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;
<%
        RegeResidenzaModel lResidenza = new RegeResidenzaModel(regeresidenza);
%>
          <font class="campo">Modifica Rege <%=lResidenza.getDescrTipoResidenza()%></font>
      </td>
    </tr>
  </table>
  <br>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
 <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaRegeResidenza">

  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Indirizzo</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>" type="text" name="<%=ICostantiRegeResidenza.CAMPO_INDIRIZZO%>" maxlength="100" size="50">
      </td>
		</tr>
		<tr>
      <td class="l">Cap</td>
      <td class="l">
        <%String lCap = lResidenza.getCap();
        if(lCap!=null && lCap.equals("-"))
           {lCap ="";}%>
        <input value="<%=lCap%>" Title="CAP" type="text" name="<%=ICostantiRegeResidenza.CAMPO_CAP%>" maxlength="5" size="5">
      </td>
		</tr>
		<tr>
      <td class="l">Luogo<font class="ob">(*)</font></td>
      <td class="l">
      <input Title="Luogo" name="<%=ICostantiRegeResidenza.CAMPO_DESCR_COMUNE%>" value="<%=lResidenza.getDescrComune()%>" type="text" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadModificaRegeResidenza','<%=ICostantiRegeResidenza.CAMPO_DESCR_COMUNE%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
      </td>
		</tr>
    	<tr>
       <td class="l">Comune Estero</font></td>
       <td class="l">
       <input Title="Comune Estero" name="<%=ICostantiRegeResidenza.CAMPO_DESC_COMUNE_ESTERO%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>" type="text" maxlength="200" size="35">
       </td>
		</tr>
		<tr>
      <td class="l">Stato</td>
      <td class="l">
        <select title="Stato" name="<%=ICostantiRegeResidenza.CAMPO_COD_STATO%>">
          <%=nazioni%>
        </select>
      </td>
 		</tr>
    <tr>
      <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Conferma">
      </td>
    </tr>

  </table>

  <input type="HIDDEN" name="<%=ICostantiRegeResidenza.CAMPO_COD_TIPO_RESIDENZA%>" value="<%=lResidenza.getCodTipoResidenza()%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.regesies.regeresidenza.action.ActModificaRegeResidenza">
  <input type="HIDDEN" name="<%=ICostantiRegeResidenza.CAMPO_ID_FILE%>" value="<%=lResidenza.getIdFile()%>">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
  <input type="HIDDEN" name="<%=ICostantiRegeResidenza.CAMPO_COD_TIPO_RESIDENZA%>" value="R">

  </form>
    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadModificaRegeResidenza");

      frmvalidator.addValidation("<%=ICostantiRegeResidenza.CAMPO_CAP%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRegeResidenza.CAMPO_CAP%>","minlen=5","La lunghezza minima per il CAP è di 5 caratteri");
      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>