package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.enums.Categoria;
import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.GrupoAtendimento;


/**
 * @author Ronald Piacenti Junior
 * 
 */

@SuppressWarnings("serial")
@Named
@ViewScoped
public class GrupoAtendimentoMB extends MB {

	@Inject
	private Usuario usuario;
	
	@Inject @OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private GrupoAtendimento grupoAtendimento = new GrupoAtendimento();
	
	private List<GrupoAtendimento> gruposDeAtendimento = new ArrayList<GrupoAtendimento>();
		
	private LazyDataModel<GrupoAtendimento> grupoAtendimentoDataModel;
	
	
	
	
	public LazyDataModel<GrupoAtendimento> novaInstancia(){
		return new LazyDataModel<GrupoAtendimento>() {
			@Override
			public List<GrupoAtendimento> load(int first, int pageSize,	String sortField, SortOrder sortOrder,	Map<String, Object> filters) {
				Paginador<GrupoAtendimento> p = new Paginador<GrupoAtendimento>();
				p.setEntityBean(new GrupoAtendimento());
				p.getEntityBean().setMcuCd(usuario.getMcucia());
				p.setPrimeiroRegistro(first);
				p.setQuantidadeDeRegistrosPorPagina(pageSize);
				p = getListaDeGruposDeAtendimentoPaginada(p);
				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
			}
		};
	}
	

	/**
	 * @return the categorias
	 */
	public Categoria []  categoriasDeGrupoAtendimeto() {
		return Categoria.values();
	}


	/**
	 * @return the grupoAtendimento
	 */
	public GrupoAtendimento getGrupoAtendimento() {
		return grupoAtendimento;
	}

	/**
	 * @param grupoAtendimento
	 *            the grupoAtendimento to set
	 */
	public void setGrupoAtendimento(GrupoAtendimento grupoAtendimento) {
		this.grupoAtendimento = grupoAtendimento;
	}

	
	public Paginador<GrupoAtendimento> getListaDeGruposDeAtendimentoPaginada(Paginador<GrupoAtendimento> p) {
		Connection connection = null;		
			
		if (p.getEntityBean().getMcuCd() != null) {
			try{
				connection = dataSource.getConnection();
				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				p =  dao.listaTodosGruposDeAtendimentoPaginado(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}			
		}
		return p;

	}
	
	
	public void adicionaGrupoAtendimento() throws CorreiosException {
		
		Connection connection = null;
		
		try{
			connection = dataSource.getConnection();
			GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
			getGrupoAtendimento().setMcuCd(usuario.getMcucia());
			int numRecord = dao.verificaSeExiste(getGrupoAtendimento());
			if(numRecord == 0){
				dao.insert(getGrupoAtendimento());
				connection.commit();
				setGrupoAtendimento(new GrupoAtendimento());
				setGrupoAtendimentoDataModel(novaInstancia());
				adicionaMensagemSucesso(null, MensagemID.INCLUSAO_SUCESSO);
				
			}else{
				adicionaMensagemInfo(null, MensagemID.REGISTRO_ENCONTRADO);
			}
			
		} catch (SQLException e) {
			throw utilCorreiosException.erro(e);
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        
	}

	public void editaDescricao(CellEditEvent event) throws CorreiosException  {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		if(newValue != null && !newValue.equals(oldValue)){
			GrupoAtendimento grupoAtendimento = getGrupoAtendimentoDataModel().getRowData();
			Connection connection = null;
			
			try {
				connection = dataSource.getConnection();
				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				dao.update(grupoAtendimento);
				connection.commit();
				
				adicionaMensagemSucesso(null, MensagemID.GRUPO_ALTERADO, grupoAtendimento.getNumGrupo()+"");
			}  catch (SQLException e) {
				throw utilCorreiosException.erro(e);
				
			}finally{
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
						//sem tratamento por aqui.
					}
				}
			}
		}

	}

	/* Exclui linhas da tabela de grupo e banco de dados */

	@SuppressWarnings("unchecked")
	public void deleteGrupoAtendimento(GrupoAtendimento grupoAtendimento) throws CorreiosException {

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
			
			int resu = dao.deleteGRP(grupoAtendimento);
			connection.commit();
			
			((List<GrupoAtendimento>)getGrupoAtendimentoDataModel().getWrappedData()).remove(grupoAtendimento);

			if (resu == 0) {
				adicionaMensagemAlerta(null, MensagemID.GRUPO_NAO_EXCLUIDO, grupoAtendimento.getNumGrupo()+"");
			} else {
				adicionaMensagemSucesso(null, MensagemID.EXCLUSAO_SUCESSO);
			}

		} catch (SQLException e) {
			throw utilCorreiosException.erro(e);
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


	/**
	 * @return the gruposDeAtendimento
	 */
	public List<GrupoAtendimento> getGruposDeAtendimento() {
		return gruposDeAtendimento;
	}


	/**
	 * @param gruposDeAtendimento the gruposDeAtendimento to set
	 */
	public void setGruposDeAtendimento(List<GrupoAtendimento> gruposDeAtendimento) {
		this.gruposDeAtendimento = gruposDeAtendimento;
	}


	/**
	 * @return the grupoAtendimentoDataModel
	 */
	public LazyDataModel<GrupoAtendimento> getGrupoAtendimentoDataModel() {
		if(grupoAtendimentoDataModel == null){
			grupoAtendimentoDataModel = novaInstancia();
		}
		return grupoAtendimentoDataModel;
	}


	/**
	 * @param grupoAtendimentoDataModel the grupoAtendimentoDataModel to set
	 */
	public void setGrupoAtendimentoDataModel(
			LazyDataModel<GrupoAtendimento> grupoAtendimentoDataModel) {
		this.grupoAtendimentoDataModel = grupoAtendimentoDataModel;
	}

	
}
