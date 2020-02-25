<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="relazione" scope="request" class="siap.siepe.relazione.model.RelazioneModel"/>
<jsp:useBean id="ufficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="altroUfficioDestinatario" scope="request" class="java.lang.String"/>

<script language="JavaScript">
  var desktop;
  // Lista Uffici per TIPO_UFFICIO
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  function Verify()
  {
    return true;
  }

</script>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="TrasferisciRelazione">
 <table cellspacing="2" cellpadding="2"  width="75%">
 		<tr>
		   <td class="l">Ufficio di Destinazione</td>
       <td class="l"><font class="campo"><%=ufficioDestinatario.getDescrTipoUfficio() + " " +  ufficioDestinatario.getDescrComune()%></font></td>
		</tr>
 
   <tr><td>&nbsp;</td></tr>

   <tr>
     <td class="label" width="25%" colspan ="2">Ulteriore destinatario</td>
   </tr>

   <tr>
     <td class="l">Tipo Ufficio </td>
     <td class="L">
      <select Title="Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
        <%=altroUfficioDestinatario%>
      </select>
     </td>
   </tr>
   <tr>
     <td class="l">Sede </td>
     <td class="L">
       <input title="Sede Destinatario"  type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
         <a href="Javascript:ListaUfficiPerTipo('TrasferisciRelazione','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>',document.TrasferisciRelazione.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.TrasferisciRelazione.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.selectedIndex].value);">
           <img src="/images/filefolder.gif" border=0>
         </a>
     </td>
   </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
 
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.relazione.action.ActTrasferisciRelazione" >
  <input type="HIDDEN" name="<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>" value="<%=relazione.getIdRelazione()%>" >
  <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=ufficioDestinatario.getCodUfficio()%>" >
</form>