<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="contenuto"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"   	scope="request" class="java.util.Date"/>
<jsp:useBean id="fascicolo_origine" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>


<%
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  // Flag che indica la presenza del fascicolo origine;
  boolean isFascicoloOrigine = (fascicolo_origine != null && fascicolo_origine.getFascicoloSiusModel() != null && fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius() != null) ? true : false;
String vuota = "";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Declaratoria estinzione della pena</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">

    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaEP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;

      var ritorno = true;
      ritorno = ControlliDate();
      return ritorno;
    }

    function ControlliDate()
    {
     var ritorno = true;
     var data_inizio_periodo = document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
     var data_termine_periodo = document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;

     if (data_termine_periodo.length > 2 )
     {
        ritorno = ConfrontaConDataEmissione(data_termine_periodo, "data termine periodo");
        if (ritorno)
           ritorno = ConfrontaConDataEmissione(data_inizio_periodo, "data inizio periodo");
        if (ritorno)
        {
         if ( CompareDate(data_termine_periodo,data_inizio_periodo ) )
         {
          alert( "La data inizio periodo deve precedere la data termine periodo");
          ritorno =  false;
         }
       }
     }
     else if (data_inizio_periodo.length > 2 )
     {
       ritorno = ConfrontaConDataEmissione(data_inizio_periodo, "data inizio periodo");
     }
     return ritorno;
    }
  </script>

    <script language="JavaScript">
    function ConfrontaConDataEmissione(data, nome)
    {
     var ritorno = true;
     var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';

       if ( data.length == 2 || ! ControllaData(data) )
       {
        alert( "" + nome + " non valida");
        ritorno =  false;
       }
       else if ( CompareDate(data_emissione, data) )
       {
        alert( ""  + nome + "  deve precedere Data Emissione Ordinanza" );
        ritorno =  false;
       }
      return ritorno;
    }
  </script>
  <script language="JavaScript">
     function DettaglioFascicolo()
    {
      var desktop;
      var id_fascicolo = document.InserisciOrdinanzaEP.<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE%>.value;
      if (id_fascicolo.length > 1)
      {
        var link = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActListaFascicoliOrigineSoggCont&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE%>=" + id_fascicolo;
        //desktop = location.replace(link);
        window.open(link,"Dettaglio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
     // return desktop;
    }
  </script>
  <script language="JavaScript">

      function ListaFascicoliOrigine(a_formname, a_fieldname1,a_fieldname2, a_fieldname3 )
      {
        var desktop;
        var link = "/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActListaFascicoliOrigineSoggCont&formname=" + a_formname + "&fieldname1=" + a_fieldname1;
        link = link + "&fieldname2=" + a_fieldname2;
        link = link + "&fieldname3=" + a_fieldname3;

        link = link + "&<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>=U041" ;
        link = link + "&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=" + "<%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getIdSoggetto()%>";
        //alert("link -> " + link);
        //desktop = window.open(link , "Selezione Fascicolo Origine", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
        window.open(link,"Ricerca","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
//return desktop;
        }

  </script>

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>


  <body class="corpo" >

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Declaratoria estinzione della pena</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaEP">
    <table width=35%>
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
    </table>
      <table>
    <tr>
        <td class="Titolo" colspan=6 > Selezionare il Procedimento di riferimento dell'Ufficio di Sorveglianza, presente in Archivio</td>
    </tr>
     <tr>
      <td class="L">
        <font class="label">Procedimento selezionato N.</font>
       </td>
        <td class="l">
          <input Title="Anno SIUS"  type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" value="<%=isFascicoloOrigine ? fascicolo_origine.getFascicoloSiusModel().getChiaveAnno().toString() : vuota%>" readonly>
          /<input Title="Numero SIUS" type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6" value="<%=isFascicoloOrigine ? fascicolo_origine.getFascicoloSiusModel().getChiaveProgr().toString() : vuota%>"   readonly>
        </td>

      <td class="l">
        <a class="cliccabile" href= "JavaScript:DettaglioFascicolo()">
          dettaglio
        </a>
        </td>
          <td class="l">
            <a class="cliccabile" href="Javascript:ListaFascicoliOrigine('InserisciOrdinanzaEP','<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE%>', '<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>', '<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>' );">
            Seleziona il fascicolo dalla lista  <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
        </tr>
       </table>

    <tr> <td>&nbsp;</td> </tr>
    <table cellspacing="2" cellpadding="2" style="width: 90%;">
    	<tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    	</tr>
    	<tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    	</tr>
    	<%
   		for (int i=0; i< tenori.length;i++)
    	{
    	%>
       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>

		  <tr><td>&nbsp;</td> </tr>

    	<tr>
				<td class="l" colspan="2">Ulteriore descrizione della decisione</td>
    		<td class="l" colspan="2"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
			</tr>

    </table>
<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr> </tr>

		<tr>
    	<td class="Titolo" colspan="6"> In caso di pena non validamente espiata indicare: <td>
  	</tr>

		<tr>
      <td class="l">Data Inizio Periodo <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
      <td class="l">Data Termine Periodo <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

		<tr> </tr>

    <tr>
      <td class="l">Pena rideterminata <br> (AA-MM-GG)</td>
      <td class="L">
        <input value="" title="Numero Anni Detenzione" type="text" size="3" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Mesi Detenzione" type="text" size="3" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Giorni Detenzione" type="text" size="4" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>"  >
      </td>
    </tr>
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE%>" value=<%=isFascicoloOrigine ? fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius().toString() : ""%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaEP");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>


 </body>

</html>