<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>

<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>


<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
   <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function Verify(i)
	  {
    if (document.RicercaEstesaFascicolo.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length == 1)
			{
       alert('Il campo Sede Ufficio è obbligatorio');
       document.RicercaEstesaFascicolo.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();
			 return false;
		  }

     if (i==1)
     {
       document.RicercaEstesaFascicolo.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaFascicolo";
       //document.RicercaEstesaFascicolo.submit();
     }
      if (i==2)
     {
       document.RicercaEstesaFascicolo.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaFascicoloPerTrasferimento";
       //document.RicercaEstesaFascicolo.submit();
     }
    }



   </script>
</head>

<body class="corpo">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='RicercaEstesaFascicolo'>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" >

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento in altre BDI</font></td>
    </tr>
  </table>

  <br>

  <table cellpadding=2 cellspacing=2>
    <tr><td class="Titolo" colspan=4>Fascicolo </td></tr>
    <tr>
      <td class="L"> Anno/Numero Procedimento <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="L">
        <input type="text" title="Anno Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14">
      </td>
      </tr>
      <tr>
         <td class="L">Tipo Ufficio <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L">
             <select Title="Autorita Esterna" name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" >
               <%=autoritaEsterna%>
             </select>
             </td>
      </tr>
      <tr>
       <tr>
       <td class="l">Sede <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="L">
       <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('RicercaEstesaFascicolo','<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>');">
       <img src="/images/filefolder.gif" border=0></a></td>
   </tr>
   <tr>
      <td> &nbsp;&nbsp; </td>
   </tr>
     <tr>
      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return Verify(1);" >
      </td>


      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca Per Trasferimento" onClick="javascript:return Verify(2);">
      </td>
    </tr>
  </table>
</form>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("RicercaEstesaFascicolo");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>","req");
  frmvalidator.addValidation("<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>","req");


</script>
</body>
</html>