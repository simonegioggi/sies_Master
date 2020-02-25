<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel"%>
<%@ page import="siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.posizionemateriale.model.PosizioneMaterialeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="posizioni"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="cod_ufficio"              scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="data_minima"              scope="request" class="java.util.Date"/>
<jsp:useBean id="tipo_posizione_materiale" scope="request" class="java.lang.String"/>

<%
// Viene valorizzato il flag per distinguere il caso Posizione Materiale fascicolo SIUS/SIGE
// da quello SIEP di default.
  boolean lCasoSius = false;
  boolean lCasoSige = false;
  if (tipo_posizione_materiale != null && tipo_posizione_materiale.equalsIgnoreCase("SIUS")){
      lCasoSius = true;
  } else if (tipo_posizione_materiale != null && tipo_posizione_materiale.equalsIgnoreCase("SIGE")){
	  lCasoSige = true; 
  }
%>

<html>
<head>
<title>[S.I.A.P.] - GestionePosizioneMaterialeFasc </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">
function Verify()
{
   var data_minima = "<%=DateUtils.getDateToString(data_minima,"dd/MM/yyyy")%>";
   var data_inizio=document.LoadInserisciPosizioneMaterialeFasc.<%=ICostantiPosizioneMaterialeFasc.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneMaterialeFasc.<%=ICostantiPosizioneMaterialeFasc.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneMaterialeFasc.<%=ICostantiPosizioneMaterialeFasc.CAMPO_ANNO_DATA_INIZIO%>.value;
   var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

   if (! ControllaData(data_inizio))
   {
     alert('Data Inizio non valida!');
     return false;
   }

   // Controllo data di sistema >= Data Inizio
   if ( !CompareDate( data_inizio, data_sistema ) )
   {
     alert('Data Inizio non può essere superiore alla data odierna');
     return false;
    }
   if (ControllaData(data_minima))
   {
      // Controllo data di Inizio >= Data Minima
      if ( !CompareDate( data_minima, data_inizio ) )
      {
        alert("Data Inizio non può precedere la data " + data_minima);
        return false;
      }
   }

   return true;
}
</script>

</head>


<body class="corpo">
  <table>
   <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      //PosizioneMaterialeFascModel lModel = new PosizioneMaterialeFascModel();
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        if (lCasoSius){
          lAzione = "siap.sius.posizionematerialefascsius.action.ActInserisciPosizioneMaterialeFasc";
        } else if (lCasoSige){
          lAzione = "siap.sige.posizionematerialefascsige.action.ActInserisciPosizioneMaterialeFasc";	
        } else {
          lAzione = "siap.siep.posizionematerialefasc.action.ActInserisciPosizioneMaterialeFasc";
        }
    %>
       <font class="campo">Associazione di una Posizione Materiale al Fascicolo</font>
    <%
      }
      else if( modalita.equals("M") )
      {
       lAzione = "siap.siep.posizionematerialefasc.action.ActModificaPosizioneMaterialeFasc";
       //lModel = posizionematerialefasc;
     %>
       <font class="campo">Modifica di un PosizioneMaterialeFasc</font>
     <%}%>
    </td>
   </tr>
 </table>
    <br>
<%
        if (lCasoSius)
        {
          // sintesi Fasicolo SIUS
%>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<%
        } else if (lCasoSige){
  		  // sintesi Fascicolo SIGE
%>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
<%
        }
        else
        {
          // Sintesi Fascicolo SIEP
%>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%   }  %>
    <br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneMaterialeFasc">
   <input type="hidden" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>">
   <input type="hidden" value="<%=cod_ufficio%>"  name="<%= ICostantiPosizioneMaterialeFasc.CAMPO_COD_UFFICIO%>"  >


 <table cellspacing=4 cellpadding=4>
    <tr>
      <td class="l">Data Inizio<font class="ob">(*)</font></td>
      <td class="l">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiPosizioneMaterialeFasc.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiPosizioneMaterialeFasc.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiPosizioneMaterialeFasc.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

   <tr>
    <td class="l">Posizione Materiale</td>
    <td class="l">
      <select  name="<%=ICostantiPosizioneMaterialeFasc.CAMPO_COD_POSIZIONE_MATERIALE%>"  class="small">
      <%
        String cod;
        String desc;
        for (int i=0;i<posizioni.size();i++)
        {
          cod=((PosizioneMaterialeModel)posizioni.get(i)).getCodPosizioneMateriale();
          desc= ((PosizioneMaterialeModel)posizioni.get(i)).getDescPosizioneMateriale() + " - " + cod;
      %>
         <option value="<%=cod%>"><%=desc%></option>
<%      }
%>
      </select>
     </td>
   </tr>
 </table>
<BR>
<input type="submit" class=bottone name="go" value="Conferma">
</form>
 <script language="JavaScript" type="text/javascript"> var frmvalidator  = new Validator("LoadInserisciPosizioneMaterialeFasc");
  frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno  della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese  della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_ANNO_DATA_INIZIO%>","req","Il campo Anno della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPosizioneMaterialeFasc.CAMPO_ANNO_DATA_INIZIO%>","numeric");
 </script>
 </body>
</html>