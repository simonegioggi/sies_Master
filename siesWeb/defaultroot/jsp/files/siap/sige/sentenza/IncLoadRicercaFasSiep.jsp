<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="tipoUfficioSIEPTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="next_action" scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Iscrizione atti con Titolo esecutivo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">

    function radioBase()
    {
      if(document.LoadRicercaFascicolo.tipo[0].checked)
      {
        AbilitaDiv ("singolo");
     	DisabilitaDiv("divIntervallo");
      }
      else if (document.LoadRicercaFascicolo.tipo[1].checked )
      {
       DisabilitaDiv("singolo");
     	AbilitaDiv("divIntervallo");
      }
    }


    function VerifySingolo()
    {
      
      document.LoadRicercaFascicolo.valoreRadio.value='0';
    
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value=="" )
      {
        alert("Il campo Anno è obbligatorio");
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value=="" )
      {
        alert("Il campo Progressivo è obbligatorio");
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
      return true;
    }

    function VerifyIntervallo()
    {
       
       document.LoadRicercaFascicolo.valoreRadio.value='1';
    
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value=="")
      {
        alert('Il campo Anno Iniziale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value=="")
      {
        alert('Il campo Progressivo Iniziale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value=="")
      {
        alert('Il campo Anno Finale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value=="")
      {
        alert('Il campo Progressivo Finale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
        return false;
      }

      // L'anno iniziale deve essere = anno finale.
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value   !=
          document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
      {
        alert('I campi Anno Iniziale e Anno Finale devono essere uguali');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
        return false;
      }

      // Il progr. finale deve essere > del progr. iniziale. La loro differenza non può essere > 200.
      var progr_finale   = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value;
      var progr_iniziale = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value;
      if (progr_finale - progr_iniziale > 200)
      {
        alert('Intervallo progressivi troppo ampio ( massimo 200 )!');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
        return false;
      }
      return true;
    }

  </script>

  <script language="JavaScript">
      var desktop;
      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaFascicolo'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicolo">
    <input type="HIDDEN" name="valoreRadio" value="">

<%
          FascicoloSiepModel lModel = new FascicoloSiepModel();
%>
    <table width=68% >
       <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSIEPTrattino" class=small name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO%>" >
            <%= tipoUfficioSIEPTrattino %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Procura </td>
        <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO%>"
              value="<%=lModel.getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO %>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>

    <table width=68%>
      <tr><td class="Titolo" >Tipo Ricerca</td>
        <td class="Titolo">Singolo Procedimento SIEP&nbsp;
          <input type="radio" name="tipo" value="Singolo" checked onClick="radioBase();">
            &nbsp;&nbsp;Intervallo Procedimenti SIEP &nbsp;
          <input type="radio" name="tipo" value="Intervallo"  onClick="radioBase();">
      </tr>
    </table>
  <div id="comuneFascicoloSiep" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
    <div id="divIntervallo" style="position:relative; top: 0; left: 0; visibility:hidden; ">      
    <table width=68%>
      <tr>
        <td class="l"width=35%>Anno/Numero Iniziale <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP Iniziale"  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE %>" maxlength="4" size="4" onBlur="javascript:document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE %>.value=document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE %>.value" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP Iniziale" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE %>" maxlength="14" size="14">
        </td>
      </tr>
         <tr>
        <td class="l">Anno/Numero Finale <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP Finale"  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP Finale" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE %>" maxlength="14" size="14">
        </td>
      </tr>      
 <tr><td>
 <input onclick="Javascript:return VerifyIntervallo();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
</td></tr>
 </table>
 </div>
  <div id="singolo" style="position:absolute;  top: 0; left: 0;   visibility:visible; " >  
    <table>
      <tr>
        <td class="l" width=35%>Anno/Numero <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP "  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP " type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>" maxlength="14" size="14">
           &nbsp;&nbsp;<a href="Javascript:TrasformaRes('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>');">
           R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
           <a href="Javascript:TrasformaPret('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>');">
           P.T.<img src="/images/filefolder.gif" border=0></a>
        </td>
      </tr>
 <tr><td>
 <input onclick="Javascript:return VerifySingolo();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
</td></tr>
    </table>
 </div>
</div>
 </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaFascicolo");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=14","La lunghezza massima per il Numero Fascicolo è di 14 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
  </script>
  </body>
</html>