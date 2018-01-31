package br.com.correios.ppjsiscap.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.inject.Named;




/**
 * Classe responsável pela transformação de arquivos em objetos comuns da
 * java.io.
 * 
 * @author Arivaldo Junior
 */
@Named
public final class UtilArquivo {	
	
	/**
	 * Cria um novo arquivo com permissão de leitura e escrita.
	 * @param caminhoNome
	 * @param file
	 */
	public  void novoArquivo(String caminhoNome, byte [] file){
		try {
			RandomAccessFile ras = new RandomAccessFile(caminhoNome, "rw" );
			ras.setLength(file.length);
			FileChannel channel = ras.getChannel();
			ByteBuffer wrap = ByteBuffer.wrap(file);			
			channel.write(wrap);
			channel.force(true);
			channel.close();
			ras.close();
		} catch (FileNotFoundException e) {
			UtilLog.logger.warn("Erro ao recuperar/carregar o arquivo "+caminhoNome, e);
		} catch (IOException e) {
			UtilLog.logger.warn("Erro ao recuperar/carregar o arquivo "+caminhoNome, e);	
		}
	}

	/**
	 * Retorna o InputStream do arquivo passado por parâmetro.
	 * 
	 * @param path path do arquivo.
	 * @return InputStream do arquivo passado por parâmetro
	 */
	public  InputStream getInputStream(String path) {
		InputStream is = null;

		if (!"".equals(path)) {
			is = getFileInputStream(path);
			is = getResourceAsStreamDoClassLoader(is, path);
			is = getResourceAsStream(is, path);
		}

		return is;
	}

	/**
	 * Retorna o Properties do path passado por parâmetro.
	 * 
	 * @param path path do arquivo de propriedades.
	 * @return properties.
	 */
	public  Properties getProperties(String path) {
		Properties properties = null;

		if (!"".equals(path)) {
			try {
				properties = new Properties();
				properties.load(getInputStream(path));
			} catch (IOException e) {
				UtilLog.logger.warn("Erro ao recuperar/carregar o arquivo "+path, e);					
			}
		}
		return properties;
	}
	
	/**
	 * Retorna o Properties do path passado por parametro.
	 * 
	 * @param nomeDoArquivoDeProperties path do arquivo de propriedades.
	 * @return properties.
	 */
	public  ResourceBundle getResourceBundle(String nomeDoArquivoDeProperties, Locale locale) {
		ResourceBundle resourceBundle = null;
		if (!"".equals(nomeDoArquivoDeProperties)) {
			resourceBundle = ResourceBundle.getBundle(
					nomeDoArquivoDeProperties,locale != null ? locale : Locale.getDefault(), getClassLoader());
		}		
		return resourceBundle;
	}
	

	/**
	 * Retorna o FileInputStream do path passado por parametro.
	 * 
	 * @param path Path do arquivo.
	 * @return FileInputStream do path.
	 */
	public  FileInputStream getFileInputStream(String path) {
		FileInputStream fis = null;

		if (!"".equals(path)) {
			try {
				fis = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				// Não faz nada.
			}
		}
		return fis;
	}

	/**
	 * Retorna o FileInputStream do path passado por parâmetro.
	 * 
	 * @param arquivo Arquivo.
	 * @return FileInputStream do path.
	 */
	public  FileInputStream getFileInputStream(File arquivo) {
		FileInputStream fis = null;
		if (arquivo != null) {
			try {
				fis = new FileInputStream(arquivo);
			} catch (FileNotFoundException e) {
				// Não faz nada.
			}
		}
		return fis;
	}

	/**
	 * Retorna o BufferedReader do path passado por parâmetro.
	 * 
	 * @param path Arquivo.
	 * @return BufferedReader do path.
	 */
	public  BufferedReader getBufferedReader(String path) {
		BufferedReader reader = null;

		if (!"".equals(path)) {
			InputStream is = getInputStream(path);
			if (is != null) {
				InputStreamReader isReader = new InputStreamReader(is);
				reader = new BufferedReader(isReader);
			}
		}
		return reader;
	}

	/**
	 * Retorna o BufferedReader do reader
	 * 
	 * @param reader Reader
	 * @return BufferedReader do reader
	 */
	public  BufferedReader getBufferedReader(Reader reader) {
		BufferedReader resultado = null;

		if (reader != null) {
			resultado = new BufferedReader(reader);
		}
		return resultado;
	}

	/**
	 * Retorna o BufferedReader do path passado por parâmetro.
	 * 
	 * @param arquivo Arquivo.
	 * @return BufferedReader do path.
	 */
	public  BufferedReader getBufferedReader(File arquivo) {
		BufferedReader reader = null;

		if (arquivo != null && arquivo.exists()) {
			try {
				FileReader fReader = new FileReader(arquivo);
				if (fReader != null) {
					reader = new BufferedReader(fReader);
				}
			} catch (FileNotFoundException e) {
				String path = arquivo.getAbsolutePath();
				UtilLog.logger.warn("Erro ao recuperar/carregar o arquivo "+path, e);
			}
		}
		return reader;
	}
	
	

	/**
	 * Retorna o texto do arquivo passado por parâmetro.
	 * 
	 * @param path Arquivo
	 * @return Texto do arquivo.
	 */
	public  String getTextoDoArquivo(String path) {
		String texto = "";

		if (!"".equals(path)) {
			BufferedReader br = getBufferedReader(path);
			texto = getTextoDoArquivo(br);
		}
		return texto;
	}

	/**
	 * Retorna o texto do arquivo passado por parâmetro.
	 * 
	 * @param arquivo Arquivo
	 * @return Texto do arquivo.
	 */
	public  String getTextoDoArquivo(File arquivo) {
		String texto = "";

		if (arquivo != null) {
			BufferedReader br = getBufferedReader(arquivo);
			texto = getTextoDoArquivo(br);
		}
		return texto;
	}

	/**
	 * Retorna o texto do arquivo passado por parâmetro.
	 * 
	 * @param reader StringBuffered do arquivo
	 * @return Texto do arquivo.
	 */
	public  String getTextoDoArquivo(BufferedReader reader) {
		StringBuffer buffer = new StringBuffer();

		if (reader != null) {
			String linha = null;
			try {
				while ((linha = reader.readLine()) != null) {
					buffer.append(linha).append("\r\n");
				}
			} catch (IOException e) {
				UtilLog.logger.warn("Erro ao ler o texto do reader.");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						UtilLog.logger.warn("Erro ao fechar o reader.", e);
					} finally {
						reader = null;
					}
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Retorna uma Lista de objetos do tipo File do path passado por
	 * parâmetro. O path deverá ser uma pasta do sistema.
	 * 
	 * @param pathDaPasta Path da pasta.
	 * @return coleção de objetos do tipo java.io.File
	 */
	public  List<File> getListaDeFile(String pathDaPasta) {
		List<File> res = new ArrayList<File>(1);
		if (!"".equals(pathDaPasta)) {
			File pasta = new File(pathDaPasta);
			if (pasta.isDirectory()) {
				File[] arquivos = pasta.listFiles();
				res.addAll(Arrays.asList(arquivos));
			}
		}
		return res;
	}

	/**
	 * Retorna uma coleção de objetos do tipo File do path passado por
	 * parâmetro. O path deverá ser uma pasta do sistema.
	 * 
	 * @param pathDaPasta Path da pasta.
	 * @return coleção de objetos do tipo java.io.File
	 */
	public  List<InputStream> getColecaoDeInputStream(String pathDaPasta) {
		List<InputStream> res = new ArrayList<InputStream>(1);
		List<File> listaDeFile = getListaDeFile(pathDaPasta);
		for(File f : listaDeFile){
			res.add(getFileInputStream(f));
		}		
		return res;
	}

	/**
	 * Fecha um stream.
	 * 
	 * @param stream Stream que será fechado.
	 * @return stream fechado.
	 */
	public  OutputStream fechar(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				UtilLog.logger.warn("Não foi possível fechar o stream", e);				
			} finally {
				stream = null;
			}
		}
		return stream;
	}

	/**
	 * Retorna o InputStream do path passado por parâmetro, o InputStream será
	 * recuperado do class dessa classe.
	 * 
	 * @param inputStream InputStream validado.
	 * @param path path do arquivo.
	 * @return InputStream
	 */
	private  InputStream getResourceAsStream(InputStream inputStream,
			String path) {
		if (inputStream == null) {
			inputStream = UtilArquivo.class.getResourceAsStream(path);
		}
		return inputStream;
	}

	/**
	 * Retorna InputStream do path passado por parâmetro, o InputStream será
	 * recuperado do classloader atual.
	 * 
	 * @param inputStream InputStream validado
	 * @param path path do arquivo.
	 * @return InputStream do path passado por parâmetro.
	 */
	private  InputStream getResourceAsStreamDoClassLoader(
			InputStream inputStream, String path) {
		if (inputStream == null) {
			inputStream = getClassLoader().getResourceAsStream(path);
		}
		return inputStream;
	}

	/**
	 * Retorna ClassLoader usado para recupera os arquivos.
	 * 
	 * @return ClassLoader usado para recupera os arquivos
	 */
	private  ClassLoader getClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = UtilArquivo.class.getClassLoader();
		}
		return cl;
	}

}
