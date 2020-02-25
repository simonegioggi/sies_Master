<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="azionechiamante" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="actionPerForm" scope="request" class="java.lang.String"/>


<html>
<head>
  <title> [S.I.E.S.] - Selezione Autorità Destinataria - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>  
  <script language="JavaScript"> 
  var flagVerify = true;    
 
  function ListaUffici(a_formname,a_fieldname){
    var TipoUff = document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }
  
  function Verify(){  
    if(flagVerify==false){
      return true;
    }
    else{ 
      if (   document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length==0
          || document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="-")
      {
        alert('Il campo Autorità è obbligatorio');
        return false;
      } 
      return true;
    } 
  }
  
  function pulisci(){
    flagVerify = false;
    //pulisco il validator;
    for(var itr=0;itr < document.forms['f'].elements.length;itr++){
      document.forms['f'].elements[itr].validationset = null;
    }
    document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value = "";
    document.f.<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>.value = "";
  }
  
  </script>


</head>

<body class="corpo">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=actionPerForm%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
    <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE%>" value="<%=azionechiamante%>"  >
    
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>      
        <td class=LBG>
          <font class="label">Funzione :</font>&nbsp;<font class="campo">Selezione Autorità destinataria della Ritrasmissione</font>
        </td>
      </tr>
    </table>

    <br>
    
    <table cellpadding=2 cellspacing=2>
      <tr>
      <td class="L">Autorità <font class=ob>(*)</font></td>
      <td class="l">
        <select name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" >
          <option value="-">-</option>
          <option value="PM">PROCURA REPUBBLICA PRESSO TRIBUNALE</option>
          <option value="PGCAP">PROCURA GENERALE PRESSO CORTE D'APPELLO</option>
        </select>
      </td>
    </tr> 
    <tr>
      <td class="l">
        Luogo <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>" value="<%=descrComune%>" maxlength="35" size="35" >
        <input type="hidden" Title="distrettoUffcio" name="distrettoUffcio" value="" size=35 >
        <a href="Javascript:ListaUffici('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  
    <tr>
      <td colspan="2">
        <br><br><input class="bottone" name="Invia" value="Invia" type="submit" onclick="flagVerify=true; caricaValidator()">
      </td>
    </tr>
  </table>  
</form>

  <script language="JavaScript" type="text/javascript">  
    function caricaValidator() {
    var frmvalidator  = new Validator("f");
    frmvalidator.addValidation("<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>","req","Il campo Luogo è obbligatorio");
        
    frmvalidator.setAddnlValidationFunction("Verify");
  }
   </script>
</body>
</html>