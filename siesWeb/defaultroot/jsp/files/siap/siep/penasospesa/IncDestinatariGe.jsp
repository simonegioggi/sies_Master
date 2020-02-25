<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>


<script language="JavaScript">
    var desktop;
     
    // Lista Uffici per TIPO_UFFICIO
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaAutorita(a_formname,a_fieldname,codTipoUfficio)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>

<% 		  
	//Tipi Ufficio:  "-", 'CAP', 'CAPSM', 'CAS', 'CASAP', 'GIP', 'GIPM', 'TRIBSD', 'DIB', 'DIBM'
	Option lOptionAltreAut = new Option( DecodificheManager.getInstance().getTipiUfficioSigeTrattino(), "-");
/*

    Option lOptionAltreAut = new Option( DecodificheManager.getInstance().getTipoUfficio());
	String[] lFiltroUffici = {"-", "CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD"};
	lOptionAltreAut.setFilter( lFiltroUffici); //solo le Autorità Emittenti.
 */ 
	String lAutorita = lOptionAltreAut.toString();
%>		  
<html>
  		<table style="width: 95%;">
			    <tr><td class="Titolo" colspan=4>Destinatari </td></tr>
     	</table>
     	<table style="width: 95%;">
     	<tr >
        <td class="l">Ufficio del Giudice  
           <select title="Destinatario" name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>">
            <%=lAutorita%>
          </select>
        </td>
           <td class="l"> Sez. <input title="Sezione" name="<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>"
               value="-" type="text" size="7" maxlength="35" >
        </td>
            <td class="l"> Sede
           <input Title="Sede" name="<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>"
              value="" type="text" maxlength="35" size="20">
              <a href="Javascript:ListaUfficiPerTipo(document.forms[0].name, '<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>',document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>').value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
  </table>
</html>