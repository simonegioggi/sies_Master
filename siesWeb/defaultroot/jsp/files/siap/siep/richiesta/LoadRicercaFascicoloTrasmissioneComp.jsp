<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="azionechiamante" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="actionPerForm" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>  
  <script language="JavaScript"> 
  var flagVerify = true;    
 
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }  
  
  function Verify(){  
    if(flagVerify==false){
      return true
    }
    else{ 
      if (document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length==0
       || document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="-"){
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
    document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = "";
    document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = "";
    document.f.<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>.value = "";
  }
  
  </script>


</head>

<body class="corpo" onLoad="document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus()">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=actionPerForm%>">
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>      
      <td class=LBG>
        <font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento</font>
      </td>
    </tr>
  </table>

  <br>
  <input type="hidden" value="<%=azionechiamante%>" name="<%=ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE%>" >
  <table cellpadding=2 cellspacing=2>
    <tr>
      <td class="l"> 
        Anno/Numero SIEP <font class=ob>(*)</font>
      </td>
      <td class="L">
        <input type="text" title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="L">Autorità <font class=ob>(*)</font></td>
      <td class="l">
        <select name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" >
          <option value="-">-</option>
          <option value="PM">PROCURA REPUBBLICA PRESSO TRIBUNALE</option>
          <option value="PGCAP">PROCURA GENERALE PRESSO CORTE D'APPELLO</option>
          <option value="PMM">PROCURA REPUBBLICA PRESSO TRIBUNALE DIE MINORENNI</option>
        </select>
      </td>
    </tr> 
    <tr>
      <td class="l">
        Luogo <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
      <td class="L">
        <input title="Sede Autorita"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>" value="<%=descrComune%>" maxlength="35" size="35" >
        <input type="hidden" Title="distrettoUffcio" name="distrettoUffcio" value="" size=35 >
        
        <a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>',document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  
    <tr>
      <td colspan="2">
        <br><br><input class="bottone" name="Carica" value="Carica" type="submit" onclick="flagVerify=true; caricaValidator()">   
        <input class="bottone" name="InserisciTitolo" value="Inserisci Titolo" type="submit" onclick="pulisci()">
     </td>
    </tr>
  </table>    
  
</form>
  <script language="JavaScript" type="text/javascript">  
    function caricaValidator() {
    var frmvalidator  = new Validator("f");
               
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req","Il campo Numero Procedimento è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");
      
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno Procedimento è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
           
    frmvalidator.addValidation("<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>","req","Il campo Luogo è obbligatorio");
          
    frmvalidator.setAddnlValidationFunction("Verify");
  }
   </script>
</body>
</html>