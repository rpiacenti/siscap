package br.com.correios.ppjsiscap.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Classe utilitaria com operacoes em datas.
 * 
 * @author arivaldojunior
 */
public final class UtilData {
	
	

	/**
	 * Retorna a quantidade de dias do ano.
	 * 
	 * @param data
	 *            Data.
	 * @return quantidade de dias do ano.
	 */
	public  int getDiasDoAno(Date data) {
		int res = 0;

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			res = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		}
		return res;
	}

	/**
	 * Retorna a quantidade de dias do ano.
	 * 
	 * @param ano
	 *            Ano.
	 * @return quantidade de dias do ano.
	 */
	public  int getDiasDoAno(int ano) {
		int dias = 0;

		if (getPrimeiroAnoDefault() <= ano) {
			Date data = null;
			int mes = getPrimeiroMesDefault();
			int dia = getPrimeiroDiaDoMesDefault();

			data = novoCalendar(dia, mes, ano).getTime();
			dias = getDiasDoAno(data);
		}

		return dias;
	}
	
	/**
	 * Retorna instancia de Calendar.
	 * 
	 * @param dia
	 *            dia
	 * @param mes
	 *            mes
	 * @param ano
	 *            ano
	 * @return instancia de Calendar
	 */
	public  Calendar novoCalendar(int dia, int mes, int ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes - 1, dia);
		return calendar;
	}

	/**
	 * Retorna a quantidade de dias do mês.
	 * 
	 * @param data
	 *            Data.
	 * @return quantidade de dias.
	 */
	public  int getDiasDoMes(Date data) {
		int res = 0;
		if (data != null) {
			Calendar calendar = novoCalendar(data);
			res = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return res;
	}

	/**
	 * Instancia um objeto do tipo Calendar e o ajusta para a data passada
	 * como parametro.
	 * @param data
	 * @return Calendar
	 */
	public  Calendar novoCalendar(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar;
	}

	/**
	 * Retorna a quantidade de dias do mes.
	 * 
	 * @param month
	 *            Mes.
	 * @return quantidade de dias.
	 */
	public  int getDiasDoMes(int month) {
		int res = 0;

		if (month > getPrimeiroMesDefault()) {
			Calendar calendar = Calendar.getInstance();
			Date data = novoDate(getPrimeiroDiaDoMesDefault(),
					month, calendar.get(Calendar.YEAR));
			res = getDiasDoMes(data);
		}

		return res;
	}

	/**
	 * Retorna uma instancia de Date de acordo com os parametros
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	private  Date novoDate(int dia, int mes, int ano) {		
		return novoCalendar(dia, mes, ano).getTime();
	}

	/**
	 * Retorna o dia da semana.<br>
	 * <dd>1 = domingo<br>
	 * <dd>2 = segunda<br>
	 * <dd>3 = terca<br>
	 * <dd>4 = quarta<br>
	 * <dd>5 = quinta<br>
	 * <dd>6 = sexta<br>
	 * <dd>7 = sabado
	 * 
	 * @param data
	 *            Data.
	 * @return dia da semana.
	 */
	public  int getDiaDaSemana(Date data) {
		int res = 0;

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			res = calendar.get(Calendar.DAY_OF_WEEK);
		}
		return res;
	}

	/**
	 * Retorna a hora no formato de 12 horas.
	 * 
	 * @param data
	 *            Data.
	 * @return hora.
	 */
	public  int getHoraNoFormatoDe12(Date data) {
		int res = 0;
		if (data != null) {
			Calendar calendar = novoCalendar(data);
			res = calendar.get(Calendar.HOUR);
		}
		return res;
	}

	/**
	 * Retorna a hora no formato de 24 horas.
	 * 
	 * @param data
	 *            Data.
	 * @return hora.
	 */
	public  int getHoraNoFormatoDe24(Date data) {
		int res = 0;

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			res = calendar.get(Calendar.HOUR_OF_DAY);
		}
		return res;
	}

	/**
	 * Efetua o parse da string para um Date.
	 * 
	 * @param string
	 *            String que será convertida.
	 * @param pattern
	 *            Pattern usado na conversão.
	 * @return Date.
	 */
	public  Date parse(String string, String pattern) {
		Date res = null;

		if (!"".equals(string) && !"".equals(pattern)) {

			SimpleDateFormat sdf = null;
			try {
				sdf = novoSimpleDateFormat(pattern);
				sdf.setLenient(false);
				res = sdf.parse(string);
			} catch (IllegalArgumentException e) {
				UtilLog.logger.warn("Pattern inválido", e);
			} catch (ParseException e) {
				UtilLog.logger.warn("Parser não efetuado", e);
			}
		}

		return res;
	}
	
	/**
	 * Retorna a instância de um SimpleDateFormat.
	 * 
	 * @param pattern
	 *            Pattern da formatação.
	 * @return instância de um SimpleDateFormat
	 */
	public  SimpleDateFormat novoSimpleDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * Efetua o parse da string para um Date.
	 * 
	 * @param string
	 *            String que será convertida.
	 * @param pattern
	 *            Pattern usado na conversão.
	 * @param lenient
	 *            Se true a data inválida será retornada nula.
	 * @return Date.
	 */
	public  Date parse(String string, String pattern, boolean lenient) {
		Date res = null;

		if (!"".equals(string) && !"".equals(pattern)) {
			SimpleDateFormat sdf = null;
			try {
				sdf = novoSimpleDateFormat(pattern);
				sdf.setLenient(lenient);
				res = sdf.parse(string);
			} catch (IllegalArgumentException e) {
				UtilLog.logger.warn("Pattern inválido", e);
			} catch (ParseException e) {
				UtilLog.logger.warn("Parser não efetuado", e);
			}
		}

		return res;
	}

	/**
	 * Retorna ano da data.
	 * 
	 * @param data
	 *            Data
	 * @return ano da data.
	 */
	public  int getAno(Date data) {
		int resultado = getPrimeiroAnoDefault();

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			resultado = calendar.get(Calendar.YEAR);
		}

		return resultado;
	}

	/**
	 * Retorna mês da data.
	 * 
	 * @param data
	 *            Data
	 * @return mês da data.
	 */
	public  int getMes(Date data) {
		int resultado = getPrimeiroMesDefault();

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			resultado = calendar.get(Calendar.MONTH);
			resultado++;
		}

		return resultado;
	}

	/**
	 * Retorna dia da data.
	 * 
	 * @param data
	 *            Data
	 * @return dia da data.
	 */
	public  int getDia(Date data) {
		int resultado = getPrimeiroDiaDoMesDefault();

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			resultado = calendar.get(Calendar.DAY_OF_MONTH);
		}

		return resultado;
	}

	/**
	 * Retorna o último dia do mês da data informada.
	 * 
	 * @param data
	 *            Data
	 * @return último dia do mês.
	 */
	public  int getUltimoDiaDoMes(Date data) {
		int resultado = 0;

		if (data != null) {
			Calendar calendar = novoCalendar(data);
			resultado = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return resultado;
	}

	/**
	 * Retorna o último dia do mês informado.
	 * 
	 * @param mes
	 * @return último dia do mês.
	 */
	public  int getUltimoDiaDoMes(int mes) {
		int result = 0;

		if (mes >= getJaneiro() && mes <= getDezembro()) {
			Date data = new Date();
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.DAY_OF_MONTH, getPrimeiroDiaDoMesDefault());
			calendar.set(Calendar.MONTH, (mes - 1));
			result = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return result;
	}

	/**
	 * Retorna o último dia do mês da data atual.
	 * 
	 * @return último dia do mês.
	 */
	public  int getUltimoDiaDoMes() {
		Date data = new Date();
		return getUltimoDiaDoMes(data);
	}

	/**
	 * Ajusta horas, minutos e segundos na data.
	 * 
	 * @param data
	 *            Data que será atualizada.
	 * @param horas
	 *            horas
	 * @param minutos
	 *            minutos
	 * @param segundos
	 *            segundos
	 * @return data atualizada
	 */
	public  Date ajustarData(Date data, int horas, int minutos,
			int segundos) {
		if (data != null) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.HOUR, horas);
			calendar.set(Calendar.MINUTE, minutos);
			calendar.set(Calendar.SECOND, segundos);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta horas, minutos e segundos na data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param hora
	 *            hora
	 * @return data atualizada
	 */
	public  Date ajustarData(Date data, Time hora) {
		if (data != null && hora != null) {
			Calendar horaCalendar = novoCalendar(hora);

			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.HOUR, horaCalendar.get(Calendar.HOUR));
			calendar.set(Calendar.MINUTE, horaCalendar.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, horaCalendar.get(Calendar.SECOND));
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta a hora da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param hora
	 *            Hora
	 * @return data atualizada
	 */
	public  Date ajustarDataParaHora(Date data, int hora) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.HOUR_OF_DAY, hora);
			data = calendar.getTime();
		}
		return data;
	}
	
	/**
	 * Ajusta os minutos da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param minuto
	 *            Minuto
	 * @return data atualizada
	 */
	public  Date ajustarDataParaMinuto(Date data, int minuto) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.MINUTE, minuto);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta os segundos da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param segundos
	 *            Segundos
	 * @return data atualizada
	 */
	public  Date ajustarDataParaSegundos(Date data, int segundos) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.SECOND, segundos);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta o dia da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param dia
	 *            Dia
	 * @return data atualizada
	 */
	public  Date ajustarDataParaDia(Date data, int dia) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.DAY_OF_MONTH, dia);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta o mês da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param mes
	 *            Dia
	 * @return data atualizada
	 */
	public  Date ajustarDataParaMes(Date data, int mes) {
		if (data != null ) {
			mes = (mes - 1);
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.MONTH, mes);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta o ano da data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param ano
	 *            Ano
	 * @return data atualizada
	 */
	public  Date ajustarDataParaAno(Date data, int ano) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.YEAR, ano);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta os milisegundos de uma data.
	 * 
	 * @param data
	 *            Data que ser� atualizada.
	 * @param milisegundos
	 *            Milisegundos
	 * @return data atualizada
	 */
	public  Date ajustarDataParaMilisegundo(Date data, int milisegundos) {
		if (data != null ) {
			Calendar calendar = novoCalendar(data);
			calendar.set(Calendar.MILLISECOND, milisegundos);
			data = calendar.getTime();
		}
		return data;
	}

	/**
	 * Ajusta o timezone para o identificador determinado.
	 * 
	 * @param identificador
	 *            Identificador do timezone. Ex: GMT-3:00
	 */
	public  void ajustarTimeZone(String identificador) {
		TimeZone timeZone = TimeZone.getTimeZone(identificador);
		TimeZone.setDefault(timeZone);
	}

	/**
	 * Retorna nova data nula.
	 * 
	 * @return data nula.
	 */
	public  Date getDataNula() {
		int dia = getPrimeiroDiaDoMesDefault();
		int mes = getPrimeiroMesDefault();
		int ano = getPrimeiroAnoDefault();

		return novoDate(dia, mes, ano);
	}

	/**
	 * Retorna primeiro dia do mês default.
	 * 
	 * @return primeiro dia do mês
	 */
	public  int getPrimeiroDiaDoMesDefault() {
		return 1;
	}

	/**
	 * Retorna primeiro mês do ano default.
	 * 
	 * @return primeiro mês do ano
	 */
	public  int getPrimeiroMesDefault() {
		return Calendar.JANUARY;
	}

	/**
	 * Retorna primeiro ano válido default.
	 * 
	 * @return primeiro ano válido
	 */
	public  int getPrimeiroAnoDefault() {
		return 1800;
	}

	/**
	 * Retorna inteiro que representa o mês de janeiro.
	 * 
	 * @return inteiro que representa o mês de janeiro.
	 */
	public  int getJaneiro() {
		return 1;
	}

	/**
	 * Retorna inteiro que representa o mês de fevereiro.
	 * 
	 * @return inteiro que representa o mês de fevereiro.
	 */
	public  int getFevereiro() {
		return 2;
	}

	/**
	 * Retorna inteiro que representa o mês de mar�o.
	 * 
	 * @return inteiro que representa o mês de mar�o.
	 */
	public  int getMarco() {
		return 3;
	}

	/**
	 * Retorna inteiro que representa o mês de Abril.
	 * 
	 * @return inteiro que representa o mês de Abril.
	 */
	public  int getAbril() {
		return 4;
	}

	/**
	 * Retorna inteiro que representa o mês de Maio.
	 * 
	 * @return inteiro que representa o mês de Maio.
	 */
	public  int getMaio() {
		return 5;
	}

	/**
	 * Retorna inteiro que representa o mês de Junho.
	 * 
	 * @return inteiro que representa o mês de Junho.
	 */
	public  int getJunho() {
		return 6;
	}

	/**
	 * Retorna inteiro que representa o mês de Julho.
	 * 
	 * @return inteiro que representa o mês de Julho.
	 */
	public  int getJulho() {
		return 7;
	}

	/**
	 * Retorna inteiro que representa o mês de Agosto.
	 * 
	 * @return inteiro que representa o mês de Agosto.
	 */
	public  int getAgosto() {
		return 8;
	}

	/**
	 * Retorna inteiro que representa o mês de Setembro.
	 * 
	 * @return inteiro que representa o mês de Setembro.
	 */
	public  int getSetembro() {
		return 9;
	}

	/**
	 * Retorna inteiro que representa o mês de Outubro.
	 * 
	 * @return inteiro que representa o mês de Outubro.
	 */
	public  int getOutubro() {
		return 10;
	}

	/**
	 * Retorna inteiro que representa o mês de Novembro.
	 * 
	 * @return inteiro que representa o mês de Novembro.
	 */
	public  int getNovembro() {
		return 11;
	}

	/**
	 * Retorna inteiro que representa o mês de Dezembro.
	 * 
	 * @return inteiro que representa o mês de Dezembro.
	 */
	public  int getDezembro() {
		return 12;
	}

	public String getDataAtual() {        
	    SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
	    Date dataAtual = new Date(System.currentTimeMillis());
	    String data = sd.format(dataAtual);        
	    return data;
	}
	
}
