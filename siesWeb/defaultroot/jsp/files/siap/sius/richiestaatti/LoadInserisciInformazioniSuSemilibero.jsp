<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="dataInsFS"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti2"  scope="request" class="java.lang.String"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciInformazioniSuSemilibero";
%>


<script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function ListaUfficiCSSA(a_formname,a_fieldname)
    {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
 	// MEV10-s3: aggiunta funzione per scegliere la selezione alla lista di riferimento a seconda del tipo selezionato
    function scegliTipo() {
    	var tipo = document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value;
    	if ("B5" == tipo)
    		ListaUSSM('LoadInserisciInformazioniSuSemilibero','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]');
    	else
    		ListaUfficiCSSA('LoadInserisciInformazioniSuSemilibero','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]');
    }
    function ListaUSSM(a_formname,a_fieldname) {
   		desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaUSSM&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
</script>

<script language="JavaScript">
    function Verify()
    {
      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0].value == '')
          {
              alert('La Sede del destinatario n°1 è obbligatoria');
              return false;
          }
      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value != '-'
          && document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value == '')
          {
              alert('La Sede del destinatario n°2 è obbligatoria');
              return false;
          }
      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value != '-'
          && document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2].value == '')
          {
              alert('La Sede del destinatario n°3 è obbligatoria');
              return false;
          }

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == '-'
          && document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value == '-'
          && document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value == '-')
          {
              alert('Inserire almeno un Destinatario con la relativa Sede.');
              return false;
          }

      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_emissione))
      {
        alert('Data di emissione non valida');
        return false;
      }

      // Data emissione minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_emissione,data_sistema) )
      {
        alert('Data Emissione maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Sius <= data Emissione
      if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      {
        alert('Data Emissione minore della data di inserimento del fascicolo SIUS!');
        return false;
      }

      if (document.LoadInserisciInformazioniSuSemilibero.<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>[2].value == '')
          {
              alert('La Ditta presso cui lavora il detenuto è un campo obbligatorio');
              return false;
          }
      return true;
    }
</script>

<html>
  <head>
      <title>[S.I.E.S.] - Richiesta Informazioni su semilibero</title>
      <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
      <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
      <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

  <body onLoad="document.forms[0].elements[0].focus()" class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label"> Funzione :</font>&nbsp;
          <font class="campo">Richiesta Informazioni su semilibero</font>
        </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="<%=ICostantiRichiestaAtti.MSG_BUTTON_HISTORY%>" width="24" height="24" border="0">
        </a>
      </td>
      </tr>

      <tr>
        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciInformazioniSuSemilibero">
      <table cellspacing=2 cellpadding=2>

        <tr>
          <td class="l">Data Emissione <font class=ob>(*)</font></td>
          <td class="L">
            <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Mese"   value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Anno"   value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>

        <!-- Primo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario n°1</td>
          <td class="L" colspan=3>
          <table>
          <tr >
          <td class="l"> Tipo</td>
          <td class="l" >
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti2 %>
          </select>
          </td>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
          	<%-- MEV10-s3: aggiunto controllo sul tipo --%>
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:scegliTipo();">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>


        <!-- secondo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario n°2</td>
          <td class="L" colspan=3>
          <table>
          <tr >
          <td class="l"> Tipo</td>
          <td class="l" >
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti1 %>
          </select>
          </td>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuni('LoadInserisciInformazioniSuSemilibero','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]');">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>

        <!-- terzo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario n°3</td>
          <td class="L" colspan=3>
          <table>
          <tr >
          <td class="l"> Tipo</td>
          <td class="l" >
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti1 %>
          </select>
          </td>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuni('LoadInserisciInformazioniSuSemilibero','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2]');">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>
      </table>

      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="l">Data semilibertà </td>
          <td class="l"><input type="text" Title="Data semilibertà" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>" size="40"></td>
        </tr>
        <tr>
          <td class="l">Concessa dal Tribunale di Sorveglianza di </td>
          <td class="l"><input type="text" Title="Concessa dal Tribunale di sorveglianza di" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>" size="40"></td>
        </tr>
        <tr>
          <td class="l">Ditta presso cui lavora il detenuto<font class=ob>(*)</font></td>
          <td class="l"><input type="text" Title="Ditta presso cui lavora il detenuto" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>" size="40"></td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=40 rows=4 ></textarea>
            </td>
            <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
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
      var frmvalidator = new Validator("LoadInserisciInformazioniSuSemilibero");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
<%--       frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha"); --%>

      //Chiama la funzione di Verify().
      frmvalidator.setAddnlValidationFunction("Verify");
    </script>

  </body>
</html>