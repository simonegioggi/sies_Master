<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="TipoDefinizione"     scope="request" class="java.lang.String" />
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="provvedimento"       scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />

<jsp:useBean id="TornaQui"         scope="request" class="java.lang.String"/>
<jsp:useBean id="descrizione"      scope="request" class="java.lang.String" />
<jsp:useBean id="data_definizione" scope="request" class="java.util.Date" />
<jsp:useBean id="modalita"         scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile"     scope="request" class="java.lang.String" />

<%
boolean readonly = false;
if (modalita.equalsIgnoreCase("dettaglio"))
   readonly = true;

/* Estrazione della data udienza  o data iscrizione */
 String data1;
 if (FascicoloSigeEsteso.getUdienzaProcedimento()!=null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null)
  data1 = DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(FascicoloSigeEsteso.getFascicoloSige().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Load Definizione Procedimento SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

    <script language="JavaScript">
      var desktop;
      function  Verifica()
      {
        var ritorno = true;
        var data_minima = '<%=data1%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_definizione = document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

          // Controllo della data di Definizione
          if (ritorno && (! ControllaData(data_definizione)))
          {
            alert('Data Definizione non valida: '+ data_definizione );
            return false;
          }
          // Controllo data di sistema >= Data Definizione .
          else if( !CompareDate( data_definizione, data_sistema) )
          {
            alert('Data Definizione non può essere superiore alla data odierna!');
            ritorno =  false;
          }
         else if ( !CompareDate( data_minima, data_definizione) )
         {
            alert("Data Definizione non può precedere: " + data_minima);
            ritorno =  false;
         }
        return ritorno;
      }
      
      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>

  </head>
<%
    String lAction = "";
    String lDocumento = null;
    String lTitolo = "";
    String lActRet = null;

    if (modalita.equalsIgnoreCase("inserimento"))
    {
       lAction = "siap.sige.fascicolo.action.ActDefinizioneProcedimento";
       lTitolo = "Inserimento Definizione Procedimento";
    } 
    else if (modalita.equalsIgnoreCase("dettaglio"))
    {
       lTitolo = "Dettaglio Definizione Procedimento";
    } 
    else if (modalita.equalsIgnoreCase("modifica"))
    {
       lTitolo = "Modifica Definizione Procedimento";
       lAction = "siap.sige.fascicolo.action.ActDefinizioneProcedimento";
    }
%>

  	<body class="corpo">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class="LBG"><font class="label">Funzione: </font>
          		<font class="campo"><%=lTitolo%></font>
      		</td>
<%
if (modalita.equalsIgnoreCase("dettaglio")) {
%>	
	
      		<td class="LBG">
				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
					<jsp:param name="Modificabile" value="<%=Modificabile%>" />
					<jsp:param name="ValoreIdEntita" value="<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString()%>" />
          		</jsp:include>
     		</td>
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

<%
	// 20170626: aggiunto controllo preventivo
	if (provvedimento != null && provvedimento.getEventoNotifica() != null) {
		if (provvedimento.getEventoNotifica().getEvento() != null
				&& provvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null
				|| provvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null
				&& provvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
	    	<!-- BOTTONE DI VALIDAZIONE -->
	    	<td class="LBG">
	      		<a href="Javascript:lookUpload();">
	        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>upload24.gif" alt="Valida" width="24" height="24" border="0">
	      		</a>
	    	</td>
<%
      	}
	}
}
%>
	    </tr>
	    <tr>
	    	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
	    </tr>
	</table>

 	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadDefinizioneProcedimento">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Definizione<font class="ob">(*)</font></td>
      <td class="L">
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_DEFINIZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_DEFINIZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiFascicoloSige.CAMPO_ANNO_DATA_DEFINIZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

<%
	  // MEV 15 - Revisione SIGE
	  if(!readonly){
%>
		<a href="javascript:calendario('LoadDefinizioneProcedimento','<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_DEFINIZIONE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_DEFINIZIONE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_DEFINIZIONE%>');">
      			<img src="/images/calendario.gif" border=0>
       	</a>
<%		  
	  }
%>
      </td>
    </tr>

  <tr>
    <td class="l">Tipo Definizione<font class="ob">(*)</font></td>
    <td class="l">
    <select title="TipoDefinizione" name="<%=ICostantiFascicoloSige.CAMPO_TIPO_DEFINIZIONE%>" <% if (readonly) {%> disabled <%}%>>
     <%=TipoDefinizione%>
     </select>
    </td>
  </tr>

  <tr>
    <td class="l">Ulteriore Descrizione</td>
    <td class="l">
        <TEXTAREA <% if (readonly) {%> readonly <%}%>  Title="Descrizione" name="<%=ICostantiFascicoloSige.CAMPO_DESCR_DEFINIZIONE%>" cols=75 ROWS=3 ><%=descrizione%></textarea>
    </td>
  </tr>
<%
  if (!readonly)
{
%>
  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
<%} %>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadDefinizioneProcedimento");
     //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verifica");
 </script>

  <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActUploadDefinizioneProcedimento">
<%
			if(provvedimento.getProvvedimento() != null && provvedimento.getEventoNotifica().getEvento() != null){
%>
            	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=provvedimento.getEventoNotifica().getEvento().getIdEvento()%>">
<%
			}
%>
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sige.fascicolo.action.ActLoadDefinizioneProcedimento">
          </td>
        </tr>
      </table>
	</FORM>
  </div>
  <br>
  <br>

  </body>
</html>