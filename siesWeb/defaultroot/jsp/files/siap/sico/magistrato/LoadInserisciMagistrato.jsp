<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="elencoFlagStato"     scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - GestioneMagistrato </title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"> </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">

      var DataIni= "ciao";   // data Inizio Validità limite inferiore
      function initDataIni (data)
      {
        DataIni = data;
      }


      function  Verify()
      {
        var ritorno = true;
        var data_to_verify1 = document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
        var data_to_verify2 = document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

        if ((document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_NOME%>.value.length == 0)
            || (document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value.length == 0))
        {
          alert("Occorre inserire i dati necessari alla individuazione del magistrato");
          ritorno = false;
        }
        else if (! ControllaData(data_to_verify1))
        {
            alert('Data di Inizio Validità scorretta');
            ritorno = false;
        }
/*        else if (! ControllaData(DataIni))
        {
              alert('Data di appoggio scorretta');
              alert(DataIni);
              ritorno = false;
        }
*/      else if (! CompareDate(DataIni, data_to_verify1))
        {
              alert('Data di Inizio Validità non può essere anticipata');
              ritorno = false;
        }
        else if (data_to_verify2.length > 2)
        {
          if (! ControllaData(data_to_verify2))
          {
                  alert('Data di Fine Validità scorretta');
                  ritorno = false;
          }
          else if (! CompareDate(data_to_verify1, data_to_verify2))
          {
                    alert('Data di Inizio Validità non può essere successiva a quella di Fine');
                    ritorno = false;
          }
        }
        return ritorno;
      }

      function ListaWMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.w_magistrato.action.ActLoadRicercaWMagistrato&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
          MagistratoModel lMagistrato= null;
          String lAzione = new String();

          if( modalita.equals("I") )
          {
            lMagistrato = new MagistratoModel();
            lAzione = "siap.sico.magistrato.action.ActInserisciMagistrato";
            lMagistrato.setDataInizioValidita(DateUtils.getSysDate());
        %>
          <font class="campo">Inserimento di un Magistrato</font>
        <%
          }
          else if( modalita.equals("M") )
          {
            lMagistrato = new MagistratoModel(magistrato);
            lAzione = "siap.sico.magistrato.action.ActModificaMagistrato";
            // lModel = magistrato;
        %>
          <font class="campo">Modifica di un Magistrato</font>
        <%
        }
        %>
          <script language="JavaScript">
            initDataIni( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lMagistrato.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
          </script>
        </td>
      </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMagistrato">
      <table cellspacing=2 cellpadding=2>
       <tr>
          <td class="l">Cognome (*)</td>
          <td class="l"><input value="<%=lMagistrato.getCognome() %>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>"  readonly > </td>
      <%
        if( modalita.equals("I") )
        {
      %>
          <td class="l">
            <a href="Javascript:ListaWMagistrati('LoadInserisciMagistrato');">
            Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
        <% } %>

        </tr>
        <tr>
          <td class="l">Nome (*)</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getNome()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"  readonly></td>
        </tr>
        <tr>
          <td class="l">Codice CSM (*)</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  readonly></td>
        </tr>
        <tr>
          <td class="l">Email Ufficio</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getEMailUfficio()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_E_MAIL_UFFICIO%>"  ></td>
        </tr>
        <tr>
          <td class="l">Email Privata</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getEMailPrivata()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_E_MAIL_PRIVATA %>"  ></td>
        </tr>
        <tr>
          <td class="l">Num. cell.</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getNumCellulare()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_NUM_CELLULARE  %>"  ></td>
        </tr>
        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiMagistrato.CAMPO_FLAG_STATO %>">
            <%= elencoFlagStato %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Data Inizio Validità</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataInizioValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_GIORNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataInizioValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_MESE_DATA_INIZIO_VALIDITA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataInizioValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        <tr>
          <td class="l">Data Fine Validità</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    </form>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciMagistrato");
        frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>","maxlen=4","La lunghezza massima per l'Anno  è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>","minlen=4","La lunghezza minima per l'Anno  è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>","numeric");

        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
  </body>
</html>